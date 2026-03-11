package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Media;

public record MediaDisplayDto(
        Long id,
        String title
) {

    public static MediaDisplayDto from(Media media) {
        return new MediaDisplayDto(
                media.getId(),
                media.getTitle()
        );
    }
}