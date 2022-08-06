<template>
  <ModalVue label="Reactions" :open="openModal">
    <div class="px-10 pb-10 text-black dark:text-white">
      <div v-for="reaction in reactions" :key="reaction.id">
        <div class="flex justify-between">
          <div>{{ reaction.reactor }}</div>
          <div v-if="reaction.liked" class="text-green-300">
            <font-awesome-icon icon="thumbs-up" class="icon alt" />
          </div>
          <div v-else-if="reaction.disliked">
            <font-awesome-icon icon="thumbs-down" class="icon alt" />
          </div>
        </div>
      </div>
    </div>
  </ModalVue>
</template>

<script setup>
import { ref, onMounted } from "vue";
import ModalVue from "./Modal.vue";
import api from "../http-common.js";
import { useUserStore } from "../stores/user.js";
import { useRouter } from "vue-router";
import LoginVue from "./Login.vue";

const user = useUserStore();
const router = useRouter();

const reactions = ref([]);

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  videoId: {
    required: true,
  },
});

const openModal = ref(props.open);
const videoId = ref(props.videoId);

onMounted(() => {
  loadReactions();
});

function loadReactions() {
  api.get(`/video/likes/${videoId.value}`).then((response) => {
    response.data.forEach((reaction) => {
      reactions.value.push(reaction);
    });
  });

  api.get(`/video/dislikes/${videoId.value}`).then((response) => {
    response.data.forEach((reaction) => {
      reactions.value.push(reaction);
    });
  });
}
</script>
