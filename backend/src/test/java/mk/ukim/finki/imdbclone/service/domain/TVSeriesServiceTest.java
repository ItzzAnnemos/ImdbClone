package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.domain.TVSeries;
import mk.ukim.finki.imdbclone.repository.TVSeriesRepository;
import mk.ukim.finki.imdbclone.service.domain.impl.TVSeriesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TVSeriesServiceTest {

    @Autowired
    private TVSeriesRepository tvSeriesRepository;

    private TVSeriesService tvSeriesService;

    @BeforeEach
    void setUp() {
        tvSeriesService = new TVSeriesServiceImpl(tvSeriesRepository);
        tvSeriesRepository.deleteAll();
    }

    private TVSeries createTVSeries(String title, String status) {
        TVSeries tvSeries = new TVSeries();
        tvSeries.setTitle(title);
        tvSeries.setStatus(status);
        tvSeries.setDescription("Desc");
        tvSeries.setNumberOfSeasons(1);
        return tvSeries;
    }

    @Test
    void shouldCreateAndRetrieveTVSeries() {
        TVSeries series = createTVSeries("Breaking Bad", "Ended");
        TVSeries saved = tvSeriesService.create(series);

        assertThat(saved.getId()).isNotNull();

        Optional<TVSeries> byId = tvSeriesService.getById(saved.getId());
        assertThat(byId).isPresent();
        assertThat(byId.get().getTitle()).isEqualTo("Breaking Bad");
    }

    @Test
    void shouldUpdateTVSeries() {
        TVSeries saved = tvSeriesService.create(createTVSeries("Old Title", "Returning Series"));

        TVSeries updateDetails = new TVSeries();
        updateDetails.setTitle("New Title");
        updateDetails.setStatus("Ended");

        TVSeries updated = tvSeriesService.update(saved.getId(), updateDetails);

        assertThat(updated.getTitle()).isEqualTo("New Title");
        assertThat(updated.getStatus()).isEqualTo("Ended");
    }

    @Test
    void shouldDeleteTVSeries() {
        TVSeries saved = tvSeriesService.create(createTVSeries("To Delete", "Ended"));

        tvSeriesService.delete(saved.getId());

        assertThat(tvSeriesService.getById(saved.getId())).isEmpty();
    }

    @Test
    void shouldFilterByStatus() {
        tvSeriesService.create(createTVSeries("Show 1", "Ended"));
        tvSeriesService.create(createTVSeries("Show 2", "Returning Series"));
        tvSeriesService.create(createTVSeries("Show 3", "Ended"));

        List<TVSeries> endedShows = tvSeriesService.getByStatus("Ended");
        assertThat(endedShows).hasSize(2);
        assertThat(endedShows).extracting(TVSeries::getStatus).containsOnly("Ended");
    }
}
