package mk.ukim.finki.imdbclone.service.domain.helper;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.UserPreference;
import mk.ukim.finki.imdbclone.model.enumerations.Role;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PreferenceMatchingHelper {

    public double calculatePreferenceMatchScore(Media media, UserPreference preference) {

        switch (preference.getPreferenceType()) {

            case GENRE -> {
                boolean matchesGenre = media.getGenres() != null &&
                        media.getGenres().stream()
                                .anyMatch(g -> g.getName().equalsIgnoreCase(preference.getPreferenceValue()));

                if (matchesGenre) {
                    return preference.getScore();
                }
            }

            case DIRECTOR -> {
                boolean matchesDirector = media.getCastAndCrew() != null &&
                        media.getCastAndCrew().stream()
                                .filter(mp -> mp.getRole() == Role.DIRECTOR)
                                .map(mp -> mp.getPerson())
                                .filter(Objects::nonNull)
                                .map(p -> p.getFirstName() + " " + p.getLastName())
                                .anyMatch(name -> name.equalsIgnoreCase(preference.getPreferenceValue()));

                if (matchesDirector) {
                    return preference.getScore();
                }
            }

            case ACTOR -> {
                boolean matchesActor = media.getCastAndCrew() != null &&
                        media.getCastAndCrew().stream()
                                .filter(mp -> mp.getRole() == Role.MAIN_ACTOR)
                                .map(mp -> mp.getPerson())
                                .filter(Objects::nonNull)
                                .map(p -> p.getFirstName() + " " + p.getLastName())
                                .anyMatch(name -> name.equalsIgnoreCase(preference.getPreferenceValue()));

                if (matchesActor) {
                    return preference.getScore();
                }
            }
        }

        return 0.0;
    }
}