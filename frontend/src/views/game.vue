<template>
    <div class="game-cover-screen">
        <div class="result-title">ПОБЕДА !!!</div>
    </div>
    <div class="game__wrapper">
        <div class="game__container">
            <div class="game__problem-container">
                <div class="game__problem-header">
                    <div class="game__problem-heading-text">{{ currentProblemIndex + 1 }}. {{ currentProblem.title }}</div>
                    <div class="game__problem-difficulty"
                    :class="{
                        'easy': currentDifficulty === 'легко',
                        'medium': currentDifficulty === 'нормально так',
                        'hard': currentDifficulty === 'тяжко',
                    }">{{ currentDifficulty }}</div>
                    <button @click="onQuit" class="quit-button">Сдаться</button>
                </div>
                <div class="game__problem-description">
                    {{ currentProblem.description }}
                </div>
                <div class="game__problem-example"
                v-for="(example, idx) in currentProblem.examples">
                    <div class="title">Пример {{ idx + 1 }}:</div>
                    <div class="game__problem-example-block">
                        <span class="strong">Входные данные:</span> 
                            <div class="variable-example"
                            v-for="(key, value) in example.inputData">{{ value }} = {{ key }}</div>
                        <span class="strong">Выходные данные:</span> {{ example.expectedOutput }}<br>
                        <span class="strong">Пояснение:</span> {{ example.description || "Не имеется"}}
                    </div>
                </div>
            </div>
            <div class="game__double-container">
                <div class="game__code-container">
                    <div class="game__heading-text"><span style="color: var(--green); font-weight: 800;"><></span> Код</div>
                    <CodeInputBoxComponent ref="codeInputBox" />
                    <div class="button-container">
                        <button 
                            class="send-button" 
                            @click="onSubmit"
                            :disabled="isLoading"
                        >
                            <span v-if="isLoading" class="button-loading-spinner"></span>
                            <span v-else>Отправить результат</span>
                        </button>
                        <div v-if="isLoading" class="loading-text">Проверяем решение...</div>
                    </div>
                </div>
                <div 
                    class="game__result-container"
                    :class="{ 'highlight-container': shouldHighlightResults }"
                >
                    <div class="game__heading-text results">
                        <span style="color: var(--green); font-weight: 800;">✔</span> Результат
                        <div class="feedback-message">{{ selectedTestCase?.errorMessage }}</div>
                    </div>
                    <div v-if="areResultsEmpty" class="game__results">{{ currentResult }}</div>
                    <div v-else class="game__results">
                        <div class="test-case-btns">
                            <div 
                                v-for="(testcase, idx) in testCases" 
                                :key="idx"
                                class="test-case-btn"
                                :class="getTestCaseStatusClass(testcase.status)"
                                @click="selectTestCase(idx)"
                            >
                                <span class="test-case-icon">
                                    <span v-if="testcase.status === 'PASSED'">✓</span>
                                    <span v-else-if="testcase.status === 'FAILED'">✗</span>
                                    <span v-else>?</span>
                                </span>
                                Тест {{ idx + 1 }}
                            </div>
                        </div>
                        <div class="test-case-content"
                        :class="{ 
                            'failed': selectedTestCase.status === 'FAILED', 
                            'passed': selectedTestCase.status === 'PASSED', 
                            'error': selectedTestCase.status === 'ERROR', 
                        }">
                            <div class="title">Входные данные</div>
                            <div class="input-data data-line">{{ dictToVariables(selectedTestCase?.inputData) }}</div>
                            <div class="title">Ожидаемый вывод</div>
                            <div class="expected-output data-line">{{ selectedTestCase?.expectedOutput }}</div>
                            <div class="title">Ваш вывод</div>
                            <div class="actual-output data-line">{{ selectedTestCase?.actualOutput || 'null' }}</div>
                            <div class="title">Время исполнения</div>
                            <div class="execution-time data-line">{{ selectedTestCase?.executionTime }}s</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="game__animation-container">
                <div class="game__heading-text"><span style="color: var(--green); font-weight: 800;">✔</span> Бокс</div>
                <div class="game__bars">
                    <div class="profile__descr--info--lvl">
                        <div class="profile__descr--info--lvl--text">
                            {{ me?.nickname }} (вы)
                        </div>
                        <div class="profile__descr--info--lvl--bar">
                            <div class="profile__descr--info--lvl--bar--currentbar" :style="{width: `calc(${currentProblemIndex * 100 / problemAmount}%)`}">
                                
                            </div>
                            <div class="currentbar__text">
                                {{ currentProblemIndex }} / {{ problemAmount }}
                            </div>
                        </div>
                    </div>
                    <div class="profile__descr--info--lvl">
                        <div class="profile__descr--info--lvl--text">
                            {{ opponent?.nickname }} (вы)
                        </div>
                        <div class="profile__descr--info--lvl--bar">
                            <div class="profile__descr--info--lvl--bar--currentbar" :style="{width: `calc(${opponentsCurrentProblemIndex * 100 / problemAmount}%)`}">
                                
                            </div>
                            <div class="currentbar__text">
                                {{ opponentsCurrentProblemIndex }} / {{ problemAmount }}
                            </div>
                        </div>
                    </div>
                </div>
                <video 
                    v-if="showAction"
                    ref="animationVideo"
                    :src="animationSource"
                    autoplay
                    playsinline
                    muted
                    @ended="onActionEnd"
                    style="width: auto; height: 50%;">
                </video>
                <img 
                    v-else 
                    :src="animationSource" 
                    style="width: auto; height: 50%;">
                </img>
            </div>
        </div>
    </div>
