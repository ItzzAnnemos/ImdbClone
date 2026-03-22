import { useState, useEffect } from "react";
import { Layout } from "../components/layout/Layout";
import { MediaSlider } from "../components/ui/MediaSlider";
import { Button } from "../components/ui/Button";
import { Hero } from "../components/ui/Hero";
import { Loader2 } from "lucide-react";
import * as mediaService from "../lib/mediaService";

export function Home() {
    const [trendingMovies, setTrendingMovies] = useState([]);
    const [recentMovies, setRecentMovies] = useState([]);
    const [trendingTVSeries, setTrendingTVSeries] = useState([]);
    const [recentTVSeries, setRecentTVSeries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

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

    const MediaSection = ({ title, items }) => (
        <MediaSlider title={title} items={items} />
    );

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
                        <Button onClick={() => window.location.reload()} variant="outline" className="mt-4">
                            Retry
                        </Button>
                    </div>
                ) : (
                    <>
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
