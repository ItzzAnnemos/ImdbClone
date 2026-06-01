export function DiscoveryPageHeader({ title, subtitle, count, eyebrow }) {
    return (
        <header className="mb-6 border-b border-border/70 pb-5">
            {eyebrow && (
                <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-yellow-500">
                    {eyebrow}
                </p>
            )}
            <div className="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
                <div className="border-l-4 border-yellow-400 pl-4">
                    <h1 className="text-3xl font-bold leading-tight text-foreground">{title}</h1>
                    {subtitle && (
                        <p className="mt-2 max-w-3xl text-sm leading-6 text-muted-foreground">
                            {subtitle}
                        </p>
                    )}
                </div>
                {typeof count === "number" && (
                    <div className="shrink-0 rounded-md border border-border bg-secondary px-3 py-1.5 text-sm font-medium text-secondary-foreground">
                        {count} {count === 1 ? "result" : "results"}
                    </div>
                )}
            </div>
        </header>
    );
}