</template>

<script setup>
import router from '@/router';
import { ref, onMounted, computed, watch, nextTick } from 'vue';

import { useDuelStore } from '@/stores/duels';
import { duelsApi } from '@/api/duels';
import CodeInputBoxComponent from '@/components/codeInputBoxComponent.vue';
import { dictToVariables } from '@/composables/dictToVariables';
import { useUserStore } from '@/stores/user';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { getUserID } from '@/composables/getUserID';

// Misc Variables
const props = defineProps({
    id: {
        type: Number,
        required: true,
    },
});
const codeInputBox = ref(null);
const duelStore = useDuelStore();
const userStore = useUserStore();
const field = document.querySelector('main');
const difficulties = {
    "EASY": "легко",
    "MEDIUM": "нормально так",
    "HARD": "тяжко"
}
const gameEnded = ref(false);
const stompClient = ref(null);
const userId = getUserID();

// Loading state
const isLoading = ref(false);
const shouldHighlightResults = ref(false);

// Animation
const showAction = ref(false);
const animations = {
    playerAttack: "/player_attack.mp4",
    enemyAttack: "/enemy_attack.mp4",
    idle: "/idle.gif"
}
const animationSource = ref(animations.idle);

// Problems
const problems = ref([]);
const currentProblemIndex = ref(0);
const opponentsCurrentProblemIndex = ref(0);
const currentProblem = ref({});
const areResultsEmpty = ref(true);

// Duel
const duel = ref(null);
const me = computed(() => duel.value?.participants.filter(user => user.userId === userId)[0]);
const opponent = computed(() => duel.value?.participants.filter(user => user.userId !== userId)[0]);
const problemAmount = computed(() => duel.value?.problemCount);

// Current Problem
const currentDifficulty = computed(() => {
    return difficulties[currentProblem.value.difficulty] || '';
});
const currentResult = ref("Тут будет выводится результат вашего сабмита =)");
const testCases = ref(null);
const summary = ref(null);
const selectedTestCase = ref(null);

// Watchers
watch(currentProblemIndex, (newIndex) => {
    currentProblem.value = problems.value[newIndex];
});

watch(currentProblem, (newProblem) => {
    let variables = "";
    let length = Object.entries(newProblem.inputDataTypes).length;
    let count = 0;
    Object.entries(newProblem.inputDataTypes).forEach(([key, value]) => {
        count += 1;
        variables += `${key}: ${value}`;
        if (count < length) variables += ', ';
    });
    codeInputBox.value.code = 
`def ${newProblem.funcName}(${variables}) -> ${newProblem.outputDataType}:
    pass`;
});

