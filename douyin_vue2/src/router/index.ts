import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const router = new VueRouter({
  mode: "history",
  base: import.meta.env.BASE_URL,
  routes: [
    {
      path: "/wallet",
      name: "wallet",
      component: () => import("../views/shop/wallet.vue"),
    },
    {
      path: "/shopcat",
      name: "shopcat",
      component: () => import("../views/shop/shopcat.vue"),
    },
    {
      path: "/coupon",
      name: "coupon",
      component: () => import("../views/shop/coupon.vue"),
    },
    {
      path: "/videobox",
      name: "videobox",
      component: () => import("../components/videobox.vue"),
    },
    {
      path: "/testmap",
      name: "testmap",
      component: () => import("../components/leftmap.vue"),
    },
    {
      path: "/map",
      name: "map",
      component: () => import("../views/gaode.vue"),
    },
    {
      path: "/homeone",
      name: "home",
      component: () => import("../views/one.vue"),
    },
    {
      path: "/",
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
      path: "/auth/:userId",
      name: "AuthVue",
      component: () => import("../views/auth/auth.vue"),
    },

    {
      path: "/xiaodetail/:id",
      name: "XiaoDetailVue",
      component: () => import("../views/xiaoxi/xiaoxitdetail.vue"),
    },
    {
      path: "/shopdetail/:id",
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

    {
      path: "/chat",
      name: "Login",
      component: () => import("../views/chat/Login.vue"),
    },
    {
      path: "/chat/chatroom",
      name: "ChatRoom",
      component: () => import("../views/chat/ChatRoom.vue"),
    },
    {
      path: "/chat/adminlogin",
      name: "AdminLogin",
      component: () => import("../views/admin/AdminLogin.vue"),
    },
    {
      path: "/chat/home",
      name: "Home",
      component: () => import("../views/admin/Home.vue"),
    },
    {
      path: "/chat/userinfo",
      name: "用户信息管理",
      component: () => import("../views/admin/UserInfo.vue"),
    },
    {
      path: "/chat/groupChatRecord",
      name: "群聊记录管理",
      component: () => import("../views/admin/GroupChatRecord.vue"),
    },
    {
      path: "/chat/privateChatRecord",
      name: "私聊记录管理",
      component: () => import("../views/admin/PrivateChatRecord.vue"),
    },
  ],
});

export default router;
