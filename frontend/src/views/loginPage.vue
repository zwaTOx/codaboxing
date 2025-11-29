<template>
    <div class="PageWrapper">
        <div class="authPage">
            <h1 class="authPage__title">Вход в аккаунт</h1> 
            <CitingsComponent></CitingsComponent>
            <form class="authPage__form" @submit.prevent="handleLogin">
                <div class="authPage__input-container"
                @click="focus('email')">
                    <div id="email-cover" class="authPage__input-cover">E-mail</div>
                    <input id="email-field" v-model="email" type="text" required placeholder="E-mail"
                    class="authPage__form--input" />
                </div>
                <div class="authPage__input-container"
                @click="focus('password')">
                    <div id="password-cover" class="authPage__input-cover">Пароль</div>
                    <input id="password-field" v-model="password" type="password" required placeholder="Пароль"
                    class="authPage__form--input" />
                </div>
                <button type="submit" class="border-button authPage__form--button">Войти</button>
                <div class="authPage__registration">Нет аккаунта? <u @click="toRegistration">Зарегистрируйтесь</u></div>
            </form>
        </div>
    </div>
</template>

<script setup>
import CitingsComponent from '@/components/citingsComponent.vue';
</script>

<script>
import { authStore } from '@/stores/auth'

export default {
    data() {
        return {
            email: '',
            password: '',
        }
    },
    methods: {
        async handleLogin() {
            const feedbackElement = document.querySelector('#feedback');

            const userData = {
                email: this.email,
                password: this.password
            }
            const auth = authStore()
            const response = await auth.login(userData)
            if (response.success) {
                console.log('Cookies after login:', document.cookie)
                feedbackElement.classList.remove('feedback-error');
                feedbackElement.classList.add('feedback-success');
                feedbackElement.innerHTML = "Добро пожаловать!";

                setTimeout(() => {
                    console.log('Cookies after login:', document.cookie)
                    this.$router.push('/profile')
                }, 2000);
                
            } else {
                console.log('Login error:', response.error)
                feedbackElement.classList.remove('feedback-success');
                feedbackElement.classList.add('feedback-error');
                feedbackElement.innerHTML = response.error.response.data.error;
            }
        },
        focus(type) {
            const cover = document.querySelector(`#${type}-cover`);
            const field = document.querySelector(`#${type}-field`);

            if (!cover.classList.contains('active-cover'))
                cover.classList.add('active-cover')

            if (!field.classList.contains('active-field')) {
                field.classList.add('active-field')
                field.focus()
            }
        },
        toRegistration() {
            this.$router.push('register');
        },
    }
}
</script>