package mk.ukim.finki.imdbclone.config.tmdb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration for the optional TMDB (The Movie Database) import.
 * Bound from the {@code tmdb.*} properties. Disabled by default, so the
 * application behaves exactly as before unless {@code tmdb.enabled=true}.
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

    /** How many pages of /movie/popular to import (20 movies per page). */
    private int pages = 5;

    /** Maximum number of cast members to store per movie. */
    private int maxCast = 8;
}
