<template>
  <div class="videoaside">
    <!-- 用户头像 -->
    <div class="touxiang avatar" @click="goauth(videoData.userId)">
      <img
        :src="videoData.userAvatar"
        alt="avatar"
        style="width: 1.4rem; height: 1.4rem"
      />
    </div>

    <!-- 点赞 -->
    <div
      class="aside like"
      @click="toggleLike"
      :class="{ liked: isLiked }"
    ></div>
    <div class="num">{{ likeNum }}</div>
    <!-- 显示点赞数量 -->

    <!-- 评论 -->
    <div
      class="aside comment"
      @click="toggleComment"
      :class="{ clicked: isCommentClicked }"
    ></div>
    <div class="num">{{ videoData.commentNum }}</div>

    <!-- 收藏 -->
    <div
      class="aside collect"
      @click="toggleCollect"
      :class="{ collected: isCollected }"
    ></div>
    <div class="num">{{ collectNum }}</div>

    <!-- 分享 -->
    <div
      class="aside share"
      @click="toggleShare"
      :class="{ clicked: isShareClicked }"
    ></div>
    <div class="num">{{ videoData.shareNum }}</div>

    <!-- 音乐 -->
    <div class="touxiang music" @click="gomusic(videoData.musicId)">
      <img
        :src="
          videoData.musicAvatar
            ? videoData.musicAvatar
            : 'https://copyright.bdstatic.com/vcg/creative/96a0c34e29b4a7ba461a745116c8766a.eps'
        "
        alt="music"
        style="width: 1.4rem; height: 1.4rem"
      />
    </div>
  </div>
</template>

<script>
import { like, collect } from '@/api/video';

export default {
  props: {
    videoData: Object
  },
  data() {
    return {
      userId: '1',
      isLiked: this.videoData.isLiked || false, // 从videoData初始化点赞状态
      likeNum: this.videoData.likeNum || 0, // 初始点赞数量
      isCollected: this.videoData.isCollected || false, // 从videoData初始化收藏状态
      collectNum: this.videoData.collectNum || 0, // 初始收藏数量
      isCommentClicked: false, // 控制评论图标点击状态
      isShareClicked: false, // 控制分享图标点击状态
      videoDateOne: {}
    };
  },
  // inject: ['fromAside', 'sharedState'],
  methods: {
    send() {
      // 直接调用祖先注入的函数进行传值
    },

    showcomment() {
      // this.fromAside(true);把这换成用vueX
      // this.fromAside(true);把这换成用vueX
      // this.fromAside(true);把这换成用vueX
      this.$store.dispatch('videoInfo/setVideoInfo', this.videoDateOne);
    },
    toggleComment() {
      this.isCommentClicked = true; // 设置评论图标点击状态
      setTimeout(() => {
        this.isCommentClicked = false; // 0.5秒后恢复状态
      }, 500);
      this.showcomment(); // 调用评论方法
      this.$emit('show-comment', true); // 触发事件通知父组件显示评论区
    },
    test() {
      console.log('test');
    },
    toggleLike() {
      this.isLiked = !this.isLiked; // 切换点赞状态
      if (this.isLiked) {
        this.likeNum += 1; // 点赞时，点赞数量增加1
      } else {
        this.likeNum -= 1; // 取消点赞时，点赞数量减少1
      }
      this.behaviour('like');

      // 调用 API 或其他逻辑处理
      console.log('videoDateOne:', this.videoDateOne);
      console.log('videoId:', this.videoDateOne.videoId);
      console.log('videoId type:', typeof this.videoDateOne.videoId);

      // 确保contentId是一个数字，而不是对象
      const contentId =
        typeof this.videoDateOne.videoId === 'object'
          ? Number(
              this.videoDateOne.videoId.id ||
                this.videoDateOne.videoId.videoId ||
                0
            )
          : Number(this.videoDateOne.videoId);

      var params = {
        type: this.isLiked ? 'red' : 'nored',
        contentType: 'video',
        contentId: contentId
      };

      console.log('发送的点赞参数:', params);
      console.log('contentId type:', typeof params.contentId);

      like(params)
        .then((result) => {
          console.log('点赞成功:', result);
        })
        .catch((error) => {
          console.error('点赞出错:', error);
        });
    },

    toggleCollect() {
      this.isCollected = !this.isCollected; // 切换收藏状态
      if (this.isCollected) {
        this.collectNum += 1; // 收藏时，收藏数量增加1
      } else {
        this.collectNum -= 1; // 取消收藏时，收藏数量减少1
      }
      this.behaviour('collect');

      // 调用 API 或其他逻辑处理
      console.log('videoDateOne:', this.videoDateOne);
      console.log('videoId:', this.videoDateOne.videoId);
      console.log('videoId type:', typeof this.videoDateOne.videoId);

      // 确保contentId是一个数字，而不是对象
      const contentId =
        typeof this.videoDateOne.videoId === 'object'
          ? Number(
              this.videoDateOne.videoId.id ||
                this.videoDateOne.videoId.videoId ||
                0
            )
          : Number(this.videoDateOne.videoId);

      var params = {
        type: this.isCollected ? 'collect' : 'uncollect',
        contentType: 'video',
        contentId: contentId
      };

      console.log('发送的收藏参数:', params);
      console.log('contentId type:', typeof params.contentId);

      collect(params)
        .then((result) => {
          console.log('收藏成功:', result);
        })
        .catch((error) => {
          console.error('收藏出错:', error);
        });
    },
    toggleShare() {
      this.isShareClicked = true; // 设置分享图标点击状态
      setTimeout(() => {
        this.isShareClicked = false; // 0.5秒后恢复状态
      }, 500);
      this.$store.commit('videoInfo/OPEN_SHARE_POPUP'); // 调用 Vuex 打开分享弹窗
      this.behaviour('share'); // 调用分享方法
    },
    behaviour(name) {
      console.log(name);
      console.log('behaviour called');
    },
    goauth(userId) {
      this.$router.push({ path: `/auth/${userId}` });
    },
    gomusic(musicid) {
      this.$router.push({ path: `/music/${musicid}` });
    }
  },
  mounted() {
    this.videoDateOne = this.videoData;
  }
};
</script>

