import { useState, useRef, useEffect } from "react";
import { Link } from "react-router-dom";
import { Search, Menu, User, LogOut, ChevronDown, Bookmark } from "lucide-react";
import { Button } from "../ui/Button";
import { ModeToggle } from "../ui/ModeToggle";
import { useAuth } from "../../context/AuthContext";

export function Navbar() {
    const { user, logout } = useAuth();
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsDropdownOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);
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
                    <div className="flex items-center gap-2">
                        <ModeToggle />
                        {user ? (
                            <div className="relative" ref={dropdownRef}>
                                <Button
                                    variant="ghost"
                                    size="icon"
                                    className="rounded-full"
                                    onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                                >
                                    <User className="h-5 w-5" />
                                </Button>

                                {isDropdownOpen && (
                                    <div className="absolute right-0 mt-2 w-48 rounded-md shadow-lg bg-popover border border-border overflow-hidden z-50">
                                        <div className="px-4 py-3 border-b border-border/50">
                                            <p className="text-sm font-medium text-foreground truncate">
                                                {user.username}
                                            </p>
                                        </div>
                                        <div className="p-1">
                                            <Link
                                                to="/watchlist"
                                                onClick={() => setIsDropdownOpen(false)}
                                                className="w-full flex items-center justify-start gap-2 px-3 py-2 text-sm text-foreground hover:bg-secondary/80 rounded-sm transition-colors mb-1"
                                            >
                                                <Bookmark className="h-4 w-4" />
                                                <span>Watchlist</span>
                                            </Link>
                                            <button
                                                onClick={() => {
                                                    setIsDropdownOpen(false);
                                                    logout();
                                                }}
                                                className="w-full flex items-center justify-start gap-2 px-3 py-2 text-sm text-destructive hover:bg-destructive/10 rounded-sm transition-colors"
                                            >
                                                <LogOut className="h-4 w-4" />
                                                <span>Logout</span>
                                            </button>
                                        </div>
                                    </div>
                                )}
                            </div>
                        ) : (
                            <div className="flex items-center gap-2">
                                <Link to="/login">
                                    <Button variant="ghost" size="sm" className="hidden sm:flex">
                                        Login
                                    </Button>
                                </Link>
                                <Link to="/register">
                                    <Button size="sm" className="hidden sm:flex bg-foreground text-background">
                                        Sign Up
                                    </Button>
                                </Link>
                                <Link to="/login" className="sm:hidden">
                                    <Button variant="ghost" size="icon">
                                        <User className="h-5 w-5" />
                                    </Button>
                                </Link>
                            </div>
                        )}
                        <Button variant="ghost" size="icon" className="md:hidden">
                            <Menu className="h-5 w-5" />
                        </Button>
                    </div>
                </div>
            </div>
        </nav>
    );
}
