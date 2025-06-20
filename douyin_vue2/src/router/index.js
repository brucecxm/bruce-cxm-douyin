import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const router = new VueRouter({
  mode: 'history',
  base: import.meta.env.BASE_URL,
  routes: [
    {
      path: '/wallet',
      name: 'wallet',
      component: () => import('../views/shop/wallet.vue')
    },
    {
      path: '/test',
      name: 'testregister',
      component: () => import('@/views/test/testregister.vue')
    },
    {
      path: '/testone',
      name: 'test',
      component: () => import('@/views/test/testshop.vue')
    },
    {
      path: '/shopcat',
      name: 'shopcat',
      component: () => import('../views/shop/shopcat.vue')
    },
    {
      path: '/store',
      name: 'store',
      component: () => import('../views/shop/store.vue')
    },
    {
      path: '/searchshop',
      name: 'searchshop',
      component: () => import('../views/shop/searchshop.vue')
    },
    {
      path: '/shangchuan',
      name: 'shangchuanshop',
      component: () => import('../views/shangchuan/shangchuan.vue')
    },
    {
      path: '/shangchuandetail/',
      name: 'shangchuandetailVue',
      component: () => import('../views/shangchuan/shangchuandetail.vue')
    },
    {
      path: '/coupon',
      name: 'coupon',
      component: () => import('../views/shop/coupon.vue')
    },
    {
      path: '/videodetail',
      name: 'videodetail',
      component: () => import('@/views/video/videodetail.vue')
    },

    {
      path: '/testmap',
      name: 'testmap',
      component: () => import('../components/leftmap.vue')
    },
    {
      path: '/map',
      name: 'map',
      component: () => import('../views/gaode.vue')
    },
    {
      path: '/homeone',
      name: 'home',
      component: () => import('../views/one.vue')
    },
    {
      path: '/order', // 动态路由参数 :id
      name: 'order',
      component: () => import('../views/shop/order.vue')
    },
    {
      path: '/chongzhi/:userid', // 动态路由参数 :id
      name: 'chongzhi',
      component: () => import('../views/shop/chongzhi.vue')
    },
    {
      path: '/shopmessage/:userid', // 动态路由参数 :id
      name: 'shopmessage',
      component: () => import('../views/shop/shopmessage.vue')
    },
    {
      path: '/',
      name: 'homeone',
      component: () => import('../views/home.vue')
    },
    {
      path: '/authhome',
      name: 'authhome',
      component: () => import('../views/auth/auth.vue')
    },
    // {
    //   path: "/freevideo",
    //   name: "FreeVideoVue",
    //   component: () => import("../views/home/FreeVideo.vue"),
    // },
    {
      path: '/me',
      name: 'MeVue',
      component: () => import('../views/me/me.vue')
    },
    {
      path: '/shop',
      name: 'ShopVue',
      component: () => import('../views/shop/shop.vue')
    },
    {
      path: '/login',
      name: 'LoginVue',
      component: () => import('../views/login/login.vue')
    },
    {
      path: '/d',
      name: 'dVue',
      component: () => import('../views/testone/d.vue')
    },
    {
      path: '/search',
      name: 'SearchVue',
      component: () => import('../views/search/search.vue')
    },
    {
      path: '/xiaoxi',
      name: 'XiaoXiVue',
      component: () => import('../views/xiaoxi/xiaoxi.vue')
    },
    {
      path: '/xiaoxi',
      name: 'XiaoXiVue',
      component: () => import('../views/xiaoxi/xiaoxi.vue')
    },
    {
      path: '/chat',
      name: 'ChatVue',
      component: () => import('../views/chat/beifen.vue')
    },
    {
      path: '/chatyi',
      name: 'ChatyiVue',
      component: () => import('../views/chat/yiliao.vue')
    },
    {
      path: '/music/:musicid',
      name: 'MusicVue',
      component: () => import('../views/music/music.vue')
    },

    {
      path: '/auth/:userId',
      name: 'AuthVue',
      component: () => import('../views/auth/auth.vue')
    },

    {
      path: '/xiaodetail/:id',
      name: 'XiaoDetailVue',
      component: () => import('../views/xiaoxi/xiaoxitdetail.vue')
    },
    {
      path: '/transfer/:id',
      name: 'transferVue',
      component: () => import('../views/shop/transfer.vue')
    },

    {
      path: '/shopdetail/:id',
      name: 'ShopDetailVue',
      component: () => import('../views/shop/shopdetail.vue')
    },
    {
      path: '/setting',
      name: 'SettingVue',
      component: () => import('../views/setting.vue')
    },
    {
      path: '/shopSearchDetail',
      name: 'shopSearchDetailVue',
      component: () => import('../views/shop/shopSearchDetail.vue')
    },
    {
      path: '/friend',
      name: 'FriendVue',
      component: () => import('../views/me/friend.vue')
    },
    {
      path: '/handleFriend',
      name: 'handleFriend',
      component: () => import('../views/me/handlefriend.vue')
    },
    {
      path: '/editMeInfo',
      name: 'editMeInfoVue',
      component: () => import('../views/me/editMeInfo.vue')
    },
    {
      path: '/articledetail',
      name: 'Articledetail',
      component: () => import('../views/home/articledetail.vue')
    },
    {
      path: '/searchdetail',
      name: 'Searchdetail',
      component: () => import('../views/search/searchdetail.vue')
    }
    // Add more routes as needed
    // {
    //   path: '/about',
    //   name: 'about',
    //   component: () => import('../views/AboutView.vue')
    // }
  ]
});

export default router;
