import { Link } from "react-router-dom";
import { Search, Menu, User } from "lucide-react";
import { Button } from "../ui/Button";
import { ModeToggle } from "../ui/ModeToggle";

export function Navbar() {
    return (
        <nav className="sticky top-0 z-50 w-full border-b border-border/40 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
            <div className="container flex h-16 items-center justify-between px-4 md:px-8">
                <div className="flex items-center gap-6">
                    <Link to="/" className="flex items-center gap-2 font-bold text-lg md:text-xl text-primary hover:opacity-90 transition-opacity">
                        <span className="bg-yellow-500 text-black px-2 py-0.5 rounded-md">IMDb</span>
                        <span className="hidden sm:inline-block">Clone</span>
                    </Link>
                    <div className="hidden md:flex items-center text-sm font-medium text-muted-foreground gap-6">
                        <Link to="#" className="hover:text-foreground transition-colors">Movies</Link>
                        <Link to="#" className="hover:text-foreground transition-colors">TV Shows</Link>
                        <Link to="#" className="hover:text-foreground transition-colors">Celebs</Link>
                    </div>
                </div>

                <div className="flex flex-1 items-center justify-center max-w-md mx-4 hidden sm:flex">
                    <div className="relative w-full">
                        <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                        <input
                            type="search"
                            placeholder="Search IMDb..."
                            className="w-full rounded-md border border-input bg-secondary px-9 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                        />
                    </div>
                </div>

                <div className="flex items-center gap-2">
                    <Button variant="ghost" size="sm" className="hidden sm:flex items-center gap-2">
                        <span className="font-semibold">Watchlist</span>
                    </Button>
                    <ModeToggle />
                    <Button variant="ghost" size="icon">
                        <User className="h-5 w-5" />
                    </Button>
                    <Button variant="ghost" size="icon" className="md:hidden">
                        <Menu className="h-5 w-5" />
                    </Button>
                </div>
            </div>
        </nav>
    );
}
