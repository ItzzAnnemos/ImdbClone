import { useState, useEffect, useCallback } from "react";
import api from "../lib/api";

/**
 * Custom hook to manage a single movie's watchlist state.
 *
 * Returns:
 *   inWatchlist  – boolean
 *   loading      – boolean (initial check in progress)
 *   toggling     – boolean (add/remove request in progress)
 *   toggle       – () => Promise<void>  – call to add or remove
 */
export function useWatchlist(username, mediaId) {
    const [inWatchlist, setInWatchlist] = useState(false);
    const [loading, setLoading] = useState(false);
    const [toggling, setToggling] = useState(false);

    // Check once whether this media is already in the watchlist
    useEffect(() => {
        if (!username || !mediaId) return;

        let cancelled = false;
        setLoading(true);

        api.get(`/api/user/${encodeURIComponent(username)}/watchlist/${mediaId}`)
            .then((res) => {
                if (!cancelled) setInWatchlist(!!res.data);
            })
            .catch(() => {
                // Silently fail – treat as not in watchlist
                if (!cancelled) setInWatchlist(false);
            })
            .finally(() => {
                if (!cancelled) setLoading(false);
            });

        return () => {
            cancelled = true;
        };
    }, [username, mediaId]);

    const toggle = useCallback(async () => {
        if (!username || !mediaId || toggling) return;
        setToggling(true);
        try {
            if (inWatchlist) {
                await api.delete(
                    `/api/user/${encodeURIComponent(username)}/watchlist/${mediaId}`,
                );
                setInWatchlist(false);
            } else {
                await api.post(
                    `/api/user/${encodeURIComponent(username)}/watchlist/${mediaId}`,
                );
                setInWatchlist(true);
            }
        } catch (err) {
            console.error("Watchlist toggle error:", err);
        } finally {
            setToggling(false);
        }
    }, [username, mediaId, inWatchlist, toggling]);

    return { inWatchlist, loading, toggling, toggle };
}
