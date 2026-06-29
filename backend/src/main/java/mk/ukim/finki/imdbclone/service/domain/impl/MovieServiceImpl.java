package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.model.domain.Movie;
import mk.ukim.finki.imdbclone.repository.MovieRepository;
import mk.ukim.finki.imdbclone.service.domain.MovieService;
import mk.ukim.finki.imdbclone.service.domain.helper.MediaSimilarityHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MovieServiceImpl extends MediaServiceImpl<Movie> implements MovieService {

    private static final int DEFAULT_CHART_LIMIT = 250;

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
        movie.setTrailerUrl(movieDetails.getTrailerUrl());
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

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getTop250() {
        return getTop250(PageRequest.of(0, DEFAULT_CHART_LIMIT));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getTop250(Pageable pageable) {
        return movieRepository.findAllByOrderByAverageRatingDescReleaseYearDescTitleAsc(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getMostPopular() {
        return getMostPopular(PageRequest.of(0, DEFAULT_CHART_LIMIT));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getMostPopular(Pageable pageable) {
        return movieRepository.findMostPopularRanked(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getRankedByGenre(String genreName) {
        return movieRepository.findByGenres_Name(genreName)
                .stream()
                .sorted(topRatedComparator())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getRankedByGenre(String genreName, Pageable pageable) {
        return movieRepository.findByGenres_NameOrderByAverageRatingDescReleaseYearDescTitleAsc(genreName, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByGenre(String genreName) {
        return movieRepository.countByGenres_Name(genreName);
    }

    private Comparator<Movie> topRatedComparator() {
        return Comparator
                .comparing((Movie movie) -> valueOrZero(movie.getAverageRating())).reversed()
                .thenComparing((Movie movie) -> movie.getRatings().size(), Comparator.reverseOrder())
                .thenComparing((Movie movie) -> valueOrZero(movie.getReleaseYear()), Comparator.reverseOrder())
                .thenComparing(Movie::getTitle);
    }

    private Comparator<Movie> popularComparator() {
        return Comparator
                .comparing((Movie movie) -> movie.getRatings().size(), Comparator.reverseOrder())
                .thenComparing((Movie movie) -> valueOrZero(movie.getAverageRating()), Comparator.reverseOrder())
                .thenComparing((Movie movie) -> valueOrZero(movie.getReleaseYear()), Comparator.reverseOrder())
                .thenComparing(Movie::getTitle);
    }

    private Double valueOrZero(Double value) {
        return value == null ? 0.0 : value;
    }

    private Integer valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return movieRepository.count();
    }
}
