package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TVSeriesRepository extends JpaRepository<TVSeries, Long> {

    /**
     * Find all TV series with the given status (e.g. "Ended", "Returning Series",
     * "Canceled")
     *
     * @param status the status to filter by
     * @return List of TV series matching the status
     */
    List<TVSeries> findAllByStatus(String status);

    /**
     * Find all TV series whose title contains the given string (case-insensitive)
     *
     * @param title the search term
     * @return List of matching TV series
     */
    List<TVSeries> findAllByTitleContainingIgnoreCase(String title);

    /**
     * Find the top 10 TV series ordered by average rating in descending order
     *
     * @return List of the top 10 highest-rated TV series
     */
    List<TVSeries> findTop10ByOrderByAverageRatingDesc();

    /**
     * Find the top 10 most recently added TV series
     *
     * @return List of the 10 newest TV series
     */
    List<TVSeries> findTop10ByOrderByCreatedAtDesc();
}
