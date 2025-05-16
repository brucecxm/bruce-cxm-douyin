<template>
  <div class="app" @touchstart="onTouchStart" @touchmove="onTouchMove" @touchend="onTouchEnd">
    <!-- 顶部导航栏 -->
    <div class="nav-bar" ref="navBar">
      <div 
        v-for="(t, idx) in videoTypes" 
        :key="t" 
        :class="['nav-item', { active: currentTypeIndex === idx }]" 
        @click="goToType(idx)"
        ref="navItems"
      >
        {{ t }}
      </div>
      <div class="nav-underline" :style="underlineStyle"></div>
    </div>

    <!-- 横向滑动容器，宽度 = 频道数 * 100vw -->
    <div class="horizontal-wrapper" :style="horizontalWrapperStyle">
      <!-- 每个频道的视频列表 -->
      <div 
        v-for="(type, typeIdx) in videoTypes" 
        :key="type" 
        class="vertical-wrapper"
        :style="verticalWrapperStyle(typeIdx)"
      >
        <video 
          v-for="(video, videoIdx) in videosMap[type]" 
          :key="video.id" 
          :src="video.url" 
          class="video-item" 
          :class="{ active: videoIdx === currentVideoIndex && typeIdx === currentTypeIndex }"
          playsinline
          webkit-playsinline
          muted
          loop
          preload="auto"
          @play="onVideoPlay(typeIdx, videoIdx)"
        ></video>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      videoTypes: ['热门', '关注', '附近'],
      videosMap: {
        '热门': [
          { id: 1, url: 'https://www.w3schools.com/html/mov_bbb.mp4' },
          { id: 2, url: 'https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.webm' },
        ],
        '关注': [
          { id: 3, url: 'https://interactive-examples.mdn.mozilla.net/media/cc0-videos/bee.webm' },
          { id: 4, url: 'https://interactive-examples.mdn.mozilla.net/media/cc0-videos/river.webm' },
        ],
        '附近': [
          { id: 5, url: 'https://interactive-examples.mdn.mozilla.net/media/cc0-videos/turtle.webm' },
          { id: 6, url: 'https://interactive-examples.mdn.mozilla.net/media/cc0-videos/river.webm' },
        ],
      },
      currentTypeIndex: 0,
      currentVideoIndex: 0,

      touchStartX: 0,
      touchStartY: 0,
      touchEndX: 0,
      touchEndY: 0,

      isHorizontalSwiping: false,
      isVerticalSwiping: false,

      animatingHorizontal: false,
      animatingVertical: false,
    }
  },
  computed: {
    horizontalWrapperStyle() {
      return {
        width: `${this.videoTypes.length * 100}vw`,
        display: 'flex',
        transform: `translateX(${-this.currentTypeIndex * 100}vw)`,
        transition: this.animatingHorizontal ? 'transform 0.3s ease' : 'none',
      }
    },
    verticalWrapperStyle() {
      // Return function to compute vertical translateY for each vertical-wrapper
      return (typeIdx) => {
        if (typeIdx !== this.currentTypeIndex) {
          return {
            height: '100vh',
            overflow: 'hidden',
            position: 'relative',
            transform: 'translateY(0)',
            transition: 'none',
            width: '100vw',
            flexShrink: 0,
          }
        }
        return {
          height: '100vh',
          overflow: 'hidden',
          position: 'relative',
          transform: `translateY(${-this.currentVideoIndex * 100}vh)`,
          transition: this.animatingVertical ? 'transform 0.3s ease' : 'none',
          width: '100vw',
          flexShrink: 0,
        }
      }
    },
    underlineStyle() {
      const navItems = this.$refs.navItems
      if (!navItems || navItems.length === 0) {
        return {}
      }
      const el = navItems[this.currentTypeIndex]
      return {
        width: el.offsetWidth + 'px',
        left: el.offsetLeft + 'px',
        transition: 'left 0.3s ease, width 0.3s ease',
      }
    }
  },
  methods: {
    onVideoPlay(typeIdx, videoIdx) {
      // 可扩展，播放视频事件
    },
    goToType(idx) {
      if (idx === this.currentTypeIndex) return
      this.animatingHorizontal = true
      this.currentTypeIndex = idx
      this.currentVideoIndex = 0
      setTimeout(() => {
        this.animatingHorizontal = false
      }, 300)
    },
    onTouchStart(e) {
      this.touchStartX = e.touches[0].clientX
      this.touchStartY = e.touches[0].clientY
      this.isHorizontalSwiping = false
      this.isVerticalSwiping = false
    },
    onTouchMove(e) {
      this.touchEndX = e.touches[0].clientX
      this.touchEndY = e.touches[0].clientY
    },
    onTouchEnd() {
      const deltaX = this.touchEndX - this.touchStartX
      const deltaY = this.touchEndY - this.touchStartY

      if (Math.abs(deltaX) > Math.abs(deltaY)) {
        // 横向滑动切换频道
        if (deltaX > 50) {
          this.prevType()
        } else if (deltaX < -50) {
          this.nextType()
        }
      } else {
        // 纵向滑动切换视频
        if (deltaY > 50) {
          this.prevVideo()
        } else if (deltaY < -50) {
          this.nextVideo()
        }
      }
    },
    prevType() {
      if (this.currentTypeIndex > 0) {
        this.animatingHorizontal = true
        this.currentTypeIndex--
        this.currentVideoIndex = 0
        setTimeout(() => { this.animatingHorizontal = false }, 300)
      }
    },
    nextType() {
      if (this.currentTypeIndex < this.videoTypes.length - 1) {
        this.animatingHorizontal = true
        this.currentTypeIndex++
        this.currentVideoIndex = 0
        setTimeout(() => { this.animatingHorizontal = false }, 300)
      }
    },
    prevVideo() {
      const videos = this.videosMap[this.videoTypes[this.currentTypeIndex]]
      if (this.currentVideoIndex > 0) {
        this.animatingVertical = true
        this.currentVideoIndex--
        setTimeout(() => { this.animatingVertical = false }, 300)
      }
    },
    nextVideo() {
      const videos = this.videosMap[this.videoTypes[this.currentTypeIndex]]
      if (this.currentVideoIndex < videos.length - 1) {
        this.animatingVertical = true
        this.currentVideoIndex++
        setTimeout(() => { this.animatingVertical = false }, 300)
      }
    },
  },
  mounted() {
    this.$nextTick(() => {
      window.addEventListener('resize', () => this.$forceUpdate())
    })
  }
}
</script>

