package mk.ukim.finki.imdbclone.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.CreateMovieDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayMovieDto;
import mk.ukim.finki.imdbclone.model.dto.SearchResultDto;
import mk.ukim.finki.imdbclone.service.application.MovieApplicationService;
import mk.ukim.finki.imdbclone.service.application.SearchApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    private final MovieApplicationService movieApplicationService;
    private final SearchApplicationService searchApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayMovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieApplicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayMovieDto> getMovieById(@PathVariable Long id) {
        Optional<DisplayMovieDto> movie = movieApplicationService.findById(id);
        return movie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<DisplayMovieDto> createMovie(@RequestBody CreateMovieDto movieDto) {
        return movieApplicationService.save(movieDto)
                .map(movie -> ResponseEntity.status(HttpStatus.CREATED).body(movie))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<DisplayMovieDto> updateMovie(
            @PathVariable Long id,
            @RequestBody CreateMovieDto movieDto) {

        return movieApplicationService.update(id, movieDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/search")
//    public ResponseEntity<SearchResultDto> searchMovies(@RequestParam String title) {
//        return ResponseEntity.ok(searchApplicationService.search(title));
//    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<DisplayMovieDto>> getTopRatedMovies() {
        return ResponseEntity.ok(movieApplicationService.findTopRated());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<DisplayMovieDto>> getRecentMovies() {
        return ResponseEntity.ok(movieApplicationService.findRecent());
    }

    @GetMapping("/director")
    public ResponseEntity<List<DisplayMovieDto>> getMoviesByDirector(@RequestParam String director) {
        return ResponseEntity.ok(movieApplicationService.findByDirector(director));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<DisplayMovieDto>> getMoviesByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(movieApplicationService.findByYear(year));
    }

    @GetMapping("/year-range")
    public ResponseEntity<List<DisplayMovieDto>> getMoviesByYearRange(
            @RequestParam Integer startYear,
            @RequestParam Integer endYear) {

        return ResponseEntity.ok(
                movieApplicationService.findByYearRange(startYear, endYear)
        );
    }

    @GetMapping("/genre/{genreName}")
    public ResponseEntity<List<DisplayMovieDto>> getMoviesByGenre(@PathVariable String genreName) {
        return ResponseEntity.ok(movieApplicationService.findByGenre(genreName));
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<List<DisplayCardMediaDto>> findSimilar(@PathVariable Long id) {
        return ResponseEntity.ok(movieApplicationService.findSimilar(id));
    }
}