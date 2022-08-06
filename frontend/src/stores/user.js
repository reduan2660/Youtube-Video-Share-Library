import { defineStore } from "pinia";
import { useCookies } from "vue3-cookies";
import api from "../http-common.js";

export const useUserStore = defineStore({
  id: "user",
  state: () => ({
    loggedIn: false,
    access_token: "",
    refresh_token: "",
  }),
  getters: {},
  actions: {
    setToken(new_access_token, new_refresh_token) {
      const { cookies } = useCookies();

      this.loggedIn = true;
      this.access_token = new_access_token;
      this.refresh_token = new_refresh_token;
    },

    logout() {
      this.loggedIn = false;
    },
  },
});
