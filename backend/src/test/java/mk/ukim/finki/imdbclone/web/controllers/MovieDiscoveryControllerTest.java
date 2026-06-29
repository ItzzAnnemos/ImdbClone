package mk.ukim.finki.imdbclone.web.controllers;

import mk.ukim.finki.imdbclone.model.dto.CreateMovieDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayMovieDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedMediaDto;
import mk.ukim.finki.imdbclone.model.dto.PagedResponseDto;
import mk.ukim.finki.imdbclone.service.application.MovieApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MovieDiscoveryControllerTest {

    private final FakeMovieApplicationService movieApplicationService = new FakeMovieApplicationService();
    private final MovieController movieController = new MovieController(movieApplicationService, null);

    @Test
    void returnsTop250MoviesFromApplicationService() {
        movieApplicationService.top250 = List.of(
                rankedMedia(1L, 1, "The Godfather", 10.0, 1L),
                rankedMedia(2L, 2, "Inception", 9.0, 2L)
        );

        var response = movieController.getTop250Movies();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(DisplayRankedMediaDto::rank).containsExactly(1, 2);
        assertThat(response.getBody()).extracting(DisplayRankedMediaDto::title)
                .containsExactly("The Godfather", "Inception");
    }

    @Test
    void returnsRankedMoviesByGenreFromApplicationService() {
        movieApplicationService.byGenre = List.of(rankedMedia(3L, 1, "Arrival", 8.2, 3L));

        var response = movieController.getRankedMoviesByGenre("Sci-Fi");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(movieApplicationService.requestedGenre).isEqualTo("Sci-Fi");
    }

    @Test
    void returnsPagedRankedMoviesByGenreFromApplicationService() {
        movieApplicationService.byGenre = List.of(rankedMedia(3L, 51, "Arrival", 8.2, 3L));

        var response = movieController.getRankedMoviesByGenre("Sci-Fi", 1, 50);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().page()).isEqualTo(1);
        assertThat(response.getBody().items()).hasSize(1);
        assertThat(movieApplicationService.requestedGenre).isEqualTo("Sci-Fi");
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
                2020,
                rating,
                ratingCount,
                List.of("Drama"),
                "movie"
        );
    }

    private static class FakeMovieApplicationService implements MovieApplicationService {
        private List<DisplayRankedMediaDto> top250 = List.of();
        private List<DisplayRankedMediaDto> byGenre = List.of();
        private String requestedGenre;

        @Override
        public List<DisplayRankedMediaDto> findTop250() {
            return top250;
        }

        @Override
        public List<DisplayRankedMediaDto> findMostPopular() {
            return List.of();
        }

        @Override
        public PagedResponseDto<DisplayRankedMediaDto> findMostPopular(int page, int size) {
            return PagedResponseDto.of(List.of(), page, size, 0);
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
        public List<DisplayMovieDto> findAll() {
            return List.of();
        }

        @Override
        public Optional<DisplayMovieDto> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public Optional<DisplayMovieDto> save(CreateMovieDto createMovieDto) {
            return Optional.empty();
        }

        @Override
        public Optional<DisplayMovieDto> update(Long id, CreateMovieDto createMovieDto) {
            return Optional.empty();
        }

        @Override
        public void delete(Long id) {
        }

        @Override
        public List<DisplayMovieDto> findTopRated() {
            return List.of();
        }

        @Override
        public List<DisplayMovieDto> findRecent() {
            return List.of();
        }

        @Override
        public List<mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto> findSimilar(Long id) {
            return List.of();
        }

        @Override
        public List<DisplayMovieDto> findByDirector(String director) {
            return List.of();
        }

        @Override
        public List<DisplayMovieDto> findByYear(Integer year) {
            return List.of();
        }

        @Override
        public List<DisplayMovieDto> findByYearRange(Integer startYear, Integer endYear) {
            return List.of();
        }

        @Override
        public List<DisplayMovieDto> findByGenre(String genreName) {
            return List.of();
        }
    }
}
