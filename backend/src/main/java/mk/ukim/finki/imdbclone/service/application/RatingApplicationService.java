package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreateRatingDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRatingDto;

import java.util.List;
import java.util.Optional;

public interface RatingApplicationService {

    Optional<DisplayRatingDto> save(CreateRatingDto ratingDto);

    void delete(Long userId, Long mediaId);

    Optional<DisplayRatingDto> findByUserAndMedia(Long userId, Long mediaId);

    List<DisplayRatingDto> findByMedia(Long mediaId);

    List<DisplayRatingDto> findByUser(Long userId);

    Double getAverageRating(Long mediaId);

    Long getRatingCount(Long mediaId);
}