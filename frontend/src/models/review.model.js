/**
 * Mirrors backend: DisplayReviewDto
 *
 * Raw API shape:
 * {
 *   username:   String
 *   mediaTitle: String
 *   reviewText: String
 *   createdAt:  String          (ISO datetime)
 *   updatedAt:  String | null   (ISO datetime — null when not edited)
 * }
 */

/**
 * @typedef {Object} Review
 * @property {string}      username
 * @property {string}      mediaTitle
 * @property {string}      reviewText
 * @property {string}      createdAt   - ISO datetime string
 * @property {string|null} updatedAt   - ISO datetime string, null if never edited
 * @property {boolean}     isEdited    - convenience flag
 */

/**
 * @param {Object} raw
 * @returns {Review}
 */
export function fromApi(raw) {
    return {
        username:   raw.username,
        mediaTitle: raw.mediaTitle,
        reviewText: raw.reviewText,
        createdAt:  raw.createdAt,
        updatedAt:  raw.updatedAt ?? null,
        isEdited:   raw.updatedAt != null,
    };
}

/**
 * @param {Object[]} rawList
 * @returns {Review[]}
 */
export function fromApiList(rawList) {
    return rawList.map(fromApi);
}
