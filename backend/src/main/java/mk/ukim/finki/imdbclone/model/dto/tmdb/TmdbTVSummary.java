package mk.ukim.finki.imdbclone.model.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbTVSummary(Long id, String name) {
}
