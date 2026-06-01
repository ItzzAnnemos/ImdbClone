package mk.ukim.finki.imdbclone.model.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbVideos(List<TmdbVideo> results) {
}
