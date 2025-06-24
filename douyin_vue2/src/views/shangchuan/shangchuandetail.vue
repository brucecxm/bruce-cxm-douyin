<template>
  <div class="publish-page">
    <!-- 视频预览 -->
    <div class="video-preview">
      <img :src="videoThumbnail" alt="video" v-if="videoThumbnail" />
      <video :src="videoURL" style="width: 100%; height: 100%"></video>
    </div>

    <!-- 标题输入框 -->
    <div class="input-area">
      <textarea
        v-model="title"
        placeholder="这一刻的想法..."
        maxlength="100"
      ></textarea>
    </div>

    <!-- 话题选择 -->
    <div class="section-title">添加话题</div>
    <FlexibleButtonPanel
      :items="topicItems"
      :cols="3"
      :gap="8"
      @btnClick="onTopicClick"
    />

    <!-- 添加位置 -->
    <div class="section-title">添加位置</div>
    <FlexibleButtonPanel
      :items="locationItems"
      :cols="2"
      :gap="8"
      @btnClick="onLocationClick"
    />

    <!-- 权限设置 -->
    <div class="section-title">谁可以看</div>
    <FlexibleButtonPanel
      :items="privacyItems"
      :cols="2"
      :gap="12"
      @btnClick="onPrivacyClick"
    />

    <!-- 高级设置 -->
    <div class="advance-settings">
      <button @click="openSettings">高级设置</button>
    </div>

    <!-- 底部发布栏 -->
    <div class="bottom-bar">
      <FlexibleButtonPanel
        :items="buttonItems"
        :cols="3"
        :gap="10"
        :fontSize="15"
        :iconSize="24"
        @btnClick="onBtnClick"
        @btnLongPress="onBtnLongPress"
      >
        <template #btn-0="{ item }">
          <div style="display: flex; align-items: center; font-size: 0.5rem">
            <img
              :src="item.icon"
              alt=""
              style="width: 24px; height: 24px; border-radius: 12px"
            />
            <span style="margin-left: 4px">{{ item.text }}</span>
          </div>
        </template>
        <template #btn-1="{ item }">
          <div style="display: flex; align-items: center; font-size: 0.5rem">
            <img
              :src="item.icon"
              alt=""
              style="width: 300px; height: 24px; border-radius: 12px"
            />
            <span style="margin-left: 4px">{{ item.text }}</span>
          </div>
        </template>
      </FlexibleButtonPanel>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import FlexibleButtonPanel from '@/components/FlexibleButtonPanel.vue';
