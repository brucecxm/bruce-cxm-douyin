<template>
  <div>
    <h2>聊天室</h2>
    <input v-model="text" placeholder="说点什么" />
    <button @click="sendText">发送文字</button>

    <input type="file" @change="uploadFile" />
    <div v-for="msg in messages" :key="msg.content">
      <p>{{ msg.sender }}：</p>
      <div v-if="msg.type === 'text'">{{ msg.content }}</div>
      <div v-else-if="msg.type === 'image'">
        <img :src="msg.fileUrl" style="max-width: 200px" />
      </div>
      <div v-else-if="msg.type === 'video'">
        <video :src="msg.fileUrl" controls style="max-width: 300px" />
      </div>
    </div>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from '@/utils/request';

export default {
  data() {
    return {
      text: '',
      messages: [],
      stompClient: null
    };
  },
  mounted() {
    const socket = new SockJS('/api/ws');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, () => {
      this.stompClient.subscribe('/topic/messages', (msg) => {
        const message = JSON.parse(msg.body);
        this.messages.push(message);
      });
    });
  },
  methods: {
    sendText() {
      const msg = {
        sender: '小明',
        content: this.text,
        type: 'text'
      };
      this.stompClient.send('/app/chat', {}, JSON.stringify(msg));
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
        sender: '小明',
        type,
        fileUrl: url
      };
      this.stompClient.send('/app/chat', {}, JSON.stringify(msg));
    }
  }
};
</script>
