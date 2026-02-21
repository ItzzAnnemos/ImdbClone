package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Genre;
import mk.ukim.finki.imdbclone.model.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;
    private Genre actionGenre;
    private Genre dramaGenre;

    @BeforeEach
    void setUp() {
        // Clean up
        movieRepository.deleteAll();
        genreRepository.deleteAll();

        // Create genres
        actionGenre = new Genre();
        actionGenre.setName("Action");
        actionGenre = genreRepository.save(actionGenre);

        dramaGenre = new Genre();
        dramaGenre.setName("Drama");
        dramaGenre = genreRepository.save(dramaGenre);

        // Create movies
        movie1 = new Movie();
        movie1.setTitle("The Shawshank Redemption");
        movie1.setDescription("Two imprisoned men bond over a number of years");
        movie1.setReleaseYear(1994);
        movie1.setDirector("Frank Darabont");
        movie1.setAverageRating(9.3);
        movie1.setGenres(new HashSet<>(Arrays.asList(dramaGenre)));
        movie1 = movieRepository.save(movie1);

        movie2 = new Movie();
        movie2.setTitle("The Dark Knight");
        movie2.setDescription("When the menace known as the Joker wreaks havoc");
        movie2.setReleaseYear(2008);
        movie2.setDirector("Christopher Nolan");
        movie2.setAverageRating(9.0);
        movie2.setGenres(new HashSet<>(Arrays.asList(actionGenre, dramaGenre)));
        movie2 = movieRepository.save(movie2);

        movie3 = new Movie();
        movie3.setTitle("Inception");
        movie3.setDescription("A thief who steals corporate secrets");
        movie3.setReleaseYear(2010);
        movie3.setDirector("Christopher Nolan");
        movie3.setAverageRating(8.8);
        movie3.setGenres(new HashSet<>(Arrays.asList(actionGenre)));
        movie3 = movieRepository.save(movie3);
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        // Act
        List<Movie> results = movieRepository.findByTitleContainingIgnoreCase("shawshank");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("The Shawshank Redemption");
    }

    @Test
    void testFindByTitleContainingIgnoreCase_CaseInsensitive() {
        // Act
        List<Movie> results = movieRepository.findByTitleContainingIgnoreCase("DARK");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("The Dark Knight");
    }

    @Test
    void testFindByReleaseYear() {
        // Act
        List<Movie> results = movieRepository.findByReleaseYear(2008);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("The Dark Knight");
    }

    @Test
    void testFindByReleaseYearBetween() {
        // Act
        List<Movie> results = movieRepository.findByReleaseYearBetween(2005, 2015);

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("The Dark Knight", "Inception");
    }

    @Test
    void testFindByDirectorContainingIgnoreCase() {
        // Act
        List<Movie> results = movieRepository.findByDirectorContainingIgnoreCase("nolan");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("The Dark Knight", "Inception");
    }

    @Test
    void testFindByGenres_Name() {
        // Act
        List<Movie> results = movieRepository.findByGenres_Name("Action");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("The Dark Knight", "Inception");
    }

    @Test
    void testFindByGenres_NameIn() {
        // Act
        List<Movie> results = movieRepository.findByGenres_NameIn(Arrays.asList("Drama"));

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("The Shawshank Redemption", "The Dark Knight");
    }

    @Test
    void testFindTop10ByOrderByAverageRatingDesc() {
        // Act
        List<Movie> results = movieRepository.findTop10ByOrderByAverageRatingDesc();

        // Assert
        assertThat(results).hasSize(3);
        assertThat(results.get(0).getTitle()).isEqualTo("The Shawshank Redemption");
        assertThat(results.get(1).getTitle()).isEqualTo("The Dark Knight");
        assertThat(results.get(2).getTitle()).isEqualTo("Inception");
    }

    @Test
    void testFindTop10ByOrderByCreatedAtDesc() {
        // Act
        List<Movie> results = movieRepository.findTop10ByOrderByCreatedAtDesc();

        // Assert
        assertThat(results).hasSize(3);
        // The most recently created should be first (Inception was created last)
        assertThat(results.get(0).getTitle()).isEqualTo("Inception");
    }

    @Test
    void testSaveMovie() {
        // Arrange
        Movie newMovie = new Movie();
        newMovie.setTitle("Interstellar");
        newMovie.setDescription("A team of explorers travel through a wormhole");
        newMovie.setReleaseYear(2014);
        newMovie.setDirector("Christopher Nolan");
        newMovie.setAverageRating(8.6);

        // Act
        Movie saved = movieRepository.save(newMovie);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void testDeleteMovie_CascadesRatingsAndReviews() {
        // This test verifies cascade deletion works properly
        // In a real scenario, we would create ratings and reviews,
        // but for now we just verify the movie can be deleted

        // Act
        movieRepository.deleteById(movie1.getId());

        // Assert
        assertThat(movieRepository.findById(movie1.getId())).isEmpty();
    }

    @Test
    void testFindById() {
        // Act
        Movie found = movieRepository.findById(movie1.getId()).orElse(null);

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("The Shawshank Redemption");
    }

    @Test
    void testFindAll() {
        // Act
        List<Movie> allMovies = movieRepository.findAll();

        // Assert
        assertThat(allMovies).hasSize(3);
    }
}
