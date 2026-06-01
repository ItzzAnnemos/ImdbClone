import api from "./api";
import { pagedSearchResultFromApi } from "../models/search.model";

const DEFAULT_SEARCH_SIZE = 5;

export async function searchCatalog(query, page = 0, size = DEFAULT_SEARCH_SIZE, options = {}) {
    const response = await api.get("/api/search", {
        params: { query, page, size },
        signal: options.signal,
    });

    return pagedSearchResultFromApi(response.data);
}
