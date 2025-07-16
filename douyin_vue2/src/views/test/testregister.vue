<template>
  <div class="video-container">
    <video
      id="hls-player"
      class="video-js vjs-default-skin"
      controls
      preload="auto"
      width="100%"
      height="auto"
    >
      <source :src="videoUrl" type="application/x-mpegURL" />
      <p class="vjs-no-js">您的浏览器不支持HTML5视频播放，请升级浏览器。</p>
    </video>
  </div>
</template>

<script>
import { onMounted, onUnmounted, ref } from 'vue';
// 导入 video.js 核心及样式
import videojs from 'video.js';
import 'video.js/dist/video-js.css';
// 导入 HLS 支持（Vite 兼容）
import '@videojs/http-streaming';

export default {
  setup() {
    // 视频链接（替换为你的 m3u8 地址）
    const videoUrl = ref(
      'http://192.168.200.130:9000/douyin/testfile/hls/20230726_054720.mp4_427071097/index.m3u8?Content-Disposition=attachment%3B%20filename%3D%22testfile%2Fhls%2F20230726_054720.mp4_427071097%2Findex.m3u8%22&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minio%2F20250716%2F%2Fs3%2Faws4_request&X-Amz-Date=20250716T010410Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=74f918f930695f00828c276269b09a0704cd7664921706afda785e3bd4b3cc00'
    );
    let player = null;

    onMounted(() => {
      // 初始化播放器
      player = videojs('hls-player', {
        autoplay: false, // 自动播放（需用户交互后生效，浏览器限制）
        fluid: true, // 自适应容器大小
        responsive: true,
        playbackRates: [0.5, 1, 1.5, 2], // 倍速播放选项
        controlBar: {
          fullscreenToggle: true // 显示全屏按钮
        }
      });

      // 监听播放错误
      player.on('error', () => {
        console.error('播放错误详情：', player.error());
        alert('视频播放失败，请检查链接或网络状态');
      });

      // 监听加载状态（验证缓存行为）
      player.on('loadedmetadata', () => {
        console.log('视频元数据加载完成，开始缓存播放');
      });
    });

    onUnmounted(() => {
      // 销毁播放器，释放资源
      if (player) {
        player.dispose();
      }
    });

    return { videoUrl };
  }
};
</script>

<style scoped>
.video-container {
  width: 90%;
  max-width: 1200px;
  margin: 2rem auto;
  padding: 1rem;
  background: #f0f0f0;
  border-radius: 8px;
}

/* 调整视频播放器样式 */
::v-deep .video-js {
  border-radius: 4px;
  overflow: hidden;
}
</style>
