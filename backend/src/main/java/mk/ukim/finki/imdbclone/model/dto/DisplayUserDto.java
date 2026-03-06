package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.User;

public record DisplayUserDto(
        String username,
        String firstName,
        String lastName
) {

    public static DisplayUserDto from(User user) {
        return new DisplayUserDto(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public User toUser() {
        return new User(username, firstName, lastName);
    }
}
