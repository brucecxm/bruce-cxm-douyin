<template>
  <div class="messages-page">
    <!-- 顶部导航 -->
    <header class="messages-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">消息</div>
      <div class="header-right">
        <i class="more-icon">⋮</i>
      </div>
    </header>

    <!-- 消息分类标签 -->
    <div class="message-tabs">
      <div class="tab-item active" @click="switchTab('chat')">聊天</div>
      <div class="tab-item" @click="switchTab('notice')">通知</div>
      <div class="tab-item" @click="switchTab('system')">系统</div>
    </div>

    <!-- 聊天消息列表 -->
    <div class="chat-list" v-if="activeTab === 'chat'">
      <div
        class="message-item"
        v-for="message in chatMessages"
        :key="message.id"
        @click="goToChat(message.id, message.sender.id)"
      >
        <div class="sender-avatar">
          <img :src="message.sender.avatar" :alt="message.sender.nickname" />
          <div class="unread-badge" v-if="message.unreadCount > 0">
            {{ message.unreadCount }}
          </div>
        </div>
        <div class="message-content">
          <div class="message-header">
            <div class="sender-name">{{ message.sender.nickname }}</div>
            <div class="message-time">{{ message.time }}</div>
          </div>
          <div class="message-preview">{{ message.preview }}</div>
        </div>
      </div>
    </div>

    <!-- 通知消息列表 -->
    <div class="notice-list" v-else-if="activeTab === 'notice'">
      <div class="notice-item" v-for="notice in notices" :key="notice.id">
        <div class="notice-icon">{{ notice.icon }}</div>
        <div class="notice-content">
          <div class="notice-title">{{ notice.title }}</div>
          <div class="notice-desc">{{ notice.desc }}</div>
        </div>
        <div class="notice-time">{{ notice.time }}</div>
      </div>
    </div>

    <!-- 系统消息列表 -->
    <div class="system-list" v-else-if="activeTab === 'system'">
      <div
        class="system-item"
        v-for="system in systemMessages"
        :key="system.id"
      >
        <div class="system-icon">📢</div>
        <div class="system-content">
          <div class="system-title">{{ system.title }}</div>
          <div class="system-desc">{{ system.desc }}</div>
          <div class="system-time">{{ system.time }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';
import { get } from '../../utils/apiUtils';

export default {
  name: 'MessagesPage',
  data() {
    return {
      activeTab: 'chat',
      chatMessages: [],
      notices: [],
      systemMessages: [],
      userId: parseInt(localStorage.getItem('userId')) || 25,
      pollingTimer: null // 轮询定时器
    };
  },
  mounted() {
    // 设置页面标题
    nativeUtils.setTitleBar('消息');
    // 加载消息数据
    this.loadMessages();
    // 启动轮询获取新消息（每5秒一次）
    this.startPolling();
  },
  beforeUnmount() {
    // 清除轮询定时器
    this.stopPolling();
  },
  methods: {
    // 从API加载消息数据
    async loadMessages() {
      try {
        // 加载聊天消息
        await this.loadChatMessages();

        // 加载通知消息
        await this.loadNotices();

        // 加载系统消息
        await this.loadSystemMessages();
      } catch (error) {
        console.error('加载消息数据失败:', error);
        // 初始化空数据
        this.chatMessages = [];
        this.notices = [];
        this.systemMessages = [];
      }
    },

    // 加载聊天消息
    async loadChatMessages() {
      try {
        // 调用API获取聊天消息列表
        const response = await get('/newshop/message/user/' + this.userId);

        if (response.code === 0 && response.data) {
          this.chatMessages = response.data;
        } else {
          // 如果API调用失败，使用空列表
          this.chatMessages = [];
        }
      } catch (error) {
        console.error('加载聊天消息失败:', error);
        // 使用空列表
        this.chatMessages = [];
      }
    },

    // 加载通知消息
    async loadNotices() {
      try {
        // 调用API获取通知消息列表
        const response = await get('/newshop/notice/user/' + this.userId);

        if (response.code === 0 && response.data) {
          this.notices = response.data;
        } else {
          // 如果API调用失败，使用空列表
          this.notices = [];
        }
      } catch (error) {
        console.error('加载通知消息失败:', error);
        // 使用空列表
        this.notices = [];
      }
    },

    // 加载系统消息
    async loadSystemMessages() {
      try {
        // 调用API获取系统消息列表
        const response = await get('/newshop/system-message');

        if (response.code === 0 && response.data) {
          this.systemMessages = response.data;
        } else {
          // 如果API调用失败，使用空列表
          this.systemMessages = [];
        }
      } catch (error) {
        console.error('加载系统消息失败:', error);
        // 使用空列表
        this.systemMessages = [];
      }
    },

    // 启动轮询获取新消息
    startPolling() {
      this.pollingTimer = setInterval(() => {
        this.loadChatMessages();
      }, 5000); // 每5秒轮询一次
    },

    // 停止轮询
    stopPolling() {
      if (this.pollingTimer) {
        clearInterval(this.pollingTimer);
        this.pollingTimer = null;
      }
    },

    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1);
    },

    // 切换消息标签
    switchTab(tab) {
      this.activeTab = tab;
    },

    // 跳转到聊天页面
    async goToChat(chatId, userId) {
      // 标记该聊天消息为已读
      const message = this.chatMessages.find((item) => item.id === chatId);
      if (message) {
        message.unreadCount = 0;
      }
      // 跳转到聊天页面
      routerUtils.navigateTo(`/chat.html?chatId=${chatId}&userId=${userId}`);
    }
  }
};
</script>

<style scoped>
.messages-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 顶部导航 */
.messages-header {
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

.back-icon,
.more-icon {
  font-size: 20px;
  color: #333;
  cursor: pointer;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

/* 消息分类标签 */
.message-tabs {
  display: flex;
  background-color: white;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.tab-item {
  flex: 1;
  text-align: center;
  font-size: 16px;
  color: #666;
  cursor: pointer;
  position: relative;
  padding-bottom: 10px;
}

.tab-item.active {
  color: #42b983;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 2px;
  background-color: #42b983;
}

/* 聊天消息列表 */
.chat-list,
.notice-list,
.system-list {
  padding: 10px;
}

.message-item,
.notice-item,
.system-item {
  background-color: white;
  border-radius: 10px;
  margin-bottom: 10px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding: 15px;
  display: flex;
  align-items: flex-start;
  cursor: pointer;
}

/* 发送者头像 */
.sender-avatar {
  position: relative;
  margin-right: 15px;
}

.sender-avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.unread-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: #ff4444;
  color: white;
  font-size: 12px;
  font-weight: bold;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 消息内容 */
.message-content {
  flex: 1;
  min-width: 0;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.sender-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.message-preview {
  font-size: 14px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 通知消息 */
.notice-icon {
  font-size: 24px;
  margin-right: 15px;
  margin-top: 5px;
}

.notice-content {
  flex: 1;
  min-width: 0;
}

.notice-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.notice-desc {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 系统消息 */
.system-icon {
  font-size: 24px;
  margin-right: 15px;
  margin-top: 5px;
}

.system-content {
  flex: 1;
  min-width: 0;
}

.system-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.system-desc {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
}

.system-time {
  font-size: 12px;
  color: #999;
}
</style>
