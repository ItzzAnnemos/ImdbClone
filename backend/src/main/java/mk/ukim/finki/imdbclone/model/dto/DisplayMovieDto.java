package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Movie;

import java.util.List;
import java.util.stream.Collectors;

public record DisplayMovieDto(
        Long id,
        String title,
        String description,
        Integer releaseYear,
        String posterUrl,
        Double averageRating,
        Integer duration,
        List<DisplayGenreDto> genres,
        List<DisplayMediaPersonDto> cast
) {

    public static DisplayMovieDto from(Movie movie) {
        return new DisplayMovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getReleaseYear(),
                movie.getPosterUrl(),
                movie.getAverageRating(),
                movie.getDuration(),
                DisplayGenreDto.from(movie.getGenres().stream().toList()),
                DisplayMediaPersonDto.from(movie.getCastAndCrew())
        );
    }

    public static List<DisplayMovieDto> from(List<Movie> movies) {
        return movies.stream()
                .map(DisplayMovieDto::from)
                .collect(Collectors.toList());
    }
}