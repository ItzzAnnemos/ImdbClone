package mk.ukim.finki.imdbclone.config.init;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.imdbclone.model.domain.*;
import mk.ukim.finki.imdbclone.model.enumerations.Role;
import mk.ukim.finki.imdbclone.repository.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@Profile("dev")
public class DataInitializer {
    private final PasswordEncoder passwordEncoder;
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
                           MediaPersonRepository mediaPersonRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
        this.tvSeriesRepository = tvSeriesRepository;
        this.ratingRepository = ratingRepository;
        this.reviewRepository = reviewRepository;
        this.personRepository = personRepository;
        this.mediaPersonRepository = mediaPersonRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        users = new ArrayList<>();
        seedUserIfMissing("john_doe", "John", "Doe", "john@test.com");
        seedUserIfMissing("jane_smith", "Jane", "Smith", "jane@test.com");
        seedUserIfMissing("moviefan99", "Mike", "Fan", "mike@test.com");
        seedUserIfMissing("alice_w", "Alice", "Wong", "alice@test.com");
        seedUserIfMissing("carlos_m", "Carlos", "Mendez", "carlos@test.com");
        users = userRepository.findAll();

        /*
         * Legacy mock catalogue seed is intentionally disabled by default.
         *
         * The active catalogue now comes from TMDB through TmdbImportRunner. The
         * old hand-written movie, TV, person, rating, and review examples are left
         * below as reference only and can be temporarily re-enabled for debugging by
         * running the JVM with:
         *
         *   -Dimdbclone.enableLegacyMockData=true
         */
        if (!Boolean.getBoolean("imdbclone.enableLegacyMockData")) {
            genres = genreRepository.findAll();
            people = personRepository.findAll();
            movies = movieRepository.findAll();
            tvSeries = tvSeriesRepository.findAll();
            mediaPersons = mediaPersonRepository.findAll();
            ratings = ratingRepository.findAll();
            reviews = reviewRepository.findAll();
            return;
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

            Genre g6 = new Genre();
            g6.setName("Thriller");
            genres.add(g6);

            Genre g7 = new Genre();
            g7.setName("Romance");
            genres.add(g7);

            Genre g8 = new Genre();
            g8.setName("Adventure");
            genres.add(g8);

            Genre g9 = new Genre();
            g9.setName("Biography");
            genres.add(g9);

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

            Person p5 = new Person();
            p5.setFirstName("Milcho");
            p5.setLastName("Manchevski");
            p5.setBirthDate(LocalDate.of(1959, 9, 18));

            Person p6 = new Person();
            p6.setFirstName("Kiril");
            p6.setLastName("Cenevski");
            p6.setBirthDate(LocalDate.of(1943, 2, 23));

            // --- Actors & additional directors (indices 6+) ---
            Person p7 = new Person();
            p7.setFirstName("Leonardo");
            p7.setLastName("DiCaprio");
            p7.setBirthDate(LocalDate.of(1974, 11, 11));

            Person p8 = new Person();
            p8.setFirstName("Al");
            p8.setLastName("Pacino");
            p8.setBirthDate(LocalDate.of(1940, 4, 25));

            Person p9 = new Person();
            p9.setFirstName("Marlon");
            p9.setLastName("Brando");
            p9.setBirthDate(LocalDate.of(1924, 4, 3));

            Person p10 = new Person();
            p10.setFirstName("Bryan");
            p10.setLastName("Cranston");
            p10.setBirthDate(LocalDate.of(1956, 3, 7));

            Person p11 = new Person();
            p11.setFirstName("Aaron");
            p11.setLastName("Paul");
            p11.setBirthDate(LocalDate.of(1979, 8, 27));

            Person p12 = new Person();
            p12.setFirstName("Keanu");
            p12.setLastName("Reeves");
            p12.setBirthDate(LocalDate.of(1964, 9, 2));

            Person p13 = new Person();
            p13.setFirstName("Cillian");
            p13.setLastName("Murphy");
            p13.setBirthDate(LocalDate.of(1976, 5, 25));

            Person p14 = new Person();
            p14.setFirstName("Matthew");
            p14.setLastName("McConaughey");
            p14.setBirthDate(LocalDate.of(1969, 11, 4));

            Person p15 = new Person();
            p15.setFirstName("Robert");
            p15.setLastName("De Niro");
            p15.setBirthDate(LocalDate.of(1943, 8, 17));

            Person p16 = new Person();
            p16.setFirstName("Adam");
            p16.setLastName("Scott");
            p16.setBirthDate(LocalDate.of(1973, 4, 3));

            Person p17 = new Person();
            p17.setFirstName("Jeremy");
            p17.setLastName("Strong");
            p17.setBirthDate(LocalDate.of(1978, 12, 25));

            Person p18 = new Person();
            p18.setFirstName("Pedro");
            p18.setLastName("Pascal");
            p18.setBirthDate(LocalDate.of(1975, 4, 2));

            Person p19 = new Person();
            p19.setFirstName("Martin");
            p19.setLastName("Scorsese");
            p19.setBirthDate(LocalDate.of(1942, 11, 17));

            Person p20 = new Person();
            p20.setFirstName("Denis");
            p20.setLastName("Villeneuve");
            p20.setBirthDate(LocalDate.of(1967, 10, 3));

            this.personRepository.saveAll(List.of(
                    p1, p2, p3, p4, p5, p6,
                    p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20));
            people = this.personRepository.findAll();
        } else {
            people = personRepository.findAll();
        }

