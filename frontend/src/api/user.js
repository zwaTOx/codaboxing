import axios from "axios";
import { url } from "./url";
import { loadFromCache } from "@/cache/cache";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const api = axios.create({
    baseURL: "/api",
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true
})

api.interceptors.request.use(
    (config) => {
        const token = loadFromCache('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);


export const userApi = {
    profileInfo: () => api.get('/v1/users/me'),
    
    changeUsername: (newData) => api.patch('/v1/users/me', newData),
    changePassword: (passwordData) => api.put('/v1/users/me/password', passwordData),

    getHistory: () => api.get('/v1/users/me/duels'),
}