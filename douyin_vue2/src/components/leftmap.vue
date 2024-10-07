<template>
    <div id="map" style="height: 500px;"></div>
</template>
  
<script>
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

export default {
    name: 'MapComponent',
    mounted() {
        this.initMap();
    },
    methods: {
        initMap() {
            const map = L.map('map').setView([51.505, -0.09], 13); // 设置地图中心和缩放级别

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
            }).addTo(map);

            L.marker([51.5, -0.09]).addTo(map) // 添加标记
                .bindPopup('A pretty CSS3 popup.<br> Easily customizable.')
                .openPopup();
        },
    }
};
</script>
  
<style>
/* 这里可以添加自定义样式 */
</style>
  