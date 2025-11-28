import axios from "axios";

const BASE_URL = `/api`

const api = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true
})

export const duelsApi = {
    connectDuel: () => api.post('/v1/duels/connect'),
    createDuel: () => api.post('/v1/duels'),
    disconnect: (duelId) => api.delete(`/v1/duels/${duelId}/disconnect`)
}