<template>
  <div class="container">
    <!-- 加载状态遮罩 -->
    <div v-if="loading && videobox.length === 0" class="loading-overlay">
      <div class="loading-spinner"></div>
      <div class="loading-text">加载中...</div>
    </div>

    <InfiniteList
      v-if="!loading || videobox.length > 0"
      :itemInfo="currentDisplayData"
      :itemsPerRow="3"
      :append="false"
      boxWidth="33%"
      boxHeight="250px"
      :gap="3"
      :loading="loading"
      :noMoreData="noMoreData"
      @loadMore="loadMoreData"
    >
      <template v-slot:header>
        <!-- 顶部背景图 -->
        <div
          class="profile-header"
          :style="{
            backgroundImage: 'url(' + (userInfo.backImg || defaultBackImg) + ')'
          }"
        >
          <img
            class="avatar"
            :src="userInfo.avatar || defaultAvatar"
            alt="头像"
          />
          <div class="douyinnum" style="color: white">
            <div class="user-name">{{ userInfo.nickname || '未设置昵称' }}</div>
            <div class="user-id">抖音号：{{ userInfo.id }}</div>
          </div>
        </div>
        <!-- 用户统计信息 -->
        <div class="user-stats">
          <div class="stat-item" @click="gofriend('followers')">
            <div class="num">{{ userInfo.followers || '0' }}</div>
            <div class="label">粉丝</div>
          </div>
          <div class="stat-item" @click="gofriend('following')">
            <div class="num">{{ userInfo.following || '0' }}</div>
            <div class="label">关注</div>
          </div>
          <div class="stat-item" @click="gofriend('likes')">
            <div class="num">{{ userInfo.likes || '0' }}</div>
            <div class="label">获赞</div>
          </div>
          <button class="edit-home-btn">编辑主页</button>
          <!-- <el-button
class="edit-btn"
type="primary" @click="goEditMeInfo"
          >编辑主页</el-button
          > -->
        </div>
        <div class="userinfo">
          <div class="intro" style="font-size: 15px; margin-top: 10px">
            {{ userInfo.myIntro || userInfo.jieshao || '暂无个人简介' }}
          </div>
          <div class="bio">
            <div v-if="userInfo.city" class="location">
              🏙️ {{ userInfo.city }}
            </div>
            <div v-if="userInfo.school" class="school">
              🎓 {{ userInfo.school }}
            </div>
            <div v-if="userInfo.age" class="age">📅 {{ userInfo.age }}岁</div>
            <div v-if="userInfo.isAnchor" class="anchor">⚓ 主播</div>
          </div>
        </div>
        <under-line-tags-vue
          :navItems="parentMessage"
          :active-index.sync="activeTabIndex"
          @change="handleTabChange"
        ></under-line-tags-vue>
      </template>
      <template #item="{ item }">
        <div
          @click="navigateToDetail(item)"
          class="itemdetail"
          :style="{
            backgroundImage: `url(${item.cover})`,
            width: '100%',
            height: '100%',
            backgroundSize: 'cover',
            backgroundPosition: 'center center'
          }"
        >
          <!-- 显示播放量或点赞数 -->
          <div class="item-stats">
            <span v-if="activeTabIndex === 0" class="play-count">{{
              formatNumber(item.playCount || 0)
            }}</span>
            <span v-else class="like-count">{{
              formatNumber(item.likeCount || 0)
            }}</span>
          </div>
        </div>
      </template>
      <template #empty>
        <div class="empty-state">
          <div class="empty-icon">
            {{
              activeTabIndex === 0 ? '📹' : activeTabIndex === 1 ? '💬' : '❤️'
            }}
          </div>
          <div class="empty-text">{{ getEmptyText() }}</div>
        </div>
      </template>
    </InfiniteList>

    <footer-vue class="footer"></footer-vue>
  </div>
