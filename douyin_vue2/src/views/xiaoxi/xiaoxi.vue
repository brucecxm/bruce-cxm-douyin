<template>
  <div class="message-page">
    <!-- 侧边菜单 (保留你的逻辑) -->
    <caidan :style="caidancss"></caidan>

    <!-- 主体内容容器 -->
    <div class="main-content" :style="othercss">
      <!-- 1. 顶部导航栏 (Fixed Header) -->
      <div class="nav-header">
        <div class="nav-left">
          <!-- 这里的图标可以用你的图片替换 -->
          <div class="icon-btn" @click="showcaidanone">☰</div>
        </div>
        <div class="nav-center">消息</div>
        <div class="nav-right">
          <div class="icon-btn">🔍</div>
          <div class="icon-btn">+</div>
        </div>
      </div>

      <!-- 2. 可滚动区域 (中间部分) -->
      <div
        class="scroll-container"
        ref="scrollContainer"
        @scroll="handleScroll"
      >
        <!-- A. 搜索栏 (截图风格) -->
        <div class="search-box-wrapper">
          <div class="search-box"><i class="search-icon">🔍</i> 搜索</div>
        </div>

        <!-- B. 横向好友列表 (在线状态) -->
        <div class="horizontal-section">
          <HorizontalScrollList
            :users="topUsers"
            :avatarStyle="{
              width: '56px',
              height: '56px',
              border: '2px solid #2878ff',
              padding: '1px'
            }"
            :nameStyle="{ fontSize: '12px', marginTop: '6px', color: '#666' }"
            @user-click="handleUserClick"
          />
        </div>

        <!-- C. 系统消息分类 (仿截图中的 互动消息/粉丝 等) -->
        <div class="system-messages">
          <div class="sys-item" @click="handleFriendRequests">
            <div class="sys-icon icon-pink">💬</div>
            <div class="sys-text">互动消息</div>
            <div v-if="hasFriendRequests" class="sys-badge">
              {{ friendRequests.length }}
            </div>
            <div class="sys-arrow">></div>
          </div>
          <div class="sys-item">
            <div class="sys-icon icon-blue">👥</div>
            <div class="sys-text">粉丝</div>
            <div class="sys-arrow">></div>
          </div>
        </div>

        <!-- D. 消息列表 (Chat List) -->
        <div class="chat-list">
          <div
            v-for="(item, index) in filteredUsers"
            :key="index"
            class="chat-item"
            @click="handleUserClick(item)"
          >
            <!-- 头像区 -->
            <div class="avatar-box">
              <img :src="item.avatar || defaultAvatar" class="avatar-img" />
              <div v-if="item.isOnline" class="online-dot"></div>
            </div>

            <!-- 内容区 -->
            <div class="content-box">
              <div class="row-top">
                <span class="username">{{ item.username }}</span>
                <span class="time">{{ item.lastTime || '刚刚' }}</span>
              </div>
              <div class="row-bottom">
                <span class="last-msg">{{
                  item.lastMsg || '点击开始聊天...'
                }}</span>
                <div v-if="item.unreadCount" class="badge">
                  {{ item.unreadCount }}
                </div>
              </div>
            </div>
          </div>

          <!-- 加载状态 -->
          <div v-if="loading" class="loading-tips">加载中...</div>
          <div v-if="!hasMore && userinfos.length > 0" class="no-more-tips">
            - 没有更多了 -
          </div>
          <div v-if="!loading && userinfos.length === 0" class="empty-tips">
            暂无消息
          </div>
        </div>
      </div>

      <!-- 3. 底部导航 (Footer) -->
      <div class="footer-wrapper">
        <footer-vue></footer-vue>
      </div>
    </div>
  </div>
</template>

<script>
import HorizontalScrollList from '@/components/HorizontalScrollList.vue';
import caidan from '@/components/home/caidan.vue';
import footerVue from '@/components/home/footer.vue';
import {
  getFriendApiDetail,
  getUserList,
  getrequestFriendApi
} from '@/api/user.js';

