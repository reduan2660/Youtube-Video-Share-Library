<template>
  <div>Upload a Video</div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useUserStore } from "../stores/user.js";
import { useRouter } from "vue-router";

const user = useUserStore();
const router = useRouter();

onMounted(() => {
  if (!user.loggedIn) router.push({ name: "home" });
});
</script>
