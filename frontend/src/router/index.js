import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/video/:id",
      name: "video",
      component: () => import("../views/VideoView.vue"),
    },
    {
      path: "/upload",
      name: "upload",
      component: () => import("../views/UploadView.vue"),
    },
    {
      path: "/profile",
      name: "profile",
      component: () => import("../views/ProfileView.vue"),
    },
    {
      path: "/myvideos",
      name: "myvideos",
      component: () => import("../views/MyVideoView.vue"),
    },
    {
      path: "/verify/email/:email/code/:code",
      name: "verify",
      component: () => import("../views/VerifyView.vue"),
    },
  ],
});

export default router;
