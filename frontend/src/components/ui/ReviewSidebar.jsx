import { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { X, Send, MessageSquare, Pencil } from "lucide-react";
import { Button } from "./Button";
import { cn } from "../../lib/utils";

export function ReviewSidebar({ isOpen, onClose, onSubmit, title, initialReview }) {
    const [reviewText, setReviewText] = useState("");

    useEffect(() => {
        if (initialReview) {
            setReviewText(initialReview.reviewText);
        } else {
            setReviewText("");
        }
    }, [initialReview, isOpen]);

    if (!isOpen) return null;

    return (
        <AnimatePresence>
            <div className="fixed inset-0 z-50 flex justify-end">
                {/* Backdrop */}
                <motion.div
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    onClick={onClose}
                    className="absolute inset-0 bg-black/60 backdrop-blur-sm"
                />

                {/* Sidebar Content */}
                <motion.div
                    initial={{ x: "100%" }}
                    animate={{ x: 0 }}
                    exit={{ x: "100%" }}
                    transition={{ type: "spring", damping: 25, stiffness: 200 }}
                    className="relative w-full max-w-lg bg-card border-l border-border h-full shadow-2xl flex flex-col"
                >
                    <div className="p-6 border-b border-border flex items-center justify-between bg-muted/30">
                        <div className="flex items-center gap-3">
                            <div className="p-2 bg-primary/10 rounded-lg">
                                {initialReview ? (
                                    <Pencil className="h-5 w-5 text-primary" />
                                ) : (
                                    <MessageSquare className="h-5 w-5 text-primary" />
                                )}
                            </div>
                            <h3 className="text-xl font-bold">
                                {initialReview ? "Edit Your Review" : "Write a Review"}
                            </h3>
                        </div>
                        <button
                            onClick={onClose}
                            className="p-2 hover:bg-muted rounded-full transition-colors"
                        >
                            <X className="h-5 w-5" />
                        </button>
                    </div>

                    <div className="flex-1 overflow-y-auto p-6 space-y-6">
                        <div>
                            <p className="text-sm text-muted-foreground uppercase tracking-widest font-bold mb-1">
                                {initialReview ? "You are editing review for" : "You are reviewing"}
                            </p>
                            <h4 className="text-2xl font-bold">{title}</h4>
                        </div>

                        <div className="space-y-4">
                            <label className="block">
                                <span className="text-sm font-bold block mb-2 text-muted-foreground uppercase">
                                    {initialReview ? "Update your thoughts" : "Your thoughts"}
                                </span>
                                <textarea
                                    value={reviewText}
                                    onChange={(e) => setReviewText(e.target.value)}
                                    placeholder="Write your thoughts here..."
                                    className={cn(
                                        "w-full h-64 p-4 bg-muted border border-border rounded-xl focus:ring-2 focus:ring-primary focus:border-transparent outline-none transition-all resize-none text-lg",
                                        reviewText.length > 0 && reviewText.length < 10 && "border-destructive focus:ring-destructive"
                                    )}
                                />
                                <div className="flex justify-between mt-2">
                                    <p className={cn(
                                        "text-xs font-medium",
                                        reviewText.length > 0 && reviewText.length < 10 ? "text-destructive" : "text-muted-foreground"
                                    )}>
                                        {reviewText.length > 0 && reviewText.length < 10 
                                            ? `Minimum 10 characters required (${10 - reviewText.length} more)`
                                            : reviewText.length > 5000 
                                                ? "Maximum 5000 characters allowed"
                                                : "Describe your experience"}
                                    </p>
                                    <span className={cn(
                                        "text-xs font-mono",
                                        (reviewText.length < 10 && reviewText.length > 0) || reviewText.length > 5000 ? "text-destructive font-bold" : "text-muted-foreground"
                                    )}>
                                        {reviewText.length}/5000
                                    </span>
                                </div>
                            </label>

                            <div className="bg-muted/50 p-4 rounded-xl border border-dashed border-border">
                                <h5 className="text-xs font-bold uppercase text-muted-foreground mb-2">Review Guidelines</h5>
                                <ul className="text-xs text-muted-foreground space-y-1 list-disc pl-4">
                                    <li>Be respectful and avoid spoilers if possible.</li>
                                    <li>Focus on the quality of the media and performance.</li>
                                    <li>Help other users by being honest and descriptive.</li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div className="p-6 border-t border-border bg-muted/30">
                        <Button
                            onClick={() => {
                                onSubmit(reviewText);
                                if (!initialReview) setReviewText("");
                            }}
                            disabled={reviewText.length < 10 || reviewText.length > 5000}
                            className="w-full h-14 rounded-xl font-bold text-lg flex items-center justify-center gap-2"
                        >
                            <Send className="h-5 w-5" />
                            {initialReview ? "Save Changes" : "Submit Review"}
                        </Button>
                    </div>
                </motion.div>
            </div>
        </AnimatePresence>
    );
}
