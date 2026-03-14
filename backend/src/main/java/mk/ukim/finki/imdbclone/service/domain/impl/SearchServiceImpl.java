package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.model.dto.SearchItemDto;
import mk.ukim.finki.imdbclone.model.dto.SearchResultDto;
import mk.ukim.finki.imdbclone.repository.MediaRepository;
import mk.ukim.finki.imdbclone.repository.PersonRepository;
import mk.ukim.finki.imdbclone.service.domain.SearchService;
import mk.ukim.finki.imdbclone.util.SearchMatcherUtil;
import mk.ukim.finki.imdbclone.util.SearchQueryUtil;
import org.springframework.stereotype.Service;
import mk.ukim.finki.imdbclone.repository.MediaPersonRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private static final int MAX_RESULTS = 20;

    private final MediaRepository mediaRepository;
    private final PersonRepository personRepository;
    private final MediaPersonRepository mediaPersonRepository;

    @Override
    public SearchResultDto search(String query) {
        if (query == null || query.isBlank()) {
            return new SearchResultDto(List.of(), "empty");
        }

        String normalizedQuery = SearchQueryUtil.normalize(query);

        Map<String, SearchItemDto> results = new LinkedHashMap<>();
        List<String> sources = new ArrayList<>();

        searchMedia(normalizedQuery, results, sources);
        searchPeople(normalizedQuery, results, sources);
        searchByYear(normalizedQuery, results, sources);

        List<SearchItemDto> finalResults = results.values().stream()
                .sorted(Comparator.comparing(SearchItemDto::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        return new SearchResultDto(
                finalResults,
                sources.isEmpty() ? "empty" : String.join(", ", sources)
        );
    }

    private void searchMedia(
            String query,
            Map<String, SearchItemDto> results,
            List<String> sources
    ) {
        List<Media> allMedia = mediaRepository.findAll();
        boolean found = false;

        for (Media media : allMedia) {
            double score = SearchMatcherUtil.scoreTextMatch(media.getTitle(), query);

            if (score > 0.0 || SearchMatcherUtil.fuzzyMatch(media.getTitle(), query, 2)) {
                addOrUpdateMediaResult(results, media, Math.max(score, 50.0));
                found = true;
            }
        }

        if (found) {
            sources.add("media");
        }
    }

    private void searchPeople(
            String query,
            Map<String, SearchItemDto> results,
            List<String> sources
    ) {
        List<Person> allPeople = personRepository.findAll();
        boolean found = false;

        for (Person person : allPeople) {
            String fullName = person.getFirstName() + " " + person.getLastName();
            double score = SearchMatcherUtil.scoreTextMatch(fullName, query);

            if (score > 0.0 || SearchMatcherUtil.fuzzyMatch(fullName, query, 2)) {
                addOrUpdatePersonResult(results, person, Math.max(score, 90.0));
                found = true;

                List<MediaPerson> relations = mediaPersonRepository.findByPersonId(person.getId());

                for (MediaPerson relation : relations) {
                    Media media = relation.getMedia();
                    addOrUpdateMediaResult(results, media, 70.0);
                }
            }
        }

        if (found) {
            sources.add("person");
        }
    }

    private void searchByYear(
            String query,
            Map<String, SearchItemDto> results,
            List<String> sources
    ) {
        if (!query.matches("\\d{4}")) {
            return;
        }

        Integer year = Integer.parseInt(query);
        List<Media> mediaByYear = mediaRepository.findByReleaseYear(year);

        if (!mediaByYear.isEmpty()) {
            for (Media media : mediaByYear) {
                addOrUpdateMediaResult(results, media, 85.0);
            }
            sources.add("year");
        }
    }

    private void addOrUpdateMediaResult(
            Map<String, SearchItemDto> results,
            Media media,
            double score
    ) {
        String key = "media-" + media.getId();
        SearchItemDto newItem = SearchItemDto.fromMedia(media, score);

        SearchItemDto existing = results.get(key);
        if (existing == null || score > existing.score()) {
            results.put(key, newItem);
        }
    }

    private void addOrUpdatePersonResult(
            Map<String, SearchItemDto> results,
            Person person,
            double score
    ) {
        String key = "person-" + person.getId();
        SearchItemDto newItem = SearchItemDto.fromPerson(person, score);

        SearchItemDto existing = results.get(key);
        if (existing == null || score > existing.score()) {
            results.put(key, newItem);
        }
    }
}