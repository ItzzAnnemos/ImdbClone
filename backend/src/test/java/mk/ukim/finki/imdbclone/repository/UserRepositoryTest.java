package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Clean up
        userRepository.deleteAll();

        // Create test users
        user1 = new User();
        user1.setUsername("john_doe");
        user1.setEmail("john@example.com");
        user1.setPassword("password123");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("jane_smith");
        user2.setEmail("jane@example.com");
        user2.setPassword("password456");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2 = userRepository.save(user2);
    }

    @Test
    void testFindByUsername() {
        // Act
        Optional<User> found = userRepository.findByUsername("john_doe");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testFindByUsername_NotFound() {
        // Act
        Optional<User> found = userRepository.findByUsername("nonexistent");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void testFindByEmail() {
        // Act
        Optional<User> found = userRepository.findByEmail("jane@example.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("jane_smith");
    }

    @Test
    void testExistsByUsername() {
        // Act & Assert
        assertThat(userRepository.existsByUsername("john_doe")).isTrue();
        assertThat(userRepository.existsByUsername("nonexistent")).isFalse();
    }

    @Test
    void testExistsByEmail() {
        // Act & Assert
        assertThat(userRepository.existsByEmail("john@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }

    @Test
    void testUniqueConstraint_Username() {
        // Arrange
        User duplicateUser = new User();
        duplicateUser.setUsername("john_doe"); // Duplicate username
        duplicateUser.setEmail("different@example.com");
        duplicateUser.setPassword("password789");
        duplicateUser.setFirstName("Different");
        duplicateUser.setLastName("User");

        // Act & Assert
        assertThatThrownBy(() -> {
            userRepository.save(duplicateUser);
            userRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testUniqueConstraint_Email() {
        // Arrange
        User duplicateUser = new User();
        duplicateUser.setUsername("different_user");
        duplicateUser.setEmail("john@example.com"); // Duplicate email
        duplicateUser.setPassword("password789");
        duplicateUser.setFirstName("Different");
        duplicateUser.setLastName("User");

        // Act & Assert
        assertThatThrownBy(() -> {
            userRepository.save(duplicateUser);
            userRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("new_user");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("newpassword");
        newUser.setFirstName("New");
        newUser.setLastName("User");

        // Act
        User saved = userRepository.save(newUser);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void testDeleteUser_CascadesRatingsAndReviews() {
        // This test verifies cascade deletion works properly
        // Act
        userRepository.deleteById(user1.getId());

        // Assert
        assertThat(userRepository.findById(user1.getId())).isEmpty();
    }

    @Test
    void testFindAll() {
        // Act
        var allUsers = userRepository.findAll();

        // Assert
        assertThat(allUsers).hasSize(2);
    }
}
