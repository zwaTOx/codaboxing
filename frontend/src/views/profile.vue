<template>
    <div class="profile">
        <div class="profile__descr">
            <div class="profile__descr--icon">
                {{ profile.initials }}
            </div>
            <div class="profile__descr--info">
                <div class="profile__descr--info--name">
                    {{ profile.name }}
                </div>
                <div class="profile__descr--info--lvl">
                    <div class="profile__descr--info--lvl--text">
                        УР {{ profile.lvl }}
                    </div>
                    <div class="profile__descr--info--lvl--bar">
                        <div class="profile__descr--info--lvl--bar--currentbar" :style="{width: `calc(${profile.progress*100/profile.next_lvl}%)`}">
                            
                        </div>
                        <div class="currentbar__text">
                            {{ profile.progress }} / {{ profile.next_lvl }}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="profile__stats profile_comp">
            <h1>Статистика</h1>
            <div class="stats_cards">
                <div class="stats_card">
                    <div class="stats_card__descr">
                        Сыграно игр
                    </div>
                    <div class="stats_card__name">
                        БОКС
                    </div>
                    <div class="stats_card__value">
                        {{ stats.totalGames }}
                    </div>
                    <!-- <div class="stats_card__info">
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${count_stats})`}">
                            <div class="stats_card__info--side--value" >
                                {{ stats. }}
                            </div>
                            <div class="stats_card__info--side--sign">
                                KOs
                            </div>
                        </div>
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${count_stats})`}">
                            <div class="stats_card__info--side--value">
                                19945
                            </div>
                            <div class="stats_card__info--side--sign">
                                МАКС. ОЧКОВ
                            </div>
                        </div>
                    </div> -->
                </div>
                <div class="stats_card">
                    <div class="stats_card__descr">
                        Текущий
                    </div>
                    <div class="stats_card__name">
                        РЕЙТИНГ
                    </div>
                    <div class="stats_card__value">
                        {{ stats.rating }}
                    </div>
                    <div class="stats_card__info">
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${1})`}">
                            <div class="stats_card__info--side--value">
                                {{ stats.maxRating }}
                            </div>
                            <div class="stats_card__info--side--sign">
                                ВЫСШИЙ
                            </div>
                        </div>
                    </div>
                </div>
                <div class="stats_card">
                    <div class="stats_card__descr">
                        Текущий
                    </div>
                    <div class="stats_card__name">
                        ПРОЦЕНТ ПОБЕД
                    </div>
                    <div class="stats_card__value">
                        {{ stats.winRate }}
                    </div>
                    <div class="stats_card__info">
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${count_stats})`}">
                            <div class="stats_card__info--side--value">
                                {{ stats.wins }}
                            </div>
                            <div class="stats_card__info--side--sign">
                                ПОБЕД
                            </div>
                        </div>
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${count_stats})`}">
                            <div class="stats_card__info--side--value">
                                {{ stats.losses }}
                            </div>
                            <div class="stats_card__info--side--sign">
                                ПОРАЖЕНИЯ
                            </div>
                        </div>
                    </div>
                </div>
                <div class="stats_card">
                    <div class="stats_card__descr">
                        Текущая
                    </div>
                    <div class="stats_card__name">
                        СЕРИЯ ПОБЕД
                    </div>
                    <div class="stats_card__value">
                        {{ stats.currentWinStreak }}
                    </div>
                    <div class="stats_card__info">
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${1})`}">
                            <div class="stats_card__info--side--value">
                                {{ stats.maxWinStreak }}
                            </div>
                            <div class="stats_card__info--side--sign">
                                ЛУЧШАЯ СЕРИЯ
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
        </div>
        <div class="profile__history profile_comp">
            <h1>История</h1>
            <history-component v-for="game in history" :key="game.id" :data="game" />
        </div>
    </div>
</template>

<script>
import historyComponent from '@/components/historyComponent.vue';
import { userStore } from '@/stores/user'

export default {
    components: { historyComponent },
    data() {
        return {
            // games: 4,
            // ko: 1,
            // max_points: 19945,

            // rating: 240,
            // high: 1,
            // lower: 1,

            // procent_win: 33.3,

            count_stats: 2,
            profile: {
                initials: "",
                name: 'Константин Денисов',
                lvl: 2,
                progress: 24,
                next_lvl: 150,
            },
            stats: { },
            history: [
                {
                    id: 1,
                    type: 'win',
                    opponent: 'Яков Гречнев',
                    opponent_rank: 'Новичок I',
                    rating: 240,
                    progress: '+12',
                },
                {
                    id: 2,
                    type: 'lose',
                    opponent: 'Эрик Бочектуев',
                    opponent_rank: 'Новичок II',
                    rating: 252,
                    progress: '-12',
                },
                {
                    id: 3,
                    type: 'lose',
                    opponent: 'Илья Найдуков',
                    opponent_rank: 'Новичок II',
                    rating: 252,
                    progress: '-9',
                }
            ]
        }
    },
    methods: {
        getInitials(value) {
            return value.split(' ').map(name => name[0]).join('')
        },
        async getProfile() {
            try {
                const response = await userStore().getProfile()
                if (response.success) {
                    console.log('Данные профиля получены',response.data)
                    this.stats = response.data;
                } else {
                    console.log('Error fetching profile:', response.error)
                }
            } catch (error) {
                console.log('Error fetching profile:', error)
            }
        },
    },
    async mounted() {
        await this.getProfile()
        this.profile.initials = this.getInitials(this.profile.name)
        for (let i = 0; i < this.history.length; i++) {
            this.history[i].opponent_initials = this.getInitials(this.history[i].opponent)
        }
        console.log(this.history)
    }
}
</script>