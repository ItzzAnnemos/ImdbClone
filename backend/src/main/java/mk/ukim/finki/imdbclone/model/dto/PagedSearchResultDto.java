package mk.ukim.finki.imdbclone.model.dto;

import java.util.List;

/**
 * A single page of search results plus pagination metadata.
 *
 * @param results       the items on the current page
 * @param interpretedAs which sources contributed (e.g. "media, person")
 * @param page          the current zero-based page index
 * @param size          the requested page size
 * @param totalResults  the total number of matches across all pages
 * @param totalPages    the total number of pages
 * @param hasNext       whether a next page exists
 * @param hasPrevious   whether a previous page exists
 */
public record PagedSearchResultDto(
        List<SearchItemDto> results,
        String interpretedAs,
        int page,
        int size,
        long totalResults,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
}
