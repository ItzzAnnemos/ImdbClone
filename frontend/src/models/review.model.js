/**
 * Mirrors backend: DisplayReviewDto
 *
 * Raw API shape:
 * {
 *   id:         Number
 *   username:   String
 *   mediaId:    Long
 *   mediaTitle: String
 *   posterUrl:  String | null
 *   reviewText: String
 *   createdAt:  String          (ISO datetime)
 *   updatedAt:  String | null   (ISO datetime — null when not edited)
 * }
 */

/**
 * @typedef {Object} Review
 * @property {number}      id
 * @property {string}      username
 * @property {number|null} mediaId
 * @property {string}      mediaTitle
 * @property {string|null} image
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
        id: raw.id,
        username: raw.username,
        mediaId: raw.mediaId ?? null,
        mediaTitle: raw.mediaTitle,
        image: raw.posterUrl ?? null,
        reviewText: raw.reviewText,
        createdAt: raw.createdAt,
        updatedAt: raw.updatedAt ?? null,
        isEdited: raw.updatedAt != null,
    };
}

/**
 * @param {Object[]} rawList
 * @returns {Review[]}
 */
export function fromApiList(rawList) {
    return rawList.map(fromApi);
}
