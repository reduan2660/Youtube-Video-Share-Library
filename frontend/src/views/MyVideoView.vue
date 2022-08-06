<template>
  <main class="flex flex-col items-center">
    <div class="text-2xl font-medium underline italic">My Videos</div>
    <div
      class="w-screen grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 grid-flow-row gap-4 pt-10"
    >
      <div v-for="video in videos" :key="video.id">
        <VideoVue
          @click="this.$router.push(`/video/${video.id}`)"
          :info="video"
        />
      </div>
    </div>

    <div @click="loadMore" class="text-gray-600 pt-10 cursor-pointer">
      {{ loadMoreLabel }}
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "../http-common.js";
import VideoVue from "../components/Video.vue";
import { useUserStore } from "../stores/user.js";
import { useRouter } from "vue-router";

const user = useUserStore();
const router = useRouter();

const videos = ref([]);
const loadMoreLabel = ref("Load More");
const page = ref(0);

onMounted(() => {
  if (!user.loggedIn) router.push({ name: "home" });
  else loadVideos(0);
});

function loadVideos(counter) {
  if (counter < 2) {
    loadMoreLabel.value = "Loading";

    api
      .get(`/video/my?page=${page.value}&size=5`, {
        headers: {
          AUTHORIZATION: `Bearer ${user.access_token}`,
        },
      })

      .then((response) => {
        response.data.forEach((video) => {
          videos.value.push(video);
        });

        if (response.data.length == 0) loadMoreLabel.value = "No more videos";
        else loadMoreLabel.value = "Load More";
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

function loadMore() {
  page.value += 1;
  loadVideos(0);
}

function openVideo(id) {}
</script>