export default {
  components: { FlexibleButtonPanel },
  data() {
    return {
      videoId: '',
      videoURL: '',
      userId: '123456', // 模拟用户 ID，真实情况从登录态获取
      videoThumbnail: '',
      title: '',
      selectedTopic: null,
      selectedLocation: null,
      selectedPrivacy: '公开',
      videoData: '',
      loading: false,

      topicItems: [
        { text: '#热门抖音助手', bgColor: '#f5f5f5', textColor: '#333' },
        { text: '#即拍即发', bgColor: '#f5f5f5', textColor: '#333' },
        { text: '#新偏橙视生活', bgColor: '#f5f5f5', textColor: '#333' }
      ],
      locationItems: [
        { text: '神州数码西安科技园', bgColor: '#eee', textColor: '#111' },
        { text: '陕西大会堂', bgColor: '#eee', textColor: '#111' }
      ],
      privacyItems: [
        { text: '公开', bgColor: '#ffeded', textColor: '#f00', bold: true },
        { text: '仅好友可见', bgColor: '#eee', textColor: '#111' }
      ],
      buttonItems: [
        {
          height: '10px',
          width: '50px',
          text: '存草稿',
          icon: 'http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960'
        },
        {
          height: '10px',
          text: '朋友日常',
          bgColor: '#464646',
          icon: 'http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960'
        },
        {
          height: '10px',
          text: '发作品',
          bgColor: '#ff2c55',
          borderRadius: 20
        }
      ]
    };
  },
  mounted() {
    // 获取路由参数中的 videoId 和 videoThumbnail
    this.videoId = this.$route.query.video || '';
    this.videoThumbnail = this.$route.query.photo || '';

    if (this.videoThumbnail) {
      console.log('视频缩略图 URL:', this.videoThumbnail);
    } else {
      if (this.videoId) {
        // 通过 Vuex getter 获取视频数据
        const videoData = this.$store.getters['video/getVideo'](this.videoId);
        if (videoData) {
          this.videoData = videoData;
          this.videoURL = videoData.url; // 直接使用 URL，无需重新创建
          console.log('视频预览 URL:', this.videoURL);

          // 可选：设置视频缩略图（如果路由里有 photo 参数）
          if (this.$route.query.photo) {
            this.videoThumbnail = this.$route.query.photo;
          }
        } else {
          console.error('找不到视频数据，ID:', this.videoId);
          // 这里可以添加错误处理逻辑，例如显示提示或跳转
        }
      } else {
        console.error('缺少视频ID参数');
        // 这里也可以添加错误处理逻辑
      }
    }
  },
  methods: {
    onBtnClick({ index, item }) {
      if (item.text === '发作品') {
        this.publishPost();
      } else {
        console.log(`点击按钮 ${index}: ${item.text}`);
      }
    },
    onBtnLongPress({ index, item }) {
      console.log(`长按按钮 ${index}: ${item.text}`);
    },
    onTopicClick({ item }) {
      this.selectedTopic = item.text;
      console.log('点击话题：', item.text);
    },
    onLocationClick({ item }) {
      this.selectedLocation = item.text;
      console.log('点击位置：', item.text);
    },
    onPrivacyClick({ item }) {
      this.selectedPrivacy = item.text;
      console.log('选择权限：', item.text);
    },
    openSettings() {
      console.log('进入高级设置');
    },
    async publishPost() {
      if (this.loading) return;
      if (!this.title) return console.log('请输入想法内容');
      if (!this.videoThumbnail && !this.videoData)
        return console.log('请上传视频');

      this.loading = true;

      try {
        // 创建FormData对象
        const formData = new FormData();

        // 添加视频文件
        if (this.videoData && this.videoData.blob) {
          // 使用原始文件名或生成一个唯一文件名
          const fileName = `video_${Date.now()}.mp4`;
          formData.append('videoFile', this.videoData.blob, fileName);
        }

        // 添加视频缩略图
        if (this.videoThumbnail) {
          // 假设this.videoThumbnail也是一个Blob或File对象
          formData.append('thumbnail', this.videoThumbnail);
        }

        // 添加其他表单字段
        formData.append('userId', this.userId);
        formData.append('title', this.title);
        formData.append('topic', this.selectedTopic || '');
        formData.append('location', this.selectedLocation || '');
        formData.append('privacy', this.selectedPrivacy || '公开');
        formData.append('createdAt', new Date().toISOString());

        // 添加视频元数据
        if (this.videoData && this.videoData.metadata) {
          formData.append('metadata', JSON.stringify(this.videoData.metadata));
        }

        // 设置请求头，注意不要手动设置Content-Type，让浏览器自动设置
        const config = {
          headers: {
            'Content-Type': 'multipart/form-data' // 实际上这里应该由浏览器自动设置
          }
        };

        // 发送请求
        const res = await axios.post('/api/video/publish', formData, config);
        console.log('发布成功！');

        // 重置表单或执行其他操作
        this.resetForm();
      } catch (err) {
        console.error('发布失败', err);
        console.log('发布失败，请稍后重试');
      } finally {
        this.loading = false;
        this.$router.push('/'); // 发布成功后跳转到首页
      }
    }
  }
};
</script>

<style scoped>
.publish-page {
  padding: 16px;
  padding-bottom: 80px; /* 留出底部按钮空间 */
  font-family: Arial, sans-serif;
}

.video-preview {
  width: 60%;
  height: 200px;
  background: #eee;
  border-radius: 8px;
  margin: 0 auto 12px; /* 上下边距 0 和 12px，左右自动居中 */
  position: relative;
  overflow: hidden;
}

.video-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-btn {
  width: 100%;
  height: 100%;
  font-size: 48px;
  color: #999;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.input-area textarea {
  width: 100%;
  min-height: 80px;
  border: none;
  resize: none;
  padding: 8px;
  font-size: 16px;
  background: #fafafa;
  border-radius: 6px;
  margin-bottom: 12px;
}

.section-title {
  font-weight: bold;
  margin: 12px 0 6px;
  font-size: 15px;
}

.advance-settings {
  text-align: center;
  margin: 20px 0;
}

.advance-settings button {
  padding: 6px 16px;
  border: none;
  background-color: #eee;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 95%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  padding: 12px 16px;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.05);
  background-color: #fff;
  z-index: 100;
}

.ghost-btn {
  background-color: #f5f5f5;
  border: none;
  padding: 10px 16px;
  border-radius: 24px;
  font-size: 16px;
}

.primary-btn {
  background-color: #ff2c55;
  color: #fff;
  border: none;
  padding: 10px 24px;
  border-radius: 24px;
  font-size: 16px;
  font-weight: bold;
}
</style>
