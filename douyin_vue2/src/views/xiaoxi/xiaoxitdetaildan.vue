<template>
    <div>
        <div>
            <input v-model="username" placeholder="Enter your username" />
            <button @click="connect">Connect</button> <!-- 连接按钮 -->
        </div>
        <ul id="chat">
            <li v-for="(message, index) in messages" :key="index"
                :style="{ color: message.type === 'JOIN' ? 'green' : 'black' }">
                {{ formatMessage(message) }}
            </li>
        </ul> <!-- 聊天记录的显示区域 -->
        <div>
            <input v-model="message" placeholder="Enter your message" />
            <button @click="sendMessage">Send</button> <!-- 发送按钮 -->
        </div>
    </div>
</template>
  
<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export default {
    
    data() {
        return {
            stompClient: null,
            username: '',
            message: '',
            messages: []
        };
    },

    //接受来自父组件的id和我的id 的组合
      props: {
        friendandmeId: {
            type: String,
            required: true
        },
      },
    mounted() {
        // 自动连接到 WebSocket，当组件挂载时调用
        this.connect();
    },
    methods: {
        connect() {
            const socket = new SockJS('http://localhost:9998/chat'); // 创建 SockJS 对象
            this.stompClient = Stomp.over(socket); // 用 STOMP 包装 SockJS 对象

            // 连接到 WebSocket
            this.stompClient.connect({}, (frame) => {
                console.log('Connected: ' + frame);

                // 订阅 /topic/public 频道，接收消息并显示
                this.stompClient.subscribe('/topic/${this.friendandmeId}', (messageOutput) => {
                    this.showMessage(JSON.parse(messageOutput.body));
                });

                // 发送用户加入消息到 /app/chat.addUser 频道
                this.stompClient.send('/app/chat.addUser',
                    {},
                    JSON.stringify({ sender: this.username, type: 'JOIN' })
                );
            }, (error) => {
                console.error('Error connecting to WebSocket:', error);
            });
        },
        sendMessage() {
            const messageContent = this.message.trim();

            if (messageContent && this.stompClient) {
                const chatMessage = {
                    sender: this.username,
                    content: messageContent,
                    type: 'CHAT'
                };

                // 发送聊天消息到 /app/chat.sendMessage 频道
                this.stompClient.send('/app/chat.sendMessagedan', {}, JSON.stringify(chatMessage));
                this.message = ''; // 清空输入框
            }
        },
        showMessage(message) {
            this.messages.push(message);
        },
        formatMessage(message) {
            if (message.type === 'JOIN') {
                return `${message.sender} joined`;
            } else if (message.type === 'CHAT') {
                return `${message.sender}: ${message.content}`;
            }
            return '';
        }
    }
};
</script>
  
<style scoped>
#chat {
    list-style: none;
    /* 去除列表项的样式 */
    padding: 0;
    /* 去除内边距 */
    width: 300px;
    /* 聊天区域的宽度 */
    border: 1px solid #ccc;
    /* 设置边框 */
    height: 200px;
    /* 聊天区域的高度 */
    overflow-y: scroll;
    /* 允许纵向滚动 */
    margin-bottom: 10px;
    /* 添加底部外边距 */
}

#chat li {
    margin-bottom: 10px;
    /* 列表项之间的间距 */
}
</style>
  