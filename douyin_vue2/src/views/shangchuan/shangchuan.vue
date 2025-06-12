<template>
  <div class="camera-container">
    <!-- 顶部操作栏 -->
    <div class="top-bar">
      <button class="close-btn" @click="goback">×</button>
      <button class="music-btn" @click="showMusic = true">🎵 选择音乐</button>
    </div>
    <!-- 右侧工具栏 -->
    <div class="right-sidebar">
      <button class="sidebar-btn" @click="showSwitchCamera = true">🔄</button>
      <button class="sidebar-btn" @click="showFlash = true">⚡</button>
      <button class="sidebar-btn" @click="showSettings = true">⚙️</button>
      <button class="sidebar-btn" @click="showEffects = true">🌀</button>
      <button class="sidebar-btn" @click="showTimer = true">⏱️</button>
      <button class="sidebar-btn" @click="showBeauty = true">💄</button>
      <button class="sidebar-btn" @click="showDownload = true">⬇️</button>
    </div>

    <!-- 拍摄区域 -->
    <div class="capture-area" v-if="bottomNav">
      <button class="feature-btn" @click="showEffects = true">✨ 特效</button>
      <div class="capture-wrapper">
        <!-- 进度环，仅视频录制时显示 -->
        <svg
          v-if="isRecording"
          class="progress-ring"
          :width="ringSize"
          :height="ringSize"
        >
          <circle
            class="progress-ring__circle"
            :stroke-dasharray="circumference"
            :stroke-dashoffset="circumference * (1 - recordProgress)"
            :r="ringRadius"
            :cx="ringSize / 2"
            :cy="ringSize / 2"
          />
        </svg>
        <button
          ref="captureBtn"
          class="capture-btn"
          :class="{ 'photo-animate': photoAnimate }"
          @mousedown.prevent="pressStart"
          @touchstart.prevent="pressStart"
          @mouseup.prevent="pressEnd"
          @touchend.prevent="pressEnd"
          @mouseleave.prevent="cancelPress"
        ></button>
      </div>
      <button class="gallery-btn" @click="handleClick">🖼 相册</button>
    </div>

    <!-- 隐藏文件输入 -->
    <input
      v-show="false"
      ref="fileInput"
      type="file"
      accept="image/*"
      @change="handleFileChange"
    />

    <!-- 摄像头预览 & Canvas -->
    <video ref="video" autoplay playsinline muted class="video-preview"></video>
    <canvas ref="canvas" style="display: none"></canvas>

    <!-- 拍照结果 -->
    <div v-if="photo" class="result-container">
      <img :src="photo" class="result-media" />
    </div>
    <!-- 录制结果 -->
    <div v-if="videoURL" class="result-container">
      <video :src="videoURL" controls class="result-media"></video>
    </div>

    <!-- 上传下一步按钮 -->
    <div class="bottomButton" v-if="!bottomNav">
      <FlexibleButtonPanel
        :items="buttonItems"
        :cols="2"
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
              style="width: 24px; height: 24px; border-radius: 12px"
            />
            <span style="margin-left: 4px">{{ item.text }}</span>
          </div>
        </template>
      </FlexibleButtonPanel>
    </div>

    <!-- 弹窗 -->
    <SlidePopup
      v-if="isAnyPopupVisible"
      direction="bottom"
      width="100vw"
      height="40vh"
      :isEdge="false"
      @close="closeAllPopups"
    >
      <!-- 弹窗详情保持不变 -->
    </SlidePopup>
    <!-- 底部导航 -->
    <div class="bottom-bar" v-if="bottomNav">
      <button class="nav-btn" @click="selectTab('text')">文字</button>
      <button
        class="nav-btn"
        @click="selectTab('camera')"
        :class="{ active: activeTab === 'camera' }"
      >
        相机
      </button>
      <button class="nav-btn" @click="selectTab('template')">模板</button>
      <button class="nav-btn" @click="selectTab('live')">开直播</button>
    </div>
  </div>
</template>

