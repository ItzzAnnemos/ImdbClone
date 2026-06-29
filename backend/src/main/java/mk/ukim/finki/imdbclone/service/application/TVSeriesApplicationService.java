package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreateTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.PagedResponseDto;

import java.util.List;

public interface TVSeriesApplicationService
        extends MediaApplicationService<CreateTVSeriesDto, DisplayTVSeriesDto> {

    List<DisplayTVSeriesDto> findByStatus(String status);

    /**
     * Find the highest-rated TV series and map them to ranked display DTOs.
     *
     * @return List of up to 250 ranked TV series DTOs
     */
    List<DisplayRankedMediaDto> findTop250();

    /**
     * Find the most popular TV series and map them to ranked display DTOs.
     *
     * @return List of up to 250 popular TV series DTOs
     */
    List<DisplayRankedMediaDto> findMostPopular();

    /**
     * Find a page of most popular TV series and map them to ranked display DTOs.
     *
     * @param page zero-based page index
     * @param size requested page size
     * @return paged response containing popular TV series DTOs
     */
    PagedResponseDto<DisplayRankedMediaDto> findMostPopular(int page, int size);

    /**
     * Find TV series in a genre and map them to ranked display DTOs.
     *
     * @param genreName the genre name
     * @return List of ranked TV series DTOs in that genre
     */
    List<DisplayRankedMediaDto> findRankedByGenre(String genreName);

    /**
     * Find a page of TV series in a genre and map them to ranked display DTOs.
     *
     * @param genreName the genre name
     * @param page      zero-based page index
     * @param size      requested page size
     * @return paged response containing ranked TV series DTOs in that genre
     */
    PagedResponseDto<DisplayRankedMediaDto> findRankedByGenre(String genreName, int page, int size);
}
