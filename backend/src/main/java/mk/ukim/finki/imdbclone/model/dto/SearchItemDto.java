package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.Person;

public record SearchItemDto(
        Long id,
        String title,
        String imageUrl,
        String type,
        Double score
) {

    public static SearchItemDto fromMedia(Media media, Double score) {
        return new SearchItemDto(
                media.getId(),
                media.getTitle(),
                media.getPosterUrl(),
                media.getClass().getSimpleName(),
                score
        );
    }

    public static SearchItemDto fromPerson(Person person, Double score) {
        return new SearchItemDto(
                person.getId(),
                person.getFirstName() + " " + person.getLastName(),
                person.getProfilePictureUrl(),
                "Person",
                score
        );
    }
}