package mk.ukim.finki.imdbclone.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.CreateTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayCardMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayTVSeriesDto;
import mk.ukim.finki.imdbclone.service.application.TVSeriesApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tv-series")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TVSeriesController {

    private final TVSeriesApplicationService tvSeriesApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayTVSeriesDto>> getAllTVSeries() {
        return ResponseEntity.ok(tvSeriesApplicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayTVSeriesDto> getTVSeriesById(@PathVariable Long id) {
        Optional<DisplayTVSeriesDto> tvSeries = tvSeriesApplicationService.findById(id);
        return tvSeries.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<DisplayTVSeriesDto> createTVSeries(@RequestBody CreateTVSeriesDto tvSeriesDto) {
        return tvSeriesApplicationService.save(tvSeriesDto)
                .map(series -> ResponseEntity.status(HttpStatus.CREATED).body(series))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<DisplayTVSeriesDto> updateTVSeries(
            @PathVariable Long id,
            @RequestBody CreateTVSeriesDto tvSeriesDto) {
        return tvSeriesApplicationService.update(id, tvSeriesDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTVSeries(@PathVariable Long id) {
        tvSeriesApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<DisplayTVSeriesDto>> searchTVSeries(@RequestParam String title) {
        return ResponseEntity.ok(tvSeriesApplicationService.search(title));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<DisplayTVSeriesDto>> getTopRatedTVSeries() {
        return ResponseEntity.ok(tvSeriesApplicationService.findTopRated());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<DisplayTVSeriesDto>> getRecentTVSeries() {
        return ResponseEntity.ok(tvSeriesApplicationService.findRecent());
    }

    @GetMapping("/status")
    public ResponseEntity<List<DisplayTVSeriesDto>> getTVSeriesByStatus(@RequestParam String status) {
        return ResponseEntity.ok(tvSeriesApplicationService.findByStatus(status));
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<List<DisplayCardMediaDto>> findSimilar(@PathVariable Long id) {
        return ResponseEntity.ok(tvSeriesApplicationService.findSimilar(id));
    }
}