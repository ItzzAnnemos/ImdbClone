package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.repository.UserRepository;
import mk.ukim.finki.imdbclone.service.domain.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
        userRepository.deleteAll();
    }

    private User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
        return user;
    }

    @Test
    void shouldCreateAndRetrieveUser() {
        User user = createTestUser("johndoe", "john@example.com");
        User saved = userService.createUser(user);

        assertThat(saved.getId()).isNotNull();

        Optional<User> byId = userService.getUserById(saved.getId());
        assertThat(byId).isPresent();

        Optional<User> byUsername = userService.getUserByUsername("johndoe");
        assertThat(byUsername).isPresent();

        Optional<User> byEmail = userService.getUserByEmail("john@example.com");
        assertThat(byEmail).isPresent();
    }

    @Test
    void shouldUpdateUser() {
        User user = createTestUser("johndoe", "john@example.com");
        User saved = userService.createUser(user);

        User updateDetails = new User();
        updateDetails.setFirstName("John");
        updateDetails.setLastName("Smith");
        updateDetails.setEmail("john.smith@example.com");

        User updated = userService.updateUser(saved.getId(), updateDetails);

        assertThat(updated.getFirstName()).isEqualTo("John");
        assertThat(updated.getLastName()).isEqualTo("Smith");
        assertThat(updated.getEmail()).isEqualTo("john.smith@example.com");
    }

    @Test
    void shouldThrowWhenCreatingDuplicateUsername() {
        userService.createUser(createTestUser("johndoe", "john1@example.com"));

        assertThatThrownBy(() -> userService.createUser(createTestUser("johndoe", "john2@example.com")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Username already taken: johndoe");
    }

    @Test
    void shouldThrowWhenCreatingDuplicateEmail() {
        userService.createUser(createTestUser("john1", "john@example.com"));

        assertThatThrownBy(() -> userService.createUser(createTestUser("john2", "john@example.com")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already registered: john@example.com");
    }

    @Test
    void shouldDeleteUser() {
        User saved = userService.createUser(createTestUser("johndoe", "john@example.com"));

        userService.deleteUser(saved.getId());

        assertThat(userService.getUserById(saved.getId())).isEmpty();
    }
}
