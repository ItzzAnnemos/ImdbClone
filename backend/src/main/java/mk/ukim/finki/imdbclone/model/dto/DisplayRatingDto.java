package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Rating;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DisplayRatingDto(
        String username,
        Long mediaId,
        String mediaTitle,
        String posterUrl,
        String trailerUrl,
        Double averageRating,
        String mediaType,
        Integer rating,
        LocalDateTime createdAt
) {

    public static DisplayRatingDto from(Rating rating) {
        DisplayCardMediaDto media = DisplayCardMediaDto.from(rating.getMedia());
        return new DisplayRatingDto(
                rating.getUser().getUsername(),
                media.id(),
                rating.getMedia().getTitle(),
                media.posterUrl(),
                media.trailerUrl(),
                media.averageRating(),
                media.type(),
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
