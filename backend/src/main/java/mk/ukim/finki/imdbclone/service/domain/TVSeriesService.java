package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;

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
}
