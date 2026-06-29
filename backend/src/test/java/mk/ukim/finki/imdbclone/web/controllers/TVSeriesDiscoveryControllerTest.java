package mk.ukim.finki.imdbclone.web.controllers;

import mk.ukim.finki.imdbclone.model.dto.CreateTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.PagedResponseDto;
import mk.ukim.finki.imdbclone.service.application.TVSeriesApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TVSeriesDiscoveryControllerTest {

    private final FakeTVSeriesApplicationService tvSeriesApplicationService = new FakeTVSeriesApplicationService();
    private final TVSeriesController tvSeriesController = new TVSeriesController(tvSeriesApplicationService, null);

    @Test
    void returnsMostPopularTVSeriesFromApplicationService() {
        tvSeriesApplicationService.mostPopular = List.of(
                rankedMedia(1L, 1, "Breaking Bad", 9.0, 1L),
                rankedMedia(2L, 2, "Severance", 8.0, 1L)
        );

        var response = tvSeriesController.getMostPopularTVSeries();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(DisplayRankedMediaDto::title)
                .containsExactly("Breaking Bad", "Severance");
    }

    @Test
    void returnsRankedTVSeriesByGenreFromApplicationService() {
        tvSeriesApplicationService.byGenre = List.of(rankedMedia(3L, 1, "The Bear", 8.6, 0L));

        var response = tvSeriesController.getRankedTVSeriesByGenre("Drama");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(tvSeriesApplicationService.requestedGenre).isEqualTo("Drama");
    }

    @Test
    void returnsPagedRankedTVSeriesByGenreFromApplicationService() {
        tvSeriesApplicationService.byGenre = List.of(rankedMedia(3L, 51, "The Bear", 8.6, 0L));

        var response = tvSeriesController.getRankedTVSeriesByGenre("Drama", 1, 50);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().page()).isEqualTo(1);
        assertThat(response.getBody().items()).hasSize(1);
        assertThat(tvSeriesApplicationService.requestedGenre).isEqualTo("Drama");
    }

    private static DisplayRankedMediaDto rankedMedia(Long id,
                                                     Integer rank,
                                                     String title,
                                                     Double rating,
                                                     Long ratingCount) {
        return new DisplayRankedMediaDto(
                id,
                rank,
                title,
                null,
                null,
                2022,
                rating,
                ratingCount,
                List.of("Drama"),
                "tv"
        );
    }

    private static class FakeTVSeriesApplicationService implements TVSeriesApplicationService {
        private List<DisplayRankedMediaDto> mostPopular = List.of();
        private List<DisplayRankedMediaDto> byGenre = List.of();
        private String requestedGenre;

        @Override
        public List<DisplayRankedMediaDto> findTop250() {
            return List.of();
        }

        @Override
        public List<DisplayRankedMediaDto> findMostPopular() {
            return mostPopular;
        }

        @Override
        public PagedResponseDto<DisplayRankedMediaDto> findMostPopular(int page, int size) {
            return PagedResponseDto.of(mostPopular, page, size, mostPopular.size());
        }

        @Override
        public List<DisplayRankedMediaDto> findRankedByGenre(String genreName) {
            requestedGenre = genreName;
            return byGenre;
        }

        @Override
        public PagedResponseDto<DisplayRankedMediaDto> findRankedByGenre(String genreName, int page, int size) {
            requestedGenre = genreName;
            return PagedResponseDto.of(byGenre, page, size, byGenre.size());
        }

        @Override
        public List<DisplayTVSeriesDto> findAll() {
            return List.of();
        }

        @Override
        public Optional<DisplayTVSeriesDto> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public Optional<DisplayTVSeriesDto> save(CreateTVSeriesDto createTVSeriesDto) {
            return Optional.empty();
        }

        @Override
        public Optional<DisplayTVSeriesDto> update(Long id, CreateTVSeriesDto createTVSeriesDto) {
            return Optional.empty();
        }

        @Override
        public void delete(Long id) {
        }

        @Override
        public List<DisplayTVSeriesDto> findTopRated() {
            return List.of();
        }

        @Override
        public List<DisplayTVSeriesDto> findRecent() {
            return List.of();
        }

        @Override
        public List<mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto> findSimilar(Long id) {
            return List.of();
        }

        @Override
        public List<DisplayTVSeriesDto> findByStatus(String status) {
            return List.of();
        }
    }
}
