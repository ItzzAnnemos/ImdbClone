package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.model.enumerations.Role;

import java.util.List;
import java.util.stream.Collectors;

public record CreateMediaPersonDto(
        Long mediaId,
        Long personId,
        Role role,
        String characterName
) {

    public static CreateMediaPersonDto from(MediaPerson mediaPerson) {
        return new CreateMediaPersonDto(
                mediaPerson.getMedia().getId(),
                mediaPerson.getPerson().getId(),
                mediaPerson.getRole(),
                mediaPerson.getCharacterName()
        );
    }

    public static List<CreateMediaPersonDto> from(List<MediaPerson> mediaPersons) {
        return mediaPersons.stream()
                .map(CreateMediaPersonDto::from)
                .collect(Collectors.toList());
    }

    public MediaPerson toMediaPerson(Media media, Person person) {
        return new MediaPerson(null, media, person, role, characterName);
    }
}