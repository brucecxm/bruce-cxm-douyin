<template>
  <div class="camera-container">
    <!-- Top Bar -->
    <div class="top-bar">
      <button class="close-btn" @click="goback">×</button>
      <button class="music-btn" @click="showMusic = true">🎵 选择音乐</button>
    </div>

    <!-- Right Sidebar -->
    <div class="right-sidebar">
      <button class="sidebar-btn" @click="showSwitchCamera = true">🔄</button>
      <button class="sidebar-btn" @click="showFlash = true">⚡</button>
      <button class="sidebar-btn" @click="showSettings = true">⚙️</button>
      <button class="sidebar-btn" @click="showEffects = true">🌀</button>
      <button class="sidebar-btn" @click="showTimer = true">⏱️</button>
      <button class="sidebar-btn" @click="showBeauty = true">💄</button>
      <button class="sidebar-btn" @click="showDownload = true">⬇️</button>
    </div>

    <!-- Bottom Navigation -->
    <div class="bottom-bar">
      <button class="nav-btn" @click="selectTab('text')">文字</button>
      <button class="nav-btn active" @click="selectTab('camera')">相机</button>
      <button class="nav-btn" @click="selectTab('template')">模板</button>
      <button class="nav-btn" @click="selectTab('live')">开直播</button>
    </div>

    <!-- Capture Area -->
    <div class="capture-area">
      <button class="feature-btn" @click="showEffects = true">✨ 特效</button>
      <button class="capture-btn" @click="takePhoto"></button>
      <button class="gallery-btn" @click="handleClick">🖼 相册</button>
    </div>
    <input
      v-show="false"
      ref="fileInput"
      type="file"
      accept="image/*"
      style="display: none"
      @change="handleFileChange"
    />
    <!-- 视频预览 -->
    <video
      ref="video"
      autoplay
      playsinline
      muted
      style="
        width: 100%;
        height: auto;
        background: black;
        position: absolute;
        top: 50px;
        left: 0;
        z-index: 1;
      "
    ></video>

    <!-- 隐藏canvas用于截图 -->
    <canvas ref="canvas" style="display: none"></canvas>

    <!-- 拍照结果 -->
    <div
      v-if="photo"
      style="
        position: relative;
        z-index: 10;
        margin-top: 10px;
        background: #222;
        padding: 10px;
      "
    >
      <h3>拍照结果：</h3>
      <img :src="photo" style="max-width: 100%" />
    </div>

    <!-- 统一弹窗 -->
    <SlidePopup
      v-if="isAnyPopupVisible"
      :direction="'bottom'"
      width="100vw"
      height="40vh"
      :isEdge="false"
      @close="closeAllPopups"
    >
      <template v-if="showMusic">
        <el-input v-model="input" placeholder="搜索音乐"></el-input>
        <under-line-tags-vue
          :navItems="parentMessage"
          :active-index.sync="activeTabIndex"
          @change="handleTabChange"
        />
        <div class="song-list">
          <div
            class="song-item"
            v-for="(song, index) in filteredSongs"
            :key="index"
          >
            <img class="avatar" :src="song.avatar" />
            <div class="info">
              <div class="title">{{ song.title }}</div>
              <div class="author">{{ song.author }}</div>
            </div>
          </div>
        </div>
      </template>

      <template v-else-if="showSwitchCamera">
        <h3>切换摄像头</h3>
        <button @click="switchCamera">切换前后摄像头</button>
      </template>

      <template v-else-if="showFlash">
        <h3>闪光灯设置</h3>
        <button @click="toggleFlash">
          闪光灯: {{ flashOn ? '开' : '关' }}
        </button>
      </template>

      <template v-else-if="showSettings">
        <h3>设置</h3>
        <p>这里可以放置摄像头相关设置</p>
      </template>

      <template v-else-if="showEffects">
        <h3>特效</h3>
        <p>选择不同的滤镜或特效</p>
      </template>

      <template v-else-if="showTimer">
        <h3>倒计时</h3>
        <button @click="startTimer">开始倒计时(3秒)</button>
      </template>

      <template v-else-if="showBeauty">
        <h3>美颜</h3>
        <p>这里放置美颜参数调节</p>
      </template>

      <template v-else-if="showDownload">
        <h3>下载照片</h3>
        <button @click="downloadPhoto" :disabled="!photo">下载当前照片</button>
      </template>
    </SlidePopup>
  </div>
