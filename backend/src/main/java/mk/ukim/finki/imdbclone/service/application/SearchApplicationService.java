package mk.ukim.finki.imdbclone.service.application;

import mk.ukim.finki.imdbclone.model.dto.PagedSearchResultDto;
import mk.ukim.finki.imdbclone.model.dto.SearchResultDto;

public interface SearchApplicationService {

    SearchResultDto search(String query);

    PagedSearchResultDto search(String query, int page, int size);
}
