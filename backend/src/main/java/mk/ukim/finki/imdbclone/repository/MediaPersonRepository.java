package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MediaPersonRepository extends JpaRepository<MediaPerson, Long> {

    List<MediaPerson> findByPersonId(Long personId);

    /**
     * Find media credits for a batch of people, limited by pageable.
     *
     * @param personIds person IDs whose credits should be loaded
     * @param pageable  pagination and limit information
     * @return List of media credits for the selected people
     */
    List<MediaPerson> findByPerson_IdIn(Collection<Long> personIds, Pageable pageable);
}
