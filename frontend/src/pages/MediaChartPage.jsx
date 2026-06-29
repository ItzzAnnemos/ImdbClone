import { useCallback, useEffect, useMemo, useState } from "react";
import { Layout } from "../components/layout/Layout";
import { ChartPagination } from "../components/ui/ChartPagination";
import { DiscoveryPageHeader } from "../components/ui/DiscoveryPageHeader";
import {
    DiscoveryEmptyState,
    DiscoveryErrorState,
    DiscoveryLoadingState,
} from "../components/ui/DiscoveryState";
import { RankedMediaList } from "../components/ui/RankedMediaList";
import * as discoveryService from "../lib/discoveryService";

const chartCopy = {
    movie: {
        label: "Movies",
        "top-250": {
            title: "Top 250 Movies",
            subtitle: "The highest rated movies ranked by IMDbClone users.",
        },
        "most-popular": {
            title: "Most Popular Movies",
            subtitle: "Movies with the strongest audience activity across ratings.",
        },
    },
    tv: {
        label: "TV Shows",
        "top-250": {
            title: "Top 250 TV Shows",
            subtitle: "The highest rated TV shows ranked by IMDbClone users.",
        },
        "most-popular": {
            title: "Most Popular TV Shows",
            subtitle: "TV shows with the strongest audience activity across ratings.",
        },
    },
};

const DEFAULT_PAGE_SIZE = 20;

export function MediaChartPage({ mediaType, chartType }) {
    const [items, setItems] = useState([]);
    const [pageInfo, setPageInfo] = useState(null);
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(DEFAULT_PAGE_SIZE);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const copy = useMemo(() => chartCopy[mediaType][chartType], [mediaType, chartType]);

    const fetchItems = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            if (chartType === "top-250") {
                const nextItems = await discoveryService.getTop250(mediaType);
                setItems(nextItems);
                setPageInfo(null);
            } else {
                const response = await discoveryService.getMostPopularMedia(
                    mediaType,
                    page,
                    pageSize,
                );
                setItems(response.items);
                setPageInfo(response);
            }
        } catch (err) {
            console.error("Failed to load media chart:", err);
            setError("Failed to load this chart. Please try again.");
        } finally {
            setLoading(false);
        }
    }, [chartType, mediaType, page, pageSize]);

    useEffect(() => {
        fetchItems();
    }, [fetchItems]);

    useEffect(() => {
        setPage(0);
    }, [chartType, mediaType]);

    const handlePageChange = useCallback((nextPage) => {
        setPage(nextPage);
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, []);

    const handlePageSizeChange = useCallback((nextPageSize) => {
        setPageSize(nextPageSize);
        setPage(0);
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, []);

    return (
        <Layout>
            <DiscoveryPageHeader
                eyebrow={chartCopy[mediaType].label}
                title={copy.title}
                subtitle={copy.subtitle}
                count={!loading && !error ? (pageInfo?.totalItems ?? items.length) : undefined}
            />

            {loading && <DiscoveryLoadingState label={`Loading ${copy.title.toLowerCase()}...`} />}
            {!loading && error && <DiscoveryErrorState message={error} onRetry={fetchItems} />}
            {!loading && !error && items.length === 0 && (
                <DiscoveryEmptyState
                    title="No titles found"
                    message="There are no titles available for this chart yet."
                />
            )}
            {!loading && !error && items.length > 0 && (
                <>
                    <RankedMediaList items={items} />
                    {chartType !== "top-250" && (
                        <ChartPagination
                            pageInfo={pageInfo}
                            pageSize={pageSize}
                            onPageChange={handlePageChange}
                            onPageSizeChange={handlePageSizeChange}
                        />
                    )}
                </>
            )}
        </Layout>
    );
}
