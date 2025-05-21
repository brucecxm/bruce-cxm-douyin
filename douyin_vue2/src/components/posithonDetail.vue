<template>
  <div class="temp">
    <button @click="showmap">showmap</button>
  </div>
</template>

<script>
export default {
  name: 'AmapComponent',
  mounted() {
    this.initMap();
  },
  methods: {
    showmap() {
      this.$router.push('/map');
    }
  }
};
</script>

<style>
/* 这里可以添加自定义样式 */
</style>
