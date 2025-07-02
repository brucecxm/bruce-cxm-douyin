<template>
  <div class="ad-container" :class="{ show: isShow }">
    <!-- 背景图片区域 -->
    <div class="ad-background"></div>

    <!-- 倒计时 -->
    <div class="countdown" v-if="showCountdown">{{ countdown }}s 后进入</div>

    <!-- 跳过按钮 -->
    <button class="skip-btn" @click="skipAd">跳过</button>
  </div>
</template>

<script>
export default {
  name: 'Ad',
  data() {
    return {
      isShow: false,
      countdown: 5,
      showCountdown: true,
      timer: null
    };
  },
  mounted() {
    this.isShow = true;
    this.startCountdown();
  },
  methods: {
    startCountdown() {
      this.timer = setInterval(() => {
        this.countdown--;
        if (this.countdown <= 0) {
          this.hideAd();
        }
      }, 1000);
    },
    hideAd() {
      clearInterval(this.timer);
      this.isShow = false;

      this.$store.dispatch('ad/markAdAsSeen');

      // 跳转首页
      setTimeout(() => {
        this.$router.push('/');
      }, 300);
    },
    skipAd() {
      this.hideAd();
    }
  },
  beforeDestroy() {
    clearInterval(this.timer);
  }
};
</script>

<style scoped>
.ad-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #000;
  transition: opacity 0.3s ease-in-out;
  opacity: 1;
}

.ad-container.hide {
  opacity: 0;
  pointer-events: none;
}

.ad-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-image: url(../assets/ad.png); /* 替换成你的广告图路径 */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  z-index: -1;
}

/* 倒计时样式 */
.countdown {
  position: absolute;
  top: 30px;
  right: 30px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 14px;
  z-index: 10;
}

/* 跳过按钮 */
.skip-btn {
  position: absolute;
  top: 30px;
  right: 120px;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  border: 1px solid #fff;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  z-index: 11;
  transition: all 0.3s ease;
}

.skip-btn:hover {
  background: rgba(255, 255, 255, 0.4);
  color: #000;
  border-color: #fff;
}
</style>
