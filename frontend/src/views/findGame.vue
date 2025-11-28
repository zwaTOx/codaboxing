<template>
    <div class="PageWrapper">
        <div class="mainPage">
            <h1 class="title">Идет поиск игры...</h1>
            
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
                const response = await useDuelStore().connectDuel()

                if (response.success) {
                    console.log('дуэль найдена')
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