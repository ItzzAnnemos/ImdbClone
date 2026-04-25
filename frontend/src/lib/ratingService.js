import api from "./api";
import * as Rating from "../models/rating.model";

/**
 * Fetches the user's rating for a specific media.
 * @param {number} userId
 * @param {number} mediaId
 * @returns {Promise<Rating.Rating|null>}
 */
export async function getUserRating(userId, mediaId) {
    try {
        const response = await api.get(`/api/ratings/by-user-media`, {
            params: { userId, mediaId }
        });
        return Rating.fromApi(response.data);
    } catch (err) {
        if (err.response && err.response.status === 404) {
            return null;
        }
        throw err;
    }
}

/**
 * Submits a rating for a media.
 * @param {Object} ratingDto
 * @returns {Promise<Rating.Rating>}
 */
export async function rateMedia(ratingDto) {
    const response = await api.post("/api/ratings/add", ratingDto);
    return Rating.fromApi(response.data);
}

/**
 * Deletes a user's rating for a media.
 * @param {number} userId
 * @param {number} mediaId
 */
export async function deleteRating(userId, mediaId) {
    await api.delete("/api/ratings/delete", {
        params: { userId, mediaId }
    });
}

/**
 * Gets the average rating for a media item.
 * @param {number} mediaId
 */
export async function getAverageRating(mediaId) {
    const response = await api.get(`/api/ratings/media/${mediaId}/average`);
    return response.data;
}

/**
 * Gets the rating count for a media item.
 * @param {number} mediaId
 */
export async function getRatingCount(mediaId) {
    const response = await api.get(`/api/ratings/media/${mediaId}/count`);
    return response.data;
}
