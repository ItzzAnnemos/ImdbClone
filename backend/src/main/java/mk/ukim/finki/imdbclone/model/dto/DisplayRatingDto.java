package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Rating;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DisplayRatingDto(
        String username,
        String mediaTitle,
        Integer rating,
        LocalDateTime createdAt
) {

    public static DisplayRatingDto from(Rating rating) {
        return new DisplayRatingDto(
                rating.getUser().getUsername(),
                rating.getMedia().getTitle(),
                rating.getRating(),
                rating.getCreatedAt()
        );
    }

    public static List<DisplayRatingDto> from(List<Rating> ratings) {
        return ratings.stream()
                .map(DisplayRatingDto::from)
                .collect(Collectors.toList());
    }
}