import { Link } from "react-router-dom";
import { cn } from "../../lib/utils";

export function GenrePicker({ genres, mediaType, selectedGenreName }) {
    const basePath = mediaType === "tv" ? "/tv/genre" : "/movies/genre";

    return (
        <div className="mb-6 flex flex-wrap gap-2">
            {genres.map((genre) => {
                const selected = genre.name.toLowerCase() === selectedGenreName?.toLowerCase();
                return (
                    <Link
                        key={genre.id}
                        to={`${basePath}/${encodeURIComponent(genre.name)}`}
                        className={cn(
                            "rounded-full border px-3 py-1.5 text-sm font-medium transition",
                            selected
                                ? "border-yellow-400 bg-yellow-400 text-black"
                                : "border-border bg-secondary text-secondary-foreground hover:border-yellow-400 hover:text-yellow-400",
                        )}
                    >
                        {genre.name}
                    </Link>
                );
            })}
        </div>
    );
}
