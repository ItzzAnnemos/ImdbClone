package mk.ukim.finki.imdbclone.service.application.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.SearchResultDto;
import mk.ukim.finki.imdbclone.service.application.SearchApplicationService;
import mk.ukim.finki.imdbclone.service.domain.SearchService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchApplicationServiceImpl implements SearchApplicationService {

    private final SearchService searchService;

    @Override
    public SearchResultDto search(String query) {
        return searchService.search(query);
    }
}