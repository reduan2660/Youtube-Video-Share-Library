<template>
  <ModalVue :label="labels[actionMode].modal" :open="openModal"
    ><div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
      <div class="pb-6 px-6 lg:px-8">
        <form class="space-y-6" @submit.prevent="processSubmission">
          <div v-if="actionMode == 'registration'">
            <label
              for="name"
              class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300"
              >Your name</label
            >
            <input
              type="text"
              name="name"
              id="name"
              v-model="name"
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
              placeholder="Your name"
              required
            />
          </div>
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
          <div class="flex justify-center" v-if="loginerror">
            <div class="flex items-center"></div>
            <span href="#" class="text-sm text-red-500">{{ errlabel }}</span>
          </div>
          <button
            type="submit"
            class="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
          >
            {{ labels[actionMode].submitBtn }}
          </button>
          <div class="flex flex-row items-center">
            <div
              class="mr-2 text-sm font-medium text-gray-500 dark:text-gray-300"
            >
              {{ labels[actionMode].toggleHelper }}
            </div>
            <div
              @click="toggleMode"
              class="cursor-pointer text-blue-700 hover:underline dark:text-blue-500"
            >
              {{ labels[actionMode].toggleBtn }}
            </div>
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

let name = ref("");
let email = ref("");
let password = ref("");
const loginerror = ref(false);
const errlabel = ref("Failed to login. Please check your credentials.");

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

const labels = ref({
  login: {
    modal: "Sign in to continue",
    submitBtn: "Login to your account",
    toggleHelper: "Not registered?",
    toggleBtn: "Create account",
  },
  registration: {
    modal: "Create an account",
    submitBtn: "Sign up",
    toggleHelper: "Already have an account?",
    toggleBtn: "Login instead",
  },
});

const actionMode = ref("login");

function login() {
  labels.value.login.submitBtn = "Processing";
  loginerror.value = false;
  api
    .get("/user/login", {
      params: {
        email: email.value,
        password: password.value,
      },
    })
    .then((response) => {
      labels.value.login.submitBtn = "Logged in";

      user.setToken(response.data.access_token, response.data.refresh_token);

      api
        .get("/user/profile", {
          headers: {
            AUTHORIZATION: `Bearer ${response.data.access_token}`,
          },
        })
        .then((response) => {
          user.setUser(response.data.verified);
        });

      if (props.onSuccess != "return") router.push({ name: props.onSuccess });
      openModal.value = false;
    })
    .catch((err) => {
      labels.value.login.submitBtn = "Login to your account";
      errlabel.value = "Failed to login. Please check your credentials.";
      loginerror.value = true;
    });
}

function registration() {
  loginerror.value = false;
  labels.value.registration.submitBtn = "Signing up";
  api
    .post("/user/registration", {
      name: name.value,
      email: email.value,
      password: password.value,
    })
    .then((response) => {
      labels.value.registration.submitBtn = "Signed up";
      login();
    })
    .catch((err) => {
      for (let key in err.response.data) {
        errlabel.value = err.response.data[key];
      }
      loginerror.value = true;
      labels.value.registration.submitBtn = "Sign up";
    });
}

function processSubmission() {
  if (actionMode.value == "login") login();
  else if (actionMode.value == "registration") registration();
}

function toggleMode() {
  if (actionMode.value == "login") actionMode.value = "registration";
  else if (actionMode.value == "registration") actionMode.value = "login";
}
</script>
