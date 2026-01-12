import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '@/stores'; // 引入store，用于在全局守卫中获取角色/权限
import { authGuard, roleGuard, permissionGuard } from './guards';

Vue.use(VueRouter);

const routes = [
  { 
    path: '/',
    redirect: '/douyin/home'
  },
  {
    path: '/douyin',
    name: 'DouyinRoot',
    redirect: '/douyin/home',
    children: [
      {
        path: '/douyin/ad', // 新增广告页路由
        name: 'Ad',
        component: () => import('@/pages/douyin/views/Ad.vue'),
        meta: { isAd: true }
      },
      {
        path: '/douyin/wallet',
        name: 'Wallet',
        component: () => import('@/pages/douyin/views/shop/wallet.vue')
      },
      {
        path: '/douyin/test',
        name: 'Test',
        component: () => import('@/pages/douyin/views/test/test.vue'),
        meta: {
          // requiresAuth: true,
          // roles: ['admin', 'user'],
          // permissions: ['home:view']
        }
      },
      {
        path: '/douyin/dashang',
        name: 'dashang',
        component: () => import('@/pages/douyin/views/shangchuan/upload.vue'),
        meta: {
          requiresAuth: true, // 必须加这行，才会触发登录检查
          roles: ['admin', 'user'], // 需要的角色
          permissions: ['home:view'] // 需要的权限
        }
      },
      {
        path: '/douyin/testone',
        name: 'TestOne',
        component: () => import('@/pages/douyin/views/test/testshop.vue')
      },
      {
        path: '/douyin/shopcat',
        name: 'Shopcat',
        component: () => import('@/pages/douyin/views/shop/shopcat.vue')
      },
      {
        path: '/douyin/store',
        name: 'Store',
        component: () => import('@/pages/douyin/views/shop/store.vue')
      },
      {
        path: '/douyin/searchshop',
        name: 'Searchshop',
        component: () => import('@/pages/douyin/views/shop/searchshop.vue')
      },
      {
        path: '/douyin/shangchuan',
        name: 'Shangchuanshop',
        component: () => import('@/pages/douyin/views/shangchuan/shangchuan.vue')
      },
      {
        path: '/douyin/shangchuandetail',
        name: 'ShangchuandetailVue',
        component: () => import('@/pages/douyin/views/shangchuan/shangchuandetail.vue')
      },
      {
        path: '/douyin/coupon',
        name: 'Coupon',
        component: () => import('@/pages/douyin/views/shop/coupon.vue'),
        meta: {
          requiresAuth: true,
          roles: ['admin', 'user'],
          permissions: ['home:view']
        }
      },
      {
        path: '/douyin/videodetail',
        name: 'Videodetail',
        component: () => import('@/pages/douyin/views/video/videodetail.vue')
      },
      {
        path: '/douyin/testmap',
        name: 'Testmap',
        component: () => import('@/pages/douyin/components/leftmap.vue')
      },
      {
        path: '/douyin/map',
        name: 'Map',
        component: () => import('@/pages/douyin/views/gaode.vue')
      },
      {
        path: '/douyin/homeone',
        name: 'Home',
        component: () => import('@/pages/douyin/views/one.vue')
      },
      {
        path: '/douyin/shop/order',
        name: 'Order',
        component: () => import('@/pages/douyin/views/shop/order.vue'),
        meta: {
          requiresAuth: true,
          roles: ['admin', 'user']
        }
      },
      {
        path: '/douyin/shop/orderlist',
        name: 'OrderList',
        component: () => import('@/pages/douyin/views/shop/orderlist.vue'),
        meta: {
          requiresAuth: true,
          roles: ['admin', 'user']
        }
      },
      {
        path: '/douyin/shop/payment',
        name: 'Payment',
        component: () => import('@/pages/douyin/views/shop/payment.vue'),
        meta: {
          requiresAuth: true,
          roles: ['admin', 'user']
        }
      },
      {
        path: '/douyin/chongzhi/:userid',
        name: 'Chongzhi',
        component: () => import('@/pages/douyin/views/shop/chongzhi.vue')
      },
      {
        path: '/douyin/shopmessage/:userid',
        name: 'Shopmessage',
        component: () => import('@/pages/douyin/views/shop/shopmessage.vue')
      },
      {
        path: '/douyin/home',
        name: 'Homeone',
        component: () => import('@/pages/douyin/views/home.vue')
      },
      {
        path: '/douyin/authhome',
        name: 'Authhome',
        component: () => import('@/pages/douyin/views/auth/auth.vue')
      },
      {
        path: '/douyin/me',
        name: 'MeVue',
        component: () => import('@/pages/douyin/views/me/me.vue')
      },
      {
        path: '/douyin/shop',
        name: 'ShopVue',
        component: () => import('@/pages/douyin/views/shop/shop.vue')
      },
      {
        path: '/douyin/login',
        name: 'LoginVue',
        component: () => import('@/pages/douyin/views/login/login.vue')
      },
      {
        path: '/douyin/d',
        name: 'DVue',
        component: () => import('@/pages/douyin/views/testone/d.vue')
      },
      {
        path: '/douyin/search',
        name: 'SearchVue',
        component: () => import('@/pages/douyin/views/search/search.vue')
      },
      {
        path: '/douyin/xiaoxi',
        name: 'XiaoXiVue',
        component: () => import('@/pages/douyin/views/xiaoxi/xiaoxi.vue')
      },
      {
        path: '/douyin/chat',
        name: 'ChatVue',
        component: () => import('@/pages/douyin/views/chat/beifen.vue')
      },
      {
        path: '/douyin/chatyi',
        name: 'ChatyiVue',
        component: () => import('@/pages/douyin/views/chat/yiliao.vue')
      },
      {
        path: '/douyin/music/:musicid',
        name: 'MusicVue',
        component: () => import('@/pages/douyin/views/music/music.vue')
      },
      {
        path: '/douyin/auth/:userId',
        name: 'AuthVue',
        component: () => import('@/pages/douyin/views/auth/auth.vue')
      },
      {
        path: '/douyin/xiaodetail/:id',
        name: 'XiaoDetailVue',
        component: () => import('@/pages/douyin/views/xiaoxi/xiaoxitdetail.vue')
      },
      {
        path: '/douyin/transfer/:id',
        name: 'TransferVue',
        component: () => import('@/pages/douyin/views/shop/transfer.vue')
      },
      {
        path: '/douyin/shopdetail/:id',
        name: 'ShopDetailVue',
        component: () => import('@/pages/douyin/views/shop/shopdetail.vue')
      },
      {
        path: '/douyin/setting',
        name: 'SettingVue',
        component: () => import('@/pages/douyin/views/setting.vue')
      },
      {
        path: '/douyin/shopSearchDetail',
        name: 'ShopSearchDetailVue',
        component: () => import('@/pages/douyin/views/shop/shopSearchDetail.vue')
      },
      {
        path: '/douyin/friend',
        name: 'FriendVue',
        component: () => import('@/pages/douyin/views/me/friend.vue')
      },
      {
        path: '/douyin/handleFriend',
        name: 'HandleFriend',
        component: () => import('@/pages/douyin/views/me/handlefriend.vue')
      },
      {
        path: '/douyin/UserRelation',
        name: 'UserRelation',
        component: () => import('@/pages/douyin/views/user/UserRelation.vue')
      },
      {
        path: '/douyin/editMeInfo',
        name: 'EditMeInfoVue',
        component: () => import('@/pages/douyin/views/me/editMeInfo.vue')
      },
      {
        path: '/douyin/permissionRoleUser',
        name: 'permissionRoleUser',
        component: () => import('@/pages/douyin/views/user/PermissionRoleUser.vue')
      },
      {
        path: '/douyin/articledetail',
        name: 'Articledetail',
        component: () => import('@/pages/douyin/views/home/articledetail.vue')
      },
      {
        path: '/douyin/searchdetail',
        name: 'Searchdetail',
        component: () => import('@/pages/douyin/views/search/searchdetail.vue')
      },
      {
        path: '/douyin/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('@/pages/douyin/views/NotFound.vue')
      }
    ]
  },
  // 其他页面路由
  {
    path: '/index',
    name: 'Index',
    component: () => import('@/pages/index/App.vue')
  },
  {
    path: '/page1',
    name: 'Page1',
    component: () => import('@/pages/page1/App.vue')
  },
  {
    path: '/page2',
    name: 'Page2',
    component: () => import('@/pages/page2/App.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/login/App.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/pages/register/App.vue')
  },
  {
    path: '/categories',
    name: 'Categories',
    component: () => import('@/pages/categories/App.vue')
  },
  {
    path: '/address',
    name: 'Address',
    component: () => import('@/pages/address/App.vue')
  },
  {
    path: '/detail',
    name: 'Detail',
    component: () => import('@/pages/detail/App.vue')
  },
  {
    path: '/publish',
    name: 'Publish',
    component: () => import('@/pages/publish/App.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/pages/profile/App.vue')
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/pages/search/App.vue')
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('@/pages/orders/App.vue')
  },
  {
    path: '/orderDetail',
    name: 'OrderDetail',
    component: () => import('@/pages/orderDetail/App.vue')
  },
  {
    path: '/favorites',
    name: 'Favorites',
    component: () => import('@/pages/favorites/App.vue')
  },
  {
    path: '/messages',
    name: 'Messages',
    component: () => import('@/pages/messages/App.vue')
  },
  {
    path: '/chat',
    name: 'Chat',
    component: () => import('@/pages/chat/App.vue')
  },
  {
    path: '/myProducts',
    name: 'MyProducts',
    component: () => import('@/pages/myProducts/App.vue')
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('@/pages/settings/App.vue')
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('@/pages/cart/App.vue')
  },
  {
    path: '/websocket-test',
    name: 'WebSocketTest',
    component: () => import('@/pages/websocket-test/App.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'GlobalNotFound',
    component: () => import('@/pages/douyin/views/NotFound.vue')
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

// 修复：全局前置守卫（只注册1个，动态判断逻辑）
router.beforeEach((to, from, next) => {
  // 1. 先处理「不需要认证」的路由（如登录页、广告页）
  if (to.path === '/login' || to.path === '/douyin/login' || to.meta.isAd) {
    next(); // 直接放行
    return;
  }

  // 2. 对「需要认证」的路由（meta.requiresAuth: true），执行登录检查
  if (to.meta.requiresAuth) {
    const roles = store.getters['auth/getRoles'];
    // 2.1 未登录：跳转到登录页（带重定向参数）
    if (!roles || roles.length === 0) {
      next({ path: '/douyin/login', query: { redirect: to.fullPath } });
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