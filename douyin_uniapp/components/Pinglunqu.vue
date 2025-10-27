<template>
  <div class="pinglunqu">
    <!-- 关闭按钮 -->
    <button @click="sendMessage" class="close-button">×</button>
    <!-- 评论列表 -->
    <div class="comments-list">
      <!-- 空状态提示 -->
      <div v-if="!commentsData || commentsData.length === 0" class="empty-state">
        <div class="empty-icon">💬</div>
        <div class="empty-text">暂无评论，快来抢沙发吧！</div>
      </div>

      <!-- 评论列表 -->
      <div v-else class="comment-item" v-for="(ping, index) in commentsData"
        :key="ping.commentId || ping.userId || index">
        <div class="avatar" @click="goToUserProfile(ping.userId)">
          <img :src="ping.avatar || 'https://via.placeholder.com/44x44/ccc/fff?text=?'" alt="头像" />
        </div>
        <div class="comment-content">
          <div class="user-name">{{ ping.username || '匿名用户' }}</div>
          <div class="comment-text">{{ ping.comment || '评论内容加载中...' }}</div>
          <div class="actions">
            <span class="like-btn" @click="likeComment(index)">
              <svg viewBox="0 0 24 24" class="like-icon" fill="currentColor">
                <path
                  d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 6 3.99 4 6.5 4c1.54 0 3.04 1 3.57 2.36h1.87C14.46 5 15.96 4 17.5 4 20.01 4 22 6 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
              </svg>
              {{ ping.likes || 0 }}
            </span>
            <span class="reply-btn" @click="replyComment(index)">回复</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 评论输入框 -->
    <div class="comment-input-container">
      <textarea v-model="newComment" placeholder="说点什么..." class="comment-input" rows="1"
        @keyup.enter="submitComment"></textarea>
      <button class="submit-btn" :disabled="!newComment.trim()" @click="submitComment">
        发送
      </button>
    </div>
  </div>
</template>
<script>
import { getComment, addcomment } from '@/api/video';
export default {
  name: 'Pinglunqu',
  data() {
    return {
      commentsData: [
        {
          commentId: 1,
          userId: 'user001',
          avatar: 'https://via.placeholder.com/44x44/ff6b6b/ffffff?text=U1',
          username: '用户001',
          comment: '这个视频太棒了！学到了很多新知识。',
          likes: 15
        },
        {
          commentId: 2,
          userId: 'user002',
          avatar: 'https://via.placeholder.com/44x44/4ecdc4/ffffff?text=U2',
          username: '用户002',
          comment: '感谢分享，很有用的内容！',
          likes: 8
        },
        {
          commentId: 3,
          userId: 'user003',
          avatar: 'https://via.placeholder.com/44x44/45b7d1/ffffff?text=U3',
          username: '用户003',
          comment: '期待更多这样的视频，加油！',
          likes: 12
        },
        {
          commentId: 4,
          userId: 'user004',
          avatar: 'https://via.placeholder.com/44x44/96ceb4/ffffff?text=U4',
          username: '用户004',
          comment: '讲得很清楚，很容易理解。',
          likes: 6
        },
        {
          commentId: 5,
          userId: 'user005',
          avatar: 'https://via.placeholder.com/44x44/feca57/ffffff?text=U5',
          username: '用户005',
          comment: '收藏了，以后慢慢看。',
          likes: 3
        }
      ],
      fatherId: '',
      newComment: '',
      videoData: {
        videoId: 'demo_video_001'
      },
      pageNum: 1,
      pageSize: 10,
      finished: false, // 用于表示是否加载完了全部数据
      loading: false // 防止重复加载
    };
  },
  methods: {
    sendMessage() {
      this.$emit('send-message', false);
    },
    likeComment(index) {
      this.commentsData[index].likes++;
    },
    replyComment(index) {
      console.log(`回复评论 ${index}`);
      // 回复逻辑你自己扩展
    },
    goToUserProfile(userId) {
      console.log(`跳转到用户 ${userId} 的主页`);
      // this.$router.push({ name: 'userProfile', params: { userId } });
    },
    onScroll(e) {
      const el = e.target;
      if (el.scrollTop + el.clientHeight >= el.scrollHeight - 100) {
        this.getCommentInit();
      }
    },
    getCommentInit() {
      if (this.finished || this.loading) return;

      this.loading = true;
      const params = {
        videoId: this.videoData.videoId,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };

      getComment(params)
        .then((res) => {
          const list = res.data?.data || [];
          if (list.length < this.pageSize) {
            this.finished = true;
          }
          // 确保 commentsData 是数组
          if (!Array.isArray(this.commentsData)) {
            this.commentsData = [];
          }
          this.commentsData = [...this.commentsData, ...list];
          this.pageNum++;
        })
        .catch((err) => {
          console.error('获取评论失败', err);
          // 如果获取失败，保持现有数据不变，避免清空
          console.log('使用默认评论数据');
        })
        .finally(() => {
          this.loading = false;
        });
    },
    submitComment() {
      const userInfoMap = this.$store.state.userInfo.userInfo; // 从 Vuex 获取用户信息

      if (this.newComment.trim() === '') return;

      const newCommentObj = {
        commentId: Date.now(),
        userId: userInfoMap.id,
        avatar: userInfoMap.avatar,
        username: userInfoMap.nickname,
        comment: this.newComment,
        likes: 0
      };

      if (!Array.isArray(this.commentsData)) {
        this.commentsData = [];
      }

      this.commentsData.unshift(newCommentObj);
      this.newComment = '';

      // 构造评论参数（你也可以不使用 this.Constant，直接写 key）
      const params = {
        videoId: parseInt(this.videoData.videoId, 10),
        userId: newCommentObj.userId,
        fatherId: this.fatherId
      };

      addcomment(params)
        .then((res) => {
          console.log('评论发布成功:', res);
        })
        .catch((err) => {
          console.error('评论发布失败:', err);
        });
    }
  },
  mounted() {
    // 安全获取视频数据，如果获取失败则使用默认值
    try {
      const videoInfo = this.$store.getters['videoInfo/getVideoInfo'];
      this.videoData = videoInfo || { videoId: 'demo_video_001' };
    } catch (error) {
      console.warn('获取视频信息失败，使用默认值', error);
      this.videoData = { videoId: 'demo_video_001' };
    }

    this.getCommentInit();

    // 安全添加滚动事件监听
    this.$nextTick(() => {
      const container = this.$el?.querySelector('.pinglunqu');
      if (container) {
        container.addEventListener('scroll', this.onScroll);
      }
    });
  }
};
</script>

