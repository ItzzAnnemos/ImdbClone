package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.Genre;
import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.Movie;

import java.util.Comparator;
import java.util.List;

public record DisplayRankedMediaDto(
        Long id,
        Integer rank,
        String title,
        String posterUrl,
        Integer releaseYear,
        Double averageRating,
        Long ratingCount,
        List<String> genres,
        String type
) {

    public static DisplayRankedMediaDto from(Media media, int rank) {
        return new DisplayRankedMediaDto(
                media.getId(),
                rank,
                media.getTitle(),
                media.getPosterUrl(),
                media.getReleaseYear(),
                media.getAverageRating(),
                (long) media.getRatings().size(),
                media.getGenres()
                        .stream()
                        .map(Genre::getName)
                        .sorted(Comparator.naturalOrder())
                        .toList(),
                media instanceof Movie ? "movie" : "tv"
        );
    }
}
