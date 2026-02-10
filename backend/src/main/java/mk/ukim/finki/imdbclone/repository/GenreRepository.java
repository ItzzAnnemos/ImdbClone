package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    /**
     * Find a genre by its name
     * 
     * @param name the genre name to search for
     * @return Optional containing the genre if found
     */
    Optional<Genre> findByName(String name);

    /**
     * Check if a genre name already exists
     * 
     * @param name the genre name to check
     * @return true if genre exists, false otherwise
     */
    boolean existsByName(String name);
}
