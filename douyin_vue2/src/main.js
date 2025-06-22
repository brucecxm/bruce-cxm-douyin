import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import InfiniteScroll from 'vue-infinite-scroll';
import '@/assets/base.css';
import { createPinia, PiniaVuePlugin } from 'pinia';
import { Constant } from './constants/dict';

Vue.use(InfiniteScroll);
Vue.use(ElementUI);
Vue.use(PiniaVuePlugin);

const pinia = createPinia();
Vue.use(pinia); // ✅ 必须这一步

Vue.prototype.Constant = Constant;

if ('Notification' in window) {
  Notification.requestPermission().then((permission) => {
    console.log('通知权限:', permission);
  });
}

export const eventBus = new Vue();

new Vue({
  router,
  pinia,
  render: (h) => h(App)
}).$mount('#app');
