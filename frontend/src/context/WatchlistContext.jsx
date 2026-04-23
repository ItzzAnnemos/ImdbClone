import { createContext, useContext, useState, useEffect, useCallback } from "react";
import api from "../lib/api";
import { useAuth } from "./AuthContext";

const WatchlistContext = createContext();

export const useWatchlistContext = () => {
    return useContext(WatchlistContext);
};

export const WatchlistProvider = ({ children }) => {
    const { user } = useAuth();
    const [watchlistIds, setWatchlistIds] = useState(new Set());
    const [loading, setLoading] = useState(false);

    const fetchWatchlist = useCallback(async () => {
        if (!user) {
            setWatchlistIds(new Set());
            return;
        }

        setLoading(true);
        try {
            const response = await api.get(`/api/user/${encodeURIComponent(user.username)}/watchlist`);
            const ids = new Set(response.data.map(item => item.id));
            setWatchlistIds(ids);
        } catch (err) {
            console.error("Failed to fetch watchlist:", err);
        } finally {
            setLoading(false);
        }
    }, [user]);

    useEffect(() => {
        fetchWatchlist();
    }, [fetchWatchlist]);

    const toggleWatchlist = async (mediaId) => {
        if (!user) return;

        const numericId = Number(mediaId);
        const isInWatchlist = watchlistIds.has(numericId);
        
        try {
            if (isInWatchlist) {
                await api.delete(`/api/user/${encodeURIComponent(user.username)}/watchlist/${numericId}`);
                setWatchlistIds(prev => {
                    const next = new Set(prev);
                    next.delete(numericId);
                    return next;
                });
            } else {
                await api.post(`/api/user/${encodeURIComponent(user.username)}/watchlist/${numericId}`);
                setWatchlistIds(prev => {
                    const next = new Set(prev);
                    next.add(numericId);
                    return next;
                });
            }
        } catch (err) {
            console.error("Failed to toggle watchlist:", err);
            // Optionally: Re-fetch the full list if an error occurs to stay in sync
            fetchWatchlist();
        }
    };

    const value = {
        watchlistIds,
        loading,
        toggleWatchlist,
        isInWatchlist: (mediaId) => watchlistIds.has(Number(mediaId)),
        refresh: fetchWatchlist
    };

    return (
        <WatchlistContext.Provider value={value}>
            {children}
        </WatchlistContext.Provider>
    );
};
