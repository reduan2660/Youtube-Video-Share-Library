<template>
  <div
    class="flex items-center py-2 px-4 bg-emerald-200 dark:bg-emerald-700 text-sm"
  >
    <div class="mr-2">Your email is not verified. Please check your email.</div>
    <Btn @click="resend(0)"> {{ label }} </Btn>
  </div>
</template>

<script setup>
import Btn from "./Btn.vue";
import { ref } from "vue";
import api from "../http-common.js";
import { useUserStore } from "../stores/user.js";
const user = useUserStore();

const label = ref("Resend Email");

function resend(counter) {
  api
    .get("/user/resendcode", {
      headers: {
        AUTHORIZATION: `Bearer ${user.access_token}`,
      },
    })
    .then((response) => {
      label.value = "Email Sent";
    })
    .catch((err) => {
      if (err.response.status == 403 && err.response.data == null) {
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
            resend(counter + 1);
          })
          .catch((err) => {
            user.logout();
          });
      } else {
        console.log(err.response.data);
      }
    });
}
</script>
