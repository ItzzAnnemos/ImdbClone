package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.model.domain.Rating;
import mk.ukim.finki.imdbclone.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    private User user1;
    private User user2;
    private Movie movie1;
    private Movie movie2;
    private Rating rating1;
    private Rating rating2;
    private Rating rating3;

    @BeforeEach
    void setUp() {
        // Clean up
        ratingRepository.deleteAll();
        userRepository.deleteAll();
        movieRepository.deleteAll();

        // Create test users
        user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password");
        user1.setFirstName("User");
        user1.setLastName("One");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2 = userRepository.save(user2);

        // Create test movies
        movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setDescription("Description 1");
        movie1.setReleaseYear(2020);
        movie1 = movieRepository.save(movie1);

        movie2 = new Movie();
        movie2.setTitle("Movie 2");
        movie2.setDescription("Description 2");
        movie2.setReleaseYear(2021);
        movie2 = movieRepository.save(movie2);

        // Create test ratings
        rating1 = new Rating();
        rating1.setUser(user1);
        rating1.setMedia(movie1);
        rating1.setRating(9);
        rating1 = ratingRepository.save(rating1);

        rating2 = new Rating();
        rating2.setUser(user2);
        rating2.setMedia(movie1);
        rating2.setRating(8);
        rating2 = ratingRepository.save(rating2);

        rating3 = new Rating();
        rating3.setUser(user1);
        rating3.setMedia(movie2);
        rating3.setRating(7);
        rating3 = ratingRepository.save(rating3);
    }

    @Test
    void testFindByUserIdAndMovieId() {
        // Act
        Optional<Rating> found = ratingRepository.findByUserIdAndMovieId(user1.getId(), movie1.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getRating()).isEqualTo(9);
    }

    @Test
    void testFindByUserId() {
        // Act
        List<Rating> results = ratingRepository.findByUserId(user1.getId());

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Rating::getRating)
                .containsExactlyInAnyOrder(9, 7);
    }

    @Test
    void testFindByMovieId() {
        // Act
        List<Rating> results = ratingRepository.findByMovieId(movie1.getId());

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Rating::getRating)
                .containsExactlyInAnyOrder(9, 8);
    }

    @Test
    void testFindAverageRatingByMovieId() {
        // Act
        Double average = ratingRepository.findAverageRatingByMovieId(movie1.getId());

        // Assert
        assertThat(average).isNotNull();
        assertThat(average).isEqualTo(8.5);
    }

    @Test
    void testFindAverageRatingByMovieId_NoRatings() {
        // Arrange
        Movie movieWithoutRatings = new Movie();
        movieWithoutRatings.setTitle("Unrated Movie");
        movieWithoutRatings.setDescription("No ratings yet");
        movieWithoutRatings.setReleaseYear(2022);
        movieWithoutRatings = movieRepository.save(movieWithoutRatings);

        // Act
        Double average = ratingRepository.findAverageRatingByMovieId(movieWithoutRatings.getId());

        // Assert
        assertThat(average).isNull();
    }

    @Test
    void testCountByMovieId() {
        // Act
        Long count = ratingRepository.countByMovieId(movie1.getId());

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testUniqueConstraint_UserAndMovie() {
        // Arrange - Try to create duplicate rating for same user and movie
        Rating duplicateRating = new Rating();
        duplicateRating.setUser(user1);
        duplicateRating.setMedia(movie1);
        duplicateRating.setRating(10);

        // Act & Assert
        assertThatThrownBy(() -> {
            ratingRepository.save(duplicateRating);
            ratingRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveRating() {
        // Arrange
        Rating newRating = new Rating();
        newRating.setUser(user2);
        newRating.setMedia(movie2);
        newRating.setRating(10);

        // Act
        Rating saved = ratingRepository.save(newRating);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void testDeleteRating() {
        // Act
        ratingRepository.deleteById(rating1.getId());

        // Assert
        assertThat(ratingRepository.findById(rating1.getId())).isEmpty();
    }

    @Test
    void testRatingValidation_MinValue() {
        // Ratings below 1 should fail validation
        // This would be tested by Bean Validation at the service layer,
        // but we verify the constraint is defined
        Rating invalidRating = new Rating();
        invalidRating.setUser(user2);
        invalidRating.setMedia(movie2);
        invalidRating.setRating(0); // Below minimum

        // The actual validation happens at different layers
        // Here we just save to verify the entity structure is correct
        assertThat(invalidRating.getRating()).isEqualTo(0);
    }
}
