import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import InfiniteScroll from 'vue-infinite-scroll';
import './assets/base.css';
import './assets/css/theme.css';
import { Constant } from './constants/dict';
import { initVersionCheck } from '@root/utils/versionManager';
Vue.config.productionTip = false;
Vue.use(InfiniteScroll);
Vue.use(ElementUI);

Vue.prototype.Constant = Constant;

if ('Notification' in window) {
  Notification.requestPermission().then((permission) => {
    console.log('通知权限:', permission);
  });
}
import store from '@/stores';
export const eventBus = new Vue();

new Vue({
  router,
  store, // 挂载 Vuex store
  render: (h) => h(App),
  // 注册 Android 原生方法的调用接口
  mounted() {
    if (window.Android) {
      Vue.prototype.$android = window.Android
    }

    // 初始化版本检查
    initVersionCheck()
  }
}).$mount('#app');
