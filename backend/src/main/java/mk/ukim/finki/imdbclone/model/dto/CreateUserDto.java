package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.User;

public record CreateUserDto(
        String username,
        String password,
        String repeatPassword,
        String firstName,
        String lastName,
        String email
) {

    public User toUser() {

        if (password == null || repeatPassword == null) {
            throw new IllegalArgumentException("Password fields cannot be null.");
        }

        if (!password.equals(repeatPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        User user = new User(username, password, firstName, lastName);
        user.setEmail(email);

        return user;
    }
}