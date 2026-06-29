import { useCallback, useEffect, useMemo, useState } from "react";
import { Layout } from "../components/layout/Layout";
import { CelebrityList } from "../components/ui/CelebrityList";
import { ChartPagination } from "../components/ui/ChartPagination";
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

const DEFAULT_PAGE_SIZE = 20;

export function CelebChartPage({ chartType }) {
    const [items, setItems] = useState([]);
    const [pageInfo, setPageInfo] = useState(null);
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(DEFAULT_PAGE_SIZE);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const copy = useMemo(() => celebCopy[chartType], [chartType]);

    const fetchItems = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            if (chartType === "born-today") {
                const nextItems = await discoveryService.getBornTodayCelebs();
                setItems(nextItems);
                setPageInfo(null);
            } else {
                const response = await discoveryService.getMostPopularCelebs(page, pageSize);
                setItems(response.items);
                setPageInfo(response);
            }
        } catch (err) {
            console.error("Failed to load celeb chart:", err);
            setError("Failed to load this celeb list. Please try again.");
        } finally {
            setLoading(false);
        }
    }, [chartType, page, pageSize]);

    useEffect(() => {
        fetchItems();
    }, [fetchItems]);

    useEffect(() => {
        setPage(0);
    }, [chartType]);

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
                eyebrow="Celebs"
                title={copy.title}
                subtitle={copy.subtitle}
                count={!loading && !error ? (pageInfo?.totalItems ?? items.length) : undefined}
            />

            {loading && <DiscoveryLoadingState label={`Loading ${copy.title.toLowerCase()}...`} />}
            {!loading && error && <DiscoveryErrorState message={error} onRetry={fetchItems} />}
            {!loading && !error && items.length === 0 && (
                <DiscoveryEmptyState title={copy.emptyTitle} message={copy.emptyMessage} />
            )}
            {!loading && !error && items.length > 0 && (
                <>
                    <CelebrityList items={items} />
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
