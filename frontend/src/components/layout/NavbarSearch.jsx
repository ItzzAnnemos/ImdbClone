import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Search, XCircle } from "lucide-react";

export function NavbarSearch({ onSearch }) {
    const navigate = useNavigate();
    const [query, setQuery] = useState("");

    const clearSearch = () => {
        setQuery("");
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const trimmedQuery = query.trim();

        if (!trimmedQuery) {
            return;
        }

        navigate(`/search?query=${encodeURIComponent(trimmedQuery)}&page=0`);
        onSearch?.();
    };

    return (
        <form className="relative w-full" onSubmit={handleSubmit}>
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <input
                type="text"
                role="searchbox"
                value={query}
                onChange={(event) => setQuery(event.target.value)}
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
        </form>
    );
}
