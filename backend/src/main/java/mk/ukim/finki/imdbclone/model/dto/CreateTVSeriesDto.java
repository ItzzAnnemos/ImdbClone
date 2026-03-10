package mk.ukim.finki.imdbclone.model.dto;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;

import java.util.List;
import java.util.stream.Collectors;

public record CreateTVSeriesDto(
        String title,
        String description,
        Integer releaseYear,
        String posterUrl,
        Integer numberOfSeasons,
        String status
) {

    public static CreateTVSeriesDto from(TVSeries series) {
        return new CreateTVSeriesDto(
                series.getTitle(),
                series.getDescription(),
                series.getReleaseYear(),
                series.getPosterUrl(),
                series.getNumberOfSeasons(),
                series.getStatus()
        );
    }

    public static List<CreateTVSeriesDto> from(List<TVSeries> seriesList) {
        return seriesList.stream()
                .map(CreateTVSeriesDto::from)
                .collect(Collectors.toList());
    }

    public TVSeries toTVSeries() {
        TVSeries series = new TVSeries();
        series.setTitle(title);
        series.setDescription(description);
        series.setReleaseYear(releaseYear);
        series.setPosterUrl(posterUrl);
        series.setNumberOfSeasons(numberOfSeasons);
        series.setStatus(status);
        return series;
    }
}