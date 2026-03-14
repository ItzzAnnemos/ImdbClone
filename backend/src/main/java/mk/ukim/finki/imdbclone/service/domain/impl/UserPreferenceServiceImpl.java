package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.domain.Genre;
import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.model.domain.UserPreference;
import mk.ukim.finki.imdbclone.model.enumerations.PreferenceType;
import mk.ukim.finki.imdbclone.model.enumerations.Role;
import mk.ukim.finki.imdbclone.repository.UserPreferenceRepository;
import mk.ukim.finki.imdbclone.service.domain.UserPreferenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;

    @Override
    public void processRatingPreference(User user, Media media, Integer rating) {
        double weight = getRatingWeight(rating);

        if (weight == 0.0) {
            return;
        }

        addGenrePreferences(user, media, weight);
        addPersonPreferences(user, media, weight);
    }

    @Override
    public void processWatchlistPreference(User user, Media media) {
        double weight = 1.5;

        addGenrePreferences(user, media, weight);
        addPersonPreferences(user, media, weight);
    }

    @Override
    public List<UserPreference> getUserPreferences(User user) {
        return userPreferenceRepository.findAllByUserOrderByScoreDesc(user);
    }

    private void addGenrePreferences(User user, Media media, double weight) {
        if (media.getGenres() == null) {
            return;
        }

        for (Genre genre : media.getGenres()) {
            updatePreference(user, PreferenceType.GENRE, genre.getName(), weight);
        }
    }

    private void addPersonPreferences(User user, Media media, double weight) {
        if (media.getCastAndCrew() == null) {
            return;
        }

        for (MediaPerson mp : media.getCastAndCrew()) {
            Person person = mp.getPerson();

            if (person == null) {
                continue;
            }

            String fullName = person.getFirstName() + " " + person.getLastName();

            if (mp.getRole() == Role.DIRECTOR) {
                updatePreference(user, PreferenceType.DIRECTOR, fullName, weight);
            }

            if (mp.getRole() == Role.MAIN_ACTOR) {
                updatePreference(user, PreferenceType.ACTOR, fullName, weight);
            }
        }
    }

    private void updatePreference(User user, PreferenceType type, String value, double delta) {
        UserPreference preference = userPreferenceRepository
                .findByUserAndPreferenceTypeAndValue(user, type, value)
                .orElseGet(() -> new UserPreference(user, type, value, 0.0));

        preference.setScore(preference.getScore() + delta);
        userPreferenceRepository.save(preference);
    }

    private double getRatingWeight(Integer rating) {
        if (rating == null) return 0.0;
        if (rating >= 8) return 3.0;
        if (rating >= 6) return 1.5;
        if (rating >= 4) return 0.0;
        return -2.0;
    }
}