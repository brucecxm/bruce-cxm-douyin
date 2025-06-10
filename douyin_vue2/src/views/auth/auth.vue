<template>
  <div class="auth">
    <div
      class="backimg"
      :style="{ backgroundImage: 'url(' + userInfo.backImg + ')' }"
    >
      <!-- 左侧返回按钮 -->
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
        <!-- 放大镜图标 -->
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

        <!-- 更多（三个点）图标 -->
        <div class="icon-button" @click="onMoreClick" title="更多">
          <svg viewBox="0 0 24 24" class="icon">
            <circle cx="5" cy="12"
r="2" fill="white" />
            <circle cx="12" cy="12"
r="2" fill="white" />
            <circle cx="19" cy="12"
r="2" fill="white" />
          </svg>
        </div>
      </div>
    </div>

    <div class="touxiang">
      <img :src="userInfo.avatar" alt="头像" />
    </div>
    <div class="personinfo">
      <div class="authnum">
        <span>{{ userInfo.like }} 获赞</span>
        <span>{{ userInfo.follow }} 关注</span>
        <span>{{ userInfo.fan }} 粉丝</span>
      </div>
      <div class="authinfo">信息介绍</div>
      <div class="city">
        <span>IP:{{ userInfo.city }}</span>
      </div>
      <div @click="requestFriend" class="followbutt">+ 关注</div>
    </div>
    <under-line-tags-vue
      :navItems="parentMessage"
      :active-index.sync="activeTabIndex"
      @change="handleTabChange"
    />
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
          @click="govideodetail(item.videoid)"
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
  </div>
</template>

<script>
import { getUserInfo } from '@/api/user';
import InfiniteList from '../../components/InfiniteList.vue';
import underLineTagsVue from '../../components/underLineTags.vue';
import { requestFriendApi } from '@/api/user';
export default {
  created() {
    this.userId = this.$route.params.userId;
    console.log(this.userId);
    this.fetchItemDetails(1);
  },
  data() {
    return {
      userInfo: {},
      activeTabIndex: 0,
      page: 0, // 当前页码
      pageSize: 10,
      parentMessage: ['推荐', '热门', '收藏', '用过'],
      videobox: [], // 视频列表，初始为空
      videoboxType: {
        推荐: [
          {
            videoImg:
              'https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662'
          },
          {
            videoImg:
              'https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662'
          }
        ],
        热门: [
          {
            videoImg:
              'https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662'
          }
        ],
        收藏: [],
        用过: []
      },
      userId: '',
      loading: false,
      noMoreData: false,
      videonum: '10000', // 修正拼写为 videonum
      auth: {}
    };
  },
  components: {
    InfiniteList,
    underLineTagsVue
  },
  computed: {
    filteredSongs() {
      return this.videoboxType[this.parentMessage[this.activeTabIndex]] || [];
    }
  },
  watch: {
    filteredSongs(newVal) {
      console.log('filteredSongs 更新了：', newVal);
    }
  },

  methods: {
    govideodetail(videoid) {
      this.$router.push('/videodetail');
    },
    fetchMoreData() {
      if (this.loading || this.noMoreData) return;
      this.fetchItemDetails(this.page + 1);
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
    requestFriend() {
      const User = { toUserId: this.userId, verifyMsg: '请求关注' }; // 这里的 verifyMsg 可以根据需要修改
      requestFriendApi(User).then.catch((error) => {
        console.error('请求关注失败:', error);
      });
      // 处理关注操作
      console.log('关注操作');
    },

    gohome() {
      this.$router.push('/');
    },
    onBackClick() {
      this.$router.back();
    },

    onSearchClick() {
      this.$router.push('/search');
    },

    onMoreClick() {
      // 处理更多操作
      console.log('更多操作');
    }
  }
};
</script>

<style scoped>
html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
}

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
  z-index: 1;
  background-size: cover;
  background-position: center;
}

.fanhui {
  position: absolute;
  left: 5vw;
  top: 5vh;
  width: 40px;
  height: 40px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.right-buttons {
  position: absolute;
  right: 5vw;
  top: 5vh;
  display: flex;
  gap: 10px;
}

.icon-button {
  width: 40px;
  height: 40px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.icon {
  width: 24px;
  height: 24px;
}

.touxiang {
  z-index: 10;
  width: 25vw;
  height: 25vw;
  border-radius: 50%;
  overflow: hidden;
  position: absolute;
  top: 7vh;
  left: 10px;
  border: 3px solid white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.touxiang img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.personinfo {
  width: 100%;
  height: 30%;
  position: relative;
  background-color: white;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  z-index: 2;
  margin-top: -5vh;
  padding: 15px;
  box-sizing: border-box;
}

.authnum {
  font-size: 0.6rem;
  display: flex;
  margin-bottom: 10px;
}

.authnum span {
  font-weight: bold;
  color: #333;
}

.city {
  font-size: 0.4rem;
  background-color: rgba(0, 0, 0, 0.1);
  width: 10vw;
  padding: 5px;
  border-radius: 2px;
  text-align: center;
  margin-top: 10px;
}

.authinfo {
  font-size: 0.7rem;
  color: #666;
  margin-top: 15px;
}

.followbutt {
  width: 90%;
  height: 5vh;
  background-color: #fe1e1e;
  margin: 15px auto;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 30px;
  color: white;
  font-size: 0.8rem;
  cursor: pointer;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.videonum {
  font-size: 0.5rem;
  margin-top: 15px;
  color: #333;
  margin-bottom: 0;
  position: absolute;
  bottom: 0;
}

/* 你可以根据需要补充 InfiniteList 内部元素样式 */
</style>