<style scoped>
.pinglunqu {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100vw;
  height: 60vh;
  background: #fff;
  border-radius: 20px 20px 0 0;
  padding: 15px 15px 70px;
  box-sizing: border-box;
  overflow-y: auto;
  color: #121212;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue',
    Arial, sans-serif;
  user-select: none;
  z-index: 10000;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
}

.close-button {
  position: absolute;
  top: 10px;
  right: 15px;
  background: transparent;
  border: none;
  font-size: 28px;
  color: #666;
  cursor: pointer;
  line-height: 1;
  user-select: none;
  transition: color 0.3s ease;
}

.close-button:hover {
  color: #000;
}

.comments-list {
  margin-top: 40px;
  padding-bottom: 10px;
}

.comment-item {
  display: flex;
  margin-bottom: 16px;
  align-items: flex-start;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  overflow: hidden;
  border: 1.5px solid #ddd;
  cursor: pointer;
  flex-shrink: 0;
  margin-right: 12px;
  background: #f0f0f0;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.comment-content {
  flex: 1;
  word-break: break-word;
}

.user-name {
  font-weight: 600;
  font-size: 14px;
  color: #121212;
  margin-bottom: 3px;
}

.comment-text {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
  margin-bottom: 6px;
}

.actions {
  display: flex;
  gap: 15px;
  font-size: 13px;
  color: #888;
}

.like-btn,
.reply-btn {
  display: flex;
  align-items: center;
  cursor: pointer;
  user-select: none;
  transition: color 0.3s ease;
}

.like-btn:hover,
.reply-btn:hover {
  color: #ff2e54;
}

.like-icon {
  width: 16px;
  height: 16px;
  margin-right: 4px;
  fill: currentColor;
}

/* 底部固定评论输入框 */
.comment-input-container {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100vw;
  background: #fafafa;
  border-top: 1px solid #e1e1e1;
  padding: 10px 15px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  z-index: 10001;
}

.comment-input {
  flex: 1;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 20px;
  color: #121212;
  padding: 10px 15px;
  font-size: 14px;
  resize: none;
  outline: none;
  max-height: 80px;
  line-height: 1.4;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

.comment-input::placeholder {
  color: #bbb;
}

.submit-btn {
  background: #ff2e54;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 18px;
  margin-left: 10px;
  font-size: 14px;
  cursor: pointer;
  user-select: none;
  transition: background-color 0.3s ease;
  flex-shrink: 0;
}

.submit-btn:disabled {
  background: #d48c9b;
  cursor: not-allowed;
}

.submit-btn:not(:disabled):hover {
  background: #ff527b;
}

/* 空状态样式 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.6;
}

.empty-text {
  font-size: 14px;
  color: #bbb;
}
</style>
