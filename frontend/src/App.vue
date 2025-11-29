<script setup>
import headerComponent from './components/headerComponent.vue';
</script>

<template>
  <div class="blue-ball"></div>
  <div class="yellow-ball"></div>
  <headerComponent />
  <main class="main">
    <router-view ></router-view>
  </main>
</template>

<script>
export default {
  name: 'App',
  components: { headerComponent },
  data() {
    return {

    }
  }
}
</script>
