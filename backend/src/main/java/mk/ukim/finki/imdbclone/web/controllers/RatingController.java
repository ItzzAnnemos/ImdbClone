package mk.ukim.finki.imdbclone.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.CreateRatingDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRatingDto;
import mk.ukim.finki.imdbclone.service.application.RatingApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RatingController {

    private final RatingApplicationService ratingApplicationService;

    @GetMapping("/by-user-media")
    public ResponseEntity<DisplayRatingDto> getRating(@RequestParam Long userId,
                                                      @RequestParam Long mediaId) {
        Optional<DisplayRatingDto> rating =
                ratingApplicationService.findByUserAndMedia(userId, mediaId);

        return rating.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<DisplayRatingDto> rateMedia(@RequestBody CreateRatingDto ratingDto) {
        return ratingApplicationService.save(ratingDto)
                .map(rating -> ResponseEntity.status(HttpStatus.CREATED).body(rating))
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteRating(@RequestParam Long userId,
                                             @RequestParam Long mediaId) {
        ratingApplicationService.delete(userId, mediaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/media/{mediaId}")
    public ResponseEntity<List<DisplayRatingDto>> getRatingsByMedia(@PathVariable Long mediaId) {
        return ResponseEntity.ok(ratingApplicationService.findByMedia(mediaId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DisplayRatingDto>> getRatingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ratingApplicationService.findByUser(userId));
    }

    @GetMapping("/media/{mediaId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long mediaId) {
        return ResponseEntity.ok(ratingApplicationService.getAverageRating(mediaId));
    }

    @GetMapping("/media/{mediaId}/count")
    public ResponseEntity<Long> getRatingCount(@PathVariable Long mediaId) {
        return ResponseEntity.ok(ratingApplicationService.getRatingCount(mediaId));
    }
}