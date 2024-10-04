import Vue from "vue";
import "@/assets/base.css";
import { createPinia, PiniaVuePlugin } from "pinia";
import App from "./App.vue";
import router from "./router";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

export const EventBus = new Vue();
Vue.use(ElementUI);
Vue.use(PiniaVuePlugin);
new Vue({
  router,
  pinia: createPinia(),
  render: (h) => h(App),
}).$mount("#app");
