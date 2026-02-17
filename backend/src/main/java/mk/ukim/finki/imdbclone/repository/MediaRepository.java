package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findAllByTitleContainingIgnoreCase(String title);

    List<Media> findTop10ByOrderByAverageRatingDesc();

    List<Media> findTop10ByOrderByCreatedAtDesc();
}
