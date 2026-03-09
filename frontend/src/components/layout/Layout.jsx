import { Navbar } from "./Navbar";
import { Footer } from "./Footer";

export function Layout({ children }) {
    return (
        <div className="min-h-screen bg-background font-sans antialiased text-foreground flex flex-col">
            <Navbar />
            <main className="flex-1 container mx-auto px-4 py-6 md:px-8">{children}</main>
            <Footer />
        </div>
    );
}
