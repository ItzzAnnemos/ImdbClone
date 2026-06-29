package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreatePersonDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayPersonDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedPersonDto;
import mk.ukim.finki.imdbclone.model.dto.PagedResponseDto;

import java.util.List;
import java.util.Optional;

public interface PersonApplicationService {

    List<DisplayPersonDto> findAll();

    Optional<DisplayPersonDto> findById(Long id);

    Optional<DisplayPersonDto> save(CreatePersonDto personDto);

    Optional<DisplayPersonDto> update(Long id, CreatePersonDto personDto);

    void delete(Long id);

    List<DisplayPersonDto> search(String name);

    /**
     * Find people born on today's month and day and map them to ranked display DTOs.
     *
     * @return List of ranked person DTOs born today
     */
    List<DisplayRankedPersonDto> findBornToday();

    /**
     * Find the most popular people and map them to ranked display DTOs.
     *
     * @return List of up to 250 popular person DTOs
     */
    List<DisplayRankedPersonDto> findMostPopular();

    /**
     * Find a page of the most popular people and map them to ranked display DTOs.
     *
     * @param page zero-based page index
     * @param size requested page size
     * @return paged response containing popular person DTOs
     */
    PagedResponseDto<DisplayRankedPersonDto> findMostPopular(int page, int size);
}
