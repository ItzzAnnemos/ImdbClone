package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.repository.MovieRepository;
import mk.ukim.finki.imdbclone.service.domain.MovieService;
import mk.ukim.finki.imdbclone.service.domain.helper.MediaSimilarityHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class MovieServiceImpl extends MediaServiceImpl<Movie> implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository,
                            MediaSimilarityHelper mediaSimilarityHelper) {
        super(movieRepository, mediaSimilarityHelper);
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getTopRated() {
        return movieRepository.findTop10ByOrderByAverageRatingDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getRecent() {
        return movieRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public Movie update(Long id, Movie movieDetails) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        movie.setTitle(movieDetails.getTitle());
        movie.setDescription(movieDetails.getDescription());
        movie.setReleaseYear(movieDetails.getReleaseYear());
        movie.setPosterUrl(movieDetails.getPosterUrl());
        movie.setDuration(movieDetails.getDuration());
        return movieRepository.save(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getByDirector(String director) {
        return movieRepository.findByDirectorName(director);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getByYear(Integer year) {
        return movieRepository.findByReleaseYear(year);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getByYearRange(Integer startYear, Integer endYear) {
        return movieRepository.findByReleaseYearBetween(startYear, endYear);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getByGenre(String genreName) {
        return movieRepository.findByGenres_Name(genreName);
    }
}
