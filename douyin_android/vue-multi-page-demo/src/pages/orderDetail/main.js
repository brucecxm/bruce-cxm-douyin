import Vue from 'vue'
import App from './App.vue'

new Vue({
  render: h => h(App),
  // 注册 Android 原生方法的调用接口
  mounted() {
    if (window.Android) {
      Vue.prototype.$android = window.Android
    }
  }
}).$mount('#app')