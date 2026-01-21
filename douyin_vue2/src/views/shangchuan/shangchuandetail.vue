<template>
  <div class="publish-page">
    <!-- 顶部导航栏 -->
    <div class="top-nav">
      <div class="nav-left" @click="goBack">
        <img src="@/assets/IconFont/返回.png" alt="返回" class="nav-icon" />
      </div>
      <div class="nav-title">发布作品</div>
      <div class="nav-right"></div>
    </div>

    <!-- 视频预览 -->
    <div class="video-preview">
      <img
        :src="videoThumbnail"
        alt="video"
        v-if="videoThumbnail"
        class="preview-img"
      />
      <video
        :src="videoURL"
        class="preview-video"
        controls
        v-if="videoURL"
      ></video>
      <div v-if="!videoThumbnail && !videoURL" class="preview-placeholder">
        <img
          src="@/assets/home/加号黑.png"
          alt="加号"
          class="placeholder-icon"
        />
        <span class="placeholder-text">添加视频或图片</span>
      </div>
    </div>

    <!-- 标题输入框 -->
    <div class="input-area">
      <textarea
        v-model="title"
        placeholder="这一刻的想法..."
        maxlength="100"
        class="title-textarea"
        rows="3"
      ></textarea>
      <div class="char-count">{{ title.length }}/100</div>
    </div>

    <!-- 话题选择 -->
    <div class="setting-section">
      <div class="section-title">添加话题</div>
      <div class="topic-container">
        <FlexibleButtonPanel
          :items="topicItems"
          :cols="3"
          :gap="10"
          @btnClick="onTopicClick"
        />
      </div>
    </div>

    <!-- 添加位置 -->
    <div class="setting-section">
      <div class="section-title">添加位置</div>
      <div class="location-container">
        <FlexibleButtonPanel
          :items="locationItems"
          :cols="2"
          :gap="10"
          @btnClick="onLocationClick"
        />
      </div>
    </div>

    <!-- 权限设置 -->
    <div class="setting-section">
      <div class="section-title">谁可以看</div>
      <div class="privacy-container">
        <FlexibleButtonPanel
          :items="privacyItems"
          :cols="2"
          :gap="15"
          @btnClick="onPrivacyClick"
        />
      </div>
    </div>

    <!-- 高级设置 -->
    <div class="setting-section">
      <div class="advance-settings" @click="openSettings">
        <span class="settings-text">高级设置</span>
        <img
          src="@/assets/IconFont/进入.png"
          alt="进入"
          class="settings-icon"
        />
      </div>
    </div>

    <!-- 底部发布栏 -->
    <div class="bottom-bar">
      <div class="publish-btn-container">
        <button
          class="publish-btn"
          @click="publishPost"
          :disabled="loading || !title"
        >
          <span v-if="loading">发布中...</span>
          <span v-else>发布</span>
        </button>
      </div>
      <div class="bottom-actions">
        <FlexibleButtonPanel
          :items="buttonItems"
          :cols="3"
          :gap="15"
          :fontSize="14"
          :iconSize="22"
          @btnClick="onBtnClick"
          @btnLongPress="onBtnLongPress"
        >
          <template #btn-0="{ item }">
            <div class="action-item">
              <img :src="item.icon" alt="" class="action-icon" />
              <span class="action-text">{{ item.text }}</span>
            </div>
          </template>
          <template #btn-1="{ item }">
            <div class="action-item">
              <img :src="item.icon" alt="" class="action-icon" />
              <span class="action-text">{{ item.text }}</span>
            </div>
          </template>
        </FlexibleButtonPanel>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import FlexibleButtonPanel from '@/components/FlexibleButtonPanel.vue';
