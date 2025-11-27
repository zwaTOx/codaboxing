import { defineStore } from "pinia";
import router from '@/router'
import { saveToCache } from "@/cache/cache";
import { loadFromCache } from "@/cache/cache";
import { userApi } from "@/api/user";

export const userStore = defineStore('user', () => {
    
    const getProfile = async () => {
        try {
            const response = await userApi.profileInfo()
            return { success: true, data: response.data }
        } catch (error) {
            return { success: false, error: error }
        }
    }

    return{
        getProfile
    }
})