import Vue from 'vue'
import App from './App.vue'
import { initVersionCheck } from '@root/utils/versionManager'

new Vue({
  render: h => h(App),
  // 注册 Android 原生方法的调用接口
  mounted() {
    if (window.Android) {
      Vue.prototype.$android = window.Android
    }

    // 初始化版本检查
    initVersionCheck()
  }
}).$mount('#app')
