package mk.ukim.finki.imdbclone.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.SearchResultDto;
import mk.ukim.finki.imdbclone.service.application.SearchApplicationService;
import mk.ukim.finki.imdbclone.service.domain.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchApplicationService searchApplicationService;

    @GetMapping("/api/search")
    public SearchResultDto search(@RequestParam("query") String query) {
        return searchApplicationService.search(query);
    }
}