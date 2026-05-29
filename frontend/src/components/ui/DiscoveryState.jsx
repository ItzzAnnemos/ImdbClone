import { AlertCircle, Loader2, SearchX } from "lucide-react";
import { Button } from "./Button";

export function DiscoveryLoadingState({ label = "Loading titles..." }) {
    return (
        <div className="flex min-h-[320px] flex-col items-center justify-center gap-3 text-muted-foreground">
            <Loader2 className="h-9 w-9 animate-spin text-yellow-400" />
            <p className="text-sm">{label}</p>
        </div>
    );
}

export function DiscoveryErrorState({ message, onRetry }) {
    return (
        <div className="flex min-h-[320px] flex-col items-center justify-center gap-3 text-center">
            <AlertCircle className="h-10 w-10 text-destructive" />
            <p className="max-w-md text-sm text-destructive">
                {message || "Failed to load this page. Please try again."}
            </p>
            {onRetry && (
                <Button variant="outline" onClick={onRetry}>
                    Retry
                </Button>
            )}
        </div>
    );
}

export function DiscoveryEmptyState({ title = "No results found", message }) {
    return (
        <div className="flex min-h-[320px] flex-col items-center justify-center gap-3 text-center">
            <SearchX className="h-10 w-10 text-muted-foreground" />
            <div>
                <h2 className="text-lg font-semibold text-foreground">{title}</h2>
                {message && (
                    <p className="mt-1 max-w-md text-sm text-muted-foreground">{message}</p>
                )}
            </div>
        </div>
    );
}
