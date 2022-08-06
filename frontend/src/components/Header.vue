<template>
  <div class="px-4 py-4 flex justify-between">
    <div
      @click="this.$router.push({ name: 'home' })"
      class="font-bold cursor-pointer"
    >
      Video Library
    </div>

    <div class="flex items-center justify-center">
      <Btn class="mr-3" @click="toggleTheme">
        <font-awesome-icon icon="sun" />
      </Btn>
      <Btn @click="upload">
        <font-awesome-icon icon="circle-plus" /> Upload Video</Btn
      >
      <div @click="profile" class="ml-10 mr-4">
        <font-awesome-icon icon="user" />
      </div>
    </div>
  </div>
  <LoginVue :open="loginModal" :onSuccess="openLoginfor" :key="loginModalKey" />
</template>

<script setup>
import { ref } from "vue";
import Btn from "./Btn.vue";
import LoginVue from "../components/Login.vue";
import { useUserStore } from "../stores/user.js";
import { useRouter } from "vue-router";

const user = useUserStore();
const router = useRouter();

const loginModal = ref(false);
const loginModalKey = ref(0);
const openLoginfor = ref("");

function upload() {
  if (user.loggedIn) {
    router.push({ name: "upload" });
  } else {
    openLoginfor.value = "upload";
    loginModalKey.value = Math.random();
    loginModal.value = true;
  }
}

function profile() {
  if (user.loggedIn) {
    router.push({ name: "profile" });
  } else {
    openLoginfor.value = "profile";
    loginModalKey.value = Math.random();
    loginModal.value = true;
  }
}

function toggleTheme() {
  if (
    localStorage.theme === "dark" ||
    (!("theme" in localStorage) &&
      window.matchMedia("(prefers-color-scheme: dark)").matches)
  ) {
    document.documentElement.classList.remove("dark");
    localStorage.theme = "light";
  } else {
    document.documentElement.classList.add("dark");
    localStorage.theme = "dark";
  }
}
</script>