import { getTopics, getLocations, getPrivacies } from '@/api/video.js';
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
      dataLoading: false,

      // 从数据库获取的数据
      topicItems: [],
      locationItems: [],
      privacyItems: [],
      buttonItems: [
        {
          height: '40px',
          width: '80px',
          text: '存草稿',
          icon: 'http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 20
        },
        {
          height: '40px',
          width: '100px',
          text: '朋友日常',
          bgColor: '#464646',
          textColor: '#fff',
          icon: 'http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960',
          borderRadius: 20
        },
        {
          height: '40px',
          width: '80px',
          text: '发作品',
          bgColor: '#ff2c55',
          textColor: '#fff',
          borderRadius: 20
        }
      ]
    };
  },
  mounted() {
    // 获取路由参数中的 videoId 和 videoThumbnail
    this.videoId = this.$route.query.video || '';
    this.videoThumbnail = this.$route.query.photo || '';

    console.log('路由参数:', this.$route.query);
    console.log('获取到的 videoId:', this.videoId);
    console.log(
      '当前 Vuex videoStore 所有视频:',
      this.$store.state.videoStore.videos
    );

    if (this.videoThumbnail) {
      console.log('视频缩略图 URL:', this.videoThumbnail);
    } else {
      if (this.videoId) {
        // 通过 Vuex getter 获取视频数据，使用正确的模块名 videoStore
        const videoData = this.$store.getters['videoStore/getVideo'](
          this.videoId
        );
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
          console.error(
            '当前 Vuex 中存在的视频ID:',
            Object.keys(this.$store.state.videoStore.videos)
          );
          // 尝试从 state 中直接获取，用于调试
          const directVideo = this.$store.state.videoStore.videos[this.videoId];
          console.error('直接从 state 获取:', directVideo);
        }
      }
    }

    // 从数据库获取数据
    this.fetchData();
  },
  methods: {
    // 返回上一页
    goBack() {
      this.$router.back();
    },

    // 获取数据库数据
    async fetchData() {
      this.dataLoading = true;
      try {
        // 并行获取所有数据
        const [topicsRes, locationsRes, privaciesRes] = await Promise.all([
          getTopics(),
          getLocations(),
          getPrivacies()
        ]);

        // 处理话题数据
        if (topicsRes.data && Array.isArray(topicsRes.data)) {
          this.topicItems = topicsRes.data.map((topic) => ({
            text: topic.name,
            bgColor: topic.is_hot ? '#fff3f5' : '#f5f5f5',
            textColor: topic.is_hot ? '#ff2c55' : '#333',
            borderColor: topic.is_hot ? '#ff2c55' : '#e5e5e5',
            borderRadius: 16,
            padding: '8px 12px'
          }));
        }

        // 处理位置数据
        if (locationsRes.data && Array.isArray(locationsRes.data)) {
          this.locationItems = locationsRes.data.map((location) => ({
            text: location.name,
            bgColor: '#f5f5f5',
            textColor: '#333',
            borderRadius: 16,
            padding: '10px 16px'
          }));
        }

        // 处理权限设置数据
        if (privaciesRes.data && Array.isArray(privaciesRes.data)) {
          this.privacyItems = privaciesRes.data.map((privacy) => ({
            text: privacy.name,
            description: privacy.description,
            bgColor: '#f5f5f5',
            textColor: '#333',
            borderRadius: 20,
            padding: '10px 20px'
          }));

          // 设置默认选中项
          if (this.privacyItems.length > 0) {
            this.selectedPrivacy = this.privacyItems[0].text;
            this.privacyItems[0].bgColor = '#ffeded';
            this.privacyItems[0].textColor = '#f00';
            this.privacyItems[0].bold = true;
          }
        }
      } catch (error) {
        console.error('获取数据失败:', error);
        // 数据获取失败时使用默认数据
        this.setDefaultData();
      } finally {
        this.dataLoading = false;
      }
    },

    // 设置默认数据
    setDefaultData() {
      this.topicItems = [
        {
          text: '#热门抖音助手',
          bgColor: '#fff3f5',
          textColor: '#ff2c55',
          borderColor: '#ff2c55',
          borderRadius: 16,
          padding: '8px 12px'
        },
        {
          text: '#即拍即发',
          bgColor: '#fff3f5',
          textColor: '#ff2c55',
          borderColor: '#ff2c55',
          borderRadius: 16,
          padding: '8px 12px'
        },
        {
          text: '#新偏橙视生活',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderColor: '#e5e5e5',
          borderRadius: 16,
          padding: '8px 12px'
        }
      ];
      this.locationItems = [
        {
          text: '神州数码西安科技园',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '10px 16px'
        },
        {
          text: '陕西大会堂',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '10px 16px'
        }
      ];
      this.privacyItems = [
        {
          text: '公开',
          description: '所有人可见',
          bgColor: '#ffeded',
          textColor: '#f00',
          bold: true,
          borderRadius: 20,
          padding: '10px 20px'
        },
        {
          text: '仅好友可见',
          description: '只有你的好友可以查看',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 20,
          padding: '10px 20px'
        },
        {
          text: '私密',
          description: '仅自己可见',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 20,
          padding: '10px 20px'
        }
      ];
    },

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
    onTopicClick({ item, index }) {
      this.selectedTopic = item.text;
      console.log('点击话题：', item.text);

      // 更新选中状态
      this.topicItems = this.topicItems.map((topic, i) => ({
        ...topic,
        bgColor:
          i === index
            ? '#ff2c55'
            : topic.bgColor === '#fff3f5'
              ? '#fff3f5'
              : '#f5f5f5',
        textColor:
          i === index
            ? '#fff'
            : topic.textColor === '#ff2c55'
              ? '#ff2c55'
              : '#333',
        selected: i === index
      }));
    },
    onLocationClick({ item, index }) {
      this.selectedLocation = item.text;
      console.log('点击位置：', item.text);

      // 更新选中状态
      this.locationItems = this.locationItems.map((location, i) => ({
        ...location,
        bgColor: i === index ? '#ff2c55' : '#f5f5f5',
        textColor: i === index ? '#fff' : '#333',
        selected: i === index
      }));
    },
    onPrivacyClick({ item, index }) {
      this.selectedPrivacy = item.text;
      console.log('选择权限：', item.text);

      // 更新选中状态
      this.privacyItems = this.privacyItems.map((privacy, i) => ({
        ...privacy,
        bgColor: i === index ? '#ff2c55' : '#f5f5f5',
        textColor: i === index ? '#fff' : '#333',
        bold: i === index,
        selected: i === index
      }));
    },
    openSettings() {
      console.log('进入高级设置');
      // 这里可以添加打开高级设置的逻辑
    },
    // 将Base64字符串转换为Blob对象
    base64ToBlob(base64, mimeType) {
      const byteCharacters = atob(base64.split(',')[1]);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      return new Blob([byteArray], { type: mimeType });
    },
    async publishPost() {
      if (this.loading) return;
      if (!this.title) {
        // 显示提示信息
        alert('请输入想法内容');
        return;
      }

      // 检查是否有内容可以发布
      if (!this.videoThumbnail && !this.videoData) {
        alert('请上传图片或视频');
        return;
      }

      this.loading = true;

      try {
        // 创建FormData对象
        const formData = new FormData();

        // 添加其他表单字段
        formData.append('userId', this.userId);
        formData.append('title', this.title);
        formData.append('topic', this.selectedTopic || '');
        formData.append('location', this.selectedLocation || '');
        formData.append('privacy', this.selectedPrivacy || '公开');
        formData.append('createdAt', new Date().toISOString());

        // 检查是视频还是图片上传
        if (this.videoData && this.videoData.blob) {
          // 视频上传
          // 添加视频文件
          const fileName =
            this.videoData.metadata?.name || `video_${Date.now()}.mp4`;
          formData.append('videoFile', this.videoData.blob, fileName);

          // 添加视频缩略图
          if (this.videoThumbnail) {
            if (
              typeof this.videoThumbnail === 'string' &&
              this.videoThumbnail.startsWith('data:')
            ) {
              // 将Base64字符串转换为Blob对象
              const thumbnailBlob = this.base64ToBlob(
                this.videoThumbnail,
                'image/jpeg'
              );
              formData.append('thumbnail', thumbnailBlob, 'thumbnail.jpg');
            } else {
              // 如果已经是Blob或File对象，直接添加
              formData.append('thumbnail', this.videoThumbnail);
            }
          }

          // 添加视频元数据
          if (this.videoData.metadata) {
            formData.append(
              'metadata',
              JSON.stringify(this.videoData.metadata)
            );
          }
        } else {
          // 图片上传
          if (this.videoThumbnail) {
            let imageBlob;
            let fileName;

            if (
              typeof this.videoThumbnail === 'string' &&
              this.videoThumbnail.startsWith('data:')
            ) {
              // 将Base64字符串转换为Blob对象
              const mimeType = this.videoThumbnail.split(';')[0].split(':')[1];
              imageBlob = this.base64ToBlob(this.videoThumbnail, mimeType);
              fileName = `image_${Date.now()}.${mimeType.split('/')[1]}`;
            } else {
              // 如果已经是Blob或File对象，直接使用
              imageBlob = this.videoThumbnail;
              fileName = imageBlob.name || `image_${Date.now()}.jpg`;
            }

            // 添加图片文件
            formData.append('videoFile', imageBlob, fileName);
          }
        }

        // 发送请求，不手动设置Content-Type，让浏览器自动设置
        const res = await axios.post('/api/video/publish', formData);
        console.log('发布成功！', res.data);

        // 显示成功提示
        alert('发布成功！');

        // 重置表单
        this.resetForm();
      } catch (err) {
        console.error('发布失败', err);
        alert('发布失败，请稍后重试');
      } finally {
        this.loading = false;
        // 发布成功后跳转到首页，并清除Vuex中的视频数据
        if (this.videoId) {
          this.$store.dispatch('videoStore/removeVideo', this.videoId);
          console.log('已清除Vuex中的视频数据');
        }
        this.$router.push('/');
      }
    },
    resetForm() {
      this.title = '';
      this.selectedTopic = null;
      this.selectedLocation = null;
      this.selectedPrivacy = '公开';
      this.videoThumbnail = '';
      this.videoURL = '';
      this.videoData = '';

      // 重置选中状态
      this.fetchData();
    }
  }
};
</script>

