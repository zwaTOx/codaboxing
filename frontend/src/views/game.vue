<template>
    <div v-if="gameEnded" class="game-cover-screen active">
        <div class="result-title">ПОБЕДА !!!</div>
    </div>
    <div class="game__wrapper">
        <div class="game__container">
            <div class="game__problem-container">
                <div class="game__problem-header">
                    <div class="game__problem-heading-text">{{ currentProblemIndex + 1 }}. {{ currentProblem.title }}</div>
                    <div class="game__problem-difficulty">{{ currentDifficulty }}</div>
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
                    <button class="send-button" @click="onSubmit">Отправить результат</button>
                </div>
                <div class="game__result-container">
                    <div class="game__heading-text results">
                        <span style="color: var(--green); font-weight: 800;">✔</span> Результат
                        <div class="feedback-message">{{ selectedTestCase?.errorMessage }}</div>
                    </div>
                    <div v-if="areResultsEmpty" class="game__results">{{ currentResult }}</div>
                    <div v-else class="game__results">
                        <div class="test-case-btns">
                            <div v-for="testcase, idx in testCases" class="test-case-btn"
                            @click="selectTestCase(idx)">
                                Тест {{ idx + 1 }}
                            </div>
                        </div>
                        <div class="test-case-content">
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
import { ref, onMounted, computed, watch } from 'vue';

import { useDuelStore } from '@/stores/duels';
import { duelsApi } from '@/api/duels';
import CodeInputBoxComponent from '@/components/codeInputBoxComponent.vue';
import { dictToVariables } from '@/composables/dictToVariables';

// Misc Variables
const props = defineProps({
    id: {
        type: Number,
        required: true,
    },
});
const codeInputBox = ref(null);
const store = useDuelStore();
const field = document.querySelector('main');
const difficulties = {
    "EASY": "легко",
    "MEDIUM": "нормально так",
    "HARD": "тяжко"
}
const gameEnded = ref(false);

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
const currentProblem = ref({});
const areResultsEmpty = ref(true);

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
const onQuit = () => {
    field.classList.add('main');
    field.classList.remove('new-main');
    console.log(props.id);
    store.disconnect(props.id);
    router.push('/');
}

const onSubmit = async () => {
    console.log(codeInputBox.value.code);
    const response = await store.submitSolution(props.id, currentProblem.value.id, { code: codeInputBox.value.code });

    currentResult.value = response.data;
    testCases.value = response.data.results;
    summary.value = response.data.summary;
    console.log(response.data.summary.total === response.data.summary.passed );
    
    if (response.data.summary.total === response.data.summary.passed ) makePunch();

    areResultsEmpty.value = false;
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

    if (currentProblemIndex === problems.length) gameEnded.value = true;   
};

const rollCredits = () => {

};

onMounted(async () => {
    problems.value = (await duelsApi.getProblems(props.id)).data;
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
</style>