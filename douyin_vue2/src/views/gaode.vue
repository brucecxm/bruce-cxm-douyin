<template>
  <div id="amap" style="height: 500px"></div>
</template>

<script>
export default {
  name: 'AmapComponent',
  mounted() {
    this.initMap();
  },
  methods: {
    initMap() {
      const map = new AMap.Map('amap', {
        center: [116.397428, 39.90923], // 设置地图中心
        zoom: 13 // 设置缩放级别
      });

      const marker = new AMap.Marker({
        position: new AMap.LngLat(116.397428, 39.90923) // 设置标记位置
      });
      marker.setMap(map); // 将标记添加到地图上
    }
  }
};
</script>

<style>
/* 这里可以添加自定义样式 */
</style>
