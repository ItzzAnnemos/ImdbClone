/**
 * Mirrors backend: DisplayCardMediaDto
 *
 * Raw API shape:
 * {
 *   id:            Long
 *   title:         String
 *   posterUrl:     String | null
 *   averageRating: Double | null
 * }
 */

/**
 * @typedef {Object} MediaCard
 * @property {number}      id
 * @property {string}      title
 * @property {string|null} image         - poster image URL
 * @property {number|null} rating        - average rating (0–10)
 */

/**
 * Normalises a raw DisplayCardMediaDto response into a MediaCard.
 * @param {Object} raw - raw API response object
 * @returns {MediaCard}
 */
export function fromApi(raw) {
    return {
        id:     raw.id,
        title:  raw.title,
        image:  raw.posterUrl ?? null,
        rating: raw.averageRating ?? null,
    };
}

/**
 * Normalises an array of DisplayCardMediaDto responses.
 * @param {Object[]} rawList
 * @returns {MediaCard[]}
 */
export function fromApiList(rawList) {
    return rawList.map(fromApi);
}
