import api from "./api";
import { rankedMediaListFromApi, rankedPersonListFromApi } from "../models/discovery.model";

const mediaBasePath = {
    movie: "/api/movies",
    tv: "/api/tv-series",
};

export async function getTop250(mediaType) {
    const response = await api.get(`${mediaBasePath[mediaType]}/top-250`);
    return rankedMediaListFromApi(response.data);
}

export async function getMostPopularMedia(mediaType) {
    const response = await api.get(`${mediaBasePath[mediaType]}/most-popular`);
    return rankedMediaListFromApi(response.data);
}

export async function getMediaByGenre(mediaType, genreName) {
    const response = await api.get(
        `${mediaBasePath[mediaType]}/genre-ranked/${encodeURIComponent(genreName)}`,
    );
    return rankedMediaListFromApi(response.data);
}

export async function getGenres() {
    const response = await api.get("/api/genres");
    return response.data;
}

export async function getBornTodayCelebs() {
    const response = await api.get("/api/persons/born-today");
    return rankedPersonListFromApi(response.data);
}

export async function getMostPopularCelebs() {
    const response = await api.get("/api/persons/most-popular");
    return rankedPersonListFromApi(response.data);
}
