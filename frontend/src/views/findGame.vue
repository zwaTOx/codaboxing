<template>
    <div class="PageWrapper">
        <div class="mainPage">
            <h1 id="match-status" class="title">Идет поиск игры...</h1>
            
        </div>
    </div>
</template>



<script>
import { useDuelStore } from '@/stores/duels';

export default {
    name: 'main',
    methods: {
        async connectDuel() {
            try {
                const statusElement = document.querySelector("#match-status");
                const response = await useDuelStore().connectDuel()

                if (response.success) {
                    console.log('дуэль найдена', response.data.id)
                    statusElement.innerHTML = "Матч начнется через 3...";

                    setTimeout(() => {
                        statusElement.innerHTML = "Матч начнется через 2...";
                        setTimeout(() => {
                            statusElement.innerHTML = "Матч начнется через 1...";
                            setTimeout(() => {
                                this.$router.push({ path: `game/${response.data.id}`, params: { id: response.data.id },  })
                            }, 1000);
                        }, 1000);
                    }, 1000);
                }
            } catch(error) {

            }
        }
    },
    async mounted() {
        await this.connectDuel()
    }
}
</script>