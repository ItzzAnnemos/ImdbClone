package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

public record DisplayGenreDto(Long id, String name) {

    public static DisplayGenreDto from(Genre genre) {
        return new DisplayGenreDto(
                genre.getId(),
                genre.getName()
        );
    }

    public static List<DisplayGenreDto> from(List<Genre> genres) {
        return genres.stream()
                .map(DisplayGenreDto::from)
                .collect(Collectors.toList());
    }
}