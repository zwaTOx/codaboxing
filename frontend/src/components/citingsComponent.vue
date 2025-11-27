<template>
    <div id="feedback" class="authPage__status">{{ currentCiting }}</div>
</template>

<script>
import { getRandomNumber } from '@/composables/random';

export default {
    data() {
        return {
            citings: [],
        }
    },
    async mounted() {
        try {
            const response = await fetch('src/assets/json/citings.json')
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            this.citings = await response.json();
        } catch {
            console.error(error);
        }
    },
    computed: {
        currentCiting() {
            return this.getRandomCiting();
        }
    },
    methods: {
        getRandomCiting() {
            if (!this.citings || this.citings.length === 0) {
                return 'Загрузка цитат...';
            }
            const index = getRandomNumber(0, this.citings.length - 1);
            const citing = this.citings[index];
            return `${citing.citing} — ${citing.author} (${citing.year})`;
        },
    }
}
</script>