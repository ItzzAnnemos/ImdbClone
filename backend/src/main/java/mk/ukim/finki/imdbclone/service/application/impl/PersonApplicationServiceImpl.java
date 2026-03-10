package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.model.dto.CreatePersonDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayPersonDto;
import mk.ukim.finki.imdbclone.service.application.PersonApplicationService;
import mk.ukim.finki.imdbclone.service.domain.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonApplicationServiceImpl implements PersonApplicationService {

    private final PersonService personService;

    public PersonApplicationServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public List<DisplayPersonDto> findAll() {
        return DisplayPersonDto.from(personService.getAllPersons());
    }

    @Override
    public Optional<DisplayPersonDto> findById(Long id) {
        return personService.getPersonById(id)
                .map(DisplayPersonDto::from);
    }

    @Override
    public Optional<DisplayPersonDto> save(CreatePersonDto personDto) {
        Person person = personDto.toPerson();
        return Optional.of(
                DisplayPersonDto.from(personService.createPerson(person))
        );
    }

    @Override
    public Optional<DisplayPersonDto> update(Long id, CreatePersonDto personDto) {
        Person person = personDto.toPerson();
        return Optional.of(
                DisplayPersonDto.from(personService.updatePerson(id, person))
        );
    }

    @Override
    public void delete(Long id) {
        personService.deletePerson(id);
    }

    @Override
    public List<DisplayPersonDto> search(String name) {
        return DisplayPersonDto.from(
                personService.searchPersonsByName(name)
        );
    }
}