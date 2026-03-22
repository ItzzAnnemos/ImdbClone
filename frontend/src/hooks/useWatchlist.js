import { useState, useCallback } from "react";
import { useWatchlistContext } from "../context/WatchlistContext";

/**
 * Custom hook to manage a single movie's watchlist state.
 * Now synchronized via WatchlistContext.
 */
export function useWatchlist(username, mediaId) {
    const { isInWatchlist, toggleWatchlist, loading: contextLoading } = useWatchlistContext();
    const [toggling, setToggling] = useState(false);

    const inWatchlist = isInWatchlist(mediaId);

    const toggle = useCallback(async () => {
        if (!username || !mediaId || toggling) return;
        setToggling(true);
        try {
            await toggleWatchlist(mediaId);
        } finally {
            setToggling(false);
        }
    }, [username, mediaId, toggling, toggleWatchlist]);

    return { inWatchlist, loading: contextLoading, toggling, toggle };
}
