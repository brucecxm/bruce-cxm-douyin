<template>
  <div class="pinglunqu">
    <button @click="sendMessage" class="close-button">×</button>

    <div class="comments-list">
      <!-- 父评论 -->
      <div
        class="comment-item"
        v-for="(ping, index) in commentsData"
        :key="ping.commentId || ping.userId"
      >
        <div class="avatar" @click="goToUserProfile(ping.userId)">
          <img :src="ping.avatar" alt="头像" />
        </div>
        <div class="comment-content">
          <div class="user-name">{{ ping.username }}</div>
          <div class="comment-text">{{ ping.comment }}</div>

          <div v-if="replyTargetIndex === index" class="replying-info">
            回复 @{{ ping.username }}
          </div>

          <div class="actions">
            <span class="like-btn" @click="likeComment(index)">
              <svg viewBox="0 0 24 24" class="like-icon" fill="currentColor">
                <path
                  d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 6 3.99 4 6.5 4c1.54 0 3.04 1 3.57 2.36h1.87C14.46 5 15.96 4 17.5 4 20.01 4 22 6 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
                />
              </svg>
              {{ ping.likes }}
            </span>
            <span class="reply-btn" @click="replyComment(index)">回复</span>

            <!-- 👇 查看子评论按钮（箭头） -->
            <span
              v-if="ping.hasChildren === undefined || ping.hasChildren === true"
              class="children-btn"
              @click="toggleChildren(ping, index)"
            >
              {{ ping.showChildren ? '↑ 收起' : '↓ 查看回复' }}
            </span>
          </div>

          <!-- 👇 子评论列表（展开才显示） -->
          <div
            v-if="
              ping.showChildren &&
                ping.childrenList &&
                ping.childrenList.length > 0
            "
            class="children-list"
          >
            <div
              class="child-item"
              v-for="child in ping.childrenList"
              :key="child.commentId"
            >
              <div class="child-avatar">
                <img :src="child.avatar" alt="头像" />
              </div>
              <div class="child-content">
                <div class="child-name">{{ child.username }}</div>
                <!-- 显示 回复 @父评论作者 -->
                <div class="child-reply">回复 @{{ ping.username }}</div>
                <div class="child-text">{{ child.comment }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="comment-input-container">
      <textarea
        v-model="newComment"
        :placeholder="replyPlaceholder"
        class="comment-input"
        rows="1"
        @keyup.enter="submitComment"
      ></textarea>
      <button
        class="submit-btn"
        :disabled="!newComment.trim()"
        @click="submitComment"
      >
        发送
      </button>
      <button v-if="fatherId" class="cancel-reply-btn" @click="cancelReply">
        取消
      </button>
    </div>
  </div>
</template>

<script>
import { getComment, addcomment, getChildrenComment } from '@/api/video';
export default {
  name: 'Pinglunqu',
  data() {
    return {
      commentsData: [],
      fatherId: '',
      replyTargetIndex: -1,
      newComment: '',
      videoData: {},
      pageNum: 1,
      pageSize: 10,
      finished: false,
      loading: false
    };
  },
  computed: {
    replyPlaceholder() {
      return this.fatherId ? '回复中...' : '说点什么...';
    }
  },
  methods: {
    sendMessage() {
      this.$emit('send-message', false);
      this.cancelReply();
    },
    likeComment(index) {
      this.commentsData[index].likes++;
    },
    replyComment(index) {
      const item = this.commentsData[index];
      this.fatherId = item.commentId;
      this.replyTargetIndex = index;
      this.$nextTick(() => {
        const input = document.querySelector('.comment-input');
        input?.focus();
      });
    },
    cancelReply() {
      this.fatherId = '';
      this.replyTargetIndex = -1;
    },
    goToUserProfile(userId) {
      console.log(`跳转到用户 ${userId} 的主页`);
    },
    onScroll(e) {
      const el = e.target;
      if (el.scrollTop + el.clientHeight >= el.scrollHeight - 100) {
        this.getCommentInit();
      }
    },

    // ======================
    // 加载父评论
    // ======================
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
          const list = res.data.records || [];
          if (list.length < this.pageSize) this.finished = true;

          // 给每条父评论加默认展开状态 + 子评论数组
          const newList = list.map((item) => ({
            ...item,
            showChildren: false,
            childrenList: []
          }));

          this.commentsData = [...this.commentsData, ...newList];
          this.pageNum++;
        })
        .catch((err) => console.error('获取评论失败', err))
        .finally(() => (this.loading = false));
    },

    // ======================
    // 👇 关键：展开/收起子评论
    // ======================
    async toggleChildren(ping, index) {
      // 关闭
      if (ping.showChildren) {
        ping.showChildren = false;
        this.$set(this.commentsData, index, ping);
        return;
      }

      // 已经加载过，直接打开
      if (ping.childrenList.length > 0) {
        ping.showChildren = true;
        this.$set(this.commentsData, index, ping);
        return;
      }

      // 请求子评论
      const res = await getChildrenComment({
        fatherId: ping.commentId,
        pageNum: 1,
        pageSize: 20
      });

      ping.childrenList = res.data.records || [];
      ping.showChildren = true;
      this.$set(this.commentsData, index, ping);
    },

    // ======================
    // 发布评论
    // ======================
    submitComment() {
      const userInfoMap = this.$store.state.userInfo.userInfo;
      if (!this.newComment.trim()) return;

      const newCommentObj = {
        commentId: Date.now(),
        userId: userInfoMap.id,
        avatar: userInfoMap.avatar,
        username: userInfoMap.nickname,
        comment: this.newComment,
        likes: 0,
        fatherId: this.fatherId
      };

      // 如果是回复父评论，插入到对应子评论列表
      if (this.fatherId) {
        const target = this.commentsData.find(
          (item) => item.commentId === this.fatherId
        );
        if (target) {
          target.childrenList.unshift(newCommentObj);
        }
      } else {
        this.commentsData.unshift({
          ...newCommentObj,
          showChildren: false,
          childrenList: []
        });
      }

      const params = {
        videoId: parseInt(this.videoData.videoId, 10),
        userId: newCommentObj.userId,
        fatherId: this.fatherId || null,
        comment: this.newComment.trim()
      };

      addcomment(params)
        .then((res) => {
          console.log('发布成功', res);
          // 使用后端返回的commentId替换前端临时生成的ID
          const backendCommentId = res.data.commentId;
          if (this.fatherId) {
            const target = this.commentsData.find(
              (item) => item.commentId === this.fatherId
            );
            if (target) {
              const newCommentItem = target.childrenList[0];
              newCommentItem.commentId = backendCommentId;
            }
          } else {
            const newCommentItem = this.commentsData[0];
            newCommentItem.commentId = backendCommentId;
          }
          this.newComment = '';
          this.cancelReply();
        })
        .catch((err) => {
          console.error('发布失败', err);
          if (this.fatherId) {
            const target = this.commentsData.find(
              (item) => item.commentId === this.fatherId
            );
            target?.childrenList.shift();
          } else {
            this.commentsData.shift();
          }
        });
    }
  },
  mounted() {
    this.videoData = this.$store.getters['videoInfo/getVideoInfo'];
    this.getCommentInit();
    const container = this.$el;
    container.addEventListener('scroll', this.onScroll);
  },
  beforeDestroy() {
    const container = this.$el;
    container.removeEventListener('scroll', this.onScroll);
  }
};
</script>

