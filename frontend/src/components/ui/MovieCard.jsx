import { motion } from "framer-motion";
import { Star, Bookmark, BookmarkCheck, Loader2 } from "lucide-react";
import { Link } from "react-router-dom";
import { cn } from "../../lib/utils";
import { useAuth } from "../../context/AuthContext";
import { useWatchlist } from "../../hooks/useWatchlist";

export function MovieCard({ id, title, rating, image, year, className, ...props }) {
    const { user } = useAuth();
    const { inWatchlist, toggling, toggle } = useWatchlist(user?.username, id);

    const handleToggle = (e) => {
        e.preventDefault(); // prevent Link navigation
        e.stopPropagation();
        toggle();
    };

    return (
        <Link to={`/movie/${id}`}>
            <motion.div
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                className={cn(
                    "group relative overflow-hidden rounded-lg bg-card shadow-lg transition-all hover:shadow-xl cursor-pointer",
                    className,
                )}
                {...props}
            >
                <div className="aspect-[2/3] w-full overflow-hidden">
                    <img
                        src={image || "https://placehold.co/400x600?text=No+Image"}
                        alt={title}
                        className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-110"
                        loading="lazy"
                    />
                </div>

                {/* Watchlist button — always visible in top-right corner */}
                {user && (
                    <button
                        onClick={handleToggle}
                        disabled={toggling}
                        aria-label={inWatchlist ? "Remove from watchlist" : "Add to watchlist"}
                        className={cn(
                            "absolute top-2 right-2 z-10 flex h-8 w-8 items-center justify-center rounded-full shadow-md backdrop-blur-sm transition-all duration-200",
                            inWatchlist
                                ? "bg-primary text-primary-foreground hover:bg-primary/80"
                                : "bg-black/50 text-white hover:bg-black/70",
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

                <div className="absolute inset-x-0 bottom-0 bg-gradient-to-t from-black/90 via-black/60 to-transparent p-4 pt-12 text-white opacity-0 transition-opacity duration-300 group-hover:opacity-100">
                    <h3 className="font-bold text-lg leading-tight line-clamp-2">{title}</h3>
                    <div className="mt-2 flex items-center justify-between text-sm">
                        <div className="flex items-center gap-1 text-yellow-500">
                            <Star className="h-4 w-4 fill-current" />
                            <span className="font-medium text-white">{rating}</span>
                        </div>
                        <span className="text-gray-300">{year}</span>
                    </div>
                </div>
            </motion.div>
        </Link>
    );
}
