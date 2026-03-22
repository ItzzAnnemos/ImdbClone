import { motion } from "framer-motion";
import { Star, User2, Bookmark, BookmarkCheck, Loader2 } from "lucide-react";
import { Link } from "react-router-dom";
import { cn } from "../../lib/utils";
import { useAuth } from "../../context/AuthContext";
import { useWatchlist } from "../../hooks/useWatchlist";
import { MediaModel } from "../../models";

/**
 * @param {{ media: MediaModel.MediaCard, className?: string }} props
 */
export function WatchlistCard({ media, className, ...props }) {
    const { user } = useAuth();
    const { inWatchlist, toggling, toggle } = useWatchlist(user?.username, media.id);

    const handleToggle = (e) => {
        e.preventDefault();
        e.stopPropagation();
        toggle();
    };

    return (
        <Link to={media.type === "tv" ? `/tv/${media.id}` : `/movie/${media.id}`}>
            <motion.div
                whileHover={{ scale: 1.03 }}
                whileTap={{ scale: 0.97 }}
                className={cn(
                    "group relative flex gap-4 overflow-hidden rounded-xl bg-card shadow-lg transition-all hover:shadow-2xl cursor-pointer border border-white/5",
                    className,
                )}
                {...props}
            >
                {/* Poster */}
                <div className="relative w-28 shrink-0 overflow-hidden rounded-l-xl">
                    <img
                        src={media.image || "https://placehold.co/200x300?text=No+Image"}
                        alt={media.title}
                        className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-110"
                        loading="lazy"
                    />
                    <div className="absolute inset-0 bg-black/20 opacity-0 transition-opacity duration-300 group-hover:opacity-100" />
                </div>

                {/* Info */}
                <div className="flex flex-1 flex-col justify-between py-4 pr-4">
                    <div>
                        <div className="flex items-start justify-between gap-2">
                            <h3 className="font-bold text-lg leading-tight text-foreground line-clamp-1">
                                {media.title}
                            </h3>

                            {/* Watchlist toggle button */}
                            {user && (
                                <button
                                    onClick={handleToggle}
                                    disabled={toggling}
                                    aria-label={
                                        inWatchlist
                                            ? "Remove from watchlist"
                                            : "Add to watchlist"
                                    }
                                    className={cn(
                                        "shrink-0 flex h-8 w-8 items-center justify-center rounded-full shadow transition-all duration-200",
                                        inWatchlist
                                            ? "bg-yellow-400 text-primary-foreground hover:bg-yellow-400/80"
                                            : "bg-muted text-muted-foreground hover:bg-muted/70 hover:text-foreground",
                                    )}
                                >
                                    {toggling ? (
                                        <Loader2 className="h-4 w-4 animate-spin" />
                                    ) : inWatchlist ? (
                                        <BookmarkCheck className="h-4 w-4" />
                                    ) : (
                                        <Bookmark className="h-4 w-4" />
                                    )}
                                </button>
                            )}
                        </div>

                        {/* Meta row */}
                        <div className="mt-1 flex items-center gap-3 text-sm">
                            <div className="flex items-center gap-1 text-yellow-500">
                                <Star className="h-3.5 w-3.5 fill-current" />
                                <span className="font-semibold text-foreground">{media.rating}</span>
                            </div>
                            <span className="text-muted-foreground">{media.releaseYear}</span>
                        </div>

                        {/* Director */}
                        {media.director && (
                            <div className="mt-2 flex items-center gap-1.5 text-sm text-muted-foreground">
                                <User2 className="h-3.5 w-3.5 shrink-0" />
                                <span className="line-clamp-1">{media.director}</span>
                            </div>
                        )}

                        {/* Description */}
                        {media.description && (
                            <p className="mt-3 text-sm text-muted-foreground line-clamp-3 leading-relaxed">
                                {media.description}
                            </p>
                        )}
                    </div>

                    {/* Hover badge */}
                    <div className="mt-3 self-start rounded-full bg-primary/10 px-3 py-0.5 text-xs font-medium text-primary opacity-0 transition-opacity duration-300 group-hover:opacity-100">
                        View Details →
                    </div>
                </div>

                {/* Accent left border */}
                <div className="absolute inset-y-0 left-0 w-0.5 rounded-full bg-primary opacity-0 transition-opacity duration-300 group-hover:opacity-100" />
            </motion.div>
        </Link>
    );
}
