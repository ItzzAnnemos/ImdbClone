package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Person;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Person-specific operations.
 * Handles creating, updating, and searching for cast and crew members.
 */
public interface PersonService {

    /**
     * Create a new person.
     *
     * @param person the entity to create
     * @return the created person
     */
    Person createPerson(Person person);

    /**
     * Update an existing person by their ID.
     *
     * @param id            the ID of the person to update
     * @param personDetails the updated details
     * @return the updated person
     */
    Person updatePerson(Long id, Person personDetails);

    /**
     * Delete a person by their ID.
     *
     * @param id the ID of the person to delete
     */
    void deletePerson(Long id);

    /**
     * Fetch a person by their ID.
     *
     * @param id the ID to search for
     * @return an Optional containing the person if found
     */
    Optional<Person> getPersonById(Long id);

    /**
     * Search for persons whose first name or last name contains the given string,
     * ignoring case.
     *
     * @param name the search term
     * @return List of matching persons
     */
    List<Person> searchPersonsByName(String name);

    /**
     * Get all persons.
     *
     * @return List of all persons
     */
    List<Person> getAllPersons();
}
