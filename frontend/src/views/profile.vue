<template>
    <div class="profile">
        <div class="profile__descr">
            <div class="profile__descr--icon">
                {{ profile.initials }}
            </div>
            <div class="profile__descr--info">
                <div class="profile__descr--info--name">
                    {{ profile.nickname }}
                </div>
                <div class="profile__descr--info--lvl">
                    <div class="profile__descr--info--lvl--text">
                        УР {{ mock_data.lvl }}
                    </div>
                    <div class="profile__descr--info--lvl--bar">
                        <div class="profile__descr--info--lvl--bar--currentbar" :style="{width: `calc(${mock_data.progress*100/mock_data.next_lvl}%)`}">
                            
                        </div>
                        <div class="currentbar__text">
                            {{ mock_data.progress }} / {{ mock_data.next_lvl }}
                        </div>
                    </div>
                </div>
                <div class="profile__descr--info--edit" @click="openModal('nick')" style="display: flex; gap: 12px">
                    <div class="profile__descr--info--edit--btn" style="
                    display: flex;
                    gap: 10px;
                    align-items: center;
                    padding: 5px 10px;
                    border-radius: 24px;
                    border: 2px solid #fff;
                    width: fit-content;
                    cursor: pointer;
                    ">
                        <img src="../assets/icons/edit.svg" alt="">
                        <div class="profile__descr--info--edit--text">Изменить личные данные</div>
                    </div>

                    <div class="profile__descr--info--edit--btn" @click="openModal()" style="
                    display: flex;
                    gap: 10px;
                    align-items: center;
                    padding: 5px 10px;
                    border-radius: 24px;
                    border: 2px solid #fff;
                    width: fit-content;
                    cursor: pointer;
                    ">
                        <img src="../assets/icons/edit.svg" alt="">
                        <div class="profile__descr--info--edit--text">Изменить пароль</div>
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
                        {{ profile.totalGames }}
                    </div>
                    <!-- <div class="stats_card__info">
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${count_stats})`}">
                            <div class="stats_card__info--side--value" >
                                {{ profile. }}
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
                        {{ profile.rating }}
                    </div>
                    <div class="stats_card__info">
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${1})`}">
                            <div class="stats_card__info--side--value">
                                {{ profile.maxRating }}
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
                        {{ profile.winRate }}
                    </div>
                    <div class="stats_card__info">
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${count_stats})`}">
                            <div class="stats_card__info--side--value">
                                {{ profile.wins }}
                            </div>
                            <div class="stats_card__info--side--sign">
                                ПОБЕД
                            </div>
                        </div>
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${count_stats})`}">
                            <div class="stats_card__info--side--value">
                                {{ profile.losses }}
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
                        {{ profile.currentWinStreak }}
                    </div>
                    <div class="stats_card__info">
                        <div class="stats_card__info--side" :style="{width: `calc(100%/${1})`}">
                            <div class="stats_card__info--side--value">
                                {{ profile.maxWinStreak }}
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
        <profileEdit v-if="nicknameStatus" @update="updateData" @close="closeModal" />
        <changePassword v-show="passwordStatus" @close="closeModal" />
    </div>
    
</template>

<script>
import historyComponent from '@/components/historyComponent.vue';
import profileEdit from '@/components/profileEdit.vue';
import changePassword from '@/components/changePassword.vue';
import { getInitials } from '@/composables/getInitials';
import { useUserStore } from '@/stores/user'

export default {
    components: { profileEdit, changePassword, historyComponent },
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
            mock_data: {
                lvl: 2,
                progress: 24,
                next_lvl: 150,
            },
            profile: { },
            history1: [
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
            ],
            history: [],

            nicknameStatus: false,
            passwordStatus: false
        }
    },
    methods: {
        openModal(value) {
            if (value === 'nick') {
                this.nicknameStatus = true
            } else {
                this.passwordStatus = true
            }
        },
        closeModal() {
            this.nicknameStatus = false,
            this.passwordStatus = false
        },
        async updateData() {
            await this.getProfile()
            this.closeModal()
        },
        async getProfile() {
            try {
                const response = await useUserStore().getProfile()
                if (response.success) {
                    console.log('Данные профиля получены',response.data)
                    this.profile = response.data;
                    this.profile.initials = getInitials(this.profile.nickname)
                    await this.getHistory();
                } else {
                    console.log('Error fetching profile:', response.error)
                }
            } catch (error) {
                console.log('Error fetching profile:', error)
            }
        },
        async getHistory() {
            try {
                const response = await useUserStore().getHistory()
                if (response.success) {
                    console.log('История получена',response.data)
                    this.history = response.data.reverse();
                } else {
                    console.log('Error fetching history:', response.error)
                }
            } catch (error) {
                console.log('Error fetching history:', error)
            }
        }
    },
    async mounted() {
        await this.getProfile();
        this.profile.initials = getInitials(this.profile.nickname)
        for (let i = 0; i < this.history.length; i++) {
            this.history[i].opponent_initials = getInitials(this.history[i].opponent.nickname)
        }
        console.log(this.history)
    }
}
</script>