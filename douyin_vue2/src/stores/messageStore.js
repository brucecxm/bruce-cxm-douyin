import { defineStore } from 'pinia';

export const useMessageStore = defineStore('message', {
  state: () => ({
    messages: [],
    unreadCount: 0,
    socket: null,
    socketStatus: 'closed', // closed, connecting, open
    reconnectTimer: null,
    reconnectCount: 0,
    maxReconnectCount: 5
  }),
  getters: {
    getMessages: (state) => state.messages,
    getUnreadCount: (state) => state.unreadCount,
    getSocketStatus: (state) => state.socketStatus
  },
  actions: {
    initWebSocket() {
      try {
        this.socketStatus = 'connecting';
        this.socket = new WebSocket(
          process.env.VUE_APP_WS_URL || 'ws://localhost:8080/ws'
        );

        this.socket.onopen = () => {
          this.socketStatus = 'open';
          this.reconnectCount = 0;
          clearInterval(this.reconnectTimer);
          console.log('WebSocket连接成功');
        };

        this.socket.onmessage = (event) => {
          const message = JSON.parse(event.data);
          this.addMessage(message);
        };

        this.socket.onclose = (event) => {
          this.socketStatus = 'closed';
          console.log('WebSocket连接已关闭', event);
          this.scheduleReconnect();
        };

        this.socket.onerror = (error) => {
          this.socketStatus = 'closed';
          console.error('WebSocket发生错误', error);
          if (this.socket) {
            this.socket.close();
          }
          this.scheduleReconnect();
        };
      } catch (error) {
        console.error('初始化WebSocket失败', error);
        this.scheduleReconnect();
      }
    },

    scheduleReconnect() {
      if (this.reconnectCount < this.maxReconnectCount) {
        clearInterval(this.reconnectTimer);
        this.reconnectCount++;
        const delay = Math.min(3000, this.reconnectCount * 1000);

        this.reconnectTimer = setTimeout(() => {
          console.log(
            `尝试重新连接WebSocket (${this.reconnectCount}/${this.maxReconnectCount})`
          );
          this.initWebSocket();
        }, delay);
      } else {
        console.error('达到最大重连次数，停止尝试');
      }
    },

    addMessage(message) {
      this.messages.unshift({
        ...message,
        id: Date.now().toString(),
        timestamp: new Date().toISOString(),
        read: false
      });
      this.unreadCount++;
      this.notifyNewMessage(message);
    },

    markAllAsRead() {
      this.messages.forEach((message) => {
        message.read = true;
      });
      this.unreadCount = 0;
    },

    notifyNewMessage(message) {
      // 这里可以添加通知逻辑，如声音、震动、桌面通知等
      console.log('收到新消息:', message);

      // 浏览器通知示例
      if (Notification.permission === 'granted') {
        new Notification('新消息提醒', {
          body: message.content || '您有一条新消息',
          icon: '/logo.png'
        });
      }
    },

    closeWebSocket() {
      if (this.socket) {
        clearInterval(this.reconnectTimer);
        this.socket.close();
        this.socket = null;
        this.socketStatus = 'closed';
      }
    }
  }
});
