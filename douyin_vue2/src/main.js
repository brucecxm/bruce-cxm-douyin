import Vue from "vue";
import "@/assets/base.css";
import { createPinia, PiniaVuePlugin } from "pinia";
import App from "./App.vue";
import router from "./router";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import TDesign from 'tdesign-vue';
// 引入组件库的少量全局样式变量
import 'tdesign-vue/es/style/index.css';

Vue.config.productionTip = false;

// 创建一个新的 Vue 实例作为事件总线
export const eventBus = new Vue();
Vue.use(TDesign);
Vue.use(ElementUI);
Vue.use(PiniaVuePlugin);
new Vue({
  router,
  pinia: createPinia(),
  render: (h) => h(App),
}).$mount("#app");
