package mk.ukim.finki.imdbclone.service.domain.impl;

import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.repository.PersonRepository;
import mk.ukim.finki.imdbclone.service.domain.PersonService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private static final int MOST_POPULAR_LIMIT = 250;

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    @Transactional
    public Person updatePerson(Long id, Person personDetails) {
        Person existing = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));

        existing.setFirstName(personDetails.getFirstName());
        existing.setLastName(personDetails.getLastName());
        existing.setBiography(personDetails.getBiography());
        existing.setBirthDate(personDetails.getBirthDate());
        existing.setProfilePictureUrl(personDetails.getProfilePictureUrl());

        return personRepository.save(existing);
    }

    @Override
    @Transactional
    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> searchPersonsByName(String name) {
        return personRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getBornToday(LocalDate date) {
        return personRepository.findAll()
                .stream()
                .filter(person -> person.getBirthDate() != null)
                .filter(person -> person.getBirthDate().getMonth() == date.getMonth()
                        && person.getBirthDate().getDayOfMonth() == date.getDayOfMonth())
                .sorted(popularComparator())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getMostPopular() {
        return getMostPopular(PageRequest.of(0, MOST_POPULAR_LIMIT));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getMostPopular(Pageable pageable) {
        return personRepository.findMostPopular(pageable);
    }

    private Comparator<Person> popularComparator() {
        return Comparator
                .comparing((Person person) -> person.getMediaCredits().size(), Comparator.reverseOrder())
                .thenComparing(Person::getFirstName)
                .thenComparing(Person::getLastName);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return personRepository.count();
    }
}
