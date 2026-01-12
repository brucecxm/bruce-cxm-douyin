<template>
  <div class="camera-container">
    <!-- Top Bar -->
    <div class="top-bar">
      <button class="close-btn" @click="goback">×</button>
      <button class="music-btn" @click="show = true">🎵 选择音乐</button>
    </div>

    <!-- Right Sidebar -->
    <div class="right-sidebar">
      <button class="sidebar-btn">🔄</button>
      <button class="sidebar-btn">⚡</button>
      <button class="sidebar-btn">⚙️</button>
      <button class="sidebar-btn">🌀</button>
      <button class="sidebar-btn">⏱️</button>
      <button class="sidebar-btn">💄</button>
      <button class="sidebar-btn">⬇️</button>
    </div>

    <!-- Bottom Navigation -->
    <div class="bottom-bar">
      <button class="nav-btn">文字</button>
      <button class="nav-btn active">相机</button>
      <button class="nav-btn">模板</button>
      <button class="nav-btn">开直播</button>
    </div>

    <!-- Capture Area -->
    <div class="capture-area">
      <button class="feature-btn">✨ 特效</button>
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

    <!-- 弹窗 -->
    <SlidePopup
      v-if="show"
      :direction="'bottom'"
      width="100vw"
      height="40vh"
      :isEdge="false"
      @close="show = false"
    >
      <el-input v-model="input" placeholder="请输入内容"></el-input>
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
      show: false,
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
      }
    };
  },
  beforeDestroy() {
    // 停止摄像头
    if (this.stream) {
      this.stream.getTracks().forEach((track) => track.stop());
    }
  },
  mounted() {
    this.startCamera();
  },
  computed: {
    filteredSongs() {
      return this.songList[this.parentMessage[this.activeTabIndex]] || [];
    }
  },
  methods: {
    handleClick() {
      const isAndroid = /android/i.test(navigator.userAgent);
      const isPC = !/android|iphone|ipad|ipod/i.test(navigator.userAgent);

      console.log('当前设备是：', isAndroid ? '安卓' : isPC ? 'PC' : '其他');

      // 安卓和PC都可以通过 input 打开，只是显示效果不同
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        console.log('用户选择了文件：', file);

        // 示例：读取为 base64 可预览
        const reader = new FileReader();
        reader.onload = (e) => {
          const imageData = e.target.result;
          console.log('图片 base64 数据：', imageData);
          // 你可以在这里上传文件或显示预览
        };
        reader.readAsDataURL(file);
      }
    },
    async startCamera() {
      try {
        this.stream = await navigator.mediaDevices.getUserMedia({
          video: { facingMode: 'user' } // 前置摄像头，可改为 'environment' 后置摄像头
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
      // 同步canvas大小和视频实际宽高
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
    }
  }
};
</script>

<style scoped>
.song-list {
  padding: 10px;
  overflow-y: auto;
  max-height: 30vh;
}
.song-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  margin-right: 10px;
  object-fit: cover;
}
.info {
  display: flex;
  flex-direction: column;
}
.title {
  font-weight: bold;
  color: #4f4f4f;
  font-size: 14px;
}
.author {
  font-size: 12px;
  color: #888;
}
.camera-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  background: #000;
  overflow: hidden;
  color: white;
}
.top-bar {
  position: absolute;
  top: 0;
  width: 100%;
  display: flex;
  justify-content: space-between;
  padding: 10px;
  z-index: 20;
}
.close-btn,
.music-btn {
  background: rgba(0, 0, 0, 0.5);
  border: none;
  color: white;
  padding: 6px 10px;
  border-radius: 5px;
}
.music-btn {
  position: absolute;
  right: 40vw;
  top: 10px;
}
.right-sidebar {
  position: absolute;
  top: 50px;
  right: 10px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  z-index: 20;
}
.sidebar-btn {
  background: rgba(0, 0, 0, 0.5);
  border: none;
  color: white;
  padding: 6px;
  border-radius: 50%;
  font-size: 30px;
}
.bottom-bar {
  position: absolute;
  bottom: 0;
  width: 100%;
  display: flex;
  justify-content: space-around;
  padding: 10px;
  background: rgba(0, 0, 0, 0.5);
  z-index: 20;
}
.nav-btn {
  color: white;
  background: transparent;
  border: none;
  font-size: 16px;
}
.nav-btn.active {
  font-weight: bold;
}
.capture-area {
  position: absolute;
  bottom: 60px;
  width: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
  z-index: 20;
}
.feature-btn,
.gallery-btn {
  background: rgba(0, 0, 0, 0.5);
  border: none;
  color: white;
  padding: 10px 16px;
  border-radius: 20px;
  font-size: 14px;
}
.capture-btn {
  width: 80px;
  height: 80px;
  border: 6px solid white;
  border-radius: 50%;
  background: transparent;
  cursor: pointer;
}
</style>
