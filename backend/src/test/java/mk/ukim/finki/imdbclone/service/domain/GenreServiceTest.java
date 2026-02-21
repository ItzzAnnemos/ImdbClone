package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Genre;
import mk.ukim.finki.imdbclone.repository.GenreRepository;
import mk.ukim.finki.imdbclone.service.domain.impl.GenreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class GenreServiceTest {

    @Autowired
    private GenreRepository genreRepository;

    private GenreService genreService;

    @BeforeEach
    void setUp() {
        genreService = new GenreServiceImpl(genreRepository);
        genreRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveGenre() {
        // Create
        Genre action = genreService.createGenre("Action");
        assertThat(action.getId()).isNotNull();
        assertThat(action.getName()).isEqualTo("Action");

        // Retrieve by ID
        Optional<Genre> foundById = genreService.getGenreById(action.getId());
        assertThat(foundById).isPresent();
        assertThat(foundById.get().getName()).isEqualTo("Action");

        // Retrieve by Name
        Optional<Genre> foundByName = genreService.getGenreByName("Action");
        assertThat(foundByName).isPresent();

        // Exists
        assertThat(genreService.genreExists("Action")).isTrue();
    }

    @Test
    void shouldThrowWhenCreatingDuplicateGenre() {
        genreService.createGenre("Comedy");

        assertThatThrownBy(() -> genreService.createGenre("Comedy"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Genre already exists: Comedy");
    }

    @Test
    void shouldGetAllGenres() {
        genreService.createGenre("Action");
        genreService.createGenre("Comedy");

        List<Genre> allGenres = genreService.getAllGenres();
        assertThat(allGenres).hasSize(2);
    }

    @Test
    void shouldDeleteGenre() {
        Genre genre = genreService.createGenre("Drama");
        Long id = genre.getId();

        genreService.deleteGenre(id);

        assertThat(genreService.getGenreById(id)).isEmpty();
    }

    @Test
    void shouldThrowWhenDeletingNonExistentGenre() {
        assertThatThrownBy(() -> genreService.deleteGenre(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Genre not found with id: 999");
    }
}
