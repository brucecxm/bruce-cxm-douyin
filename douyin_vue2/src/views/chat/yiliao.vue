<template>
  <div class="chat-container">
    <div class="messages" ref="messages">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message-row', msg.sender === username ? 'me' : 'other']"
      >
        <img class="avatar" :src="getAvatar(msg.sender)" alt="avatar" />
        <div class="message-bubble">
          <div class="content">{{ msg.content }}</div>
          <div class="time">{{ formatTime(msg.timestamp) }}</div>
        </div>
      </div>
    </div>

    <div class="input-group">
      <textarea
        v-model="text"
        placeholder="请输入消息，按回车发送，Shift+Enter换行"
        @keydown.enter.prevent="handleEnter"
        @keydown.shift.enter.stop
        rows="1"
        ref="input"
      ></textarea>
      <button @click="sendText" :disabled="!text.trim()">发送</button>
    </div>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export default {
  data() {
    return {
      username: '',
      receiver: '',
      text: '',
      messages: [],
      stompClient: null,
      connected: false
    };
  },
  mounted() {
    this.username = this.getMyUserId() || '';
    this.receiver = this.getReceiverId() || '';
    this.connect();
  },
  methods: {
    getMyUserId() {
      let userId = localStorage.getItem('myUserId');
      if (userId) return userId;
      const params = new URLSearchParams(window.location.search);
      userId = params.get('myUserId') || '';
      if (userId) localStorage.setItem('myUserId', userId);
      return userId;
    },
    getReceiverId() {
      let receiverId = localStorage.getItem('receiverId');
      if (receiverId) return receiverId;
      const params = new URLSearchParams(window.location.search);
      receiverId = params.get('receiverId') || '';
      if (receiverId) localStorage.setItem('receiverId', receiverId);
      return receiverId;
    },
    connect() {
      if (!this.username) {
        alert('未获取到用户名，请检查 URL 参数 myUserId');
        return;
      }
      if (!this.receiver) {
        alert('未获取到接收者用户名，请检查 URL 参数 receiverId');
        return;
      }
      const username = encodeURIComponent(this.username);
      const socket = new SockJS(`/api/ws?username=${username}`);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
        {},
        (frame) => {
          this.connected = true;
          this.stompClient.subscribe('/user/queue/messages', (msg) => {
            const message = JSON.parse(msg.body);
            message.timestamp = message.timestamp || Date.now(); // 确保有时间戳
            this.messages.push(message);
            this.scrollToBottom();
          });
        },
        (error) => {
          console.error('连接失败:', error);
        }
      );
    },
    sendText() {
      if (!this.text.trim()) return;
      const msg = {
        sender: this.username,
        receiver: this.receiver,
        content: this.text.trim(),
        type: 'text',
        timestamp: Date.now()
      };
      this.stompClient.send('/app/private-chat', {}, JSON.stringify(msg));
      this.messages.push(msg); // 立即显示发送消息
      this.text = '';
      this.scrollToBottom();
      this.$refs.input.style.height = 'auto'; // 重置textarea高度
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messages;
        if (container && container.scrollTo) {
          container.scrollTo({
            top: container.scrollHeight,
            behavior: 'smooth'
          });
        } else if (container) {
          // 兼容旧浏览器
          container.scrollTop = container.scrollHeight;
        }
      });
    },
    handleEnter(e) {
      if (!e.shiftKey) {
        this.sendText();
      } else {
        this.text += '\n';
      }
    },
    formatTime(timestamp) {
      const date = new Date(timestamp);
      return date.toLocaleTimeString([], {
        hour: '2-digit',
        minute: '2-digit'
      });
    },
    getAvatar(username) {
      // 这里可以改成真实头像地址或者根据用户名生成头像
      // 先用简单占位颜色头像
      console.log('获取头像', username);
      return 'http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280';
    }
  }
};
</script>

<style scoped>
.chat-container {
  max-width: 600px;
  margin: 30px auto;
  display: flex;
  flex-direction: column;
  height: 80vh;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: #fefefe;
  box-shadow: 0 4px 10px rgb(0 0 0 / 0.1);
  border-radius: 10px;
  padding: 15px;
}

.messages {
  flex-grow: 1;
  overflow-y: auto;
  padding: 10px 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin-bottom: 15px;
  box-shadow: inset 0 1px 3px rgb(0 0 0 / 0.1);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.message-row {
  display: flex;
  align-items: flex-end;
  gap: 10px;
}

.message-row.me {
  flex-direction: row-reverse;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  flex-shrink: 0;
}

.message-bubble {
  max-width: 65%;
  background-color: #ffffff;
  padding: 10px 14px;
  border-radius: 18px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  font-size: 16px;
  line-height: 1.4;
  position: relative;
  white-space: pre-wrap;
  word-wrap: break-word;
  user-select: text;
  transition: background-color 0.3s ease;
}

.message-row.me .message-bubble {
  background-color: #dcf8c6;
}

.time {
  font-size: 11px;
  color: #999;
  margin-top: 6px;
  text-align: right;
  user-select: none;
}

.input-group {
  display: flex;
  gap: 10px;
  align-items: center;
}

textarea {
  flex-grow: 1;
  padding: 10px 15px;
  font-size: 16px;
  border: 1.5px solid #ccc;
  border-radius: 25px;
  outline: none;
  resize: none;
  min-height: 38px;
  max-height: 120px;
  transition: border-color 0.3s ease;
  font-family: inherit;
}

textarea:focus {
  border-color: #007bff;
}

button {
  padding: 0 20px;
  height: 38px;
  font-size: 16px;
  border-radius: 25px;
  background-color: #07c160;
  color: white;
  border: none;
  cursor: pointer;
  user-select: none;
  transition: background-color 0.3s ease;
}

button:disabled {
  background-color: #a0cdfa;
  cursor: not-allowed;
}

button:not(:disabled):hover {
  background-color: #059e4a;
}
</style>
