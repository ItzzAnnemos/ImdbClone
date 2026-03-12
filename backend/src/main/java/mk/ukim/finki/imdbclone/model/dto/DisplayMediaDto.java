package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Media;

public record DisplayMediaDto(
        Long id,
        String title
) {

    public static DisplayMediaDto from(Media media) {
        return new DisplayMediaDto(
                media.getId(),
                media.getTitle()
        );
    }
}