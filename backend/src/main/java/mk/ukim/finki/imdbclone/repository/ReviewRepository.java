package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Find all reviews for a specific movie
     * 
     * @param movieId the movie ID
     * @return List of reviews for the movie
     */
    @Query("SELECT r FROM Review r WHERE r.movie.id = :movieId")
    List<Review> findByMovieId(@Param("movieId") Long movieId);

    /**
     * Find all reviews by a specific user
     * 
     * @param userId the user ID
     * @return List of reviews by the user
     */
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId")
    List<Review> findByUserId(@Param("userId") Long userId);

    /**
     * Find a review by user ID and movie ID
     * 
     * @param userId  the user ID
     * @param movieId the movie ID
     * @return Optional containing the review if found
     */
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.movie.id = :movieId")
    Optional<Review> findByUserIdAndMovieId(@Param("userId") Long userId, @Param("movieId") Long movieId);
}
