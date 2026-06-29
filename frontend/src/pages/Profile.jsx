import { useEffect, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import { AlertCircle, Bookmark, Cog, Loader2, MessageSquareText, Star, User } from "lucide-react";
import { Layout } from "../components/layout/Layout";
import { Button } from "../components/ui/Button";
import { MediaSlider } from "../components/ui/MediaSlider";
import { PasswordChangeModal } from "../components/ui/PasswordChangeModal";
import { ProfileRatingCard } from "../components/ui/ProfileRatingCard";
import { ProfileReviewCard } from "../components/ui/ProfileReviewCard";
import { ProfileWatchlistCard } from "../components/ui/ProfileWatchlistCard";
import { useAuth } from "../context/AuthContext";
import * as ratingService from "../lib/ratingService";
import * as reviewService from "../lib/reviewService";
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

function StatTile({ icon: Icon, value, label, detail }) {
    return (
        <div className="group relative overflow-hidden rounded-xl border border-border bg-card px-2 py-2 text-left shadow-sm transition hover:border-primary/30 hover:shadow-md sm:px-4 sm:py-3">
            <div className="flex items-start justify-between gap-1.5 sm:gap-3">
                <div className="min-w-0">
                    <p className="text-lg font-bold leading-none text-foreground sm:text-2xl">
                        {value}
                    </p>
                    <p className="mt-1 truncate text-[10px] font-semibold uppercase tracking-wide text-muted-foreground sm:text-xs">
                        {label}
                    </p>
                </div>
                <div className="flex h-6 w-6 shrink-0 items-center justify-center rounded-md bg-yellow-500/15 text-yellow-500 sm:h-9 sm:w-9 sm:rounded-lg">
                    <Icon className="h-3 w-3 sm:h-4 sm:w-4" />
                </div>
            </div>
            {detail && (
                <p className="mt-2 truncate text-[10px] text-muted-foreground sm:text-xs">
                    {detail}
                </p>
            )}
        </div>
    );
}

function StatTileSkeleton() {
    return (
        <div className="rounded-xl border border-border bg-card px-2 py-2 shadow-sm sm:px-4 sm:py-3">
            <div className="flex items-start justify-between gap-1.5 sm:gap-3">
                <div className="min-w-0 space-y-2">
                    <div className="h-5 w-9 animate-pulse rounded-md bg-muted sm:h-7 sm:w-12" />
                    <div className="h-3 w-12 animate-pulse rounded-md bg-muted sm:w-20" />
                </div>
                <div className="h-6 w-6 shrink-0 animate-pulse rounded-md bg-muted sm:h-9 sm:w-9 sm:rounded-lg" />
            </div>
            <div className="mt-2 h-3 w-14 animate-pulse rounded-md bg-muted sm:mt-3 sm:w-24" />
        </div>
    );
}

export function Profile() {
    const { user } = useAuth();
    const [recommendations, setRecommendations] = useState([]);
    const [watchlist, setWatchlist] = useState([]);
    const [ratings, setRatings] = useState([]);
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [passwordForm, setPasswordForm] = useState({
        currentPassword: "",
        newPassword: "",
        repeatNewPassword: "",
    });
    const [passwordSaving, setPasswordSaving] = useState(false);
    const [passwordMessage, setPasswordMessage] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);

    useEffect(() => {
        if (!user?.id || !user?.username) return;
        let cancelled = false;

        async function fetchProfile() {
            try {
                setLoading(true);
                setError(null);
                const [recommendationData, watchlistData, ratingData, reviewData] =
                    await Promise.all([
                        userMediaService.getRecommendations(user.id),
                        userMediaService.getWatchlist(user.username),
                        ratingService.getRatingsByUser(user.id),
                        reviewService.getReviewsByUser(user.id),
                    ]);
                if (!cancelled) {
                    setRecommendations(recommendationData);
                    setWatchlist(watchlistData);
                    setRatings(
                        [...ratingData].sort(
                            (a, b) => new Date(b.createdAt || 0) - new Date(a.createdAt || 0),
                        ),
                    );
                    setReviews(
                        [...reviewData].sort(
                            (a, b) => new Date(b.createdAt || 0) - new Date(a.createdAt || 0),
                        ),
                    );
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

    const handlePasswordChange = (field, value) => {
        setPasswordForm((current) => ({ ...current, [field]: value }));
        setPasswordError("");
        setPasswordMessage("");
    };

    const handlePasswordSubmit = async (event) => {
        event.preventDefault();
        setPasswordError("");
        setPasswordMessage("");

        if (passwordForm.newPassword !== passwordForm.repeatNewPassword) {
            setPasswordError("New password and confirmation do not match.");
            return;
        }

        try {
            setPasswordSaving(true);
            await userMediaService.changePassword(user.id, passwordForm);
            setPasswordForm({
                currentPassword: "",
                newPassword: "",
                repeatNewPassword: "",
            });
            setPasswordMessage("");
            setIsPasswordModalOpen(false);
        } catch (err) {
            console.error("Failed to change password:", err);
            setPasswordError(
                "Could not change password. Check your current password and try again.",
            );
        } finally {
            setPasswordSaving(false);
        }
    };

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    const averageUserRating =
        ratings.length > 0
            ? ratings.reduce((sum, item) => sum + item.rating, 0) / ratings.length
            : null;
    const recentActivity = [
        ...ratings.map((rating) => ({
            type: "rating",
            date: rating.createdAt,
            item: rating,
        })),
        ...reviews.map((review) => ({
            type: "review",
            date: review.createdAt,
            item: review,
        })),
    ]
        .sort((a, b) => new Date(b.date || 0) - new Date(a.date || 0))
        .slice(0, 6);

    const openPasswordModal = () => {
        setPasswordError("");
        setPasswordMessage("");
        setIsPasswordModalOpen(true);
    };

    const closePasswordModal = () => {
        if (passwordSaving) {
            return;
        }

        setIsPasswordModalOpen(false);
        setPasswordError("");
        setPasswordMessage("");
        setPasswordForm({
            currentPassword: "",
            newPassword: "",
            repeatNewPassword: "",
        });
    };

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
                            {(user.firstName || user.lastName) && (
                                <p className="mt-1 text-sm text-muted-foreground">
                                    {[user.firstName, user.lastName].filter(Boolean).join(" ")}
                                </p>
                            )}
                        </div>
                        <button
                            type="button"
                            onClick={openPasswordModal}
                            className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full border border-border bg-background text-muted-foreground shadow-sm transition hover:bg-secondary hover:text-foreground"
                            aria-label="Open account settings"
                            title="Account settings"
                        >
                            <Cog className="h-5 w-5" />
                        </button>
                    </div>

                    <div className="grid w-full grid-cols-3 gap-2 sm:gap-3 md:w-auto">
                        {loading ? (
                            Array.from({ length: 3 }).map((_, index) => (
                                <StatTileSkeleton key={index} />
                            ))
                        ) : (
                            <>
                                <StatTile
                                    icon={Bookmark}
                                    value={watchlist.length}
                                    label="Saved"
                                    detail="In watchlist"
                                />
                                <StatTile
                                    icon={Star}
                                    value={
                                        averageUserRating !== null
                                            ? averageUserRating.toFixed(1)
                                            : "N/A"
                                    }
                                    label="Avg rating"
                                    detail={`${ratings.length} rated`}
                                />
                                <StatTile
                                    icon={MessageSquareText}
                                    value={reviews.length}
                                    label="Reviews"
                                    detail="Written"
                                />
                            </>
                        )}
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
                    <section className="grid gap-8 xl:grid-cols-[minmax(0,1fr)_360px]">
                        <div>
                            <SectionHeader
                                icon={MessageSquareText}
                                title="Recent Activity"
                                count={ratings.length + reviews.length}
                            />
                            {recentActivity.length > 0 ? (
                                <div className="flex flex-col gap-4">
                                    {recentActivity.map((activity) => (
                                        <div
                                            key={`${activity.type}-${
                                                activity.item.id ||
                                                activity.item.mediaId ||
                                                activity.date
                                            }`}
                                        >
                                            {activity.type === "rating" ? (
                                                <ProfileRatingCard rating={activity.item} />
                                            ) : (
                                                <ProfileReviewCard review={activity.item} />
                                            )}
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <EmptyPanel>Your ratings and reviews will appear here.</EmptyPanel>
                            )}
                            <div className="mt-4 flex flex-wrap gap-2">
                                <Link to="/ratings">
                                    <Button variant="ghost" size="sm">
                                        View ratings
                                    </Button>
                                </Link>
                            </div>
                        </div>

                        <aside>
                            <SectionHeader
                                icon={Bookmark}
                                title="Watchlist"
                                count={watchlist.length}
                                to="/watchlist"
                            />
                            {watchlist.length > 0 ? (
                                <div className="flex flex-col gap-4">
                                    {watchlist.slice(0, 3).map((media) => (
                                        <ProfileWatchlistCard key={media.id} media={media} />
                                    ))}
                                </div>
                            ) : (
                                <EmptyPanel>Your saved titles will appear here.</EmptyPanel>
                            )}
                        </aside>
                    </section>

                    <section>
                        {recommendations.length > 0 ? (
                            <MediaSlider title="Recommendations" items={recommendations} />
                        ) : (
                            <EmptyPanel>
                                Rate or save a few titles to start shaping recommendations.
                            </EmptyPanel>
                        )}
                    </section>
                </div>
            )}

            <PasswordChangeModal
                isOpen={isPasswordModalOpen}
                form={passwordForm}
                saving={passwordSaving}
                message={passwordMessage}
                error={passwordError}
                onChange={handlePasswordChange}
                onClose={closePasswordModal}
                onSubmit={handlePasswordSubmit}
            />
        </Layout>
    );
}
