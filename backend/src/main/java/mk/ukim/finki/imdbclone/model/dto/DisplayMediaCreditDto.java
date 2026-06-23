package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.enumerations.Role;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record DisplayMediaCreditDto(
        Long mediaId,
        String mediaTitle,
        Integer releaseYear,
        String posterUrl,
        Role role,
        String characterName
) {

    public static DisplayMediaCreditDto from(MediaPerson mediaPerson) {
        return new DisplayMediaCreditDto(
                mediaPerson.getMedia().getId(),
                mediaPerson.getMedia().getTitle(),
                mediaPerson.getMedia().getReleaseYear(),
                mediaPerson.getMedia().getPosterUrl(),
                mediaPerson.getRole(),
                mediaPerson.getCharacterName()
        );
    }

    public static List<DisplayMediaCreditDto> from(List<MediaPerson> mediaPersons) {
        if (mediaPersons == null) {
            return Collections.emptyList();
        }

        return mediaPersons.stream()
                .map(DisplayMediaCreditDto::from)
                .collect(Collectors.toList());
    }
}
