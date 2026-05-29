package mk.ukim.finki.imdbclone.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.PagedSearchResultDto;
import mk.ukim.finki.imdbclone.service.application.SearchApplicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchApplicationService searchApplicationService;

    /**
     * Unified search across titles, people and release year.
     *
     * @param query the search query
     * @param page  zero-based page index (default 0)
     * @param size  results per page (default 10, max 50)
     * @return a page of scored results plus pagination metadata
     */
    @GetMapping("/api/search")
    public PagedSearchResultDto search(
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return searchApplicationService.search(query, page, size);
    }
}
