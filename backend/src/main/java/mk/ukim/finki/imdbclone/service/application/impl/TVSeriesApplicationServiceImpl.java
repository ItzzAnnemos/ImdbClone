package mk.ukim.finki.imdbclone.service.application.impl;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import mk.ukim.finki.imdbclone.model.dto.CreateTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayRankedMediaDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayTVSeriesDto;
import mk.ukim.finki.imdbclone.model.dto.PagedResponseDto;
import mk.ukim.finki.imdbclone.service.application.TVSeriesApplicationService;
import mk.ukim.finki.imdbclone.service.domain.TVSeriesService;
import org.springframework.data.domain.PageRequest;
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
    public PagedResponseDto<DisplayRankedMediaDto> findMostPopular(int page, int size) {
        int normalizedPage = Math.max(0, page);
        int normalizedSize = normalizePageSize(size);
        int offset = normalizedPage * normalizedSize;

        return PagedResponseDto.of(
                toRankedMedia(
                        tvSeriesService.getMostPopular(PageRequest.of(normalizedPage, normalizedSize)),
                        offset + 1
                ),
                normalizedPage,
                normalizedSize,
                tvSeriesService.count()
        );
    }

    @Override
    public List<DisplayRankedMediaDto> findRankedByGenre(String genreName) {
        return toRankedMedia(tvSeriesService.getRankedByGenre(genreName));
    }

    @Override
    public PagedResponseDto<DisplayRankedMediaDto> findRankedByGenre(String genreName, int page, int size) {
        int normalizedPage = Math.max(0, page);
        int normalizedSize = normalizePageSize(size);
        int offset = normalizedPage * normalizedSize;

        return PagedResponseDto.of(
                toRankedMedia(
                        tvSeriesService.getRankedByGenre(
                                genreName,
                                PageRequest.of(normalizedPage, normalizedSize)
                        ),
                        offset + 1
                ),
                normalizedPage,
                normalizedSize,
                tvSeriesService.countByGenre(genreName)
        );
    }

    private List<DisplayRankedMediaDto> toRankedMedia(List<TVSeries> tvSeries) {
        return toRankedMedia(tvSeries, 1);
    }

    private List<DisplayRankedMediaDto> toRankedMedia(List<TVSeries> tvSeries, int startRank) {
        AtomicInteger rank = new AtomicInteger(1);
        return tvSeries.stream()
                .map(series -> DisplayRankedMediaDto.from(series, startRank + rank.getAndIncrement() - 1))
                .toList();
    }

    private int normalizePageSize(int size) {
        if (size < 1) {
            return 20;
        }
        return Math.min(size, 100);
    }
}
