package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.CreateTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayTVSeriesDto;

import java.util.List;

public interface TVSeriesApplicationService
        extends MediaApplicationService<CreateTVSeriesDto, DisplayTVSeriesDto> {

    List<DisplayTVSeriesDto> findByStatus(String status);
}