/**
 * Mirrors backend: DisplayRatingDto
 *
 * Raw API shape:
 * {
 *   username:   String
 *   mediaTitle: String
 *   rating:     Integer  (1–10)
 *   createdAt:  String   (ISO datetime)
 * }
 */

/**
 * @typedef {Object} Rating
 * @property {string} username
 * @property {string} mediaTitle
 * @property {number} rating      - 1–10
 * @property {string} createdAt   - ISO datetime string
 */

/**
 * @param {Object} raw
 * @returns {Rating}
 */
export function fromApi(raw) {
    return {
        username:   raw.username,
        mediaTitle: raw.mediaTitle,
        rating:     raw.rating,
        createdAt:  raw.createdAt,
    };
}

/**
 * @param {Object[]} rawList
 * @returns {Rating[]}
 */
export function fromApiList(rawList) {
    return rawList.map(fromApi);
}
