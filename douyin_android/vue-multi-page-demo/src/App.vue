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
      touchStartTime: 0,
      touchStartY: 0,
      touchEndTime: 0,
      touchEndY: 0,
      isScrolling: false,
      scrollThreshold: 300,
      displacementThreshold: 10
    };
  },
  mounted() {
    // 设置CSS属性
    document.body.style.touchAction = 'pan-y';
    document.body.style.overflow = 'hidden';
    document.body.style.overflowY = 'scroll';
    document.body.style.webkitOverflowScrolling = 'touch';
  }
};
</script>

<style>
#app {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}
</style>
