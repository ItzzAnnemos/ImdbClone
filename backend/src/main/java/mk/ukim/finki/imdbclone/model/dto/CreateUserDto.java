package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.User;

public record CreateUserDto(
        String username,
        String password,
        String repeatPassword,
        String firstName,
        String lastName,
        String email) {

    /*
     * todo: add repeat password logic
     */
    public User toUser() {
        return new User(username, password, firstName, lastName);
    }
}
