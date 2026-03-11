package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.User;

import java.util.List;

public record DisplayUserDto(
        String username,
        String firstName,
        String lastName,
        List<Long> watchlist
) {
    public static DisplayUserDto from(User user) {
        return new DisplayUserDto(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getWatchlist().stream()
                        .map(Media::getId)
                        .toList()
        );
    }
}
