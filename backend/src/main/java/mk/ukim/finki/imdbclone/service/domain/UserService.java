package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.User;

import java.util.Optional;

/**
 * Service interface for User management operations.
 */
public interface UserService {

    /**
     * Find a user by their ID
     *
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> getUserById(Long id);

    /**
     * Find a user by their username
     *
     * @param username the username
     * @return Optional containing the user if found
     */
    Optional<User> getUserByUsername(String username);

    /**
     * Find a user by their email address
     *
     * @param email the email address
     * @return Optional containing the user if found
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Persist a new user
     *
     * @param user the user to create
     * @return the saved user with generated ID
     */
    User createUser(User user);

    /**
     * Update an existing user's profile fields
     *
     * @param id          the ID of the user to update
     * @param userDetails the updated field values
     * @return the updated user
     */
    User updateUser(Long id, User userDetails);

    /**
     * Delete a user by their ID
     *
     * @param id the user ID
     */
    void deleteUser(Long id);

    /**
     * Check whether a username is already taken
     *
     * @param username the username to check
     * @return true if the username exists
     */
    boolean usernameExists(String username);

    /**
     * Check whether an email address is already registered
     *
     * @param email the email to check
     * @return true if the email exists
     */
    boolean emailExists(String email);
}
