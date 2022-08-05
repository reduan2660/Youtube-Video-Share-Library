<template>
  <div class="bg-white dark:bg-gray-900 h-screen dark:text-white">
    <HeaderVue />
    <router-view />
  </div>
</template>

<script setup>
import HeaderVue from "../components/Header.vue";
</script>
