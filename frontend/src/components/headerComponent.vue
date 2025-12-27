<template>
    <header class="headerWrapper">
        <div class="header">
            <div class="header__logo" @click="toMainPage">
                <div class="header__logo--title" :class="{nomode: !mode}">
                    {{ mode === '' ? 'CodeBoxing' : `CODE: ` }}
                </div>
                <div class="header__logo--mode">{{ `${mode}` }}</div>
            </div>
            <div v-if="isAuthenticated"
            class="header__profile">
                <div class="header__profile--icon icon" @click="toProfile">{{ initials }}</div>
                <div class="header__profile--info">
                    <div class="header__profile--info--name">{{ userName }}</div>
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
const userName = ref('');
const mode = ref("boxing");
const lvl = ref(2);
const exp = ref(240);

// Computed
const isAuthenticated = computed(() => authStore.isAuth);
const initials = computed(() => {
    return getInitials(userName.value?.nickname || '');
});

// Methods
const toMainPage = () => {
    if (isAuthenticated) {
        router.push('/main')
    } else {
        alert('user is not auth')
    };
};
const toProfile = () => {
    if (isAuthenticated) {
        router.push('/me')
    } else {
        alert('user is not auth')
    };
};
const logout = () => {
    localStorage.clear()
    authStore.initialize()
    router.push('/login')
};

onMounted(async () => {
    userName.value = (await userStore.getProfile()).data.nickname;
});
</script>