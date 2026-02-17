package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TVSeriesRepository extends JpaRepository<TVSeries, Long> {
    List<TVSeries> findAllByStatus(String status);
}
