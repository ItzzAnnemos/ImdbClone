import { useEffect, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import { AlertCircle, Bookmark, Loader2, Sparkles, Star, User } from "lucide-react";
import { Layout } from "../components/layout/Layout";
import { Button } from "../components/ui/Button";
import { MediaCard } from "../components/ui/MediaCard";
import { RatedMediaCard } from "../components/ui/RatedMediaCard";
import { WatchlistCard } from "../components/ui/WatchlistCard";
import { useAuth } from "../context/AuthContext";
import * as ratingService from "../lib/ratingService";
import * as userMediaService from "../lib/userMediaService";

function SectionHeader({ icon: Icon, title, count, to }) {
    return (
        <div className="mb-4 flex items-center justify-between gap-4">
            <div className="flex min-w-0 items-center gap-3">
                <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-primary/10">
                    <Icon className="h-4 w-4 text-primary" />
                </div>
                <div className="min-w-0">
                    <h2 className="text-lg font-semibold text-foreground">{title}</h2>
                    <p className="text-sm text-muted-foreground">
                        {count} {count === 1 ? "item" : "items"}
                    </p>
                </div>
            </div>
            {to && (
                <Link to={to}>
                    <Button variant="ghost" size="sm">
                        View all
                    </Button>
                </Link>
            )}
        </div>
    );
}

function EmptyPanel({ children }) {
    return (
        <div className="rounded-xl border border-dashed border-border bg-muted/20 px-4 py-8 text-center text-sm text-muted-foreground">
            {children}
        </div>
    );
}

export function Profile() {
    const { user } = useAuth();
    const [recommendations, setRecommendations] = useState([]);
    const [watchlist, setWatchlist] = useState([]);
    const [ratings, setRatings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!user?.id || !user?.username) return;
        let cancelled = false;

        async function fetchProfile() {
            try {
                setLoading(true);
                setError(null);
                const [recommendationData, watchlistData, ratingData] = await Promise.all([
                    userMediaService.getRecommendations(user.id),
                    userMediaService.getWatchlist(user.username),
                    ratingService.getRatingsByUser(user.id),
                ]);
                if (!cancelled) {
                    setRecommendations(recommendationData);
                    setWatchlist(watchlistData);
                    setRatings(ratingData);
                }
            } catch (err) {
                if (!cancelled) {
                    setError(
                        err?.response?.data?.message ||
                            "Failed to load your profile. Please try again.",
                    );
                }
            } finally {
                if (!cancelled) setLoading(false);
            }
        }

        fetchProfile();
        return () => {
            cancelled = true;
        };
    }, [user?.id, user?.username]);

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    const averageUserRating =
        ratings.length > 0
            ? ratings.reduce((sum, item) => sum + item.rating, 0) / ratings.length
            : null;

    return (
        <Layout>
            <div className="mb-8 border-b border-border pb-6">
                <div className="flex flex-col gap-5 md:flex-row md:items-end md:justify-between">
                    <div className="flex items-center gap-4">
                        <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-primary text-primary-foreground">
                            <User className="h-8 w-8" />
                        </div>
                        <div>
                            <p className="text-sm font-medium uppercase text-muted-foreground">
                                Profile
                            </p>
                            <h1 className="text-3xl font-bold text-foreground">{user.username}</h1>
                        </div>
                    </div>

                    <div className="grid grid-cols-3 gap-3 text-center">
                        <div className="rounded-lg border border-border px-4 py-3">
                            <p className="text-xl font-bold text-foreground">
                                {recommendations.length}
                            </p>
                            <p className="text-xs text-muted-foreground">Picks</p>
                        </div>
                        <div className="rounded-lg border border-border px-4 py-3">
                            <p className="text-xl font-bold text-foreground">{watchlist.length}</p>
                            <p className="text-xs text-muted-foreground">Saved</p>
                        </div>
                        <div className="rounded-lg border border-border px-4 py-3">
                            <p className="text-xl font-bold text-foreground">
                                {averageUserRating ? averageUserRating.toFixed(1) : "N/A"}
                            </p>
                            <p className="text-xs text-muted-foreground">Avg rating</p>
                        </div>
                    </div>
                </div>
            </div>

            {loading && (
                <div className="flex min-h-[45vh] flex-col items-center justify-center gap-4 text-muted-foreground">
                    <Loader2 className="h-10 w-10 animate-spin text-primary" />
                    <p className="text-sm">Loading your profile...</p>
                </div>
            )}

            {!loading && error && (
                <div className="flex min-h-[45vh] flex-col items-center justify-center gap-3 text-center">
                    <AlertCircle className="h-10 w-10 text-destructive" />
                    <p className="text-sm text-destructive">{error}</p>
                </div>
            )}

            {!loading && !error && (
                <div className="space-y-10">
                    <section>
                        <SectionHeader
                            icon={Sparkles}
                            title="Recommendations"
                            count={recommendations.length}
                        />
                        {recommendations.length > 0 ? (
                            <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
                                {recommendations.slice(0, 8).map((media) => (
                                    <MediaCard key={media.id} {...media} />
                                ))}
                            </div>
                        ) : (
                            <EmptyPanel>
                                Rate or save a few titles to start shaping recommendations.
                            </EmptyPanel>
                        )}
                    </section>

                    <section className="grid gap-8 xl:grid-cols-[minmax(0,1.1fr)_minmax(360px,0.9fr)]">
                        <div>
                            <SectionHeader
                                icon={Bookmark}
                                title="Watchlist"
                                count={watchlist.length}
                                to="/watchlist"
                            />
                            {watchlist.length > 0 ? (
                                <div className="space-y-4">
                                    {watchlist.slice(0, 4).map((media) => (
                                        <WatchlistCard key={media.id} media={media} />
                                    ))}
                                </div>
                            ) : (
                                <EmptyPanel>Your saved titles will appear here.</EmptyPanel>
                            )}
                        </div>

                        <div>
                            <SectionHeader
                                icon={Star}
                                title="Ratings"
                                count={ratings.length}
                                to="/ratings"
                            />
                            {ratings.length > 0 ? (
                                <div className="space-y-4">
                                    {ratings.slice(0, 5).map((rating) => (
                                        <RatedMediaCard
                                            key={`${rating.mediaId}-${rating.createdAt}`}
                                            rating={rating}
                                            compact
                                        />
                                    ))}
                                </div>
                            ) : (
                                <EmptyPanel>
                                    Your rated movies and TV series will appear here.
                                </EmptyPanel>
                            )}
                        </div>
                    </section>
                </div>
            )}
        </Layout>
    );
}
