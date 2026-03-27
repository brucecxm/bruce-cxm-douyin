<template>
  <div class="auth">
    <!-- 顶部背景图 -->
    <div
      class="backimg"
      :style="{ backgroundImage: `url(${userInfo.backImg})` }"
    >
      <div class="back-head">
        <!-- 返回按钮 -->
        <div class="fanhui" @click="onBackClick" title="返回">
          <svg viewBox="0 0 24 24" class="icon">
            <path
              d="M15 18l-6-6 6-6"
              stroke="white"
              stroke-width="2"
              fill="none"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
          </svg>
        </div>
        <!-- 右侧操作栏 -->
        <div class="right-buttons">
          <div class="icon-button" @click="onSearchClick" title="搜索">
            <svg viewBox="0 0 24 24" class="icon">
              <circle
                cx="11"
                cy="11"
                r="7"
                stroke="white"
                stroke-width="2"
                fill="none"
              />
              <line
                x1="16.5"
                y1="16.5"
                x2="21"
                y2="21"
                stroke="white"
                stroke-width="2"
              />
            </svg>
          </div>
          <div class="icon-button" @click="onMoreClick" title="更多">
            <svg viewBox="0 0 24 24" class="icon">
              <circle
cx="5"
cy="12" r="2"
fill="white" />
              <circle
cx="12"
cy="12" r="2"
fill="white" />
              <circle
cx="19"
cy="12" r="2"
fill="white" />
            </svg>
          </div>
        </div>
      </div>
      <!-- 用户头像 -->
      <div class="touxiang">
        <img
          :src="
            userInfo.avatar
              ? userInfo.avatar
              : 'https://p3-pc.douyinpic.com/img/aweme-avatar/mosaic-legacy_3795_3047680722~c5_300x300.jpeg?from=2956013662'
          "
          alt="用户头像"
        />
      </div>
    </div>

    <!-- 用户信息 -->
    <div class="personinfo">
      <div class="authnum">
        <div class="auth-item">
          <div class="auth-value">{{ userInfo.like || 0 }}</div>
          <div class="auth-label">获赞</div>
        </div>
        <div class="auth-item">
          <div class="auth-value">{{ userInfo.follow || 0 }}</div>
          <div class="auth-label">关注</div>
        </div>
        <div class="auth-item">
          <div class="auth-value">{{ userInfo.fan || 0 }}</div>
          <div class="auth-label">粉丝</div>
        </div>
      </div>
      <div class="authinfo">{{ userInfo.intro || '信息介绍' }}</div>
      <div class="city">IP: {{ userInfo.city || '未知' }}</div>
      <div class="followbutt" @click="requestFriend">+ 关注</div>
    </div>

    <!-- 标签切换 -->
    <under-line-tags-vue
      :navItems="parentMessage"
      :active-index.sync="activeTabIndex"
      @change="handleTabChange"
    />

    <!-- 视频无限加载列表 -->
    <InfiniteList
      :itemInfo="videobox"
      :itemsPerRow="3"
      :append="false"
      boxWidth="33%"
      boxHeight="250px"
      :gap="3"
      :loading="loading"
      :noMoreData="noMoreData"
      @loadMore="fetchMoreData"
    >
      <template #item="{ item }">
        <div
          class="itemdetail"
          @click="goVideoDetail(item.videoid)"
          :style="{
            backgroundImage: `url(${item.cover})`,
            width: '100%',
            height: '100%',
            backgroundSize: 'cover',
            backgroundPosition: 'center'
          }"
        ></div>
      </template>
    </InfiniteList>
  </div>
</template>

<script>
import { getUserInfo, requestFriendApi } from '@/api/user';
import InfiniteList from '@/components/InfiniteList.vue';
import underLineTagsVue from '@/components/underLineTags.vue';

export default {
  components: { InfiniteList, underLineTagsVue },
  data() {
    return {
      userId: '',
      userInfo: {},
      videobox: [],
      parentMessage: ['推荐', '热门', '收藏', '用过'],
      activeTabIndex: 0,
      queryData: {
        userId: '',
        page: 1,
        pageSize: 10
      },
      loading: false,
      noMoreData: false
    };
  },
  created() {
    this.userId = this.$route.params.userId;
    this.queryData.userId = this.userId;
    this.fetchItemDetails();
  },
  methods: {
    fetchItemDetails() {
      if (this.loading || this.noMoreData) return;
      this.loading = true;

      getUserInfo(
        this.queryData.userId,
        this.queryData.page,
        this.queryData.pageSize,
        'empty',
        false
      )
        .then((res) => {
          const videos = res.data.videoPage?.records || [];
          const total = res.data.total || 0;

          if (this.queryData.page === 1) {
            this.userInfo = res.data || {};
          }

          this.videobox.push(...videos);

          if (this.videobox.length >= total) {
            this.noMoreData = true;
          }

          this.queryData.page += 1;
        })
        .catch(() => {
          this.$toast && this.$toast('获取用户信息失败');
        })
        .finally(() => {
          this.loading = false;
        });
    },
    fetchMoreData() {
      if (!this.loading && !this.noMoreData) {
        this.fetchItemDetails();
      }
    },
    handleTabChange(index) {
      this.activeTabIndex = index;
      this.resetList();
    },
    resetList() {
      this.queryData.page = 1;
      this.videobox = [];
      this.noMoreData = false;
      this.fetchItemDetails();
    },
    requestFriend() {
      const payload = { toUserId: this.userId, verifyMsg: '请求关注' };
      requestFriendApi(payload)
        .then(() => {
          this.$toast && this.$toast('关注请求已发送');
        })
        .catch(() => {
          this.$toast && this.$toast('关注请求失败');
        });
    },
    goVideoDetail(videoid) {
      this.$router.push(`/videodetail/${videoid}`);
    },
    onBackClick() {
      this.$router.back();
    },
    onSearchClick() {
      this.$router.push('/search');
    },
    onMoreClick() {
      this.$toast && this.$toast('更多功能待开发');
    }
  }
};
</script>

<style scoped>
.auth {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  background-color: #f8f8f8;
}

.backimg {
  width: 100%;
  height: 30vh;
  position: relative;
  background-size: cover;
  background-position: center;
}

.back-head {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.fanhui,
.icon-button {
  width: 40px;
  height: 40px;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.fanhui {
  left: 5vw;
}

.right-buttons {
  right: 5vw;
  display: flex;
  gap: 10px;
}

.icon {
  width: 24px;
  height: 24px;
}

.touxiang {
  position: absolute;
  top: 15vh;
  left: 10px;
  width: 25vw;
  height: 25vw;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.touxiang img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.personinfo {
  padding: 20px;
  background: white;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

.authnum {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.auth-item {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.auth-value {
  font-size: 0.5rem;
  font-weight: bold;
  margin-bottom: 3px;
}

.auth-label {
  font-size: 0.5rem;
  color: #666;
}

.authinfo {
  font-size: 0.5rem;
  color: #333;
  margin-bottom: 10px;
  line-height: 1.4;
}

.city {
  font-size: 0.4rem;
  background: rgba(0, 0, 0, 0.05);
  padding: 5px 10px;
  border-radius: 3px;
  display: inline-block;
  margin-bottom: 15px;
}

.followbutt {
  width: 100%;
  background: #fe1e1e;
  color: white;
  border-radius: 30px;
  text-align: center;
  padding: 12px 0;
  cursor: pointer;
  font-size: 0.5rem;
  font-weight: bold;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
}

.followbutt::after {
  content: '▼';
  font-size: 0.5rem;
  transform: rotate(90deg);
}

.itemdetail {
  cursor: pointer;
}
</style>
