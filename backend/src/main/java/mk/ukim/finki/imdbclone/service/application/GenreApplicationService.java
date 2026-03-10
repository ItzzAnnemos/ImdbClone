package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreateGenreDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayGenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreApplicationService {

    List<DisplayGenreDto> findAll();

    Optional<DisplayGenreDto> findById(Long id);

    Optional<DisplayGenreDto> findByName(String name);

    Optional<DisplayGenreDto> save(CreateGenreDto genreDto);

    void delete(Long id);

    boolean exists(String name);
}