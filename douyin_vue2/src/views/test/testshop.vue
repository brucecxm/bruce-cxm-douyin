<template>
  <div class="message-center">
    <div class="message-header">
      <h3>消息中心</h3>
      <span class="unread-count" v-if="unreadCount > 0">{{ unreadCount }}</span>
      <button @click="markAllAsRead" :disabled="unreadCount === 0">
        全部标为已读
      </button>
    </div>
    
    <div class="socket-status" :class="socketStatus">
      {{ socketStatusText }}
    </div>
    
    <div class="message-list">
      <div 
        v-for="message in messages" 
        :key="message.id" 
        class="message-item"
        :class="{ 'unread': !message.read }"
      >
        <div class="message-content">
          <h4>{{ message.title || '系统通知' }}</h4>
          <p>{{ message.content || '无内容' }}</p>
          <span class="timestamp">{{ formatTime(message.timestamp) }}</span>
        </div>
        <div class="message-actions">
          <button @click="markAsRead(message.id)" v-if="!message.read">
            标为已读
          </button>
        </div>
      </div>
    </div>
    
    <div class="empty-state" v-if="messages.length === 0">
      <p>暂无消息</p>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'pinia';
import { useMessageStore } from '@/stores/messageStore';

export default {
  name: 'MessageCenter',
  computed: {
    ...mapState(useMessageStore, [
      'messages',
      'unreadCount',
      'socketStatus'
    ]),
    socketStatusText() {
      const statusMap = {
        'closed': '已断开',
        'connecting': '连接中...',
        'open': '已连接'
      };
      return statusMap[this.socketStatus] || this.socketStatus;
    }
  },
  methods: {
    ...mapActions(useMessageStore, [
      'markAllAsRead'
    ]),
    markAsRead(messageId) {
      const message = this.messages.find(msg => msg.id === messageId);
      if (message) {
        message.read = true;
        this.unreadCount--;
      }
    },
    formatTime(timestamp) {
      const date = new Date(timestamp);
      return date.toLocaleString();
    }
  },
  created() {
    // 初始化WebSocket连接
    const messageStore = useMessageStore();
    messageStore.initWebSocket();
  },
  beforeDestroy() {
    // 组件销毁前关闭WebSocket连接
    const messageStore = useMessageStore();
    messageStore.closeWebSocket();
  }
};
</script>

<style scoped>
.message-center {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.unread-count {
  background-color: #ff4d4f;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
}

.socket-status {
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 12px;
  margin-bottom: 15px;
  text-align: center;
}

.socket-status.closed {
  background-color: #ffebee;
  color: #b71c1c;
}

.socket-status.connecting {
  background-color: #fff8e1;
  color: #ff6f00;
}

.socket-status.open {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message-item {
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #e8e8e8;
  transition: all 0.3s;
  display: flex;
  justify-content: space-between;
  gap: 15px;
}

.message-item.unread {
  border-left: 3px solid #1890ff;
  background-color: #fafafa;
}

.message-content {
  flex-grow: 1;
}

.message-content h4 {
  margin: 0 0 8px 0;
  color: #1f1f1f;
}

.message-content p {
  margin: 0 0 10px 0;
  color: #595959;
  font-size: 14px;
}

.timestamp {
  font-size: 12px;
  color: #8c8c8c;
}

.message-actions {
  display: flex;
  align-items: flex-start;
}

button {
  padding: 6px 12px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 12px;
}
