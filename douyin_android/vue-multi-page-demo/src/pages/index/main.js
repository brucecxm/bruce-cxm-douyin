import { createApp } from 'vue'
import App from './App.vue'
import { initVersionCheck } from '../../utils/versionManager'

const app = createApp(App)

// 注册 Android 原生方法的调用接口
if (window.Android) {
  app.config.globalProperties.$android = window.Android
}

// 初始化版本检查
initVersionCheck()

app.mount('#app')
