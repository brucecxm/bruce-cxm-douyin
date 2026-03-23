import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '@/stores'; // 引入store，用于在全局守卫中获取角色/权限
import { authGuard, roleGuard, permissionGuard } from './guards';

Vue.use(VueRouter);

const routes = [
  {
    path: '/ad', // 新增广告页路由
    name: 'Ad',
    component: () => import('../views/Ad.vue'),
    meta: { isAd: true }
  },
  {
    path: '/wallet',
    name: 'Wallet',
    component: () => import('../views/shop/wallet.vue')
  },
  {
    path: '/test',
    name: 'Test',
    component: () => import('@/views/test/test.vue'),
    meta: {
      // requiresAuth: true,
      // roles: ['admin', 'user'],
      // permissions: ['home:view']
    }
  },
  {
    path: '/dashang',
    name: 'dashang',
    component: () => import('@/views/shangchuan/upload.vue'),
    meta: {
      requiresAuth: true, // 必须加这行，才会触发登录检查
      roles: ['admin', 'user'], // 需要的角色
      permissions: ['home:view'] // 需要的权限
    }
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
    path: '/shangchuandetail',
    name: 'ShangchuandetailVue',
    component: () => import('../views/shangchuan/shangchuandetail.vue')
  },
  {
    path: '/coupon',
    name: 'Coupon',
    component: () => import('../views/shop/coupon.vue'),
    meta: {
      requiresAuth: true,
      roles: ['admin', 'user'],
      permissions: ['home:view']
    }
  },
  {
    path: '/videodetail/:id',
    name: 'Videodetail',
    component: () => import('@/views/video/videodetail.vue')
  },
  {
    path: '/tongzhi',
    name: 'Tongzhi',
    component: () => import('@/views/xiaoxi/note.vue')
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
    path: '/shop/order',
    name: 'Order',
    component: () => import('../views/shop/order.vue'),
    meta: {
      requiresAuth: true,
      roles: ['admin', 'user']
    }
  },
  {
    path: '/shop/orderlist',
    name: 'OrderList',
    component: () => import('../views/shop/orderlist.vue'),
    meta: {
      requiresAuth: true,
      roles: ['admin', 'user']
    }
  },
  {
    path: '/shop/payment',
    name: 'Payment',
    component: () => import('../views/shop/payment.vue'),
    meta: {
      requiresAuth: true,
      roles: ['admin', 'user']
    }
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
    path: '/friend-requests',
    name: 'FriendRequests',
    component: () => import('../views/xiaoxi/friend-requests.vue')
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
    name: 'UserRelation',
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
    path: '/newshop',
    name: 'NewShopIndex',
    component: () => import('../views/index/App.vue')
  },
  {
    path: '/newshop/page1',
    name: 'NewShopPage1',
    component: () => import('../views/page1/App.vue')
  },
  {
    path: '/newshop/page2',
    name: 'NewShopPage2',
    component: () => import('../views/page2/App.vue')
  },
  {
    path: '/newshop/login',
    name: 'NewShopLogin',
    component: () => import('../views/login/App.vue')
  },
  {
    path: '/newshop/register',
    name: 'NewShopRegister',
    component: () => import('../views/register/App.vue')
  },
  {
    path: '/newshop/categories',
    name: 'NewShopCategories',
    component: () => import('../views/categories/App.vue')
  },
  {
    path: '/newshop/address',
    name: 'NewShopAddress',
    component: () => import('../views/address/App.vue')
  },
  {
    path: '/newshop/detail',
    name: 'NewShopDetail',
    component: () => import('../views/detail/App.vue')
  },
  {
    path: '/newshop/publish',
    name: 'NewShopPublish',
    component: () => import('../views/publish/App.vue')
  },
  {
    path: '/newshop/profile',
    name: 'NewShopProfile',
    component: () => import('../views/profile/App.vue')
  },
  {
    path: '/newshop/search',
    name: 'NewShopSearch',
    component: () => import('../views/search/App.vue')
  },
  {
    path: '/newshop/orders',
    name: 'NewShopOrders',
    component: () => import('../views/orders/App.vue')
  },
  {
    path: '/newshop/orderDetail',
    name: 'NewShopOrderDetail',
    component: () => import('../views/orderDetail/App.vue')
  },
  {
    path: '/newshop/favorites',
    name: 'NewShopFavorites',
    component: () => import('../views/favorites/App.vue')
  },
  {
    path: '/newshop/messages',
    name: 'NewShopMessages',
    component: () => import('../views/messages/App.vue')
  },
  {
    path: '/newshop/chat',
    name: 'NewShopChat',
    component: () => import('../views/chat/App.vue')
  },
  {
    path: '/newshop/myProducts',
    name: 'NewShopMyProducts',
    component: () => import('../views/myProducts/App.vue')
  },
  {
    path: '/newshop/settings',
    name: 'NewShopSettings',
    component: () => import('../views/settings/App.vue')
  },
  {
    path: '/newshop/cart',
    name: 'NewShopCart',
    component: () => import('../views/cart/App.vue')
  },
  {
    path: '/newshop/websocket-test',
    name: 'NewShopWebSocketTest',
    component: () => import('../views/websocket-test/App.vue')
  },
  {
    path: '/admin/resume-upload',
    name: 'ResumeUpload',
    component: () => import('../views/admin/ResumeUpload.vue'),
    meta: {
      requiresAuth: true,
      roles: ['admin']
    }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue')
  }
];

const router = new VueRouter({
  mode: 'history',
  base: import.meta.env.BASE_URL,
  routes
});

// 修复：全局前置守卫（只注册1个，动态判断逻辑）
router.beforeEach((to, from, next) => {
  // 1. 先处理「不需要认证」的路由（如登录页、广告页）
  if (to.path === '/login' || to.meta.isAd) {
    next(); // 直接放行
    return;
  }

  // 2. 对「需要认证」的路由（meta.requiresAuth: true），执行登录检查
  if (to.meta.requiresAuth) {
    const roles = store.getters['auth/getRoles'];
    // 2.1 未登录：跳转到登录页（带重定向参数）
    if (!roles || roles.length === 0) {
      next({ path: '/login', query: { redirect: to.fullPath } });
      return;
    }

    // 2.2 已登录：检查角色（如果路由有meta.roles）
    if (to.meta.roles && to.meta.roles.length > 0) {
      // 调用roleGuard，传入路由meta中的roles，执行角色检查
      const checkRole = roleGuard(to.meta.roles);
      // 执行角色守卫逻辑（这里需要手动传to/from/next，因为roleGuard返回的是守卫函数）
      checkRole(to, from, (roleNext) => {
        // 角色守卫返回403时，直接跳转；否则继续检查权限
        if (roleNext.path === '/403') {
          next(roleNext);
        } else {
          // 2.3 检查权限（如果路由有meta.permissions）
          if (to.meta.permissions && to.meta.permissions.length > 0) {
            const checkPerm = permissionGuard(to.meta.permissions);
            checkPerm(to, from, next); // 执行权限守卫，直接用全局的next
          } else {
            next(); // 没有权限要求，直接放行
          }
        }
      });
      return;
    }

    // 2.4 已登录，但路由没有角色要求：直接检查权限（如果有）
    if (to.meta.permissions && to.meta.permissions.length > 0) {
      const checkPerm = permissionGuard(to.meta.permissions);
      checkPerm(to, from, next);
      return;
    }

    // 2.5 已登录，且没有角色/权限要求：直接放行
    next();
  } else {
    // 3. 不需要认证的路由（没有meta.requiresAuth）：直接放行
    next();
  }
});

export default router;