<script>
import SlidePopup from '@/components/SlidePopup.vue';
import FlexibleButtonPanel from '@/components/FlexibleButtonPanel.vue';
import { useVideoStore } from '../../stores/uploadVideo';
export default {
  name: 'CameraInterface',
  components: { SlidePopup, FlexibleButtonPanel },
  data() {
    return {
      // 底部按钮项
      buttonItems: [
        {
          text: '朋友日常',
          bgColor: '#464646',
          icon: 'http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960'
        },
        { text: '下一步', bgColor: '#ff2c55', borderRadius: 20 }
      ],
      bottomNav: true,
      stream: null,
      photo: '',
      videoURL: '',
      input: '',
      // 长按／短按检测
      pressTimer: null,
      pressThreshold: 500,
      longPressActive: false,
      // 拍摄状态
      activeTab: '',
      isRecording: false,
      recordChunks: [],
      recordProgress: 0,
      recordInterval: null,
      recordingStart: 0,
      mediaRecorder: null,
      mediaRecorderStream: null,
      // 拍照动画
      photoAnimate: false,
      videoBlob: '',
      // 进度环参数
      ringSize: 90,
      ringRadius: 40,
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
  computed: {
    circumference() {
      return 2 * Math.PI * this.ringRadius;
    },
    isAnyPopupVisible() {
      return [
        this.showMusic,
        this.showSwitchCamera,
        this.showFlash,
        this.showSettings,
        this.showEffects,
        this.showTimer,
        this.showBeauty,
        this.showDownload
      ].some((v) => v);
    }
  },
  mounted() {
    this.startCamera();
  },
  beforeDestroy() {
    if (this.stream) this.stream.getTracks().forEach((t) => t.stop());
    if (this.mediaRecorderStream)
      this.mediaRecorderStream.getTracks().forEach((t) => t.stop());
    clearTimeout(this.pressTimer);
    clearInterval(this.recordInterval);
    clearInterval(this.timerId);
  },
  methods: {
    goback() {
      this.$router.back();
    },
    selectTab(tab) {
      // 切换 Tab
    },
    onBtnClick({ index, item }) {
      if (item.text === '下一步') {
        debugger;
        if (this.photo) {
          {
            this.$router.push({
              path: '/shangchuandetail',
              query: { photo: this.photo }
            });
          }
        } else {
          debugger;
          this.$router.push({
            path: '/shangchuandetail',
            query: { video: this.currentVideoId }
          });
        }
      }
    },
    onBtnLongPress() {},
    // 相册
    handleClick() {
      this.$refs.fileInput.click();
    },
    handleFileChange(e) {
      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (ev) => console.log(ev.target.result);
        reader.readAsDataURL(file);
      }
    },
    // 拍照与录像区交互
    pressStart() {
      this.longPressActive = false;
      this.pressTimer = setTimeout(() => {
        this.longPressActive = true;
        this.startRecording();
      }, this.pressThreshold);
    },
    pressEnd() {
      clearTimeout(this.pressTimer);
      if (this.longPressActive) {
        this.stopRecording();
      } else {
        this.takePhoto();
      }
    },
    cancelPress() {
      clearTimeout(this.pressTimer);
    },
    // 启动摄像头
    async startCamera() {
      try {
        this.stream = await navigator.mediaDevices.getUserMedia({
          video: { facingMode: 'user' }
        });
        this.$refs.video.srcObject = this.stream;
      } catch (err) {
        console.error('启动摄像头失败', err);
      }
    },
    // 拍照
    takePhoto() {
      debugger;
      this.bottomNav = false;
      const video = this.$refs.video;
      const canvas = this.$refs.canvas;
      const ctx = canvas.getContext('2d');
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      ctx.drawImage(video, 0, 0);
      this.photo = canvas.toDataURL('image/png');
      this.photoAnimate = true;
      setTimeout(() => (this.photoAnimate = false), 300);
    },
    // 录像
    async startRecording() {
      if (this.isRecording) return;
      this.recordChunks = [];
      this.recordProgress = 0;
      try {
        this.mediaRecorderStream = await navigator.mediaDevices.getUserMedia({
          video: true,
          audio: true
        });
        this.mediaRecorder = new MediaRecorder(this.mediaRecorderStream);
        this.mediaRecorder.ondataavailable = (e) =>
          e.data.size && this.recordChunks.push(e.data);
        this.mediaRecorder.start();
        this.recordingStart = Date.now();
        this.isRecording = true;
        this.recordInterval = setInterval(() => {
          const elapsed = Date.now() - this.recordingStart;
          this.recordProgress = Math.min(elapsed / 10000, 1);
          if (elapsed >= 10000) this.stopRecording();
        }, 100);
      } catch (err) {
        console.error('录像失败', err);
      }
    },
    // stopRecording() {
    //   if (!this.isRecording) return;
    //   clearInterval(this.recordInterval);
    //   this.mediaRecorder.stop();
    //   this.mediaRecorderStream.getTracks().forEach((t) => t.stop());
    //   this.mediaRecorder.onstop = () => {
    //     const duration = Date.now() - this.recordingStart;
    //     const blob = new Blob(this.recordChunks, { type: 'video/mp4' });
    //     debugger;
    //     this.videoBlob = blob;
    //     this.videoURL = URL.createObjectURL(blob);
    //     if (duration < this.pressThreshold) this.takePhoto();
    //   };
    //   this.isRecording = false;
    //   this.bottomNav = false;
    // },
    stopRecording() {
      if (!this.isRecording) return;
      clearInterval(this.recordInterval);
      this.mediaRecorder.stop();
      this.mediaRecorderStream.getTracks().forEach((t) => t.stop());

      this.mediaRecorder.onstop = () => {
        const duration = Date.now() - this.recordingStart;
        const blob = new Blob(this.recordChunks, { type: 'video/mp4' });

        // 存储视频到全局库
        const videoStore = useVideoStore();
        const videoId = `video_${Date.now()}`; // 生成唯一ID
        videoStore.addVideo(videoId, blob, {
          duration,
          size: blob.size,
          type: blob.type,
          blob: blob
        });
        // 保存ID到本地状态，用于后续跳转
        this.currentVideoId = videoId;
        this.videoBlob = blob;
        this.videoURL = URL.createObjectURL(blob);

        if (duration < this.pressThreshold) this.takePhoto();
      };

      this.isRecording = false;
      this.bottomNav = false;
    },
    // 其余弹窗方法等... 保持原有逻辑
    switchCamera() {},
    toggleFlash() {},
    startTimer() {},
    closeAllPopups() {}
  }
};
</script>

<style scoped>
.camera-container {
  position: relative;
  height: 100vh;
  background: black;
  color: white;
  overflow: hidden;
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
.close-btn,
.music-btn {
  font-size: 20px;
  background: transparent;
  border: none;
  color: white;
  cursor: pointer;
}
.music-btn {
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
  position: fixed;
  bottom: 60px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  z-index: 20;
}
.feature-btn,
.gallery-btn {
  background: transparent;
  border: none;
  cursor: pointer;
  font-size: 18px;
  color: white;
}
.capture-wrapper {
  position: relative;
  width: 90px;
  height: 90px;
}
.capture-btn {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 70px;
  height: 70px;
  background: white;
  border-radius: 50%;
  z-index: 2;
}
@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
}
.capture-btn.photo-animate {
  animation: pulse 0.3s ease;
}
.progress-ring {
  position: absolute;
  top: 0;
  left: 0;
  transform: rotate(-90deg);
  z-index: 1;
}
.progress-ring__circle {
  fill: transparent;
  stroke: white;
  stroke-width: 4;
  transition: stroke-dashoffset 0.1s linear;
}
.bottomButton {
  z-index: 10;
  position: fixed;
  bottom: 10px;
  left: 50%;
  width: 95%;
  transform: translateX(-50%);
}
.video-preview {
  position: absolute;
  left: 0;
  width: 100%;
  background: black;
  z-index: 1;
}
.result-container {
  position: relative;
  z-index: 10;
  margin-top: 10px;
  background: #222;
  padding: 10px;
}
.result-media {
  max-width: 100%;
  height: 80vh;
}
</style>
