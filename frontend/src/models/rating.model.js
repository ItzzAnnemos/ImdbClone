/**
 * Mirrors backend: DisplayRatingDto
 *
 * Raw API shape:
 * {
 *   username:   String
 *   mediaId:    Long
 *   mediaTitle: String
 *   posterUrl:  String | null
 *   trailerUrl: String | null
 *   averageRating: Double | null
 *   mediaType:  String
 *   rating:     Integer  (1–10)
 *   createdAt:  String   (ISO datetime)
 * }
 */

/**
 * @typedef {Object} Rating
 * @property {string} username
 * @property {number} mediaId
 * @property {string} mediaTitle
 * @property {string|null} image
 * @property {string|null} trailerUrl
 * @property {number|null} averageRating
 * @property {string} mediaType
 * @property {number} rating      - 1–10
 * @property {string} createdAt   - ISO datetime string
 */

/**
 * @param {Object} raw
 * @returns {Rating}
 */
export function fromApi(raw) {
    return {
        username: raw.username,
        mediaId: raw.mediaId,
        mediaTitle: raw.mediaTitle,
        image: raw.posterUrl ?? null,
        trailerUrl: raw.trailerUrl ?? null,
        averageRating: raw.averageRating ?? null,
        mediaType: raw.mediaType ?? "movie",
        rating: raw.rating,
        createdAt: raw.createdAt,
    };
}

/**
 * @param {Object[]} rawList
 * @returns {Rating[]}
 */
export function fromApiList(rawList) {
    return rawList.map(fromApi);
}
