import axios from "axios";
import { url } from "./url";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true
})

export const userApi = {
    profileInfo: () => api.get('/v1/users/me')
}