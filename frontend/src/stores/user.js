import { defineStore } from "pinia";
import { ref, computed } from "vue"; // Импортируем ref и computed
import { userApi } from "@/api/user";
import { useAuthStore } from "./auth";

export const useUserStore = defineStore('user', () => {
    const user = ref(null);

    const getProfile = async () => {
        try {
            const response = await userApi.profileInfo();
            user.value = response.data;
            // console.log('user data', user.value.nickname)
            return { success: true, data: response.data };
        } catch (error) {
            if (error.response?.status === 401) {
                console.log('ошибка авторизации, попытка обновления токена...');
                const refresh = await useAuthStore().refreshToken();
                if (refresh.success) {
                    console.log('токен успешно обновлен');
                    return await getProfile();
                }
            }
            return { success: false, error: error };
        }
    }

    const changeUsername = async (newData) => {
        try {
            const response = await userApi.changeUsername(newData)
            return { success: true, data: response.data }
        } catch (error) {
            return { success: false, error: error}
        }
    }

    const changePassword = async (passwordData) => {
        try {
            const response = await userApi.changePassword(passwordData)
            return { success: true, data: response.data }
        } catch (error) {
            return { success: false, error: error}
        }
    }

    return {
        user,
        getProfile,
        changeUsername,
        changePassword
    };
});
