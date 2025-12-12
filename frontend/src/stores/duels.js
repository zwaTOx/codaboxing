import { defineStore } from "pinia";
import { duelsApi } from "@/api/duels";

export const useDuelStore = defineStore('duels', () => {

    const getProblems = async (duelId) => {
        try {
            const response = await duelsApi.getProblems(duelId)
        } catch (error) {
            
        }
    }

    const submitSolution = async (duelId, problemId, body) => {
        try {
            const response = await duelsApi.submitSolution(duelId, problemId, body);
            console.log(response.json());
        } catch (error) {
            console.error(error);
        }
    }

    const connectDuel = async () => {
        try {
            const response = await duelsApi.connectDuel()
            return { success: true, data: response.data }
        } catch (error) {
            console.log('дуэль не найдена, попытка создать')
            if (error.response.status === 400) {
                const create = await createDuel()
                if (create.success) {
                    console.log('дуэль создана')
                    console.log(create.data)
                } else {
                    console.log(create.error) 
                }
            } else {
                return { success: false, error: error}
            }
            
        }
    }

    const createDuel = async () => {
        try {
            const response = await duelsApi.createDuel()
            return { success: true, data: response.data }
        } catch (error) {
            return { success: false, error: error}
        }
    }

    const disconnect = async (duelId) => {
        try {
            const response = await duelsApi.disconnect(duelId)
            return { success: true, data: response.data }
        } catch (error) {
            return { success: false, error: error}
        }
    }

    return {
        connectDuel,
        createDuel,
        disconnect,

        submitSolution,
    }
})