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

api.interceptors.response.use(
    (response) => response,
    (error) => {
        const status = error.response ? error.response.status : undefined;
        if ((status === 401 || status === 403) && localStorage.getItem("token")) {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.dispatchEvent(new Event("auth:logout"));
        }

        return Promise.reject(error);
    },
);

export default api;
