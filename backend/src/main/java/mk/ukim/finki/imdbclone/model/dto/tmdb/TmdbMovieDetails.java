package mk.ukim.finki.imdbclone.model.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbMovieDetails(
        Long id,
        String title,
        String overview,
        @JsonProperty("release_date") String releaseDate,
        Integer runtime,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("vote_average") Double voteAverage,
        List<TmdbGenre> genres,
        TmdbCredits credits
) {
}
