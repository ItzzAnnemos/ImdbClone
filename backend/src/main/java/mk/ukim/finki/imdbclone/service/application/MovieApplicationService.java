package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreateMovieDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayMovieDto;

import java.util.List;

public interface MovieApplicationService
        extends MediaApplicationService<CreateMovieDto, DisplayMovieDto> {

    List<DisplayMovieDto> findByDirector(String director);

    List<DisplayMovieDto> findByYear(Integer year);

    List<DisplayMovieDto> findByYearRange(Integer startYear, Integer endYear);

    List<DisplayMovieDto> findByGenre(String genreName);
}