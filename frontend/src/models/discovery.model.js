export function rankedMediaFromApi(raw) {
    return {
        id: raw.id,
        rank: raw.rank,
        title: raw.title,
        image: raw.posterUrl ?? null,
        year: raw.releaseYear ?? null,
        rating: raw.averageRating ?? null,
        ratingCount: raw.ratingCount ?? 0,
        genres: raw.genres ?? [],
        type: raw.type ?? "movie",
    };
}

export function rankedMediaListFromApi(rawList) {
    return rawList.map(rankedMediaFromApi);
}

export function rankedPersonFromApi(raw) {
    return {
        id: raw.id,
        rank: raw.rank,
        firstName: raw.firstName,
        lastName: raw.lastName,
        fullName: raw.fullName,
        image: raw.profilePictureUrl ?? null,
        birthDate: raw.birthDate ?? null,
        knownFor: raw.knownFor ?? [],
        creditCount: raw.creditCount ?? 0,
    };
}

export function rankedPersonListFromApi(rawList) {
    return rawList.map(rankedPersonFromApi);
}
