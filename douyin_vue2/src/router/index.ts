import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const router = new VueRouter({
  mode: "history",
  base: import.meta.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("../views/one.vue"),
    },
    {
      path: "/homeone",
      name: "homeone",
      component: () => import("../views/home.vue"),
    },
    {
      path: "/authhome",
      name: "authhome",
      component: () => import("../views/auth/auth.vue"),
    },
    {
      path: "/freevideo",
      name: "FreeVideoVue",
      component: () => import("../views/home/FreeVideo.vue"),
    },
    {
      path: "/me",
      name: "MeVue",
      component: () => import("../views/me/me.vue"),
    },
    {
      path: "/shop",
      name: "ShopVue",
      component: () => import("../views/shop/shop.vue"),
    },
    {
      path: "/login",
      name: "LoginVue",
      component: () => import("../views/login/login.vue"),
    },
    {
      path: "/search",
      name: "SearchVue",
      component: () => import("../views/search/search.vue"),
    },
    {
      path: "/xiaoxi",
      name: "XiaoXiVue",
      component: () => import("../views/xiaoxi/xiaoxi.vue"),
    },
    {
      path: "/shangchuan",
      name: "ShangchuanVue",
      component: () => import("../views/shangchuan/shangchuan.vue"),
    },
    {
      path: "/music",
      name: "MusicVue",
      component: () => import("../views/music/music.vue"),
    },
    {
      path: "/auth",
      name: "AuthVue",
      component: () => import("../views/auth/auth.vue"),
    },
    {
      path: "/xiaodetail",
      name: "XiaoDetailVue",
      component: () => import("../views/xiaoxi/xiaoxitdetail.vue"),
    },
    {
      path: "/shopdetail",
      name: "ShopDetailVue",
      component: () => import("../views/shop/shopdetail.vue"),
    },
    {
      path: "/setting",
      name: "SettingVue",
      component: () => import("../views/setting.vue"),
    },
    {
      path: "/friend",
      name: "FriendVue",
      component: () => import("../views/me/friend.vue"),
    },
    {
      path: "/articledetail",
      name: "Articledetail",
      component: () => import("../views/home/articledetail.vue"),
    },
    {
      path: "/searchdetail",
      name: "Searchdetail",
      component: () => import("../views/search/searchdetail.vue"),
    },
    // Add more routes as needed
    // {
    //   path: '/about',
    //   name: 'about',
    //   component: () => import('../views/AboutView.vue')
    // }
  ],
});

export default router;
