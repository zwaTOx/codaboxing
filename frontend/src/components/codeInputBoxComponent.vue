<template>
    <div class="game__code-input">
        <Codemirror
            v-model="code"
            :extensions="extensions"
            :style="editorStyle"
        />
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { Codemirror } from 'vue-codemirror';

import { python } from '@codemirror/lang-python';
import { githubDark } from '@uiw/codemirror-theme-github';
import { basicSetup } from 'codemirror';

const code = ref(`def execute(x):
    return x+1`);

const extensions = [basicSetup, python(), githubDark];
const editorStyle = {
    height: '400px',
    fontSize: '16px',
    fontFamily: "'Fira Code', monospace !important",
}

defineExpose({
    code,
});
</script>