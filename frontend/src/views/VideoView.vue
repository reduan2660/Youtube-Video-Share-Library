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
        <div @click="react(0, 'like')" class="pr-4 cursor-pointer">
          <font-awesome-icon
            icon="thumbs-up"
            class="icon alt text-green-300"
            v-if="liked"
          />
          <font-awesome-icon icon="thumbs-up" class="icon alt" v-else />
          {{ info.likes }}
        </div>
        <div @click="react(0, 'dislike')" class="cursor-pointer">
          <font-awesome-icon
            icon="thumbs-down"
            class="icon alt text-red-300"
            v-if="disliked"
          />
          <font-awesome-icon icon="thumbs-down" class="icon alt" v-else />
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
  <LoginVue :open="loginModal" :onSuccess="openLoginfor" :key="loginModalKey" />
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import api from "../http-common.js";
import ReactionsVue from "../components/Reactions.vue";
import { useUserStore } from "../stores/user.js";
import { useRoute } from "vue-router";
import LoginVue from "../components/Login.vue";

const route = useRoute();
const user = useUserStore();

const info = ref({});
let embededSrc = ref("");

const openReactionModal = ref(false);
const reactionModalKey = ref(0);
const reactionModalVideo = ref(0);

const loginModal = ref(false);
const loginModalKey = ref(0);
const openLoginfor = ref("return");

const liked = ref(false);
const disliked = ref(false);
const reactionVal = ref("");

onMounted(() => {
  loadVideos();
});

watch(
  () => user.loggedIn,
  () => {
    if (user.loggedIn) {
      react(0, reactionVal.value);
    }
  }
);

function loadVideos() {
  api.get(`/video/det/${route.params.id}`).then((response) => {
    info.value = response.data;
    setEmbeded(info.value.ytlink);

    if (user.loggedIn) {
      getMyReaction(0);
    }
  });
}

function setEmbeded(ytlink) {
  const regexLink =
    /http(?:s?):\/\/(?:www\.)?youtu(?:be\.com\/watch\?v=|\.be\/)([\w\-\_]*)(&(amp;)?‌​[\w\?‌​=]*)?/;

  const match = ytlink.match(regexLink);
  embededSrc.value = "https://www.youtube.com/embed/" + match[1];
}

function getMyReaction(counter) {
  console.log(info.value);
  if (counter < 2) {
    api
      .get(`/video/myreaction/${info.value.id}`, {
        headers: {
          AUTHORIZATION: `Bearer ${user.access_token}`,
        },
      })

      .then((response) => {
        liked.value = response.data.liked;
        disliked.value = response.data.disliked;
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
              getMyReaction(counter + 1);
            })
            .catch((err) => {
              user.logout();
            });
        }
      });
  }
}

function react(counter, reaction) {
  reactionVal.value = reaction;
  if (user.loggedIn) {
    api
      .post(
        `/video/${reaction}/${info.value.id}`,
        {},
        {
          headers: {
            AUTHORIZATION: `Bearer ${user.access_token}`,
          },
        }
      )
      .then((response) => {
        loadVideos();
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
              react(counter + 1, reaction);
            })
            .catch((err) => {
              user.logout();
            });
        }
      });
  } else {
    loginModal.value = true;
    loginModalKey.value = Math.random();
  }
}

function openReaction() {
  reactionModalVideo.value = info.value.id;
  openReactionModal.value = true;
  reactionModalKey.value = Math.random();
}
</script>
