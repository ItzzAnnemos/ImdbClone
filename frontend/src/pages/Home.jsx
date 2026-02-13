import { Layout } from "../components/layout/Layout";
import { MovieCard } from "../components/ui/MovieCard";
import { Button } from "../components/ui/Button";
import { Hero } from "../components/ui/Hero";

const DUMMY_MOVIES = [
    { id: 1, title: "Inception", rating: 8.8, year: 2010, image: "https://image.tmdb.org/t/p/w500/oYuLEt3zVCKqWD8f5vFZNuDHZM7.jpg" },
    { id: 2, title: "The Dark Knight", rating: 9.0, year: 2008, image: "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg" },
    { id: 3, title: "Interstellar", rating: 8.6, year: 2014, image: "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg" },
    { id: 4, title: "Parasite", rating: 8.5, year: 2019, image: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg" },
    { id: 5, title: "Avengers: Endgame", rating: 8.4, year: 2019, image: "https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg" },
    { id: 6, title: "Joker", rating: 8.2, year: 2019, image: "https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg" },
];

export function Home() {
    return (
        <Layout>
            <Hero />

            {/* Movie Grid */}
            <section>
                <div className="flex items-center justify-between mb-6">
                    <h2 className="text-2xl font-bold text-foreground">Trending Now</h2>
                    <Button variant="link" className="text-primary">View All</Button>
                </div>

                <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6">
                    {DUMMY_MOVIES.map((movie) => (
                        <MovieCard
                            key={movie.id}
                            {...movie}
                        />
                    ))}
                </div>
            </section>
        </Layout>
    );
}
