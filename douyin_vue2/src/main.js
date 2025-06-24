import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import InfiniteScroll from 'vue-infinite-scroll';
import '@/assets/base.css';

import Vuex from 'vuex'; // 引入 Vuex

import { Constant } from './constants/dict';

Vue.use(Vuex);
Vue.use(InfiniteScroll);
Vue.use(ElementUI);

Vue.prototype.Constant = Constant;

if ('Notification' in window) {
  Notification.requestPermission().then((permission) => {
    console.log('通知权限:', permission);
  });
}

export const eventBus = new Vue();

// 创建 Vuex store（这里先空，后面你填state/mutations等）
const store = new Vuex.Store({
  state: {
    // 你的全局状态
  },
  mutations: {
    // 你的同步修改
  },
  actions: {
    // 异步操作
  },
  getters: {
    // 计算属性
  }
});

new Vue({
  router,
  store, // 挂载 Vuex store
  render: (h) => h(App)
}).$mount('#app');
