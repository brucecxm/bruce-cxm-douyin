<template>
  <div id="app">
    <div class="video-container">
      <video-player :currentVideo="currentVideo" />
    </div>

    <!-- 滑动提示 -->
    <div v-if="showSwipeHint" class="swipe-hint">
      <span v-if="swipeProgress < 100">向下滑动切换视频</span>
      <span v-else class="refresh-text">继续下拉刷新页面</span>
      <div class="progress-bar" :style="{ width: swipeProgress + '%' }"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      currentVideo: 1,
      videoCount: 5,

      // 阈值配置
      lightSwipeThreshold: 50, // 轻滑阈值(像素)
      forceSwipeThreshold: 150, // 用力滑阈值(像素)
      velocityThreshold: 0.8, // 速度阈值(像素/毫秒)

      // 滑动状态
      touchStartY: 0,
      touchStartTime: 0,
      currentY: 0,
      isScrolling: false,
      showSwipeHint: false,
      swipeProgress: 0,
      allowRefresh: false
    };
  },
  mounted() {
    this.setupTouchEvents();
  },
  methods: {
    setupTouchEvents() {
      const handleTouchStart = (e) => {
        if (window.scrollY > 10) return; // 页面不在顶部时不处理

        this.touchStartY = e.touches[0].clientY;
        this.touchStartTime = Date.now();
        this.currentY = this.touchStartY;
        this.isScrolling = false;
        this.showSwipeHint = true;
        this.swipeProgress = 0;
        this.allowRefresh = false;
      };

      const handleTouchMove = (e) => {
        if (!this.showSwipeHint) return;

        this.currentY = e.touches[0].clientY;
        const diffY = this.currentY - this.touchStartY;
        const duration = Date.now() - this.touchStartTime;
        const velocity = diffY / duration;

        // 计算进度
        this.swipeProgress = Math.min(
          100,
          (diffY / this.forceSwipeThreshold) * 100
        );

        // 轻滑切换视频
        if (diffY >= this.lightSwipeThreshold && !this.isScrolling) {
          this.isScrolling = true;
          this.switchVideo();
        }

        // 用力滑允许刷新
        if (
          diffY >= this.forceSwipeThreshold ||
          (velocity > this.velocityThreshold && diffY > 80)
        ) {
          this.allowRefresh = true;
        } else {
          // 阻止默认滑动行为
          e.preventDefault();
        }
      };

      const handleTouchEnd = () => {
        if (!this.showSwipeHint) return;

        this.showSwipeHint = false;

        // 如果允许刷新且用户释放时处于刷新区域
        if (
          this.allowRefresh &&
          this.currentY - this.touchStartY > this.forceSwipeThreshold * 0.8
        ) {
          this.refreshPage();
        }

        this.$refs.videoContainer?.scrollTo({ top: 0, behavior: 'smooth' });
      };

      document.addEventListener('touchstart', handleTouchStart, {
        passive: false
      });
      document.addEventListener('touchmove', handleTouchMove, {
        passive: false
      });
      document.addEventListener('touchend', handleTouchEnd);

      this.$once('hook:beforeDestroy', () => {
        document.removeEventListener('touchstart', handleTouchStart);
        document.removeEventListener('touchmove', handleTouchMove);
        document.removeEventListener('touchend', handleTouchEnd);
      });
    },

    switchVideo() {
      // 切换到下一个视频
      this.currentVideo = (this.currentVideo % this.videoCount) + 1;
      console.log(`切换到视频 ${this.currentVideo}`);
    },

    refreshPage() {
      // 模拟页面刷新
      console.log('刷新页面');
      this.$refs.videoContainer?.scrollTo({ top: 0, behavior: 'instant' });

      // 重置状态
      setTimeout(() => {
        this.allowRefresh = false;
      }, 500);
    }
  }
};
</script>

<style scoped>
.video-container {
  height: 100vh;
  overflow: hidden;
}

.swipe-hint {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  z-index: 100;
  transition: opacity 0.3s;
}

.progress-bar {
  height: 4px;
  background-color: #42b983;
  border-radius: 2px;
  min-width: 20px;
  transition: width 0.1s;
}

.refresh-text {
  color: #ff6b6b;
}
</style>
