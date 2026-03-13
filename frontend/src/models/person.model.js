/**
 * Mirrors backend: DisplayPersonDto & DisplayMediaPersonDto
 *
 * DisplayPersonDto raw shape:
 * {
 *   firstName:         String
 *   lastName:          String
 *   biography:         String | null
 *   birthDate:         String (ISO date) | null
 *   profilePictureUrl: String | null
 * }
 *
 * DisplayMediaPersonDto raw shape:
 * {
 *   mediaTitle:      String
 *   personFirstName: String
 *   personLastName:  String
 *   role:            String  (enum: ACTOR | DIRECTOR | WRITER | PRODUCER)
 *   characterName:   String | null
 * }
 */

/**
 * @typedef {Object} Person
 * @property {string}      firstName
 * @property {string}      lastName
 * @property {string}      fullName         - convenience: "First Last"
 * @property {string|null} biography
 * @property {string|null} birthDate        - ISO date string
 * @property {string|null} profilePictureUrl
 */

/**
 * @typedef {Object} MediaPerson
 * @property {string}      mediaTitle
 * @property {string}      firstName
 * @property {string}      lastName
 * @property {string}      fullName         - convenience: "First Last"
 * @property {string}      role             - e.g. "DIRECTOR"
 * @property {string|null} characterName
 */

/**
 * @param {Object} raw
 * @returns {Person}
 */
export function fromApi(raw) {
    return {
        firstName:         raw.firstName,
        lastName:          raw.lastName,
        fullName:          `${raw.firstName} ${raw.lastName}`,
        biography:         raw.biography         ?? null,
        birthDate:         raw.birthDate         ?? null,
        profilePictureUrl: raw.profilePictureUrl ?? null,
    };
}

/**
 * @param {Object} raw
 * @returns {MediaPerson}
 */
export function mediaPersonFromApi(raw) {
    return {
        mediaTitle:    raw.mediaTitle,
        firstName:     raw.personFirstName,
        lastName:      raw.personLastName,
        fullName:      `${raw.personFirstName} ${raw.personLastName}`,
        role:          raw.role,
        characterName: raw.characterName ?? null,
    };
}

/**
 * @param {Object[]} rawList
 * @returns {Person[]}
 */
export function fromApiList(rawList) {
    return rawList.map(fromApi);
}

/**
 * @param {Object[]} rawList
 * @returns {MediaPerson[]}
 */
export function mediaPersonFromApiList(rawList) {
    return rawList.map(mediaPersonFromApi);
}