</template>
<script>
import InfiniteList from '../../components/InfiniteList.vue';
import footerVue from '@/components/footer.vue';
import underLineTagsVue from '../../components/underLineTags.vue';
import { getUserInfo } from '@/api/user';
export default {
  name: 'MePage',
  components: {
    footerVue,
    underLineTagsVue,
    InfiniteList
  },
  data() {
    return {
      parentMessage: ['视频', '评论', '点赞'],
      userInfo: {},
      loading: false,
      activeTabIndex: 0,
      page: 0, // 当前页码
      pageSize: 10,
      videobox: [], // 视频列表，初始为空
      commentList: [], // 评论列表
      likeList: [], // 点赞列表
      defaultAvatar: 'https://via.placeholder.com/150', // 默认头像
      defaultBackImg: 'https://via.placeholder.com/800x300', // 默认背景图
      hasLoadedAllTabs: {
        videos: false,
        comments: false,
        likes: false
      }
    };
  },
  computed: {
    // 根据当前选中的标签返回对应的数据
    currentDisplayData() {
      switch (this.activeTabIndex) {
        case 0: // 视频
          return this.videobox;
        case 1: // 评论
          return this.commentList;
        case 2: // 点赞
          return this.likeList;
        default:
          return this.videobox;
      }
    },

    // 根据当前标签判断是否已加载完毕
    noMoreData() {
      switch (this.activeTabIndex) {
        case 0:
          return this.hasLoadedAllTabs.videos;
        case 1:
          return this.hasLoadedAllTabs.comments;
        case 2:
          return this.hasLoadedAllTabs.likes;
        default:
          return false;
      }
    }
  },
  mounted() {
    console.log('MePage mounted');
    this.loadInitialData();
  },
  methods: {
    // 加载初始数据
    loadInitialData() {
      this.loading = true;
      this.fetchUserAndVideosData(1);
    },

    // 加载更多数据
    loadMoreData(page) {
      if (this.loading) return;

      this.loading = true;
      // 根据当前标签加载对应的数据
      switch (this.activeTabIndex) {
        case 0: // 视频
          this.fetchVideos(page);
          break;
        case 1: // 评论
          this.fetchComments(page);
          break;
        case 2: // 点赞
          this.fetchLikes(page);
          break;
      }
    },

    // 获取用户信息和视频数据
    fetchUserAndVideosData(page) {
      getUserInfo(null, page, this.pageSize)
        .then((res) => {
          if (res && res.data) {
            // 只在第一页赋值用户信息
            if (page === 1) {
              // 避免不必要的更新导致的重渲染
              const newUserInfo = res.data || {};
              // 只有当数据发生变化时才更新
              if (
                JSON.stringify(newUserInfo) !== JSON.stringify(this.userInfo)
              ) {
                this.userInfo = newUserInfo;
              }
            }

            // 处理视频数据
            const videos = res.data.videoPage.records || [];
            const total = res.data.total || 0;

            if (page === 1) {
              this.videobox = videos;
            } else {
              this.videobox = [...this.videobox, ...videos];
            }

            // 判断是否加载完毕
            if (this.videobox.length >= total) {
              this.hasLoadedAllTabs.videos = true;
            }

            this.page = page;
          }
        })
        .catch((error) => {
          console.error('获取用户信息失败:', error);
          this.$toast && this.$toast('获取用户信息失败，请稍后重试');
        })
        .finally(() => {
          this.loading = false;
        });
    },

    // 获取视频列表
    fetchVideos(page) {
      // 复用fetchUserAndVideosData方法
      this.fetchUserAndVideosData(page);
    },

    // 获取评论列表（这里是示例，实际需要调用对应的API）
    fetchComments(page) {
      // 模拟API调用
      setTimeout(() => {
        // 实际项目中应调用真实API
        this.commentList = page === 1 ? [] : this.commentList;
        this.hasLoadedAllTabs.comments = true;
        this.loading = false;
      }, 500);
    },

    // 获取点赞列表（这里是示例，实际需要调用对应的API）
    fetchLikes(page) {
      // 模拟API调用
      setTimeout(() => {
        // 实际项目中应调用真实API
        this.likeList = page === 1 ? [] : this.likeList;
        this.hasLoadedAllTabs.likes = true;
        this.loading = false;
      }, 500);
    },

    // 跳转到朋友页面，可以传递类型参数
    gofriend(type) {
      this.$router.push({
        path: '/friend',
        query: { type }
      });
    },

    // 导航到详情页
    navigateToDetail(item) {
      if (!item) return;

      switch (this.activeTabIndex) {
        case 0: // 视频
          this.$router.push(`/videoDetail/${item.id || ''}`);
          break;
        case 1: // 评论
          // 导航到评论所在的视频
          this.$router.push(
          `/videoDetail/${item.videoId || ''}?commentId=${item.id || ''}`
        );
          break;
        case 2: // 点赞
          // 导航到点赞的视频
          this.$router.push(`/videoDetail/${item.id || ''}`);
          break;
      }
    },

    // 处理标签页切换
    handleTabChange(index) {
      // 如果切换到新标签，且该标签数据未加载过，则加载数据
      if (index !== this.activeTabIndex) {
        this.activeTabIndex = index;

        // 重置加载状态
        this.loading = false;

        // 根据切换的标签加载对应数据
        switch (index) {
          case 0:
            if (!this.hasLoadedAllTabs.videos && this.videobox.length === 0) {
              this.fetchVideos(1);
            }
            break;
          case 1:
            if (
            !this.hasLoadedAllTabs.comments &&
              this.commentList.length === 0
          ) {
              this.fetchComments(1);
            }
            break;
          case 2:
            if (!this.hasLoadedAllTabs.likes && this.likeList.length === 0) {
              this.fetchLikes(1);
            }
            break;
        }
      }
    },

    // 格式化数字显示（如：1000 -> 1k）
    formatNumber(num) {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + 'w';
      } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'k';
      }
      return num.toString();
    },

    // 获取空状态文本
    getEmptyText() {
      switch (this.activeTabIndex) {
        case 0:
          return '还没有发布视频';
        case 1:
          return '还没有评论';
        case 2:
          return '还没有点赞内容';
        default:
          return '暂无数据';
      }
    },

    // 跳转到编辑主页
    goEditMeInfo() {
      this.$router.push({ path: '/editMeInfo' });
    },

    // 跳转到订单列表
    goToOrderList() {
      this.$router.push({ path: '/shop/orderlist' });
    },

    // 跳转到收藏页面
    goToCollect() {
      this.$router.push({ path: '/collect' });
    }
  }
};
</script>
<style scoped>
.container {
  min-height: 100vh;
  background: #f8f8f8;
  position: relative;
  padding-bottom: 60px; /* 为底部导航栏留出空间 */
}

