import axios from "axios";
import { url } from "./url";

const API_BASE_URL = `/api`

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