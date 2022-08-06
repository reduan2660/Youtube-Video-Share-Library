<template>
  <ModalVue label="Sign in to continue" :open="openModal"
    ><div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
      <div class="pb-6 px-6 lg:px-8">
        <form class="space-y-6" action="#">
          <div>
            <label
              for="email"
              class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300"
              >Your email</label
            >
            <input
              type="email"
              name="email"
              id="email"
              v-model="email"
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
              placeholder="name@company.com"
              required
            />
          </div>
          <div>
            <label
              for="password"
              class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300"
              >Your password</label
            >
            <input
              type="password"
              name="password"
              id="password"
              v-model="password"
              placeholder="••••••••"
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
              required
            />
          </div>
          <div class="flex justify-between">
            <div class="flex items-end"></div>
            <a
              href="#"
              class="text-sm text-blue-700 hover:underline dark:text-blue-500"
              >Lost Password?</a
            >
          </div>
          <button
            type="submit"
            @click.prevent="login"
            class="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
          >
            Login to your account
          </button>
          <div class="text-sm font-medium text-gray-500 dark:text-gray-300">
            Not registered?
            <a href="#" class="text-blue-700 hover:underline dark:text-blue-500"
              >Create account</a
            >
          </div>
        </form>
      </div>
    </div>
  </ModalVue>
</template>

<script setup>
import { ref, toRefs } from "vue";
import ModalVue from "./Modal.vue";
import api from "../http-common.js";
import { useUserStore } from "../stores/user.js";
import { useRouter } from "vue-router";

const user = useUserStore();
const router = useRouter();

let email = ref("");
let password = ref("");

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  onSuccess: {
    type: String,
    required: true,
  },
});

const openModal = ref(props.open);

function login() {
  const formData = new FormData();
  formData.append("email", email.value);
  formData.append("password", password.value);

  api
    .get("/user/login", {
      params: {
        email: email.value,
        password: password.value,
      },
    })
    .then((response) => {
      user.setToken(response.data.access_token, response.data.refresh_token);
      router.push({ name: props.onSuccess });
      openModal.value = false;
    });
}
</script>
