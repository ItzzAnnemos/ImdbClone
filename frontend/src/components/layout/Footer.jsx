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
                <div className="flex flex-col items-center gap-2 text-xs text-muted-foreground">
                    <p>an Antigravity Company</p>
                    <p>&copy; 1990-{new Date().getFullYear()} by IMDb.com, Inc.</p>
                </div>
            </div>
        </footer>
    );
}
