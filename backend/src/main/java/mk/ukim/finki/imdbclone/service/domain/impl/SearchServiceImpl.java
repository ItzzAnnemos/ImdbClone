package mk.ukim.finki.imdbclone.service.domain.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.domain.Media;
import mk.ukim.finki.imdbclone.model.domain.MediaPerson;
import mk.ukim.finki.imdbclone.model.domain.Person;
import mk.ukim.finki.imdbclone.model.dto.SearchItemDto;
import mk.ukim.finki.imdbclone.model.dto.PagedSearchResultDto;
import mk.ukim.finki.imdbclone.model.dto.SearchResultDto;
import mk.ukim.finki.imdbclone.repository.MediaRepository;
import mk.ukim.finki.imdbclone.repository.PersonRepository;
import mk.ukim.finki.imdbclone.service.domain.SearchService;
import mk.ukim.finki.imdbclone.util.SearchMatcherUtil;
import mk.ukim.finki.imdbclone.util.SearchQueryUtil;
import org.springframework.stereotype.Service;
import mk.ukim.finki.imdbclone.repository.MediaPersonRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private static final int MAX_RESULTS = 20;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DIRECT_MEDIA_LIMIT = 200;
    private static final int DIRECT_PERSON_LIMIT = 100;
    private static final int FUZZY_MEDIA_SCAN_LIMIT = 300;
    private static final int FUZZY_PERSON_SCAN_LIMIT = 150;
    private static final int PERSON_CREDIT_LIMIT = 200;
    private static final int MIN_QUERY_LENGTH_FOR_FUZZY = 3;

    private final MediaRepository mediaRepository;
    private final PersonRepository personRepository;
    private final MediaPersonRepository mediaPersonRepository;

    @Override
    public SearchResultDto search(String query) {
        if (query == null || query.isBlank()) {
            return new SearchResultDto(List.of(), "empty");
        }

        List<String> sources = new ArrayList<>();
        List<SearchItemDto> sorted = buildSortedResults(query, sources);

        List<SearchItemDto> finalResults = sorted.stream()
                .limit(MAX_RESULTS)
                .toList();

        return new SearchResultDto(
                finalResults,
                sources.isEmpty() ? "empty" : String.join(", ", sources)
        );
    }

    @Override
    public PagedSearchResultDto search(String query, int page, int size) {
        // Clamp the paging parameters into a sane range.
        if (page < 0) {
            page = 0;
        }
        if (size < 1) {
            size = DEFAULT_PAGE_SIZE;
        }
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }

        if (query == null || query.isBlank()) {
            return new PagedSearchResultDto(List.of(), "empty", page, size, 0L, 0, false, false);
        }

        List<String> sources = new ArrayList<>();
        List<SearchItemDto> sorted = buildSortedResults(query, sources);

        long totalResults = sorted.size();
        int totalPages = (int) Math.ceil((double) totalResults / size);

        int fromIndex = Math.min(page * size, sorted.size());
        int toIndex = Math.min(fromIndex + size, sorted.size());
        List<SearchItemDto> pageItems = sorted.subList(fromIndex, toIndex);

        return new PagedSearchResultDto(
                pageItems,
                sources.isEmpty() ? "empty" : String.join(", ", sources),
                page,
                size,
                totalResults,
                totalPages,
                page + 1 < totalPages,
                page > 0 && totalResults > 0
        );
    }

    /**
     * Runs all matchers for the query and returns every matching item sorted by
     * descending score. The {@code sources} list is populated as a side effect
     * with the names of the matchers that contributed.
     */
    private List<SearchItemDto> buildSortedResults(String query, List<String> sources) {
        String normalizedQuery = SearchQueryUtil.normalize(query);

        Map<String, SearchItemDto> results = new LinkedHashMap<>();

        searchMedia(normalizedQuery, results, sources);
        searchPeople(normalizedQuery, results, sources);
        searchByYear(normalizedQuery, results, sources);

        return results.values().stream()
                .sorted(Comparator.comparing(SearchItemDto::score).reversed())
                .toList();
    }

    private void searchMedia(
            String query,
            Map<String, SearchItemDto> results,
            List<String> sources
    ) {
        boolean found = false;

        List<Media> directMatches = mediaRepository.findByTitleContainingIgnoreCase(
                query,
                PageRequest.of(0, DIRECT_MEDIA_LIMIT, mediaRelevanceSort())
        );

        for (Media media : directMatches) {
            double score = SearchMatcherUtil.scoreTextMatch(media.getTitle(), query);
            if (score > 0.0) {
                addOrUpdateMediaResult(results, media, score);
                found = true;
            }
        }

        if (query.length() >= MIN_QUERY_LENGTH_FOR_FUZZY) {
            List<Media> fuzzyCandidates = mediaRepository.findAll(
                    PageRequest.of(0, FUZZY_MEDIA_SCAN_LIMIT, mediaRelevanceSort())
            ).getContent();

            for (Media media : fuzzyCandidates) {
                double score = SearchMatcherUtil.scoreTextMatch(media.getTitle(), query);

                if (score > 0.0 || SearchMatcherUtil.fuzzyMatch(media.getTitle(), query, 2)) {
                    addOrUpdateMediaResult(results, media, Math.max(score, 50.0));
                    found = true;
                }
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
        boolean found = false;
        Set<Long> matchedPersonIds = new LinkedHashSet<>();

        List<Person> directMatches = personRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                query,
                query,
                PageRequest.of(0, DIRECT_PERSON_LIMIT, personNameSort())
        );

        for (Person person : directMatches) {
            String fullName = person.getFirstName() + " " + person.getLastName();
            double score = SearchMatcherUtil.scoreTextMatch(fullName, query);

            if (score > 0.0) {
                addOrUpdatePersonResult(results, person, score);
                found = true;
                matchedPersonIds.add(person.getId());
            }
        }

        if (query.length() >= MIN_QUERY_LENGTH_FOR_FUZZY) {
            List<Person> fuzzyCandidates = personRepository.findAll(
                    PageRequest.of(0, FUZZY_PERSON_SCAN_LIMIT, personNameSort())
            ).getContent();

            for (Person person : fuzzyCandidates) {
                String fullName = person.getFirstName() + " " + person.getLastName();
                double score = SearchMatcherUtil.scoreTextMatch(fullName, query);

                if (score > 0.0 || SearchMatcherUtil.fuzzyMatch(fullName, query, 2)) {
                    addOrUpdatePersonResult(results, person, Math.max(score, 50.0));
                    found = true;
                    matchedPersonIds.add(person.getId());
                }
            }
        }

        addMediaForPeople(matchedPersonIds, results);

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
        List<Media> mediaByYear = mediaRepository.findByReleaseYear(
                year,
                PageRequest.of(0, DIRECT_MEDIA_LIMIT, mediaRelevanceSort())
        );

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

    private void addMediaForPeople(Set<Long> personIds, Map<String, SearchItemDto> results) {
        if (personIds.isEmpty()) {
            return;
        }

        List<MediaPerson> relations = mediaPersonRepository.findByPerson_IdIn(
                personIds,
                PageRequest.of(0, PERSON_CREDIT_LIMIT)
        );

        for (MediaPerson relation : relations) {
            addOrUpdateMediaResult(results, relation.getMedia(), 70.0);
        }
    }

    private Sort mediaRelevanceSort() {
        return Sort.by(
                Sort.Order.desc("averageRating"),
                Sort.Order.desc("releaseYear"),
                Sort.Order.asc("title")
        );
    }

    private Sort personNameSort() {
        return Sort.by(
                Sort.Order.asc("firstName"),
                Sort.Order.asc("lastName")
        );
    }
}
