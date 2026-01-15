<template>
  <div class="websocket-test-container">
    <h1>WebSocket测试页面</h1>

    <div class="input-section">
      <input
        type="text"
        v-model="wsUrl"
        placeholder="WebSocket服务器URL"
        class="url-input"
      />
      <select v-model="connectionType" class="connection-type-select">
        <option value="sockjs">SockJS (浏览器)</option>
        <option value="direct">直接WebSocket (Android)</option>
      </select>
      <button @click="connect" :disabled="isConnected" class="btn connect-btn">
        连接
      </button>
      <button
        @click="disconnect"
        :disabled="!isConnected"
        class="btn disconnect-btn"
      >
        断开
      </button>
    </div>

    <div class="message-section">
      <textarea
        v-model="message"
        placeholder="输入要发送的消息"
        class="message-input"
      ></textarea>
      <button @click="send" :disabled="!isConnected" class="btn send-btn">
        发送
      </button>
    </div>

    <div class="log-section">
      <h3>WebSocket日志</h3>
      <div class="log-container" ref="logContainer">
        <div v-for="(log, index) in logs" :key="index" class="log-item">
          <span class="log-time">{{ log.time }}</span>
          <span :class="['log-type', log.type]">{{ log.type }}</span>
          <span class="log-message">{{ log.message }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WebSocketTest',
  data() {
    return {
      wsUrl: 'http://1.94.131.16:7400/ws', // 使用本地后端SockJS服务
      message: '',
      logs: [],
      isConnected: false,
      stompClient: null,
      nativeWebSocket: null,
      isScriptLoaded: false,
      receiverId: 26, // 测试接收者ID
      senderId: 25, // 测试发送者ID
      connectionType: 'sockjs' // 连接类型：sockjs或direct
    };
  },
  mounted() {
    // 动态引入SockJS和STOMP.js
    this.loadScripts();
  },
  beforeUnmount() {
    // 组件销毁前断开WebSocket连接
    this.disconnect();
  },
  methods: {
    /**
     * 加载SockJS和STOMP.js脚本
     */
    loadScripts() {
      this.addLog('info', '正在检查SockJS和STOMP.js是否可用...');

      // 检查是否在Android环境
      const isAndroid = typeof window.Android !== 'undefined';

      if (isAndroid) {
        this.addLog('info', 'Android环境，将使用原生WebSocket');
        // Android环境下，使用原生WebSocket，不依赖SockJS和STOMP.js
        window.SockJS = null;
        window.Stomp = null;
        // 直接标记为可用
        this.isScriptLoaded = true;
      } else {
        // 浏览器环境下，动态加载脚本
        this.addLog('info', '浏览器环境，动态加载SockJS和STOMP.js...');

        // 加载SockJS
        const sockjsScript = document.createElement('script');
        sockjsScript.src =
          'https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js';
        sockjsScript.onload = () => {
          this.addLog('info', 'SockJS加载成功');

          // 加载STOMP.js
          const stompScript = document.createElement('script');
          stompScript.src =
            'https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js';
          stompScript.onload = () => {
            this.addLog('info', 'STOMP.js加载成功');
            this.isScriptLoaded = true;
          };
          stompScript.onerror = () => {
            this.addLog('error', 'STOMP.js加载失败');
            // 失败时也标记为可用，让用户可以尝试使用原生WebSocket
            this.isScriptLoaded = true;
          };
          document.head.appendChild(stompScript);
        };
        sockjsScript.onerror = () => {
          this.addLog('error', 'SockJS加载失败');
          // 失败时也标记为可用，让用户可以尝试使用原生WebSocket
          this.isScriptLoaded = true;
        };
        document.head.appendChild(sockjsScript);
      }
    },

    /**
     * 连接WebSocket
     */
    connect() {
      if (!this.wsUrl) {
        this.addLog('error', '请输入WebSocket服务器URL');
        return;
      }

      this.addLog('info', `正在连接到 ${this.wsUrl}`);

      // 检查是否在Android环境
      const isAndroid = typeof window.Android !== 'undefined';

      if (isAndroid) {
        // Android环境下，根据connectionType选择连接方式
        this.addLog('info', `使用Android ${this.connectionType === 'direct' ? '直接WebSocket' : 'SockJS'}连接`);

        try {
          let wsUrl;
          if (this.connectionType === 'direct') {
            // 直接WebSocket连接，使用/ws-direct端点
            wsUrl = this.wsUrl.replace('/ws', '/ws-direct').replace('http://', 'ws://').replace('https://', 'wss://');
            this.addLog('info', `直接WebSocket URL: ${wsUrl}`);
          } else {
            // SockJS连接，保持原逻辑
            wsUrl = this.wsUrl.replace('http://', 'ws://').replace('https://', 'wss://');
            this.addLog('info', `SockJS URL: ${wsUrl}`);
          }

          // 使用Android原生WebSocket连接
          const success = window.Android.connectWebSocket(wsUrl, 'websocketCallback');
          if (success) {
            this.isConnected = true;
            this.addLog('open', `Android ${this.connectionType === 'direct' ? '直接WebSocket' : 'SockJS'}连接成功`);
          } else {
            this.addLog('error', `Android ${this.connectionType === 'direct' ? '直接WebSocket' : 'SockJS'}连接失败`);
          }
        } catch (error) {
          this.addLog('error', `Android WebSocket连接出错: ${error.message}`);
        }
      } else {
        // 浏览器环境下，使用SockJS和STOMP
        this.addLog('info', '使用浏览器WebSocket连接');

        // 检查SockJS和STOMP是否可用
        if (window.SockJS && window.Stomp) {
          try {
            // 创建SockJS连接
            const sock = new window.SockJS(this.wsUrl);

            // 创建STOMP客户端
            this.stompClient = window.Stomp.over(sock);

            // 连接STOMP服务器
            this.stompClient.connect(
              {},
              (frame) => {
                this.isConnected = true;
                this.addLog('open', `WebSocket连接成功: ${frame}`);

                // 订阅消息主题
                this.stompClient.subscribe(
                  `/topic/messages/${this.senderId}`,
                  (message) => {
                    try {
                      const data = JSON.parse(message.body);
                      this.addLog(
                        'message',
                        `收到消息: ${JSON.stringify(data)}`
                      );
                    } catch (e) {
                      this.addLog('message', `收到原始消息: ${message.body}`);
                    }
                  }
                );

                this.addLog(
                  'info',
                  `已订阅主题: /topic/messages/${this.senderId}`
                );
              },
              (error) => {
                this.addLog('error', `WebSocket连接失败: ${error}`);
              }
            );
          } catch (error) {
            this.addLog('error', `连接出错: ${error.message}`);
          }
        } else {
          this.addLog('error', 'SockJS或STOMP.js不可用，尝试使用原生WebSocket');

          // 尝试使用原生WebSocket
          try {
            // 将http(s)转换为ws(wss)
            const wsUrl = this.wsUrl
              .replace('http://', 'ws://')
              .replace('https://', 'wss://');
            this.nativeWebSocket = new WebSocket(wsUrl);

            this.nativeWebSocket.onopen = () => {
              this.isConnected = true;
              this.addLog('open', '原生WebSocket连接成功');
            };

            this.nativeWebSocket.onmessage = (event) => {
              this.addLog('message', `收到消息: ${event.data}`);
            };

            this.nativeWebSocket.onerror = (error) => {
              this.addLog('error', `原生WebSocket错误: ${error}`);
            };

            this.nativeWebSocket.onclose = (event) => {
              this.isConnected = false;
              this.addLog(
                'closed',
                `原生WebSocket已关闭，代码: ${event.code}, 原因: ${event.reason}`
              );
            };
          } catch (error) {
            this.addLog('error', `原生WebSocket连接出错: ${error.message}`);
          }
        }
      }
    },

    /**
     * 断开WebSocket连接
     */
    disconnect() {
      this.addLog('info', '正在断开WebSocket连接');

      // 检查是否在Android环境
      const isAndroid = typeof window.Android !== 'undefined';

      if (isAndroid) {
        // Android环境下，使用原生WebSocket断开
        try {
          const success = window.Android.disconnectWebSocket();
          if (success) {
            this.isConnected = false;
            this.addLog('info', 'Android原生WebSocket连接已断开');
          } else {
            this.addLog('error', 'Android原生WebSocket断开失败');
          }
        } catch (error) {
          this.addLog(
            'error',
            `Android原生WebSocket断开出错: ${error.message}`
          );
        }
      } else {
        // 浏览器环境下，使用SockJS和STOMP断开
        if (this.stompClient && this.stompClient.connected) {
          this.stompClient.disconnect();
          this.isConnected = false;
          this.stompClient = null;
          this.addLog('info', 'WebSocket连接已断开');
        } else if (this.nativeWebSocket) {
          // 使用原生WebSocket断开
          if (this.nativeWebSocket.readyState === WebSocket.OPEN) {
            this.nativeWebSocket.close();
            this.addLog('info', '原生WebSocket连接已断开');
          }
          this.nativeWebSocket = null;
          this.isConnected = false;
        } else {
          this.addLog('error', 'WebSocket未连接或已断开');
        }
      }
    },

    /**
     * 发送WebSocket消息
     */
    send() {
      if (!this.message) {
        this.addLog('error', '请输入要发送的消息');
        return;
      }

      if (!this.isConnected) {
        this.addLog('error', 'WebSocket未连接');
        return;
      }

      // 检查是否在Android环境
      const isAndroid = typeof window.Android !== 'undefined';

      try {
        if (isAndroid) {
          // Android环境下，使用原生WebSocket发送
          this.addLog('send', `发送消息: ${this.message}`);
          const success = window.Android.sendWebSocketMessage(this.message);
          if (success) {
            this.addLog('info', '消息发送成功');
            // 清空输入框
            this.message = '';
          } else {
            this.addLog('error', '消息发送失败');
          }
        } else {
          // 浏览器环境下，使用SockJS和STOMP发送
          if (this.stompClient && this.stompClient.connected) {
            // 构建消息请求
            const messageRequest = {
              senderId: this.senderId,
              receiverId: this.receiverId,
              content: this.message
            };

            this.addLog('send', `发送消息: ${JSON.stringify(messageRequest)}`);

            // 发送消息
            this.stompClient.send(
              '/app/message/send',
              {},
              JSON.stringify(messageRequest)
            );

            // 清空输入框
            this.message = '';
          } else if (this.nativeWebSocket) {
            // 使用原生WebSocket发送
            this.addLog('send', `发送消息: ${this.message}`);
            this.nativeWebSocket.send(this.message);
            this.addLog('info', '消息发送成功');
            // 清空输入框
            this.message = '';
          } else {
            this.addLog('error', 'WebSocket未连接');
          }
        }
      } catch (error) {
        this.addLog('error', `发送消息失败: ${error.message}`);
      }
    },

    /**
     * 添加日志
     */
    addLog(type, message) {
      const now = new Date();
      const time = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}:${now.getSeconds().toString().padStart(2, '0')}`;

      this.logs.push({
        time,
        type,
        message
      });

      // 自动滚动到底部
      this.$nextTick(() => {
        const logContainer = this.$refs.logContainer;
        if (logContainer) {
          logContainer.scrollTop = logContainer.scrollHeight;
        }
      });
    }
  }
};
</script>

<style scoped>
.websocket-test-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.input-section {
  display: flex;
  margin-bottom: 20px;
}

.url-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 10px;
  font-size: 14px;
}

.connection-type-select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 10px;
  font-size: 14px;
  background-color: white;
  cursor: pointer;
}

.message-section {
  display: flex;
  margin-bottom: 20px;
}

.message-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 10px;
  font-size: 14px;
  resize: vertical;
  min-height: 100px;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  transition: background-color 0.3s;
}

.connect-btn {
  background-color: #4caf50;
  color: white;
}

.connect-btn:hover {
  background-color: #45a049;
}

.connect-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.disconnect-btn {
  background-color: #f44336;
  color: white;
}

.disconnect-btn:hover {
  background-color: #da190b;
}

.disconnect-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.send-btn {
  background-color: #2196f3;
  color: white;
}

.send-btn:hover {
  background-color: #0b7dda;
}

.send-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.log-section {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 10px;
}

.log-section h3 {
  margin-top: 0;
  color: #333;
}

.log-container {
  max-height: 400px;
  overflow-y: auto;
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  font-family: monospace;
  font-size: 12px;
}

.log-item {
  margin-bottom: 5px;
  padding: 5px;
  border-radius: 2px;
  background-color: white;
  border-left: 3px solid #ddd;
}

.log-time {
  color: #999;
  margin-right: 10px;
}

.log-type {
  font-weight: bold;
  margin-right: 10px;
  padding: 2px 5px;
  border-radius: 3px;
  font-size: 11px;
}

.log-type.open {
  color: #4caf50;
  background-color: #e8f5e8;
}

.log-type.message {
  color: #2196f3;
  background-color: #e3f2fd;
}

.log-type.send {
  color: #ff9800;
  background-color: #fff3e0;
}

.log-type.error {
  color: #f44336;
  background-color: #ffebee;
}

.log-type.info {
  color: #607d8b;
  background-color: #eceff1;
}

.log-type.closing,
.log-type.closed {
  color: #9c27b0;
  background-color: #f3e5f5;
}

.log-message {
  word-break: break-all;
}
</style>
