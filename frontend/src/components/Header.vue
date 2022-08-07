<template>
  <EmailVerificationAlertVue v-if="user.loggedIn && user.verified == false" />
  <div class="px-4 py-4 flex justify-between items-center">
    <div
      @click="navigateHome"
      class="font-bold cursor-pointer italic text-2xl underline"
    >
      Video Library
    </div>

    <form class="items-center ml-36 hidden md:flex">
      <label for="simple-search" class="sr-only">Search</label>
      <div class="relative w-full">
        <div
          class="flex absolute inset-y-0 left-0 items-center pl-3 pointer-events-none"
        >
          <svg
            aria-hidden="true"
            class="w-5 h-5 text-gray-500 dark:text-gray-400"
            fill="currentColor"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fill-rule="evenodd"
              d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
              clip-rule="evenodd"
            ></path>
          </svg>
        </div>
        <input
          v-model="search"
          type="text"
          id="simple-search"
          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
          placeholder="Search"
        />
      </div>
      <button
        @click.prevent="user.setsearchQuery(search)"
        type="submit"
        class="p-2.5 ml-2 text-sm font-medium text-white bg-blue-700 rounded-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
      >
        <svg
          class="w-5 h-5"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
          ></path>
        </svg>
        <span class="sr-only">Search</span>
      </button>
    </form>

    <div class="flex items-center justify-center">
      <Btn class="mr-3" @click="toggleTheme">
        <font-awesome-icon icon="sun" />
      </Btn>
      <Btn @click="upload">
        <font-awesome-icon icon="circle-plus" /> Add Video</Btn
      >
      <div @click="profile" class="ml-3 mr-4 cursor-pointer">
        <Btn> <font-awesome-icon icon="user" /> </Btn>
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
import EmailVerificationAlertVue from "./EmailVerificationAlert.vue";

const user = useUserStore();
const router = useRouter();

const loginModal = ref(false);
const loginModalKey = ref(0);
const openLoginfor = ref("");

const search = ref("");

function navigateHome() {
  router.push({ name: "home" });
  search.value = "";
  user.setsearchQuery("");
}

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
