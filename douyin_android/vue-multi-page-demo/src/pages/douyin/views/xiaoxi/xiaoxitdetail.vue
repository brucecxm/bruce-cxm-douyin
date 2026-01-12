<template>
  <div class="chat-container">
    <!-- Header -->
    <div class="header">
      <h3>Chat</h3>
    </div>

    <!-- Chat Box -->
    <div class="chat-box">
      <ul id="chat">
        <li
          v-for="(message, index) in messages"
          :key="index"
          :class="[
            'chat-message',
            message.type === 'JOIN'
              ? 'join-message'
              : message.sender === username
                ? 'user-message'
                : 'friend-message'
          ]"
        >
          {{ formatMessage(message) }}
        </li>
      </ul>
    </div>

    <!-- Message Input -->
    <div class="message-input">
      <input
        v-model="message"
        placeholder="Type a message"
        class="input-field"
      />
      <button @click="sendMessage" class="send-btn">Send</button>
    </div>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export default {
  data() {
    return {
      friendId: null,
      stompClient: null,
      username: '',
      message: '',
      messages: []
    };
  },
  created() {
    this.friendId = this.$route.params.id;
  },
  mounted() {
    this.connect();
  },
  methods: {
    connect() {
      const socket = new SockJS('http://localhost:9998/chat');
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect(
        {},
        (frame) => {
          console.log('Connected: ' + frame);
          const userId = 11;
          const friendId = this.friendId;

          this.stompClient.subscribe(
            `/topic/chat/${userId}/${friendId}`,
            (messageOutput) => {
              this.showMessage(JSON.parse(messageOutput.body));
            }
          );

          this.stompClient.subscribe(
            `/topic/chat/${friendId}/${userId}`,
            (messageOutput) => {
              this.showMessage(JSON.parse(messageOutput.body));
            }
          );

          this.stompClient.send(
            `/app/chat.addUser/${userId}/${friendId}`,
            {},
            JSON.stringify({ sender: userId, type: 'JOIN', friendId: friendId })
          );
        },
        (error) => {
          console.error('Error connecting to WebSocket:', error);
        }
      );
    },
    sendMessage() {
      const messageContent = this.message.trim();

      if (messageContent && this.stompClient) {
        const chatMessage = {
          sender: this.username,
          content: messageContent,
          type: 'CHAT'
        };

        this.stompClient.send(
          `/app/chat.sendMessage/${this.userId}/${this.friendId}`,
          {},
          JSON.stringify(chatMessage)
        );
        this.message = '';
      }
    },
    showMessage(message) {
      this.messages.push(message);
    },
    formatMessage(message) {
      if (message.type === 'JOIN') {
        return `${message.sender} joined the chat`;
      } else if (message.type === 'CHAT') {
        return `${message.sender}: ${message.content}`;
      }
      return '';
    }
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f7f7f7;
}

.header {
  background-color: #ffffff;
  padding: 10px 20px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.chat-box {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin: 10px;
}

#chat {
  list-style: none;
  padding: 0;
}

.chat-message {
  margin-bottom: 15px;
  font-size: 16px;
  word-wrap: break-word;
  line-height: 1.5;
  border-radius: 20px;
  padding: 10px 15px;
  max-width: 70%;
}

.user-message {
  background-color: #dcf8c6;
  align-self: flex-start;
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
}

.friend-message {
  background-color: #ffffff;
  border: 1px solid #ccc;
  align-self: flex-end;
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
}

.join-message {
  font-style: italic;
  color: #888;
  text-align: center;
  padding: 5px;
}

.message-input {
  display: flex;
  padding: 10px;
  background-color: #ffffff;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
  position: sticky;
  bottom: 0;
  z-index: 1000;
}

.input-field {
  flex: 1;
  padding: 12px;
  border-radius: 20px;
  border: 1px solid #ccc;
  font-size: 16px;
  background-color: #f1f1f1;
  margin-right: 10px;
}

.send-btn {
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 12px 15px;
  font-size: 16px;
  cursor: pointer;
}

.send-btn:hover {
  background-color: #45a049;
}

input:focus,
button:focus {
  outline: none;
}
</style>
