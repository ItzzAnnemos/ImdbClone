import { useEffect, useMemo, useState } from "react";
import { Navigate } from "react-router-dom";
import { Bookmark, Film, AlertCircle, Loader2, ArrowUpDown } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import { Layout } from "../components/layout/Layout";
import { WatchlistCard } from "../components/ui/WatchlistCard";
import { useAuth } from "../context/AuthContext";
import api from "../lib/api";
import { MediaModel } from "../models";
import { cn } from "../lib/utils";

const TYPE_FILTERS = [
    { value: "all", label: "All" },
    { value: "movie", label: "Movies" },
    { value: "tv", label: "TV Shows" },
];

const SORT_OPTIONS = [
    { value: "added", label: "Date Added" },
    { value: "releaseYear", label: "Release Year" },
    { value: "rating", label: "Rating" },
];

const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
        opacity: 1,
        transition: { staggerChildren: 0.08 },
    },
};

const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.4 } },
};

export function Watchlist() {
    const { user } = useAuth();
    const username = user?.username;
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [typeFilter, setTypeFilter] = useState("all");
    const [sortBy, setSortBy] = useState("added");

    useEffect(() => {
        if (!username) return;
        let cancelled = false;

        async function fetchWatchlist() {
            try {
                setLoading(true);
                setError(null);
                const response = await api.get(
                    `/api/user/${encodeURIComponent(username)}/watchlist`,
                );
                if (!cancelled) setMovies(MediaModel.fromApiList(response.data));
            } catch (err) {
                if (!cancelled) {
                    setError(
                        err?.response?.data?.message ||
                            "Failed to load your watchlist. Please try again.",
                    );
                }
            } finally {
                if (!cancelled) setLoading(false);
            }
        }

        fetchWatchlist();
        return () => {
            cancelled = true;
        };
    }, [username]);

    const visibleMovies = useMemo(() => {
        const filtered =
            typeFilter === "all" ? movies : movies.filter((m) => m.type === typeFilter);

        const sorted = [...filtered];
        switch (sortBy) {
            case "releaseYear":
                sorted.sort((a, b) => (b.releaseYear ?? 0) - (a.releaseYear ?? 0));
                break;
            case "rating":
                sorted.sort((a, b) => (b.rating ?? 0) - (a.rating ?? 0));
                break;
            case "added":
            default:
                sorted.reverse();
                break;
        }
        return sorted;
    }, [movies, typeFilter, sortBy]);

    // Redirect unauthenticated users
    // This must be after ALL hooks to avoid Rule of Hooks errors
    if (!user) {
        return <Navigate to="/login" replace />;
    }

    return (
        <Layout>
            {/* Page header */}
            <div className="mb-8 flex items-center gap-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10">
                    <Bookmark className="h-5 w-5 text-primary" />
                </div>
                <div>
                    <h1 className="text-2xl font-bold text-foreground">My Watchlist</h1>
                    <p className="text-sm text-muted-foreground">
                        Logged in as{" "}
                        <span className="font-medium text-foreground">{user.username}</span>
                    </p>
                </div>
                {!loading && !error && movies.length > 0 && (
                    <span className="ml-auto rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary">
                        {visibleMovies.length} {visibleMovies.length === 1 ? "item" : "items"}
                    </span>
                )}
            </div>

            {/* Filter + sort controls */}
            {!loading && !error && movies.length > 0 && (
                <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                    <div className="flex flex-wrap gap-2">
                        {TYPE_FILTERS.map((option) => (
                            <button
                                key={option.value}
                                onClick={() => setTypeFilter(option.value)}
                                className={cn(
                                    "rounded-full border px-3 py-1.5 text-sm font-medium transition",
                                    typeFilter === option.value
                                        ? "border-yellow-400 bg-yellow-400 text-black"
                                        : "border-border bg-secondary text-secondary-foreground hover:border-yellow-400 hover:text-yellow-400",
                                )}
                            >
                                {option.label}
                            </button>
                        ))}
                    </div>

                    <label className="flex items-center gap-2 text-sm text-muted-foreground">
                        <ArrowUpDown className="h-4 w-4" />
                        <span className="sr-only sm:not-sr-only">Sort by</span>
                        <select
                            value={sortBy}
                            onChange={(e) => setSortBy(e.target.value)}
                            className="rounded-lg border border-border bg-secondary px-3 py-1.5 text-sm font-medium text-secondary-foreground outline-none transition hover:border-yellow-400 focus:border-yellow-400"
                        >
                            {SORT_OPTIONS.map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.label}
                                </option>
                            ))}
                        </select>
                    </label>
                </div>
            )}

            {/* Loading state */}
            {loading && (
                <div className="flex min-h-[40vh] flex-col items-center justify-center gap-4 text-muted-foreground">
                    <Loader2 className="h-10 w-10 animate-spin text-primary" />
                    <p className="text-sm">Loading your watchlist…</p>
                </div>
            )}

            {/* Error state */}
            {!loading && error && (
                <div className="flex min-h-[40vh] flex-col items-center justify-center gap-3 text-center">
                    <AlertCircle className="h-10 w-10 text-destructive" />
                    <p className="text-sm text-destructive">{error}</p>
                    <button
                        onClick={() => window.location.reload()}
                        className="mt-2 rounded-lg bg-primary/10 px-4 py-2 text-sm font-medium text-primary transition hover:bg-primary/20"
                    >
                        Retry
                    </button>
                </div>
            )}

            {/* Empty state */}
            {!loading && !error && movies.length === 0 && (
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    className="flex min-h-[40vh] flex-col items-center justify-center gap-4 text-center"
                >
                    <div className="flex h-20 w-20 items-center justify-center rounded-full bg-card shadow-lg">
                        <Film className="h-10 w-10 text-muted-foreground" />
                    </div>
                    <div>
                        <h2 className="text-lg font-semibold text-foreground">
                            Your watchlist is empty
                        </h2>
                        <p className="mt-1 text-sm text-muted-foreground">
                            Start exploring movies and TV series and add them to your watchlist.
                        </p>
                    </div>
                </motion.div>
            )}

            {/* No results for the current filter */}
            {!loading && !error && movies.length > 0 && visibleMovies.length === 0 && (
                <div className="flex min-h-[30vh] flex-col items-center justify-center gap-2 text-center">
                    <p className="text-sm text-muted-foreground">
                        No {TYPE_FILTERS.find((f) => f.value === typeFilter)?.label.toLowerCase()}{" "}
                        in your watchlist yet.
                    </p>
                </div>
            )}

            {/* Movie list */}
            {!loading && !error && visibleMovies.length > 0 && (
                <AnimatePresence>
                    <motion.div
                        variants={containerVariants}
                        initial="hidden"
                        animate="visible"
                        className="flex flex-col gap-4"
                    >
                        {visibleMovies.map((movie) => (
                            <motion.div key={movie.id} variants={itemVariants}>
                                <WatchlistCard media={movie} />
                            </motion.div>
                        ))}
                    </motion.div>
                </AnimatePresence>
            )}
        </Layout>
    );
}
