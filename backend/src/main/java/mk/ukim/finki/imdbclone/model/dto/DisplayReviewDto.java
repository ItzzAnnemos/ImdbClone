package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DisplayReviewDto(
        Long id,
        String username,
        String mediaTitle,
        String reviewText,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DisplayReviewDto from(Review review) {
        LocalDateTime updated = null;

        if (review.getUpdatedAt() != null &&
                !review.getUpdatedAt().equals(review.getCreatedAt())) {
            updated = review.getUpdatedAt();
        }

        return new DisplayReviewDto(
                review.getId(),
                review.getUser().getUsername(),
                review.getMedia().getTitle(),
                review.getReviewText(),
                review.getCreatedAt(),
                updated
        );
    }

    public static List<DisplayReviewDto> from(List<Review> reviews) {
        return reviews.stream()
                .map(DisplayReviewDto::from)
                .collect(Collectors.toList());
    }
}