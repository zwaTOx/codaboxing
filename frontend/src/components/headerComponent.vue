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
            initials: ''
        }
    },
    methods: {
        getInitials() {
            this.initials = this.name.split(' ').map(name => name[0]).join('')
        },
        toMainPage() {
            if (authStore().isAuth) {
                this.$router.push('/main')
            }
        }
    },
    mounted() {
        this.getInitials()
    }
}
</script>