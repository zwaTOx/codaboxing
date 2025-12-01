import axios from "axios";
import { url } from "./url";
import { loadFromCache } from "@/cache/cache";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Authorization': `Bearer ${loadFromCache('token')}`,
        'Content-Type': 'application/json'
    },
    withCredentials: true
})

export const userApi = {
    profileInfo: () => api.get('/v1/users/me')
}