<template>
    <div class="PageWrapper">
        <div class="authPage">
            <h1 class="authPage__title">Вход в аккаунт</h1>
            <form class="authPage__form" @submit.prevent="handleLogin">
                <input v-model="email" type="text" placeholder="Email" required class="authPage__form--input" />
                <input v-model="password" type="password" placeholder="Пароль" required class="authPage__form--input" />
                <button type="submit" class="authPage__form--button">Войти</button>
            </form>
        </div>
    </div>
</template>

<script>
import { authStore } from '@/stores/auth'

export default {
    data() {
        return {
            email: '',
            password: ''
        }
    },
    methods: {
        async handleLogin() {
            const userData = {
                email: this.email,
                password: this.password
            }
            const auth = authStore()
            const response = await auth.login(userData)
            if (response.success) {
                console.log('Cookies after login:', document.cookie)

                setTimeout(() => {
                    console.log('Cookies after login:', document.cookie)
                    this.$router.push('/profile')
                }, 5000);
                
            } else {
                console.log('Login error:', response.error)
            }
        }
    }
}
</script>