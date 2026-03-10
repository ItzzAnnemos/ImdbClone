package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.model.dto.CreateMovieDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayMovieDto;
import mk.ukim.finki.imdbclone.service.application.MovieApplicationService;
import mk.ukim.finki.imdbclone.service.domain.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}