package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

public record CreateGenreDto(String name) {

    public static CreateGenreDto from(Genre genre) {
        return new CreateGenreDto(
                genre.getName()
        );
    }

    public static List<CreateGenreDto> from(List<Genre> genres) {
        return genres.stream()
                .map(CreateGenreDto::from)
                .collect(Collectors.toList());
    }

    public Genre toGenre() {
        Genre genre = new Genre();
        genre.setName(name);
        return genre;
    }
}