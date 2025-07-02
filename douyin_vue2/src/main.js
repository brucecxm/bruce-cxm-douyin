import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import InfiniteScroll from 'vue-infinite-scroll';
import '@/assets/base.css';

import { Constant } from './constants/dict';

Vue.use(InfiniteScroll);
Vue.use(ElementUI);

Vue.prototype.Constant = Constant;

if ('Notification' in window) {
  Notification.requestPermission().then((permission) => {
    console.log('通知权限:', permission);
  });
}
import store from './stores';
export const eventBus = new Vue();

new Vue({
  router,
  store, // 挂载 Vuex store
  render: (h) => h(App)
}).$mount('#app');
