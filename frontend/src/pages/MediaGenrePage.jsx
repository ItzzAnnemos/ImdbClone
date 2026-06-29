import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Layout } from "../components/layout/Layout";
import { ChartPagination } from "../components/ui/ChartPagination";
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

const DEFAULT_PAGE_SIZE = 20;

export function MediaGenrePage({ mediaType }) {
    const { genreName } = useParams();
    const navigate = useNavigate();
    const [genres, setGenres] = useState([]);
    const [items, setItems] = useState([]);
    const [pageInfo, setPageInfo] = useState(null);
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(DEFAULT_PAGE_SIZE);
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

            const response = await discoveryService.getMediaByGenre(
                mediaType,
                effectiveGenre,
                page,
                pageSize,
            );
            setItems(response.items);
            setPageInfo(response);
        } catch (err) {
            console.error("Failed to load genre page:", err);
            setError("Failed to load this genre. Please try again.");
        } finally {
            setLoading(false);
        }
    }, [mediaType, navigate, page, pageSize, selectedGenreName]);

    useEffect(() => {
        fetchPage();
    }, [fetchPage]);

    useEffect(() => {
        setPage(0);
    }, [mediaType, selectedGenreName]);

    const handlePageChange = useCallback((nextPage) => {
        setPage(nextPage);
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, []);

    const handlePageSizeChange = useCallback((nextPageSize) => {
        setPageSize(nextPageSize);
        setPage(0);
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, []);

    const title = selectedGenreName
        ? `${selectedGenreName} ${mediaLabels[mediaType]}`
        : `${mediaLabels[mediaType]} by Genre`;

    return (
        <Layout>
            <DiscoveryPageHeader
                eyebrow={mediaLabels[mediaType]}
                title={title}
                subtitle={`Browse ${mediaLabels[mediaType].toLowerCase()} by genre, ranked by rating and audience activity.`}
                count={!loading && !error ? pageInfo?.totalItems : undefined}
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
            {!loading && !error && items.length > 0 && (
                <>
                    <RankedMediaList items={items} />
                    <ChartPagination
                        pageInfo={pageInfo}
                        pageSize={pageSize}
                        onPageChange={handlePageChange}
                        onPageSizeChange={handlePageSizeChange}
                    />
                </>
            )}
        </Layout>
    );
}
