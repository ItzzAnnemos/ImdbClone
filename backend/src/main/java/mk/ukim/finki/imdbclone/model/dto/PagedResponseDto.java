package mk.ukim.finki.imdbclone.model.dto;

import java.util.List;

/**
 * A generic page of results plus pagination metadata.
 *
 * @param items       the items on the current page
 * @param page        the current zero-based page index
 * @param size        the requested page size
 * @param totalItems  the total number of items across all pages
 * @param totalPages  the total number of pages
 * @param hasNext     whether a next page exists
 * @param hasPrevious whether a previous page exists
 * @param <T>         the item type contained in the page
 */
public record PagedResponseDto<T>(
        List<T> items,
        int page,
        int size,
        long totalItems,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

    /**
     * Create a paged response and calculate page metadata from the total item count.
     *
     * @param items      the items on the current page
     * @param page       the current zero-based page index
     * @param size       the requested page size
     * @param totalItems the total number of items across all pages
     * @param <T>        the item type contained in the page
     * @return a paged response with calculated totals and navigation flags
     */
    public static <T> PagedResponseDto<T> of(List<T> items, int page, int size, long totalItems) {
        int totalPages = size <= 0 ? 0 : (int) Math.ceil((double) totalItems / size);
        return new PagedResponseDto<>(
                items,
                page,
                size,
                totalItems,
                totalPages,
                page + 1 < totalPages,
                page > 0
        );
    }
}
