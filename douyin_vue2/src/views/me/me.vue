<template>
  <div class="container">
    <!-- 顶部背景图 -->
    <div class="profile-header " :style="{ backgroundImage: 'url(' + userInfo.backImg + ')' }">

 
        <img class="avatar" :src="userInfo.avatar" alt="头像" />
        <img class="avatar" :src="userInfo.avatar" alt="头像" />
        <div class="douyinnum">
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
        <!-- <div class="edit-profile-btn" @click="goEditMeInfo">编辑主页</div> -->
        <!-- 个人简介 -->
        <div style="font-size: 15px; margin-top: 10px;">{{ userInfo.jieshao || '暂无个人简介' }}</div>

        <div class="bio">

          <div class="location">{{ userInfo.city }}</div>
          <div class="school">{{ userInfo.school || '暂无学校信息' }}</div>
        </div>
    <!-- 内容标签页 -->
    <div class="content-tabs">
      <under-line-tags-vue :navItems="parentMessage"></under-line-tags-vue>

      <!-- 视频列表 -->
      <div class="video-grid">
        <div
          class="video-item"
          v-for="(video, index) in userInfo.videoList"
          :key="index"
        >
          <img :src="video.videoUrl" alt="视频封面" />
          <div class="play-count">
            <i class="iconfont icon-bofang1"></i>
            <span>{{ video.playCount }}</span>
          </div>
        </div>
      </div>
    </div>

    <footer-vue class="footer"></footer-vue>
  </div>
</template>
<script>
import footerVue from '@/components/footer.vue';
import underLineTagsVue from '../../components/underLineTags.vue';

import {
  userInfoService
} from '@/api/user';

export default {
  components: {
    footerVue,
    underLineTagsVue
  },
  data() {
    return {
      parentMessage:['视频', '评论', '点赞','确定'],
      userInfo: {},
    };
  },
  mounted() {
    // this.getUserInfo();
    this.getUserInfojia();
  },
  methods: {
    getUserInfojia() {
    // 假数据
    this.userInfo = {
      id: '12345678',
      nickname: '测试用户',
      avatar: 'http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960',
      backImg: 'http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960',
      followers: 888,
      following: 123,
      likes: 9999,
      jieshao: '这是一段个人简介。',
      city: '西安',
      school: '西安财经大学',
      videoList: [
        {
          videoUrl: 'http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960',
          playCount: 1234
        },
        {
          videoUrl: 'http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960',
          playCount: 5678
        },
        {
          videoUrl: 'http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960',
          playCount: 9999
        }
      ]
    };
  },
    goEditMeInfo() {
      this.$router.push({ path: '/editMeInfo' });
    },
    getUserInfo() {
      userInfoService()
        .then(res => {
          this.userInfo = res.data;
        })
        .catch(err => {
          console.error(err);
        });
    }
  }
};
</script>
<style scoped>
.container {
  min-height: 100vh;
  background: #f8f8f8;
}

.profile-header {
  background-size: cover;
  height: 20vh;
  display: flex;
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
.douyinnum{
  margin-top: 10vw;
  margin-left: 3vw;
}

.avatar {
  margin-top: 5vw;
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
.bio > div{
  margin: 2px;
  background-color: rgba(0,0,0,0.1);
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
