package mk.ukim.finki.imdbclone.service.tmdb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.config.tmdb.TmdbProperties;
import mk.ukim.finki.imdbclone.model.domain.Genre;
import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbCastMember;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbCreatedBy;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbCrewMember;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbGenre;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbGenreListResponse;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMovieDetails;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMoviePage;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMovieSummary;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbTVDetails;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbTVPage;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbTVSummary;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbVideo;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbVideos;
import mk.ukim.finki.imdbclone.model.enumerations.Role;
import mk.ukim.finki.imdbclone.repository.GenreRepository;
import mk.ukim.finki.imdbclone.repository.MediaPersonRepository;
import mk.ukim.finki.imdbclone.repository.MovieRepository;
import mk.ukim.finki.imdbclone.repository.PersonRepository;
import mk.ukim.finki.imdbclone.repository.TVSeriesRepository;
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
 * Imports movies and TV series from TMDB into the local database, mapping
 * TMDB's payloads onto the existing {@link Movie}, {@link TVSeries},
 * {@link Genre}, {@link Person} and {@link MediaPerson} entities. The local
 * database stays the source of truth; this only fills it with more content.
 *
 * <p>Imports are de-duplicated by (title, release year), so running it again only
 * adds movies that are not already present. Imported movies start with TMDB's
 * vote average so discovery pages have useful rankings before local users rate
 * them.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TmdbImportService {

    private final TmdbClient tmdbClient;
    private final TmdbProperties properties;
    private final MovieRepository movieRepository;
    private final TVSeriesRepository tvSeriesRepository;
    private final GenreRepository genreRepository;
    private final PersonRepository personRepository;
    private final MediaPersonRepository mediaPersonRepository;

    /**
     * Imports up to {@code tmdb.maxMovies} movies across the configured TMDB
     * discovery sort orders.
     *
     * @return the number of new movies actually added
     */
    public int importMovies() {
        Map<Long, Genre> genresByTmdbId = importGenres(tmdbClient.getMovieGenres());

        // Track titles already present so re-runs don't create duplicates.
        Map<String, Movie> existingMoviesByKey = new HashMap<>();
        movieRepository.findAll().forEach(m ->
                existingMoviesByKey.put(mediaKey(m.getTitle(), m.getReleaseYear()), m)
        );

        // Cache people within this run to avoid repeated lookups/inserts.
        Map<String, Person> personCache = new HashMap<>();

        int imported = 0;
        int maxMovies = Math.max(1, properties.getMaxMovies());
        int pagesPerSort = Math.max(1, properties.getPages());
        int minimumVoteCount = Math.max(0, properties.getMinimumVoteCount());

        List<String> discoverySorts = properties.getDiscoverySorts();
        if (discoverySorts == null || discoverySorts.isEmpty()) {
            discoverySorts = List.of("popularity.desc");
        }

        for (String sortBy : discoverySorts) {
            if (sortBy == null || sortBy.isBlank()) {
                continue;
            }
            for (int page = 1; page <= pagesPerSort && imported < maxMovies; page++) {
                TmdbMoviePage moviePage = tmdbClient.discoverMovies(sortBy, page, minimumVoteCount);
                if (moviePage == null || moviePage.results() == null) {
                    continue;
                }
                if (moviePage.totalPages() > 0 && page > moviePage.totalPages()) {
                    break;
                }
                for (TmdbMovieSummary summary : moviePage.results()) {
                    if (summary == null || summary.id() == null || imported >= maxMovies) {
                        continue;
                    }
                    try {
                        TmdbMovieDetails details = tmdbClient.getMovieDetails(summary.id());
                        if (importMovie(details, genresByTmdbId, existingMoviesByKey, personCache)) {
                            imported++;
                        }
                    } catch (Exception ex) {
                        log.warn("Skipping TMDB movie id {} due to error: {}", summary.id(), ex.getMessage());
                    }
                }
            }
        }
        return imported;
    }

    /**
     * Imports up to {@code tmdb.maxTVSeries} TV series across the configured
     * TMDB discovery sort orders.
     *
     * @return the number of new TV series actually added
     */
    public int importTVSeries() {
        Map<Long, Genre> genresByTmdbId = importGenres(tmdbClient.getTVGenres());

        Map<String, TVSeries> existingTVSeriesByKey = new HashMap<>();
        tvSeriesRepository.findAll().forEach(series ->
                existingTVSeriesByKey.put(mediaKey(series.getTitle(), series.getReleaseYear()), series)
        );

        Map<String, Person> personCache = new HashMap<>();

        int imported = 0;
        int maxTVSeries = Math.max(1, properties.getMaxTVSeries());
        int pagesPerSort = Math.max(1, properties.getPages());
        int minimumVoteCount = Math.max(0, properties.getMinimumVoteCount());

        List<String> tvDiscoverySorts = properties.getTvDiscoverySorts();
        if (tvDiscoverySorts == null || tvDiscoverySorts.isEmpty()) {
            tvDiscoverySorts = List.of("popularity.desc");
        }

        for (String sortBy : tvDiscoverySorts) {
            if (sortBy == null || sortBy.isBlank()) {
                continue;
            }
            for (int page = 1; page <= pagesPerSort && imported < maxTVSeries; page++) {
                TmdbTVPage tvPage = tmdbClient.discoverTVSeries(sortBy, page, minimumVoteCount);
                if (tvPage == null || tvPage.results() == null) {
                    continue;
                }
                if (tvPage.totalPages() > 0 && page > tvPage.totalPages()) {
                    break;
                }
                for (TmdbTVSummary summary : tvPage.results()) {
                    if (summary == null || summary.id() == null || imported >= maxTVSeries) {
                        continue;
                    }
                    try {
                        TmdbTVDetails details = tmdbClient.getTVSeriesDetails(summary.id());
                        if (importTVSeries(details, genresByTmdbId, existingTVSeriesByKey, personCache)) {
                            imported++;
                        }
                    } catch (Exception ex) {
                        log.warn("Skipping TMDB TV series id {} due to error: {}", summary.id(), ex.getMessage());
                    }
                }
            }
        }
        return imported;
    }

    /** Upserts the TMDB genre list and returns a map from TMDB genre id to entity. */
    private Map<Long, Genre> importGenres(TmdbGenreListResponse response) {
        Map<Long, Genre> map = new HashMap<>();
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
            Map<String, Movie> existingMoviesByKey,
            Map<String, Person> personCache
    ) {
        if (details == null || details.title() == null || details.title().isBlank()) {
            return false;
        }

        Integer year = parseYear(details.releaseDate());
        if (year == null) {
            return false; // skip titles with no usable release year
        }

        String key = mediaKey(details.title(), year);
        Movie existingMovie = existingMoviesByKey.get(key);
        if (existingMovie != null) {
            updateExistingMovie(existingMovie, details);
            return false;
        }

        Movie movie = new Movie();
        movie.setTitle(truncate(details.title(), 255));
        movie.setDescription(truncate(details.overview(), 1000));
        movie.setReleaseYear(year);
        if (details.runtime() != null && details.runtime() > 0) {
            movie.setDuration(details.runtime());
        }
        if (details.voteAverage() != null && details.voteAverage() > 0) {
            movie.setAverageRating(details.voteAverage());
        }
        if (details.posterPath() != null && !details.posterPath().isBlank()) {
            movie.setPosterUrl(properties.getImageBaseUrl() + details.posterPath());
        }
        movie.setTrailerUrl(resolveTrailerUrl(details.videos()));
        movie.setGenres(resolveGenres(details.genres(), genresByTmdbId));

        Movie saved = movieRepository.save(movie);
        existingMoviesByKey.put(key, saved);

        importCredits(saved, details, personCache);
        return true;
    }

    private void updateExistingMovie(Movie movie, TmdbMovieDetails details) {
        boolean changed = false;
        String trailerUrl = resolveTrailerUrl(details.videos());

        if ((movie.getTrailerUrl() == null || movie.getTrailerUrl().isBlank()) && trailerUrl != null) {
            movie.setTrailerUrl(trailerUrl);
            changed = true;
        }
        if ((movie.getPosterUrl() == null || movie.getPosterUrl().isBlank())
                && details.posterPath() != null && !details.posterPath().isBlank()) {
            movie.setPosterUrl(properties.getImageBaseUrl() + details.posterPath());
            changed = true;
        }
        if (movie.getAverageRating() == null && details.voteAverage() != null && details.voteAverage() > 0) {
            movie.setAverageRating(details.voteAverage());
            changed = true;
        }

        if (changed) {
            movieRepository.save(movie);
        }
    }

    private boolean importTVSeries(
            TmdbTVDetails details,
            Map<Long, Genre> genresByTmdbId,
            Map<String, TVSeries> existingTVSeriesByKey,
            Map<String, Person> personCache
    ) {
        if (details == null || details.name() == null || details.name().isBlank()) {
            return false;
        }

        Integer year = parseYear(details.firstAirDate());
        if (year == null) {
            return false;
        }

        String key = mediaKey(details.name(), year);
        TVSeries existingTVSeries = existingTVSeriesByKey.get(key);
        if (existingTVSeries != null) {
            updateExistingTVSeries(existingTVSeries, details);
            return false;
        }

        TVSeries tvSeries = new TVSeries();
        tvSeries.setTitle(truncate(details.name(), 255));
        tvSeries.setDescription(truncate(details.overview(), 1000));
        tvSeries.setReleaseYear(year);
        tvSeries.setNumberOfSeasons(details.numberOfSeasons());
        tvSeries.setStatus(truncate(details.status(), 255));
        if (details.voteAverage() != null && details.voteAverage() > 0) {
            tvSeries.setAverageRating(details.voteAverage());
        }
        if (details.posterPath() != null && !details.posterPath().isBlank()) {
            tvSeries.setPosterUrl(properties.getImageBaseUrl() + details.posterPath());
        }
        tvSeries.setTrailerUrl(resolveTrailerUrl(details.videos()));
        tvSeries.setGenres(resolveGenres(details.genres(), genresByTmdbId));

        TVSeries saved = tvSeriesRepository.save(tvSeries);
        existingTVSeriesByKey.put(key, saved);

        importCredits(saved, details, personCache);
        return true;
    }

    private void updateExistingTVSeries(TVSeries tvSeries, TmdbTVDetails details) {
        boolean changed = false;
        String trailerUrl = resolveTrailerUrl(details.videos());

        if ((tvSeries.getTrailerUrl() == null || tvSeries.getTrailerUrl().isBlank()) && trailerUrl != null) {
            tvSeries.setTrailerUrl(trailerUrl);
            changed = true;
        }
        if ((tvSeries.getPosterUrl() == null || tvSeries.getPosterUrl().isBlank())
                && details.posterPath() != null && !details.posterPath().isBlank()) {
            tvSeries.setPosterUrl(properties.getImageBaseUrl() + details.posterPath());
            changed = true;
        }
        if (tvSeries.getAverageRating() == null && details.voteAverage() != null && details.voteAverage() > 0) {
            tvSeries.setAverageRating(details.voteAverage());
            changed = true;
        }
        if (tvSeries.getNumberOfSeasons() == null && details.numberOfSeasons() != null) {
            tvSeries.setNumberOfSeasons(details.numberOfSeasons());
            changed = true;
        }
        if ((tvSeries.getStatus() == null || tvSeries.getStatus().isBlank())
                && details.status() != null && !details.status().isBlank()) {
            tvSeries.setStatus(truncate(details.status(), 255));
            changed = true;
        }

        if (changed) {
            tvSeriesRepository.save(tvSeries);
        }
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

    private void importCredits(TVSeries tvSeries, TmdbTVDetails details, Map<String, Person> personCache) {
        Set<String> linked = new HashSet<>();

        if (details.createdBy() != null) {
            for (TmdbCreatedBy creator : details.createdBy()) {
                Person person = getOrCreatePerson(creator.name(), creator.profilePath(), personCache);
                if (person == null) {
                    continue;
                }
                linkCredit(tvSeries, person, Role.CREATOR, null, linked);
            }
        }

        if (details.credits() == null) {
            return;
        }

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
                if (linkCredit(tvSeries, person, role, member.character(), linked)) {
                    count++;
                }
            }
        }
    }

    private boolean linkCredit(Media media, Person person, Role role, String character, Set<String> linked) {
        String pairKey = person.getId() + "|" + role.name();
        if (linked.contains(pairKey)) {
            return false;
        }
        MediaPerson mp = new MediaPerson();
        mp.setMedia(media);
        mp.setPerson(person);
        mp.setRole(role);
        if (character != null && !character.isBlank()) {
            mp.setCharacterName(truncate(character, 255));
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

    private String resolveTrailerUrl(TmdbVideos videos) {
        if (videos == null || videos.results() == null) {
            return null;
        }

        return videos.results()
                .stream()
                .filter(video -> video != null && video.key() != null && !video.key().isBlank())
                .filter(video -> "YouTube".equalsIgnoreCase(video.site()))
                .sorted(Comparator
                        .comparing((TmdbVideo video) -> !"Trailer".equalsIgnoreCase(video.type()))
                        .thenComparing(video -> !Boolean.TRUE.equals(video.official()))
                        .thenComparing(video -> video.name() == null ? "" : video.name()))
                .map(video -> "https://www.youtube.com/embed/" + video.key())
                .findFirst()
                .orElse(null);
    }

    private String mediaKey(String title, Integer year) {
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
