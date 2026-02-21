package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.Review;
import mk.ukim.finki.imdbclone.model.domain.User;
import mk.ukim.finki.imdbclone.repository.MediaRepository;
import mk.ukim.finki.imdbclone.repository.ReviewRepository;
import mk.ukim.finki.imdbclone.repository.UserRepository;
import mk.ukim.finki.imdbclone.service.domain.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
            UserRepository userRepository,
            MediaRepository mediaRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Review createReview(Long userId, Long mediaId, String reviewText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + mediaId));

        // Guard: one review per user per media (DB constraint would also catch this,
        // but throwing a clear exception here is preferable to a
        // DataIntegrityViolationException)
        if (reviewRepository.findByUser_IdAndMedia_Id(userId, mediaId).isPresent()) {
            throw new RuntimeException("User " + userId + " has already reviewed media " + mediaId);
        }

        Review review = new Review();
        review.setUser(user);
        review.setMedia(media);
        review.setReviewText(reviewText);
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Long reviewId, String newText) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
        review.setReviewText(newText);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Review not found with id: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReview(Long userId, Long mediaId) {
        return reviewRepository.findByUser_IdAndMedia_Id(userId, mediaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByMedia(Long mediaId) {
        return reviewRepository.findByMedia_Id(mediaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUser_Id(userId);
    }
}