<style scoped>
.pinglunqu {
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
  position: relative;
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

.replying-info {
  font-size: 12px;
  color: #ff2e54;
  margin-bottom: 6px;
}

.actions {
  display: flex;
  gap: 15px;
  font-size: 13px;
  color: #888;
}

.like-btn,
.reply-btn,
.children-btn {
  display: flex;
  align-items: center;
  cursor: pointer;
  user-select: none;
  transition: color 0.3s ease;
}

.like-btn:hover,
.reply-btn:hover,
.children-btn:hover {
  color: #ff2e54;
}

.like-icon {
  width: 16px;
  height: 16px;
  margin-right: 4px;
  fill: currentColor;
}

/* ====================== */
/* 子评论样式 */
/* ====================== */
.children-list {
  margin-top: 10px;
  padding-left: 10px;
  border-left: 2px solid #eee;
}

.child-item {
  display: flex;
  margin-bottom: 10px;
  align-items: flex-start;
}

.child-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 8px;
  flex-shrink: 0;
  background: #f0f0f0;
}

.child-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.child-content {
  flex: 1;
}

.child-name {
  font-size: 13px;
  font-weight: 600;
  color: #121212;
}

.child-reply {
  font-size: 12px;
  color: #999;
  margin: 2px 0;
}

.child-text {
  font-size: 13px;
  color: #333;
  line-height: 1.4;
}

/* ====================== */
/* 输入框 */
/* ====================== */
.comment-input-container {
  position: absolute;
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

.cancel-reply-btn {
  background: transparent;
  color: #888;
  border: none;
  padding: 8px 10px;
  margin-left: 6px;
  font-size: 13px;
  cursor: pointer;
}
</style>
