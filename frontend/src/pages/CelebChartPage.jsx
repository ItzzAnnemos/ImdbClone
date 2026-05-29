import { useCallback, useEffect, useMemo, useState } from "react";
import { Layout } from "../components/layout/Layout";
import { CelebrityList } from "../components/ui/CelebrityList";
import { DiscoveryPageHeader } from "../components/ui/DiscoveryPageHeader";
import {
    DiscoveryEmptyState,
    DiscoveryErrorState,
    DiscoveryLoadingState,
} from "../components/ui/DiscoveryState";
import * as discoveryService from "../lib/discoveryService";

const celebCopy = {
    "born-today": {
        title: "Born Today",
        subtitle: "Celebrities in the IMDbClone database with birthdays today.",
        emptyTitle: "No birthdays today",
        emptyMessage: "No celebrities in the current dataset have a birthday today.",
    },
    "most-popular": {
        title: "Most Popular Celebs",
        subtitle: "People ranked by how many movie and TV credits they have in IMDbClone.",
        emptyTitle: "No celebrities found",
        emptyMessage: "There are no celebrities available yet.",
    },
};

export function CelebChartPage({ chartType }) {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const copy = useMemo(() => celebCopy[chartType], [chartType]);

    const fetchItems = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const nextItems =
                chartType === "born-today"
                    ? await discoveryService.getBornTodayCelebs()
                    : await discoveryService.getMostPopularCelebs();
            setItems(nextItems);
        } catch (err) {
            console.error("Failed to load celeb chart:", err);
            setError("Failed to load this celeb list. Please try again.");
        } finally {
            setLoading(false);
        }
    }, [chartType]);

    useEffect(() => {
        fetchItems();
    }, [fetchItems]);

    return (
        <Layout>
            <DiscoveryPageHeader
                eyebrow="Celebs"
                title={copy.title}
                subtitle={copy.subtitle}
                count={!loading && !error ? items.length : undefined}
            />

            {loading && <DiscoveryLoadingState label={`Loading ${copy.title.toLowerCase()}...`} />}
            {!loading && error && <DiscoveryErrorState message={error} onRetry={fetchItems} />}
            {!loading && !error && items.length === 0 && (
                <DiscoveryEmptyState title={copy.emptyTitle} message={copy.emptyMessage} />
            )}
            {!loading && !error && items.length > 0 && <CelebrityList items={items} />}
        </Layout>
    );
}
