// src/router/router.js
import Vue from 'vue';
import VueRouter from 'vue-router';
import { authGuard, roleGuard, permissionGuard } from './guards';

Vue.use(VueRouter);

const routes = [
  {
    path: '/ad', // 新增广告页路由
    name: 'Ad',
    component: () => import('../views/Ad.vue'), // 假设广告组件路径
    meta: { isAd: true } // 标记为广告页
  },
  {
    path: '/wallet',
    name: 'Wallet', // 修正路由名称（原名称与广告路由冲突）
    component: () => import('../views/shop/wallet.vue'),
    meta: {
      requiresAuth: true, // 需要登录
      roles: ['admin', 'user'], // 需要的角色（满足任意一个即可）
      permissions: ['home:view'] // 需要的权限（满足任意一个即可）
    }
  },
  {
    path: '/test',
    name: 'TestRegister',
    component: () => import('@/views/test/testregister.vue'),
    meta: {
      requiresAuth: true, // 需要登录
      roles: ['admin', 'user'], // 需要的角色（满足任意一个即可）
      permissions: ['home:view'] // 需要的权限（满足任意一个即可）
    }
  },
  {
    path: '/dashang',
    name: 'dashang',
    component: () => import('@/views/shangchuan/upload.vue')
  },
  {
    path: '/testone',
    name: 'Test',
    component: () => import('@/views/test/testshop.vue')
  },
  {
    path: '/shopcat',
    name: 'Shopcat',
    component: () => import('../views/shop/shopcat.vue')
  },
  {
    path: '/store',
    name: 'Store',
    component: () => import('../views/shop/store.vue')
  },
  {
    path: '/searchshop',
    name: 'Searchshop',
    component: () => import('../views/shop/searchshop.vue')
  },
  {
    path: '/shangchuan',
    name: 'Shangchuanshop',
    component: () => import('../views/shangchuan/shangchuan.vue')
  },
  {
    path: '/shangchuandetail', // 移除末尾多余斜杠
    name: 'ShangchuandetailVue',
    component: () => import('../views/shangchuan/shangchuandetail.vue')
  },
  {
    path: '/coupon',
    name: 'Coupon',
    component: () => import('../views/shop/coupon.vue'),
    meta: {
      requiresAuth: true, // 需要登录
      roles: ['admin', 'user'], // 需要的角色（满足任意一个即可）
      permissions: ['home:view'] // 需要的权限（满足任意一个即可）
    }
  },
  {
    path: '/videodetail',
    name: 'Videodetail',
    component: () => import('@/views/video/videodetail.vue')
  },

  {
    path: '/testmap',
    name: 'Testmap',
    component: () => import('../components/leftmap.vue')
  },
  {
    path: '/map',
    name: 'Map',
    component: () => import('../views/gaode.vue')
  },
  {
    path: '/homeone',
    name: 'Home',
    component: () => import('../views/one.vue')
  },
  {
    path: '/order',
    name: 'Order',
    component: () => import('../views/shop/order.vue')
  },
  {
    path: '/chongzhi/:userid',
    name: 'Chongzhi',
    component: () => import('../views/shop/chongzhi.vue')
  },
  {
    path: '/shopmessage/:userid',
    name: 'Shopmessage',
    component: () => import('../views/shop/shopmessage.vue')
  },
  {
    path: '/',
    name: 'Homeone',
    component: () => import('../views/home.vue')
  },
  {
    path: '/authhome',
    name: 'Authhome',
    component: () => import('../views/auth/auth.vue')
  },
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
    name: 'DVue',
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
    name: 'TransferVue',
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
    name: 'ShopSearchDetailVue',
    component: () => import('../views/shop/shopSearchDetail.vue')
  },
  {
    path: '/friend',
    name: 'FriendVue',
    component: () => import('../views/me/friend.vue')
  },
  {
    path: '/handleFriend',
    name: 'HandleFriend',
    component: () => import('../views/me/handlefriend.vue')
  },
  {
    path: '/UserRelation',
    name: 'HandleFriend',
    component: () => import('../views/user/UserRelation.vue')
  },
  {
    path: '/editMeInfo',
    name: 'EditMeInfoVue',
    component: () => import('../views/me/editMeInfo.vue')
  },
  {
    path: '/permissionRoleUser',
    name: 'permissionRoleUser',
    component: () => import('../views/user/PermissionRoleUser.vue')
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
  },
  {
    path: '/:pathMatch(.*)*', // 新增404路由
    name: 'NotFound',
    component: () => import('../views/NotFound.vue')
  }
];

const router = new VueRouter({
  mode: 'history',
  routes
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const authResult = authGuard(to, from, next);
    if (authResult !== true) return;

    if (to.meta.roles) {
      const roleResult = roleGuard(to.meta.roles)(to, from, next);
      if (roleResult !== true) return;
    }

    if (to.meta.permissions) {
      const permissionResult = permissionGuard(to.meta.permissions)(
        to,
        from,
        next
      );
      if (permissionResult !== true) return;
    }
  }
  next();
});

export default router;
