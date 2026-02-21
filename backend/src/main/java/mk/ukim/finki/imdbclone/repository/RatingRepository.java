package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    /**
     * Find a rating by user ID and media ID
     *
     * @param userId  the user ID
     * @param mediaId the media ID
     * @return Optional containing the rating if found
     */
    Optional<Rating> findByUser_IdAndMedia_Id(Long userId, Long mediaId);

    /**
     * Find all ratings by a specific user
     *
     * @param userId the user ID
     * @return List of ratings by the user
     */
    List<Rating> findByUser_Id(Long userId);

    /**
     * Find all ratings for a specific media item
     *
     * @param mediaId the media ID
     * @return List of ratings for the media item
     */
    List<Rating> findByMedia_Id(Long mediaId);

    /**
     * Calculate the average rating for a specific media item.
     * Requires a custom query since AVG aggregation cannot be expressed
     * as a Spring Data derived query method.
     *
     * @param mediaId the media ID
     * @return The average rating, or null if no ratings exist
     */
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.media.id = :mediaId")
    Double findAverageRatingByMediaId(@Param("mediaId") Long mediaId);

    /**
     * Count the number of ratings for a specific media item
     *
     * @param mediaId the media ID
     * @return The count of ratings
     */
    Long countByMedia_Id(Long mediaId);
}