// WTF
field.classList.remove('main');
field.classList.add('new-main');

// Methods
const getTestCaseStatusClass = (status) => {
    switch(status) {
        case 'PASSED':
            return 'test-case-passed';
        case 'FAILED':
            return 'test-case-failed';
        case 'ERROR':
            return 'test-case-error';
        case 'TIMEOUT':
            return 'test-case-timeout';
        default:
            return 'test-case-default';
    }
};

const highlightResultsContainer = () => {
    shouldHighlightResults.value = true;
    setTimeout(() => {
        shouldHighlightResults.value = false;
    }, 2000);
};

const onQuit = () => {
    field.classList.add('main');
    field.classList.remove('new-main');
    console.log(props.id);
    duelStore.disconnect(props.id);
    router.push('/');
};

const onSubmit = async () => {
    if (isLoading.value) return;
    
    isLoading.value = true;
    try {
        console.log('Отправляемый код:', codeInputBox.value.code);
        const response = await duelStore.submitSolution(props.id, currentProblem.value.id, { code: codeInputBox.value.code });
        
        if (response.data && Array.isArray(response.data.results)) {
            testCases.value = response.data.results;
            summary.value = response.data.summary;
            console.log('Количество тест-кейсов:', testCases.value.length);
            console.log('Первый тест-кейс:', testCases.value[0]);
            
            areResultsEmpty.value = false;
            
            if (testCases.value.length > 0) {
                selectTestCase(0);
            }
            
            await nextTick();
            highlightResultsContainer();
            
            if (response.data.summary && response.data.summary.total === response.data.summary.passed) {
                makePunch();
            }
        } else {
            console.log('Нестандартная структура, показываем JSON');
            defaultResultMessage.value = JSON.stringify(response.data, null, 2);
            areResultsEmpty.value = true;
        }
        
    } catch (error) {
        console.error('Ошибка:', error);
        defaultResultMessage.value = "Ошибка: " + error.message;
        areResultsEmpty.value = true;
    } finally {
        isLoading.value = false;
    }
};

const selectTestCase = (idx) => {
    selectedTestCase.value = testCases.value[idx];
    console.log(selectedTestCase);
};

const onActionEnd = () => {
    showAction.value = false;
    animationSource.value = animations.idle;
};

const makePunch = () => {
    // Move to the next problem 
    currentProblemIndex.value += 1;

    // Reset result board
    areResultsEmpty.value = true;
    
    // Make a punch
    showAction.value = true;
    animationSource.value = animations.playerAttack;

    if (currentProblemIndex.value === problems.value.length) {
        gameEnded.value = true;
        rollCredits();
    }   
};

const rollCredits = () => {
};

onMounted(async () => {
    problems.value = (await duelsApi.getProblems(props.id)).data;
    duel.value = (await duelsApi.getDuel(props.id)).data;
    console.log(duel.value)
    console.log(opponent.value, userId);

    const socketUrl = 'http://82.202.138.90:8080/ws';
            
    const socket = new SockJS(socketUrl);
    
    stompClient.value = new Client({
        webSocketFactory: () => socket,
        reconnectDelay: 5000,
        heartbeatIncoming: 10000,
        heartbeatOutgoing: 10000,
        debug: (str) => {
            console.log('🔧 STOMP:', str);
        },
        onConnect: () => {
            const privateTopic = `/user/${userId}/queue/messages`;
            stompClient.value.subscribe(privateTopic, (message) => {
                console.log('данные:', message.body);
            });
        }
    });
    
    console.log(problems.value);
    currentProblem.value = problems.value[currentProblemIndex.value];
});
</script>

<style>
.new-main {
    max-width: 100%;
    margin: 0;
    padding: 12px;
    z-index: 1;
}

/* Стили для кнопки с индикатором загрузки */
.button-container {
    display: flex;
    flex-direction: column;
    gap: 8px;
    margin-top: 10px;
}

