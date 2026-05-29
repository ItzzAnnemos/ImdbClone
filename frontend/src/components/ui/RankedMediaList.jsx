import { RankedMediaListItem } from "./RankedMediaListItem";

export function RankedMediaList({ items }) {
    return (
        <div className="overflow-hidden rounded-lg border border-border bg-card">
            {items.map((item, index) => (
                <RankedMediaListItem
                    key={`${item.type}-${item.id}`}
                    item={item}
                    isLast={index === items.length - 1}
                />
            ))}
        </div>
    );
}
