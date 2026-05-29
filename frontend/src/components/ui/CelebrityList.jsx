import { CelebrityListItem } from "./CelebrityListItem";

export function CelebrityList({ items }) {
    return (
        <div className="overflow-hidden rounded-lg border border-border bg-card">
            {items.map((item, index) => (
                <CelebrityListItem key={item.id} item={item} isLast={index === items.length - 1} />
            ))}
        </div>
    );
}
