package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.Rating;
import mk.ukim.finki.imdbclone.model.dto.CreateRatingDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRatingDto;
import mk.ukim.finki.imdbclone.service.application.RatingApplicationService;
import mk.ukim.finki.imdbclone.service.domain.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingApplicationServiceImpl implements RatingApplicationService {

    private final RatingService ratingService;

    public RatingApplicationServiceImpl(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public Optional<DisplayRatingDto> save(CreateRatingDto ratingDto) {
        Rating rating = ratingService.rateMedia(
                ratingDto.userId(),
                ratingDto.mediaId(),
                ratingDto.rating()
        );
        return Optional.of(DisplayRatingDto.from(rating));
    }

    @Override
    public void delete(Long userId, Long mediaId) {
        ratingService.deleteRating(userId, mediaId);
    }

    @Override
    public Optional<DisplayRatingDto> findByUserAndMedia(Long userId, Long mediaId) {
        return ratingService.getRating(userId, mediaId)
                .map(DisplayRatingDto::from);
    }

    @Override
    public List<DisplayRatingDto> findByMedia(Long mediaId) {
        return DisplayRatingDto.from(ratingService.getRatingsByMedia(mediaId));
    }

    @Override
    public List<DisplayRatingDto> findByUser(Long userId) {
        return DisplayRatingDto.from(ratingService.getRatingsByUser(userId));
    }

    @Override
    public Double getAverageRating(Long mediaId) {
        return ratingService.getAverageRating(mediaId);
    }

    @Override
    public Long getRatingCount(Long mediaId) {
        return ratingService.getRatingCount(mediaId);
    }
}