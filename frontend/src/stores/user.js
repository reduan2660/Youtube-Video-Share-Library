import { defineStore } from "pinia";

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
      this.loggedIn = true;
      this.access_token = new_access_token;
      this.refresh_token = new_refresh_token;
    },
  },
});