</template>

<script>
import SlidePopup from '@/components/SlidePopup.vue';
import underLineTagsVue from '@/components/underLineTags.vue';

export default {
  name: 'CameraInterface',
  components: {
    SlidePopup,
    underLineTagsVue
  },
  data() {
    return {
      stream: null,
      photo: '',
      input: '',
      parentMessage: ['推荐', '热门', '收藏', '用过'],
      activeTabIndex: 0,
      songList: {
        推荐: [
          {
            title: '@民哥创作的原声',
            author: '民哥 · 1:30',
            avatar: 'https://example.com/avatar1.jpg'
          },
          {
            title: '车大别盼',
            author: 'see you again x2.5 · 1:51',
            avatar: 'https://example.com/avatar2.jpg'
          },
          {
            title: '@江汐K创作的原声',
            author: '江汐K · 0:14',
            avatar: 'https://example.com/avatar3.jpg'
          },
          {
            title: '@敬山平创作的原声',
            author: '敬山平 · 0:15',
            avatar: 'https://example.com/avatar4.jpg'
          },
          {
            title: '美好降临',
            author: '',
            avatar: 'https://example.com/avatar5.jpg'
          }
        ],
        热门: [
          {
            title: '@民哥sssss创作的原声',
            author: '民哥 · 1:30',
            avatar: 'https://example.com/avatar1.jpg'
          },
          {
            title: '车大别盼',
            author: 'see you again x2.5 · 1:51',
            avatar: 'https://example.com/avatar2.jpg'
          },
          {
            title: '@江汐K创作的原声',
            author: '江汐K · 0:14',
            avatar: 'https://example.com/avatar3.jpg'
          },
          {
            title: '@敬山平创作的原声',
            author: '敬山平 · 0:15',
            avatar: 'https://example.com/avatar4.jpg'
          },
          {
            title: '美好降临',
            author: '',
            avatar: 'https://example.com/avatar5.jpg'
          }
        ],
        收藏: [],
        用过: []
      },
      // 弹窗控制
      showMusic: false,
      showSwitchCamera: false,
      showFlash: false,
      showSettings: false,
      showEffects: false,
      showTimer: false,
      showBeauty: false,
      showDownload: false,
      flashOn: false,
      usingFrontCamera: true,
      timerId: null,
      timerCount: 3
    };
  },
  beforeDestroy() {
    if (this.stream) {
      this.stream.getTracks().forEach((track) => track.stop());
    }
    this.clearTimer();
  },
  mounted() {
    this.startCamera();
  },
  computed: {
    filteredSongs() {
      return this.songList[this.parentMessage[this.activeTabIndex]] || [];
    },
    isAnyPopupVisible() {
      return (
        this.showMusic ||
        this.showSwitchCamera ||
        this.showFlash ||
        this.showSettings ||
        this.showEffects ||
        this.showTimer ||
        this.showBeauty ||
        this.showDownload
      );
    }
  },
  methods: {
    handleClick() {
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          const imageData = e.target.result;
          console.log('图片 base64 数据：', imageData);
        };
        reader.readAsDataURL(file);
      }
    },
    async startCamera() {
      try {
        this.stream = await navigator.mediaDevices.getUserMedia({
          video: { facingMode: 'user' }
        });
        this.$refs.video.srcObject = this.stream;
      } catch (err) {
        console.error('摄像头启动失败：', err);
      }
    },
    takePhoto() {
      const video = this.$refs.video;
      const canvas = this.$refs.canvas;
      const ctx = canvas.getContext('2d');
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
      this.photo = canvas.toDataURL('image/png');
    },
    goback() {
      this.$router.push({ path: '/' });
    },
    handleTabChange(index) {
      this.activeTabIndex = index;
    },
    closeAllPopups() {
      this.showMusic = false;
      this.showSwitchCamera = false;
      this.showFlash = false;
      this.showSettings = false;
      this.showEffects = false;
      this.showTimer = false;
      this.showBeauty = false;
      this.showDownload = false;
    },
    selectTab(tab) {
      alert(`切换到 ${tab} 模块（示例，具体可实现）`);
    },
    switchCamera() {
      this.usingFrontCamera = !this.usingFrontCamera;
      if (this.stream) {
        this.stream.getTracks().forEach((track) => track.stop());
      }
      navigator.mediaDevices
        .getUserMedia({
          video: { facingMode: this.usingFrontCamera ? 'user' : 'environment' }
        })
        .then((stream) => {
          this.stream = stream;
          this.$refs.video.srcObject = stream;
        });
      this.closeAllPopups();
    },
    toggleFlash() {
      this.flashOn = !this.flashOn;
      alert(`闪光灯已${this.flashOn ? '打开' : '关闭'}（示例）`);
      // 这里可接入闪光灯API，浏览器端通常不支持闪光灯控制
      this.closeAllPopups();
    },
    startTimer() {
      if (this.timerId) return;
      this.timerCount = 3;
      alert(`倒计时开始：${this.timerCount}秒`);
      this.timerId = setInterval(() => {
        this.timerCount--;
        console.log(`倒计时：${this.timerCount}`);
        if (this.timerCount <= 0) {
          clearInterval(this.timerId);
          this.timerId = null;
          this.takePhoto();
          alert('拍照完成！');
          this.closeAllPopups();
        }
      }, 1000);
    },
    clearTimer() {
      if (this.timerId) {
        clearInterval(this.timerId);
        this.timerId = null;
      }
    },
    downloadPhoto() {
      if (!this.photo) return;
      const link = document.createElement('a');
      link.href = this.photo;
      link.download = 'photo.png';
      link.click();
      this.closeAllPopups();
    }
  }
};
</script>

