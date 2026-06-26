import { motion } from "framer-motion";
import { CalendarDays, ChevronRight, MessageSquareText } from "lucide-react";
import { Link } from "react-router-dom";
import { cn } from "../../lib/utils";

function formatDate(date) {
    if (!date) {
        return "Recently";
    }

    return new Intl.DateTimeFormat("en", {
        month: "short",
        day: "numeric",
        year: "numeric",
    }).format(new Date(date));
}

export function ProfileReviewCard({ review, className }) {
    const content = (
        <motion.div
            whileTap={{ scale: 0.98 }}
            className={cn(
                "group flex min-h-36 overflow-hidden rounded-xl border border-border bg-card shadow-sm transition hover:border-primary/30 hover:shadow-lg",
                className,
            )}
        >
            <div className="w-24 shrink-0 overflow-hidden bg-secondary">
                <img
                    src={review.image || "https://placehold.co/200x300?text=No+Image"}
                    alt={review.mediaTitle}
                    className="h-full w-full object-cover transition duration-300 group-hover:scale-105"
                    loading="lazy"
                />
            </div>

            <div className="flex min-w-0 flex-1 flex-col justify-between p-4">
                <div className="min-w-0">
                    <div className="mb-2 flex items-start justify-between gap-3">
                        <div className="min-w-0">
                            <p className="mb-1 flex items-center gap-1.5 text-[10px] font-bold uppercase tracking-wide text-yellow-500">
                                <MessageSquareText className="h-3.5 w-3.5" />
                                Review
                            </p>
                            <h3 className="line-clamp-1 text-base font-semibold text-foreground">
                                {review.mediaTitle}
                            </h3>
                        </div>
                        {review.isEdited && (
                            <span className="shrink-0 rounded-full bg-secondary px-2 py-0.5 text-[10px] font-semibold uppercase text-muted-foreground">
                                Edited
                            </span>
                        )}
                    </div>

                    <p className="mb-3 flex items-center gap-1.5 text-sm text-muted-foreground">
                        <CalendarDays className="h-3.5 w-3.5" />
                        Reviewed on {formatDate(review.createdAt)}
                    </p>

                    <p className="line-clamp-2 text-sm leading-6 text-muted-foreground">
                        {review.reviewText}
                    </p>
                </div>

                <span className="mt-3 inline-flex items-center text-xs font-semibold text-primary opacity-0 transition group-hover:opacity-100">
                    View details <ChevronRight className="ml-1 h-3.5 w-3.5" />
                </span>
            </div>
        </motion.div>
    );

    return review.mediaId ? <Link to={`/media/${review.mediaId}`}>{content}</Link> : content;
}
