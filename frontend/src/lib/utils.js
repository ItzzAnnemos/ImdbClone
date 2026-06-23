import { clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs) {
    return twMerge(clsx(inputs));
}

export function formatRating(value) {
    const numericValue = Number(value);
    return Number.isFinite(numericValue) ? numericValue.toFixed(1) : "N/A";
}

export function formatRole(role) {
    if (!role) {
        return "";
    }

    return (
        roleLabels[role] ??
        role
            .toLowerCase()
            .split("_")
            .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
            .join(" ")
    );
}

const roleLabels = {
    MAIN_ACTOR: "Lead actor",
    ACTOR: "Actor",
    DIRECTOR: "Director",
    WRITER: "Writer",
    PRODUCER: "Producer",
    CREATOR: "Creator",
    CREW: "Crew",
};
