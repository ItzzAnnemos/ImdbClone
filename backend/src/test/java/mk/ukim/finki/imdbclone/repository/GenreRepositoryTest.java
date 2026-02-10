package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private Genre actionGenre;
    private Genre dramaGenre;

    @BeforeEach
    void setUp() {
        // Clean up
        genreRepository.deleteAll();

        // Create test genres
        actionGenre = new Genre();
        actionGenre.setName("Action");
        actionGenre = genreRepository.save(actionGenre);

        dramaGenre = new Genre();
        dramaGenre.setName("Drama");
        dramaGenre = genreRepository.save(dramaGenre);
    }

    @Test
    void testFindByName() {
        // Act
        Optional<Genre> found = genreRepository.findByName("Action");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(actionGenre.getId());
    }

    @Test
    void testFindByName_NotFound() {
        // Act
        Optional<Genre> found = genreRepository.findByName("Comedy");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void testExistsByName() {
        // Act & Assert
        assertThat(genreRepository.existsByName("Drama")).isTrue();
        assertThat(genreRepository.existsByName("Comedy")).isFalse();
    }

    @Test
    void testUniqueConstraint_Name() {
        // Arrange
        Genre duplicateGenre = new Genre();
        duplicateGenre.setName("Action"); // Duplicate name

        // Act & Assert
        assertThatThrownBy(() -> {
            genreRepository.save(duplicateGenre);
            genreRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveGenre() {
        // Arrange
        Genre newGenre = new Genre();
        newGenre.setName("Comedy");

        // Act
        Genre saved = genreRepository.save(newGenre);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Comedy");
    }

    @Test
    void testDeleteGenre() {
        // Act
        genreRepository.deleteById(actionGenre.getId());

        // Assert
        assertThat(genreRepository.findById(actionGenre.getId())).isEmpty();
    }

    @Test
    void testFindAll() {
        // Act
        var allGenres = genreRepository.findAll();

        // Assert
        assertThat(allGenres).hasSize(2);
        assertThat(allGenres).extracting(Genre::getName)
                .containsExactlyInAnyOrder("Action", "Drama");
    }

    @Test
    void testUpdateGenre() {
        // Arrange
        actionGenre.setName("Action/Adventure");

        // Act
        Genre updated = genreRepository.save(actionGenre);

        // Assert
        assertThat(updated.getName()).isEqualTo("Action/Adventure");
    }
}
