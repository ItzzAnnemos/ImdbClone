package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.domain.Person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public record DisplayRankedPersonDto(
        Long id,
        Integer rank,
        String firstName,
        String lastName,
        String fullName,
        String profilePictureUrl,
        LocalDate birthDate,
        List<String> knownFor,
        Long creditCount
) {

    public static DisplayRankedPersonDto from(Person person, int rank) {
        return new DisplayRankedPersonDto(
                person.getId(),
                rank,
                person.getFirstName(),
                person.getLastName(),
                person.getFirstName() + " " + person.getLastName(),
                person.getProfilePictureUrl(),
                person.getBirthDate(),
                person.getMediaCredits()
                        .stream()
                        .map(MediaPerson::getMedia)
                        .map(media -> media.getTitle())
                        .distinct()
                        .sorted(Comparator.naturalOrder())
                        .toList(),
                (long) person.getMediaCredits().size()
        );
    }
}
