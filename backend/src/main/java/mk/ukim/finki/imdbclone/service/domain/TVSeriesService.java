package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for TVSeries-specific operations.
 * Extends the generic {@link MediaService} base interface with TVSeries CRUD
 * and discovery,
 * and adds TV-series-specific queries.
 */
public interface TVSeriesService extends MediaService<TVSeries> {

    /**
     * Find all TV series with the given status (e.g. "Ended", "Returning Series",
     * "Canceled")
     *
     * @param status the status to filter by
     * @return List of TV series matching the status
     */
    List<TVSeries> getByStatus(String status);

    /**
     * Find TV series belonging to a specific genre.
     *
     * @param genreName the genre name
     * @return List of TV series in that genre
     */
    List<TVSeries> getByGenre(String genreName);

    /**
     * Get the highest-rated TV series for the Top 250 discovery chart.
     * TV series are ranked by average rating, release year, and title.
     *
     * @return List of up to 250 ranked TV series
     */
    List<TVSeries> getTop250();

    /**
     * Get a page of highest-rated TV series for the Top 250 discovery chart.
     *
     * @param pageable pagination and page size information
     * @return List of ranked TV series for the requested page
     */
    List<TVSeries> getTop250(Pageable pageable);

    /**
     * Get the most popular TV series for the discovery chart.
     * TV series are ranked by rating count, then average rating, release year, and title.
     *
     * @return List of up to 250 popular TV series
     */
    List<TVSeries> getMostPopular();

    /**
     * Get a page of the most popular TV series for the discovery chart.
     *
     * @param pageable pagination and page size information
     * @return List of popular TV series for the requested page
     */
    List<TVSeries> getMostPopular(Pageable pageable);

    /**
     * Get TV series in a genre ranked with the same ordering used by the Top 250 chart.
     *
     * @param genreName the genre name
     * @return List of ranked TV series in that genre
     */
    List<TVSeries> getRankedByGenre(String genreName);

    /**
     * Get a page of TV series in a genre ranked with the same ordering used by the Top 250 chart.
     *
     * @param genreName the genre name
     * @param pageable  pagination and page size information
     * @return List of ranked TV series in that genre for the requested page
     */
    List<TVSeries> getRankedByGenre(String genreName, Pageable pageable);

    /**
     * Count TV series that belong to a specific genre.
     *
     * @param genreName the genre name
     * @return total number of TV series in that genre
     */
    long countByGenre(String genreName);

    /**
     * Count all stored TV series.
     *
     * @return total number of TV series
     */
    long count();
}
