<template>
  <main class="flex flex-col items-center">
    <div
      class="w-screen grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 grid-flow-row gap-4 pt-10"
    >
      <div v-for="video in videos" :key="video.id">
        <VideoVue
          @click="openVideo(video.id)"
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
import { ref, onMounted, watch } from "vue";
import api from "../http-common.js";
import VideoVue from "../components/Video.vue";
import { useUserStore } from "../stores/user.js";
import { useRouter } from "vue-router";


const user = useUserStore();
const router = useRouter();


const videos = ref([]);
const loadMoreLabel = ref("Load More");
const page = ref(0);

const self = this;

onMounted(() => {
  loadVideos();
});

watch(
  () => user.searchQuery,
  () => {
    videos.value = [];
    loadVideos();
  }
);

function loadVideos() {
  loadMoreLabel.value = "Loading";

  api
    .get(`/video/explore?page=${page.value}&size=12&search=${user.searchQuery}`)
    .then((response) => {
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

function openVideo(id) {

  router.push(`/video/${id}`)
}
</script>
