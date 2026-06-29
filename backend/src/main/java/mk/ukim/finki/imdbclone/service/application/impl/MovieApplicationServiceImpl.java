package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.model.dto.CreateMovieDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayMovieDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedMediaDto;
import mk.ukim.finki.imdbclone.model.dto.PagedResponseDto;
import mk.ukim.finki.imdbclone.service.application.MovieApplicationService;
import mk.ukim.finki.imdbclone.service.domain.MovieService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MovieApplicationServiceImpl
        extends MediaApplicationServiceImpl<Movie, CreateMovieDto, DisplayMovieDto>
        implements MovieApplicationService {

    private final MovieService movieService;

    public MovieApplicationServiceImpl(MovieService movieService) {
        super(movieService, DisplayMovieDto::from, CreateMovieDto::toMovie);
        this.movieService = movieService;
    }

    @Override
    public List<DisplayMovieDto> findByDirector(String director) {
        return movieService.getByDirector(director)
                .stream()
                .map(DisplayMovieDto::from)
                .toList();
    }

    @Override
    public List<DisplayMovieDto> findByYear(Integer year) {
        return movieService.getByYear(year)
                .stream()
                .map(DisplayMovieDto::from)
                .toList();
    }

    @Override
    public List<DisplayMovieDto> findByYearRange(Integer startYear, Integer endYear) {
        return movieService.getByYearRange(startYear, endYear)
                .stream()
                .map(DisplayMovieDto::from)
                .toList();
    }

    @Override
    public List<DisplayMovieDto> findByGenre(String genreName) {
        return movieService.getByGenre(genreName)
                .stream()
                .map(DisplayMovieDto::from)
                .toList();
    }

    @Override
    public List<DisplayRankedMediaDto> findTop250() {
        return toRankedMedia(movieService.getTop250());
    }

    @Override
    public List<DisplayRankedMediaDto> findMostPopular() {
        return toRankedMedia(movieService.getMostPopular());
    }

    @Override
    public PagedResponseDto<DisplayRankedMediaDto> findMostPopular(int page, int size) {
        int normalizedPage = Math.max(0, page);
        int normalizedSize = normalizePageSize(size);
        int offset = normalizedPage * normalizedSize;

        return PagedResponseDto.of(
                toRankedMedia(
                        movieService.getMostPopular(PageRequest.of(normalizedPage, normalizedSize)),
                        offset + 1
                ),
                normalizedPage,
                normalizedSize,
                movieService.count()
        );
    }

    @Override
    public List<DisplayRankedMediaDto> findRankedByGenre(String genreName) {
        return toRankedMedia(movieService.getRankedByGenre(genreName));
    }

    @Override
    public PagedResponseDto<DisplayRankedMediaDto> findRankedByGenre(String genreName, int page, int size) {
        int normalizedPage = Math.max(0, page);
        int normalizedSize = normalizePageSize(size);
        int offset = normalizedPage * normalizedSize;

        return PagedResponseDto.of(
                toRankedMedia(
                        movieService.getRankedByGenre(
                                genreName,
                                PageRequest.of(normalizedPage, normalizedSize)
                        ),
                        offset + 1
                ),
                normalizedPage,
                normalizedSize,
                movieService.countByGenre(genreName)
        );
    }

    private List<DisplayRankedMediaDto> toRankedMedia(List<Movie> movies) {
        return toRankedMedia(movies, 1);
    }

    private List<DisplayRankedMediaDto> toRankedMedia(List<Movie> movies, int startRank) {
        AtomicInteger rank = new AtomicInteger(1);
        return movies.stream()
                .map(movie -> DisplayRankedMediaDto.from(movie, startRank + rank.getAndIncrement() - 1))
                .toList();
    }

    private int normalizePageSize(int size) {
        if (size < 1) {
            return 20;
        }
        return Math.min(size, 100);
    }
}
