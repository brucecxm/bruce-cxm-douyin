<template>
  <div class="container">
    <div class="header">
      <div class="settings" @click="gosetting">
        <i class="fas fa-cog"></i>
      </div>
      <div class="scan" @click="fanhui">
        <i class="fas fa-qrcode"></i>
        <span>扫一扫</span>
      </div>
    </div>
    <div class="section">
      <div class="section-title">常用功能</div>
      <div class="item">
        <div class="icon-wrapper" @click="gowallet">
          <div class="icon"><i class="fas fa-wallet"></i></div>
          <span>我的钱包</span>
        </div>
        <div class="icon-wrapper" @click="gocoupon">
          <div class="icon"><i class="fas fa-ticket-alt"></i></div>
          <span>优惠券</span>
        </div>
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-asterisk"></i></div>
          <span>小程序</span>
        </div>
      </div>
      <div class="item">
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-history"></i></div>
          <span>观看历史</span>
        </div>
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-heart"></i></div>
          <span>内容偏好</span>
        </div>
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-cloud-download-alt"></i></div>
          <span>离线模式</span>
        </div>
      </div>
      <div class="item">
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-clock"></i></div>
          <span>稍后再看</span>
        </div>
      </div>
    </div>
    <div class="section">
      <div class="section-title">生活动态</div>
      <div class="item">
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-broadcast-tower"></i></div>
          <span>直播广场</span>
        </div>
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-shopping-basket"></i></div>
          <span>附近团购</span>
        </div>
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-calendar-alt"></i></div>
          <span>活动中心</span>
        </div>
      </div>
      <div class="item">
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-gamepad"></i></div>
          <span>玩游戏</span>
        </div>
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-music"></i></div>
          <span>听抖音</span>
        </div>
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-film"></i></div>
          <span>放映厅</span>
        </div>
      </div>
      <div class="item">
        <div class="icon-wrapper">
          <div class="icon"><i class="fas fa-microphone"></i></div>
          <span>K歌</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { eventBus } from '../main'; // 导入全局事件总线
import '@fortawesome/fontawesome-free/css/all.css';

export default {
  name: 'Tanchuang',
  data() {
    return {
      tanchuang: {
        home: 'home',
        istanchuangshow: false
      }
    };
  },
  methods: {
    fanhui() {
      // 要传一个参数给home主界面
      eventBus.$emit('message-from-one', this.tanchuang); // 发送事件和数据
    },
    gosetting() {
      this.$router.push('/setting');
    },
    gocoupon() {
      this.$router.push('/coupon');
    },
    gowallet() {
      this.$router.push('/wallet');
    }
  }
};
</script>

<style scoped>
.container {
  width: 80vw;
  height: 100vh;
  margin: 0 auto;
  background-color: #f0f0f0;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  border-radius: 10px;
  overflow: hidden;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  padding: 2vw;
  background-color: white;
  border-bottom: 1px solid #ddd;
}

.settings {
  display: flex;
  align-items: center;
}

.scan {
  display: flex;
  align-items: center;
  transform: translateX(-3vw);
}

.settings i,
.scan i {
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.scan span {
  margin-left: 1vw;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.section {
  width: 100%;
  padding: 2vw;
  background-color: white;
  margin-top: 2vw;
  border-radius: 10px;
}

.section-title {
  font-weight: bold;
  margin-bottom: 1vw;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1vw;
}

.icon-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 33.33%;
}

.icon {
  width: 10vw;
  height: 10vw;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.icon-wrapper span {
  margin-top: 1vw;
  text-align: center;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}
</style>
