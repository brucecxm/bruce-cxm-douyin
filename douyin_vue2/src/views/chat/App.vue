<template>
  <div class="chat-page">
    <!-- 顶部导航 -->
    <header class="chat-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">{{ chatInfo.sender.nickname }}</div>
      <div class="header-right" @click="showMore">
        <i class="more-icon">⋮</i>
      </div>
    </header>

    <!-- 聊天内容区域 -->
    <div class="chat-content">
      <!-- 聊天记录 -->
      <div class="message-group" v-for="(group, index) in messageGroups" :key="index">
        <div class="date-divider">{{ group.date }}</div>
        <div class="message-item" v-for="message in group.messages" :key="message.id">
          <!-- 对方消息 -->
          <div class="message-other" v-if="!message.isMine">
            <div class="avatar">
              <img :src="chatInfo.sender.avatar" :alt="chatInfo.sender.nickname">
            </div>
            <div class="message-bubble-other">
              <div class="message-text">{{ message.content }}</div>
              <div class="message-time">{{ message.time }}</div>
            </div>
          </div>
          <!-- 我的消息 -->
          <div class="message-mine" v-else>
            <div class="message-bubble-mine">
              <div class="message-text">{{ message.content }}</div>
              <div class="message-time">{{ message.time }}</div>
            </div>
            <div class="avatar">
              <img :src="myInfo.avatar" :alt="myInfo.nickname">
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 商品信息卡片 -->
    <div class="product-card" @click="goToProductDetail">
      <div class="product-image">
        <img :src="chatInfo.product.image" :alt="chatInfo.product.title">
      </div>
      <div class="product-info">
        <div class="product-title">{{ chatInfo.product.title }}</div>
        <div class="product-price">¥{{ chatInfo.product.price }}</div>
      </div>
      <div class="product-arrow">→</div>
    </div>

    <!-- 输入区域 -->
    <footer class="input-area">
      <div class="input-toolbar">
        <div class="toolbar-item">😀</div>
        <div class="toolbar-item">📷</div>
        <div class="toolbar-item">📎</div>
      </div>
      <div class="input-group">
        <input type="text" class="chat-input" v-model="inputMessage" placeholder="输入消息..." @keyup.enter="sendMessage">
        <button class="send-btn" @click="sendMessage">发送</button>
      </div>
    </footer>
  </div>
</template>

<script>
import { post, get } from '../../utils/apiUtils'
import nativeUtils from '../../utils/nativeUtils'

