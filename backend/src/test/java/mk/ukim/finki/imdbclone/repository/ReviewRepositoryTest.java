package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.model.domain.Review;
import mk.ukim.finki.imdbclone.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    private User user1;
    private User user2;
    private Movie movie1;
    private Movie movie2;
    private Review review1;
    private Review review2;
    private Review review3;

    @BeforeEach
    void setUp() {
        // Clean up
        reviewRepository.deleteAll();
        userRepository.deleteAll();
        movieRepository.deleteAll();

        // Create test users
        user1 = new User();
        user1.setUsername("reviewer1");
        user1.setEmail("reviewer1@example.com");
        user1.setPassword("password");
        user1.setFirstName("Reviewer");
        user1.setLastName("One");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("reviewer2");
        user2.setEmail("reviewer2@example.com");
        user2.setPassword("password");
        user2.setFirstName("Reviewer");
        user2.setLastName("Two");
        user2 = userRepository.save(user2);

        // Create test movies
        movie1 = new Movie();
        movie1.setTitle("Reviewed Movie 1");
        movie1.setDescription("A great movie");
        movie1.setReleaseYear(2020);
        movie1 = movieRepository.save(movie1);

        movie2 = new Movie();
        movie2.setTitle("Reviewed Movie 2");
        movie2.setDescription("Another great movie");
        movie2.setReleaseYear(2021);
        movie2 = movieRepository.save(movie2);

        // Create test reviews
        review1 = new Review();
        review1.setUser(user1);
        review1.setMovie(movie1);
        review1.setReviewText("Excellent movie! Highly recommend it. The plot was engaging and the acting was superb.");
        review1 = reviewRepository.save(review1);

        review2 = new Review();
        review2.setUser(user2);
        review2.setMovie(movie1);
        review2.setReviewText("Good movie, but could have been better. The pacing was a bit slow in the middle.");
        review2 = reviewRepository.save(review2);

        review3 = new Review();
        review3.setUser(user1);
        review3.setMovie(movie2);
        review3.setReviewText("Amazing cinematography and soundtrack. A must-watch for any movie enthusiast.");
        review3 = reviewRepository.save(review3);
    }

    @Test
    void testFindByMovieId() {
        // Act
        List<Review> results = reviewRepository.findByMovieId(movie1.getId());

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(review -> review.getReviewText().substring(0, 10))
                .containsExactlyInAnyOrder("Excellent ", "Good movie");
    }

    @Test
    void testFindByUserId() {
        // Act
        List<Review> results = reviewRepository.findByUserId(user1.getId());

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    void testFindByUserIdAndMovieId() {
        // Act
        Optional<Review> found = reviewRepository.findByUserIdAndMovieId(user1.getId(), movie1.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getReviewText()).contains("Excellent movie");
    }

    @Test
    void testFindByUserIdAndMovieId_NotFound() {
        // Act
        Optional<Review> found = reviewRepository.findByUserIdAndMovieId(user2.getId(), movie2.getId());

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void testSaveReview() {
        // Arrange
        Review newReview = new Review();
        newReview.setUser(user2);
        newReview.setMovie(movie2);
        newReview.setReviewText("This is a new review for movie 2. It was fantastic and exceeded my expectations.");

        // Act
        Review saved = reviewRepository.save(newReview);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void testUpdateReview() {
        // Arrange
        review1.setReviewText(
                "Updated review: Even better after second viewing! The details I missed were incredible.");

        // Act
        Review updated = reviewRepository.save(review1);

        // Assert
        assertThat(updated.getReviewText()).contains("Updated review");
        assertThat(updated.getUpdatedAt()).isNotNull();
    }

    @Test
    void testDeleteReview() {
        // Act
        reviewRepository.deleteById(review1.getId());

        // Assert
        assertThat(reviewRepository.findById(review1.getId())).isEmpty();
    }

    @Test
    void testFindAll() {
        // Act
        List<Review> allReviews = reviewRepository.findAll();

        // Assert
        assertThat(allReviews).hasSize(3);
    }

    @Test
    void testFindById() {
        // Act
        Optional<Review> found = reviewRepository.findById(review1.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getReviewText()).contains("Excellent movie");
    }
}
