import { loadFromCache } from "@/cache/cache";
import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;
console.log(loadFromCache('token'))

const api = axios.create({
    baseURL: "/api",
    headers: {
        'Authorization': `Bearer ${loadFromCache('token')}`,
        'Content-Type': 'application/json'
    },
    withCredentials: true
})

export const duelsApi = {
    // Managing Duel Session
    connectDuel: () => api.post('/v1/duels/connect'),
    createDuel: () => api.post('/v1/duels'),
    disconnect: (duelId) => api.delete(`/v1/duels/${duelId}/disconnect`),

    // Managing Tasks
    getDuel: (id) => api.get(`/v1/duels/${id}`),
    getProblems: (duelId) => api.get(`/v1/duels/${duelId}/problems`),
    submitSolution: (duelId, taskId, body) => api.post(`v1/duels/${duelId}/problems/${taskId}/submit`, body),
}