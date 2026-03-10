package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Movie;

import java.util.List;
import java.util.stream.Collectors;

public record CreateMovieDto(
        String title,
        String description,
        Integer releaseYear,
        String posterUrl,
        Integer duration
) {

    public static CreateMovieDto from(Movie movie) {
        return new CreateMovieDto(
                movie.getTitle(),
                movie.getDescription(),
                movie.getReleaseYear(),
                movie.getPosterUrl(),
                movie.getDuration()
        );
    }

    public static List<CreateMovieDto> from(List<Movie> movies) {
        return movies.stream()
                .map(CreateMovieDto::from)
                .collect(Collectors.toList());
    }

    public Movie toMovie() {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setReleaseYear(releaseYear);
        movie.setPosterUrl(posterUrl);
        movie.setDuration(duration);
        return movie;
    }
}