<style scoped>
.camera-container {
  position: relative;
  height: 100vh;
  background: black;
  overflow: hidden;
  color: white;
  user-select: none;
}

.top-bar {
  position: fixed;
  top: 0;
  width: 100%;
  height: 50px;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px;
  z-index: 20;
}

.close-btn {
  font-size: 20px;
  background: transparent;
  border: none;
  color: white;
  cursor: pointer;
}

.music-btn {
  font-size: 20px;
  background: transparent;
  border: none;
  color: white;
  cursor: pointer;
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
}
.right-sidebar {
  position: fixed;
  top: 60px;
  right: 10px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  z-index: 20;
}

.sidebar-btn {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 20px;
  font-size: 20px;
  color: white;
  cursor: pointer;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 50px;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: space-around;
  align-items: center;
  z-index: 20;
}

.nav-btn {
  background: transparent;
  border: none;
  color: white;
  font-size: 16px;
  cursor: pointer;
}

.nav-btn.active {
  border-bottom: 2px solid #fff;
}

.capture-area {
  position: absolute;
  bottom: 60px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between; /* 两端对齐 */
  gap: 20px;
  padding: 0 20px; /* 给左右留点内边距 */
  align-items: center;
  z-index: 20;
}

.feature-btn,
.capture-btn,
.gallery-btn {
  border: none;
  cursor: pointer;
}

.feature-btn {
  color: white;
  font-size: 18px;
  background: transparent;
}

.capture-btn {
  width: 70px;
  height: 70px;
  background: white;
  border-radius: 50%;
}

.gallery-btn {
  color: white;
  font-size: 24px;
  background: transparent;
}

.song-list {
  margin-top: 10px;
  max-height: 200px;
  overflow-y: auto;
}

.song-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 20px;
  margin-right: 10px;
}

.info .title {
  font-weight: bold;
}

.info .author {
  font-size: 12px;
  color: #ccc;
}
</style>
