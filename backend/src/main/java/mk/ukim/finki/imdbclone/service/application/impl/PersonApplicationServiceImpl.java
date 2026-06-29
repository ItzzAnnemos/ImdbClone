package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.model.dto.CreatePersonDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayPersonDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedPersonDto;
import mk.ukim.finki.imdbclone.model.dto.PagedResponseDto;
import mk.ukim.finki.imdbclone.service.application.PersonApplicationService;
import mk.ukim.finki.imdbclone.service.domain.PersonService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Override
    public List<DisplayRankedPersonDto> findBornToday() {
        return toRankedPeople(personService.getBornToday(LocalDate.now()));
    }

    @Override
    public List<DisplayRankedPersonDto> findMostPopular() {
        return toRankedPeople(personService.getMostPopular());
    }

    @Override
    public PagedResponseDto<DisplayRankedPersonDto> findMostPopular(int page, int size) {
        int normalizedPage = Math.max(0, page);
        int normalizedSize = normalizePageSize(size);
        int offset = normalizedPage * normalizedSize;

        return PagedResponseDto.of(
                toRankedPeople(
                        personService.getMostPopular(PageRequest.of(normalizedPage, normalizedSize)),
                        offset + 1
                ),
                normalizedPage,
                normalizedSize,
                personService.count()
        );
    }

    private List<DisplayRankedPersonDto> toRankedPeople(List<Person> people) {
        return toRankedPeople(people, 1);
    }

    private List<DisplayRankedPersonDto> toRankedPeople(List<Person> people, int startRank) {
        AtomicInteger rank = new AtomicInteger(1);
        return people.stream()
                .map(person -> DisplayRankedPersonDto.from(person, startRank + rank.getAndIncrement() - 1))
                .toList();
    }

    private int normalizePageSize(int size) {
        if (size < 1) {
            return 20;
        }
        return Math.min(size, 100);
    }
}
