<template>
    <div class="PageWrapper">
        <div class="mainPage">
            <h1 class="title">Добро пожаловать</h1>
            <div class="button_group">
                <button class="border-button btn" @click="start">Начать игру</button>
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

<style scoped>
.title {
    margin-bottom: 14px;
}
.btn {
    background-color: var(--dark);
    width: 100%;
}
</style>