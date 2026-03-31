<template>
  <div class="me-page-container">
    <!-- 1. 顶部背景与导航区 -->
    <div
      class="header-bg"
      :style="{
        backgroundImage: userInfo.backImg
          ? `url(${userInfo.backImg})`
          : 'linear-gradient(135deg, #2c3e50 0%, #000000 100%)'
      }"
    >
      <div class="top-nav">
        <div class="left-action">
          <!-- 这里的按钮可以绑定侧边栏逻辑 -->
          <span class="icon-btn">👤+</span>
        </div>
        <div class="right-actions">
          <span class="icon-btn" @click="goWallet">🔍</span>
          <span class="icon-btn">☰</span>
        </div>
      </div>

      <!-- 用户基础信息 -->
      <div class="user-intro-area">
        <div class="avatar-wrapper">
          <img
            :src="userInfo.avatar || defaultAvatar"
            class="avatar-img"
            alt="头像"
            @click="goEditMeInfo"
          />
        </div>

        <div class="user-text-info">
          <div class="name-row">
            <span class="username">{{
              userInfo.nickname || '未设置昵称'
            }}</span>
          </div>
          <div class="douyin-id">抖音号：{{ userInfo.id || 'Loading...' }}</div>
        </div>
      </div>
    </div>

    <!-- 2. 简介与标签 (移动到头部下方，背景白色) -->
    <div class="info-section">
      <!-- 简介 -->
      <div class="bio-text">
        {{ userInfo.myIntro || userInfo.jieshao || '暂无个人简介' }}
      </div>

      <!-- 标签行 (年龄、城市、学校、性别、生日) -->
      <div class="tags-row">
        <span v-if="userInfo.age" class="tag">Age: {{ userInfo.age }}</span>
        <span v-if="userInfo.city" class="tag">{{ userInfo.city }}</span>
        <span v-if="userInfo.school" class="tag">{{ userInfo.school }}</span>
        <span
          v-if="userInfo.gender && userInfo.gender !== '不展示'"
          class="tag"
        >{{ userInfo.gender }}</span
        >
        <span
          v-if="userInfo.birthday && userInfo.birthday !== '不展示'"
          class="tag"
        >{{ userInfo.birthday }}</span
        >
        <span class="tag add-tag" @click="goEditMeInfo">+ 编辑资料</span>
      </div>

      <!-- 3. 数据统计 -->
      <div class="stats-row">
        <div class="stat-item" @click="gofriend('likes')">
          <span class="num">{{ formatNumber(userInfo.likes || 0) }}</span>
          <span class="label">获赞</span>
        </div>
        <div class="stat-item" @click="gofriend('mutualFollow')">
          <span class="num">{{
            formatNumber(userInfo.mutualFollow || 0)
          }}</span>
          <span class="label">互关</span>
        </div>
        <div class="stat-item" @click="gofriend('following')">
          <span class="num">{{ formatNumber(userInfo.following || 0) }}</span>
          <span class="label">关注</span>
        </div>
        <div class="stat-item" @click="gofriend('followers')">
          <span class="num">{{ formatNumber(userInfo.followers || 0) }}</span>
          <span class="label">粉丝</span>
        </div>
      </div>
    </div>

    <!-- 4. 选项卡 (替代原 underLineTagsVue) -->
    <div class="tabs-nav sticky-tabs">
      <div
        v-for="(tab, index) in parentMessage"
        :key="index"
        class="tab-item"
        :class="{ active: activeTabIndex === index }"
        @click="handleTabChange(index)"
      >
        <span>{{ tab }}</span>
        <div class="active-line" v-show="activeTabIndex === index"></div>
      </div>
    </div>

    <!-- 5. 内容列表区域 (替代原 InfiniteList) -->
    <div class="content-grid">
      <!-- 加载中 -->
      <div
        v-if="loading && currentDisplayData.length === 0"
        class="loading-placeholder"
      >
        加载中...
      </div>

      <!-- 空状态 -->
      <div
        v-else-if="currentDisplayData.length === 0"
        class="empty-placeholder"
      >
        {{ getEmptyText() }}
      </div>

      <!-- 视频/内容网格 -->
      <div
        v-else
        class="grid-item"
        v-for="(item, index) in currentDisplayData"
        :key="item.id || index"
        @click="navigateToDetail(item)"
      >
        <!-- 封面图 -->
        <img :src="item.cover" class="cover-img" loading="lazy" />

        <!-- 左下角数据 (点赞或播放量) -->
        <div class="item-stats-overlay">
          <span
