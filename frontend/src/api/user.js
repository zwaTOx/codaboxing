import axios from "axios";
import { url } from "./url";

const API_BASE_URL = `http://${url.path}:${url.port}/api`

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
})

export const userApi = {
    profileInfo: () => api.get('/v1/users/me', { withCredentials: true })
}