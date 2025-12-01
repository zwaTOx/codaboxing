<template>
    <div class="game-wrapper">
        <div class="game-container">
            <div class="task-container">
                <div class="task-heading">
                    <div class="task-heading-text">1. Поиск простых чисел</div>
                    <button @click="onQuit" class="quit-button">Выйти из игры</button>
                    <div class="task-difficulty">
                        easy
                    </div>
                </div>
                <div class="task-description">
                    Напишите функцию, которая возвращает все простые числа до заданного числа N.<br>Простое число - это натуральное число больше 1, которое делится только на 1 и на само себя.<br>
                    <h3>Пример:</h3>
                    Ввод: 10<br>
                    Вывод: [2, 3, 5, 7]<br>
                    <h3>Ограничения:</h3>
                    2 ≤ n ≤ 10000
                </div>
            </div>
            <div class="double-container">
                <div class="code-container">
                    <div class="task-heading-text"><> Код</div>
                    <textarea class="code-input" name="" id=""> </textarea>
                </div>
                <div class="result-container">
                    Результат
                </div>
            </div>
            <div class="animation-container">
                <img src="../assets/box.png"></img>
            </div>
        </div>

    </div>
</template>

<script setup>
const props = defineProps({
    id: {
        type: Number,
        required: true,
    }
});
import router from '@/router';
import { useDuelStore } from '@/stores/duels';

const store = useDuelStore();
const field = document.querySelector('main');
field.classList.remove('main');
field.classList.add('new-main');

const onQuit = () => {
    field.classList.add('main');
    field.classList.remove('new-main');
    console.log(props.id);
    store.disconnect(props.id);
    router.push('/');
}
</script>

<style scoped>
.new-main {
    max-width: 100%;
    margin: 0;
    padding: 12px;
    z-index: 1;
}

</style>