package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.Review;
import mk.ukim.finki.imdbclone.model.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public record CreateReviewDto(
        Long userId,
        Long mediaId,
        String reviewText
) {

    public static CreateReviewDto from(Review review) {
        return new CreateReviewDto(
                review.getUser().getId(),
                review.getMedia().getId(),
                review.getReviewText()
        );
    }

    public static List<CreateReviewDto> from(List<Review> reviews) {
        return reviews.stream()
                .map(CreateReviewDto::from)
                .collect(Collectors.toList());
    }

    public Review toReview(User user, Media media) {
        Review review = new Review();
        review.setUser(user);
        review.setMedia(media);
        review.setReviewText(reviewText);
        return review;
    }
}