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
        // If we have a token but no user details, we might want to fetch the user profile here.
        // For now, we'll just set loading to false.
        setLoading(false);
    }, [token]);

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

    const logout = () => {
        setToken(null);
        setUser(null);
        localStorage.removeItem("token");
        api.get("/api/user/logout").catch((err) => console.error("Logout error", err));
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
