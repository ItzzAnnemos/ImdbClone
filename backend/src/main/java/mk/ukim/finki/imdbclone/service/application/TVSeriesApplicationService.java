package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreateTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayTVSeriesDto;

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
     * Find TV series in a genre and map them to ranked display DTOs.
     *
     * @param genreName the genre name
     * @return List of ranked TV series DTOs in that genre
     */
    List<DisplayRankedMediaDto> findRankedByGenre(String genreName);
}
