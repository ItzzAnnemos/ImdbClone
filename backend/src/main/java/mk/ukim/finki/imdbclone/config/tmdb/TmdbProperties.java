package mk.ukim.finki.imdbclone.config.tmdb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration for the TMDB (The Movie Database) import.
 * Bound from the {@code tmdb.*} properties.
 */
@Component
@ConfigurationProperties(prefix = "tmdb")
@Getter
@Setter
public class TmdbProperties {

    /** Master switch. When false, no TMDB calls are made at all. */
    private boolean enabled = false;

    /** TMDB v4 Read Access Token, sent as a Bearer token. */
    private String apiToken = "";

    /** TMDB API base URL. */
    private String baseUrl = "https://api.themoviedb.org/3";

    /** Base URL for poster/profile images (with a size segment). */
    private String imageBaseUrl = "https://image.tmdb.org/t/p/w500";

    /** How many pages to read per discovery sort (20 items per page). */
    private int pages = 25;

    /** Hard limit for newly imported movies in one startup run. */
    private int maxMovies = 500;

    /** Hard limit for newly imported TV series in one startup run. */
    private int maxTVSeries = 300;

    /** Discovery sort orders used to build a broad starter catalogue. */
    private List<String> discoverySorts = List.of(
            "popularity.desc",
            "vote_average.desc",
            "vote_count.desc",
            "primary_release_date.desc"
    );

    /** TV discovery sort orders used to build a broad starter catalogue. */
    private List<String> tvDiscoverySorts = List.of(
            "popularity.desc",
            "vote_average.desc",
            "vote_count.desc",
            "first_air_date.desc"
    );

    /** Avoids obscure high-rated titles with only a handful of votes. */
    private int minimumVoteCount = 200;

    /** Maximum number of cast members to store per media item. */
    private int maxCast = 10;
}
