import { useCallback, useEffect, useMemo, useState } from "react";
import { Layout } from "../components/layout/Layout";
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

export function MediaChartPage({ mediaType, chartType }) {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const copy = useMemo(() => chartCopy[mediaType][chartType], [mediaType, chartType]);

    const fetchItems = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const nextItems =
                chartType === "top-250"
                    ? await discoveryService.getTop250(mediaType)
                    : await discoveryService.getMostPopularMedia(mediaType);
            setItems(nextItems);
        } catch (err) {
            console.error("Failed to load media chart:", err);
            setError("Failed to load this chart. Please try again.");
        } finally {
            setLoading(false);
        }
    }, [chartType, mediaType]);

    useEffect(() => {
        fetchItems();
    }, [fetchItems]);

    return (
        <Layout>
            <DiscoveryPageHeader
                eyebrow={chartCopy[mediaType].label}
                title={copy.title}
                subtitle={copy.subtitle}
                count={!loading && !error ? items.length : undefined}
            />

            {loading && <DiscoveryLoadingState label={`Loading ${copy.title.toLowerCase()}...`} />}
            {!loading && error && <DiscoveryErrorState message={error} onRetry={fetchItems} />}
            {!loading && !error && items.length === 0 && (
                <DiscoveryEmptyState
                    title="No titles found"
                    message="There are no titles available for this chart yet."
                />
            )}
            {!loading && !error && items.length > 0 && <RankedMediaList items={items} />}
        </Layout>
    );
}
