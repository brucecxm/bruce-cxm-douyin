<template>
  <div class="message-page">
    <div class="message-header">
      <div class="back-button" @click="goBack">
        <i class="fas fa-arrow-left"></i>
      </div>
      <div class="message-title">购物消息</div>
      <div class="settings" @click="goToSettings">
        <i class="icon-settings"></i>
      </div>
    </div>

    <div class="message-tabs">
      <div
        class="tab"
        :class="{ active: currentTab === 'all' }"
        @click="currentTab = 'all'"
      >
        > 全部消息
      </div>
      <div
        class="tab"
        :class="{ active: currentTab === 'system' }"
        @click="currentTab = 'system'"
      >
        系统消息
      </div>
      <div
        class="tab"
        :class="{ active: currentTab === 'order' }"
        @click="currentTab = 'order'"
      >
        订单消息
      </div>
    </div>

    <div class="message-list">
      <div
        v-for="message in filteredMessages"
        :key="message.id"
        class="message-item"
      >
        <div class="message-header">
          <div class="message-title">{{ message.title }}</div>
          <div class="message-time">{{ message.time }}</div>
        </div>
        <div class="message-body">{{ message.body }}</div>
        <div class="message-footer">
          <div
            class="message-status"
            :class="{ read: message.status === 'read' }"
          >
            {{ message.status === 'read' ? '已读' : '未读' }}
          </div>
          <button class="view-details" @click="viewDetails(message.id)">
            查看详情
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'all', // 默认选中“全部消息”
      messages: [
        {
          id: 1,
          title: '订单已发货',
          time: '2024-12-18 14:30',
          body: '您的订单 #12345 已经发货，预计3日内到达。',
          status: 'unread',
          type: 'order'
        },
        {
          id: 2,
          title: '系统通知',
          time: '2024-12-17 10:00',
          body: '我们的系统将于今晚进行维护，请您提前做好准备。',
          status: 'read',
          type: 'system'
        },
        {
          id: 3,
          title: '订单已支付',
          time: '2024-12-16 09:00',
          body: '您已成功支付订单 #12346，等待发货。',
          status: 'read',
          type: 'order'
        },
        {
          id: 4,
          title: '新活动通知',
          time: '2024-12-15 08:00',
          body: '快来参加我们的黑五大促活动，优惠多多！',
          status: 'unread',
          type: 'system'
        }
      ]
    };
  },
  computed: {
    filteredMessages() {
      if (this.currentTab === 'all') return this.messages;
      return this.messages.filter(
        (message) => message.type === this.currentTab
      );
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1); // 返回上一页
    },
    goToSettings() {
      this.$router.push('/settings'); // 跳转到设置页面
    },
    viewDetails(messageId) {
      // 处理查看消息详情逻辑
      console.log('View details for message:', messageId);
      // 例如：跳转到消息详情页
      this.$router.push(`/message/${messageId}`);
    }
  }
};
</script>

<style scoped>
.message-page {
  font-family: 'Arial', sans-serif;
  background-color: #f5f5f5;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.message-header {
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  padding: 12px 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  align-items: center;
}

.message-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.settings {
  font-size: 18px;
  color: #ff6a00;
  cursor: pointer;
}

.message-tabs {
  display: flex;
  justify-content: space-around;
  background-color: #fff;
  padding: 10px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.tab {
  font-size: 16px;
  color: #333;
  cursor: pointer;
  padding: 8px 12px;
  transition: background-color 0.3s;
}

.tab.active {
  background-color: #ff6a00;
  color: white;
}

.message-list {
  flex: 1;
  padding: 10px 20px;
  background-color: #fff;
  overflow-y: auto;
}

.message-item {
  background-color: #f9f9f9;
  margin-bottom: 15px;
  border-radius: 8px;
  padding: 12px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.message-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.message-body {
  font-size: 14px;
  color: #555;
  margin-bottom: 10px;
}

.message-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-status {
  font-size: 12px;
  color: #ff6a00;
}

.message-status.read {
  color: #28a745;
}

.view-details {
  background-color: #ff6a00;
  color: white;
  border: none;
  padding: 6px 12px;
  font-size: 14px;
  border-radius: 6px;
  cursor: pointer;
}

.view-details:hover {
  background-color: #e65c00;
}
</style>
