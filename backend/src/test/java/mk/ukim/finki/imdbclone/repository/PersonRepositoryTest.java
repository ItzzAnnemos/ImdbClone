package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();

        person1 = new Person();
        person1.setFirstName("Christopher");
        person1.setLastName("Nolan");
        person1.setBirthDate(LocalDate.of(1970, 7, 30));
        person1 = personRepository.save(person1);

        person2 = new Person();
        person2.setFirstName("Francis");
        person2.setLastName("Ford Coppola");
        person2.setBirthDate(LocalDate.of(1939, 4, 7));
        person2 = personRepository.save(person2);
    }

    @Test
    void testFindByFirstNameAndLastName() {
        Optional<Person> found = personRepository.findByFirstNameAndLastName("Christopher", "Nolan");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Christopher");
        assertThat(found.get().getLastName()).isEqualTo("Nolan");
    }

    @Test
    void testFindByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase() {
        List<Person> results = personRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("chris", "chris");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFirstName()).isEqualTo("Christopher");

        List<Person> results2 = personRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("coppola", "coppola");

        assertThat(results2).hasSize(1);
        assertThat(results2.get(0).getLastName()).isEqualTo("Ford Coppola");
    }

    @Test
    void testSavePerson() {
        Person newPerson = new Person();
        newPerson.setFirstName("Quentin");
        newPerson.setLastName("Tarantino");

        Person saved = personRepository.save(newPerson);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Quentin");
    }
}
