<template>
  <div class="container">
    <InfiniteList
      :itemInfo="videobox"
      :itemsPerRow="3"
      :append="false"
      boxWidth="33%"
      boxHeight="250px"
      :gap="3"
      :loading="loading"
      :noMoreData="noMoreData"
      @loadMore="fetchItemDetails"
    >
      <template v-slot:header>
        <!-- 顶部背景图 -->
        <div
          class="profile-header"
          :style="{ backgroundImage: 'url(' + userInfo.backImg + ')' }"
        >
          >
          <img class="avatar" :src="userInfo.avatar" alt="头像" />
          <div class="douyinnum" style="color: white">
            <div class="user-name">{{ userInfo.nickname }}</div>
            <div class="user-id">抖音号：{{ userInfo.id }}</div>
          </div>
        </div>
        <!-- 用户统计信息 -->
        <div class="user-stats">
          <div class="stat-item">
            <div class="num">{{ userInfo.followers || '0' }}</div>
            <div class="label">粉丝</div>
          </div>
          <div class="stat-item">
            <div class="num">{{ userInfo.following || '0' }}</div>
            <div class="label">关注</div>
          </div>
          <div class="stat-item">
            <div class="num">{{ userInfo.likes || '0' }}</div>
            <div class="label">获赞</div>
          </div>
          <div class="stat-item">
            <div class="num">{{ userInfo.likes || '0' }}</div>
            <div class="label">获赞</div>
          </div>
          <el-button @click="goEditMeInfo">编辑主页</el-button>
        </div>
        <div class="userinfo">
          <div style="font-size: 15px; margin-top: 10px">
            {{ userInfo.jieshao || '暂无个人简介' }}
          </div>
          <div class="bio">
            <div class="location">{{ userInfo.city }}</div>
            <div class="school">{{ userInfo.school || '暂无学校信息' }}</div>
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
          @click="test(item)"
          class="itemdetail"
          :style="{
            backgroundImage: `url(${item.cover})`,
            width: '100%',
            height: '100%',
            backgroundSize: 'cover',
            backgroundPosition: 'center center'
          }"
        ></div>
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
  components: {
    footerVue,
    underLineTagsVue,

    InfiniteList
  },
  data() {
    return {
      parentMessage: ['视频', '评论', '点赞', '确定'],
      userInfo: {},
      loading: false,
      noMoreData: false,
      activeTabIndex: 0,
      page: 0, // 当前页码
      pageSize: 10,
      videobox: [] // 视频列表，初始为空
    };
  },
  mounted() {
    this.fetchItemDetails(1);
  },
  methods: {
    test(item) {
      this.$router.push('/videoDetail/');
    },
    handleTabChange(index) {
      this.activeTabIndex = index;
    },
    fetchItemDetails(page) {
      if (this.loading || this.noMoreData) return;
      this.loading = true;
      getUserInfo(null, page, this.pageSize)
        .then((res) => {
          const videos = res.data.videobox || [];
          const total = res.data.total || 0;
          // 追加视频数据
          this.videobox = [...this.videobox, ...videos];
          // 只在第一页赋值musicinfo，避免重复赋值
          if (page === 1) {
            this.userInfo = res.data.userInfo || {};
          }
          // 判断是否加载完毕
          if (this.videobox.length >= total) {
            this.noMoreData = true;
          }
          this.page = page; // 更新当前页码
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
          this.$toast && this.$toast('获取视频出错');
        });
    },
    goEditMeInfo() {
      this.$router.push({ path: '/editMeInfo' });
    }
  }
};
</script>
<style scoped>
.main {
  width: 95vw;
  margin: 0 auto;
  overflow-x: hidden; /* 禁止横向滚动 */
}

.recommend-item {
  width: 100%;
  box-sizing: border-box;
  break-inside: avoid;
  background: #fff;
  border-radius: 1px;
  overflow: hidden;
}

.cover {
  width: 100%;
  display: block;
  height: auto;
}

.container {
  min-height: 100vh;
  background: #f8f8f8;
}

.profile-header {
  background-size: cover;
  height: 20vh;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
}

.cover-image {
  width: 100vw;
  height: 100%;
  object-fit: cover;
}

.user-info {
  padding: 0 4vw;
  color: #fff;
}
.douyinnum {
  margin-bottom: 8vw;
  margin-left: 3vw;
}

.avatar {
  margin-bottom: 5vw;
  display: block;
  width: 25vw;
  height: 25vw;
  border-radius: 50%;
  border: 0.4vw solid #fff;
}

.user-name {
  margin-top: 1vh;
  font-size: 4vw;
  font-weight: bold;
}

.user-id {
  margin-top: 0.5vh;
  font-size: 3vw;
  opacity: 0.8;
}

.user-stats {
  display: flex;
  margin-top: 2vh;
}

.stat-item {
  flex: 1;
  text-align: center;
}

.num {
  font-size: 4vw;
  font-weight: bold;
}

.label {
  font-size: 3vw;
  opacity: 0.8;
}

.edit-profile-btn {
  margin-top: 2vh;
  padding: 1vh 4vw;
  border: 0.2vw solid #fff;
  border-radius: 5vw;
  display: inline-block;
  font-size: 3.5vw;
}

.bio {
  display: flex;
  flex-direction: row;
  margin-top: 1.5vh;
  font-size: 3.5vw;
}
.bio > div {
  margin: 2px;
  background-color: rgba(0, 0, 0, 0.1);
}

.location,
.school {
  margin-top: 0.5vh;
  font-size: 3vw;
  opacity: 0.8;
}

.content-tabs {
  margin-top: 2vh;
  background: #fff;
}

.tab-header {
  display: flex;
  border-bottom: 0.2vw solid #eee;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 2vh 0;
  font-size: 3.5vw;
}

.tab-item.active {
  color: #000;
  font-weight: bold;
  position: relative;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 6vw;
  height: 0.4vh;
  background: #fe2c55;
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0.2vw;
  background: #eee;
  padding-bottom: 10vh;
}

.video-item {
  position: relative;
  padding-bottom: 100%;
}

.video-item img {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.play-count {
  position: absolute;
  left: 1vw;
  bottom: 1vw;
  color: #fff;
  font-size: 3vw;
  display: flex;
  align-items: center;
}

.play-count .iconfont {
  font-size: 3vw;
  margin-right: 0.5vw;
}

.footer {
  background-color: black;
  color: black;
  z-index: 10;
  position: fixed;
  bottom: 0;
  width: 100%;
}
</style>
