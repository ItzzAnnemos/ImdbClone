import { Button } from "./Button";
import { Play, Info } from "lucide-react";

export function Hero({ movie }) {
    // Default values if no movie is passed (or for the static version)
    const title = movie?.title || "Dune: Part Two";
    const description = movie?.description ||
        "Paul Atreides unites with Chani and the Fremen while on a warpath of revenge against the conspirators who destroyed his family. Facing a choice between the love of his life and the fate of the known universe, he endeavors to prevent a terrible future only he can foresee.";
    const image = movie?.image || "https://image.tmdb.org/t/p/original/mSDsSDwaP3E7dEfUPWy4J0djt4O.jpg";

    return (
        <section className="relative mb-12 rounded-xl overflow-hidden shadow-2xl">
            <div className="absolute inset-0 bg-gradient-to-r from-black via-black/70 to-transparent z-10" />
            <img
                src={image}
                alt="Featured Movie"
                className="w-full h-[500px] object-cover object-top"
            />
            <div className="absolute inset-0 z-20 flex flex-col justify-center px-8 md:px-16 max-w-2xl">
                <span className="text-yellow-500 font-bold tracking-wider mb-2">
                    FEATURED PREMIERE
                </span>
                <h1 className="text-4xl md:text-6xl font-bold text-white mb-4">{title}</h1>
                <p className="text-gray-300 text-lg mb-6 line-clamp-3">{description}</p>
                <div className="flex flex-wrap gap-4">
                    <Button size="lg" className="rounded-full gap-2">
                        <Play className="h-5 w-5 fill-current" /> Watch Trailer
                    </Button>
                    <Button variant="secondary" size="lg" className="rounded-full gap-2">
                        <Info className="h-5 w-5" /> More Info
                    </Button>
                </div>
            </div>
        </section>
    );
}
