import { useState, useRef, useEffect } from "react";
import { motion, useMotionValue, animate } from "framer-motion";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { MediaCard } from "./MediaCard";

export function MediaSlider({ title, items }) {
    const x = useMotionValue(0);
    const [canScrollLeft, setCanScrollLeft] = useState(false);
    const [canScrollRight, setCanScrollRight] = useState(true);
    const containerRef = useRef(null);
    const sliderRef = useRef(null);

    const updateScrollState = () => {
        if (!sliderRef.current || !containerRef.current) return;
        const currentX = x.get();
        const maxScroll = containerRef.current.offsetWidth - sliderRef.current.scrollWidth;

        setCanScrollLeft(currentX < -10);
        setCanScrollRight(currentX > maxScroll + 10);
    };

    useEffect(() => {
        const unsubscribe = x.on("change", updateScrollState);
        return () => unsubscribe();
    }, []);

    useEffect(() => {
        updateScrollState();
    }, [items]);

    const handleScroll = (direction) => {
        if (!sliderRef.current || !containerRef.current) return;

        const containerWidth = containerRef.current.offsetWidth;
        const currentX = x.get();
        const maxScroll = containerWidth - sliderRef.current.scrollWidth;

        const step = containerWidth * 0.75;
        let targetX = direction === "left" ? currentX + step : currentX - step;

        targetX = Math.min(0, Math.max(targetX, maxScroll));

        animate(x, targetX, {
            type: "spring",
            stiffness: 300,
            damping: 30
        });
    };

    if (!items || items.length === 0) return null;

    return (
        <section className="mb-4 md:mb-12">
            <div className="flex items-center justify-between mb-6 px-1">
                <h2 className="text-2xl font-bold text-foreground">{title}</h2>
                <div className="hidden md:flex items-center gap-2">
                    <button
                        onClick={() => handleScroll("left")}
                        disabled={!canScrollLeft}
                        className="p-2 rounded-full bg-background border shadow-sm hover:bg-muted disabled:opacity-30 disabled:cursor-not-allowed transition-all"
                        aria-label="Scroll left"
                    >
                        <ChevronLeft className="h-5 w-5" />
                    </button>
                    <button
                        onClick={() => handleScroll("right")}
                        disabled={!canScrollRight}
                        className="p-2 rounded-full bg-background border shadow-sm hover:bg-muted disabled:opacity-30 disabled:cursor-not-allowed transition-all"
                        aria-label="Scroll right"
                    >
                        <ChevronRight className="h-5 w-5" />
                    </button>
                </div>
            </div>

            <div
                ref={containerRef}
                className="relative overflow-hidden"
            >
                <motion.div
                    ref={sliderRef}
                    drag="x"
                    dragConstraints={containerRef}
                    dragElastic={0.1}
                    className="flex gap-4 cursor-grab active:cursor-grabbing pb-4"
                    style={{
                        x,
                        width: "max-content",
                        display: "flex",
                        gap: "1.5rem"
                    }}
                >
                    {items.map((item) => (
                        <div
                            key={`${item.id}-${title}`}
                            className="w-[180px] sm:w-[220px] md:w-[260px] flex-shrink-0"
                        >
                            <MediaCard {...item} />
                        </div>
                    ))}
                </motion.div>
            </div>
        </section>
    );
}
