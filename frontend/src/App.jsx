import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Home } from "./pages/Home";
import { MediaDetails } from "./pages/MediaDetails";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { Watchlist } from "./pages/Watchlist";
import { MediaReviews } from "./pages/MediaReviews";
import { AuthProvider } from "./context/AuthContext";
import { WatchlistProvider } from "./context/WatchlistContext";

function App() {
    return (
        <AuthProvider>
            <WatchlistProvider>
                <Router>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/media/:id" element={<MediaDetails />} />
                        <Route path="/media/:id/reviews" element={<MediaReviews />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        <Route path="/watchlist" element={<Watchlist />} />
                    </Routes>
                </Router>
            </WatchlistProvider>
        </AuthProvider>
    );
}

export default App;
