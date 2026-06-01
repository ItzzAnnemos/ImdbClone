import { motion } from "framer-motion";
import { CalendarDays, Star } from "lucide-react";
import { Link } from "react-router-dom";
import { cn, formatRating } from "../../lib/utils";

export function RatedMediaCard({ rating, compact = false, className }) {
    const ratedAt = rating.createdAt
        ? new Intl.DateTimeFormat("en", {
              month: "short",
              day: "numeric",
              year: "numeric",
          }).format(new Date(rating.createdAt))
        : null;

    return (
        <Link to={`/media/${rating.mediaId}`}>
            <motion.div
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
                className={cn(
                    "group flex overflow-hidden rounded-xl border border-border bg-card shadow-sm transition hover:shadow-xl",
                    compact ? "min-h-32" : "min-h-40",
                    className,
                )}
            >
                <div className={cn("shrink-0 overflow-hidden", compact ? "w-24" : "w-28")}>
                    <img
                        src={rating.image || "https://placehold.co/200x300?text=No+Image"}
                        alt={rating.mediaTitle}
                        className="h-full w-full object-cover transition duration-300 group-hover:scale-105"
                        loading="lazy"
                    />
                </div>

                <div className="flex min-w-0 flex-1 flex-col justify-between p-4">
                    <div className="min-w-0">
                        <div className="mb-2 flex items-start justify-between gap-3">
                            <div className="min-w-0">
                                <h3 className="line-clamp-2 text-base font-semibold leading-tight text-foreground">
                                    {rating.mediaTitle}
                                </h3>
                                <p className="mt-1 text-xs font-medium uppercase text-muted-foreground">
                                    {rating.mediaType === "tv" ? "TV Series" : "Movie"}
                                </p>
                            </div>
                            <div className="flex shrink-0 items-center gap-1 rounded-md bg-yellow-400 px-2 py-1 text-sm font-bold text-black">
                                <Star className="h-3.5 w-3.5 fill-current" />
                                {rating.rating}
                            </div>
                        </div>

                        <div className="flex flex-wrap items-center gap-3 text-sm text-muted-foreground">
                            <span className="flex items-center gap-1 text-foreground">
                                <Star className="h-3.5 w-3.5 fill-yellow-500 text-yellow-500" />
                                {formatRating(rating.averageRating)}
                            </span>
                            {ratedAt && (
                                <span className="flex items-center gap-1">
                                    <CalendarDays className="h-3.5 w-3.5" />
                                    {ratedAt}
                                </span>
                            )}
                        </div>
                    </div>

                    <span className="mt-3 text-xs font-medium text-primary opacity-0 transition group-hover:opacity-100">
                        View details
                    </span>
                </div>
            </motion.div>
        </Link>
    );
}
