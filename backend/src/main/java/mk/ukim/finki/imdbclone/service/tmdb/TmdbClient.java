package mk.ukim.finki.imdbclone.service.tmdb;

import mk.ukim.finki.imdbclone.config.tmdb.TmdbProperties;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbGenreListResponse;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMovieDetails;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbMoviePage;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbTVDetails;
import mk.ukim.finki.imdbclone.model.dto.tmdb.TmdbTVPage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

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

    /** Returns the official list of TV genres and their TMDB ids. */
    public TmdbGenreListResponse getTVGenres() {
        return restClient.get()
                .uri("/genre/tv/list?language=en")
                .retrieve()
                .body(TmdbGenreListResponse.class);
    }

    /** Returns one page (20 items) from TMDB movie discovery. */
    public TmdbMoviePage discoverMovies(String sortBy, int page, int minimumVoteCount) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/discover/movie")
                        .queryParam("include_adult", false)
                        .queryParam("include_video", false)
                        .queryParam("language", "en-US")
                        .queryParam("page", page)
                        .queryParam("sort_by", sortBy)
                        .queryParam("primary_release_date.lte", LocalDate.now())
                        .queryParam("vote_count.gte", minimumVoteCount)
                        .build())
                .retrieve()
                .body(TmdbMoviePage.class);
    }

    /** Returns one page (20 items) from TMDB TV discovery. */
    public TmdbTVPage discoverTVSeries(String sortBy, int page, int minimumVoteCount) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/discover/tv")
                        .queryParam("include_adult", false)
                        .queryParam("language", "en-US")
                        .queryParam("page", page)
                        .queryParam("sort_by", sortBy)
                        .queryParam("first_air_date.lte", LocalDate.now())
                        .queryParam("vote_count.gte", minimumVoteCount)
                        .build())
                .retrieve()
                .body(TmdbTVPage.class);
    }

    /** Returns full details for a movie, including cast and crew. */
    public TmdbMovieDetails getMovieDetails(Long id) {
        return restClient.get()
                .uri("/movie/{id}?append_to_response=credits,videos&language=en-US", id)
                .retrieve()
                .body(TmdbMovieDetails.class);
    }

    /** Returns full details for a TV series, including cast, crew and videos. */
    public TmdbTVDetails getTVSeriesDetails(Long id) {
        return restClient.get()
                .uri("/tv/{id}?append_to_response=credits,videos&language=en-US", id)
                .retrieve()
                .body(TmdbTVDetails.class);
    }
}