        movies = new ArrayList<>();
        if (this.movieRepository.count() == 0) {
            Movie movie1 = new Movie();
            movie1.setTitle("Inception");
            movie1.setDescription("A thief who steals corporate secrets through dream-sharing technology.");
            movie1.setReleaseYear(2010);
            movie1.setDuration(148);
            movie1.setGenres(new HashSet<>(List.of(genres.get(0), genres.get(1)))); // Action, Sci-Fi
            movie1.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg");

            Movie movie2 = new Movie();
            movie2.setTitle("The Godfather");
            movie2.setDescription(
                    "The aging patriarch of an organized crime dynasty transfers control to his son.");
            movie2.setReleaseYear(1972);
            movie2.setDuration(175);
            movie2.setGenres(new HashSet<>(List.of(genres.get(2), genres.get(4)))); // Drama, Crime
            movie2.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg");

            Movie movie3 = new Movie();
            movie3.setTitle("Interstellar");
            movie3.setDescription("Explorers travel through a wormhole to save humanity.");
            movie3.setReleaseYear(2014);
            movie3.setDuration(169);
            movie3.setGenres(new HashSet<>(List.of(genres.get(0), genres.get(1)))); // Action, Sci-Fi
            movie3.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg");

            Movie movie4 = new Movie();
            movie4.setTitle("Tenet");
            movie4.setDescription("A secret agent manipulates time to prevent World War III.");
            movie4.setReleaseYear(2020);
            movie4.setDuration(150);
            movie4.setGenres(new HashSet<>(List.of(genres.get(0), genres.get(1)))); // Action, Sci-Fi
            movie4.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BMzQxNzQzOTQwM15BMl5BanBnXkFtZTgwMDQ2NTcwODM@._V1_.jpg");

            Movie movie5 = new Movie();
            movie5.setTitle("The Matrix");
            movie5.setDescription("A hacker discovers the true nature of reality.");
            movie5.setReleaseYear(1999);
            movie5.setDuration(136);
            movie5.setGenres(new HashSet<>(List.of(genres.get(0), genres.get(1)))); // Action, Sci-Fi
            movie5.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg");

            Movie movie6 = new Movie();
            movie6.setTitle("Goodfellas");
            movie6.setDescription("The story of Henry Hill and his life in the mob.");
            movie6.setReleaseYear(1990);
            movie6.setDuration(146);
            movie6.setGenres(new HashSet<>(List.of(genres.get(2), genres.get(4)))); // Drama, Crime
            movie6.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BN2E5NzI2ZGMtY2VjNi00YTRjLWI1MDUtZGY5OWU1MWJjZjRjXkEyXkFqcGc@._V1_QL75_UX380_CR0");

            Movie movie7 = new Movie();
            movie7.setTitle("Scarface");
            movie7.setDescription("A Cuban immigrant rises to power in Miami's drug trade.");
            movie7.setReleaseYear(1983);
            movie7.setDuration(170);
            movie7.setGenres(new HashSet<>(List.of(genres.get(2), genres.get(4)))); // Drama, Crime
            movie7.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BNjdjNGQ4NDEtNTEwYS00MTgxLTliYzQtYzE2ZDRiZjFhZmNlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg");

            Movie movie8 = new Movie();
            movie8.setTitle("Casino");
            movie8.setDescription("Greed and deception in Las Vegas casinos.");
            movie8.setReleaseYear(1995);
            movie8.setDuration(178);
            movie8.setGenres(new HashSet<>(List.of(genres.get(2), genres.get(4)))); // Drama, Crime
            movie8.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BMDRlZWZjZjYtYzY2NS00ZWVjLTkwYzAtZTA2ZDAzMGRiYmYwXkEyXkFqcGc@._V1_QL75_UX380_CR0");

            Movie movie9 = new Movie();
            movie9.setTitle("Titanic");
            movie9.setDescription("A romance blossoms aboard the ill-fated Titanic.");
            movie9.setReleaseYear(1997);
            movie9.setDuration(195);
            movie9.setGenres(new HashSet<>(List.of(genres.get(2)))); // Drama
            movie9.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BYzYyN2FiZmUtYWYzMy00MzViLWJkZTMtOGY1ZjgzNWMwN2YxXkEyXkFqcGc@._V1_QL75_UX380_CR0");

            Movie movie10 = new Movie();
            movie10.setTitle("The Notebook");
            movie10.setDescription("A young couple falls in love in the 1940s.");
            movie10.setReleaseYear(2004);
            movie10.setDuration(123);
            movie10.setGenres(new HashSet<>(List.of(genres.get(2)))); // Drama
            movie10.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BZjE0ZjgzMzYtMTAxYi00NGMzLThmZDktNzFlMzA2MWRmYWQ0XkEyXkFqcGc@._V1_QL75_UX380_CR0");

            Movie movie11 = new Movie();
            movie11.setTitle("The Hangover");
            movie11.setDescription("Three friends wake up after a bachelor party with no memory.");
            movie11.setReleaseYear(2009);
            movie11.setDuration(100);
            movie11.setGenres(new HashSet<>(List.of(genres.get(3)))); // Comedy
            movie11.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BNDI2MzBhNzgtOWYyOS00NDM2LWE0OGYtOGQ0M2FjMTI2NTllXkEyXkFqcGc@._V1_QL75_UX380_CR0");

            Movie movie12 = new Movie();
            movie12.setTitle("Superbad");
            movie12.setDescription("Two high school friends try to enjoy their last days before college.");
            movie12.setReleaseYear(2007);
            movie12.setDuration(113);
            movie12.setGenres(new HashSet<>(List.of(genres.get(3)))); // Comedy
            movie12.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BNjk0MzdlZGEtNTRkOC00ZDRiLWJkYjAtMzUzYTRiNzk1YTViXkEyXkFqcGc@._V1_QL75_UX380_CR0");

            Movie movie13 = new Movie();
            movie13.setTitle("Blade Runner 2049");
            movie13.setDescription("A blade runner uncovers a secret that could change society.");
            movie13.setReleaseYear(2017);
            movie13.setDuration(164);
            movie13.setGenres(new HashSet<>(List.of(genres.get(1), genres.get(2)))); // Sci-Fi, Drama
            movie13.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BNzA1Njg4NzYxOV5BMl5BanBnXkFtZTgwODk5NjU3MzI@._V1_.jpg");

            Movie movie14 = new Movie();
            movie14.setTitle("Arrival");
            movie14.setDescription("A linguist tries to communicate with alien visitors.");
            movie14.setReleaseYear(2016);
            movie14.setDuration(116);
            movie14.setGenres(new HashSet<>(List.of(genres.get(1), genres.get(2)))); // Sci-Fi, Drama
            movie14.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BMTExMzU0ODcxNDheQTJeQWpwZ15BbWU4MDE1OTI4MzAy._V1_QL75_UX380_CR0");

            Movie movie15 = new Movie();
            movie15.setTitle("Пред дождот");
            movie15.setDescription(
                    "A Macedonian drama about interconnected stories set before the outbreak of war.");
            movie15.setReleaseYear(1994);
            movie15.setDuration(113);
            movie15.setGenres(new HashSet<>(List.of(genres.get(2)))); // Drama
            movie15.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BNmYwMmU4ZGQtMjA0YS00NjQ2LThlMzMtNzQ5NTEwNjA5MmJlXkEyXkFqcGc@._V1_QL75_UX380_CR0"); // Before
            // the
            // Rain

            Movie movie16 = new Movie();
            movie16.setTitle("Црно семе");
            movie16.setDescription(
                    "A Macedonian drama set during World War II, following the suffering of prisoners.");
            movie16.setReleaseYear(1971);
            movie16.setDuration(98);
            movie16.setGenres(new HashSet<>(List.of(genres.get(2)))); // Drama
            movie16.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BNzNiYjcxNjMtODlkNS00MzQxLThmNWMtMGYwNDkzMzM4OGYzXkEyXkFqcGc@._V1_QL75_UX380_CR0"); // Black
            // Seed

            Movie movie17 = new Movie();
            movie17.setTitle("The Dark Knight");
            movie17.setDescription(
                    "Batman faces the Joker, a criminal mastermind who wants to plunge Gotham City into anarchy.");
            movie17.setReleaseYear(2008);
            movie17.setDuration(152);
            // Action, Crime, Thriller
            movie17.setGenres(new HashSet<>(List.of(genres.get(0), genres.get(4), genres.get(5))));
            movie17.setPosterUrl(
                    "https://placehold.co/400x600?text=The+Dark+Knight");

            Movie movie18 = new Movie();
            movie18.setTitle("Joker");
            movie18.setDescription(
                    "A mentally troubled comedian embarks on a downward spiral that leads to the creation of an iconic villain.");
            movie18.setReleaseYear(2019);
            movie18.setDuration(122);
            // Drama, Crime, Thriller
            movie18.setGenres(new HashSet<>(List.of(genres.get(2), genres.get(4), genres.get(5))));
            movie18.setPosterUrl(
                    "https://placehold.co/400x600?text=Joker");

            Movie movie19 = new Movie();
            movie19.setTitle("Dune");
            movie19.setDescription(
                    "A noble family becomes embroiled in a war for control over the galaxy's most valuable asset.");
            movie19.setReleaseYear(2021);
            movie19.setDuration(155);
            // Sci-Fi, Adventure
            movie19.setGenres(new HashSet<>(List.of(genres.get(1), genres.get(7))));
            movie19.setPosterUrl(
                    "https://placehold.co/400x600?text=Dune");

            Movie movie20 = new Movie();
            movie20.setTitle("Forrest Gump");
            movie20.setDescription(
                    "The history of the United States from the 1950s to the '70s unfolds through the life of an Alabama man.");
            movie20.setReleaseYear(1994);
            movie20.setDuration(142);
            // Drama, Romance
            movie20.setGenres(new HashSet<>(List.of(genres.get(2), genres.get(6))));
            movie20.setPosterUrl(
                    "https://placehold.co/400x600?text=Forrest+Gump");

            this.movieRepository.saveAll(List.of(
                    movie1, movie2, movie3, movie4, movie5,
                    movie6, movie7, movie8,
                    movie9, movie10,
                    movie11, movie12,
                    movie13, movie14,
                    movie15, movie16,
                    movie17, movie18, movie19, movie20));

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
            show1.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BMzU5ZGYzNmQtMTdhYy00OGRiLTg0NmQtYjVjNzliZTg1ZGE4XkEyXkFqcGc@._V1_QL75_UX380_CR0");

            TVSeries show2 = new TVSeries();
            show2.setTitle("Severance");
            show2.setDescription(
                    "Mark leads a team of office workers whose memories have been surgically divided between their work and personal lives.");
            show2.setReleaseYear(2022);
            show2.setNumberOfSeasons(2);
            show2.setStatus("Returning Series");
            show2.setGenres(new HashSet<>(List.of(genres.get(1), genres.get(2)))); // Sci-Fi, Drama
            show2.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BZDI5YzJhODQtMzQyNy00YWNmLWIxMjUtNDBjNjA5YWRjMzExXkEyXkFqcGc@._V1_QL75_UX380_CR0"); // Try
            // this

            TVSeries show3 = new TVSeries();
            show3.setTitle("The Bear");
            show3.setDescription(
                    "A young chef from the fine dining world comes home to Chicago to run his family's sandwich shop.");
            show3.setReleaseYear(2022);
            show3.setNumberOfSeasons(3);
            show3.setStatus("Returning Series");
            show3.setGenres(new HashSet<>(List.of(genres.get(2), genres.get(3)))); // Drama, Comedy
            show3.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BYWZhNDZiMzAtZmZlYS00MWFmLWE2MWEtNDAxZTZiN2U4Y2U2XkEyXkFqcGc@._V1_QL75_UY562_CR35");

            TVSeries show4 = new TVSeries();
            show4.setTitle("Succession");
            show4.setDescription(
                    "The Roy family is known for controlling the biggest media and entertainment company in the world.");
            show4.setReleaseYear(2018);
            show4.setNumberOfSeasons(4);
            show4.setStatus("Ended");
            show4.setGenres(new HashSet<>(List.of(genres.get(2)))); // Drama
            show4.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BYTY4YTVkY2QtMjRmOS00YzliLWIxOWQtMTdkOTVkN2UzODNmXkEyXkFqcGc@._V1_QL75_UX380_CR0");

            TVSeries show5 = new TVSeries();
            show5.setTitle("The Last of Us");
            show5.setDescription(
                    "After a global pandemic destroys civilization, a hardened survivor takes charge of a 14-year-old girl.");
            show5.setReleaseYear(2023);
            show5.setNumberOfSeasons(2);
            show5.setStatus("Returning Series");
            show5.setGenres(new HashSet<>(List.of(genres.get(0), genres.get(2)))); // Action, Drama
            show5.setPosterUrl(
                    "https://m.media-amazon.com/images/M/MV5BYWI3ODJlMzktY2U5NC00ZjdlLWE1MGItNWQxZDk3NWNjN2RhXkEyXkFqcGc@._V1_QL75_UX380_CR0");

            TVSeries show6 = new TVSeries();
            show6.setTitle("Stranger Things");
            show6.setDescription(
                    "When a young boy vanishes, a small town uncovers a mystery involving secret experiments and supernatural forces.");
            show6.setReleaseYear(2016);
            show6.setNumberOfSeasons(4);
            show6.setStatus("Returning Series");
            // Sci-Fi, Drama, Thriller
            show6.setGenres(new HashSet<>(List.of(genres.get(1), genres.get(2), genres.get(5))));
            show6.setPosterUrl(
                    "https://placehold.co/400x600?text=Stranger+Things");

            TVSeries show7 = new TVSeries();
            show7.setTitle("The Office");
            show7.setDescription(
                    "A mockumentary on a group of typical office workers, where the workday consists of ego clashes and inappropriate behavior.");
            show7.setReleaseYear(2005);
            show7.setNumberOfSeasons(9);
            show7.setStatus("Ended");
            show7.setGenres(new HashSet<>(List.of(genres.get(3)))); // Comedy
            show7.setPosterUrl(
                    "https://placehold.co/400x600?text=The+Office");

            this.tvSeriesRepository.saveAll(List.of(show1, show2, show3, show4, show5, show6, show7));
            tvSeries = this.tvSeriesRepository.findAll();
        } else {
            tvSeries = tvSeriesRepository.findAll();
        }

