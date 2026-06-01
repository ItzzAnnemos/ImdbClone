package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import mk.ukim.finki.imdbclone.model.dto.CreateTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayTVSeriesDto;
import mk.ukim.finki.imdbclone.service.application.TVSeriesApplicationService;
import mk.ukim.finki.imdbclone.service.domain.TVSeriesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TVSeriesApplicationServiceImpl
        extends MediaApplicationServiceImpl<TVSeries, CreateTVSeriesDto, DisplayTVSeriesDto>
        implements TVSeriesApplicationService {

    private final TVSeriesService tvSeriesService;

    public TVSeriesApplicationServiceImpl(TVSeriesService tvSeriesService) {
        super(tvSeriesService, DisplayTVSeriesDto::from, CreateTVSeriesDto::toTVSeries);
        this.tvSeriesService = tvSeriesService;
    }

    @Override
    public List<DisplayTVSeriesDto> findByStatus(String status) {
        return tvSeriesService.getByStatus(status)
                .stream()
                .map(DisplayTVSeriesDto::from)
                .toList();
    }

    @Override
    public List<DisplayRankedMediaDto> findTop250() {
        return toRankedMedia(tvSeriesService.getTop250());
    }

    @Override
    public List<DisplayRankedMediaDto> findMostPopular() {
        return toRankedMedia(tvSeriesService.getMostPopular());
    }

    @Override
    public List<DisplayRankedMediaDto> findRankedByGenre(String genreName) {
        return toRankedMedia(tvSeriesService.getRankedByGenre(genreName));
    }

    private List<DisplayRankedMediaDto> toRankedMedia(List<TVSeries> tvSeries) {
        AtomicInteger rank = new AtomicInteger(1);
        return tvSeries.stream()
                .map(series -> DisplayRankedMediaDto.from(series, rank.getAndIncrement()))
                .toList();
    }
}
