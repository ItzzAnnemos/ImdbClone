package mk.ukim.finki.imdbclone.config.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.config.tmdb.TmdbProperties;
import mk.ukim.finki.imdbclone.service.tmdb.TmdbImportService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Runs the optional TMDB import once on startup, after the bean lifecycle (and
 * therefore after the @PostConstruct {@link DataInitializer} seed). Does nothing
 * unless {@code tmdb.enabled=true} and an API token is configured, so it is safe
 * to leave in place for normal runs.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TmdbImportRunner implements ApplicationRunner {

    private final TmdbProperties properties;
    private final TmdbImportService importService;

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.isEnabled()) {
            return;
        }
        if (properties.getApiToken() == null || properties.getApiToken().isBlank()) {
            log.warn("TMDB import is enabled but no 'tmdb.api-token' is set; skipping import.");
            return;
        }
        log.info("Starting TMDB movie import ({} page(s))...", properties.getPages());
        try {
            int imported = importService.importPopularMovies();
            log.info("TMDB import finished: {} new movie(s) added.", imported);
        } catch (Exception ex) {
            log.error("TMDB import failed: {}", ex.getMessage(), ex);
        }
    }
}
