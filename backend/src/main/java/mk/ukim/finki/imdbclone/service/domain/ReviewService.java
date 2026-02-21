package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Review;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Review operations.
 * A user may only submit one review per media item (enforced by DB unique
 * constraint
 * and duplicate check in the implementation).
 */
public interface ReviewService {

    /**
     * Create a new review for a media item.
     * Throws if the user has already reviewed this media.
     *
     * @param userId     the ID of the reviewing user
     * @param mediaId    the ID of the media being reviewed
     * @param reviewText the review content (10–5000 characters)
     * @return the saved Review
     */
    Review createReview(Long userId, Long mediaId, String reviewText);

    /**
     * Update the text of an existing review
     *
     * @param reviewId the ID of the review to update
     * @param newText  the new review content
     * @return the updated Review
     */
    Review updateReview(Long reviewId, String newText);

    /**
     * Delete a review by its ID
     *
     * @param reviewId the ID of the review to delete
     */
    void deleteReview(Long reviewId);

    /**
     * Find a user's review for a specific media item
     *
     * @param userId  the user's ID
     * @param mediaId the media's ID
     * @return Optional containing the review if found
     */
    Optional<Review> getReview(Long userId, Long mediaId);

    /**
     * Find a review by its ID
     *
     * @param reviewId the review ID
     * @return the review
     */
    Review getReviewById(Long reviewId);

    /**
     * Find all reviews for a specific media item
     *
     * @param mediaId the media's ID
     * @return List of reviews
     */
    List<Review> getReviewsByMedia(Long mediaId);

    /**
     * Find all reviews written by a specific user
     *
     * @param userId the user's ID
     * @return List of reviews
     */
    List<Review> getReviewsByUser(Long userId);
}