export default {
  name: 'MessagePage',
  components: {
    footerVue,
    caidan,
    HorizontalScrollList
  },
  data() {
    return {
      defaultAvatar: 'https://ui-avatars.com/api/?background=random', // 默认头像
      userinfos: [],
      topUsers: [],
      loading: false,
      hasMore: true,
      page: 1,
      pageSize: 10,

      // 好友请求相关
      friendRequests: [],
      hasFriendRequests: false,

      // 样式状态
      caidancss: {
        position: 'absolute',
        right: '100vw',
        bottom: '0px',
        transition: 'right 0.3s ease', // 优化动画时间
        height: '100vh',
        width: '80vw',
        background: 'white',
        zIndex: 1000,
        boxShadow: '2px 0 8px rgba(0,0,0,0.1)'
      },
      othercss: {
        transition: 'transform 0.3s ease',
        transform: 'translateX(0)',
        height: '100vh',
        width: '100vw',
        display: 'flex',
        flexDirection: 'column'
      }
    };
  },
  computed: {
    filteredUsers() {
      // 如果你需要前端搜索过滤，可以在这里处理，
      // 但通常消息列表是按时间排序的，搜索建议单独做一个页面或弹窗
      return this.userinfos;
    }
  },
  methods: {
    showcaidanone() {
      // 使用 transform 性能更好
      if (this.caidancss.right === '100vw') {
        this.caidancss.right = '20vw';
        this.othercss.transform = 'translateX(80vw)';
      } else {
        this.caidancss.right = '100vw';
        this.othercss.transform = 'translateX(0)';
      }
    },

    // 1. 获取顶部横向好友列表 (Stories)
    async fetchFriendInfos() {
      try {
        const response = await getFriendApiDetail();
        if (response && response.data) {
          this.topUsers = response.data.map((friend) => ({
            name: friend.nickname || friend.username || '用户',
            avatar: friend.avatar || this.defaultAvatar,
            userId: friend.userId || friend.id
          }));
        }
      } catch (error) {
        console.error('Fetch friends failed:', error);
      }
    },

    // 2. 获取消息列表 (分页)
    async fetchUserInfos() {
      if (this.loading || !this.hasMore) return;
      this.loading = true;

      try {
        const response = await getUserList(this.page, this.pageSize);
        if (response && response.code === 0) {
          const records = response.data.records || [];

          if (records.length < this.pageSize) {
            this.hasMore = false;
          }

          const newItems = records.map((user) => ({
            userId: user.id,
            username: user.username || user.nickname || '未知用户',
            avatar: user.avatar || this.defaultAvatar,
            lastMsg: user.sign || '分享了视频', // 模拟最后一条消息
            lastTime: '10:30', // 模拟时间，实际应由后端返回
            unreadCount: Math.floor(Math.random() * 3), // 模拟未读数
            isOnline: true
          }));

          this.userinfos = [...this.userinfos, ...newItems];
          this.page++;
        } else {
          this.hasMore = false;
        }
      } catch (e) {
        console.error(e);
      } finally {
        this.loading = false;
      }
    },

    // 3. 滚动监听
    handleScroll(e) {
      const { scrollTop, clientHeight, scrollHeight } = e.target;
      // 距离底部 50px 时触发加载
      if (scrollTop + clientHeight >= scrollHeight - 50) {
        this.fetchUserInfos();
      }
    },

    // 4. 点击跳转聊天
    handleUserClick(user) {
      const id = user.userId || user.id;
      if (id) {
        this.$router.push({ path: '/chatyi', query: { id } });
      }
    },

    // 5. 获取好友请求列表
    async fetchFriendRequests() {
      try {
        const response = await getrequestFriendApi();
        if (response && response.data) {
          this.friendRequests = response.data;
          this.hasFriendRequests = this.friendRequests.length > 0;
        }
      } catch (error) {
        console.error('Fetch friend requests failed:', error);
      }
    },

    // 6. 处理好友请求点击事件
    handleFriendRequests() {
      this.$router.push('/friend-requests');
    }
  },
  mounted() {
    this.fetchFriendInfos();
    this.fetchUserInfos();
    this.fetchFriendRequests();

    // 隐藏全局滚动条
    document.body.style.overflow = 'hidden';
  }
};
</script>

