import { ChevronLeft, ChevronRight } from "lucide-react";
import { Button } from "./Button";

const pageSizeOptions = [10, 20, 50, 100];

export function ChartPagination({ pageInfo, pageSize = 20, onPageChange, onPageSizeChange }) {
    if (!pageInfo) {
        return null;
    }

    const currentPage = pageInfo.page + 1;
    const showPageControls = pageInfo.totalPages > 1;

    return (
        <div className="mt-6 flex flex-col items-center justify-between gap-3 border-t border-border pt-4 sm:flex-row">
            <div className="flex flex-col items-center gap-2 sm:flex-row sm:gap-4">
                {showPageControls && (
                    <p className="text-sm text-muted-foreground">
                        Page {currentPage} of {pageInfo.totalPages}
                    </p>
                )}
                {onPageSizeChange && (
                    <div className="flex flex-col items-center gap-2 sm:flex-row">
                        <span className="text-sm text-muted-foreground">Show</span>
                        <div
                            className="grid grid-cols-4 rounded-md border border-border bg-card p-1"
                            role="group"
                            aria-label="Items per page"
                        >
                            {pageSizeOptions.map((option) => (
                                <button
                                    key={option}
                                    type="button"
                                    aria-pressed={pageSize === option}
                                    onClick={() => onPageSizeChange(option)}
                                    className={`h-8 min-w-10 rounded px-2 text-sm font-medium transition-colors ${
                                        pageSize === option
                                            ? "bg-primary text-primary-foreground"
                                            : "text-muted-foreground hover:bg-muted hover:text-foreground"
                                    }`}
                                >
                                    {option}
                                </button>
                            ))}
                        </div>
                    </div>
                )}
            </div>
            <div className="flex items-center gap-2">
                {showPageControls && (
                    <>
                        <Button
                            type="button"
                            variant="outline"
                            size="sm"
                            disabled={!pageInfo.hasPrevious}
                            onClick={() => onPageChange(pageInfo.page - 1)}
                        >
                            <ChevronLeft className="mr-1 h-4 w-4" />
                            Previous
                        </Button>
                        <Button
                            type="button"
                            variant="outline"
                            size="sm"
                            disabled={!pageInfo.hasNext}
                            onClick={() => onPageChange(pageInfo.page + 1)}
                        >
                            Next
                            <ChevronRight className="ml-1 h-4 w-4" />
                        </Button>
                    </>
                )}
            </div>
        </div>
    );
}
