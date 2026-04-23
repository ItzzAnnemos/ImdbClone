import { useState, useEffect } from "react";
import { useParams, useLocation, Link } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import {
    Star,
    Clock,
    Calendar,
    Play,
    Plus,
    Check,
    ChevronRight,
    Loader2,
    Users,
    Clapperboard,
    Info
} from "lucide-react";
import { Layout } from "../components/layout/Layout";
import { Button } from "../components/ui/Button";
import { MediaSlider } from "../components/ui/MediaSlider";
import * as mediaService from "../lib/mediaService";
import { useAuth } from "../context/AuthContext";
import { useWatchlist } from "../hooks/useWatchlist";
import { cn } from "../lib/utils";

export function MediaDetails() {
    const { id } = useParams();
    const location = useLocation();
    const { user } = useAuth();

    const isTV = location.pathname.startsWith("/tv");
    const { inWatchlist, toggling, toggle } = useWatchlist(user?.username, id);

    const [media, setMedia] = useState(null);
    const [similarMedia, setSimilarMedia] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            setError(null);
            try {
                const mediaData = isTV
                    ? await mediaService.getTVSeriesById(id)
                    : await mediaService.getMovieById(id);

                const similarData = isTV
                    ? await mediaService.getSimilarTVSeries(id)
                    : await mediaService.getSimilarMovies(id);

                setMedia(mediaData);
                setSimilarMedia(similarData);
            } catch (err) {
                console.error("Error fetching media details:", err);
                setError("Failed to load media details. Please try again later.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
        window.scrollTo(0, 0);
    }, [id, isTV]);

    if (loading) {
        return (
            <Layout>
                <div className="flex h-[70vh] items-center justify-center">
                    <Loader2 className="h-12 w-12 animate-spin text-primary" />
                </div>
            </Layout>
        );
    }

    if (error || !media) {
        return (
            <Layout>
                <div className="flex flex-col items-center justify-center h-[70vh] text-center px-4">
                    <Info className="h-16 w-16 text-muted-foreground mb-4" />
                    <h2 className="text-2xl font-bold mb-2">Oops! Something went wrong</h2>
                    <p className="text-muted-foreground mb-6 max-w-md">{error || "Media not found."}</p>
                    <Button onClick={() => window.location.reload()}>Try Again</Button>
                </div>
            </Layout>
        );
    }

    const directors = media.cast.filter(p => p.role === "DIRECTOR" || p.role === "CREATOR");
    const writers = media.cast.filter(p => p.role === "WRITER");
    const stars = media.cast.filter(p => p.role === "MAIN_ACTOR" || p.role === "ACTOR").slice(0, 3);

    return (
        <Layout>
            <div className="relative pb-20">
                {/* Background Backdrop Blur (IMDb style) */}
                <div className="absolute inset-0 h-[500px] overflow-hidden -z-10 opacity-20 blur-3xl pointer-events-none">
                    <img
                        src={media.image}
                        alt=""
                        className="w-full h-full object-cover scale-150"
                    />
                </div>

                <div className="container mx-auto px-4 py-8">
                    {/* Header Section */}
                    <div className="flex flex-col md:flex-row md:items-end justify-between gap-6 mb-8">
                        <div>
                            <h1 className="text-4xl md:text-6xl font-extrabold tracking-tight mb-2">
                                {media.title}
                            </h1>
                            <div className="flex flex-wrap items-center gap-4 text-muted-foreground text-sm md:text-base">
                                <span className="flex items-center gap-1">
                                    <Calendar className="h-4 w-4" /> {media.year}
                                </span>
                                <span className="text-border">•</span>
                                {isTV ? (
                                    <span>{media.numberOfSeasons} {media.numberOfSeasons === 1 ? 'Season' : 'Seasons'}</span>
                                ) : (
                                    <span className="flex items-center gap-1">
                                        <Clock className="h-4 w-4" /> {Math.floor(media.duration / 60)}h {media.duration % 60}m
                                    </span>
                                )}
                                <span className="text-border">•</span>
                                <span className="uppercase tracking-wider font-semibold border px-2 py-0.5 rounded text-xs">
                                    {isTV ? media.status : "Movie"}
                                </span>
                            </div>
                        </div>

                        <div className="flex items-center gap-6">
                            <div className="flex flex-col items-center">
                                <span className="text-xs uppercase tracking-widest text-muted-foreground font-bold mb-1">IMDb RATING</span>
                                <div className="flex items-center gap-2">
                                    <Star className="h-8 w-8 text-yellow-400 fill-current" />
                                    <div>
                                        <div className="text-2xl font-bold leading-none">{media.rating?.toFixed(1) || "N/A"}<span className="text-muted-foreground text-sm">/10</span></div>
                                        <div className="text-[10px] text-muted-foreground font-medium uppercase tracking-tighter">from users</div>
                                    </div>
                                </div>
                            </div>

                            {user && (
                                <Button
                                    variant={inWatchlist ? "secondary" : "primary"}
                                    size="lg"
                                    className={cn(
                                        "h-14 px-8 rounded-full font-bold transition-all duration-300",
                                        !inWatchlist && "bg-yellow-400 hover:bg-yellow-500 text-black border-none"
                                    )}
                                    onClick={toggle}
                                    disabled={toggling}
                                >
                                    {toggling ? (
                                        <Loader2 className="h-5 w-5 animate-spin mr-2" />
                                    ) : inWatchlist ? (
                                        <Check className="h-5 w-5 mr-2" />
                                    ) : (
                                        <Plus className="h-5 w-5 mr-2" />
                                    )}
                                    {inWatchlist ? "In Watchlist" : "Add to Watchlist"}
                                </Button>
                            )}
                        </div>
                    </div>

                    {/* Main Media Section */}
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 mb-12">
                        {/* Poster */}
                        <motion.div
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            className="relative aspect-[2/3] rounded-xl overflow-hidden shadow-2xl group"
                        >
                            <img
                                src={media.image || "https://placehold.co/600x900?text=No+Poster"}
                                alt={media.title}
                                className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105"
                            />
                        </motion.div>

                        {/* Trailer/Hero Placeholder */}
                        <motion.div
                            initial={{ opacity: 0, scale: 0.95 }}
                            animate={{ opacity: 1, scale: 1 }}
                            className="lg:col-span-2 relative aspect-video bg-muted rounded-xl overflow-hidden group shadow-2xl"
                        >
                            <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/20 to-transparent z-10" />
                            <img
                                src={media.image} // In real app, this would be a backdrop/trailer thumbnail
                                alt="Trailer"
                                className="w-full h-full object-cover opacity-60 grayscale-[0.5] group-hover:grayscale-0 transition-all duration-700"
                            />
                            <div className="absolute inset-0 z-20 flex flex-col items-center justify-center">
                                <motion.div
                                    whileHover={{ scale: 1.1 }}
                                    whileTap={{ scale: 0.9 }}
                                    className="h-20 w-20 bg-yellow-400 rounded-full flex items-center justify-center cursor-pointer shadow-lg shadow-yellow-400/20"
                                >
                                    <Play className="h-10 w-10 text-black fill-current ml-1" />
                                </motion.div>
                                <p className="mt-4 font-bold text-lg tracking-widest uppercase text-white shadow-sm">Play Trailer</p>
                            </div>
                        </motion.div>
                    </div>

                    {/* Content Section */}
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-12">
                        <div className="lg:col-span-2 space-y-8">
                            {/* Genres */}
                            <div className="flex flex-wrap gap-2">
                                {media.genres.map(genre => (
                                    <Link
                                        key={genre.id}
                                        to={`/genre/${genre.name}`}
                                        className="px-4 py-1.5 rounded-full border border-border hover:bg-muted transition-colors text-sm font-medium"
                                    >
                                        {genre.name}
                                    </Link>
                                ))}
                            </div>

                            {/* Description */}
                            <div>
                                <h3 className="text-xl font-bold mb-3 flex items-center gap-2">
                                    <Info className="h-5 w-5 text-yellow-400" /> Plot Summary
                                </h3>
                                <p className="text-lg leading-relaxed text-muted-foreground">
                                    {media.description || "No description available for this title."}
                                </p>
                            </div>

                            {/* Key Personnel */}
                            <div className="space-y-4 border-y border-border py-6">
                                {directors.length > 0 && (
                                    <div className="flex gap-4">
                                        <span className="font-bold min-w-[100px]">{isTV ? 'Creators' : 'Directors'}</span>
                                        <div className="flex flex-wrap gap-x-4 gap-y-1">
                                            {directors.map(d => (
                                                <Link key={d.personId} to={`/person/${d.personId}`} className="text-primary hover:underline font-medium">
                                                    {d.personFirstName} {d.personLastName}
                                                </Link>
                                            ))}
                                        </div>
                                    </div>
                                )}
                                {writers.length > 0 && (
                                    <div className="flex gap-4 border-t border-border/50 pt-4">
                                        <span className="font-bold min-w-[100px]">Writers</span>
                                        <div className="flex flex-wrap gap-x-4 gap-y-1">
                                            {writers.map(w => (
                                                <Link key={w.personId} to={`/person/${w.personId}`} className="text-primary hover:underline font-medium">
                                                    {w.personFirstName} {w.personLastName}
                                                </Link>
                                            ))}
                                        </div>
                                    </div>
                                )}
                                {stars.length > 0 && (
                                    <div className="flex gap-4 border-t border-border/50 pt-4">
                                        <span className="font-bold min-w-[100px]">Stars</span>
                                        <div className="flex flex-wrap gap-x-4 gap-y-1">
                                            {stars.map(s => (
                                                <Link key={s.personId} to={`/person/${s.personId}`} className="text-primary hover:underline font-medium">
                                                    {s.personFirstName} {s.personLastName}
                                                </Link>
                                            ))}
                                        </div>
                                    </div>
                                )}
                            </div>

                            {/* Full Cast */}
                            <section>
                                <div className="flex items-center justify-between mb-6">
                                    <h3 className="text-2xl font-bold flex items-center gap-2">
                                        <Users className="h-6 w-6 text-yellow-400" /> Top Cast
                                    </h3>
                                    <Link to={`/title/${id}/fullcredits`} className="text-primary hover:underline flex items-center text-sm font-bold">
                                        Full cast & crew <ChevronRight className="h-4 w-4" />
                                    </Link>
                                </div>
                                <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-6">
                                    {media.cast.filter(p => p.role.includes("ACTOR")).slice(0, 8).map((person, i) => (
                                        <motion.div
                                            key={`${person.personId}-${i}`}
                                            initial={{ opacity: 0, y: 10 }}
                                            animate={{ opacity: 1, y: 0 }}
                                            transition={{ delay: i * 0.05 }}
                                            className="flex flex-col items-center text-center group"
                                        >
                                            <div className="w-24 h-24 md:w-32 md:h-32 rounded-full overflow-hidden mb-3 border-2 border-transparent group-hover:border-yellow-400 transition-all duration-300">
                                                <img
                                                    src={person.personProfilePictureUrl || `https://ui-avatars.com/api/?name=${person.personFirstName}+${person.personLastName}&background=random`}
                                                    alt={person.personFirstName}
                                                    className="w-full h-full object-cover"
                                                />
                                            </div>
                                            <Link to={`/person/${person.personId}`} className="font-bold hover:text-primary transition-colors">
                                                {person.personFirstName} {person.personLastName}
                                            </Link>
                                            <span className="text-sm text-muted-foreground line-clamp-1">{person.characterName || "Actor"}</span>
                                        </motion.div>
                                    ))}
                                </div>
                            </section>
                        </div>

                        {/* Sidebar */}
                        <div className="space-y-8">
                            <div className="bg-card border border-border rounded-xl p-6 shadow-lg">
                                <h4 className="font-bold mb-4 uppercase tracking-widest text-xs text-muted-foreground">More Like This</h4>
                                <div className="space-y-4">
                                    {similarMedia.slice(0, 4).map(item => (
                                        <Link
                                            key={item.id}
                                            to={item.type === 'tv' ? `/tv/${item.id}` : `/movie/${item.id}`}
                                            className="flex gap-4 group"
                                        >
                                            <div className="w-16 h-24 flex-shrink-0 overflow-hidden rounded shadow-md">
                                                <img
                                                    src={item.posterUrl}
                                                    alt={item.title}
                                                    className="w-full h-full object-cover transition-transform group-hover:scale-110"
                                                />
                                            </div>
                                            <div className="flex flex-col justify-center">
                                                <span className="font-bold line-clamp-2 group-hover:text-primary transition-colors">
                                                    {item.title}
                                                </span>
                                                <div className="flex items-center gap-2 mt-1">
                                                    <Star className="h-3 w-3 text-yellow-400 fill-current" />
                                                    <span className="text-xs text-muted-foreground">{item.averageRating?.toFixed(1) || "N/A"}</span>
                                                </div>
                                            </div>
                                        </Link>
                                    ))}
                                </div>
                                <Button variant="secondary" className="w-full mt-6 text-sm font-bold py-5">
                                    Explore More Similar
                                </Button>
                            </div>

                            <div className="bg-gradient-to-br from-yellow-400 to-yellow-600 rounded-xl p-6 text-black shadow-lg shadow-yellow-500/20">
                                <Clapperboard className="h-8 w-8 mb-4" />
                                <h4 className="text-xl font-extrabold mb-2 leading-tight">Pro Tip: Track your favorites</h4>
                                <p className="text-sm font-medium opacity-90 mb-4">
                                    Sign in to save movies to your watchlist and get personalized recommendations based on what you love.
                                </p>
                                {!user && (
                                    <Link to="/login">
                                        <Button className="w-full bg-black text-white hover:bg-black/80 border-none font-bold">
                                            Join Now
                                        </Button>
                                    </Link>
                                )}
                            </div>
                        </div>
                    </div>
                </div>

                {/* Similar Media Section (Full Width Slider) */}
                {similarMedia.length > 0 && (
                    <section className="mt-16 py-16">
                        <div className="container mx-auto px-4">
                            <h3 className="text-3xl font-bold mb-8 px-4 border-l-4 border-yellow-400 pl-6">
                                Customers also watched
                            </h3>
                            <MediaSlider
                                title=""
                                items={similarMedia.map(m => ({
                                    id: m.id,
                                    title: m.title,
                                    rating: m.averageRating,
                                    image: m.posterUrl,
                                    type: m.type
                                }))}
                            />
                        </div>
                    </section>
                )}
            </div>
        </Layout>
    );
}