<style scoped>
.publish-page {
  padding: 0;
  padding-bottom: 100px;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue',
    Arial, sans-serif;
  background-color: #fafafa;
  min-height: 100vh;
}

/* 顶部导航栏 */
.top-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background-color: #fff;
  border-bottom: 1px solid #e5e5e5;
  position: sticky;
  top: 0;
  z-index: 101;
}

.nav-left,
.nav-right {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-icon {
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.nav-title {
  font-size: 18px;
  font-weight: 600;
  color: #111;
}

/* 视频预览 */
.video-preview {
  width: 100%;
  height: 300px;
  background-color: #000;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-img,
.preview-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
}

.placeholder-icon {
  width: 48px;
  height: 48px;
  margin-bottom: 8px;
  opacity: 0.6;
}

.placeholder-text {
  font-size: 14px;
}

/* 内容区域 */
.input-area {
  padding: 16px;
  background-color: #fff;
  margin-bottom: 10px;
  position: relative;
}

.title-textarea {
  width: 100%;
  border: none;
  resize: none;
  padding: 12px;
  font-size: 16px;
  background: #fafafa;
  border-radius: 12px;
  outline: none;
  font-family: inherit;
  box-shadow: inset 0 0 0 1px #e5e5e5;
  transition: all 0.2s ease;
}

.title-textarea:focus {
  box-shadow: inset 0 0 0 2px #ff2c55;
  background-color: #fff;
}

.char-count {
  position: absolute;
  right: 28px;
  bottom: 28px;
  font-size: 12px;
  color: #999;
}

/* 设置区域 */
.setting-section {
  background-color: #fff;
  margin-bottom: 10px;
  padding: 16px;
}

.section-title {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 12px;
  color: #111;
}

/* 话题和位置容器 */
.topic-container,
.location-container,
.privacy-container {
  margin-top: 8px;
}

/* 高级设置 */
.advance-settings {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  background-color: #f5f5f5;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.advance-settings:hover {
  background-color: #e5e5e5;
}

.settings-text {
  font-size: 14px;
  color: #333;
}

.settings-icon {
  width: 20px;
  height: 20px;
  opacity: 0.6;
}

/* 底部发布栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 16px;
  background-color: #fff;
  box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.08);
  z-index: 100;
}

.publish-btn-container {
  margin-bottom: 12px;
}

.publish-btn {
  width: 100%;
  padding: 14px;
  background-color: #ff2c55;
  color: #fff;
  border: none;
  border-radius: 28px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  opacity: 1;
}

.publish-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background-color: #ff2c55;
}

.publish-btn:hover:not(:disabled) {
  background-color: #ff1a47;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 44, 85, 0.3);
}

.bottom-actions {
  display: flex;
  justify-content: center;
}

.action-item {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #666;
}

.action-icon {
  width: 20px;
  height: 20px;
  margin-right: 4px;
}

.action-text {
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .video-preview {
    height: 250px;
  }

  .publish-page {
    padding-bottom: 90px;
  }
}
</style>
