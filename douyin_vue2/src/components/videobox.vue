<template>
  <div
    class="container"
    @mousedown="startDrag"
    @mouseup="stopDrag"
    @mousemove="drag"
    @mouseleave="stopDrag"
    @touchstart="startDrag"
    @touchend="stopDrag"
    @touchmove="drag"
    :style="{ width: boxWidth, height: boxHeight * 0.1 + 'vh' }"
  >
    >
    <!-- 视频盒子 -->
    <div
      v-for="(box, index) in boxes"
      :key="index"
      class="box"
      :style="{
        top: box.top * 0.1 + 'vh',
        backgroundColor: box.color,
        width: boxWidth,
        height: boxHeight * 0.1 + 'vh'
      }"
    >
      >
      <!-- 视频侧边栏组件 -->
      <div class="videoasideone">
        <videoaside-vue :video-data="videoboxdata[index]"></videoaside-vue>
      </div>

      <!-- 视频文章组件 -->
      <div class="videoarticle">
        <videoarticle-vue :video-data="videoboxdata[index]"></videoarticle-vue>
      </div>

      <!-- 视频播放 -->
      <video
        ref="videos"
        class="videoone"
        :src="videoboxdata[index].videoUrl"
        preload="true"
        loop
        x5-video-player-type="h5-page"
        x5-video-player-fullscreen="false"
        webkit-playsinline="true"
        x5-playsinline="true"
        playsinline="true"
      >
        <p>您的浏览器不支持 video 标签。</p>
      </video>
    </div>
  </div>
</template>

<script>
import videoasideVue from './videoaside.vue';
import videoarticleVue from './videoarticle.vue';
import { homegetVideo } from '../api/video';

export default {
  components: {
    videoasideVue,
    videoarticleVue
  },
  data() {
    return {
      videoboxdata: [], // 初始的视频数据
      isDragging: false, // 是否正在拖拽
      startY: 0, // 拖拽起始 Y 坐标
      currentY: 0, // 当前 Y 坐标
      boxWidth: '100%', // 每个盒子的宽度
      activeIndex: null, // 当前活跃的盒子索引
      boxHeight: 950, // 每个盒子的高度
      boxes: [], // 存储视频盒子的位置数据
      videodatahistory: [], // 存储历史的视频数据
      page: 1, // 当前页数
      pageSize: 4, // 每次请求的视频数量
      debounceTimer: null // 防抖定时器
    };
  },

  watch: {
    // 当 videoboxdata 变化时，动态更新 boxes 数量
    videoboxdata(newData) {
      this.updateBoxesBasedOnVideoData(newData);
    }
  },

  created() {
    // 初始化时的视频盒子数据
    this.updateBoxesBasedOnVideoData(this.videoboxdata);
  },

  mounted() {
    // 初始化视频数据
    this.loadVideos();
  },

  methods: {
    // 请求视频数据
    loadVideos() {
      homegetVideo(this.page, this.pageSize, 'w')
        .then((videoArr) => {
          if (Array.isArray(videoArr.data) && videoArr.data.length > 0) {
            this.videodatahistory.push(...videoArr.data); // 更新历史视频数据
            this.videoboxdata = [...this.videodatahistory]; // 更新 videoboxdata 显示的视频数据
          }
        })
        .catch((error) => {
          console.error('获取视频出错:', error);
        });
    },

    // 更新盒子的位置基于 videoboxdata 的长度
    updateBoxesBasedOnVideoData(videoData) {
      this.boxes = videoData.map((_, index) => ({
        color: 'black',
        top: index * this.boxHeight
      }));
      this.updateBoxesPosition();
    },

    // 防抖处理
    debounceLoadVideos() {
      if (this.debounceTimer) {
        clearTimeout(this.debounceTimer); // 清除之前的定时器
      }
      this.debounceTimer = setTimeout(() => {
        if (this.isAtBottom()) {
          this.page++; // 增加分页
          this.loadVideos(); // 加载更多视频
        }
      }, 300); // 延迟300ms执行
    },

    // 更新视频播放状态
    updateVideoPlayback() {
      const containerHeight = this.$el.clientHeight;
      const threshold = 0.9;

      this.$refs.videos.forEach((video, index) => {
        const box = this.boxes[index];
        const boxTop = box.top;
        const boxBottom = boxTop + this.boxHeight * 0.1;

        const visibleHeight =
          Math.min(containerHeight, boxBottom) - Math.max(0, boxTop);
        const visibleRatio = visibleHeight / (this.boxHeight * 0.1);

        if (visibleRatio >= threshold) {
          video.play();
        } else {
          video.pause();
        }
      });
    },

    // 拖拽事件
    startDrag(event) {
      this.isDragging = true;
      this.startY = this.getEventClientY(event) - this.currentY;
    },

    stopDrag() {
      this.isDragging = false;
      this.updateBoxPositionBasedOnCurrentY();
      this.updateVideoPlayback();
    },

    drag(event) {
      if (this.isDragging) {
        let newY = this.getEventClientY(event) - this.startY;
        newY = Math.max(
          -(this.boxHeight * (this.boxes.length - 1)),
          Math.min(newY, 0)
        );
        this.currentY = newY;
        this.updateBoxesPosition();
        this.updateVideoPlayback();
        this.debounceLoadVideos();
      }
    },

    getEventClientY(event) {
      return event.type.startsWith('touch')
        ? event.touches[0].clientY
        : event.clientY;
    },

    updateBoxesPosition() {
      this.boxes.forEach((box, index) => {
        box.top = this.currentY + index * this.boxHeight;
      });
    },

    updateBoxPositionBasedOnCurrentY() {
      const nearestIndex = Math.round(Math.abs(this.currentY) / this.boxHeight);
      this.currentY = -nearestIndex * this.boxHeight;
      this.updateBoxesPosition();
    },

    isAtBottom() {
      const lastBox = this.boxes[this.boxes.length - 1];
      const containerHeight = this.$el.clientHeight;
      return lastBox.top + this.boxHeight * 0.1 <= containerHeight;
    },

    go(index) {
      this.activeIndex = index;
      const newY = -index * this.boxHeight;
      this.currentY = newY;
      this.updateBoxesPosition();
      this.updateVideoPlayback();
    }
  }
};
</script>

<style scoped>
.videoasideone {
  display: block;
  z-index: 5;
  position: absolute;
  right: 0;
  bottom: 6vh;
  margin-right: 4vw;
}

.videoarticle {
  display: block;
  z-index: 5;
  position: absolute;
  left: 0;
  bottom: 2vh;
}

.videoone {
  display: block;
  z-index: 10;
  object-fit: cover;
  margin: 0 auto;
  width: 100%;
  height: 100%;
}

.container {
  display: flex;
  flex-direction: column;
  position: relative;
  margin: 0;
  height: 95vh;
}

.box {
  position: absolute;
  transition: top 0.3s ease;
}
</style>
