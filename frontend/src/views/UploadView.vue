<template>
  <div class="w-screen px-4 md:px-96 flex flex-col items-center">
    <div class="w-full pt-12">
      <div>Name</div>
      <input
        v-model="name"
        type="url"
        id="website"
        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
        placeholder="E Hawa by Meghdol"
        required
      />
    </div>

    <div class="w-full pt-12">
      <div>Youtube Link</div>
      <input
        v-model="ytlink"
        type="url"
        id="website"
        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
        placeholder="https://youtu.be/9Oa_mWpckns"
        required
      />
    </div>

    <div class="pt-12 text-red-400">{{ errLabel }}</div>

    <div class="pt-12">
      <Btn @click="upload(0)">{{ btnLabel }}</Btn>
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

const name = ref("");
const ytlink = ref("");

const errLabel = ref("");
const newVideoId = ref();
const btnLabel = ref("Upload");
const uploadSuccess = ref(false);

onMounted(() => {
  if (!user.loggedIn) router.push({ name: "home" });
});

function upload(counter) {
  if (!uploadSuccess.value) {
    if (counter < 2) {
      errLabel.value = "";

      api
        .post(
          "/video/upload",
          {
            name: name.value,
            ytlink: ytlink.value,
          },
          {
            headers: {
              AUTHORIZATION: `Bearer ${user.access_token}`,
            },
          }
        )
        .then((response) => {
          uploadSuccess.value = true;
          btnLabel.value = "Preview Video";
          newVideoId.value = response.data.id;
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
                upload(counter + 1);
              })
              .catch((err) => {
                user.logout();
              });
          } else {
            console.log(err.response.data);
            for (let key in err.response.data) {
              errLabel.value = err.response.data[key];
            }
          }
        });
    }
  } else {
    router.push(`video/${newVideoId.value}`);
  }
}
</script>
