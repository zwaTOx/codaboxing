<template>
    <div class="historyCard" :class="data.resultType === 'WIN' ? 'win' : 'lose' ">
        <div class="historyCard__info">
            <div class="historyCard__info--type">{{ data.resultType === 'WIN' ? 'Победа' : 'Поражение' }}</div>
            <div class="historyCard__info--opponent">
                <div class="opponent__icon"> 
                    {{ data.opponent_initials }}
                </div>
                <div class="opponent__info">
                    <div class="opponent__info--name">{{ data.opponent.nickname }}</div>
                    <div class="opponent__info--rating">{{ `${data.opponent_rank} (${data.rating})` }}</div>
                </div>
            </div>
        </div>
        <div class="historyCard__rating">
            {{ data.resultType === 'WIN' ? '+' : '-' }}{{ data.gainedRating }}
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {

        }
    },
    props: {
        data: {
            type: Object,
            req: true
        }
    }
}
</script>