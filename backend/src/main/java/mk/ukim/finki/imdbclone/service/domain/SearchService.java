package mk.ukim.finki.imdbclone.service.domain;

import mk.ukim.finki.imdbclone.model.dto.PagedSearchResultDto;
import mk.ukim.finki.imdbclone.model.dto.SearchResultDto;

public interface SearchService {

    SearchResultDto search(String query);

    /**
     * Same search as {@link #search(String)} but returns a single page of the
     * scored results together with pagination metadata.
     *
     * @param query the raw search query
     * @param page  the zero-based page index
     * @param size  the number of results per page
     * @return a page of results plus pagination metadata
     */
    PagedSearchResultDto search(String query, int page, int size);

}