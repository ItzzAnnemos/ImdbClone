package mk.ukim.finki.imdbclone.model.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbTVDetails(
        Long id,
        String name,
        String overview,
        @JsonProperty("first_air_date") String firstAirDate,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("vote_average") Double voteAverage,
        @JsonProperty("number_of_seasons") Integer numberOfSeasons,
        String status,
        List<TmdbGenre> genres,
        @JsonProperty("created_by") List<TmdbCreatedBy> createdBy,
        TmdbCredits credits,
        TmdbVideos videos
) {
}
