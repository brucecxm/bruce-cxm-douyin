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
  name: 'videobox-vue',
  components: {
    videoasideVue,
    videoarticleVue
  },
  props: {
    externalVideos: {
      type: Array,
      default: () => []
    },
    onReachEnd: {
      type: Function,
      default: null
    }
  },
  data() {
    return {
      videoboxdata: [], // 当前显示的视频数据
      isDragging: false,
      startY: 0,
      currentY: 0,
      boxWidth: '100%',
      activeIndex: null,
      boxHeight: 950,
      boxes: [],
      videodatahistory: [], // 存储所有视频数据（外部传入+内部加载）
      page: 1,
      pageSize: 4,
      debounceTimer: null,
      dragDirection: null // 新增：记录滑动方向
    };
  },

  watch: {
    // 监听外部传入的视频变化
    externalVideos(newVideos) {
      if (newVideos && newVideos.length > 0) {
        this.appendExternalVideos(newVideos);
      }
    },

    // 当 videoboxdata 变化时更新盒子位置
    videoboxdata(newData) {
      this.updateBoxesBasedOnVideoData(newData);
    }
  },

  created() {
    if (this.externalVideos.length > 0) {
      this.appendExternalVideos(this.externalVideos);
    } else {
      this.loadVideos();
    }
  },

  methods: {
    // 允许父组件调用以传入分页数据
    appendPageData(newPageVideos) {
      if (Array.isArray(newPageVideos) && newPageVideos.length > 0) {
        this.videodatahistory.push(...newPageVideos);
        this.videoboxdata = [...this.videodatahistory];
      }
    },
    // 加载视频数据（分页）
    loadVideos() {
      homegetVideo(this.page, this.pageSize, 1)
        .then((videoArr) => {
          if (Array.isArray(videoArr.data) && videoArr.data.length > 0) {
            this.videodatahistory.push(...videoArr.data);
            this.videoboxdata = [...this.videodatahistory];
          }
        })
        .catch((error) => {
          console.error('获取视频出错:', error);
        });
    },

    // 追加外部传入的视频数据
    appendExternalVideos(videos) {
      // 避免重复添加
      const newVideos = videos.filter(
        (v) => !this.videodatahistory.some((old) => old.id === v.id)
      );
      if (newVideos.length > 0) {
        this.videodatahistory.push(...newVideos);
        this.videoboxdata = [...this.videodatahistory];
      }
    },

    updateBoxesBasedOnVideoData(videoData) {
      this.boxes = videoData.map((_, index) => ({
        color: 'black',
        top: index * this.boxHeight
      }));
      this.updateBoxesPosition();
    },

    debounceLoadVideos() {
      if (this.debounceTimer) {
        clearTimeout(this.debounceTimer);
      }
      this.debounceTimer = setTimeout(() => {
        if (this.isAtBottom()) {
          // 触发父组件分页方法
          if (this.onReachEnd) {
            this.onReachEnd();
          } else {
            // 如果没有传入分页方法，默认内部加载
            this.page++;
            this.loadVideos();
          }
        }
      }, 300);
    },

    updateVideoPlayback() {
      const containerHeight = this.$el.clientHeight;
      const threshold = 0.9;

      // 确保 $refs.videos 是数组
      if (!this.$refs.videos) return;
      const videos = Array.isArray(this.$refs.videos)
        ? this.$refs.videos
        : [this.$refs.videos];

      videos.forEach((video, index) => {
        if (!video || !this.boxes[index]) return;

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
    updateBoxPositionBasedOnCurrentY() {
      // 计算当前滑动距离相对于盒子高度的比例
      const ratio = Math.abs(this.currentY) / this.boxHeight;

      // 获取当前索引和小数部分
      const currentIndex = Math.floor(ratio);
      const decimalPart = ratio - currentIndex;

      // 快速滑动检测（新增逻辑）
      let nearestIndex = currentIndex;
      if (this.isFastSwipe) {
        // 快速上滑，切换到下一个视频
        if (
          this.dragDirection === 'up' &&
          currentIndex < this.boxes.length - 1
        ) {
          nearestIndex = currentIndex + 1;
        }
        // 快速下滑，切换到上一个视频
        else if (this.dragDirection === 'down' && currentIndex > 0) {
          nearestIndex = currentIndex - 1;
        }
        this.isFastSwipe = false; // 重置快速滑动标记
      }
      // 普通滑动，使用阈值判断
      else {
        // 设置统一的切换阈值
        const threshold = 0.1;
        nearestIndex =
          decimalPart >= threshold ? currentIndex + 1 : currentIndex;
      }
      // 确保索引在有效范围内
      const maxIndex = this.boxes.length - 1;
      const clampedIndex = Math.min(Math.max(nearestIndex, 0), maxIndex);

      // 更新位置
      this.currentY = -clampedIndex * this.boxHeight;
      this.updateBoxesPosition();
    },

    // 修改 drag 方法，添加速度检测
    drag(event) {
      if (this.isDragging) {
        const startDragTime = this.dragStartTime || Date.now();
        const currentTime = Date.now();
        const dragDuration = currentTime - startDragTime;

        let newY = this.getEventClientY(event) - this.startY;
        newY = Math.max(
          -(this.boxHeight * (this.boxes.length - 1)),
          Math.min(newY, 0)
        );

        // 计算滑动速度 (像素/毫秒)
        const dragDistance = Math.abs(newY - this.currentY);
        const speed = dragDuration > 0 ? dragDistance / dragDuration : 0;
        // 检测快速滑动 (速度阈值可根据实际情况调整)
        if (speed > 0.5) {
          this.isFastSwipe = true;
        }

        // 记录滑动方向和开始时间
        this.dragDirection = newY > this.currentY ? 'down' : 'up';
        this.dragStartTime = startDragTime === 0 ? currentTime : startDragTime;

        this.currentY = newY;
        this.updateBoxesPosition();
        this.updateVideoPlayback();
        this.debounceLoadVideos();
      }
    },

    // 修改 startDrag 方法，添加时间记录
    startDrag(event) {
      this.isDragging = true;
      this.startY = this.getEventClientY(event) - this.currentY;
      this.dragStartTime = Date.now(); // 记录拖拽开始时间
      this.isFastSwipe = false; // 重置快速滑动标记
    },
    stopDrag() {
      this.isDragging = false;
      this.updateBoxPositionBasedOnCurrentY();
      this.updateVideoPlayback();
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
