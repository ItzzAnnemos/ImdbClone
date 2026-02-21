package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Rating;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Rating operations.
 * Handles upsert of ratings and keeps {@code Media.averageRating} in sync.
 */
public interface RatingService {

    /**
     * Create or update a rating for a media item by a user.
     * After saving, {@code Media.averageRating} is recalculated and persisted.
     *
     * @param userId  the ID of the user rating the media
     * @param mediaId the ID of the media being rated
     * @param rating  the rating value (1–10)
     * @return the saved or updated Rating
     */
    Rating rateMedia(Long userId, Long mediaId, Integer rating);

    /**
     * Delete a user's rating for a media item.
     * After deletion, {@code Media.averageRating} is recalculated and persisted.
     *
     * @param userId  the user's ID
     * @param mediaId the media's ID
     */
    void deleteRating(Long userId, Long mediaId);

    /**
     * Find a specific user's rating for a media item
     *
     * @param userId  the user's ID
     * @param mediaId the media's ID
     * @return Optional containing the rating if found
     */
    Optional<Rating> getRating(Long userId, Long mediaId);

    /**
     * Find all ratings for a specific media item
     *
     * @param mediaId the media's ID
     * @return List of ratings
     */
    List<Rating> getRatingsByMedia(Long mediaId);

    /**
     * Find all ratings submitted by a specific user
     *
     * @param userId the user's ID
     * @return List of ratings
     */
    List<Rating> getRatingsByUser(Long userId);

    /**
     * Calculate the current average rating for a media item
     *
     * @param mediaId the media's ID
     * @return the average rating, or null if no ratings exist
     */
    Double getAverageRating(Long mediaId);

    /**
     * Count the total number of ratings for a media item
     *
     * @param mediaId the media's ID
     * @return the number of ratings
     */
    Long getRatingCount(Long mediaId);
}
