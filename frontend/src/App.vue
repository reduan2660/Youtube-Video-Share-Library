<template>
  <defaultLayout />
</template>

<script setup>
import defaultLayout from "./layout/default.vue";
import { onMounted } from "@vue/runtime-core";

onMounted(() => {
  if (
    localStorage.theme === "dark" ||
    (!("theme" in localStorage) &&
      window.matchMedia("(prefers-color-scheme: dark)").matches)
  ) {
    document.documentElement.classList.add("dark");
  } else {
    document.documentElement.classList.remove("dark");
  }
});
</script>
