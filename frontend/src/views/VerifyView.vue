<template>
  <div class="flex items-center justify-center">
    <div class="py-10 px-10 font-bold">{{ label }}</div>
    <div>
      <Btn v-if="verificationSucess" @click="navigateHome"> Back to Home</Btn>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "../http-common.js";
import { useRoute, useRouter } from "vue-router";
import Btn from "../components/Btn.vue";

const route = useRoute();
const router = useRouter();
const label = ref("Verification in process");
const verificationSucess = ref(false);

onMounted(() => {
  api
    .post(`/user/verify?code=${route.params.code}&email=${route.params.email}`)
    .then((response) => {
      label.value = "Verification Successful";
      verificationSucess.value = true;
    })
    .catch((err) => {
      label.value = "Verification Failed";
      verificationSucess.value = false;
    });
});

function navigateHome() {
  router.push({ name: "home" });
}
</script>
