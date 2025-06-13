<script>
export default {
  name: 'App',
  mounted() {
    // 方法1: 使用CSS属性
    document.body.style.touchAction = 'pan-y';
    document.body.style.overflow = 'hidden';
    document.body.style.overflowY = 'scroll';
    document.body.style.webkitOverflowScrolling = 'touch';

    // 方法2: 使用JavaScript监听触摸事件
    this.setupTouchEvents();
  },
  methods: {
    setupTouchEvents() {
      let scrollStart = 0;
      let isHandling = false;

      const handleTouchStart = (e) => {
        scrollStart = e.touches[0].clientY;
        isHandling = true;
      };

      const handleTouchMove = (e) => {
        if (!isHandling) return;

        const currentY = e.touches[0].clientY;
        const diffY = currentY - scrollStart;

        // 当页面滚动到顶部且用户继续向下滑动时阻止默认行为
        if (window.scrollY <= 0 && diffY > 0) {
          e.preventDefault();
        }
      };

      const handleTouchEnd = () => {
        isHandling = false;
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
