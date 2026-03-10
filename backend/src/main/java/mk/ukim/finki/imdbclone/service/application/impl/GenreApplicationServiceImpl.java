package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.Genre;
import mk.ukim.finki.imdbclone.model.dto.CreateGenreDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayGenreDto;
import mk.ukim.finki.imdbclone.service.application.GenreApplicationService;
import mk.ukim.finki.imdbclone.service.domain.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreApplicationServiceImpl implements GenreApplicationService {

    private final GenreService genreService;

    public GenreApplicationServiceImpl(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    public List<DisplayGenreDto> findAll() {
        return DisplayGenreDto.from(genreService.getAllGenres());
    }

    @Override
    public Optional<DisplayGenreDto> findById(Long id) {
        return genreService.getGenreById(id)
                .map(DisplayGenreDto::from);
    }

    @Override
    public Optional<DisplayGenreDto> findByName(String name) {
        return genreService.getGenreByName(name)
                .map(DisplayGenreDto::from);
    }

    @Override
    public Optional<DisplayGenreDto> save(CreateGenreDto genreDto) {
        Genre genre = genreService.createGenre(genreDto.name());
        return Optional.of(DisplayGenreDto.from(genre));
    }

    @Override
    public void delete(Long id) {
        genreService.deleteGenre(id);
    }

    @Override
    public boolean exists(String name) {
        return genreService.genreExists(name);
    }
}