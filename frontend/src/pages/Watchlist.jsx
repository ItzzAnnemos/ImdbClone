import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { Bookmark, Film, AlertCircle, Loader2 } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import { Layout } from "../components/layout/Layout";
import { WatchlistCard } from "../components/ui/WatchlistCard";
import { useAuth } from "../context/AuthContext";
import api from "../lib/api";
import { MediaModel } from "../models";

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
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Redirect unauthenticated users
    if (!user) {
        return <Navigate to="/login" replace />;
    }

    useEffect(() => {
        let cancelled = false;

        async function fetchWatchlist() {
            try {
                setLoading(true);
                setError(null);
                const response = await api.get(
                    `/api/user/${encodeURIComponent(user.username)}/watchlist`,
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
    }, [user.username]);

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
                        <span className="font-medium text-foreground">
                            {user.username}
                        </span>
                    </p>
                </div>
                {!loading && !error && movies.length > 0 && (
                    <span className="ml-auto rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary">
                        {movies.length} {movies.length === 1 ? "movie" : "movies"}
                    </span>
                )}
            </div>

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
                            Start exploring movies and add them to your watchlist.
                        </p>
                    </div>
                </motion.div>
            )}

            {/* Movie list */}
            {!loading && !error && movies.length > 0 && (
                <AnimatePresence>
                    <motion.div
                        variants={containerVariants}
                        initial="hidden"
                        animate="visible"
                        className="flex flex-col gap-4"
                    >
                        {movies.map((movie) => (
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
