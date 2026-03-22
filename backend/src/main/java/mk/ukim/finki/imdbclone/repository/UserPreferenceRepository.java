package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.model.domain.UserPreference;
import mk.ukim.finki.imdbclone.model.enumerations.PreferenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    Optional<UserPreference> findByUserAndPreferenceTypeAndPreferenceValue(
            User user,
            PreferenceType preferenceType,
            String preferenceValue
    );

    List<UserPreference> findAllByUserOrderByScoreDesc(User user);
}