        mediaPersons = new ArrayList<>();
        if (this.mediaPersonRepository.count() == 0) {
            MediaPerson mp1 = new MediaPerson();
            mp1.setMedia(movies.get(0)); // Inception
            mp1.setPerson(people.get(0)); // Christopher Nolan
            mp1.setRole(Role.DIRECTOR);

            MediaPerson mp2 = new MediaPerson();
            mp2.setMedia(movies.get(1)); // The Godfather
            mp2.setPerson(people.get(1)); // Francis Ford Coppola
            mp2.setRole(Role.DIRECTOR);

            MediaPerson mp3 = new MediaPerson();
            mp3.setMedia(tvSeries.get(0)); // Breaking Bad
            mp3.setPerson(people.get(2)); // Vince Gilligan
            mp3.setRole(Role.CREATOR);

            MediaPerson mp4 = new MediaPerson();
            mp4.setMedia(tvSeries.get(1)); // Severance
            mp4.setPerson(people.get(3)); // Dan Erickson
            mp4.setRole(Role.CREATOR);

            MediaPerson mp5 = new MediaPerson();
            mp5.setMedia(movies.get(2)); // Interstellar
            mp5.setPerson(people.get(0)); // Christopher Nolan
            mp5.setRole(Role.DIRECTOR);

            MediaPerson mp6 = new MediaPerson();
            mp6.setMedia(movies.get(3)); // Tenet
            mp6.setPerson(people.get(0)); // Christopher Nolan
            mp6.setRole(Role.DIRECTOR);

            MediaPerson mp7 = new MediaPerson();
            mp7.setMedia(movies.get(14)); // Пред дождот
            mp7.setPerson(people.get(4)); // Milcho Manchevski
            mp7.setRole(Role.DIRECTOR);

            MediaPerson mp8 = new MediaPerson();
            mp8.setMedia(movies.get(15)); // Црно семе
            mp8.setPerson(people.get(5)); // Kiril Cenevski
            mp8.setRole(Role.DIRECTOR);

            // ----- Cast (main actors) and extra directors -----
            MediaPerson mp9 = new MediaPerson();
            mp9.setMedia(movies.get(0)); // Inception
            mp9.setPerson(people.get(6)); // Leonardo DiCaprio
            mp9.setRole(Role.MAIN_ACTOR);
            mp9.setCharacterName("Dom Cobb");

            MediaPerson mp10 = new MediaPerson();
            mp10.setMedia(movies.get(0)); // Inception
            mp10.setPerson(people.get(12)); // Cillian Murphy
            mp10.setRole(Role.ACTOR);
            mp10.setCharacterName("Robert Fischer");

            MediaPerson mp11 = new MediaPerson();
            mp11.setMedia(movies.get(1)); // The Godfather
            mp11.setPerson(people.get(8)); // Marlon Brando
            mp11.setRole(Role.MAIN_ACTOR);
            mp11.setCharacterName("Vito Corleone");

            MediaPerson mp12 = new MediaPerson();
            mp12.setMedia(movies.get(1)); // The Godfather
            mp12.setPerson(people.get(7)); // Al Pacino
            mp12.setRole(Role.MAIN_ACTOR);
            mp12.setCharacterName("Michael Corleone");

            MediaPerson mp13 = new MediaPerson();
            mp13.setMedia(movies.get(2)); // Interstellar
            mp13.setPerson(people.get(13)); // Matthew McConaughey
            mp13.setRole(Role.MAIN_ACTOR);
            mp13.setCharacterName("Cooper");

            MediaPerson mp14 = new MediaPerson();
            mp14.setMedia(movies.get(4)); // The Matrix
            mp14.setPerson(people.get(11)); // Keanu Reeves
            mp14.setRole(Role.MAIN_ACTOR);
            mp14.setCharacterName("Neo");

            MediaPerson mp15 = new MediaPerson();
            mp15.setMedia(movies.get(5)); // Goodfellas
            mp15.setPerson(people.get(14)); // Robert De Niro
            mp15.setRole(Role.MAIN_ACTOR);
            mp15.setCharacterName("Jimmy Conway");

            MediaPerson mp16 = new MediaPerson();
            mp16.setMedia(movies.get(5)); // Goodfellas
            mp16.setPerson(people.get(18)); // Martin Scorsese
            mp16.setRole(Role.DIRECTOR);

            MediaPerson mp17 = new MediaPerson();
            mp17.setMedia(movies.get(6)); // Scarface
            mp17.setPerson(people.get(7)); // Al Pacino
            mp17.setRole(Role.MAIN_ACTOR);
            mp17.setCharacterName("Tony Montana");

            MediaPerson mp18 = new MediaPerson();
            mp18.setMedia(movies.get(7)); // Casino
            mp18.setPerson(people.get(14)); // Robert De Niro
            mp18.setRole(Role.MAIN_ACTOR);
            mp18.setCharacterName("Ace Rothstein");

            MediaPerson mp19 = new MediaPerson();
            mp19.setMedia(movies.get(7)); // Casino
            mp19.setPerson(people.get(18)); // Martin Scorsese
            mp19.setRole(Role.DIRECTOR);

            MediaPerson mp20 = new MediaPerson();
            mp20.setMedia(movies.get(8)); // Titanic
            mp20.setPerson(people.get(6)); // Leonardo DiCaprio
            mp20.setRole(Role.MAIN_ACTOR);
            mp20.setCharacterName("Jack Dawson");

            MediaPerson mp21 = new MediaPerson();
            mp21.setMedia(movies.get(12)); // Blade Runner 2049
            mp21.setPerson(people.get(19)); // Denis Villeneuve
            mp21.setRole(Role.DIRECTOR);

            MediaPerson mp22 = new MediaPerson();
            mp22.setMedia(movies.get(13)); // Arrival
            mp22.setPerson(people.get(19)); // Denis Villeneuve
            mp22.setRole(Role.DIRECTOR);

            MediaPerson mp23 = new MediaPerson();
            mp23.setMedia(movies.get(16)); // The Dark Knight
            mp23.setPerson(people.get(0)); // Christopher Nolan
            mp23.setRole(Role.DIRECTOR);

            MediaPerson mp24 = new MediaPerson();
            mp24.setMedia(movies.get(16)); // The Dark Knight
            mp24.setPerson(people.get(12)); // Cillian Murphy
            mp24.setRole(Role.ACTOR);
            mp24.setCharacterName("Jonathan Crane");

            MediaPerson mp25 = new MediaPerson();
            mp25.setMedia(movies.get(18)); // Dune
            mp25.setPerson(people.get(19)); // Denis Villeneuve
            mp25.setRole(Role.DIRECTOR);

            MediaPerson mp26 = new MediaPerson();
            mp26.setMedia(tvSeries.get(0)); // Breaking Bad
            mp26.setPerson(people.get(9)); // Bryan Cranston
            mp26.setRole(Role.MAIN_ACTOR);
            mp26.setCharacterName("Walter White");

            MediaPerson mp27 = new MediaPerson();
            mp27.setMedia(tvSeries.get(0)); // Breaking Bad
            mp27.setPerson(people.get(10)); // Aaron Paul
            mp27.setRole(Role.MAIN_ACTOR);
            mp27.setCharacterName("Jesse Pinkman");

            MediaPerson mp28 = new MediaPerson();
            mp28.setMedia(tvSeries.get(1)); // Severance
            mp28.setPerson(people.get(15)); // Adam Scott
            mp28.setRole(Role.MAIN_ACTOR);
            mp28.setCharacterName("Mark Scout");

            MediaPerson mp29 = new MediaPerson();
            mp29.setMedia(tvSeries.get(3)); // Succession
            mp29.setPerson(people.get(16)); // Jeremy Strong
            mp29.setRole(Role.MAIN_ACTOR);
            mp29.setCharacterName("Kendall Roy");

            MediaPerson mp30 = new MediaPerson();
            mp30.setMedia(tvSeries.get(4)); // The Last of Us
            mp30.setPerson(people.get(17)); // Pedro Pascal
            mp30.setRole(Role.MAIN_ACTOR);
            mp30.setCharacterName("Joel Miller");

            this.mediaPersonRepository.saveAll(List.of(
                    mp1, mp2, mp3, mp4, mp5, mp6, mp7, mp8,
                    mp9, mp10, mp11, mp12, mp13, mp14, mp15, mp16, mp17, mp18, mp19, mp20,
                    mp21, mp22, mp23, mp24, mp25, mp26, mp27, mp28, mp29, mp30));
            mediaPersons = mediaPersonRepository.findAll();
        } else {
            mediaPersons = mediaPersonRepository.findAll();
        }

