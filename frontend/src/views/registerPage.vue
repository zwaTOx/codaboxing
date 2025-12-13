<template>
    <div class="PageWrapper">
        <div class="authPage">
            <h1 class="authPage__title">Регистрация</h1>
            <CitingsComponent />
            <form class="authPage__form" @submit.prevent="handleRegister">
                <div class="authPage__input-container"
                @click="focus('email')">
                    <div id="email-cover" class="authPage__input-cover">E-mail</div>
                    <input id="email-field" v-model="form.email" type="text" placeholder="Email" required class="authPage__form--input" />
                </div>
                <div class="authPage__input-container"
                @click="focus('username')">
                    <div id="username-cover" class="authPage__input-cover">Username</div>
                    <input id="username-field" v-model="form.nickname" type="text" placeholder="Username" required class="authPage__form--input" />
                </div>
                <div class="authPage__input-container"
                @click="focus('password')">
                    <div id="password-cover" class="authPage__input-cover">Пароль</div>
                    <input id="password-field" v-model="form.password" type="password" placeholder="Пароль" required class="authPage__form--input" />
                </div>
                <div class="authPage__input-container"
                @click="focus('verify-password')">
                    <div id="verify-password-cover" class="authPage__input-cover">Повторите пароль</div>
                    <input id="verify-password-field" v-model="form.verifyPassword" type="password" placeholder="Подтвердите пароль" required class="authPage__form--input" />
                </div>
                <button type="submit" class="authPage__form--button border-button">Зарегистрироваться</button>
                <div class="authPage__registration">Уже зарегистрированы? <u @click="toLogin">Авторизуйтесь</u></div>
            </form>
        </div>
    </div>
</template>

<script setup>
import CitingsComponent from '@/components/citingsComponent.vue';
</script>

<script>
import router from '@/router';
import { useAuthStore } from '@/stores/auth'

export default {
    name: 'register',
    data() {
        return {
            form: {
                email: '',
                nickname: '',
                password: '',
                verifyPassword: ''
            }
        }
    },
    methods: {
        async handleRegister() {
            try {
                const feedbackElement = document.querySelector('#feedback');

                const response = await useAuthStore().register(this.form)
                if (response.success) {
                    feedbackElement.classList.remove('feedback-error');
                    feedbackElement.classList.add('feedback-success');
                    feedbackElement.innerHTML = `Привет, <b>${response.data.nickname}</b>! Теперь авторизуйся.`;
                    console.log(response);

                    setTimeout(() => {
                        router.push('/login');
                        console.log('Registration successful');
                    }, 2000);

                } else {
                    feedbackElement.classList.remove('feedback-success');
                    feedbackElement.classList.add('feedback-error');
                    feedbackElement.innerHTML = response.error.response.data.error;
                    console.log(response.error)
                }
            } catch (error) {
                console.log(error)
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
        toLogin() {
            router.push('login');
        },
    },
    mounted() {
        localStorage.clear()    
    }
}
</script>