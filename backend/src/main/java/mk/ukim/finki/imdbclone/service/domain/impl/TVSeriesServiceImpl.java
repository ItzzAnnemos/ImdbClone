package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import mk.ukim.finki.imdbclone.repository.TVSeriesRepository;
import mk.ukim.finki.imdbclone.service.domain.TVSeriesService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mk.ukim.finki.imdbclone.service.domain.helper.MediaSimilarityHelper;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
public class TVSeriesServiceImpl extends MediaServiceImpl<TVSeries> implements TVSeriesService {

    private static final int DEFAULT_CHART_LIMIT = 250;

    private final TVSeriesRepository tvSeriesRepository;

    public TVSeriesServiceImpl(TVSeriesRepository tvSeriesRepository,
                               MediaSimilarityHelper mediaSimilarityHelper) {
        super(tvSeriesRepository, mediaSimilarityHelper);
        this.tvSeriesRepository = tvSeriesRepository;
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
        tvSeries.setTrailerUrl(tvSeriesDetails.getTrailerUrl());
        tvSeries.setNumberOfSeasons(tvSeriesDetails.getNumberOfSeasons());
        tvSeries.setStatus(tvSeriesDetails.getStatus());
        return tvSeriesRepository.save(tvSeries);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getByStatus(String status) {
        return tvSeriesRepository.findAllByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getByGenre(String genreName) {
        return tvSeriesRepository.findByGenres_Name(genreName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getTop250() {
        return getTop250(PageRequest.of(0, DEFAULT_CHART_LIMIT));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getTop250(Pageable pageable) {
        return tvSeriesRepository.findAllByOrderByAverageRatingDescReleaseYearDescTitleAsc(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getMostPopular() {
        return getMostPopular(PageRequest.of(0, DEFAULT_CHART_LIMIT));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getMostPopular(Pageable pageable) {
        return tvSeriesRepository.findMostPopularRanked(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getRankedByGenre(String genreName) {
        return tvSeriesRepository.findByGenres_Name(genreName)
                .stream()
                .sorted(topRatedComparator())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TVSeries> getRankedByGenre(String genreName, Pageable pageable) {
        return tvSeriesRepository.findByGenres_NameOrderByAverageRatingDescReleaseYearDescTitleAsc(
                genreName,
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public long countByGenre(String genreName) {
        return tvSeriesRepository.countByGenres_Name(genreName);
    }

    private Comparator<TVSeries> topRatedComparator() {
        return Comparator
                .comparing((TVSeries tvSeries) -> valueOrZero(tvSeries.getAverageRating())).reversed()
                .thenComparing((TVSeries tvSeries) -> tvSeries.getRatings().size(), Comparator.reverseOrder())
                .thenComparing((TVSeries tvSeries) -> valueOrZero(tvSeries.getReleaseYear()), Comparator.reverseOrder())
                .thenComparing(TVSeries::getTitle);
    }

    private Comparator<TVSeries> popularComparator() {
        return Comparator
                .comparing((TVSeries tvSeries) -> tvSeries.getRatings().size(), Comparator.reverseOrder())
                .thenComparing((TVSeries tvSeries) -> valueOrZero(tvSeries.getAverageRating()), Comparator.reverseOrder())
                .thenComparing((TVSeries tvSeries) -> valueOrZero(tvSeries.getReleaseYear()), Comparator.reverseOrder())
                .thenComparing(TVSeries::getTitle);
    }

    private Double valueOrZero(Double value) {
        return value == null ? 0.0 : value;
    }

    private Integer valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return tvSeriesRepository.count();
    }
}
