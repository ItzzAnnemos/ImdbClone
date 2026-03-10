package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.enumerations.Role;

import java.util.List;
import java.util.stream.Collectors;

public record DisplayMediaPersonDto(
        String mediaTitle,
        String personFirstName,
        String personLastName,
        Role role,
        String characterName
) {

    public static DisplayMediaPersonDto from(MediaPerson mediaPerson) {
        return new DisplayMediaPersonDto(
                mediaPerson.getMedia().getTitle(),
                mediaPerson.getPerson().getFirstName(),
                mediaPerson.getPerson().getLastName(),
                mediaPerson.getRole(),
                mediaPerson.getCharacterName()
        );
    }

    public static List<DisplayMediaPersonDto> from(List<MediaPerson> mediaPersons) {
        return mediaPersons.stream()
                .map(DisplayMediaPersonDto::from)
                .collect(Collectors.toList());
    }
}