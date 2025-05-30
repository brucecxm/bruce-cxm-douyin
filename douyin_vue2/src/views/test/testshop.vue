<template>
  <div>
    <div v-if="notification" class="notification">
      <p>{{ notification }}</p>
    </div>
    <button @click="openSocket">开启通知</button>
    <button @click="closeSocket">关闭通知</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      notification: null,
      socket: null,
      sender: '匿名用户'
    };
  },
  methods: {
    openSocket() {
      // 如果已经有 WebSocket 连接，则先关闭
      if (this.socket) {
        this.socket.close();
      }

      // 创建 WebSocket 连接
      this.socket = new WebSocket(
        '/api/user/wsnote?username=' + encodeURIComponent(this.sender)
      );

      // 连接打开时的回调
      this.socket.onopen = () => {
        console.log('WebSocket连接已建立');
        this.socket.send('Hello, server!'); // 向服务器发送初始消息
      };

      // 收到消息时的回调
      this.socket.onmessage = (event) => {
        const message = event.data;
        console.log('收到服务器消息:', message);
        this.notification = message; // 将消息存储到 data 中
      };

      // 连接关闭时的回调
      this.socket.onclose = (event) => {
        console.log('WebSocket连接已关闭', event);
      };

      // 连接出错时的回调
      this.socket.onerror = (error) => {
        console.error('WebSocket发生错误', error);
      };
    },

    closeSocket() {
      // 关闭 WebSocket 连接
      if (this.socket) {
        this.socket.close();
        this.socket = null; // 清空 WebSocket 实例
      }
      this.notification = null; // 清空通知
    }
  },

  beforeDestroy() {
    // 确保在组件销毁时关闭 WebSocket 连接
    if (this.socket) {
      this.socket.close();
    }
  }
};
</script>

<style scoped>
.notification {
  padding: 10px;
  background-color: #ff9800;
  color: white;
  margin-bottom: 20px;
  border-radius: 5px;
}
button {
  margin: 10px;
}
</style>
