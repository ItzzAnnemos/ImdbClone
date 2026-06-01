package mk.ukim.finki.imdbclone.service.tmdb;

import mk.ukim.finki.imdbclone.config.tmdb.TmdbProperties;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbGenreListResponse;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMovieDetails;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMoviePage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Thin wrapper around the TMDB REST API. Authenticates with the v4 Read Access
 * Token as a Bearer header (the recommended TMDB auth method).
 */
@Component
public class TmdbClient {

    private final RestClient restClient;

    public TmdbClient(TmdbProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + properties.getApiToken())
                .defaultHeader("accept", "application/json")
                .build();
    }

    /** Returns the official list of movie genres and their TMDB ids. */
    public TmdbGenreListResponse getMovieGenres() {
        return restClient.get()
                .uri("/genre/movie/list?language=en")
                .retrieve()
                .body(TmdbGenreListResponse.class);
    }

    /** Returns one page (20 items) of the most popular movies. */
    public TmdbMoviePage getPopularMovies(int page) {
        return restClient.get()
                .uri("/movie/popular?language=en-US&page={page}", page)
                .retrieve()
                .body(TmdbMoviePage.class);
    }

    /** Returns full details for a movie, including cast and crew. */
    public TmdbMovieDetails getMovieDetails(Long id) {
        return restClient.get()
                .uri("/movie/{id}?append_to_response=credits&language=en-US", id)
                .retrieve()
                .body(TmdbMovieDetails.class);
    }
}