/* 加载遮罩 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top-color: #fe2c55;
  animation: spin 1s ease-in-out infinite;
}

.loading-text {
  margin-top: 10px;
  color: #666;
  font-size: 14px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 个人资料头部 */
.profile-header {
  background-size: cover;
  background-position: center;
  height: 20vh;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  position: relative;
  padding-bottom: 20px;
  padding-left: 15px;
}

.profile-header::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50%;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.3));
}

.douyinnum {
  margin-bottom: 8vw;
  margin-left: 3vw;
  position: relative;
  z-index: 1;
}

.avatar {
  margin-bottom: 5vw;
  display: block;
  width: 25vw;
  height: 25vw;
  max-width: 100px;
  max-height: 100px;
  border-radius: 50%;
  border: 3px solid #fff;
  object-fit: cover;
  position: relative;
  z-index: 1;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.user-name {
  margin-top: 1vh;
  font-size: 4vw;
  font-weight: bold;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.user-id {
  margin-top: 0.5vh;
  font-size: 3vw;
  opacity: 0.9;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

/* 用户统计信息 */
.user-stats {
  display: flex;
  margin-top: 2vh;
  background: white;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.stat-item {
  flex: 1;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 5px 0;
}

.stat-item:active {
  background-color: rgba(0, 0, 0, 0.05);
}

.num {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.label {
  font-size: 12px;
  color: #666;
  margin-top: 2px;
}

.edit-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 4px 12px;
  font-size: 12px;
  border-radius: 20px;
  background: rgba(0, 0, 0, 0.3);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.5);
  z-index: 2;
}

/* 用户信息 */
.userinfo {
  background: white;
  padding: 15px;
  margin-bottom: 10px;
}

.intro {
  font-size: 14px;
  color: #333;
  line-height: 1.5;
  margin-bottom: 10px;
}

.bio {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.bio > div {
  padding: 4px 8px;
  background-color: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
  font-size: 12px;
  color: #666;
}

/* 内容项样式 */
.itemdetail {
  position: relative;
  overflow: hidden;
  transition: transform 0.3s ease;
}

.itemdetail:active {
  transform: scale(0.98);
}

.item-stats {
  position: absolute;
  bottom: 5px;
  right: 5px;
  color: white;
  font-size: 12px;
  background: rgba(0, 0, 0, 0.5);
  padding: 2px 6px;
  border-radius: 10px;
}

/* 空状态样式 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 10px;
}

.empty-text {
  font-size: 14px;
}

/* 底部导航栏 */
.footer {
  background-color: white;
  color: #333;
  z-index: 10;
  position: fixed;
  bottom: 0;
  width: 100%;
  box-shadow: 0 -1px 5px rgba(0, 0, 0, 0.1);
}

/* 响应式优化 */
@media (min-width: 768px) {
  .container {
    max-width: 768px;
    margin: 0 auto;
    padding-bottom: 70px;
  }

  .profile-header {
    height: 180px;
    padding-bottom: 30px;
  }

  .avatar {
    width: 120px;
    height: 120px;
    margin-bottom: 20px;
  }

  .user-name {
    font-size: 20px;
  }

  .user-id {
    font-size: 14px;
  }

  .num {
    font-size: 20px;
  }

  .label {
    font-size: 14px;
  }
}

.edit-home-btn {
  /* 按钮基础样式 */
  padding: 8px 16px;
  border: none;
  margin-right: 10px;
  border-radius: 4px; /* 圆角效果 */
  background-color: #f5f5f5; /* 浅灰色背景 */
  color: #333; /* 文字颜色 */
  font-size: 14px; /* 文字大小 */
  cursor: pointer; /* 鼠标悬浮时的指针样式 */
}
.edit-home-btn:hover {
  /* 悬浮时的交互效果（可选） */
  background-color: #e9e9e9;
}
</style>
