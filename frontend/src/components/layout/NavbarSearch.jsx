import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Film, Search, UserRound, XCircle } from "lucide-react";
import { searchCatalog } from "../../lib/searchService";

function getSuggestionHref(result) {
    return result.type === "Person" ? `/person/${result.id}` : `/media/${result.id}`;
}

export function NavbarSearch({ onSearch }) {
    const navigate = useNavigate();
    const [query, setQuery] = useState("");
    const [suggestions, setSuggestions] = useState([]);
    const [isOpen, setIsOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const searchRef = useRef(null);

    useEffect(() => {
        const trimmedQuery = query.trim();

        if (trimmedQuery.length < 2) {
            setSuggestions([]);
            setIsOpen(false);
            setLoading(false);
            return;
        }

        const controller = new AbortController();
        const timeoutId = window.setTimeout(async () => {
            try {
                setLoading(true);
                const page = await searchCatalog(trimmedQuery, 0, 5, {
                    signal: controller.signal,
                });
                setSuggestions(page.results);
                setIsOpen(true);
            } catch (error) {
                if (error.name !== "CanceledError" && error.name !== "AbortError") {
                    setSuggestions([]);
                    setIsOpen(false);
                }
            } finally {
                setLoading(false);
            }
        }, 250);

        return () => {
            controller.abort();
            window.clearTimeout(timeoutId);
        };
    }, [query]);

    useEffect(() => {
        const handlePointerDown = (event) => {
            if (!searchRef.current?.contains(event.target)) {
                setIsOpen(false);
            }
        };

        document.addEventListener("pointerdown", handlePointerDown);
        return () => document.removeEventListener("pointerdown", handlePointerDown);
    }, []);

    const clearSearch = () => {
        setQuery("");
        setSuggestions([]);
        setIsOpen(false);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const trimmedQuery = query.trim();

        if (!trimmedQuery) {
            return;
        }

        navigate(`/search?query=${encodeURIComponent(trimmedQuery)}&page=0`);
        setIsOpen(false);
        onSearch?.();
    };

    const handleSuggestionClick = (result) => {
        setQuery("");
        setSuggestions([]);
        setIsOpen(false);
        navigate(getSuggestionHref(result));
        onSearch?.();
    };

    return (
        <form ref={searchRef} className="relative w-full" onSubmit={handleSubmit}>
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <input
                type="text"
                role="searchbox"
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                onFocus={() => query.trim().length >= 2 && setIsOpen(true)}
                placeholder="Search IMDb..."
                className="w-full rounded-md border border-input bg-secondary px-9 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            />
            {query && (
                <button
                    type="button"
                    onClick={clearSearch}
                    className="absolute right-2.5 top-2.5 text-muted-foreground transition-colors hover:text-foreground"
                    aria-label="Clear search"
                >
                    <XCircle className="h-4 w-4" />
                </button>
            )}
            {isOpen && query.trim().length >= 2 && (
                <div className="absolute left-0 right-0 top-full z-50 mt-2 overflow-hidden rounded-md border border-border bg-popover shadow-xl">
                    {loading && suggestions.length === 0 ? (
                        <div className="px-3 py-3 text-sm text-muted-foreground">Searching...</div>
                    ) : suggestions.length > 0 ? (
                        <div className="max-h-80 overflow-y-auto py-1">
                            {suggestions.map((result) => {
                                const isPerson = result.type === "Person";
                                return (
                                    <button
                                        key={`${result.type}-${result.id}`}
                                        type="button"
                                        onClick={() => handleSuggestionClick(result)}
                                        className="flex w-full items-center gap-3 px-3 py-2 text-left transition-colors hover:bg-secondary"
                                    >
                                        <div className="flex h-10 w-8 shrink-0 items-center justify-center overflow-hidden rounded bg-secondary">
                                            {result.image ? (
                                                <img
                                                    src={result.image}
                                                    alt=""
                                                    className="h-full w-full object-cover"
                                                />
                                            ) : isPerson ? (
                                                <UserRound className="h-5 w-5 text-muted-foreground" />
                                            ) : (
                                                <Film className="h-5 w-5 text-muted-foreground" />
                                            )}
                                        </div>
                                        <div className="min-w-0">
                                            <div className="truncate text-sm font-medium text-foreground">
                                                {result.title}
                                            </div>
                                            <div className="text-xs text-muted-foreground">
                                                {isPerson ? "Person" : "Media"}
                                            </div>
                                        </div>
                                    </button>
                                );
                            })}
                        </div>
                    ) : (
                        <div className="px-3 py-3 text-sm text-muted-foreground">
                            No suggestions found
                        </div>
                    )}
                </div>
            )}
        </form>
    );
}
