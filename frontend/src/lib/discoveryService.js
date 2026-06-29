import api from "./api";
import {
    rankedMediaListFromApi,
    rankedMediaPageFromApi,
    rankedPersonListFromApi,
    rankedPersonPageFromApi,
} from "../models/discovery.model";

const mediaBasePath = {
    movie: "/api/movies",
    tv: "/api/tv-series",
};

export async function getTop250(mediaType, page = null, size = 20) {
    const response = await api.get(`${mediaBasePath[mediaType]}/top-250`, {
        params: page === null ? undefined : { page, size },
    });
    return page === null
        ? rankedMediaListFromApi(response.data)
        : rankedMediaPageFromApi(response.data);
}

export async function getMostPopularMedia(mediaType, page = null, size = 20) {
    const response = await api.get(`${mediaBasePath[mediaType]}/most-popular`, {
        params: page === null ? undefined : { page, size },
    });
    return page === null
        ? rankedMediaListFromApi(response.data)
        : rankedMediaPageFromApi(response.data);
}

export async function getMediaByGenre(mediaType, genreName, page = null, size = 20) {
    const response = await api.get(
        `${mediaBasePath[mediaType]}/genre-ranked/${encodeURIComponent(genreName)}`,
        {
            params: page === null ? undefined : { page, size },
        },
    );
    return page === null
        ? rankedMediaListFromApi(response.data)
        : rankedMediaPageFromApi(response.data);
}

export async function getGenres() {
    const response = await api.get("/api/genres");
    return response.data;
}

export async function getBornTodayCelebs() {
    const response = await api.get("/api/persons/born-today");
    return rankedPersonListFromApi(response.data);
}

export async function getMostPopularCelebs(page = null, size = 20) {
    const response = await api.get("/api/persons/most-popular", {
        params: page === null ? undefined : { page, size },
    });
    return page === null
        ? rankedPersonListFromApi(response.data)
        : rankedPersonPageFromApi(response.data);
}
