package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Media;

import java.util.List;

public record DisplayCardMediaDto(
        Long id,
        String title,
        String posterUrl,
        Double averageRating
) {

    public static DisplayCardMediaDto from(Media media) {
        return new DisplayCardMediaDto(
                media.getId(),
                media.getTitle(),
                media.getPosterUrl(),
                media.getAverageRating()
        );
    }

    public static List<DisplayCardMediaDto> from(List<Media> mediaList) {
        return mediaList.stream()
                .map(DisplayCardMediaDto::from)
                .toList();
    }
}