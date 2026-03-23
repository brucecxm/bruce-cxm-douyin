import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import InfiniteScroll from 'vue-infinite-scroll';
import '@/assets/base.css';
import './assets/css/theme.css';
import { Constant } from './constants/dict';
// 导入 Font Awesome
// 注册所有图标
// 注册为全局组件
Vue.config.productionTip = false;
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

// WebSocket连接管理
let socket = null;
const sender = '匿名用户';

// 初始化WebSocket连接
function initWebSocket() {
  // 如果已经有 WebSocket 连接，则先关闭
  if (socket) {
    socket.close();
  }

  // 创建 WebSocket 连接
  socket = new WebSocket(
    '/api/user/wsnote?username=' + encodeURIComponent(sender)
  );

  // 连接打开时的回调
  socket.onopen = () => {
    console.log('WebSocket连接已建立');
    socket.send('Hello, server!'); // 向服务器发送初始消息
  };

  // 收到消息时的回调
  socket.onmessage = (event) => {
    const message = event.data;
    console.log('收到服务器消息:', message);
    // 使用ElementUI的Message组件显示通知
    Vue.prototype.$message({
      message: message,
      type: 'info',
      duration: 3000
    });
    // 通过事件总线发送通知
    eventBus.$emit('notification', message);
  };

  // 连接关闭时的回调
  socket.onclose = (event) => {
    console.log('WebSocket连接已关闭', event);
    // 尝试重连
    setTimeout(initWebSocket, 3000);
  };

  // 连接出错时的回调
  socket.onerror = (error) => {
    console.error('WebSocket发生错误', error);
  };
}

// 应用启动时初始化WebSocket连接
initWebSocket();

// 全局方法，用于关闭WebSocket连接
Vue.prototype.closeWebSocket = function () {
  if (socket) {
    socket.close();
    socket = null;
  }
  console.log('WebSocket已关闭');
};

new Vue({
  router,
  store, // 挂载 Vuex store
  render: (h) => h(App)
}).$mount('#app');
