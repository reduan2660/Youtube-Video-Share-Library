<template>
  <div class="w-screen px-4 md:px-96 flex flex-col items-center">
    <div class="w-full pt-12">
      <div>Name</div>
      <div class="outline rounded-sm py-1 px-2 mt-2">{{ userInfo.name }}</div>
    </div>

    <div class="w-full pt-12">
      <div>Email</div>
      <div class="outline rounded-sm py-1 px-2 mt-2">{{ userInfo.email }}</div>
    </div>

    <div class="pt-12">
      <Btn @click="myvideo">Uploaded Videos</Btn>
    </div>

    <div class="pt-12">
      <Btn @click="logout">Log out</Btn>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useUserStore } from "../stores/user.js";
import { useRouter } from "vue-router";
import Btn from "../components/Btn.vue";
import api from "../http-common.js";

const user = useUserStore();
const router = useRouter();

const userInfo = ref({
  name: "",
  email: "",
});

onMounted(() => {
  if (!user.loggedIn) router.push({ name: "home" });
  else getProfile(0);
});

function getProfile(counter) {
  if (counter < 2) {
    api
      .get("/user/profile", {
        headers: {
          AUTHORIZATION: `Bearer ${user.access_token}`,
        },
      })
      .then((response) => {
        userInfo.value = response.data;
      })
      .catch((err) => {
        if (err.response.status == 403) {
          // refresh token
          api
            .get("/token/refresh", {
              headers: {
                AUTHORIZATION: `Bearer ${user.refresh_token}`,
              },
            })
            .then((refreshResponse) => {
              user.setToken(
                refreshResponse.data.access_token,
                refreshResponse.data.refresh_token
              );
              upload(counter + 1);
            })
            .catch((err) => {
              user.logout();
            });
        }
      });
  }
}

function logout() {
  user.logout();
  router.push({ name: "home" });
}

function myvideo() {
  router.push({ name: "myvideos" });
}
</script>
