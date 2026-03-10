package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record CreatePersonDto(
        String firstName,
        String lastName,
        String biography,
        LocalDate birthDate,
        String profilePictureUrl
) {

    public static CreatePersonDto from(Person person) {
        return new CreatePersonDto(
                person.getFirstName(),
                person.getLastName(),
                person.getBiography(),
                person.getBirthDate(),
                person.getProfilePictureUrl()
        );
    }

    public static List<CreatePersonDto> from(List<Person> persons) {
        return persons.stream()
                .map(CreatePersonDto::from)
                .collect(Collectors.toList());
    }

    public Person toPerson() {
        return new Person(
                null,
                firstName,
                lastName,
                biography,
                birthDate,
                profilePictureUrl,
                null,
                null,
                null
        );
    }
}