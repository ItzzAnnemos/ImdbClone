package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Media;

import java.util.List;
import java.util.Optional;

/**
 * Generic base service interface for all Media subtypes (Movie, TVSeries,
 * etc.).
 * Provides common CRUD and discovery operations typed to the concrete media
 * type T.
 *
 * @param <T> the concrete Media subtype
 */
public interface MediaService<T extends Media> {

    /**
     * Retrieve all media items of type T
     *
     * @return List of all entities
     */
    List<T> getAll();

    /**
     * Find a media item by its ID
     *
     * @param id the entity ID
     * @return Optional containing the entity if found
     */
    Optional<T> getById(Long id);

    /**
     * Persist a new media item
     *
     * @param entity the entity to create
     * @return the saved entity with generated ID
     */
    T create(T entity);

    /**
     * Update an existing media item
     *
     * @param id            the ID of the entity to update
     * @param entityDetails the updated field values
     * @return the updated entity
     */
    T update(Long id, T entityDetails);

    /**
     * Delete a media item by its ID
     *
     * @param id the entity ID
     */
    void delete(Long id);

    /**
     * Search for media by title (case-insensitive partial match)
     *
     * @param title the search term
     * @return List of matching media items
     */
    List<T> search(String title);

    /**
     * Retrieve the top 10 highest-rated media items of type T
     *
     * @return List of top-rated entities
     */
    List<T> getTopRated();

    /**
     * Retrieve the 10 most recently added media items of type T
     *
     * @return List of the newest entities
     */
    List<T> getRecent();
}