<style scoped>
.touxiang {
  width: 1.4rem;
  height: 1.4rem;
}

.num {
  color: white;
  font-size: 0.4rem;
  margin-top: -0.3rem;
}

.aside {
  width: 1rem;
  height: 1rem;
  position: relative;
  /* 为了定位心形图标 */
}

.videoaside {
  display: flex;
  flex-direction: column;
  width: 2rem;
  color: rgb(81, 67, 67);
  height: 12rem;
  justify-content: space-between;
  align-items: center;
}

.avatar {
  border-radius: 0.7rem;
  overflow: hidden;
}

.music {
  border-radius: 0.7rem;
  overflow: hidden;
}

/* 点赞图标 */
.like {
  background-image: url(../assets/home/点赞.png);
  background-size: 1rem 1rem;
  transition: transform 0.3s;
  /* 添加过渡效果 */
}

.like.liked {
  background-image: url(../assets/home/红心.png);
  /* 替换为红心图标 */
  transform: scale(1.2);
  /* 放大效果 */
}

/* 收藏图标 */
.collect {
  background-image: url(../assets/home/收藏.png);
  background-size: 1rem 1rem;
  transition:
    transform 0.3s,
    background-color 0.3s;
  /* 添加过渡效果 */
}

.collect.collected {
  background-image: url(../assets/home/黄星.png);
  /* 替换为黄星图标 */
  transform: scale(1.2);
  /* 放大效果 */
}

/* 评论图标 */
.comment {
  background-image: url(../assets/home/评论.png);
  background-size: 1rem 1rem;
  transition: transform 0.3s;
  /* 添加过渡效果 */
}

.comment.clicked {
  transform: scale(1.2);
  /* 放大效果 */
}

/* 分享图标 */
.share {
  background-image: url(../assets/home/转发.png);
  background-size: 1rem 1rem;
  transition: transform 0.3s;
  /* 添加过渡效果 */
}

.share.clicked {
  transform: scale(1.2);
  /* 放大效果 */
}
</style>
