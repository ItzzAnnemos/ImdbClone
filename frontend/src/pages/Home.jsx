import { useState, useEffect } from "react";
import { Layout } from "../components/layout/Layout";
import { MediaSlider } from "../components/ui/MediaSlider";
import { Button } from "../components/ui/Button";
import { Hero } from "../components/ui/Hero";
import { AlertCircle, Loader2 } from "lucide-react";
import * as mediaService from "../lib/mediaService";
import * as userMediaService from "../lib/userMediaService";
import { useAuth } from "../context/AuthContext";

export function Home() {
    const { user } = useAuth();
    const [trendingMovies, setTrendingMovies] = useState([]);
    const [recentMovies, setRecentMovies] = useState([]);
    const [trendingTVSeries, setTrendingTVSeries] = useState([]);
    const [recentTVSeries, setRecentTVSeries] = useState([]);
    const [recommendations, setRecommendations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [recommendationsLoading, setRecommendationsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [recommendationsError, setRecommendationsError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const [trendingM, recentM, trendingTV, recentTV] = await Promise.all([
                    mediaService.getTrendingMovies(),
                    mediaService.getRecentMovies(),
                    mediaService.getTrendingTVSeries(),
                    mediaService.getRecentTVSeries(),
                ]);

                setTrendingMovies(trendingM);
                setRecentMovies(recentM);
                setTrendingTVSeries(trendingTV);
                setRecentTVSeries(recentTV);
                setError(null);
            } catch (err) {
                console.error("Error fetching media content:", err);
                setError("Failed to load content. Please try again later.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        if (!user?.id) {
            setRecommendations([]);
            setRecommendationsLoading(false);
            setRecommendationsError(null);
            return;
        }
        let cancelled = false;

        async function fetchRecommendations() {
            try {
                setRecommendationsLoading(true);
                setRecommendationsError(null);
                const data = await userMediaService.getRecommendations(user.id);
                if (!cancelled) setRecommendations(data);
            } catch (err) {
                console.error("Error fetching recommendations:", err);
                if (!cancelled) {
                    setRecommendationsError("Failed to load recommendations.");
                }
            } finally {
                if (!cancelled) setRecommendationsLoading(false);
            }
        }

        fetchRecommendations();
        return () => {
            cancelled = true;
        };
    }, [user?.id]);

    const MediaSection = ({ title, items }) => <MediaSlider title={title} items={items} />;

    const RecommendationSection = () => {
        if (!user) return null;

        return (
            <section className="mb-4 md:mb-12">
                {recommendationsLoading && (
                    <div className="flex min-h-36 items-center justify-center gap-3 rounded-xl border border-border bg-muted/20 text-sm text-muted-foreground">
                        <Loader2 className="h-5 w-5 animate-spin text-primary" />
                        Loading recommendations...
                    </div>
                )}

                {!recommendationsLoading && recommendationsError && (
                    <div className="flex min-h-36 items-center justify-center gap-3 rounded-xl border border-destructive/30 bg-destructive/5 text-sm text-destructive">
                        <AlertCircle className="h-5 w-5" />
                        {recommendationsError}
                    </div>
                )}

                {!recommendationsLoading &&
                    !recommendationsError &&
                    recommendations.length === 0 && (
                        <div className="rounded-xl border border-dashed border-border bg-muted/20 px-4 py-8 text-center text-sm text-muted-foreground">
                            Rate or save a few movies and TV series to shape this section.
                        </div>
                    )}

                {!recommendationsLoading && !recommendationsError && recommendations.length > 0 && (
                    <MediaSlider title="Recommended for You" items={recommendations} />
                )}
            </section>
        );
    };

    return (
        <Layout>
            <Hero movie={trendingMovies[0]} />

            <div className="container mx-auto px-4 py-8">
                {loading ? (
                    <div className="flex flex-col items-center justify-center min-h-[400px] text-muted-foreground">
                        <Loader2 className="h-10 w-10 animate-spin mb-4 text-primary" />
                        <p>Loading your favorite content...</p>
                    </div>
                ) : error ? (
                    <div className="flex flex-col items-center justify-center min-h-[400px] text-destructive">
                        <p className="text-xl font-semibold mb-2">Oops!</p>
                        <p>{error}</p>
                        <Button
                            onClick={() => window.location.reload()}
                            variant="outline"
                            className="mt-4"
                        >
                            Retry
                        </Button>
                    </div>
                ) : (
                    <>
                        <RecommendationSection />
                        <MediaSection title="Trending Movies" items={trendingMovies} />
                        <MediaSection title="Recent Movies" items={recentMovies} />
                        <MediaSection title="Trending TV Series" items={trendingTVSeries} />
                        <MediaSection title="Recent TV Series" items={recentTVSeries} />
                    </>
                )}
            </div>
        </Layout>
    );
}
