<template>
  <div class="pinglunqu">
    <button @click="sendMessage()" class="close-button">关闭评论区</button>

    <!-- 评论内容展示 -->
    <div
      class="comment"
      v-for="(ping, index) in commentsData"
      :key="ping.userId"
    >
      <div class="touxiangqu">
        <div class="touxiang" @click="goToUserProfile(ping.commentId)">
          <img :src="ping.avatar" alt="头像" />
        </div>
      </div>
      <div class="xiaoxi">
        <p class="name">{{ ping.username }}</p>
        <p class="comment-text">{{ ping.comment }}</p>
        <div class="actions">
          <span class="action" @click="likeComment(index)"
            >❤️ {{ ping.likes }}</span
          >
          <span class="action" @click="replyComment(index)">回复</span>
        </div>
      </div>
    </div>

    <!-- 发布评论的输入框 -->
    <div class="comment-input">
      <textarea
        v-model="newComment"
        placeholder="写下你的评论..."
        class="input-textarea"
      ></textarea>
      <button @click="submitComment" class="submit-button">发布</button>
    </div>
  </div>
</template>

<script>
import { getcomment, homegetVideocontent } from '@/api/video';
import { videoInfoStore } from '../stores/videoInfo';
import { addcomment } from '@/api/video.js'; // 导入 API 函数
import { useUserInfoStore } from '@/stores/userInfo';

export default {
  name: 'Pinglunqu',
  data() {
    return {
      commentsData: [
        // 示例评论数据
        // 示例评论数据
      ],
      newComment: '', // 用于绑定输入框的评论内容
      videoData: {}
    };
  },
  methods: {
    sendMessage() {
      console.log('评论区已关闭');
      this.$emit('send-message', false);
    },
    likeComment(index) {
      this.commentsData[index].likes++; // 增加点赞量
      console.log(`点赞评论 ${index}`);
    },
    replyComment(index) {
      console.log(`回复评论 ${index}`);
      // 在此处处理回复逻辑
    },
    goToUserProfile(userId) {
      console.log(`跳转到用户 ${userId} 的主页`);
      // 跳转到用户主页的逻辑可以是：
      // this.$router.push({ name: 'userProfile', params: { userId } });
    },
    getcommentone() {
      const videoId = this.videoData.videoid;
      const params = { videoid: videoId }; // 获取当前视频的 videoid

      getcomment(params) // 调用 API 获取评论数据
        .then((videoArr) => {
          this.commentsData = videoArr.data.data; // 将评论数据保存到组件的 data 中
        })
        .catch((error) => {
          console.error('获取视频评论出错:', error);
        });
    },
    submitComment() {
      const userInfo = useUserInfoStore();
      var userInfoMap = {};
      userInfoMap = userInfo.userInfo;
      // const userInfo=userInfoStore();
      // const userInfoMap= userInfo.getuserInfo;
      // 确保 newComment 不为空
      if (this.newComment.trim() !== '') {
        const newCommentObj = {
          commentId: Date.now(), // 使用当前时间戳作为评论 ID
          userId: userInfoMap.id, // 使用用户 ID
          avatar: userInfoMap.avatar, // 使用用户头像
          username: userInfoMap.nickname,
          comment: this.newComment,
          likes: 0
        };

        // 确保 commentsData 是一个数组
        if (!Array.isArray(this.commentsData)) {
          console.log('commentsData 不是数组，初始化为空数组');
          this.commentsData = []; // 如果不是数组，初始化为空数组
        }

        // 将新评论添加到评论列表的顶部
        this.commentsData.unshift(newCommentObj);

        // 清空输入框
        this.newComment = '';

        const params = {
          videoid: parseInt(this.videoData.videoid, 10),
          userId: newCommentObj.userId,
          comment: newCommentObj.comment,
          lastId: '-1'
        };

        // const params=newCommentObj
        addcomment(params)
          .then((res) => {
            console.log('评论发布成功:', res);
          })
          .catch((error) => {
            console.error('评论发布失败:', error);
          });

        console.log('发布评论:', newCommentObj);
        console.log('更新后的评论数据:', this.commentsData);
      } else {
        console.log('评论内容不能为空');
      }
    }
  },

  mounted() {
    const videoInfo = videoInfoStore();
    this.videoData = videoInfo.getvideoInfo;
    this.getcommentone();
  },
  beforeDestroy() {}
};
</script>
<style scoped>
.pinglunqu {
  width: 100vw;
  height: 60vh;
  background-color: #f9f9f9;
  border-radius: 10px 10px 0 0;
  padding: 20px;
  box-sizing: border-box;
  overflow-y: auto;
}

/* 评论区关闭按钮 */
.close-button {
  background-color: #f04e23;
  color: white;
  padding: 10px 20px;
  border-radius: 30px;
  font-size: 14px;
  border: none;
  cursor: pointer;
  margin-bottom: 20px;
}

/* 评论内容 */
.comment {
  display: flex;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.touxiangqu {
  width: 50px;
  height: 50px;
  margin-right: 15px;
}

.touxiang {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
}

.touxiang img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.xiaoxi {
  flex: 1;
}

.name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.comment-text {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  margin-bottom: 10px;
}

.actions {
  display: flex;
  align-items: center;
}

.action {
  font-size: 14px;
  margin-right: 15px;
  color: #ff7a59;
  cursor: pointer;
}

.action:hover {
  text-decoration: underline;
}

/* 发布评论输入框样式 */
.comment-input {
  position: fixed;
  bottom: 0;
  width: 100%;
  background-color: white;
  padding: 15px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  border-top: 1px solid #ddd;
}

.input-textarea {
  width: 80%;
  height: 40px;
  padding: 10px;
  font-size: 14px;
  border-radius: 5px;
  border: 1px solid #ccc;
  resize: none;
}

.submit-button {
  width: 15%;
  padding: 10px;
  margin-left: 10px;
  background-color: #f04e23;
  color: white;
  border-radius: 5px;
  font-size: 14px;
  border: none;
  cursor: pointer;
}

.submit-button:hover {
  background-color: #e03b18;
}
</style>
