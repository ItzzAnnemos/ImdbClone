import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Layout } from "../components/layout/Layout";
import { DiscoveryPageHeader } from "../components/ui/DiscoveryPageHeader";
import {
    DiscoveryEmptyState,
    DiscoveryErrorState,
    DiscoveryLoadingState,
} from "../components/ui/DiscoveryState";
import { GenrePicker } from "../components/ui/GenrePicker";
import { RankedMediaList } from "../components/ui/RankedMediaList";
import * as discoveryService from "../lib/discoveryService";

const mediaLabels = {
    movie: "Movies",
    tv: "TV Shows",
};

export function MediaGenrePage({ mediaType }) {
    const { genreName } = useParams();
    const navigate = useNavigate();
    const [genres, setGenres] = useState([]);
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const selectedGenreName = useMemo(
        () => (genreName ? decodeURIComponent(genreName) : null),
        [genreName],
    );

    const fetchPage = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const nextGenres = await discoveryService.getGenres();
            setGenres(nextGenres);

            const effectiveGenre = selectedGenreName || nextGenres[0]?.name;
            if (!effectiveGenre) {
                setItems([]);
                return;
            }

            if (!selectedGenreName) {
                const basePath = mediaType === "tv" ? "/tv/genre" : "/movies/genre";
                navigate(`${basePath}/${encodeURIComponent(effectiveGenre)}`, { replace: true });
                return;
            }

            const nextItems = await discoveryService.getMediaByGenre(mediaType, effectiveGenre);
            setItems(nextItems);
        } catch (err) {
            console.error("Failed to load genre page:", err);
            setError("Failed to load this genre. Please try again.");
        } finally {
            setLoading(false);
        }
    }, [mediaType, navigate, selectedGenreName]);

    useEffect(() => {
        fetchPage();
    }, [fetchPage]);

    const title = selectedGenreName
        ? `${selectedGenreName} ${mediaLabels[mediaType]}`
        : `${mediaLabels[mediaType]} by Genre`;

    return (
        <Layout>
            <DiscoveryPageHeader
                eyebrow={mediaLabels[mediaType]}
                title={title}
                subtitle={`Browse ${mediaLabels[mediaType].toLowerCase()} by genre, ranked by rating and audience activity.`}
                count={!loading && !error ? items.length : undefined}
            />

            {genres.length > 0 && (
                <GenrePicker
                    genres={genres}
                    mediaType={mediaType}
                    selectedGenreName={selectedGenreName}
                />
            )}

            {loading && <DiscoveryLoadingState label="Loading genre titles..." />}
            {!loading && error && <DiscoveryErrorState message={error} onRetry={fetchPage} />}
            {!loading && !error && items.length === 0 && (
                <DiscoveryEmptyState
                    title="No titles found"
                    message="There are no titles available for this genre yet."
                />
            )}
            {!loading && !error && items.length > 0 && <RankedMediaList items={items} />}
        </Layout>
    );
}
