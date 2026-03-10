package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreateReviewDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewApplicationService {

    Optional<DisplayReviewDto> save(CreateReviewDto reviewDto);

    Optional<DisplayReviewDto> update(Long reviewId, String newText);

    void delete(Long reviewId);

    Optional<DisplayReviewDto> findByUserAndMedia(Long userId, Long mediaId);

    Optional<DisplayReviewDto> findById(Long reviewId);

    List<DisplayReviewDto> findByMedia(Long mediaId);

    List<DisplayReviewDto> findByUser(Long userId);
}