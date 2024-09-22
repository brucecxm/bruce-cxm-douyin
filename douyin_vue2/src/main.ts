import Vue from "vue";
import "@/assets/base.css";
import { createPinia, PiniaVuePlugin } from "pinia";
import App from "./App.vue";
import router from "./router";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
// 创建一个新的 Vue 实例作为事件总线
export const eventBus = new Vue();
Vue.use(ElementUI);
Vue.use(PiniaVuePlugin);
new Vue({
  router,
  pinia: createPinia(),
  render: (h) => h(App),
}).$mount("#app");
