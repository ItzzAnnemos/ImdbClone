package mk.ukim.finki.imdbclone.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.CreateReviewDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayReviewDto;
import mk.ukim.finki.imdbclone.service.application.ReviewApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    private final ReviewApplicationService reviewApplicationService;

    @PostMapping("/add")
    public ResponseEntity<DisplayReviewDto> createReview(@RequestBody CreateReviewDto reviewDto) {
        return reviewApplicationService.save(reviewDto)
                .map(review -> ResponseEntity.status(HttpStatus.CREATED).body(review))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/edit/{reviewId}")
    public ResponseEntity<DisplayReviewDto> updateReview(
            @PathVariable Long reviewId,
            @RequestBody CreateReviewDto reviewDto) {

        return reviewApplicationService.update(reviewId, reviewDto.reviewText())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewApplicationService.delete(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user-media")
    public ResponseEntity<DisplayReviewDto> getReview(@RequestParam Long userId,
                                                      @RequestParam Long mediaId) {
        Optional<DisplayReviewDto> review =
                reviewApplicationService.findByUserAndMedia(userId, mediaId);

        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
    public ResponseEntity<List<DisplayReviewDto>> getReviewsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewApplicationService.findByUser(userId));
    }
}