import { useState, useEffect } from "react";
import { useParams, Link, useLocation } from "react-router-dom";
import {
    Star,
    ChevronLeft,
    MessageSquarePlus,
    Loader2,
    Filter,
    ArrowUpDown
} from "lucide-react";
import { Layout } from "../components/layout/Layout";
import { Button } from "../components/ui/Button";
import { ReviewCard } from "../components/ui/ReviewCard";
import { ReviewSidebar } from "../components/ui/ReviewSidebar";
import * as mediaService from "../lib/mediaService";
import * as reviewService from "../lib/reviewService";
import * as ratingService from "../lib/ratingService";
import { useAuth } from "../context/AuthContext";

export function MediaReviews() {
    const { id } = useParams();
    const { user } = useAuth();

    const [media, setMedia] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [userReview, setUserReview] = useState(null);
    const [loading, setLoading] = useState(true);
    const [ratingCount, setRatingCount] = useState(0);
    const [isReviewSidebarOpen, setIsReviewSidebarOpen] = useState(false);
    const [editingReview, setEditingReview] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                // We don't know if it's TV or Movie from the URL /title/:id/reviews
                // But typically our backend can return generic Media info?
                // Let's try movie first, then TV if it fails, or just use a generic endpoint if we had one.
                // For now, let's try to get it from location state or just try both.

                let mediaData = await mediaService.getMediaById(id);

                const reviewsData = await reviewService.getMediaReviews(id);
                const count = await ratingService.getRatingCount(id);

                if (user) {
                    const review = await reviewService.getUserReview(user.id, id);
                    setUserReview(review);
                }

                setMedia(mediaData);
                setReviews(reviewsData);
                setRatingCount(count);
            } catch (err) {
                console.error("Error fetching reviews data:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
        window.scrollTo(0, 0);
    }, [id]);

    const handleReviewSubmit = async (text) => {
        try {
            if (editingReview) {
                await reviewService.updateReview(editingReview.id, text);
            } else {
                await reviewService.addReview({
                    userId: user.id,
                    mediaId: id,
                    reviewText: text
                });
            }
            setIsReviewSidebarOpen(false);
            setEditingReview(null);
            const reviewsData = await reviewService.getMediaReviews(id);
            setReviews(reviewsData);

            const review = await reviewService.getUserReview(user.id, id);
            setUserReview(review);
        } catch (err) {
            console.error("Failed to submit review:", err);
        }
    };

    const handleReviewEdit = (review) => {
        setEditingReview(review);
        setIsReviewSidebarOpen(true);
    };

    const handleReviewDelete = async (reviewId) => {
        if (!window.confirm("Are you sure you want to delete your review?")) return;
        try {
            await reviewService.deleteReview(reviewId);
            const reviewsData = await reviewService.getMediaReviews(id);
            setReviews(reviewsData);
            setUserReview(null);
        } catch (err) {
            console.error("Failed to delete review:", err);
        }
    };

    if (loading) {
        return (
            <Layout>
                <div className="flex h-[70vh] items-center justify-center">
                    <Loader2 className="h-12 w-12 animate-spin text-primary" />
                </div>
            </Layout>
        );
    }

    if (!media) return null;

    return (
        <Layout>
            <div className="container mx-auto px-4 py-8">
                {/* Header Section */}
                <div className="flex flex-col md:flex-row gap-8 mb-12 bg-card border border-border p-6 rounded-2xl shadow-sm">
                    <div className="w-32 h-48 flex-shrink-0 rounded-lg overflow-hidden shadow-md">
                        <img
                            src={media.image}
                            alt={media.title}
                            className="w-full h-full object-cover"
                        />
                    </div>
                    <div className="flex-1">
                        <Link
                            to={`/media/${id}`}
                            className="text-muted-foreground hover:text-primary flex items-center gap-1 text-sm font-bold mb-2 transition-colors"
                        >
                            <ChevronLeft className="h-4 w-4" /> Back to details
                        </Link>
                        <h1 className="text-4xl font-black mb-4">{media.title} <span className="text-muted-foreground font-normal">({media.year})</span></h1>

                        <div className="flex flex-wrap items-center gap-6">
                            <div className="flex items-center gap-2">
                                <div className="p-1.5 bg-yellow-400 rounded-lg">
                                    <Star className="h-5 w-5 text-black fill-current" />
                                </div>
                                <div>
                                    <div className="text-xl font-bold leading-none">{media.rating?.toFixed(1) || "N/A"}<span className="text-muted-foreground text-sm">/10</span></div>
                                    <div className="text-[10px] text-muted-foreground font-bold uppercase">{ratingCount.toLocaleString()} RATINGS</div>
                                </div>
                            </div>

                            <div className="h-10 w-px bg-border hidden md:block" />

                            <div>
                                <div className="text-xl font-bold leading-none">{reviews.length}</div>
                                <div className="text-[10px] text-muted-foreground font-bold uppercase">USER REVIEWS</div>
                            </div>

                            {user && !userReview && (
                                <Button
                                    onClick={() => setIsReviewSidebarOpen(true)}
                                    className="md:ml-auto bg-yellow-400 hover:bg-yellow-500 text-black border-none font-bold flex items-center gap-2"
                                >
                                    <MessageSquarePlus className="h-5 w-5" />
                                    Write a Review
                                </Button>
                            )}
                        </div>
                    </div>
                </div>

                {/* Reviews List Section */}
                <div className="max-w-4xl mx-auto">
                    <div className="flex items-center justify-between mb-8 pb-4 border-b border-border">
                        <h2 className="text-2xl font-bold">User Reviews</h2>
                    </div>

                    {reviews.length > 0 ? (
                        <div className="space-y-6">
                            {reviews.map((review, i) => (
                                <ReviewCard
                                    key={i}
                                    review={review}
                                    onEdit={handleReviewEdit}
                                    onDelete={handleReviewDelete}
                                />
                            ))}
                        </div>
                    ) : (
                        <div className="bg-muted/30 border border-dashed border-border rounded-2xl p-20 text-center">
                            <p className="text-xl text-muted-foreground mb-6">No one has reviewed this title yet.</p>
                            {user && !userReview && (
                                <Button
                                    size="lg"
                                    onClick={() => setIsReviewSidebarOpen(true)}
                                    className="bg-yellow-400 hover:bg-yellow-500 text-black border-none"
                                >
                                    Be the first to review
                                </Button>
                            )}
                        </div>
                    )}
                </div>
            </div>

            <ReviewSidebar
                isOpen={isReviewSidebarOpen}
                onClose={() => {
                    setIsReviewSidebarOpen(false);
                    setEditingReview(null);
                }}
                onSubmit={handleReviewSubmit}
                title={media.title}
                initialReview={editingReview}
            />
        </Layout>
    );
}
