package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Genre;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Genre management operations.
 */
public interface GenreService {

    /**
     * Retrieve all genres
     *
     * @return List of all genres
     */
    List<Genre> getAllGenres();

    /**
     * Find a genre by its ID
     *
     * @param id the genre ID
     * @return Optional containing the genre if found
     */
    Optional<Genre> getGenreById(Long id);

    /**
     * Find a genre by its name
     *
     * @param name the genre name
     * @return Optional containing the genre if found
     */
    Optional<Genre> getGenreByName(String name);

    /**
     * Create a new genre with the given name
     *
     * @param name the genre name
     * @return the created genre
     */
    Genre createGenre(String name);

    /**
     * Delete a genre by its ID
     *
     * @param id the genre ID
     */
    void deleteGenre(Long id);

    /**
     * Check whether a genre with the given name already exists
     *
     * @param name the genre name
     * @return true if the genre exists
     */
    boolean genreExists(String name);
}
