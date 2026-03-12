package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import mk.ukim.finki.imdbclone.repository.TVSeriesRepository;
import mk.ukim.finki.imdbclone.service.domain.TVSeriesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mk.ukim.finki.imdbclone.service.domain.helper.MediaSimilarityHelper;

import java.util.List;

@Slf4j
@Service
@Transactional
public class TVSeriesServiceImpl extends MediaServiceImpl<TVSeries> implements TVSeriesService {

    private final TVSeriesRepository tvSeriesRepository;

    public TVSeriesServiceImpl(TVSeriesRepository tvSeriesRepository,
                               MediaSimilarityHelper mediaSimilarityHelper) {
        super(tvSeriesRepository, mediaSimilarityHelper);
        this.tvSeriesRepository = tvSeriesRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> search(String title) {
        return tvSeriesRepository.findAllByTitleContainingIgnoreCase(title);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getTopRated() {
        return tvSeriesRepository.findTop10ByOrderByAverageRatingDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getRecent() {
        return tvSeriesRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public TVSeries update(Long id, TVSeries tvSeriesDetails) {
        TVSeries tvSeries = tvSeriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TV Series not found with id: " + id));
        tvSeries.setTitle(tvSeriesDetails.getTitle());
        tvSeries.setDescription(tvSeriesDetails.getDescription());
        tvSeries.setReleaseYear(tvSeriesDetails.getReleaseYear());
        tvSeries.setPosterUrl(tvSeriesDetails.getPosterUrl());
        tvSeries.setNumberOfSeasons(tvSeriesDetails.getNumberOfSeasons());
        tvSeries.setStatus(tvSeriesDetails.getStatus());
        return tvSeriesRepository.save(tvSeries);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getByStatus(String status) {
        return tvSeriesRepository.findAllByStatus(status);
    }
}
