package mk.ukim.finki.imdbclone.service.tmdb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.config.tmdb.TmdbProperties;
import mk.ukim.finki.imdbclone.model.domain.Genre;
import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbCastMember;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbCrewMember;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbGenre;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbGenreListResponse;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMovieDetails;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMoviePage;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMovieSummary;
import mk.ukim.finki.imdbclone.model.enumerations.Role;
import mk.ukim.finki.imdbclone.repository.GenreRepository;
import mk.ukim.finki.imdbclone.repository.MediaPersonRepository;
import mk.ukim.finki.imdbclone.repository.MovieRepository;
import mk.ukim.finki.imdbclone.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Imports popular movies from TMDB into the local database, mapping TMDB's
 * payloads onto the existing {@link Movie}, {@link Genre}, {@link Person} and
 * {@link MediaPerson} entities. The local database stays the source of truth;
 * this only fills it with more content.
 *
 * <p>Imports are de-duplicated by (title, release year), so running it again only
 * adds movies that are not already present. {@code averageRating} is intentionally
 * left null on imported movies so that it is driven by your own users' ratings.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TmdbImportService {

    private final TmdbClient tmdbClient;
    private final TmdbProperties properties;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final PersonRepository personRepository;
    private final MediaPersonRepository mediaPersonRepository;

    /**
     * Imports up to {@code tmdb.pages} pages of popular movies.
     *
     * @return the number of new movies actually added
     */
    public int importPopularMovies() {
        Map<Long, Genre> genresByTmdbId = importGenres();

        // Track titles already present so re-runs don't create duplicates.
        Set<String> existingKeys = new HashSet<>();
        movieRepository.findAll().forEach(m -> existingKeys.add(movieKey(m.getTitle(), m.getReleaseYear())));

        // Cache people within this run to avoid repeated lookups/inserts.
        Map<String, Person> personCache = new HashMap<>();

        int imported = 0;
        for (int page = 1; page <= properties.getPages(); page++) {
            TmdbMoviePage moviePage = tmdbClient.getPopularMovies(page);
            if (moviePage == null || moviePage.results() == null) {
                continue;
            }
            for (TmdbMovieSummary summary : moviePage.results()) {
                if (summary == null || summary.id() == null) {
                    continue;
                }
                try {
                    TmdbMovieDetails details = tmdbClient.getMovieDetails(summary.id());
                    if (importMovie(details, genresByTmdbId, existingKeys, personCache)) {
                        imported++;
                    }
                } catch (Exception ex) {
                    log.warn("Skipping TMDB movie id {} due to error: {}", summary.id(), ex.getMessage());
                }
            }
        }
        return imported;
    }

    /** Upserts the TMDB genre list and returns a map from TMDB genre id to entity. */
    private Map<Long, Genre> importGenres() {
        Map<Long, Genre> map = new HashMap<>();
        TmdbGenreListResponse response = tmdbClient.getMovieGenres();
        if (response == null || response.genres() == null) {
            return map;
        }
        for (TmdbGenre tmdbGenre : response.genres()) {
            if (tmdbGenre.name() == null || tmdbGenre.name().isBlank()) {
                continue;
            }
            Genre genre = genreRepository.findByName(tmdbGenre.name())
                    .orElseGet(() -> {
                        Genre g = new Genre();
                        g.setName(tmdbGenre.name());
                        return genreRepository.save(g);
                    });
            map.put(tmdbGenre.id(), genre);
        }
        return map;
    }

    private boolean importMovie(
            TmdbMovieDetails details,
            Map<Long, Genre> genresByTmdbId,
            Set<String> existingKeys,
            Map<String, Person> personCache
    ) {
        if (details == null || details.title() == null || details.title().isBlank()) {
            return false;
        }

        Integer year = parseYear(details.releaseDate());
        if (year == null) {
            return false; // skip titles with no usable release year
        }

        String key = movieKey(details.title(), year);
        if (existingKeys.contains(key)) {
            return false;
        }

        Movie movie = new Movie();
        movie.setTitle(truncate(details.title(), 255));
        movie.setDescription(truncate(details.overview(), 1000));
        movie.setReleaseYear(year);
        if (details.runtime() != null && details.runtime() > 0) {
            movie.setDuration(details.runtime());
        }
        if (details.posterPath() != null && !details.posterPath().isBlank()) {
            movie.setPosterUrl(properties.getImageBaseUrl() + details.posterPath());
        }
        movie.setGenres(resolveGenres(details.genres(), genresByTmdbId));

        Movie saved = movieRepository.save(movie);
        existingKeys.add(key);

        importCredits(saved, details, personCache);
        return true;
    }

    private Set<Genre> resolveGenres(List<TmdbGenre> tmdbGenres, Map<Long, Genre> genresByTmdbId) {
        Set<Genre> result = new HashSet<>();
        if (tmdbGenres == null) {
            return result;
        }
        for (TmdbGenre tmdbGenre : tmdbGenres) {
            Genre genre = genresByTmdbId.get(tmdbGenre.id());
            if (genre == null && tmdbGenre.name() != null && !tmdbGenre.name().isBlank()) {
                genre = genreRepository.findByName(tmdbGenre.name())
                        .orElseGet(() -> {
                            Genre g = new Genre();
                            g.setName(tmdbGenre.name());
                            return genreRepository.save(g);
                        });
                genresByTmdbId.put(tmdbGenre.id(), genre);
            }
            if (genre != null) {
                result.add(genre);
            }
        }
        return result;
    }

    private void importCredits(Movie movie, TmdbMovieDetails details, Map<String, Person> personCache) {
        if (details.credits() == null) {
            return;
        }

        // (personId|role) pairs already linked to this movie, to respect the
        // unique constraint on media_persons.
        Set<String> linked = new HashSet<>();

        // Top-billed cast: first is the lead, the rest are supporting actors.
        if (details.credits().cast() != null) {
            List<TmdbCastMember> cast = new ArrayList<>(details.credits().cast());
            cast.sort(Comparator.comparingInt(TmdbCastMember::order));
            int count = 0;
            for (TmdbCastMember member : cast) {
                if (count >= properties.getMaxCast()) {
                    break;
                }
                Person person = getOrCreatePerson(member.name(), member.profilePath(), personCache);
                if (person == null) {
                    continue;
                }
                Role role = (count == 0) ? Role.MAIN_ACTOR : Role.ACTOR;
                if (linkCredit(movie, person, role, member.character(), linked)) {
                    count++;
                }
            }
        }

        // Directors from the crew list.
        if (details.credits().crew() != null) {
            for (TmdbCrewMember member : details.credits().crew()) {
                if (!"Director".equalsIgnoreCase(member.job())) {
                    continue;
                }
                Person person = getOrCreatePerson(member.name(), member.profilePath(), personCache);
                if (person == null) {
                    continue;
                }
                linkCredit(movie, person, Role.DIRECTOR, null, linked);
            }
        }
    }

    private boolean linkCredit(Movie movie, Person person, Role role, String character, Set<String> linked) {
        String pairKey = person.getId() + "|" + role.name();
        if (linked.contains(pairKey)) {
            return false;
        }
        MediaPerson mp = new MediaPerson();
        mp.setMedia(movie);
        mp.setPerson(person);
        mp.setRole(role);
        if (character != null && !character.isBlank()) {
            mp.setCharacterName(character);
        }
        mediaPersonRepository.save(mp);
        linked.add(pairKey);
        return true;
    }

    private Person getOrCreatePerson(String fullName, String profilePath, Map<String, Person> cache) {
        if (fullName == null || fullName.isBlank()) {
            return null;
        }
        String trimmed = fullName.trim();
        int spaceIdx = trimmed.indexOf(' ');
        String firstName = spaceIdx < 0 ? trimmed : trimmed.substring(0, spaceIdx);
        String lastName = spaceIdx < 0 ? trimmed : trimmed.substring(spaceIdx + 1).trim();

        final String finalLastName = lastName.isBlank() ? firstName : lastName;

        String cacheKey = (firstName + "|" + finalLastName).toLowerCase(Locale.ROOT);

        Person cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        Optional<Person> existing =
                personRepository.findByFirstNameAndLastName(firstName, finalLastName);

        Person person = existing.orElseGet(() -> {
            Person p = new Person();
            p.setFirstName(firstName);
            p.setLastName(finalLastName);

            if (profilePath != null && !profilePath.isBlank()) {
                p.setProfilePictureUrl(properties.getImageBaseUrl() + profilePath);
            }
            return personRepository.save(p);
        });

        cache.put(cacheKey, person);
        return person;
    }

    private Integer parseYear(String releaseDate) {
        if (releaseDate == null || releaseDate.length() < 4) {
            return null;
        }
        try {
            return Integer.parseInt(releaseDate.substring(0, 4));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String movieKey(String title, Integer year) {
        return (title == null ? "" : title.trim().toLowerCase(Locale.ROOT)) + "|" + year;
    }

    /** Truncates a string to {@code max} characters to fit database column limits. */
    private String truncate(String value, int max) {
        if (value == null) {
            return null;
        }
        return value.length() <= max ? value : value.substring(0, max);
    }
}
