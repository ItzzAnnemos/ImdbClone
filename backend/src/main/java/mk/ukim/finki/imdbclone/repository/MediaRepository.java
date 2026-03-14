package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.enumerations.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
