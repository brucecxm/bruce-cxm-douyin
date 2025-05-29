<template>
  <div class="chat-container">
    <h2 class="room-title">群聊：{{ roomId }}</h2>

    <div v-if="connected" class="chat-content">
      <!-- 消息展示区域 -->
      <div class="messages">
        <div
          v-for="msg in messages"
          :key="msg.id"
          :class="['message-item', msg.sender === sender ? 'me' : 'other']"
        >
          <img class="avatar" :src="getAvatar(msg.sender)" />

          <div class="bubble-wrapper">
            <div class="nickname">{{ msg.sender }}</div>
            <div class="bubble">
              <div v-if="msg.type === 'text'" class="text-msg">
                {{ msg.messageContent }}
              </div>
              <div v-else-if="msg.type === 'image'">
                <img :src="msg.fileUrl" class="img-msg" />
              </div>
              <div v-else-if="msg.type === 'video'">
                <video :src="msg.fileUrl" controls class="video-msg" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 发送区域 -->
      <div class="input-area">
        <input v-model="text" placeholder="说点什么..." />
        <button @click="sendText">发送</button>
        <input type="file" @change="uploadFile" />
      </div>
    </div>

    <div v-else class="connecting">正在连接聊天室...</div>
  </div>
</template>
<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from '@/utils/request';

export default {
  data() {
    return {
      roomId: '',
      sender: '',
      text: '',
      messages: [],
      stompClient: null,
      connected: false
    };
  },
  mounted() {
    const params = new URLSearchParams(window.location.search);
    this.roomId = params.get('roomId') || 'default';
    this.sender = params.get('username') || '匿名用户';
    this.connect();
  },
  methods: {
    connect() {
      const socket = new SockJS(
        `/api/ws?username=${encodeURIComponent(this.sender)}`
      );
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, () => {
        this.connected = true;
        this.stompClient.subscribe(`/topic/messages/${this.roomId}`, (msg) => {
          const message = JSON.parse(msg.body);
          if (!message.id) {
            message.id = Date.now() + Math.random();
          }
          this.messages.push(message);
        });
      });
    },
    sendText() {
      if (!this.text.trim()) return;
      const msg = {
        sender: this.sender,
        type: 'text',
        messageContent: this.text
      };
      this.stompClient.send(
        `/app/chat/${this.roomId}`,
        { 'content-type': 'application/json' },
        JSON.stringify(msg)
      );
      this.text = '';
    },
    async uploadFile(event) {
      const file = event.target.files[0];
      if (!file) return;

      const formData = new FormData();
      formData.append('file', file);

      const res = await axios.post('/user/file/upload', formData);
      const url = res.data.url;

      const type = file.type.startsWith('video') ? 'video' : 'image';
      const msg = {
        sender: this.sender,
        type,
        fileUrl: url
      };

      this.stompClient.send(
        `/app/chat/${this.roomId}`,
        { 'content-type': 'application/json' },
        JSON.stringify(msg)
      );
    },
    getAvatar(sender) {
      // 这里你可以根据 sender 获取用户头像，这里我们用默认头像
      return 'http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280';
    }
  }
};
</script>
<style scoped>
.chat-container {
  max-width: 700px;
  margin: 20px auto;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 10px;
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
}

.room-title {
  text-align: center;
  margin-bottom: 15px;
}

.connecting {
  text-align: center;
  font-size: 16px;
  color: gray;
}

.chat-content {
  display: flex;
  flex-direction: column;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  max-height: 500px;
}

.message-item {
  display: flex;
  margin-bottom: 12px;
  align-items: flex-end;
}

.message-item.other {
  flex-direction: row;
}

.message-item.me {
  flex-direction: row-reverse;
}

.avatar {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  margin: 0 8px;
}

.bubble-wrapper {
  max-width: 65%;
}

.nickname {
  font-size: 12px;
  color: #666;
  margin-bottom: 2px;
}

.bubble {
  border-radius: 10px;
  padding: 8px 12px;
  word-break: break-word;
  background-color: white;
}

.message-item.me .bubble {
  background-color: #a7e855;
  color: #000;
}

.text-msg {
  font-size: 14px;
}

.img-msg {
  max-width: 180px;
  border-radius: 8px;
}

.video-msg {
  max-width: 240px;
  border-radius: 8px;
}

.input-area {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  border-top: 1px solid #ccc;
}

.input-area input[type='text'] {
  flex: 1;
  padding: 8px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.input-area button {
  padding: 8px 16px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
}
</style>
