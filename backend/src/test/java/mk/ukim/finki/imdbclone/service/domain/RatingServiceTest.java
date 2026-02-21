package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.model.domain.Rating;
import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.repository.MediaRepository;
import mk.ukim.finki.imdbclone.repository.MovieRepository;
import mk.ukim.finki.imdbclone.repository.RatingRepository;
import mk.ukim.finki.imdbclone.repository.UserRepository;
import mk.ukim.finki.imdbclone.service.domain.impl.RatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RatingServiceTest {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    private RatingService ratingService;

    private User testUser1;
    private User testUser2;
    private Movie testMovie;

    @BeforeEach
    void setUp() {
        ratingService = new RatingServiceImpl(ratingRepository, mediaRepository, userRepository);

        ratingRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();

        // Set up base entities
        User u1 = new User();
        u1.setUsername("user1");
        u1.setEmail("u1@ex.m");
        u1.setPassword("password123");
        u1.setFirstName("f");
        u1.setLastName("l");
        testUser1 = userRepository.save(u1);

        User u2 = new User();
        u2.setUsername("user2");
        u2.setEmail("u2@ex.m");
        u2.setPassword("password123");
        u2.setFirstName("f");
        u2.setLastName("l");
        testUser2 = userRepository.save(u2);

        Movie m = new Movie();
        m.setTitle("Test Movie");
        testMovie = movieRepository.save(m);
    }

    @Test
    void shouldCreateRatingAndSyncAverage() {
        Rating saved = ratingService.rateMedia(testUser1.getId(), testMovie.getId(), 8);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getRating()).isEqualTo(8);

        // Verify that Media's averageRating was synced down to the DB
        Movie reloadedMovie = movieRepository.findById(testMovie.getId()).get();
        assertThat(reloadedMovie.getAverageRating()).isEqualTo(8.0);
    }

    @Test
    void shouldUpsertExistingRatingAndSyncAverage() {
        // User 1 rates 10
        ratingService.rateMedia(testUser1.getId(), testMovie.getId(), 10);

        // User 2 rates 2
        ratingService.rateMedia(testUser2.getId(), testMovie.getId(), 2);

        Movie reloadedMovie = movieRepository.findById(testMovie.getId()).get();
        // (10 + 2) / 2 = 6.0
        assertThat(reloadedMovie.getAverageRating()).isEqualTo(6.0);

        // User 1 updates rating to 6
        Rating updatedRating = ratingService.rateMedia(testUser1.getId(), testMovie.getId(), 6);
        assertThat(updatedRating.getRating()).isEqualTo(6);

        // Fast check that there are still only 2 ratings
        List<Rating> ratings = ratingService.getRatingsByMedia(testMovie.getId());
        assertThat(ratings).hasSize(2);

        // (6 + 2) / 2 = 4.0
        reloadedMovie = movieRepository.findById(testMovie.getId()).get();
        assertThat(reloadedMovie.getAverageRating()).isEqualTo(4.0);
    }

    @Test
    void shouldDeleteRatingAndSyncAverage() {
        ratingService.rateMedia(testUser1.getId(), testMovie.getId(), 10);
        ratingService.rateMedia(testUser2.getId(), testMovie.getId(), 2);

        ratingService.deleteRating(testUser1.getId(), testMovie.getId());

        List<Rating> ratings = ratingService.getRatingsByMedia(testMovie.getId());
        assertThat(ratings).hasSize(1);

        // Only User 2's rating remains
        Movie reloadedMovie = movieRepository.findById(testMovie.getId()).get();
        assertThat(reloadedMovie.getAverageRating()).isEqualTo(2.0);
    }
}
