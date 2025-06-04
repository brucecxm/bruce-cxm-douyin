<template>
  <div class="videoarticle" @click="testone">
    <div class="username" @click="goauth(videoData.userid)">
      {{ videoData.username }}
    </div>
    <div div class="article">{{ videoData.videoContent }}</div>
  </div>
</template>

<script>
export default {
  props: {
    videoData: {
      type: Object,

      default() {
        return {
          collectNum: '', // 收藏数量
          commentNum: '', // 评论数量
          likeNum: '', // 点赞数量
          shareNum: '', // 分享数量
          userAvatar: '',
          userid: '', // 用户 ID
          username: '', // 用户名
          videoUrl: '', // 视频 URL
          video_comment: '', // 视频评论
          videoid: '' // 视频 ID
        };
      }
    }
  },
  data() {
    return {};
  },
  methods: {
    testone() {
      console.log('sssssss');
    },
    goauth(userId) {
      this.$router.push({ path: `/auth/${userId}` });
    }
  }
};
</script>

<style scoped>
.videoarticle {
  width: 75vw;
  max-height: 20vh;
  color: white;
}

.article {
  font-size: 0.5rem;
  margin-left: 0.5rem;
}

.username {
  font-size: 0.6rem;
  margin-bottom: 0.3rem;
  margin-left: 0.5rem;
}
</style>