v-if="activeTabIndex === 0"
          >▶ {{ formatNumber(item.playCount || 0) }}</span
          >
          <span v-else>❤ {{ formatNumber(item.likeCount || 0) }}</span>
        </div>
      </div>
    </div>

    <!-- 加载更多触发器 (简化版) -->
    <div
      v-if="!noMoreData && currentDisplayData.length > 0"
      class="load-more-btn"
      @click="loadMoreData(page + 1)"
    >
      点击加载更多
    </div>
    <div
      v-if="noMoreData && currentDisplayData.length > 0"
      class="no-more-text"
    >
      - 到底了 -
    </div>

    <!-- 6. 底部组件 -->
    <footer-vue class="footer"></footer-vue>
  </div>
</template>

<script>
// 保留你要求的 footer 组件
import footerVue from '@/components/home/footer.vue';
// 引入 API
import { getUserInfo } from '@/api/user';

export default {
  name: 'MePage',
  components: {
    footerVue
  },
  data() {
    return {
      // 这里的 Tabs 对应你的逻辑
      parentMessage: ['作品', '私密', '喜欢'], // 根据抖音风格稍微改了下文案，逻辑不变
      userInfo: {},
      loading: false,
      activeTabIndex: 0,
      page: 1,
      pageSize: 10,

      // 数据容器
      videobox: [], // 作品
      commentList: [], // 私密/评论 (复用你的逻辑变量名)
      likeList: [], // 喜欢

      defaultAvatar: 'https://ui-avatars.com/api/?background=0D8ABC&color=fff',
      hasLoadedAllTabs: {
        videos: false,
        comments: false, // 对应 Tab 1
        likes: false // 对应 Tab 2
      }
    };
  },
  computed: {
    currentDisplayData() {
      switch (this.activeTabIndex) {
        case 0:
        return this.videobox;
        case 1:
        return this.commentList;
        case 2:
        return this.likeList;
        default:
        return [];
      }
    },
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
    this.loadInitialData();
  },
  methods: {
    // ---- 原始逻辑方法 ----

    formatDuration(seconds) {
      if (!seconds || isNaN(seconds)) return '0:00';
      const mins = Math.floor(seconds / 60);
      const secs = Math.floor(seconds % 60);
      return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
    },

    formatNumber(num) {
      if (!num) return '0';
      if (num >= 10000) return (num / 10000).toFixed(1) + 'w';
      if (num >= 1000) return (num / 1000).toFixed(1) + 'k';
      return num.toString();
    },

    loadInitialData() {
      this.loading = true;
      this.fetchUserAndVideosData(1);
    },

    // 核心数据获取逻辑
    fetchUserAndVideosData(page) {
      // 注意：这里为了防止报错，加了 try-catch 或空判断，实际运行依赖你的 api
      getUserInfo(null, page, this.pageSize, 'empty', false)
        .then((res) => {
          if (res && res.data) {
            if (page === 1) {
              this.userInfo = res.data;
            }

            const videos = res.data.videoPage?.records || [];
            const total = res.data.total || 0;

            if (page === 1) {
              this.videobox = videos;
            } else {
              this.videobox = [...this.videobox, ...videos];
            }

            if (this.videobox.length >= total) {
              this.hasLoadedAllTabs.videos = true;
            }
            this.page = page;
          }
        })
        .catch((error) => {
          console.error('获取信息失败', error);
        })
        .finally(() => {
          this.loading = false;
        });
    },

    loadMoreData(nextPage) {
      if (this.loading || this.noMoreData) return;
      this.loading = true;

      if (this.activeTabIndex === 0) {
        this.fetchUserAndVideosData(nextPage);
      } else if (this.activeTabIndex === 1) {
        this.fetchComments(nextPage);
      } else {
        this.fetchLikes(nextPage);
      }
    },

    // 模拟的 Fetch 方法 (你需要替换回真实的 API)
    fetchComments(page) {
      setTimeout(() => {
        // 模拟空数据或加载完成
        this.hasLoadedAllTabs.comments = true;
        this.loading = false;
      }, 500);
    },

    fetchLikes(page) {
      setTimeout(() => {
        this.hasLoadedAllTabs.likes = true;
        this.loading = false;
      }, 500);
    },

    handleTabChange(index) {
      if (index !== this.activeTabIndex) {
        this.activeTabIndex = index;
        // 如果切到的 Tab 没数据，尝试加载
        if (index === 1 && this.commentList.length === 0) this.fetchComments(1);
        if (index === 2 && this.likeList.length === 0) this.fetchLikes(1);
      }
    },

    navigateToDetail(item) {
      if (!item) return;
      this.$router.push(`/videoDetail/${item.id}`);
    },

    getEmptyText() {
      const texts = ['留下你的第一个作品吧', '暂无私密作品', '还没喜欢过视频'];
      return texts[this.activeTabIndex] || '暂无数据';
    },

    // ---- 路由跳转 ----
    goEditMeInfo() {
      this.$router.push({ path: '/editMeInfo' });
    },
    goWallet() {
      this.$router.push('/wallet');
    },
    gofriend(type) {
      this.$router.push({ path: '/friend', query: { type } });
    }
  }
};
</script>

