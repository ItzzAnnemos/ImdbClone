import axios from "axios";

// Create an Axios instance
const api = axios.create({
    baseURL: "http://localhost:8080", // Replace with your backend URL
});

// Add a request interceptor to add the JWT token to the Authorization header
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

export default api;