<style lang="scss" scoped>
/* 基础容器设置 */
.message-page {
  position: relative;
  width: 100%;
  height: 100vh;
  background-color: #fff;
  overflow: hidden; /* 防止页面整体滚动 */
}

/* 顶部导航 */
.nav-header {
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 16px;
  background: #fff;
  border-bottom: 1px solid #f5f5f5;
  flex-shrink: 0; /* 防止被挤压 */
  z-index: 10;
}

.nav-center {
  font-size: 17px;
  font-weight: 600;
  color: #161823;
}

.icon-btn {
  font-size: 20px;
  padding: 4px;
  color: #333;
}

.nav-right {
  display: flex;
  gap: 15px;
}

/* 滚动区域核心布局 */
.scroll-container {
  flex: 1; /* 占据除了 Header 和 Footer 外的所有空间 */
  overflow-y: auto; /* 允许垂直滚动 */
  overflow-x: hidden;
  background-color: #fff;
  padding-bottom: 20px;

  /* 隐藏滚动条但保留功能 */
  &::-webkit-scrollbar {
    display: none;
  }
}

/* 搜索框 */
.search-box-wrapper {
  padding: 10px 16px 0;
}
.search-box {
  background: #f1f1f1;
  border-radius: 4px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 14px;
}
.search-icon {
  margin-right: 6px;
  font-style: normal;
}

/* 横向好友列表 */
.horizontal-section {
  padding: 10px 0;
  border-bottom: 1px solid #f8f8f8;
}

/* 系统消息区 (仿截图) */
.system-messages {
  padding: 0 16px;
  margin-top: 10px;
}

.sys-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
}

.sys-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  margin-right: 12px;
}

.icon-pink {
  background: linear-gradient(135deg, #ff7eb3, #ff758c);
}
.icon-blue {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.sys-text {
  font-size: 16px;
  font-weight: 500;
  color: #161823;
  flex: 1;
}

.sys-arrow {
  color: #ccc;
  font-size: 14px;
}

/* 聊天列表样式 */
.chat-list {
  padding: 0 16px;
}

.chat-item {
  display: flex;
  padding: 12px 0;
  cursor: pointer;

  &:active {
    background-color: #f9f9f9;
  }
}

.avatar-box {
  position: relative;
  margin-right: 12px;
}

.avatar-img {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid #eee;
}

.online-dot {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  background: #3ac674;
  border: 2px solid white;
  border-radius: 50%;
}

.content-box {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-bottom: 1px solid #f8f8f8; /* 细分割线 */
  padding-right: 4px;
}

.row-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.username {
  font-size: 16px;
  font-weight: 500;
  color: #161823;
}

.time {
  font-size: 12px;
  color: #999;
}

.row-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.last-msg {
  font-size: 13px;
  color: #999;
  max-width: 200px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.badge {
  background: #fe2c55;
  color: white;
  font-size: 10px;
  padding: 0 5px;
  height: 16px;
  line-height: 16px;
  border-radius: 8px;
  min-width: 16px;
  text-align: center;
}

/* 底部状态文字 */
.loading-tips,
.no-more-tips,
.empty-tips {
  text-align: center;
  color: #999;
  padding: 20px 0;
  font-size: 13px;
}

/* Footer 固定容器 */
.footer-wrapper {
  height: 50px; /* footer组件的高度 */
  flex-shrink: 0;
  border-top: 1px solid #eee;
  z-index: 10;
}
</style>
