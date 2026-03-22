package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;

import java.util.List;
import java.util.stream.Collectors;

public record DisplayTVSeriesDto(
        Long id,
        String title,
        String description,
        Integer releaseYear,
        String posterUrl,
        Double averageRating,
        Integer numberOfSeasons,
        String status
) {

    public static DisplayTVSeriesDto from(TVSeries series) {
        return new DisplayTVSeriesDto(
                series.getId(),
                series.getTitle(),
                series.getDescription(),
                series.getReleaseYear(),
                series.getPosterUrl(),
                series.getAverageRating(),
                series.getNumberOfSeasons(),
                series.getStatus()
        );
    }

    public static List<DisplayTVSeriesDto> from(List<TVSeries> seriesList) {
        return seriesList.stream()
                .map(DisplayTVSeriesDto::from)
                .collect(Collectors.toList());
    }
}