        ratings = new ArrayList<>();
        if (this.ratingRepository.count() == 0) {
            // Rating matrix. Each row is {userIndex, mediaSelector, score}.
            // mediaSelector >= 0  -> movies.get(selector)
            // mediaSelector <  0  -> tvSeries.get(-selector - 1)
            //   (-1 = Breaking Bad, -2 = Severance, -3 = The Bear, -4 = Succession,
            //    -5 = The Last of Us, -6 = Stranger Things, -7 = The Office)
            int[][] ratingData = {
                    // john_doe (0): Nolan, sci-fi and a crime classic
                    {0, 0, 10}, {0, 2, 9}, {0, 3, 8}, {0, 4, 9}, {0, 16, 10}, {0, -1, 9}, {0, 14, 10},
                    // jane_smith (1): crime, drama and prestige TV
                    {1, 0, 8}, {1, 1, 9}, {1, 5, 9}, {1, 6, 8}, {1, 7, 8}, {1, -1, 10}, {1, -4, 9}, {1, 14, 9},
                    // moviefan99 (2): mainstream, romance and comedy
                    {2, 1, 10}, {2, 8, 9}, {2, 19, 10}, {2, 9, 8}, {2, -7, 9}, {2, -3, 8}, {2, 15, 8},
                    // alice_w (3): science fiction and thrillers
                    {3, 4, 10}, {3, 12, 9}, {3, 13, 9}, {3, 18, 9}, {3, -2, 9}, {3, -6, 8}, {3, 2, 8}, {3, 0, 9},
                    // carlos_m (4): crime films and prestige TV
                    {4, 5, 10}, {4, 7, 9}, {4, 6, 9}, {4, 1, 9}, {4, -1, 10}, {4, -4, 8}, {4, 17, 8}
            };

            List<Rating> ratingList = new ArrayList<>();
            for (int[] row : ratingData) {
                Rating rating = new Rating();
                rating.setUser(users.get(row[0]));
                Media media = row[1] >= 0 ? movies.get(row[1]) : tvSeries.get(-row[1] - 1);
                rating.setMedia(media);
                rating.setRating(row[2]);
                ratingList.add(rating);
            }

            this.ratingRepository.saveAll(ratingList);
            ratings = ratingRepository.findAll();

            // Compute each title's average directly from the seeded ratings so the
            // stored averageRating always matches the data (rounded to 1 decimal).
            // Titles with no ratings keep a null average ("no ratings yet").
            for (Movie movie : movies) {
                Double avg = ratingRepository.findAverageRatingByMediaId(movie.getId());
                movie.setAverageRating(avg == null ? null : Math.round(avg * 10.0) / 10.0);
            }
            movieRepository.saveAll(movies);

            for (TVSeries series : tvSeries) {
                Double avg = ratingRepository.findAverageRatingByMediaId(series.getId());
                series.setAverageRating(avg == null ? null : Math.round(avg * 10.0) / 10.0);
            }
            tvSeriesRepository.saveAll(tvSeries);
        } else {
            ratings = ratingRepository.findAll();
        }

