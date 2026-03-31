<template>
  <div class="friend-requests-page">
    <!-- 侧边菜单 -->
    <caidan :style="caidancss"></caidan>

    <!-- 主体内容容器 -->
    <div class="main-content" :style="othercss">
      <!-- 顶部导航栏 -->
      <div class="nav-header">
        <div class="nav-left">
          <div class="icon-btn" @click="goBack">←</div>
        </div>
        <div class="nav-center">好友请求</div>
        <div class="nav-right">
          <div class="icon-btn">✏️</div>
        </div>
      </div>

      <!-- 可滚动区域 -->
      <div class="scroll-container">
        <!-- 好友请求列表 -->
        <div class="request-list">
          <div
            v-for="(request, index) in friendRequests"
            :key="index"
            class="request-item"
          >
            <!-- 头像区 -->
            <div class="avatar-box">
              <img :src="request.avatar || defaultAvatar" class="avatar-img" />
            </div>

            <!-- 内容区 -->
            <div class="content-box">
              <div class="row-top">
                <span class="username">{{
                  request.nickname || request.username
                }}</span>
                <span class="time">{{ request.createTime || '刚刚' }}</span>
              </div>
              <div class="row-middle">
                <span class="verify-msg">{{
                  request.verifyMsg || '请求添加您为好友'
                }}</span>
              </div>
              <div class="row-bottom">
                <button
                  class="btn-accept"
                  @click="handleRequest(request.requestId, 'accept')"
                >
                  接受
                </button>
                <button
                  class="btn-reject"
                  @click="handleRequest(request.requestId, 'reject')"
                >
                  拒绝
                </button>
              </div>
            </div>
          </div>

          <!-- 空状态 -->
          <div
            v-if="!loading && friendRequests.length === 0"
            class="empty-tips"
          >
            暂无好友请求
          </div>

          <!-- 加载状态 -->
          <div v-if="loading" class="loading-tips">加载中...</div>
        </div>
      </div>

      <!-- 底部导航 -->
      <div class="footer-wrapper">
        <footer-vue></footer-vue>
      </div>
    </div>
  </div>
</template>

<script>
import caidan from '@/components/home/caidan.vue';
import footerVue from '@/components/home/footer.vue';
import { getrequestFriendApi } from '@/api/user.js';
import request from '@/utils/request.js';

export default {
  name: 'FriendRequestsPage',
  components: {
    footerVue,
    caidan
  },
  data() {
    return {
      defaultAvatar: 'https://ui-avatars.com/api/?background=random',
      friendRequests: [],
      loading: false,

      // 样式状态
      caidancss: {
        position: 'absolute',
        right: '100vw',
        bottom: '0px',
        transition: 'right 0.3s ease',
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
  methods: {
    goBack() {
      this.$router.back();
    },

    async fetchFriendRequests() {
      this.loading = true;
      try {
        const response = await getrequestFriendApi();
        if (response && response.data) {
          this.friendRequests = response.data;
        }
      } catch (error) {
        console.error('Fetch friend requests failed:', error);
      } finally {
        this.loading = false;
      }
    },

    async handleRequest(requestId, action) {
      try {
        // 调用后端处理好友请求的接口
        const response = await request.post('/user/friend/handle', {
          requestId: requestId,
          action: action
        });
        if (response && response.code === 0) {
          // 处理成功后刷新列表
          this.fetchFriendRequests();
        }
      } catch (error) {
        console.error('Handle friend request failed:', error);
      }
    }
  },
  mounted() {
    this.fetchFriendRequests();
    document.body.style.overflow = 'hidden';
  }
};
</script>

<style lang="scss" scoped>
/* 基础容器设置 */
.friend-requests-page {
  position: relative;
  width: 100%;
  height: 100vh;
  background-color: #fff;
  overflow: hidden;
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
  flex-shrink: 0;
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

/* 滚动区域核心布局 */
.scroll-container {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  background-color: #fff;
  padding-bottom: 20px;

  &::-webkit-scrollbar {
    display: none;
  }
}

/* 请求列表样式 */
.request-list {
  padding: 0 16px;
}

.request-item {
  display: flex;
  padding: 16px 0;
  border-bottom: 1px solid #f8f8f8;
}

.avatar-box {
  margin-right: 12px;
}

.avatar-img {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid #eee;
}

.content-box {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.row-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
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

.row-middle {
  margin-bottom: 12px;
}

.verify-msg {
  font-size: 14px;
  color: #666;
  line-height: 1.4;
}

.row-bottom {
  display: flex;
  gap: 12px;
}

.btn-accept {
  flex: 1;
  height: 36px;
  background: #2878ff;
  color: white;
  border: none;
  border-radius: 18px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;

  &:active {
    background: #0056b3;
  }
}

.btn-reject {
  flex: 1;
  height: 36px;
  background: #f1f1f1;
  color: #666;
  border: none;
  border-radius: 18px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;

  &:active {
    background: #e0e0e0;
  }
}

/* 底部状态文字 */
.loading-tips,
.empty-tips {
  text-align: center;
  color: #999;
  padding: 40px 0;
  font-size: 14px;
}

/* Footer 固定容器 */
.footer-wrapper {
  height: 50px;
  flex-shrink: 0;
  border-top: 1px solid #eee;
  z-index: 10;
}
</style>
