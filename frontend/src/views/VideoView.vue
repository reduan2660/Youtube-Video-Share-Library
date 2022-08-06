<template>
  <div class="flex flex-col items-center">
    <div class="w-screen md:w-3/4">
      <div class="aspect-w-16 aspect-h-9">
        <iframe
          :src="embededSrc"
          frameborder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowfullscreen
        ></iframe>
      </div>
    </div>

    <div
      class="flex flex-row w-full px-6 pt-6 justify-between md:justify-around"
    >
      <div>{{ info.views }} <font-awesome-icon icon="eye" /></div>
      <div>
        <span class="italic font-thin"> Uploaded by </span
        ><span class="font-semibold"> {{ info.owner }} | </span>
        <span
          @click="openReaction"
          class="cursor-pointer font-light underline underline-offset-2"
        >
          More Details</span
        >
      </div>
      <div class="flex">
        <div class="pr-4">
          <font-awesome-icon icon="thumbs-up" class="icon alt" />
          {{ info.likes }}
        </div>
        <div>
          <font-awesome-icon icon="thumbs-down" class="icon alt" />
          {{ info.dislikes }}
        </div>
      </div>
    </div>
  </div>
  <ReactionsVue
    :open="openReactionModal"
    :videoId="reactionModalVideo"
    :key="reactionModalKey"
  />
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "../http-common.js";
import ReactionsVue from "../components/Reactions.vue";

import { useRoute } from "vue-router";
const route = useRoute();

const info = ref({});
let embededSrc = ref("");

const openReactionModal = ref(false);
const reactionModalKey = ref(0);
const reactionModalVideo = ref(0);

onMounted(() => {
  loadVideos();
});

function loadVideos() {
  api.get(`/video/det/${route.params.id}`).then((response) => {
    info.value = response.data;
    setEmbeded(info.value.ytlink);
  });
}

function setEmbeded(ytlink) {
  const regexLink =
    /http(?:s?):\/\/(?:www\.)?youtu(?:be\.com\/watch\?v=|\.be\/)([\w\-\_]*)(&(amp;)?‌​[\w\?‌​=]*)?/;

  const match = ytlink.match(regexLink);
  embededSrc.value = "https://www.youtube.com/embed/" + match[1];
}

function openReaction() {
  reactionModalVideo.value = info.value.id;
  openReactionModal.value = true;
  reactionModalKey.value = Math.random();
}
</script>
