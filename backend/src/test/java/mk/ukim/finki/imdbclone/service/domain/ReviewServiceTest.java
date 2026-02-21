package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.model.domain.Review;
import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.repository.MediaRepository;
import mk.ukim.finki.imdbclone.repository.MovieRepository;
import mk.ukim.finki.imdbclone.repository.ReviewRepository;
import mk.ukim.finki.imdbclone.repository.UserRepository;
import mk.ukim.finki.imdbclone.service.domain.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private MovieRepository movieRepository;

    private ReviewService reviewService;

    private User testUser;
    private Movie testMovie;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewServiceImpl(reviewRepository, userRepository, mediaRepository);

        reviewRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();

        User u = new User();
        u.setUsername("rev_user");
        u.setEmail("rev@ex.m");
        u.setPassword("password123");
        u.setFirstName("f");
        u.setLastName("l");
        testUser = userRepository.save(u);

        Movie m = new Movie();
        m.setTitle("Review Movie");
        testMovie = movieRepository.save(m);
    }

    @Test
    void shouldCreateAndRetrieveReview() {
        Review review = reviewService.createReview(testUser.getId(), testMovie.getId(), "Great movie!");

        assertThat(review.getId()).isNotNull();
        assertThat(review.getReviewText()).isEqualTo("Great movie!");

        List<Review> movieReviews = reviewService.getReviewsByMedia(testMovie.getId());
        assertThat(movieReviews).hasSize(1);
    }

    @Test
    void shouldThrowWhenCreatingDuplicateReview() {
        reviewService.createReview(testUser.getId(), testMovie.getId(), "First review.");

        assertThatThrownBy(() -> reviewService.createReview(testUser.getId(), testMovie.getId(), "Second review!"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("has already reviewed media");
    }

    @Test
    void shouldUpdateReview() {
        Review review = reviewService.createReview(testUser.getId(), testMovie.getId(), "Initial text.");

        Review updated = reviewService.updateReview(review.getId(), "Updated text.");

        assertThat(updated.getReviewText()).isEqualTo("Updated text.");
        assertThat(reviewService.getReviewById(review.getId()).getReviewText()).isEqualTo("Updated text.");
    }

    @Test
    void shouldDeleteReview() {
        Review review = reviewService.createReview(testUser.getId(), testMovie.getId(), "To be deleted soon");

        reviewService.deleteReview(review.getId());

        assertThat(reviewService.getReview(testUser.getId(), testMovie.getId())).isEmpty();
    }
}
