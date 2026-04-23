import api from "./api";
import * as Movie from "../models/movie.model";
import * as TVSeries from "../models/tvSeries.model";

/**
 * Fetches top-rated (trending) movies from the backend.
 * @returns {Promise<Movie[]>}
 */
export async function getTrendingMovies() {
    const response = await api.get("/api/movies/top-rated");
    return Movie.fromApiList(response.data);
}

/**
 * Fetches recently added movies from the backend.
 * @returns {Promise<Movie[]>}
 */
export async function getRecentMovies() {
    const response = await api.get("/api/movies/recent");
    return Movie.fromApiList(response.data);
}

/**
 * Fetches top-rated (trending) TV series from the backend.
 * @returns {Promise<TVSeries[]>}
 */
export async function getTrendingTVSeries() {
    const response = await api.get("/api/tv-series/top-rated");
    return TVSeries.fromApiList(response.data);
}

/**
 * Fetches recently added TV series from the backend.
 * @returns {Promise<TVSeries[]>}
 */
export async function getRecentTVSeries() {
    const response = await api.get("/api/tv-series/recent");
    return TVSeries.fromApiList(response.data);
}

/**
 * Fetches movie details by ID.
 * @param {number|string} id
 * @returns {Promise<Movie>}
 */
export async function getMovieById(id) {
    const response = await api.get(`/api/movies/${id}`);
    return Movie.fromApi(response.data);
}

/**
 * Fetches TV series details by ID.
 * @param {number|string} id
 * @returns {Promise<TVSeries>}
 */
export async function getTVSeriesById(id) {
    const response = await api.get(`/api/tv-series/${id}`);
    return TVSeries.fromApi(response.data);
}

/**
 * Fetches similar movies.
 * @param {number|string} id
 */
export async function getSimilarMovies(id) {
    const response = await api.get(`/api/movies/${id}/similar`);
    return response.data; // These are DisplayCardMediaDto
}

/**
 * Fetches similar TV series.
 * @param {number|string} id
 */
export async function getSimilarTVSeries(id) {
    const response = await api.get(`/api/tv-series/${id}/similar`);
    return response.data; // These are DisplayCardMediaDto
}
