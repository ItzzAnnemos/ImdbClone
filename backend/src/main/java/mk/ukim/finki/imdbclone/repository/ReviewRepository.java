package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Find all reviews for a specific media item
     *
     * @param mediaId the media ID
     * @return List of reviews for the media item
     */
    List<Review> findByMedia_Id(Long mediaId);

    /**
     * Find all reviews by a specific user
     *
     * @param userId the user ID
     * @return List of reviews by the user
     */
    List<Review> findByUser_Id(Long userId);

    /**
     * Find a review by user ID and media ID
     *
     * @param userId  the user ID
     * @param mediaId the media ID
     * @return Optional containing the review if found
     */
    Optional<Review> findByUser_IdAndMedia_Id(Long userId, Long mediaId);
}
