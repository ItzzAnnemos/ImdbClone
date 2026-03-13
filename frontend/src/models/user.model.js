/**
 * Mirrors backend: DisplayUserDto & LoginResponseDto
 *
 * DisplayUserDto raw shape:
 * {
 *   username:  String
 *   firstName: String
 *   lastName:  String
 * }
 *
 * LoginResponseDto raw shape:
 * {
 *   token: String
 * }
 */

/**
 * @typedef {Object} User
 * @property {string} username
 * @property {string} firstName
 * @property {string} lastName
 */

/**
 * @typedef {Object} LoginResponse
 * @property {string} token
 */

/**
 * @param {Object} raw
 * @returns {User}
 */
export function fromApi(raw) {
    return {
        username:  raw.username,
        firstName: raw.firstName,
        lastName:  raw.lastName,
    };
}

/**
 * @param {Object} raw
 * @returns {LoginResponse}
 */
export function loginResponseFromApi(raw) {
    return {
        token: raw.token,
    };
}
