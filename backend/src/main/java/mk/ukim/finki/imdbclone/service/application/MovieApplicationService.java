package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreateMovieDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayMovieDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedMediaDto;
import mk.ukim.finki.imdbclone.model.dto.PagedResponseDto;

import java.util.List;

public interface MovieApplicationService
        extends MediaApplicationService<CreateMovieDto, DisplayMovieDto> {

    List<DisplayMovieDto> findByDirector(String director);

    List<DisplayMovieDto> findByYear(Integer year);

    List<DisplayMovieDto> findByYearRange(Integer startYear, Integer endYear);

    List<DisplayMovieDto> findByGenre(String genreName);

    /**
     * Find the highest-rated movies and map them to ranked display DTOs.
     *
     * @return List of up to 250 ranked movie DTOs
     */
    List<DisplayRankedMediaDto> findTop250();

    /**
     * Find the most popular movies and map them to ranked display DTOs.
     *
     * @return List of up to 250 popular movie DTOs
     */
    List<DisplayRankedMediaDto> findMostPopular();

    /**
     * Find a page of most popular movies and map them to ranked display DTOs.
     *
     * @param page zero-based page index
     * @param size requested page size
     * @return paged response containing popular movie DTOs
     */
    PagedResponseDto<DisplayRankedMediaDto> findMostPopular(int page, int size);

    /**
     * Find movies in a genre and map them to ranked display DTOs.
     *
     * @param genreName the genre name
     * @return List of ranked movie DTOs in that genre
     */
    List<DisplayRankedMediaDto> findRankedByGenre(String genreName);

    /**
     * Find a page of movies in a genre and map them to ranked display DTOs.
     *
     * @param genreName the genre name
     * @param page      zero-based page index
     * @param size      requested page size
     * @return paged response containing ranked movie DTOs in that genre
     */
    PagedResponseDto<DisplayRankedMediaDto> findRankedByGenre(String genreName, int page, int size);
}
