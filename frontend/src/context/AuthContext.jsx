import { createContext, useContext, useState, useEffect } from "react";
import api from "../lib/api";

const AuthContext = createContext();

export const useAuth = () => {
    return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem("token") || null);
    const [loading, setLoading] = useState(true); // Optional: If you want to fetch user details on load

    useEffect(() => {
        if (token && !user) {
            try {
                // Decode the JWT payload (the middle part of the token)
                const payload = JSON.parse(atob(token.split(".")[1]));
                // The backend JwtHelper puts the username in the 'sub' (subject) claim
                if (payload.sub) {
                    setUser({ username: payload.sub });
                }
            } catch (err) {
                console.error("Failed to decode token", err);
                // Token is malformed; clean up
                setToken(null);
                localStorage.removeItem("token");
            }
        }
        setLoading(false);
    }, [token, user]);

    const login = async (username, password) => {
        const response = await api.post("/api/user/login", { username, password });
        const { token } = response.data;
        setToken(token);
        localStorage.setItem("token", token);

        // In a real app, you might also fetch user details here or decode the JWT
        setUser({ username });
        return response.data;
    };

    const register = async (userData) => {
        const response = await api.post("/api/user/register", userData);
        return response.data;
    };

    const logout = async () => {
        try {
            await api.get("/api/user/logout");
        } catch (err) {
            console.error("Logout error", err);
        } finally {
            setToken(null);
            setUser(null);
            localStorage.removeItem("token");
        }
    };

    const value = {
        user,
        token,
        loading,
        login,
        register,
        logout,
    };

    return <AuthContext.Provider value={value}>{!loading && children}</AuthContext.Provider>;
};
