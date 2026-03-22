/**
 * Mirrors backend: DisplayTVSeriesDto
 *
 * Raw API shape:
 * {
 *   title:           String
 *   description:     String | null
 *   releaseYear:     Integer | null
 *   posterUrl:       String | null
 *   averageRating:   Double | null
 *   numberOfSeasons: Integer | null
 *   status:          String | null   (e.g. "Ongoing", "Ended")
 * }
 */

/**
 * @typedef {Object} TVSeries
 * @property {number}      id
 * @property {string}      title
 * @property {string|null} description
 * @property {number|null} year
 * @property {string|null} image            - poster image URL
 * @property {number|null} rating           - average rating (0–10)
 * @property {number|null} numberOfSeasons
 * @property {string|null} status
 */

/**
 * @param {Object} raw
 * @returns {TVSeries}
 */
export function fromApi(raw) {
    return {
        id:              raw.id,
        title:           raw.title,
        description:     raw.description     ?? null,
        year:            raw.releaseYear      ?? null,
        image:           raw.posterUrl        ?? null,
        rating:          raw.averageRating    ?? null,
        numberOfSeasons: raw.numberOfSeasons  ?? null,
        status:          raw.status           ?? null,
        type:            "tv",
    };
}

/**
 * @param {Object[]} rawList
 * @returns {TVSeries[]}
 */
export function fromApiList(rawList) {
    return rawList.map(fromApi);
}
