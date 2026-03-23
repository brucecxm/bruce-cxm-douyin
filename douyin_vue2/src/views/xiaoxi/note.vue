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
  </div>
</template>

<script>
import { eventBus } from '../../main';

export default {
  data() {
    return {
      notifications: [] // 存储多个通知
    };
  },
  created() {
    // 监听事件总线上的通知消息
    eventBus.$on('notification', (message) => {
      this.notifications.push(message);
    });
  },
  methods: {
    removeNotification(index) {
      // 移除指定的通知
      this.notifications.splice(index, 1);
    }
  },
  beforeDestroy() {
    // 移除事件监听器
    eventBus.$off('notification');
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
</style>
