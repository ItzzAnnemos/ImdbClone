package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.Movie;

import java.util.List;

public record DisplayCardMediaDto(
        Long id,
        String title,
        String posterUrl,
        Double averageRating,
        String type
) {

    public static DisplayCardMediaDto from(Media media) {
        String type = media instanceof Movie ? "movie" : "tv";
        return new DisplayCardMediaDto(
                media.getId(),
                media.getTitle(),
                media.getPosterUrl(),
                media.getAverageRating(),
                type
        );
    }

    public static List<DisplayCardMediaDto> from(List<Media> mediaList) {
        return mediaList.stream()
                .map(DisplayCardMediaDto::from)
                .toList();
    }
}