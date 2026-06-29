package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Media;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    /**
     * Find all media items whose title contains the given string (case-insensitive)
     *
     * @param title the search term
     * @return List of media items matching the title
     */
    List<Media> findAllByTitleContainingIgnoreCase(String title);

    /**
     * Find media items whose title contains the search term, limited by pageable.
     *
     * @param title    the search term
     * @param pageable pagination and sorting information
     * @return List of matching media items
     */
    List<Media> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * Find the top 10 media items ordered by average rating in descending order
     *
     * @return List of the top 10 highest-rated media items across all types
     */
    List<Media> findTop10ByOrderByAverageRatingDesc();

    /**
     * Find the top 10 most recently added media items
     *
     * @return List of the 10 newest media items across all types
     */
    List<Media> findTop10ByOrderByCreatedAtDesc();

    List<Media> findByReleaseYear(Integer releaseYear);

    /**
     * Find media items released in a specific year, limited by pageable.
     *
     * @param releaseYear the release year
     * @param pageable    pagination and sorting information
     * @return List of media items from that year
     */
    List<Media> findByReleaseYear(Integer releaseYear, Pageable pageable);

    List<Media> findByIdNotInOrderByAverageRatingDescReleaseYearDesc(Collection<Long> excludedIds, Pageable pageable);

    List<Media> findAllByOrderByAverageRatingDescReleaseYearDesc(Pageable pageable);
}
