package mk.ukim.finki.imdbclone.repository;

import mk.ukim.finki.imdbclone.model.domain.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    /**
     * Find the most popular people using media credit count as the primary ranking signal.
     *
     * @param pageable pagination and limit information
     * @return List of people ordered by credit count and name
     */
    @Query("""
           SELECT p
           FROM Person p
           LEFT JOIN p.mediaCredits credit
           GROUP BY p
           ORDER BY COUNT(credit) DESC, p.firstName ASC, p.lastName ASC
           """)
    List<Person> findMostPopular(Pageable pageable);

}