export default {
  name: 'ChatPage',
  data() {
    return {
      inputMessage: '',
      myInfo: {
        id: parseInt(localStorage.getItem('userId')) || 25,
        nickname: localStorage.getItem('userNickname') || '我',
        avatar: localStorage.getItem('userAvatar') || 'https://via.placeholder.com/50x50?text=MyAvatar'
      },
      chatInfo: {
        userId: '',
        chatId: '',
        productId: '',
        sender: {
          id: '',
          nickname: '',
          avatar: ''
        },
        product: {
          id: '',
          title: '',
          price: '',
          image: ''
        }
      },
      messageGroups: [],
      loading: false,
      // WebSocket相关
      stompClient: null,
      webSocketConnected: false
    }
  },
  mounted() {
    // 从URL获取参数
    this.parseUrlParams()
    // 初始化数据并获取聊天记录
    this.initAndFetch()
    // 连接WebSocket
    this.connectWebSocket()
  },
  beforeUnmount() {
    // 断开WebSocket连接
    this.disconnectWebSocket()
  },
  methods: {
    // 解析URL参数
    parseUrlParams() {
      const urlParams = new URLSearchParams(window.location.search)
      this.chatInfo.userId = urlParams.get('userId') || ''
      this.chatInfo.chatId = urlParams.get('chatId') || ''
      this.chatInfo.productId = urlParams.get('productId') || ''
      console.log('解析到的参数:', { userId: this.chatInfo.userId, chatId: this.chatInfo.chatId, productId: this.chatInfo.productId })
    },
    
    // 初始化数据并获取聊天记录（确保顺序）
    async initAndFetch() {
      try {
        await this.initChatData()
        await this.fetchChatMessages()
      } catch (error) {
        console.error('初始化和获取聊天记录失败:', error)
      }
    },
    
    // 连接WebSocket
    connectWebSocket() {
      // 引入SockJS和STOMP.js
      const script = document.createElement('script')
      script.src = 'https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js'
      script.onload = () => {
        const stompScript = document.createElement('script')
        stompScript.src = 'https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js'
        stompScript.onload = () => {
          this.initWebSocket()
        }
        document.head.appendChild(stompScript)
      }
      document.head.appendChild(script)
    },
    
    // 初始化WebSocket连接
    initWebSocket() {
      const SockJS = window.SockJS
      const Stomp = window.Stomp
      
      if (SockJS && Stomp) {
        // 创建SockJS连接，使用正确的代理路径
        const socket = new SockJS('/ws')
        // 创建STOMP客户端
        this.stompClient = Stomp.over(socket)
        
        // 连接STOMP服务器
        this.stompClient.connect({}, (frame) => {
          console.log('WebSocket连接成功:', frame)
          this.webSocketConnected = true
          
          // 订阅消息主题
          this.stompClient.subscribe(`/topic/messages/${this.myInfo.id}`, (message) => {
            const newMessage = JSON.parse(message.body)
            console.log('收到新消息:', newMessage)
            // 刷新聊天记录
            this.fetchChatMessages()
          })
        }, (error) => {
          console.error('WebSocket连接失败:', error)
          this.webSocketConnected = false
          // 尝试重新连接（5秒后）
          setTimeout(() => this.connectWebSocket(), 5000)
        })
      }
    },
    
    // 断开WebSocket连接
    disconnectWebSocket() {
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.disconnect()
        console.log('WebSocket连接已断开')
        this.webSocketConnected = false
      }
    },
    
    // 初始化聊天数据
    async initChatData() {
      try {
        // 获取当前用户信息（实际项目中从localStorage或API获取）
        const userStr = localStorage.getItem('user')
        if (userStr) {
          const user = JSON.parse(userStr)
          this.myInfo.id = user.id
          this.myInfo.nickname = user.nickname
          this.myInfo.avatar = user.avatar || this.myInfo.avatar
        }
        
        // 确保对方用户ID类型正确
        const receiverId = Number(this.chatInfo.userId)
        
        // 从API获取对方用户信息
        if (receiverId) {
          try {
            const response = await get(`/user/${receiverId}`)
            if (response.code === 0 && response.data) {
              this.chatInfo.sender = {
                id: response.data.id,
                nickname: response.data.nickname,
                avatar: response.data.avatar || `https://via.placeholder.com/50x50?text=User${receiverId}`
              }
            }
          } catch (error) {
            console.error('获取对方用户信息失败:', error)
            // 如果获取失败，使用默认值
            this.chatInfo.sender = {
              id: receiverId,
              nickname: `用户${receiverId}`,
              avatar: `https://via.placeholder.com/50x50?text=User${receiverId}`
            }
          }
        }
        
        // 从API获取商品信息
        if (this.chatInfo.productId) {
          const productId = Number(this.chatInfo.productId)
          try {
            const response = await get(`/product/${productId}`)
            if (response.code === 0 && response.data) {
              this.chatInfo.product = {
                id: response.data.id,
                title: response.data.title,
                price: response.data.price,
                image: response.data.image || `https://via.placeholder.com/80x80?text=Product${productId}`
              }
            }
          } catch (error) {
            console.error('获取商品信息失败:', error)
            // 如果获取失败，使用默认值
            this.chatInfo.product = {
              id: productId,
              title: `商品${productId}`,
              price: 0,
              image: `https://via.placeholder.com/80x80?text=Product${productId}`
            }
          }
        } else {
          // 如果没有商品ID，使用默认商品信息
          this.chatInfo.product = {
            id: 0,
            title: '未知商品',
            price: 0,
            image: `https://via.placeholder.com/80x80?text=Product`
          }
        }
      } catch (error) {
        console.error('初始化聊天数据失败:', error)
        nativeUtils.showToast('初始化聊天数据失败', 1500)
      }
    },
    
    // 获取聊天记录
    async fetchChatMessages() {
      try {
        this.loading = true
        
        // 验证必填参数
        if (!this.myInfo.id || !this.chatInfo.userId) {
          console.error('缺少必要参数:', { myInfoId: this.myInfo.id, userId: this.chatInfo.userId })
          nativeUtils.showToast('无法获取聊天记录，缺少必要参数', 1500)
          // 清空消息列表，不再使用模拟数据
          this.messageGroups = []
          return
        }
        
        // 确保参数类型正确，转换为Number类型
        const userId1 = Number(this.myInfo.id)
        const userId2 = Number(this.chatInfo.userId)
        
        // 调用真实API获取聊天记录
        const response = await get(`/message/chat`, {
          userId1: userId1,
          userId2: userId2
        })
        
        if (response.code === 0 && response.data) {
          this.processMessages(response.data)
        } else {
          nativeUtils.showToast('获取聊天记录失败', 1500)
          // 清空消息列表，不再使用模拟数据
          this.messageGroups = []
        }
      } catch (error) {
        console.error('获取聊天记录失败:', error)
        nativeUtils.showToast('获取聊天记录失败', 1500)
        // 清空消息列表，不再使用模拟数据
        this.messageGroups = []
      } finally {
        this.loading = false
      }
    },
    
    // 滚动到底部
    scrollToBottom() {
      setTimeout(() => {
        const chatContent = document.querySelector('.chat-content')
        if (chatContent) {
          chatContent.scrollTop = chatContent.scrollHeight
        }
      }, 100)
    },
    
    // 处理聊天记录，按日期分组
    processMessages(messages) {
      if (!messages || messages.length === 0) {
        // 没有消息时显示空列表，不再使用模拟数据
        this.messageGroups = []
        return
      }
      
      // 按日期分组
      const groupedMessages = messages.reduce((groups, message) => {
        const date = new Date(message.createdAt).toLocaleDateString('zh-CN')
        if (!groups[date]) {
          groups[date] = []
        }
        
        const messageItem = {
          id: message.id,
          content: message.content,
          time: new Date(message.createdAt).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }),
          isMine: message.sender.id === this.myInfo.id
        }
        
        groups[date].push(messageItem)
        return groups
      }, {})
      
      // 转换为数组格式
      this.messageGroups = Object.entries(groupedMessages).map(([date, messages]) => ({
        date,
        messages
      }))
      
      // 滚动到底部，显示最新消息
      this.scrollToBottom()
    },
    
    // 返回上一页
    goBack() {
      window.history.back()
    },
    
    // 显示更多选项
    showMore() {
      nativeUtils.showToast('更多功能开发中', 1500)
    },
    
    // 跳转到商品详情
    goToProductDetail() {
      window.location.href = `/detail.html?id=${this.chatInfo.product.id}`
    },
    
    // 发送消息
    sendMessage() {
      if (!this.inputMessage.trim()) return
      
      // 验证必填参数
      if (!this.myInfo.id || !this.chatInfo.userId) {
        console.error('缺少必要参数:', { myInfoId: this.myInfo.id, userId: this.chatInfo.userId })
        nativeUtils.showToast('无法发送消息，缺少必要参数', 1500)
        return
      }
      
      try {
        // 确保参数类型正确，转换为Number类型
        const senderId = Number(this.myInfo.id)
        const receiverId = Number(this.chatInfo.userId)
        const messageContent = this.inputMessage.trim()
        
        const messageRequest = {
          senderId: senderId,
          receiverId: receiverId,
          content: messageContent
        }
        
        if (this.webSocketConnected && this.stompClient) {
          // 使用WebSocket发送消息
          this.stompClient.send('/app/message/send', {}, JSON.stringify(messageRequest))
          // 清空输入框
          this.inputMessage = ''
        } else {
          // 降级使用HTTP POST请求
          this.sendHttpRequest(messageRequest)
        }
      } catch (error) {
        console.error('发送消息失败:', error)
        nativeUtils.showToast('发送消息失败', 1500)
      }
    },
    
    // 使用HTTP请求发送消息（降级方案）
    async sendHttpRequest(messageRequest) {
      try {
        const response = await post('/message', messageRequest)
        
        if (response.code === 0) {
          // 清空输入框
          this.inputMessage = ''
          // 刷新聊天记录，确保获取到最新消息（包括对方可能发送的）
          await this.fetchChatMessages()
        } else {
          nativeUtils.showToast('发送消息失败', 1500)
        }
      } catch (error) {
        console.error('发送HTTP请求失败:', error)
        nativeUtils.showToast('发送消息失败', 1500)
      }
    },
  }
}
</script>

