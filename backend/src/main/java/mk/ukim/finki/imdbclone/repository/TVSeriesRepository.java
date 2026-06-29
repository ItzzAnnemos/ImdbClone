package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
     * Find TV series by a specific genre name.
     *
     * @param genreName the genre name
     * @return List of TV series in that genre
     */
    List<TVSeries> findByGenres_Name(String genreName);

    /**
     * Find TV series in a genre ordered for ranked browse pages using a pageable limit.
     *
     * @param genreName the genre name
     * @param pageable  pagination and limit information
     * @return List of TV series in the genre ordered by average rating, release year, and title
     */
    List<TVSeries> findByGenres_NameOrderByAverageRatingDescReleaseYearDescTitleAsc(
            String genreName,
            Pageable pageable);

    /**
     * Count TV series that belong to a specific genre.
     *
     * @param genreName the genre name
     * @return total number of TV series in that genre
     */
    long countByGenres_Name(String genreName);

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

    /**
     * Find TV series ordered for the Top 250 chart using a pageable limit.
     *
     * @param pageable pagination and limit information
     * @return List of TV series ordered by average rating, release year, and title
     */
    List<TVSeries> findAllByOrderByAverageRatingDescReleaseYearDescTitleAsc(Pageable pageable);

    /**
     * Find the most popular TV series using rating count as the primary ranking signal.
     *
     * @param pageable pagination and limit information
     * @return List of TV series ordered by rating count, average rating, release year, and title
     */
    @Query("""
           SELECT series
           FROM TVSeries series
           LEFT JOIN series.ratings rating
           GROUP BY series
           ORDER BY COUNT(rating) DESC, COALESCE(series.averageRating, 0) DESC, series.releaseYear DESC, series.title ASC
           """)
    List<TVSeries> findMostPopularRanked(Pageable pageable);
}
