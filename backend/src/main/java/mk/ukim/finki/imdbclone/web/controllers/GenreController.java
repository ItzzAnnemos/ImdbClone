package mk.ukim.finki.imdbclone.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.CreateGenreDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayGenreDto;
import mk.ukim.finki.imdbclone.service.application.GenreApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class GenreController {

    private final GenreApplicationService genreApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayGenreDto>> getAllGenres() {
        return ResponseEntity.ok(genreApplicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayGenreDto> getGenreById(@PathVariable Long id) {
        Optional<DisplayGenreDto> genre = genreApplicationService.findById(id);
        return genre.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-name")
    public ResponseEntity<DisplayGenreDto> getGenreByName(@RequestParam String name) {
        Optional<DisplayGenreDto> genre = genreApplicationService.findByName(name);
        return genre.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<DisplayGenreDto> createGenre(@RequestBody CreateGenreDto genreDto) {
        return genreApplicationService.save(genreDto)
                .map(genre -> ResponseEntity.status(HttpStatus.CREATED).body(genre))
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> genreExists(@RequestParam String name) {
        return ResponseEntity.ok(genreApplicationService.exists(name));
    }
}