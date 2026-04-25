import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Star, X } from "lucide-react";
import { Button } from "./Button";
import { cn } from "../../lib/utils";

export function RatingModal({ isOpen, onClose, onRate, initialRating = 0, title }) {
    const [hoveredRating, setHoveredRating] = useState(0);
    const [selectedRating, setSelectedRating] = useState(initialRating);

    if (!isOpen) return null;

    return (
        <AnimatePresence>
            <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
                {/* Backdrop */}
                <motion.div
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    onClick={onClose}
                    className="absolute inset-0 bg-black/80 backdrop-blur-sm"
                />

                {/* Modal Content */}
                <motion.div
                    initial={{ opacity: 0, scale: 0.9, y: 20 }}
                    animate={{ opacity: 1, scale: 1, y: 0 }}
                    exit={{ opacity: 0, scale: 0.9, y: 20 }}
                    className="relative w-full max-w-md bg-card border border-border rounded-2xl shadow-2xl overflow-hidden"
                >
                    <div className="p-6">
                        <div className="flex items-center justify-between mb-8">
                            <div className="flex items-center gap-3">
                                <div className="p-2 bg-yellow-400 rounded-lg">
                                    <Star className="h-6 w-6 text-black fill-current" />
                                </div>
                                <h3 className="text-xl font-bold">Rate this</h3>
                            </div>
                            <button
                                onClick={onClose}
                                className="p-2 hover:bg-muted rounded-full transition-colors"
                            >
                                <X className="h-5 w-5" />
                            </button>
                        </div>

                        <div className="text-center mb-8">
                            <h4 className="text-2xl font-black text-yellow-400 uppercase tracking-tight mb-2">
                                {title}
                            </h4>
                            <div className="text-6xl font-bold text-foreground">
                                {selectedRating || hoveredRating || "?"}
                                <span className="text-2xl text-muted-foreground ml-1">/ 10</span>
                            </div>
                        </div>

                        {/* Stars Grid */}
                        <div className="flex justify-center gap-1 mb-10">
                            {[...Array(10)].map((_, i) => {
                                const ratingValue = i + 1;
                                const isFilled = ratingValue <= (hoveredRating || selectedRating);
                                
                                return (
                                    <motion.button
                                        key={ratingValue}
                                        whileHover={{ scale: 1.2 }}
                                        whileTap={{ scale: 0.9 }}
                                        onMouseEnter={() => setHoveredRating(ratingValue)}
                                        onMouseLeave={() => setHoveredRating(0)}
                                        onClick={() => setSelectedRating(ratingValue)}
                                        className="relative p-1 focus:outline-none"
                                    >
                                        <Star
                                            className={cn(
                                                "h-8 w-8 transition-colors",
                                                isFilled 
                                                    ? "text-yellow-400 fill-current" 
                                                    : "text-muted-foreground"
                                            )}
                                        />
                                    </motion.button>
                                );
                            })}
                        </div>

                        <Button
                            onClick={() => onRate(selectedRating)}
                            disabled={!selectedRating}
                            className="w-full h-14 rounded-xl font-bold text-lg bg-yellow-400 hover:bg-yellow-500 text-black border-none"
                        >
                            Rate
                        </Button>
                    </div>
                </motion.div>
            </div>
        </AnimatePresence>
    );
}
