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
    >
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
      <!-- 用div包一下 避免父组件中absolute对子组件flex布局的影响 -->
      <div class="videoasideone">
        <videoaside-vue :video-data="videoboxdata[index]"></videoaside-vue>
      </div>

      <div class="videoarticle">
        <div class="position" @click="showPosition()">距离你1km</div>

        <videoarticle-vue></videoarticle-vue>
      </div>

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
import {
  homegetVideo,
  homegetVideomore,
  homegetVideocontent
} from '../api/video';
export default {
  components: {
    videoasideVue,
    videoarticleVue
  },
  watch: {},
  mounted() {
    this.updateVideoPlayback();

    // 自动发送请求给后端
    homegetVideo()
      .then((videoArr) => {
        console.log(videoArr);

        // 校验 videoArr.data 是否存在且为非空数组
        if (Array.isArray(videoArr.data) && videoArr.data.length > 0) {
          this.videoboxdata = videoArr.data;
        } else {
          console.log('获取到的视频数据为空或格式不正确:', videoArr.data);
          // 你可以在这里进行其他处理，比如给用户提示
        }
      })
      .catch((error) => {
        console.error('获取视频出错:', error);
        console.log('获取视频出错  videobox中的');
      });
  },
  data() {
    return {
      // 使用props传递数据 使用props的数据在组件中是单向传输的 但是我可以通过请求异步修改数据库中的数据 就可以了
      videoboxdata: [
        {
          videoUrl: '',
          videoArticle: '',
          username: '',
          userAvatar: '',
          likeNum: '1',
          commentNum: '10',
          shareNum: '100',
          collectNum: '1000',
          musicAvatar: '',
          musicName: ''
        },
        {
          videoUrl: '',
          videoArticle: '',
          username: '',
          userAvatar: '',
          likeNum: '2',
          commentNum: '20',
          shareNum: '200',
          collectNum: '2000',
          musicAvatar: '',
          musicName: ''
        },
        {
          videoUrl: '',

          videoArticle: '',
          username: '',
          userAvatar: '',
          likeNum: '3',
          commentNum: '30',
          shareNum: '300',
          collectNum: '3000',
          musicAvatar: '',
          musicName: ''
        },
        {
          videoUrl: '',

          videoArticle: '',
          username: '',
          userAvatar: '',
          likeNum: '4',
          commentNum: '40',
          shareNum: '400',
          collectNum: '4000',
          musicAvatar: '',
          musicName: ''
        }
      ],
      isDragging: false,
      startY: 0,
      currentY: 0,
      boxWidth: '100%',
      activeIndex: null,
      // 因为受下面函数的影响  如果boxheight的数值太小  滑动会不流畅 因此我用了900 上面乘以0.01
      boxHeight: 950,
      boxes: []
    };
  },
  created() {
    this.boxes = [
      { color: 'black', top: 0 },
      { color: 'black', top: 1 * this.boxHeight },
      { color: 'black', top: 2 * this.boxHeight },
      { color: 'black', top: 3 * this.boxHeight }
    ];
  },

  methods: {
    showPosition() {
      this.$router.push('/map');
    },

    startDrag(event) {
      //event.preventDefault();
      this.isDragging = true;
      this.startY = this.getEventClientY(event) - this.currentY;
    },
    stopDrag(event) {
      // event.preventDefault();
      this.isDragging = false;
      this.updateBoxPositionBasedOnCurrentY();
      this.updateVideoPlayback();
    },
    drag(event) {
      if (this.isDragging) {
        let newY = this.getEventClientY(event) - this.startY;
        newY = Math.max(
          -(this.boxHeight * (this.boxes.length - 1)),
          Math.min(newY, 0)
        );
        this.currentY = newY;
        this.updateBoxesPosition();
        this.updateVideoPlayback(); // 添加这一行
      }
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
    updateBoxPositionBasedOnCurrentY() {
      const nearestIndex = Math.round(Math.abs(this.currentY) / this.boxHeight);
      this.currentY = -nearestIndex * this.boxHeight;
      this.updateBoxesPosition();
    },
    updateVideoPlayback() {
      const containerHeight = this.$el.clientHeight; // 获取容器的实际高度
      const threshold = 0.9; // 设置一个阈值，例如90%可见才算完全可见

      this.$refs.videos.forEach((video, index) => {
        const box = this.boxes[index];
        const boxTop = box.top;
        const boxBottom = boxTop + this.boxHeight * 0.1; // 因为你的高度使用了 * 0.1 的缩放

        // 计算视频在视口中可见的比例
        const visibleHeight =
          Math.min(containerHeight, boxBottom) - Math.max(0, boxTop);
        const visibleRatio = visibleHeight / (this.boxHeight * 0.1);

        // 如果视频盒子几乎完全在视口内（根据阈值），播放视频，否则暂停
        if (visibleRatio >= threshold) {
          video.play();
        } else {
          video.pause();
        }
      });
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
.position {
  font-size: 0.5rem;
  width: 5rem;
  height: 1rem;
  background-color: green;
  color: white;
  /* 文字颜色设置为白色 */
  display: flex;
  /* 使用flex布局来居中文本 */
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
  border-radius: 0.5rem;
  /* 为盒子加上圆角 */
  transform: translateY(-3rem) translateX(0.5rem);
}

.videoasideone {
  display: block;
  z-index: 5;
  position: absolute;
  /* 侧边栏的位置 */
  right: 0;
  bottom: 6vh;
  margin-right: 4vw;
}

.videoarticle {
  display: block;
  z-index: 5;
  position: absolute;
  /* 侧边栏的位置 */
  left: 0;
  bottom: 2vh;
}

.videoone {
  display: block;
  z-index: 1;
  object-fit: cover;
  margin: 0 auto;
  width: 100%;
  height: 100%;
}

.container {
  display: flex;
  flex-direction: column;
  position: relative;
  height: 90vh;
}

.box {
  position: absolute;
  transition: top 0.3s ease;
}
</style>
