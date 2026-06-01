package mk.ukim.finki.imdbclone.model.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbVideo(
        String name,
        String key,
        String site,
        String type,
        Boolean official
) {
}
