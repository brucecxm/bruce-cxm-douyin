<script>
export default {
  name: 'App',
  data() {
    return {
      touchStartTime: 0,
      touchStartY: 0,
      touchEndTime: 0,
      touchEndY: 0,
      isScrolling: false,
      scrollThreshold: 300, // 速度阈值，单位为像素/秒
      displacementThreshold: 10 // 最小位移阈值，单位为像素
    };
  },
  mounted() {
    // 设置CSS属性
    document.body.style.touchAction = 'pan-y';
    document.body.style.overflow = 'hidden';
    document.body.style.overflowY = 'scroll';
    document.body.style.webkitOverflowScrolling = 'touch';

    // 设置触摸事件监听
    this.setupTouchEvents();
  },
  methods: {
    setupTouchEvents() {
      const handleTouchStart = (e) => {
        this.touchStartTime = Date.now();
        this.touchStartY = e.touches[0].clientY;
        this.isScrolling = true;
      };

      const handleTouchMove = (e) => {
        if (!this.isScrolling) return;

        const currentY = e.touches[0].clientY;
        const diffY = currentY - this.touchStartY;
        const currentTime = Date.now();
        const timeDiff = currentTime - this.touchStartTime;

        // 计算滑动速度 (像素/毫秒)
        const speed = Math.abs(diffY / timeDiff);

        // 当页面滚动到顶部且用户继续向下滑动时
        if (window.scrollY <= 0 && diffY > 0) {
          // 如果速度超过阈值且位移足够，允许滚动，否则阻止
          if (
            speed * 1000 > this.scrollThreshold &&
            Math.abs(diffY) > this.displacementThreshold
          ) {
            // 快速滑动 - 允许滚动
          } else {
            // 慢速滑动 - 阻止默认行为
            e.preventDefault();
          }
        }
      };

      const handleTouchEnd = () => {
        this.touchEndTime = Date.now();
        this.touchEndY = 0;
        this.isScrolling = false;
      };

      // 添加事件监听器
      document.addEventListener('touchstart', handleTouchStart, {
        passive: false
      });
      document.addEventListener('touchmove', handleTouchMove, {
        passive: false
      });
      document.addEventListener('touchend', handleTouchEnd);

      // 组件销毁时移除事件监听器
      this.$once('hook:beforeDestroy', () => {
        document.removeEventListener('touchstart', handleTouchStart);
        document.removeEventListener('touchmove', handleTouchMove);
        document.removeEventListener('touchend', handleTouchEnd);
      });
    }
  }
};
</script>

<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style></style>
