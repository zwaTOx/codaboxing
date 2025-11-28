import { defineStore } from "pinia";
import router from '@/router'
import { saveToCache } from "@/cache/cache";
import { loadFromCache } from "@/cache/cache";
import { userApi } from "@/api/user";
import { authStore } from "./auth";

export const userStore = defineStore('user', () => {
    
    const getProfile = async () => {
        try {
            const response = await userApi.profileInfo()
            return { success: true, data: response.data }
        } catch (error) {
            if (error.response.status === 401) {
                console.log('ошибка авторизации, попытка обновления токена...')
                const refresh = await authStore().refreshToken()
                if (refresh.success) {
                    console.log('токен успешно обновлен')
                }
            } else {
                return { success: false, error: error }
            }
            
        }
    }

    return{
        getProfile
    }
})