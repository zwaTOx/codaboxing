<template>
    <header class="headerWrapper">
        <div class="header">
            <div class="header__logo" @click="toMainPage">
                <div class="header__logo--title" :class="{nomode: !mode}">
                    {{ mode === '' ? 'Codegonks' : `CODE: ` }}
                </div>
                <div class="header__logo--mode">{{ `${mode}` }}</div>
            </div>
            <div v-if="isAuthenticated"
            class="header__profile">
                <div class="header__profile--icon icon">{{ initials }}</div>
                <div class="header__profile--info">
                    <div class="header__profile--info--name">{{ userData.nickname }}</div>
                    <div class="header__profile--info--lvl">{{ `${lvl} уровень (${exp})` }}</div>
                </div>
            </div>
            <div v-if="isAuthenticated" class="logout border-button" @click="logout()">Выйти</div>
        </div>
    </header>
</template>

<script setup>
import router from '@/router';
import { computed, onMounted, ref } from 'vue';
import { getInitials } from '@/composables/getInitials';

import { useAuthStore } from '@/stores/auth';
import { useUserStore } from '@/stores/user';

// Stores
const userStore = useUserStore();
const authStore = useAuthStore();

// Mock Data
const mode = ref("BOXING");
const lvl = ref(2);
const exp = ref(240);
const userData = ref({});

// Computed
const isAuthenticated = computed(() => authStore.isAuth);
const initials = computed(() => {
    return getInitials(userData.value.nickname || '');
});

// Methods
const toMainPage = () => {
    if (isAuthenticated) {
        router.push('/main')
    } else {
        alert('user is not auth')
    };
};
const logout = () => {
    localStorage.clear()
    authStore.initialize()
    router.push('/login')

    updateUserData();
};
const updateUserData = async () => {
    if (!isAuthenticated) userData.value = {};
    else {
        try {
            const response = await userStore.getProfile();
            if (response.success) {
                console.log('Данные профиля получены',response.data);
                userData.value = response.data;
            } else {
                console.log('Error fetching profile:', response.error);
            }
        } catch (error) {
            console.log('Error fetching profile:', error);
        }
    }
};

onMounted(async () => {
    updateUserData();
});
</script>