import axios from "axios";
import { url } from "./url";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
console.log(API_BASE_URL, 'new comments');
console.log(API_BASE_URL, 'new comments');
console.log(API_BASE_URL, 'new comments');

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true
})

export const authApi = {
    login: (userData) => api.post('/v1/auth/login', userData),
    register: (userData) => api.post('/v1/auth/signup', userData),
    refreshToken: () => api.post('/v1/auth/refresh')
}