package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.Movie;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Movie-specific operations.
 * Extends the generic {@link MediaService} base interface with Movie CRUD and
 * discovery,
 * and adds movie-specific queries.
 */
public interface MovieService extends MediaService<Movie> {

    /**
     * Retrieve all movies.
     * Delegates to {@link MediaService#getAll()}.
     *
     * @return List of all movies
     */
    default List<Movie> getAllMovies() {
        return getAll();
    }

    /**
     * Find a movie by its ID.
     * Delegates to {@link MediaService#getById(Long)}.
     *
     * @param id the movie ID
     * @return Optional containing the movie if found
     */
    default Optional<Movie> getMovieById(Long id) {
        return getById(id);
    }

    /**
     * Persist a new movie.
     * Delegates to {@link MediaService#create(Object)}.
     *
     * @param movie the movie to create
     * @return the saved movie with generated ID
     */
    default Movie createMovie(Movie movie) {
        return create(movie);
    }

    /**
     * Update an existing movie.
     * Delegates to {@link MediaService#update(Long, Object)}.
     *
     * @param id    the ID of the movie to update
     * @param movie the updated movie details
     * @return the updated movie
     */
    default Movie updateMovie(Long id, Movie movie) {
        return update(id, movie);
    }

    /**
     * Delete a movie by its ID.
     * Delegates to {@link MediaService#delete(Long)}.
     *
     * @param id the movie ID
     */
    default void deleteMovie(Long id) {
        delete(id);
    }

    /**
     * Search for movies by title (case-insensitive partial match).
     * Delegates to {@link MediaService#search(String)}.
     *
     * @param title the search term
     * @return List of matching movies
     */
    default List<Movie> searchMovies(String title) {
        return search(title);
    }

    // ── Movie-specific queries ──

    /**
     * Find movies by director name (case-insensitive partial match)
     *
     * @param director the director name search term
     * @return List of matching movies
     */
    List<Movie> getByDirector(String director);

    /**
     * Find movies released in a specific year
     *
     * @param year the release year
     * @return List of movies from that year
     */
    List<Movie> getByYear(Integer year);

    /**
     * Find movies released within a year range (inclusive)
     *
     * @param startYear the start of the range
     * @param endYear   the end of the range
     * @return List of movies in the year range
     */
    List<Movie> getByYearRange(Integer startYear, Integer endYear);

    /**
     * Find movies belonging to a specific genre
     *
     * @param genreName the genre name
     * @return List of movies in that genre
     */
    List<Movie> getByGenre(String genreName);
}
