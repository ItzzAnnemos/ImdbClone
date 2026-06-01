import { useCallback, useEffect, useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { ChevronLeft, ChevronRight, Film, Search, UserRound } from "lucide-react";
import { Layout } from "../components/layout/Layout";
import { Button } from "../components/ui/Button";
import { searchCatalog } from "../lib/searchService";

const SEARCH_RESULTS_SIZE = 10;

function getResultHref(result) {
    return result.type === "Person" ? `/person/${result.id}` : `/media/${result.id}`;
}

function getResultTypeLabel(type) {
    if (type === "TVSeries") {
        return "TV series";
    }

    return type || "Result";
}

function ResultIcon({ type }) {
    if (type === "Person") {
        return <UserRound className="h-10 w-10 text-muted-foreground" />;
    }

    return <Film className="h-10 w-10 text-muted-foreground" />;
}

function SearchResultRow({ result }) {
    return (
        <Link
            to={getResultHref(result)}
            className="group grid grid-cols-[72px_minmax(0,1fr)] gap-4 border-b border-border px-1 py-4 transition-colors hover:bg-secondary/50 sm:grid-cols-[86px_minmax(0,1fr)] sm:px-3"
        >
            <div className="flex h-28 w-[72px] items-center justify-center overflow-hidden rounded-sm bg-secondary ring-1 ring-border sm:h-32 sm:w-[86px]">
                {result.image ? (
                    <img
                        src={result.image}
                        alt=""
                        className="h-full w-full object-cover"
                        loading="lazy"
                    />
                ) : (
                    <ResultIcon type={result.type} />
                )}
            </div>
            <div className="min-w-0 py-1">
                <h2 className="truncate text-lg font-semibold text-foreground group-hover:text-yellow-500">
                    {result.title}
                </h2>
                <div className="mt-1 flex flex-wrap items-center gap-2 text-sm text-muted-foreground">
                    <span>{getResultTypeLabel(result.type)}</span>
                    {result.score !== null && (
                        <>
                            <span aria-hidden="true">|</span>
                            <span>{Math.round(result.score)}% match</span>
                        </>
                    )}
                </div>
            </div>
        </Link>
    );
}

export function SearchResults() {
    const [searchParams, setSearchParams] = useSearchParams();
    const query = searchParams.get("query")?.trim() ?? "";
    const page = Math.max(Number(searchParams.get("page") ?? 0) || 0, 0);
    const [searchPage, setSearchPage] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchResults = useCallback(async () => {
        if (!query) {
            setSearchPage(null);
            setError(null);
            return;
        }

        try {
            setLoading(true);
            setError(null);
            setSearchPage(await searchCatalog(query, page, SEARCH_RESULTS_SIZE));
        } catch (err) {
            console.error("Failed to load search results:", err);
            setError("Search is unavailable right now. Please try again.");
            setSearchPage(null);
        } finally {
            setLoading(false);
        }
    }, [page, query]);

    useEffect(() => {
        fetchResults();
    }, [fetchResults]);

    const currentPage = (searchPage?.page ?? page) + 1;
    const totalPages = Math.max(searchPage?.totalPages ?? 1, 1);
    const resultCountLabel = useMemo(() => {
        if (!searchPage) {
            return "";
        }

        return `${searchPage.totalResults} result${searchPage.totalResults === 1 ? "" : "s"}`;
    }, [searchPage]);

    const setPage = (nextPage) => {
        setSearchParams({
            query,
            page: String(Math.max(nextPage, 0)),
        });
    };

    return (
        <Layout>
            <div className="mx-auto max-w-5xl">
                <div className="border-b border-border pb-4">
                    <p className="mb-2 text-xs font-semibold uppercase text-yellow-500">
                        IMDbClone search
                    </p>
                    <h1 className="text-3xl font-bold tracking-tight">Search results</h1>
                    {query ? (
                        <p className="mt-2 text-muted-foreground">
                            Results for <span className="font-medium text-foreground">{query}</span>
                            {resultCountLabel && ` - ${resultCountLabel}`}
                        </p>
                    ) : (
                        <p className="mt-2 text-muted-foreground">
                            Enter a search term in the navigation bar.
                        </p>
                    )}
                </div>

                {loading && (
                    <div className="flex items-center gap-3 py-10 text-muted-foreground">
                        <Search className="h-5 w-5 animate-pulse" />
                        Searching...
                    </div>
                )}

                {!loading && error && (
                    <div className="mt-6 rounded-md border border-destructive/30 bg-destructive/10 p-4">
                        <p className="mb-3 text-sm text-destructive">{error}</p>
                        <Button variant="outline" size="sm" onClick={fetchResults}>
                            Retry
                        </Button>
                    </div>
                )}

                {!loading && !error && query && searchPage?.results.length === 0 && (
                    <div className="py-10 text-muted-foreground">No results found.</div>
                )}

                {!loading && !error && searchPage?.results.length > 0 && (
                    <>
                        <section className="divide-y-0">
                            {searchPage.results.map((result) => (
                                <SearchResultRow
                                    key={`${result.type}-${result.id}`}
                                    result={result}
                                />
                            ))}
                        </section>

                        <div className="mt-6 flex items-center justify-between">
                            <Button
                                variant="outline"
                                size="sm"
                                disabled={!searchPage.hasPrevious}
                                onClick={() => setPage(page - 1)}
                                className="gap-2"
                            >
                                <ChevronLeft className="h-4 w-4" />
                                Previous
                            </Button>
                            <span className="text-sm text-muted-foreground">
                                Page {currentPage} of {totalPages}
                            </span>
                            <Button
                                variant="outline"
                                size="sm"
                                disabled={!searchPage.hasNext}
                                onClick={() => setPage(page + 1)}
                                className="gap-2"
                            >
                                Next
                                <ChevronRight className="h-4 w-4" />
                            </Button>
                        </div>
                    </>
                )}
            </div>
        </Layout>
    );
}
