<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      scrollThreshold: 10, // 触发阻止的垂直滑动阈值(像素)
      velocityThreshold: 0.5, // 速度阈值(像素/毫秒)
      touchStartY: 0,
      touchStartTime: 0,
      isScrolling: false,
      preventCounter: 0
    };
  },
  mounted() {
    // 基础样式设置
    document.body.style.touchAction = 'pan-y';
    document.body.style.overflow = 'hidden';
    document.body.style.overflowY = 'scroll';
    document.body.style.webkitOverflowScrolling = 'touch';

    this.setupTouchEvents();
  },
  methods: {
    setupTouchEvents() {
      const handleTouchStart = (e) => {
        this.touchStartY = e.touches[0].clientY;
        this.touchStartTime = Date.now();
        this.isScrolling = false;
      };

      const handleTouchMove = (e) => {
        if (this.isScrolling) return;

        const currentY = e.touches[0].clientY;
        const diffY = currentY - this.touchStartY;
        const duration = Date.now() - this.touchStartTime;
        const velocity = Math.abs(diffY / duration); // 计算滑动速度

        // 动态调整阈值
        let effectiveThreshold = this.scrollThreshold;
        if (velocity > this.velocityThreshold) {
          // 快速滑动时降低阈值，提高敏感度
          effectiveThreshold *= 0.7;
        }

        // 页面在顶部且向下滑动超过阈值
        if (window.scrollY <= 0 && diffY > effectiveThreshold) {
          e.preventDefault();
          this.isScrolling = true;
          this.preventCounter++;

          // 添加视觉反馈
          this.showPreventFeedback();
        }
      };

      document.addEventListener('touchstart', handleTouchStart, {
        passive: false
      });
      document.addEventListener('touchmove', handleTouchMove, {
        passive: false
      });

      this.$once('hook:beforeDestroy', () => {
        document.removeEventListener('touchstart', handleTouchStart);
        document.removeEventListener('touchmove', handleTouchMove);
      });
    },

    showPreventFeedback() {
      // 添加阻止反馈样式
      const body = document.body;
      body.classList.add('prevent-feedback');

      setTimeout(() => {
        body.classList.remove('prevent-feedback');
      }, 150);
    }
  }
};
</script>

<style scoped>
/* 添加阻止操作的视觉反馈 */
.prevent-feedback {
  transition: background-color 0.15s;
  background-color: rgba(255, 255, 255, 0.9);
}
</style>
