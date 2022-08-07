<template>
  <defaultLayout />
</template>

<script setup>
import defaultLayout from "./layout/default.vue";
import { onMounted, onBeforeMount } from "@vue/runtime-core";
import api from "./http-common.js";
import { useUserStore } from "./stores/user.js";

const user = useUserStore();

onBeforeMount(() => {
  if (localStorage.getItem("refresh_token")) {
    refreshToken();
  }
});

onMounted(() => {
  setTheme();
});

function refreshToken() {
  api
    .get("/token/refresh", {
      headers: {
        AUTHORIZATION: `Bearer ${localStorage.getItem("refresh_token")}`,
      },
    })
    .then((refreshResponse) => {
      user.setToken(
        refreshResponse.data.access_token,
        refreshResponse.data.refresh_token
      );

      api
        .get("/user/profile", {
          headers: {
            AUTHORIZATION: `Bearer ${refreshResponse.data.access_token}`,
          },
        })
        .then((response) => {
          user.setUser(response.data.verified);
        });
    })
    .catch((err) => {
      user.logout();
    });
}

function setTheme() {
  if (
    localStorage.theme === "dark" ||
    (!("theme" in localStorage) &&
      window.matchMedia("(prefers-color-scheme: dark)").matches)
  ) {
    document.documentElement.classList.add("dark");
  } else {
    document.documentElement.classList.remove("dark");
  }
}
</script>