<style scoped>
.app {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background: black;
  color: white;
  user-select: none;
  -webkit-user-select: none;
  touch-action: none;
  display: flex;
  flex-direction: column;
}
.nav-bar {
  display: flex;
  position: relative;
  height: 44px;
  background-color: #111;
  border-bottom: 1px solid #444;
  user-select: none;
}
.nav-item {
  flex: 1;
  text-align: center;
  line-height: 44px;
  font-weight: 500;
  font-size: 16px;
  color: #aaa;
  cursor: pointer;
  user-select: none;
  transition: color 0.3s ease;
}
.nav-item.active {
  color: #fff;
  font-weight: 700;
}
.nav-underline {
  position: absolute;
  bottom: 0;
  height: 2px;
  background-color: #07c160;
  transition: left 0.3s ease, width 0.3s ease;
}

.horizontal-wrapper {
  flex-grow: 1;
  display: flex;
  will-change: transform;
  touch-action: none;
  position: relative;
}

.vertical-wrapper {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  will-change: transform;
}

.video-item {
  position: absolute;
  width: 100vw;
  height: 100vh;
  object-fit: cover;
  top: 0;
  left: 0;
  opacity: 0;
  transition: opacity 0.3s ease;
  background: black;
  pointer-events: none;
}
.video-item.active {
  opacity: 1;
  pointer-events: auto;
}
</style>



<!-- 


<template>
  <div>
 
    <VideoSwiper 
      :type="currentType" 
      @update:type="onTypeChange"
    />
  </div>
</template>

<script>
import VideoSwiper from './test.vue'

export default {
  components: { VideoSwiper },
  data() {
    return {
      currentType: '热门', // 父组件默认类型
    }
  },
  methods: {
    onTypeChange(newType) {
      this.currentType = newType
    }
  }
}
</script> -->
