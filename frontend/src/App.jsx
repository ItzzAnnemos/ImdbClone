import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Home } from "./pages/Home";
import { MediaDetails } from "./pages/MediaDetails";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { Watchlist } from "./pages/Watchlist";
import { AuthProvider } from "./context/AuthContext";
import { WatchlistProvider } from "./context/WatchlistContext";

function App() {
    return (
        <AuthProvider>
            <WatchlistProvider>
                <Router>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/movie/:id" element={<MediaDetails />} />
                        <Route path="/tv/:id" element={<MediaDetails />} />
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