<style scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部导航 */
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-icon, .more-icon {
  font-size: 20px;
  color: #333;
  cursor: pointer;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

/* 聊天内容区域 */
.chat-content {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
}

/* 日期分隔线 */
.date-divider {
  text-align: center;
  font-size: 12px;
  color: #999;
  margin: 15px 0;
}

/* 消息项 */
.message-group {
  margin-bottom: 20px;
}

.message-item {
  margin-bottom: 15px;
  display: flex;
  align-items: flex-end;
}

/* 对方消息 */
.message-other {
  display: flex;
  align-items: flex-end;
}

/* 我的消息 */
.message-mine {
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
}

/* 头像 */
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 10px;
  flex-shrink: 0;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 消息气泡 */
.message-bubble-other {
  background-color: white;
  border-radius: 15px 15px 15px 5px;
  padding: 10px 15px;
  max-width: 70%;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative;
}

.message-bubble-mine {
  background-color: #42b983;
  color: white;
  border-radius: 15px 15px 5px 15px;
  padding: 10px 15px;
  max-width: 70%;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative;
}

.message-text {
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 5px;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.message-time {
  font-size: 11px;
  text-align: right;
  color: #999;
}

.message-bubble-mine .message-time {
  color: rgba(255, 255, 255, 0.7);
}

/* 商品信息卡片 */
.product-card {
  display: flex;
  align-items: center;
  background-color: white;
  margin: 0 15px 15px;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.product-image {
  width: 60px;
  height: 60px;
  border-radius: 5px;
  overflow: hidden;
  margin-right: 15px;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
  min-width: 0;
}

.product-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
}

.product-price {
  font-size: 16px;
  font-weight: bold;
  color: #ff4444;
}

.product-arrow {
  font-size: 16px;
  color: #999;
  margin-left: 10px;
}

/* 输入区域 */
.input-area {
  background-color: white;
  padding: 10px 15px;
  border-top: 1px solid #eee;
}

.input-toolbar {
  display: flex;
  margin-bottom: 10px;
}

.toolbar-item {
  font-size: 20px;
  margin-right: 20px;
  cursor: pointer;
}

.input-group {
  display: flex;
  align-items: center;
}

.chat-input {
  flex: 1;
  border: 1px solid #ddd;
  border-radius: 20px;
  padding: 10px 15px;
  font-size: 14px;
  outline: none;
  resize: none;
}

.send-btn {
  margin-left: 10px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 10px 20px;
  font-size: 14px;
  cursor: pointer;
}

.send-btn:hover {
  background-color: #3aa876;
}
</style>