        reviews = new ArrayList<>();
        if (this.reviewRepository.count() == 0) {
            Review rev1 = new Review();
            rev1.setUser(users.get(0));
            rev1.setMedia(movies.get(0));
            rev1.setReviewText(
                    "Absolutely mind-blowing from start to finish. The practical effects are insane.");

            Review rev2 = new Review();
            rev2.setUser(users.get(1));
            rev2.setMedia(movies.get(0));
            rev2.setReviewText(
                    "Great concept but the exposition dumps were a bit heavy in the first half.");

            Review rev3 = new Review();
            rev3.setUser(users.get(2));
            rev3.setMedia(movies.get(1));
            rev3.setReviewText("The greatest movie ever made. Brando's performance is legendary.");

            Review rev4 = new Review();
            rev4.setUser(users.get(0));
            rev4.setMedia(tvSeries.get(0));
            rev4.setReviewText(
                    "The character development of Walter White is unparalleled in television history.");

            Review rev5 = new Review();
            rev5.setUser(users.get(0));
            rev5.setMedia(movies.get(14)); // Пред дождот
            rev5.setReviewText(
                    "One of the most important Macedonian films, visually powerful and emotionally deep.");

            Review rev6 = new Review();
            rev6.setUser(users.get(3)); // alice_w
            rev6.setMedia(movies.get(4)); // The Matrix
            rev6.setReviewText(
                    "A genre-defining sci-fi masterpiece. The action still holds up decades later.");

            Review rev7 = new Review();
            rev7.setUser(users.get(3)); // alice_w
            rev7.setMedia(movies.get(18)); // Dune
            rev7.setReviewText(
                    "Stunning visuals and sound design. Villeneuve really understood the source material.");

            Review rev8 = new Review();
            rev8.setUser(users.get(4)); // carlos_m
            rev8.setMedia(movies.get(5)); // Goodfellas
            rev8.setReviewText(
                    "Scorsese at his peak. Every scene is quotable and the pacing is relentless.");

            Review rev9 = new Review();
            rev9.setUser(users.get(4)); // carlos_m
            rev9.setMedia(tvSeries.get(0)); // Breaking Bad
            rev9.setReviewText(
                    "The best paid-off character arc on television. Cranston is unforgettable.");

            Review rev10 = new Review();
            rev10.setUser(users.get(1)); // jane_smith
            rev10.setMedia(tvSeries.get(3)); // Succession
            rev10.setReviewText(
                    "Sharp writing and brutal family politics. Hard to root for anyone, impossible to look away.");

            Review rev11 = new Review();
            rev11.setUser(users.get(2)); // moviefan99
            rev11.setMedia(movies.get(19)); // Forrest Gump
            rev11.setReviewText(
                    "Charming and heartfelt. A feel-good classic I can rewatch any time.");

            Review rev12 = new Review();
            rev12.setUser(users.get(0)); // john_doe
            rev12.setMedia(movies.get(16)); // The Dark Knight
            rev12.setReviewText(
                    "The definitive comic-book film. Ledger's performance elevates the whole movie.");

            this.reviewRepository.saveAll(List.of(
                    rev1, rev2, rev3, rev4, rev5,
                    rev6, rev7, rev8, rev9, rev10, rev11, rev12));
            reviews = reviewRepository.findAll();
        } else {
            reviews = reviewRepository.findAll();
        }
    }

    private void seedUserIfMissing(String username, String firstName, String lastName, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userRepository.save(user);
    }
}
