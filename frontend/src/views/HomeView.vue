<template>
  <main class="flex flex-col items-center">
    <div
      class="w-screen grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 grid-flow-row gap-4 pt-10"
    >
      <div v-for="video in videos" :key="video.id">
        <VideoVue :info="video" />
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

const videos = ref([]);
const loadMoreLabel = ref("Load More");
const page = ref(0);

onMounted(() => {
  loadVideos();
});

function loadVideos() {
  loadMoreLabel.value = "Loading";

  api.get(`/video/explore?page=${page.value}&size=10`).then((response) => {
    response.data.forEach((video) => {
      videos.value.push(video);
    });

    if (response.data.length == 0) loadMoreLabel.value = "No more videos";
    else loadMoreLabel.value = "Load More";
  });
}

function loadMore() {
  page.value += 1;
  loadVideos();
}
</script>
