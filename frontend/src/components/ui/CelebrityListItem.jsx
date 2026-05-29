import { CalendarDays, Film } from "lucide-react";
import { cn } from "../../lib/utils";

export function CelebrityListItem({ item, isLast }) {
    const initials = item.fullName
        .split(" ")
        .map((part) => part.charAt(0))
        .join("")
        .slice(0, 2)
        .toUpperCase();

    return (
        <div
            className={cn(
                "grid grid-cols-[2.5rem_4.5rem_1fr] gap-4 p-4 transition hover:bg-secondary/60 sm:grid-cols-[3rem_5rem_1fr_auto]",
                !isLast && "border-b border-border/70",
            )}
        >
            <div className="pt-1 text-right text-lg font-semibold text-muted-foreground">
                {item.rank}
            </div>
            <div className="flex aspect-square items-center justify-center overflow-hidden rounded-full bg-secondary text-lg font-bold text-muted-foreground">
                {item.image ? (
                    <img
                        src={item.image}
                        alt={item.fullName}
                        className="h-full w-full object-cover"
                        loading="lazy"
                    />
                ) : (
                    initials
                )}
            </div>
            <div className="min-w-0">
                <h2 className="text-base font-semibold text-foreground sm:text-lg">
                    {item.fullName}
                </h2>
                <div className="mt-2 flex flex-wrap gap-x-4 gap-y-2 text-sm text-muted-foreground">
                    {item.birthDate && (
                        <span className="inline-flex items-center gap-1.5">
                            <CalendarDays className="h-4 w-4" />
                            Born {item.birthDate}
                        </span>
                    )}
                    <span className="inline-flex items-center gap-1.5">
                        <Film className="h-4 w-4" />
                        {item.creditCount} {item.creditCount === 1 ? "credit" : "credits"}
                    </span>
                </div>
                {item.knownFor.length > 0 && (
                    <p className="mt-2 text-sm text-muted-foreground">
                        Known for{" "}
                        <span className="text-foreground">{item.knownFor.join(", ")}</span>
                    </p>
                )}
            </div>
        </div>
    );
}
