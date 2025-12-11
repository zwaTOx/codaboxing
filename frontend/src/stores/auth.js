import { defineStore } from "pinia";
import router from '@/router'
import { ref, computed } from "vue"
import { saveToCache } from "@/cache/cache";
import { loadFromCache } from "@/cache/cache";
import { authApi } from "@/api/auth";

export const useAuthStore = defineStore('auth', () => {
    const isAuth = ref(false);
    const isAuthenticated = computed(() => isAuth.value);

    const login = async (userData) => {
        try {
            const response = await authApi.login(userData)
            console.log('Login successful:', response.data);
            saveToCache('token', response.data.accessToken);
            saveToCache('refresh_token', response.data.refreshToken);
            saveToCache('is_auth', true);
            isAuth.value = true;
            return { success: true, data: response.data }
        } catch (error) {
            // if (error.response.status === 401) {
            //     await refreshToken()
            // }
            return { success: false, error: error}
        }
    }

    const register = async (userData) => {
        try {
            const response = await authApi.register(userData)
            return { success: true, data: response.data}
        } catch (error) {
            return { success: false, error: error }
        }
    }

    const refreshToken = async () => {
        try {
            const response = await authApi.refreshToken()
            console.log('Token refreshed successfully');
            return { success: true, data: response.data }
        } catch (error) {
            console.log('Error refreshing token:', error)
        }
    }

    const initialize = async () => {
        if (loadFromCache('is_auth')) {
            isAuth.value = true;
            console.log('User is authenticated');
        } else {
            isAuth.value = false;
            console.log('User is not authenticated', loadFromCache('is_auth'));
        }
    }

    return {
        isAuth: isAuthenticated,

        login,
        register,
        refreshToken,
        initialize
    }
})