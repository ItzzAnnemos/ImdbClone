import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Home } from "./pages/Home";
import { MediaDetails } from "./pages/MediaDetails";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { Watchlist } from "./pages/Watchlist";
import { MediaReviews } from "./pages/MediaReviews";
import { MediaChartPage } from "./pages/MediaChartPage";
import { MediaGenrePage } from "./pages/MediaGenrePage";
import { CelebChartPage } from "./pages/CelebChartPage";
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
                        <Route
                            path="/movies/top-250"
                            element={<MediaChartPage mediaType="movie" chartType="top-250" />}
                        />
                        <Route
                            path="/movies/most-popular"
                            element={<MediaChartPage mediaType="movie" chartType="most-popular" />}
                        />
                        <Route
                            path="/movies/genre"
                            element={<MediaGenrePage mediaType="movie" />}
                        />
                        <Route
                            path="/movies/genre/:genreName"
                            element={<MediaGenrePage mediaType="movie" />}
                        />
                        <Route
                            path="/tv/top-250"
                            element={<MediaChartPage mediaType="tv" chartType="top-250" />}
                        />
                        <Route
                            path="/tv/most-popular"
                            element={<MediaChartPage mediaType="tv" chartType="most-popular" />}
                        />
                        <Route path="/tv/genre" element={<MediaGenrePage mediaType="tv" />} />
                        <Route
                            path="/tv/genre/:genreName"
                            element={<MediaGenrePage mediaType="tv" />}
                        />
                        <Route
                            path="/celebs/born-today"
                            element={<CelebChartPage chartType="born-today" />}
                        />
                        <Route
                            path="/celebs/most-popular"
                            element={<CelebChartPage chartType="most-popular" />}
                        />
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
