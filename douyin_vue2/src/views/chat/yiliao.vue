<!-- http://localhost:5173/chatyi?myUserId=146&receiverId=147 -->
<!-- http://localhost:5173/chatyi?myUserId=147&receiverId=146 -->
<template>
  <div class="chat-container">
    <div class="messages" ref="messages">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message-row', msg.sender === myId ? 'me' : 'other']"
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
import { mapState } from 'vuex';
import Stomp from 'stompjs';

export default {
  data() {
    return {
      myInfo: {}, // 自己信息
      receiverInfo: {}, // 对方信息
      myId: '',
      receiver: '',
      text: '',
      messages: [],
      stompClient: null,
      connected: false
    };
  },
  computed: {
    // 映射 userInfo 模块的 state（需指定模块名）
    ...mapState('userInfo', ['userInfo'])
    // 映射后可直接通过 this.userInfo 获取数据
  },
  mounted() {
    debugger;
    console.log('组件创建时的用户信息：', this.userInfo);
    this.myId = this.userInfo.id;
    this.receiver = this.$route.query.id;
    if (!this.myId || !this.receiver) {
      this.$message.error('系统错误，请联系管理员');
      return; // 终止后续逻辑
    }
    this.connect();
  },
  methods: {
    getAvatar(sender) {
      // 根据 sender 判断返回头像
      if (sender == this.myId) {
        return (
          this.myInfo.avatar ||
          'http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        );
      } else {
        return (
          this.receiverInfo.avatar ||
          'http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        );
      }
    },
    connect() {
      if (!this.myId) {
        alert('未获取到用户名，请检查 URL 参数 myUserId');
        return;
      }
      if (!this.receiver) {
        alert('未获取到接收者用户名，请检查 URL 参数 receiverId');
        return;
      }
      const myId = encodeURIComponent(this.myId);
      const socket = new SockJS(`/api/user/ws?myId=${myId}`);
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
        sender: this.myId,
        receiver: this.receiver,
        content: this.text.trim(),
        type: 'text',
        timestamp: Date.now()
      };
      debugger;
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
.chat-header {
  display: flex;
  align-items: center;
  padding: 12px 0 18px 0;
  border-bottom: 1px solid #eee;
  background: #fff;
  position: sticky;
  top: 0;
  z-index: 10;
}
.header-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  margin-right: 12px;
  border: 1px solid #eee;
}
.header-name {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}
</style>
