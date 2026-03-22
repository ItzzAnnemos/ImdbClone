/**
 * Mirrors backend: DisplayMovieDto
 *
 * Raw API shape:
 * {
 *   title:         String
 *   description:   String | null
 *   releaseYear:   Integer | null
 *   posterUrl:     String | null
 *   averageRating: Double | null
 *   duration:      Integer | null   (minutes)
 * }
 */

/**
 * @typedef {Object} Movie
 * @property {number}      id
 * @property {string}      title
 * @property {string|null} description
 * @property {number|null} year
 * @property {string|null} image         - poster image URL
 * @property {number|null} rating        - average rating (0–10)
 * @property {number|null} duration      - runtime in minutes
 */

/**
 * Normalises a raw DisplayMovieDto response into a Movie.
 * @param {Object} raw
 * @returns {Movie}
 */
export function fromApi(raw) {
    return {
        id:          raw.id,
        title:       raw.title,
        description: raw.description   ?? null,
        year:        raw.releaseYear   ?? null,
        image:       raw.posterUrl     ?? null,
        rating:      raw.averageRating ?? null,
        duration:    raw.duration      ?? null,
        type:        "movie",
    };
}

/**
 * @param {Object[]} rawList
 * @returns {Movie[]}
 */
export function fromApiList(rawList) {
    return rawList.map(fromApi);
}
