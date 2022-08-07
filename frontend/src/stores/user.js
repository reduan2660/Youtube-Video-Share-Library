import { defineStore } from "pinia";
import api from "../http-common.js";

export const useUserStore = defineStore({
  id: "user",
  state: () => ({
    loggedIn: false,
    access_token: "",
    refresh_token: "",
    verified: true,
    searchQuery: "",
  }),
  getters: {},
  actions: {
    setToken(new_access_token, new_refresh_token) {
      this.loggedIn = true;
      this.access_token = new_access_token;
      this.refresh_token = new_refresh_token;

      localStorage.setItem("refresh_token", new_refresh_token);
    },

    setUser(isverified) {
      this.verified = isverified;
    },

    setsearchQuery(new_Search_query) {
      this.searchQuery = new_Search_query;
    },

    logout() {
      this.loggedIn = false;
    },
  },
});
