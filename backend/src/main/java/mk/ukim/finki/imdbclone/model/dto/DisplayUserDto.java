package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.User;

public record DisplayUserDto(
        Long id,
        String username,
        String firstName,
        String lastName
) {
    public static DisplayUserDto from(User user) {
        return new DisplayUserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
