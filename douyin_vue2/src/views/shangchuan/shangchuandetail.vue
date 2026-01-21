<template>
  <div class="publish-page">
    <!-- 顶部导航栏 -->
    <div class="top-nav">
      <div class="nav-left" @click="goBack">
        <span class="nav-back-text">返回</span>
      </div>
      <div class="nav-title">发布作品</div>
      <div class="nav-right"></div>
    </div>

    <!-- 视频预览 -->
    <div class="video-preview">
      <div class="preview-container">
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
        <div class="edit-cover-btn" v-if="videoThumbnail || videoURL">
          编辑封面
        </div>
        <div v-if="!videoThumbnail && !videoURL" class="preview-placeholder">
          <div class="add-icon">+</div>
          <span class="placeholder-text">添加视频或图片</span>
        </div>
      </div>
    </div>

    <!-- 标题和描述输入 -->
    <div class="content-section">
      <div class="input-item">
        <input
          v-model="title"
          placeholder="添加标题"
          maxlength="50"
          class="title-input"
        />
        <div class="char-count title-count">{{ title.length }}/50</div>
      </div>
      <div class="input-item">
        <textarea
          v-model="description"
          placeholder="添加作品描述..."
          maxlength="100"
          class="description-textarea"
          rows="3"
        ></textarea>
        <div class="char-count desc-count">{{ description.length }}/100</div>
      </div>

      <!-- 快捷功能按钮 -->
      <div class="quick-actions">
        <div class="quick-btn"># 话题</div>
        <div class="quick-btn">@ 朋友</div>
        <div class="quick-btn">
          <span class="more-text">更多</span>
        </div>
      </div>
    </div>

    <!-- 话题选择 -->
    <div class="setting-section topics-section">
      <div class="section-label">热门话题</div>
      <div class="topics-list">
        <FlexibleButtonPanel
          :items="topicItems"
          :cols="3"
          :gap="8"
          @btnClick="onTopicClick"
        />
      </div>
    </div>

    <!-- 添加位置 -->
    <div class="setting-section location-section">
      <div class="location-header">
        <span class="location-label">位置</span>
        <span class="location-text">你在哪里</span>
      </div>
      <div class="locations-list">
        <FlexibleButtonPanel
          :items="locationItems"
          :cols="2"
          :gap="8"
          @btnClick="onLocationClick"
        />
      </div>
    </div>

    <!-- 音乐选择 -->
    <div class="setting-section music-section">
      <div class="section-label">选择音乐</div>
      <div class="music-list">
        <FlexibleButtonPanel
          :items="musicItems"
          :cols="3"
          :gap="8"
          @btnClick="onMusicClick"
        />
      </div>
    </div>

    <!-- 添加标签 -->
    <div class="setting-section tags-section">
      <div class="section-label">添加标签</div>
      <div class="tags-tip">发布成功后将保存内容至本地</div>
    </div>

    <!-- 底部操作栏 -->
    <div class="bottom-bar">
      <div class="bottom-content">
        <div class="share-btn" @click="onShare">
          <span class="share-text">分享</span>
        </div>

        <div
          class="daily-option"
          :class="{ active: isDaily }"
          @click="toggleDaily"
        >
          <span class="daily-text">限时日常</span>
        </div>

        <button class="publish-btn" @click="publishPost" :disabled="loading">
          <span v-if="loading">发布中...</span>
          <span v-else>发作品</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import FlexibleButtonPanel from '@/components/FlexibleButtonPanel.vue';
