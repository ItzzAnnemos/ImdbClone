package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.repository.MovieRepository;
import mk.ukim.finki.imdbclone.service.domain.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MovieServiceTest {

    @Autowired
    private MovieRepository movieRepository;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieServiceImpl(movieRepository);
        movieRepository.deleteAll();
    }

    private Movie createMovie(String title, Integer year) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setReleaseYear(year);
        movie.setDescription("Test desc");
        return movie;
    }

    @Test
    void shouldFilterMoviesByYear() {
        movieService.createMovie(createMovie("Inception", 2010));
        movieService.createMovie(createMovie("Interstellar", 2014));
        movieService.createMovie(createMovie("Avatar", 2009));

        List<Movie> year2010 = movieService.getByYear(2010);
        assertThat(year2010).hasSize(1);
        assertThat(year2010.get(0).getTitle()).isEqualTo("Inception");

        List<Movie> range = movieService.getByYearRange(2009, 2010);
        assertThat(range).hasSize(2);
    }

    @Test
    void shouldUpdateMovie() {
        Movie movie = movieService.createMovie(createMovie("Old Title", 2010));

        Movie updateDetails = new Movie();
        updateDetails.setTitle("New Title");
        updateDetails.setReleaseYear(2010);

        Movie updated = movieService.updateMovie(movie.getId(), updateDetails);

        assertThat(updated.getTitle()).isEqualTo("New Title");
        assertThat(movieService.getMovieById(movie.getId()).get().getTitle()).isEqualTo("New Title");
    }

    @Test
    void shouldDeleteMovie() {
        Movie movie = movieService.createMovie(createMovie("To Delete", 2010));

        movieService.deleteMovie(movie.getId());

        assertThat(movieService.getMovieById(movie.getId())).isEmpty();
    }
}
