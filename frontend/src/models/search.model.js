/**
 * Mirrors backend: PagedSearchResultDto and SearchItemDto.
 */

export function searchItemFromApi(raw) {
    return {
        id: raw.id,
        title: raw.title,
        image: raw.imageUrl ?? null,
        type: raw.type ?? "Media",
        score: raw.score ?? null,
    };
}

export function pagedSearchResultFromApi(raw) {
    return {
        results: Array.isArray(raw.results) ? raw.results.map(searchItemFromApi) : [],
        interpretedAs: raw.interpretedAs ?? "",
        page: raw.page ?? 0,
        size: raw.size ?? 10,
        totalResults: raw.totalResults ?? 0,
        totalPages: raw.totalPages ?? 0,
        hasNext: Boolean(raw.hasNext),
        hasPrevious: Boolean(raw.hasPrevious),
    };
}
