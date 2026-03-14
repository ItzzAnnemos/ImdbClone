package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaPersonRepository extends JpaRepository<MediaPerson, Long> {

    List<MediaPerson> findByPersonId(Long personId);
}
