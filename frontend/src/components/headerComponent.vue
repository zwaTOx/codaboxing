<template>
    <header class="headerWrapper">
        <div class="header">
            <div class="header__logo" @click="toMainPage">
                <div class="header__logo--title" :class="{nomode: !mode}">
                    {{ mode === '' ? 'Codegonks' : `CODE: ` }}
                </div>
                <div class="header__logo--mode">{{ `${mode}` }}</div>
            </div>
            <div class="header__profile">
                <div class="header__profile--icon icon">{{ initials }}</div>
                <div class="header__profile--info">
                    <div class="header__profile--info--name">Константин Денисов</div>
                    <div class="header__profile--info--lvl">{{ `${lvl} уровень (${exp})` }}</div>
                </div>
            </div>
            <div v-if="isAuthenticated" class="logout border-button" @click="logout()">Выйти</div>
        </div>
    </header>
</template>

<script>
import router from '@/router';
import { authStore } from '@/stores/auth';
export default {
    data() {
        return {
            mode: '',
            lvl: 2,
            exp: 240,
            name: 'Константин Денисов',
            initials: '',
        }
    },
    computed: {
        isAuthenticated() {
            return authStore().isAuth
        }
    },
    methods: {
        getInitials() {
            this.initials = this.name.split(' ').map(name => name[0]).join('')
        },
        toMainPage() {
            if (this.isAuthenticated) {
                this.$router.push('/main')
            } else {
                alert('user is not auth')
            }
        } ,
        logout() {
            localStorage.clear()
            authStore().initialize()
            this.$router.push('/login')
        }
    },
    mounted() {
        this.getInitials()
    }
}
</script>