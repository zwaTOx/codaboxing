<template>
    <div class="PageWrapper">
        <div class="mainPage">
            <h1 class="title">Добро пожаловать</h1>
            <div class="button_group">
                <button @click="start">Начать игру</button>
            </div>
        </div>
    </div>
</template>



<script>
export default {
    name: 'main',
    methods: {
        start() {
            this.$router.push('/find_game')
        }
    }
}
</script>