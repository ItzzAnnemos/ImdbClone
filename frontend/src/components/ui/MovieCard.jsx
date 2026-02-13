import { motion } from "framer-motion";
import { Star } from "lucide-react";
import { Link } from "react-router-dom";
import { cn } from "../../lib/utils";

export function MovieCard({ id, title, rating, image, year, className, ...props }) {
    return (
        <Link to={`/movie/${id}`}>
            <motion.div
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                className={cn(
                    "group relative overflow-hidden rounded-lg bg-card shadow-lg transition-all hover:shadow-xl cursor-pointer",
                    className
                )}
                {...props}
            >
                <div className="aspect-[2/3] w-full overflow-hidden">
                    <img
                        src={image || "https://placehold.co/400x600?text=No+Image"}
                        alt={title}
                        className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-110"
                        loading="lazy"
                    />
                </div>
                <div className="absolute inset-x-0 bottom-0 bg-gradient-to-t from-black/90 via-black/60 to-transparent p-4 pt-12 text-white opacity-0 transition-opacity duration-300 group-hover:opacity-100">
                    <h3 className="font-bold text-lg leading-tight line-clamp-2">{title}</h3>
                    <div className="mt-2 flex items-center justify-between text-sm">
                        <div className="flex items-center gap-1 text-yellow-500">
                            <Star className="h-4 w-4 fill-current" />
                            <span className="font-medium text-white">{rating}</span>
                        </div>
                        <span className="text-gray-300">{year}</span>
                    </div>
                </div>
            </motion.div>
        </Link>
    );
}
