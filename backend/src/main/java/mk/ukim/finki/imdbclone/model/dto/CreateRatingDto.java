package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.Rating;
import mk.ukim.finki.imdbclone.model.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public record CreateRatingDto(
        Long userId,
        Long mediaId,
        Integer rating
) {

    public static CreateRatingDto from(Rating rating) {
        return new CreateRatingDto(
                rating.getUser().getId(),
                rating.getMedia().getId(),
                rating.getRating()
        );
    }

    public static List<CreateRatingDto> from(List<Rating> ratings) {
        return ratings.stream()
                .map(CreateRatingDto::from)
                .collect(Collectors.toList());
    }

    public Rating toRating(User user, Media media) {
        return new Rating(null, user, media, rating, null);
    }
}