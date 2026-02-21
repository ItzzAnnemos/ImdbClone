package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.Rating;
import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.repository.MediaRepository;
import mk.ukim.finki.imdbclone.repository.RatingRepository;
import mk.ukim.finki.imdbclone.repository.UserRepository;
import mk.ukim.finki.imdbclone.service.domain.RatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;

    public RatingServiceImpl(RatingRepository ratingRepository,
            MediaRepository mediaRepository,
            UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Rating rateMedia(Long userId, Long mediaId, Integer rating) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + mediaId));

        // Upsert: update existing rating if present, otherwise create a new one
        Rating existingRating = ratingRepository.findByUser_IdAndMedia_Id(userId, mediaId)
                .orElseGet(() -> {
                    Rating r = new Rating();
                    r.setUser(user);
                    r.setMedia(media);
                    return r;
                });

        existingRating.setRating(rating);
        Rating saved = ratingRepository.save(existingRating);

        syncAverageRating(media);

        return saved;
    }

    @Override
    public void deleteRating(Long userId, Long mediaId) {
        Rating rating = ratingRepository.findByUser_IdAndMedia_Id(userId, mediaId)
                .orElseThrow(() -> new RuntimeException(
                        "Rating not found for userId=" + userId + ", mediaId=" + mediaId));

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + mediaId));

        ratingRepository.delete(rating);
        syncAverageRating(media);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rating> getRating(Long userId, Long mediaId) {
        return ratingRepository.findByUser_IdAndMedia_Id(userId, mediaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rating> getRatingsByMedia(Long mediaId) {
        return ratingRepository.findByMedia_Id(mediaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rating> getRatingsByUser(Long userId) {
        return ratingRepository.findByUser_Id(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating(Long mediaId) {
        return ratingRepository.findAverageRatingByMediaId(mediaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getRatingCount(Long mediaId) {
        return ratingRepository.countByMedia_Id(mediaId);
    }

    /**
     * Recalculates and persists {@code Media.averageRating} after any mutation.
     */
    private void syncAverageRating(Media media) {
        Double avg = ratingRepository.findAverageRatingByMediaId(media.getId());
        media.setAverageRating(avg);
        mediaRepository.save(media);
    }
}
