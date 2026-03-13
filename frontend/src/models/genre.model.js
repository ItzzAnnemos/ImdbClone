/**
 * Mirrors backend: DisplayGenreDto
 *
 * Raw API shape:
 * {
 *   id:   Long
 *   name: String
 * }
 */

/**
 * @typedef {Object} Genre
 * @property {number} id
 * @property {string} name
 */

/**
 * @param {Object} raw
 * @returns {Genre}
 */
export function fromApi(raw) {
    return {
        id:   raw.id,
        name: raw.name,
    };
}

/**
 * @param {Object[]} rawList
 * @returns {Genre[]}
 */
export function fromApiList(rawList) {
    return rawList.map(fromApi);
}
