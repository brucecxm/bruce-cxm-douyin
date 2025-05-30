<template>
  <div>
    <div v-if="notifications.length" class="notifications">
      <div
        v-for="(notification, index) in notifications"
        :key="index"
        class="notification"
      >
        <div class="notification-header">
          <span class="notification-title">新通知</span>
          <button class="close-btn" @click="removeNotification(index)">
            ×
          </button>
        </div>
        <p class="notification-content">{{ notification }}</p>
      </div>
    </div>
    <button @click="openSocket">开启通知</button>
    <button @click="closeSocket">关闭通知</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      notifications: [], // 存储多个通知
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
        this.notifications.push(message); // 将新通知添加到通知列表中
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
      console.log('WebSocket已关闭');
    },

    removeNotification(index) {
      // 移除指定的通知
      this.notifications.splice(index, 1);
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
.notifications {
  margin: 20px;
}

.notification {
  background-color: #f8f8f8;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.3s ease;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.notification-title {
  font-weight: bold;
  color: #333;
}

.close-btn {
  border: none;
  background: none;
  font-size: 20px;
  color: #999;
  cursor: pointer;
  transition: color 0.3s ease;
}

.close-btn:hover {
  color: #ff6f61;
}

.notification-content {
  color: #555;
}

button {
  margin: 10px;
  padding: 10px 20px;
  border: none;
  background-color: #409eff;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #66b1ff;
}
</style>
