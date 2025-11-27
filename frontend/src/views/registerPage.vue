<template>
    <div class="PageWrapper">
        <div class="authPage">
            <h1 class="authPage__title">Регистрация</h1>
            <form class="authPage__form" @submit.prevent="handleRegister">
                <input v-model="form.email" type="text" placeholder="Email" required class="authPage__form--input" />
                <input v-model="form.password" type="password" placeholder="Пароль" required class="authPage__form--input" />
                <input v-model="form.verifyPassword" type="password" placeholder="Подтвердите пароль" required class="authPage__form--input" />
                <button type="submit" class="authPage__form--button">Зарегистрироваться</button>
            </form>
        </div>
    </div>
</template>

<script>
import router from '@/router';
import { authStore } from '@/stores/auth'

export default {
    name: 'register',
    data() {
        return {
            form: {
                email: '',
                password: '',
                verifyPassword: ''
            }
        }
    },
    methods: {
        async handleRegister() {
            try {
                const response = await authStore().register(this.form)
                if (response.success) {
                    router.push('/login')
                    console.log('Registration successful')
                } else {
                    alert('Ошибка регистрации')
                    console.log(response.error)
                }
            } catch (error) {
                console.log(error)
            }
        }
    },
    mounted() {
        localStorage.clear()    
    }
}
</script>