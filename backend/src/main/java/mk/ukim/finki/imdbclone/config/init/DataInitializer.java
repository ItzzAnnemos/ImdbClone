package mk.ukim.finki.imdbclone.config.init;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.imdbclone.model.domain.*;
import mk.ukim.finki.imdbclone.model.enumerations.Role;
import mk.ukim.finki.imdbclone.repository.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer {
    public static List<User> users;
    public static List<Genre> genres;
    public static List<Movie> movies;
    public static List<TVSeries> tvSeries;
    public static List<Rating> ratings;
    public static List<Review> reviews;
    public static List<Person> people;
    public static List<MediaPerson> mediaPersons;

    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final TVSeriesRepository tvSeriesRepository;
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;
    private final PersonRepository personRepository;
    private final MediaPersonRepository mediaPersonRepository;

    public DataInitializer(UserRepository userRepository,
            GenreRepository genreRepository,
            MovieRepository movieRepository,
            TVSeriesRepository tvSeriesRepository,
            RatingRepository ratingRepository,
            ReviewRepository reviewRepository,
            PersonRepository personRepository,
            MediaPersonRepository mediaPersonRepository) {
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
        this.tvSeriesRepository = tvSeriesRepository;
        this.ratingRepository = ratingRepository;
        this.reviewRepository = reviewRepository;
        this.personRepository = personRepository;
        this.mediaPersonRepository = mediaPersonRepository;
    }

    @PostConstruct
    public void init() {
        users = new ArrayList<>();
        if (this.userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("john_doe");
            user1.setPassword("password123");
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setEmail("john@test.com");
            User user2 = new User();
            user2.setUsername("jane_smith");
            user2.setPassword("password123");
            user2.setFirstName("Jane");
            user2.setLastName("Smith");
            user2.setEmail("jane@test.com");
            User user3 = new User();
            user3.setUsername("moviefan99");
            user3.setPassword("password123");
            user3.setFirstName("Mike");
            user3.setLastName("Fan");
            user3.setEmail("mike@test.com");
            this.userRepository.saveAll(List.of(user1, user2, user3));
            users = this.userRepository.findAll();
        } else {
            users = userRepository.findAll();
        }

        genres = new ArrayList<>();
        if (this.genreRepository.count() == 0) {
            Genre g1 = new Genre();
            g1.setName("Action");
            genres.add(g1);
            Genre g2 = new Genre();
            g2.setName("Sci-Fi");
            genres.add(g2);
            Genre g3 = new Genre();
            g3.setName("Drama");
            genres.add(g3);
            Genre g4 = new Genre();
            g4.setName("Comedy");
            genres.add(g4);
            Genre g5 = new Genre();
            g5.setName("Crime");
            genres.add(g5);
            this.genreRepository.saveAll(genres);
        } else {
            genres = genreRepository.findAll();
        }

        people = new ArrayList<>();
        if (this.personRepository.count() == 0) {
            Person p1 = new Person();
            p1.setFirstName("Christopher");
            p1.setLastName("Nolan");
            p1.setBirthDate(LocalDate.of(1970, 7, 30));
            Person p2 = new Person();
            p2.setFirstName("Francis");
            p2.setLastName("Ford Coppola");
            p2.setBirthDate(LocalDate.of(1939, 4, 7));
            Person p3 = new Person();
            p3.setFirstName("Vince");
            p3.setLastName("Gilligan");
            p3.setBirthDate(LocalDate.of(1967, 2, 10));
            Person p4 = new Person();
            p4.setFirstName("Dan");
            p4.setLastName("Erickson");

            this.personRepository.saveAll(List.of(p1, p2, p3, p4));
            people = this.personRepository.findAll();
        } else {
            people = personRepository.findAll();
        }

        movies = new ArrayList<>();
        if (this.movieRepository.count() == 0) {
            Movie movie1 = new Movie();
            movie1.setTitle("Inception");
            movie1.setDescription("A thief who steals corporate secrets through the use of dream-sharing technology.");
            movie1.setReleaseYear(2010);
            movie1.setDuration(148);
            movie1.setGenres(new HashSet<>(List.of(genres.get(0), genres.get(1)))); // Action, Sci-Fi

            Movie movie2 = new Movie();
            movie2.setTitle("The Godfather");
            movie2.setDescription(
                    "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.");
            movie2.setReleaseYear(1972);
            movie2.setDuration(175);
            movie2.setGenres(new HashSet<>(List.of(genres.get(2), genres.get(4)))); // Drama, Crime

            this.movieRepository.saveAll(List.of(movie1, movie2));
            movies = this.movieRepository.findAll();
        } else {
            movies = movieRepository.findAll();
        }

        tvSeries = new ArrayList<>();
        if (this.tvSeriesRepository.count() == 0) {
            TVSeries show1 = new TVSeries();
            show1.setTitle("Breaking Bad");
            show1.setDescription(
                    "A high school chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine.");
            show1.setReleaseYear(2008);
            show1.setNumberOfSeasons(5);
            show1.setStatus("Ended");
            show1.setGenres(new HashSet<>(List.of(genres.get(2), genres.get(4)))); // Drama, Crime

            TVSeries show2 = new TVSeries();
            show2.setTitle("Severance");
            show2.setDescription(
                    "Mark leads a team of office workers whose memories have been surgically divided between their work and personal lives.");
            show2.setReleaseYear(2022);
            show2.setNumberOfSeasons(2);
            show2.setStatus("Returning Series");
            show2.setGenres(new HashSet<>(List.of(genres.get(1), genres.get(2)))); // Sci-Fi, Drama

            this.tvSeriesRepository.saveAll(List.of(show1, show2));
            tvSeries = this.tvSeriesRepository.findAll();
        } else {
            tvSeries = tvSeriesRepository.findAll();
        }

        mediaPersons = new ArrayList<>();
        if (this.mediaPersonRepository.count() == 0) {
            MediaPerson mp1 = new MediaPerson();
            mp1.setMedia(movies.get(0));
            mp1.setPerson(people.get(0));
            mp1.setRole(Role.DIRECTOR); // Nolan -> Inception
            MediaPerson mp2 = new MediaPerson();
            mp2.setMedia(movies.get(1));
            mp2.setPerson(people.get(1));
            mp2.setRole(Role.DIRECTOR); // Coppola -> Godfather
            MediaPerson mp3 = new MediaPerson();
            mp3.setMedia(tvSeries.get(0));
            mp3.setPerson(people.get(2));
            mp3.setRole(Role.CREATOR); // Gilligan -> Breaking Bad
            MediaPerson mp4 = new MediaPerson();
            mp4.setMedia(tvSeries.get(1));
            mp4.setPerson(people.get(3));
            mp4.setRole(Role.CREATOR); // Erickson -> Severance

            this.mediaPersonRepository.saveAll(List.of(mp1, mp2, mp3, mp4));
            mediaPersons = mediaPersonRepository.findAll();
        } else {
            mediaPersons = mediaPersonRepository.findAll();
        }

        ratings = new ArrayList<>();
        if (this.ratingRepository.count() == 0) {
            Rating r1 = new Rating();
            r1.setUser(users.get(0));
            r1.setMedia(movies.get(0));
            r1.setRating(10);
            Rating r2 = new Rating();
            r2.setUser(users.get(1));
            r2.setMedia(movies.get(0));
            r2.setRating(8);
            Rating r3 = new Rating();
            r3.setUser(users.get(2));
            r3.setMedia(movies.get(1));
            r3.setRating(10);
            Rating r4 = new Rating();
            r4.setUser(users.get(0));
            r4.setMedia(tvSeries.get(0));
            r4.setRating(9);
            Rating r5 = new Rating();
            r5.setUser(users.get(1));
            r5.setMedia(tvSeries.get(1));
            r5.setRating(8);

            this.ratingRepository.saveAll(List.of(r1, r2, r3, r4, r5));
            ratings = ratingRepository.findAll();

            // Sync average ratings manually since we're bypassing rating service
            movies.get(0).setAverageRating(9.0); // (10+8)/2
            movies.get(1).setAverageRating(10.0);
            movieRepository.saveAll(movies);

            tvSeries.get(0).setAverageRating(9.0);
            tvSeries.get(1).setAverageRating(8.0);
            tvSeriesRepository.saveAll(tvSeries);
        } else {
            ratings = ratingRepository.findAll();
        }

        reviews = new ArrayList<>();
        if (this.reviewRepository.count() == 0) {
            Review rev1 = new Review();
            rev1.setUser(users.get(0));
            rev1.setMedia(movies.get(0));
            rev1.setReviewText("Absolutely mind-blowing from start to finish. The practical effects are insane.");

            Review rev2 = new Review();
            rev2.setUser(users.get(1));
            rev2.setMedia(movies.get(0));
            rev2.setReviewText("Great concept but the exposition dumps were a bit heavy in the first half.");

            Review rev3 = new Review();
            rev3.setUser(users.get(2));
            rev3.setMedia(movies.get(1));
            rev3.setReviewText("The greatest movie ever made. Brando's performance is legendary.");

            Review rev4 = new Review();
            rev4.setUser(users.get(0));
            rev4.setMedia(tvSeries.get(0));
            rev4.setReviewText("The character development of Walter White is unparalleled in television history.");

            this.reviewRepository.saveAll(List.of(rev1, rev2, rev3, rev4));
            reviews = reviewRepository.findAll();
        } else {
            reviews = reviewRepository.findAll();
        }
    }
}
