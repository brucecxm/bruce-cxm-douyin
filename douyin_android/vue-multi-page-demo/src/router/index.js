import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Index',
    component: () => import('../views/index/App.vue')
  },
  {
    path: '/page1',
    name: 'Page1',
    component: () => import('../views/page1/App.vue')
  },
  {
    path: '/page2',
    name: 'Page2',
    component: () => import('../views/page2/App.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/App.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/register/App.vue')
  },
  {
    path: '/categories',
    name: 'Categories',
    component: () => import('../views/categories/App.vue')
  },
  {
    path: '/address',
    name: 'Address',
    component: () => import('../views/address/App.vue')
  },
  {
    path: '/detail',
    name: 'Detail',
    component: () => import('../views/detail/App.vue')
  },
  {
    path: '/publish',
    name: 'Publish',
    component: () => import('../views/publish/App.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/profile/App.vue')
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/search/App.vue')
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('../views/orders/App.vue')
  },
  {
    path: '/orderDetail',
    name: 'OrderDetail',
    component: () => import('../views/orderDetail/App.vue')
  },
  {
    path: '/favorites',
    name: 'Favorites',
    component: () => import('../views/favorites/App.vue')
  },
  {
    path: '/messages',
    name: 'Messages',
    component: () => import('../views/messages/App.vue')
  },
  {
    path: '/chat',
    name: 'Chat',
    component: () => import('../views/chat/App.vue')
  },
  {
    path: '/myProducts',
    name: 'MyProducts',
    component: () => import('../views/myProducts/App.vue')
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('../views/settings/App.vue')
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/cart/App.vue')
  },
  {
    path: '/websocket-test',
    name: 'WebSocketTest',
    component: () => import('../views/websocket-test/App.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/'
  }
];

const router = new VueRouter({
  mode: 'history',
  base: import.meta.env.BASE_URL,
  routes
});

export default router;
