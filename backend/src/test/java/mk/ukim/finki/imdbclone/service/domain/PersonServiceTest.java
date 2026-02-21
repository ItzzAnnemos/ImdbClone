package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.repository.PersonRepository;
import mk.ukim.finki.imdbclone.service.domain.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class PersonServiceTest {

    @Autowired
    private PersonRepository personRepository;

    private PersonService personService;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        personService = new PersonServiceImpl(personRepository);
    }

    private Person createPerson(String firstName, String lastName, int year, int month, int day) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setBirthDate(LocalDate.of(year, month, day));
        return person;
    }

    @Test
    void shouldCreatePerson() {
        Person person = new Person();
        person.setFirstName("Christopher");
        person.setLastName("Nolan");

        Person created = personService.createPerson(person);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getFirstName()).isEqualTo("Christopher");
    }

    @Test
    void shouldUpdatePerson() {
        Person person = personService.createPerson(createPerson("Old", "Name", 1970, 7, 30));

        Person updateDetails = new Person();
        updateDetails.setFirstName("Chris");
        updateDetails.setLastName("Nolan");
        updateDetails.setBirthDate(LocalDate.of(1970, 7, 30));

        Person updated = personService.updatePerson(person.getId(), updateDetails);

        assertThat(updated.getFirstName()).isEqualTo("Chris");
        assertThat(updated.getLastName()).isEqualTo("Nolan");
        assertThat(personService.getPersonById(person.getId()).get().getFirstName()).isEqualTo("Chris");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPerson() {
        assertThatThrownBy(() -> personService.updatePerson(999L, new Person()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldGetPersonById() {
        Person person = personService.createPerson(createPerson("Christopher", "Nolan", 1970, 7, 30));

        Optional<Person> found = personService.getPersonById(person.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Christopher");
    }

    @Test
    void shouldSearchPersonsByName() {
        personService.createPerson(createPerson("Christopher", "Nolan", 1970, 7, 30));
        personService.createPerson(createPerson("Francis", "Ford Coppola", 1939, 4, 7));

        List<Person> results = personService.searchPersonsByName("Nolan");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getLastName()).isEqualTo("Nolan");
    }

    @Test
    void shouldDeletePerson() {
        Person person = personService.createPerson(createPerson("To Delete", "Person", 2000, 1, 1));

        personService.deletePerson(person.getId());

        assertThat(personService.getPersonById(person.getId())).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPerson() {
        assertThatThrownBy(() -> personService.deletePerson(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}
