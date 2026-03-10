package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.Review;
import mk.ukim.finki.imdbclone.model.dto.CreateReviewDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayReviewDto;
import mk.ukim.finki.imdbclone.service.application.ReviewApplicationService;
import mk.ukim.finki.imdbclone.service.domain.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewApplicationServiceImpl implements ReviewApplicationService {

    private final ReviewService reviewService;

    public ReviewApplicationServiceImpl(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public Optional<DisplayReviewDto> save(CreateReviewDto reviewDto) {
        Review review = reviewService.createReview(
                reviewDto.userId(),
                reviewDto.mediaId(),
                reviewDto.reviewText()
        );
        return Optional.of(DisplayReviewDto.from(review));
    }

    @Override
    public Optional<DisplayReviewDto> update(Long reviewId, String newText) {
        return Optional.of(
                DisplayReviewDto.from(reviewService.updateReview(reviewId, newText))
        );
    }

    @Override
    public void delete(Long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @Override
    public Optional<DisplayReviewDto> findByUserAndMedia(Long userId, Long mediaId) {
        return reviewService.getReview(userId, mediaId)
                .map(DisplayReviewDto::from);
    }

    @Override
    public Optional<DisplayReviewDto> findById(Long reviewId) {
        return Optional.of(
                DisplayReviewDto.from(reviewService.getReviewById(reviewId))
        );
    }

    @Override
    public List<DisplayReviewDto> findByMedia(Long mediaId) {
        return DisplayReviewDto.from(reviewService.getReviewsByMedia(mediaId));
    }

    @Override
    public List<DisplayReviewDto> findByUser(Long userId) {
        return DisplayReviewDto.from(reviewService.getReviewsByUser(userId));
    }
}