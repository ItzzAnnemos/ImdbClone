package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import mk.ukim.finki.imdbclone.model.dto.CreateTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayTVSeriesDto;
import mk.ukim.finki.imdbclone.service.application.TVSeriesApplicationService;
import mk.ukim.finki.imdbclone.service.domain.TVSeriesService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}