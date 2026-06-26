import { motion } from "framer-motion";
import { ChevronRight, Star } from "lucide-react";
import { Link } from "react-router-dom";
import { cn, formatRating } from "../../lib/utils";

export function ProfileWatchlistCard({ media, className }) {
    return (
        <Link to={`/media/${media.id}`}>
            <motion.div
                whileTap={{ scale: 0.98 }}
                className={cn(
                    "group flex min-h-32 overflow-hidden rounded-xl border border-border bg-card shadow-sm transition hover:border-primary/30 hover:shadow-lg",
                    className,
                )}
            >
                <div className="w-24 shrink-0 overflow-hidden bg-secondary">
                    <img
                        src={media.image || "https://placehold.co/200x300?text=No+Image"}
                        alt={media.title}
                        className="h-full w-full object-cover transition duration-300 group-hover:scale-105"
                        loading="lazy"
                    />
                </div>

                <div className="flex min-w-0 flex-1 flex-col justify-between p-4">
                    <div className="min-w-0">
                        <p className="mb-1 text-[10px] font-bold uppercase tracking-wide text-yellow-500">
                            Saved title
                        </p>
                        <h3 className="line-clamp-2 text-base font-semibold leading-tight text-foreground">
                            {media.title}
                        </h3>
                        <div className="mt-2 flex items-center gap-2 text-sm text-muted-foreground">
                            <span className="flex items-center gap-1 text-foreground">
                                <Star className="h-3.5 w-3.5 fill-yellow-500 text-yellow-500" />
                                {formatRating(media.rating)}
                            </span>
                            <span>{media.type === "tv" ? "TV Series" : "Movie"}</span>
                        </div>
                    </div>

                    <span className="mt-3 inline-flex items-center text-xs font-semibold text-primary opacity-0 transition group-hover:opacity-100">
                        View details <ChevronRight className="ml-1 h-3.5 w-3.5" />
                    </span>
                </div>
            </motion.div>
        </Link>
    );
}