import {
  getTopics,
  getLocations,
  getPrivacies,
  getmumsic,
  getMusicList
} from '@/api/video.js';
export default {
  components: { FlexibleButtonPanel },
  data() {
    return {
      videoId: '',
      videoURL: '',
      userId: '123456', // 模拟用户 ID，真实情况从登录态获取
      videoThumbnail: '',
      title: '',
      description: '',
      selectedTopic: null,
      selectedLocation: null,
      selectedPrivacy: '公开',
      isDaily: false,
      videoData: '',
      loading: false,
      dataLoading: false,

      // 音乐相关
      musicItems: [],
      selectedMusic: null,
      musicLoading: false,

      // 从数据库获取的数据
      topicItems: [],
      locationItems: [],
      privacyItems: [],
      buttonItems: []
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
        const [topicsRes, locationsRes, privaciesRes, musicRes] =
          await Promise.all([
            getTopics(),
            getLocations(),
            getPrivacies(),
            getMusicList()
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

        // 处理音乐数据
        if (musicRes.data && Array.isArray(musicRes.data)) {
          this.musicItems = musicRes.data.map((music) => ({
            text: music.name,
            bgColor: '#f5f5f5',
            textColor: '#333',
            borderRadius: 16,
            padding: '8px 12px',
            musicId: music.id.toString()
          }));
        } else {
          // 如果没有获取到音乐数据，使用默认数据
          this.initMusicData();
        }
      } catch (error) {
        console.error('获取数据失败:', error);
        // 数据获取失败时使用默认数据
        this.setDefaultData();
      } finally {
        this.dataLoading = false;
      }
    },

    // 初始化音乐数据（备用）
    initMusicData() {
      // 这里使用模拟音乐数据，实际项目中应该从后端API获取
      this.musicItems = [
        {
          text: '热门音乐1',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '1'
        },
        {
          text: '热门音乐2',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '2'
        },
        {
          text: '热门音乐3',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '3'
        },
        {
          text: '热门音乐4',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '4'
        },
        {
          text: '热门音乐5',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '5'
        },
        {
          text: '热门音乐6',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '6'
        }
      ];
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
      // 默认音乐数据
      this.musicItems = [
        {
          text: '热门音乐1',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '1'
        },
        {
          text: '热门音乐2',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '2'
        },
        {
          text: '热门音乐3',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '3'
        },
        {
          text: '热门音乐4',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '4'
        },
        {
          text: '热门音乐5',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '5'
        },
        {
          text: '热门音乐6',
          bgColor: '#f5f5f5',
          textColor: '#333',
          borderRadius: 16,
          padding: '8px 12px',
          musicId: '6'
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

    // 音乐选择事件处理
    onMusicClick({ item, index }) {
      this.selectedMusic = item;
      console.log('点击音乐：', item.text, item.musicId);

      // 更新选中状态
      this.musicItems = this.musicItems.map((music, i) => ({
        ...music,
        bgColor: i === index ? '#ff2c55' : '#f5f5f5',
        textColor: i === index ? '#fff' : '#333',
        selected: i === index
      }));
    },
    onShare() {
      console.log('分享作品');
      // 分享功能逻辑
    },
    toggleDaily() {
      this.isDaily = !this.isDaily;
      console.log('切换限时日常:', this.isDaily);
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
        formData.append('musicId', this.selectedMusic?.musicId || '');
        formData.append('musicName', this.selectedMusic?.text || '');
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
      this.selectedMusic = null;
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
  padding-bottom: 80px;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue',
    Arial, sans-serif;
  background-color: #fff;
  min-height: 100vh;
}

/* 顶部导航栏 */
.top-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background-color: #fff;
  border-bottom: 1px solid #f0f0f0;
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

.nav-back-text {
  font-size: 16px;
  color: #000;
  font-weight: 500;
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
  background-color: #fff;
  padding: 20px 0;
  display: flex;
  justify-content: center;
}

.preview-container {
  width: 220px;
  height: 390px;
  background-color: #000;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
}

.preview-img,
.preview-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.edit-cover-btn {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.preview-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  height: 100%;
}

.add-icon {
  width: 48px;
  height: 48px;
  margin-bottom: 8px;
  opacity: 0.6;
  font-size: 40px;
  color: #999;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.placeholder-text {
  font-size: 14px;
}

/* 内容区域 */
.content-section {
  padding: 16px;
  background-color: #fff;
  margin-bottom: 10px;
}

.input-item {
  margin-bottom: 16px;
  position: relative;
}

.title-input {
  width: 100%;
  border: none;
  padding: 12px 0;
  font-size: 18px;
  font-weight: 600;
  outline: none;
  background-color: transparent;
  border-bottom: 1px solid transparent;
  transition: all 0.2s ease;
}

.title-input:focus {
  border-bottom-color: #ff2c55;
}

.description-textarea {
  width: 100%;
  border: none;
  resize: none;
  padding: 12px 0;
  font-size: 16px;
  outline: none;
  background-color: transparent;
  line-height: 1.5;
  font-family: inherit;
}

.char-count {
  position: absolute;
  right: 0;
  bottom: 4px;
  font-size: 12px;
  color: #999;
}

.title-count {
  bottom: 8px;
}

.desc-count {
  bottom: 4px;
}

/* 快捷功能按钮 */
.quick-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 0;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.quick-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background-color: #f5f5f5;
  border-radius: 16px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease;
}

.quick-btn:hover {
  background-color: #e5e5e5;
}

.more-text {
  font-size: 14px;
  color: #333;
}

/* 设置区域 */
.setting-section {
  background-color: #fff;
  margin-bottom: 10px;
  padding: 16px;
}

.section-label {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 12px;
  color: #111;
}

/* 话题区域 */
.topics-section {
  padding-bottom: 12px;
}

.topics-list {
  margin-top: 8px;
}

/* 位置区域 */
.location-section {
  padding-bottom: 12px;
}

.location-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 16px;
  color: #111;
  font-weight: 600;
}

.location-label {
  font-size: 14px;
  color: #666;
  margin-right: 8px;
  background-color: #f5f5f5;
  padding: 4px 8px;
  border-radius: 12px;
}

.location-text {
  font-weight: 600;
}

.locations-list {
  margin-top: 8px;
}

/* 标签区域 */
.tags-section {
  padding-bottom: 12px;
}

.tags-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

/* 底部操作栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 12px 16px;
  background-color: #fff;
  border-top: 1px solid #f0f0f0;
  z-index: 100;
}

.bottom-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.share-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 16px;
  background-color: #f5f5f5;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.share-btn:hover {
  background-color: #e5e5e5;
}

.share-text {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.daily-option {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  background-color: #f5f5f5;
  border-radius: 22px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid transparent;
}

.daily-option.active {
  background-color: #fff;
  border-color: #ff2c55;
}

.daily-text {
  font-size: 14px;
  color: #333;
}

.publish-btn {
  padding: 12px 24px;
  background-color: #ff2c55;
  color: #fff;
  border: none;
  border-radius: 22px;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .preview-container {
    width: 200px;
    height: 350px;
  }

  .daily-text {
    font-size: 13px;
  }

  .publish-btn {
    padding: 10px 20px;
    font-size: 15px;
  }

  .publish-page {
    padding-bottom: 80px;
  }
}
</style>
