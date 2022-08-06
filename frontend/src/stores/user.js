import { defineStore } from "pinia";
import { useCookies } from "vue3-cookies";

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
      // cookies.set("AUTHORIZATION", `Bearer ${this.access_token}`);
      // cookies.set("refresh_token", this.refresh_token);
    },
  },
});
