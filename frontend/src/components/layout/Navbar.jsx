import { useState, useRef, useEffect } from "react";
import { Link } from "react-router-dom";
import { Search, Menu, User, LogOut, ChevronDown, Bookmark } from "lucide-react";
import { Button } from "../ui/Button";
import { ModeToggle } from "../ui/ModeToggle";
import { useAuth } from "../../context/AuthContext";

const navMenus = [
    {
        label: "Movies",
        items: [
            { label: "Top 250 Movies", to: "/movies/top-250" },
            { label: "Most Popular Movies", to: "/movies/most-popular" },
            { label: "Browse Movies by Genre", to: "/movies/genre" },
        ],
    },
    {
        label: "TV Shows",
        items: [
            { label: "Top 250 TV Shows", to: "/tv/top-250" },
            { label: "Most Popular TV Shows", to: "/tv/most-popular" },
            { label: "Browse TV Shows by Genre", to: "/tv/genre" },
        ],
    },
    {
        label: "Celebs",
        items: [
            { label: "Born Today", to: "/celebs/born-today" },
            { label: "Most Popular Celebs", to: "/celebs/most-popular" },
        ],
    },
];

export function Navbar() {
    const { user, logout } = useAuth();
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const [openNavMenu, setOpenNavMenu] = useState(null);
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
    const dropdownRef = useRef(null);
    const navMenuRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsDropdownOpen(false);
            }
            if (navMenuRef.current && !navMenuRef.current.contains(event.target)) {
                setOpenNavMenu(null);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);
    return (
        <nav className="sticky top-0 z-50 w-full border-b border-border/40 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
            <div className="container flex h-16 items-center justify-between px-4 md:px-8">
                <div className="flex items-center gap-6">
                    <Link
                        to="/"
                        className="flex items-center gap-2 font-bold text-lg md:text-xl text-primary hover:opacity-90 transition-opacity"
                    >
                        <span className="bg-yellow-400 text-black px-2 py-0.5 rounded-md">
                            IMDb
                        </span>
                        <span className="hidden sm:inline-block">Clone</span>
                    </Link>
                    <div
                        className="hidden md:flex items-center text-sm font-medium text-muted-foreground gap-2"
                        ref={navMenuRef}
                    >
                        {navMenus.map((menu) => (
                            <div key={menu.label} className="relative">
                                <button
                                    type="button"
                                    onClick={() =>
                                        setOpenNavMenu(
                                            openNavMenu === menu.label ? null : menu.label,
                                        )
                                    }
                                    className="flex items-center gap-1 rounded-md px-3 py-2 transition-colors hover:bg-secondary hover:text-foreground"
                                >
                                    {menu.label}
                                    <ChevronDown className="h-3.5 w-3.5" />
                                </button>
                                {openNavMenu === menu.label && (
                                    <div className="absolute left-0 top-full z-50 mt-2 w-56 overflow-hidden rounded-md border border-border bg-popover shadow-lg">
                                        <div className="p-1">
                                            {menu.items.map((item) => (
                                                <Link
                                                    key={item.to}
                                                    to={item.to}
                                                    onClick={() => setOpenNavMenu(null)}
                                                    className="block rounded-sm px-3 py-2 text-sm text-popover-foreground transition-colors hover:bg-secondary"
                                                >
                                                    {item.label}
                                                </Link>
                                            ))}
                                        </div>
                                    </div>
                                )}
                            </div>
                        ))}
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
                                    <Button
                                        size="sm"
                                        className="hidden sm:flex bg-foreground text-background"
                                    >
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
                        <Button
                            variant="ghost"
                            size="icon"
                            className="md:hidden"
                            onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                        >
                            <Menu className="h-5 w-5" />
                        </Button>
                    </div>
                </div>
            </div>
            {isMobileMenuOpen && (
                <div className="border-t border-border bg-background px-4 py-4 md:hidden">
                    <div className="flex flex-col gap-4">
                        {navMenus.map((menu) => (
                            <div key={menu.label}>
                                <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-muted-foreground">
                                    {menu.label}
                                </p>
                                <div className="flex flex-col gap-1">
                                    {menu.items.map((item) => (
                                        <Link
                                            key={item.to}
                                            to={item.to}
                                            onClick={() => setIsMobileMenuOpen(false)}
                                            className="rounded-md px-3 py-2 text-sm text-foreground transition hover:bg-secondary"
                                        >
                                            {item.label}
                                        </Link>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </nav>
    );
}
