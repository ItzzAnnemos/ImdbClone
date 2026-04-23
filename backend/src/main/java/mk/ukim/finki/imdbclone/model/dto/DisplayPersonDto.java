package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record DisplayPersonDto(
        String firstName,
        String lastName,
        String biography,
        LocalDate birthDate,
        String profilePictureUrl) {

    public static DisplayPersonDto from(Person person) {
        return new DisplayPersonDto(
                person.getFirstName(),
                person.getLastName(),
                person.getBiography(),
                person.getBirthDate(),
                person.getProfilePictureUrl());
    }

    public static List<DisplayPersonDto> from(List<Person> persons) {
        return persons.stream()
                .map(DisplayPersonDto::from)
                .collect(Collectors.toList());
    }
}