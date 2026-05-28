package mk.ukim.finki.imdbclone.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.CreateReviewDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayReviewDto;
import mk.ukim.finki.imdbclone.service.application.ReviewApplicationService;
import mk.ukim.finki.imdbclone.web.helpers.ControllerAuthorizationHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    private final ReviewApplicationService reviewApplicationService;
    private final ControllerAuthorizationHelper authorizationHelper;

    @PostMapping("/add")
    public ResponseEntity<DisplayReviewDto> createReview(@RequestBody CreateReviewDto reviewDto,
                                                         Authentication authentication) {
        if (!authorizationHelper.isAuthenticatedUserId(reviewDto.userId(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return reviewApplicationService.save(reviewDto)
                .map(review -> ResponseEntity.status(HttpStatus.CREATED).body(review))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/edit/{reviewId}")
    public ResponseEntity<DisplayReviewDto> updateReview(
            @PathVariable Long reviewId,
            @RequestBody CreateReviewDto reviewDto,
            Authentication authentication) {
        Optional<DisplayReviewDto> existingReview = reviewApplicationService.findById(reviewId);
        if (existingReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!authorizationHelper.isAuthenticatedUsername(existingReview.get().username(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return reviewApplicationService.update(reviewId, reviewDto.reviewText())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId,
                                             Authentication authentication) {
        Optional<DisplayReviewDto> existingReview = reviewApplicationService.findById(reviewId);
        if (existingReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!authorizationHelper.isAuthenticatedUsername(existingReview.get().username(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        reviewApplicationService.delete(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user-media")
    public ResponseEntity<DisplayReviewDto> getReview(@RequestParam Long userId,
                                                      @RequestParam Long mediaId,
                                                      Authentication authentication) {
        if (!authorizationHelper.isAuthenticatedUserId(userId, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<DisplayReviewDto> review =
                reviewApplicationService.findByUserAndMedia(userId, mediaId);

        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<DisplayReviewDto> getReviewById(@PathVariable Long reviewId) {
        return reviewApplicationService.findById(reviewId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/media/{mediaId}")
    public ResponseEntity<List<DisplayReviewDto>> getReviewsByMedia(@PathVariable Long mediaId) {
        return ResponseEntity.ok(reviewApplicationService.findByMedia(mediaId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DisplayReviewDto>> getReviewsByUser(@PathVariable Long userId,
                                                                   Authentication authentication) {
        if (!authorizationHelper.isAuthenticatedUserId(userId, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(reviewApplicationService.findByUser(userId));
    }
}