<style lang="scss" scoped>
/* 全局容器 */
.me-page-container {
  min-height: 100vh;
  background-color: #fff;
  padding-bottom: 60px; /* 留给 footer 的空间 */
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 1. 顶部背景区 */
.header-bg {
  width: 100%;
  height: 200px;
  background-size: cover;
  background-position: center;
  position: relative;
  color: white;
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

/* 顶部导航图标 */
.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 2;
}

.icon-btn {
  background: rgba(0, 0, 0, 0.3);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  cursor: pointer;
}

.right-actions {
  display: flex;
  gap: 10px;
}

/* 头像和名字布局 (左对齐) */
.user-intro-area {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: -40px; /* 让头像一半在背景里，一半在白色区域 */
  z-index: 2;
}

.avatar-wrapper {
  width: 90px;
  height: 90px;
  border-radius: 50%;
  border: 4px solid #fff; /* 白色边框 */
  background: #fff;
  overflow: hidden;
  flex-shrink: 0;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-text-info {
  margin-top: -20px; /* 调整文字位置 */
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.username {
  font-size: 20px;
  font-weight: bold;
  color: white;
}

.douyin-id {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
  margin-top: 4px;
}

/* 2. 信息展示区 */
.info-section {
  padding: 50px 16px 10px; /* padding-top 50px 是为了避开下沉的头像 */
  background: #fff;
}

.bio-text {
  font-size: 15px;
  color: #161823;
  margin-bottom: 12px;
  line-height: 1.4;
}

.tags-row {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 20px;
}

.tag {
  background: #f1f1f1;
  color: #666;
  font-size: 11px;
  padding: 4px 8px;
  border-radius: 2px;
}

.add-tag {
  background: rgba(0, 0, 0, 0.05);
  font-weight: 500;
}

/* 3. 数据统计行 */
.stats-row {
  display: flex;
  justify-content: flex-start;
  gap: 25px;
}

.stat-item {
  display: flex;
  align-items: baseline;
  gap: 4px;
  cursor: pointer;
}

.num {
  font-size: 18px;
  font-weight: bold;
  color: #161823;
}

.label {
  font-size: 13px;
  color: #888;
}

/* 4. 选项卡 Sticky */
.sticky-tabs {
  position: sticky;
  top: 0;
  background: #fff;
  z-index: 10;
  border-bottom: 1px solid #f1f1f1;
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 14px 0;
  font-size: 15px;
  color: #888;
  position: relative;
  cursor: pointer;
}

.tab-item.active {
  color: #161823;
  font-weight: 600;
}

.active-line {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 40px;
  height: 2px;
  background: #face15; /* 抖音黄 */
}

/* 5. 视频网格布局 */
.content-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 强制三列 */
  gap: 1px;
  background: #fff;
}

.grid-item {
  position: relative;
  padding-bottom: 133%; /* 3:4 比例，模拟手机视频 */
  background: #f8f8f8;
  overflow: hidden;
  cursor: pointer;
}

.cover-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-stats-overlay {
  position: absolute;
  bottom: 6px;
  left: 6px;
  color: white;
  font-size: 12px;
  font-weight: 600;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
}

/* 空状态和加载更多 */
.empty-placeholder,
.loading-placeholder {
  grid-column: 1 / -1; /* 占满整行 */
  text-align: center;
  padding: 60px 0;
  color: #999;
  font-size: 14px;
}

.load-more-btn {
  text-align: center;
  padding: 15px;
  color: #666;
  font-size: 13px;
  cursor: pointer;
}

.no-more-text {
  text-align: center;
  padding: 15px;
  color: #ccc;
  font-size: 12px;
}
</style>
