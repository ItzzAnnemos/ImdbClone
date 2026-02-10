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
     * Find a rating by user ID and movie ID
     * 
     * @param userId  the user ID
     * @param movieId the movie ID
     * @return Optional containing the rating if found
     */
    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId AND r.movie.id = :movieId")
    Optional<Rating> findByUserIdAndMovieId(@Param("userId") Long userId, @Param("movieId") Long movieId);

    /**
     * Find all ratings by a specific user
     * 
     * @param userId the user ID
     * @return List of ratings by the user
     */
    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId")
    List<Rating> findByUserId(@Param("userId") Long userId);

    /**
     * Find all ratings for a specific movie
     * 
     * @param movieId the movie ID
     * @return List of ratings for the movie
     */
    @Query("SELECT r FROM Rating r WHERE r.movie.id = :movieId")
    List<Rating> findByMovieId(@Param("movieId") Long movieId);

    /**
     * Calculate the average rating for a specific movie
     * 
     * @param movieId the movie ID
     * @return The average rating, or null if no ratings exist
     */
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.movie.id = :movieId")
    Double findAverageRatingByMovieId(@Param("movieId") Long movieId);

    /**
     * Count the number of ratings for a specific movie
     * 
     * @param movieId the movie ID
     * @return The count of ratings
     */
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.movie.id = :movieId")
    Long countByMovieId(@Param("movieId") Long movieId);
}
