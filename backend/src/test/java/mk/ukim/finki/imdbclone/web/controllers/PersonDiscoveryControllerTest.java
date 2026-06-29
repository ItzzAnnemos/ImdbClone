package mk.ukim.finki.imdbclone.web.controllers;

import mk.ukim.finki.imdbclone.model.dto.CreatePersonDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayPersonDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedPersonDto;
import mk.ukim.finki.imdbclone.model.dto.PagedResponseDto;
import mk.ukim.finki.imdbclone.service.application.PersonApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PersonDiscoveryControllerTest {

    private final FakePersonApplicationService personApplicationService = new FakePersonApplicationService();
    private final PersonController personController = new PersonController(personApplicationService);

    @Test
    void returnsBornTodayCelebsFromApplicationService() {
        personApplicationService.bornToday = List.of(
                rankedPerson(1L, 1, "Christopher", "Nolan", 3L)
        );

        var response = personController.getBornToday();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(DisplayRankedPersonDto::fullName)
                .containsExactly("Christopher Nolan");
    }

    @Test
    void returnsMostPopularCelebsFromApplicationService() {
        personApplicationService.mostPopular = List.of(
                rankedPerson(1L, 1, "Christopher", "Nolan", 3L),
                rankedPerson(2L, 2, "Vince", "Gilligan", 1L)
        );

        var response = personController.getMostPopularPersons();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(DisplayRankedPersonDto::creditCount)
                .containsExactly(3L, 1L);
    }

    private static DisplayRankedPersonDto rankedPerson(Long id,
                                                       Integer rank,
                                                       String firstName,
                                                       String lastName,
                                                       Long creditCount) {
        return new DisplayRankedPersonDto(
                id,
                rank,
                firstName,
                lastName,
                firstName + " " + lastName,
                null,
                LocalDate.of(1970, 7, 30),
                List.of("Inception"),
                creditCount
        );
    }

    private static class FakePersonApplicationService implements PersonApplicationService {
        private List<DisplayRankedPersonDto> bornToday = List.of();
        private List<DisplayRankedPersonDto> mostPopular = List.of();

        @Override
        public List<DisplayRankedPersonDto> findBornToday() {
            return bornToday;
        }

        @Override
        public List<DisplayRankedPersonDto> findMostPopular() {
            return mostPopular;
        }

        @Override
        public PagedResponseDto<DisplayRankedPersonDto> findMostPopular(int page, int size) {
            return PagedResponseDto.of(mostPopular, page, size, mostPopular.size());
        }

        @Override
        public List<DisplayPersonDto> findAll() {
            return List.of();
        }

        @Override
        public Optional<DisplayPersonDto> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public Optional<DisplayPersonDto> save(CreatePersonDto personDto) {
            return Optional.empty();
        }

        @Override
        public Optional<DisplayPersonDto> update(Long id, CreatePersonDto personDto) {
            return Optional.empty();
        }

        @Override
        public void delete(Long id) {
        }

        @Override
        public List<DisplayPersonDto> search(String name) {
            return List.of();
        }
    }
}
