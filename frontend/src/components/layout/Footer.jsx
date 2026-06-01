import { Facebook, Instagram, Twitter } from "lucide-react";
import { Link } from "react-router-dom";
import { Button } from "../ui/Button";

export function Footer() {
    return (
        <footer className="w-full border-t border-border/40 bg-background py-8 md:py-12">
            <div className="container px-4 md:px-8 flex flex-col items-center gap-8">
                {/* Social Icons */}
                <div className="flex gap-6">
                    <Button
                        variant="ghost"
                        size="icon"
                        className="hover:text-yellow-500 hover:bg-transparent"
                    >
                        <Instagram className="h-6 w-6" />
                    </Button>
                    <Button
                        variant="ghost"
                        size="icon"
                        className="hover:text-yellow-500 hover:bg-transparent"
                    >
                        <Twitter className="h-6 w-6" />
                    </Button>
                    <Button
                        variant="ghost"
                        size="icon"
                        className="hover:text-yellow-500 hover:bg-transparent"
                    >
                        <Facebook className="h-6 w-6" />
                    </Button>
                </div>

                {/* Links */}
                <div className="flex flex-wrap justify-center gap-x-8 gap-y-4 text-sm font-medium text-foreground">
                    <Link to="#" className="hover:underline underline-offset-4">
                        Get the IMDb App
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        Help
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        Site Index
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        IMDbPro
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        Box Office Mojo
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        License IMDb Data
                    </Link>
                </div>

                <div className="flex flex-wrap justify-center gap-x-8 gap-y-4 text-sm font-medium text-foreground">
                    <Link to="#" className="hover:underline underline-offset-4">
                        Press Room
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        Advertising
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        Jobs
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        Conditions of Use
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        Privacy Policy
                    </Link>
                    <Link to="#" className="hover:underline underline-offset-4">
                        Your Ads Privacy Choices
                    </Link>
                </div>

                {/* Copyright */}
                <div className="flex flex-col items-center gap-3 text-center text-xs text-muted-foreground">
                    <div className="flex flex-col items-center gap-2">
                        <span className="font-medium text-foreground">
                            IMDbClone student project
                        </span>
                        <a
                            href="https://www.themoviedb.org"
                            target="_blank"
                            rel="noreferrer"
                            aria-label="The Movie Database"
                        >
                            <img
                                src="https://www.themoviedb.org/assets/2/v4/logos/v2/blue_square_2-d537fb228cf3ded904ef09b136fe3fec72548ebc1fea3fbbd1ad9e36364db38b.svg"
                                alt="TMDB"
                                className="h-8 w-auto"
                            />
                        </a>
                    </div>
                    <p>This product uses the TMDB API but is not endorsed or certified by TMDB.</p>
                    <p>
                        Movie data and images are provided by{" "}
                        <a
                            href="https://www.themoviedb.org"
                            target="_blank"
                            rel="noreferrer"
                            className="underline underline-offset-4 hover:text-foreground"
                        >
                            The Movie Database
                        </a>
                        .
                    </p>
                    <p>&copy; {new Date().getFullYear()} IMDbClone team.</p>
                </div>
            </div>
        </footer>
    );
}