.send-button {
    position: relative;
    padding: 10px 20px;
    background-color: var(--green);
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-weight: 600;
    transition: background-color 0.3s, transform 0.2s;
    min-height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.send-button:hover:not(:disabled) {
    background-color: #2ecc71;
    transform: translateY(-2px);
}

.send-button:disabled {
    opacity: 0.7;
    cursor: not-allowed;
}

.button-loading-spinner {
    width: 20px;
    height: 20px;
    border: 3px solid rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    border-top-color: white;
    animation: spin 1s linear infinite;
}

.loading-text {
    text-align: center;
    color: var(--green);
    font-size: 0.9em;
    animation: pulse 1.5s infinite;
}

/* Анимация подсветки контейнера результатов */
.highlight-container {
    animation: highlight-pulse 1s ease-in-out;
    box-shadow: 0 0 20px rgba(46, 204, 113, 0.5);
}

/* Стили для тест-кейсов */
.test-case-btns {
    display: flex;
    gap: 8px;
    margin-bottom: 15px;
}

.test-case-btn {
    padding: 8px 12px;
    border-radius: 5px;
    cursor: pointer;
    font-weight: 500;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    gap: 5px;
    border: 2px solid transparent;
    user-select: none;
}

.test-case-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.test-case-icon {
    font-weight: bold;
}

/* Цвета статусов тест-кейсов */
.test-case-passed {
    background-color: rgba(46, 204, 113, 0.15);
    color: var(--green);
    border-color: var(--green);
}

.test-case-passed:hover {
    background-color: rgba(60, 231, 70, 0.15);
}

.test-case-failed {
    background-color: rgba(231, 76, 60, 0.15);
    color: #c0392b;
    border-color: #e74c3c;
}

.test-case-failed:hover {
    background-color: rgba(231, 76, 60, 0.25);
}

.test-case-error {
    background-color: rgba(155, 89, 182, 0.15);
    color: #8e44ad;
    border-color: #9b59b6;
}

.test-case-error:hover {
    background-color: rgba(155, 89, 182, 0.25);
}

.test-case-timeout {
    background-color: rgba(243, 156, 18, 0.15);
    color: #d35400;
    border-color: #f39c12;
}

.test-case-timeout:hover {
    background-color: rgba(243, 156, 18, 0.25);
}

.test-case-default {
    background-color: rgba(52, 152, 219, 0.15);
    color: #2980b9;
    border-color: #3498db;
}

.test-case-default:hover {
    background-color: var(--background-color);
}

/* Контейнер с контентом тест-кейса */
.test-case-content {
    background: rgba(255, 255, 255, 0.05);
    border-radius: 8px;
    padding: 15px;
    border-left: 4px solid var(--white);
}

.title {
    font-weight: 600;
    color: white;
    margin: 8px 0 4px;
    font-size: 0.9em;
}
.test-case-content.passed {
    border-left: 4px solid var(--green);
    .title {
        color: var(--green); 
    } 
}
.test-case-content.failed {
    border-left: 4px solid var(--red);
    .title {
        color: var(--red); 
    } 
}
.test-case-content.error {
    border-left: 4px solid #8e44ad;
    .title {
        color: #8e44ad;
    } 
}

.data-line {
    background: rgba(0, 0, 0, 0.1);
    padding: 8px;
    border-radius: 4px;
    margin-bottom: 10px;
    font-family: monospace;
    word-break: break-all;
}

.game__problem-difficulty.easy { color: var(--green); }
.game__problem-difficulty.medium { color: var(--yellow); }
.game__problem-difficulty.hard { color: var(--red); }

.game__bars {
    display: flex;
    flex-direction: column;
    gap: 12px;
    background-color: var(--dark);
    padding: 12px;
}

/* Анимации */
@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

@keyframes pulse {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.7; }
}

@keyframes highlight-pulse {
    0%, 100% { 
        box-shadow: 0 0 0 rgba(46, 204, 113, 0); 
    }
    50% { 
        background-color: #f8f186; 
    }
}
</style>