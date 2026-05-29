import { Bookmark, BookmarkCheck, Loader2, Star } from "lucide-react";
import { Link } from "react-router-dom";
import { cn } from "../../lib/utils";
import { useAuth } from "../../context/AuthContext";
import { useWatchlist } from "../../hooks/useWatchlist";

export function RankedMediaListItem({ item, isLast }) {
    const { user } = useAuth();
    const { inWatchlist, toggling, toggle } = useWatchlist(user?.username, item.id);

    const handleWatchlistClick = (event) => {
        event.preventDefault();
        event.stopPropagation();
        toggle();
    };

    return (
        <Link
            to={`/media/${item.id}`}
            className={cn(
                "group grid grid-cols-[2.5rem_4.5rem_1fr] gap-4 p-4 transition hover:bg-secondary/60 sm:grid-cols-[3rem_5rem_1fr_auto]",
                !isLast && "border-b border-border/70",
            )}
        >
            <div className="pt-1 text-right text-lg font-semibold text-muted-foreground">
                {item.rank}
            </div>
            <div className="aspect-[2/3] overflow-hidden rounded bg-secondary">
                <img
                    src={item.image || "https://placehold.co/160x240?text=No+Image"}
                    alt={item.title}
                    className="h-full w-full object-cover transition duration-300 group-hover:scale-105"
                    loading="lazy"
                />
            </div>
            <div className="min-w-0">
                <div className="flex flex-wrap items-center gap-2">
                    <h2 className="text-base font-semibold text-foreground group-hover:text-yellow-400 sm:text-lg">
                        {item.title}
                    </h2>
                    <span className="rounded border border-border px-1.5 py-0.5 text-[11px] uppercase text-muted-foreground">
                        {item.type === "tv" ? "TV" : "Movie"}
                    </span>
                </div>
                <div className="mt-1 flex flex-wrap items-center gap-x-3 gap-y-1 text-sm text-muted-foreground">
                    {item.year && <span>{item.year}</span>}
                    {item.genres.length > 0 && <span>{item.genres.join(", ")}</span>}
                </div>
                <div className="mt-3 flex flex-wrap items-center gap-4 text-sm">
                    <span className="inline-flex items-center gap-1 font-semibold text-foreground">
                        <Star className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                        {item.rating ? item.rating.toFixed(1) : "N/A"}
                    </span>
                    <span className="text-muted-foreground">
                        {item.ratingCount} {item.ratingCount === 1 ? "rating" : "ratings"}
                    </span>
                </div>
            </div>
            {user && (
                <button
                    onClick={handleWatchlistClick}
                    disabled={toggling}
                    aria-label={inWatchlist ? "Remove from watchlist" : "Add to watchlist"}
                    className={cn(
                        "col-start-3 mt-2 flex h-9 w-9 items-center justify-center rounded-full transition sm:col-start-auto sm:mt-0",
                        inWatchlist
                            ? "bg-yellow-400 text-black hover:bg-yellow-300"
                            : "bg-secondary text-muted-foreground hover:text-foreground",
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
        </Link>
    );
}
