package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Find movies by title containing the search term (case-insensitive)
     * 
     * @param title the search term
     * @return List of movies matching the title
     */
    List<Movie> findByTitleContainingIgnoreCase(String title);

    /**
     * Find movies by release year
     * 
     * @param year the release year
     * @return List of movies released in that year
     */
    List<Movie> findByReleaseYear(Integer year);

    /**
     * Find movies released between two years (inclusive)
     * 
     * @param startYear the start year
     * @param endYear   the end year
     * @return List of movies released in the year range
     */
    List<Movie> findByReleaseYearBetween(Integer startYear, Integer endYear);

    /**
     * Find movies by director name (case-insensitive partial match)
     * 
     * @param director the director name search term
     * @return List of movies by the director
     */
    @Query("SELECT m FROM Movie m JOIN m.castAndCrew mp JOIN mp.person p " +
            "WHERE mp.role = 'DIRECTOR' AND " +
            "(LOWER(p.firstName) LIKE LOWER(CONCAT('%', :directorName, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :directorName, '%')))")
    List<Movie> findByDirectorName(@Param("directorName") String directorName);

    /**
     * Find movies by a specific genre name
     * 
     * @param genreName the genre name
     * @return List of movies in that genre
     */
    List<Movie> findByGenres_Name(String genreName);

    /**
     * Find movies that belong to any of the specified genres
     * 
     * @param genreNames list of genre names
     * @return List of movies matching any of the genres
     */
    List<Movie> findByGenres_NameIn(List<String> genreNames);

    /**
     * Find the top 10 movies ordered by average rating in descending order
     *
     * @return List of the top 10 highest-rated movies
     */
    List<Movie> findTop10ByOrderByAverageRatingDesc();

    /**
     * Find the top 10 most recently created movies
     * 
     * @return List of the 10 newest movies
     */
    List<Movie> findTop10ByOrderByCreatedAtDesc();
}
