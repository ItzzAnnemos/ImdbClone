package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.model.domain.Genre;
import mk.ukim.finki.imdbclone.repository.GenreRepository;
import mk.ukim.finki.imdbclone.service.domain.GenreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> getGenreByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public Genre createGenre(String name) {
        if (genreRepository.existsByName(name)) {
            throw new RuntimeException("Genre already exists: " + name);
        }
        Genre genre = new Genre();
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new RuntimeException("Genre not found with id: " + id);
        }
        genreRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean genreExists(String name) {
        return genreRepository.existsByName(name);
    }
}
