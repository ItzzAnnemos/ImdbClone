import { Star, MoreHorizontal, Pencil, Trash2 } from "lucide-react";
import { format } from "date-fns";
import { useAuth } from "../../context/AuthContext";
import { useState, useRef, useEffect } from "react";
import { cn } from "../../lib/utils";

export function ReviewCard({ review, onEdit, onDelete }) {
    const { user } = useAuth();
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const menuRef = useRef(null);
    
    const date = review.createdAt ? format(new Date(review.createdAt), "dd MMM yyyy") : "Recently";
    const isOwner = user && user.username === review.username;

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setIsMenuOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    return (
        <div className="bg-card border border-border rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow relative">
            <div className="flex items-start justify-between mb-4">
                <div className="flex items-center gap-3">
                    <div className="h-10 w-10 rounded-full bg-primary/10 flex items-center justify-center font-bold text-primary">
                        {review.username?.charAt(0).toUpperCase()}
                    </div>
                    <div>
                        <h4 className="font-bold text-foreground">{review.username}</h4>
                        <p className="text-xs text-muted-foreground">
                            {date}
                            {review.isEdited && <span className="ml-2 text-[10px] uppercase tracking-tighter opacity-70">(Edited)</span>}
                        </p>
                    </div>
                </div>
                
                {isOwner && (
                    <div className="relative" ref={menuRef}>
                        <button 
                            onClick={() => setIsMenuOpen(!isMenuOpen)}
                            className="text-muted-foreground hover:text-foreground p-1 rounded-full hover:bg-muted transition-colors"
                        >
                            <MoreHorizontal className="h-5 w-5" />
                        </button>
                        
                        {isMenuOpen && (
                            <div className="absolute right-0 mt-2 w-36 bg-popover border border-border rounded-lg shadow-xl z-20 overflow-hidden animate-in fade-in zoom-in duration-200">
                                <button
                                    onClick={() => {
                                        setIsMenuOpen(false);
                                        onEdit(review);
                                    }}
                                    className="flex items-center gap-2 w-full px-4 py-2.5 text-sm hover:bg-muted transition-colors text-foreground"
                                >
                                    <Pencil className="h-4 w-4" /> Edit
                                </button>
                                <button
                                    onClick={() => {
                                        setIsMenuOpen(false);
                                        onDelete(review.id);
                                    }}
                                    className="flex items-center gap-2 w-full px-4 py-2.5 text-sm hover:bg-destructive/10 text-destructive hover:text-destructive transition-colors border-t border-border"
                                >
                                    <Trash2 className="h-4 w-4" /> Delete
                                </button>
                            </div>
                        )}
                    </div>
                )}
            </div>

            <div className="mb-2">
                <p className="text-muted-foreground leading-relaxed">
                    {review.reviewText}
                </p>
            </div>
        </div>
    );
}
