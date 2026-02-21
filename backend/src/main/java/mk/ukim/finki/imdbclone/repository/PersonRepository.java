package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Find a person by their exact first name and last name.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @return an Optional containing the Person if found
     */
    Optional<Person> findByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Search for people by a partial match on either their first or last name,
     * ignoring case.
     *
     * @param firstName the first name search term
     * @param lastName  the last name search term
     * @return List of matching persons
     */
    List<Person> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
