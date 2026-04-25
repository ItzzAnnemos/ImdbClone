import api from "./api";
import * as Review from "../models/review.model";

/**
 * Fetches reviews for a specific media.
 * @param {number} mediaId
 * @returns {Promise<Review.Review[]>}
 */
export async function getMediaReviews(mediaId) {
    const response = await api.get(`/api/reviews/media/${mediaId}`);
    return Review.fromApiList(response.data);
}

/**
 * Submits a review for a media.
 * @param {Object} reviewDto
 * @returns {Promise<Review.Review>}
 */
export async function addReview(reviewDto) {
    const response = await api.post("/api/reviews/add", reviewDto);
    return Review.fromApi(response.data);
}

/**
 * Fetches the user's review for a specific media.
 * @param {number} userId
 * @param {number} mediaId
 * @returns {Promise<Review.Review|null>}
 */
export async function getUserReview(userId, mediaId) {
    try {
        const response = await api.get(`/api/reviews/by-user-media`, {
            params: { userId, mediaId }
        });
        return Review.fromApi(response.data);
    } catch (err) {
        if (err.response && err.response.status === 404) {
            return null;
        }
        throw err;
    }
}
/**
 * Updates an existing review.
 * @param {number} reviewId
 * @param {string} text
 * @returns {Promise<Review.Review>}
 */
export async function updateReview(reviewId, text) {
    const response = await api.put(`/api/reviews/edit/${reviewId}`, { reviewText: text });
    return Review.fromApi(response.data);
}

/**
 * Deletes a review.
 * @param {number} reviewId
 */
export async function deleteReview(reviewId) {
    await api.delete(`/api/reviews/delete/${reviewId}`);
}
