import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { AlertCircle, Loader2, Star } from "lucide-react";
import { AnimatePresence, motion } from "framer-motion";
import { Layout } from "../components/layout/Layout";
import { RatedMediaCard } from "../components/ui/RatedMediaCard";
import { useAuth } from "../context/AuthContext";
import * as ratingService from "../lib/ratingService";

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

export function Ratings() {
    const { user } = useAuth();
    const [ratings, setRatings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!user?.id) return;
        let cancelled = false;

        async function fetchRatings() {
            try {
                setLoading(true);
                setError(null);
                const data = await ratingService.getRatingsByUser(user.id);
                if (!cancelled) setRatings(data);
            } catch (err) {
                if (!cancelled) {
                    setError(
                        err?.response?.data?.message ||
                            "Failed to load your ratings. Please try again.",
                    );
                }
            } finally {
                if (!cancelled) setLoading(false);
            }
        }

        fetchRatings();
        return () => {
            cancelled = true;
        };
    }, [user?.id]);

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    return (
        <Layout>
            <div className="mb-8 flex items-center gap-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-yellow-400/15">
                    <Star className="h-5 w-5 fill-yellow-500 text-yellow-500" />
                </div>
                <div>
                    <h1 className="text-2xl font-bold text-foreground">My Ratings</h1>
                    <p className="text-sm text-muted-foreground">
                        Movies and TV series rated by{" "}
                        <span className="font-medium text-foreground">{user.username}</span>
                    </p>
                </div>
                {!loading && !error && ratings.length > 0 && (
                    <span className="ml-auto rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary">
                        {ratings.length} {ratings.length === 1 ? "rating" : "ratings"}
                    </span>
                )}
            </div>

            {loading && (
                <div className="flex min-h-[40vh] flex-col items-center justify-center gap-4 text-muted-foreground">
                    <Loader2 className="h-10 w-10 animate-spin text-primary" />
                    <p className="text-sm">Loading your ratings...</p>
                </div>
            )}

            {!loading && error && (
                <div className="flex min-h-[40vh] flex-col items-center justify-center gap-3 text-center">
                    <AlertCircle className="h-10 w-10 text-destructive" />
                    <p className="text-sm text-destructive">{error}</p>
                </div>
            )}

            {!loading && !error && ratings.length === 0 && (
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    className="flex min-h-[40vh] flex-col items-center justify-center gap-4 text-center"
                >
                    <div className="flex h-20 w-20 items-center justify-center rounded-full bg-card shadow-lg">
                        <Star className="h-10 w-10 text-muted-foreground" />
                    </div>
                    <div>
                        <h2 className="text-lg font-semibold text-foreground">No ratings yet</h2>
                        <p className="mt-1 text-sm text-muted-foreground">
                            Rate movies and TV series to build your taste profile.
                        </p>
                    </div>
                </motion.div>
            )}

            {!loading && !error && ratings.length > 0 && (
                <AnimatePresence>
                    <motion.div
                        variants={containerVariants}
                        initial="hidden"
                        animate="visible"
                        className="grid gap-4 lg:grid-cols-2"
                    >
                        {ratings.map((rating) => (
                            <motion.div
                                key={`${rating.mediaId}-${rating.createdAt}`}
                                variants={itemVariants}
                            >
                                <RatedMediaCard rating={rating} />
                            </motion.div>
                        ))}
                    </motion.div>
                </AnimatePresence>
            )}
        </Layout>
    );
}
