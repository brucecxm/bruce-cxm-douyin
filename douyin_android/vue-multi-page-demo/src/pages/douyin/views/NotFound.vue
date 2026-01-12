<template>
  <div class="not-found-container">
    <div class="error-code" :class="{ animate: isMounted }">404</div>
    <div class="error-message" :class="{ animate: isMounted }">
      <h2>页面找不到</h2>
      <p>您请求的页面不存在或已被移除</p>
    </div>
    <button @click="goToHome" class="back-home-btn">返回首页</button>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
  data() {
    return {
      isMounted: false
    };
  },
  mounted() {
    this.isMounted = true;
  },
  methods: {
    goToHome() {
      this.$router.push('/');
    }
  }
};
</script>

<style scoped>
.not-found-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 0 20px;
  background: #f5f5f5;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  color: #ff4d4f;
  margin: 0;
  transition: all 0.8s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  opacity: 0;
  transform: translateY(-20px);
}

.error-code.animate {
  opacity: 1;
  transform: translateY(0);
}

.error-message {
  margin: 20px 0;
  transition: all 0.8s cubic-bezier(0.175, 0.885, 0.32, 1.275) 0.2s;
  opacity: 0;
  transform: translateY(20px);
}

.error-message.animate {
  opacity: 1;
  transform: translateY(0);
}

.error-message h2 {
  font-size: 32px;
  color: #333;
  margin-bottom: 10px;
}

.error-message p {
  font-size: 16px;
  color: #666;
  max-width: 400px;
  margin: 0 auto;
}

.back-home-btn {
  background: #409eff;
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 30px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.back-home-btn:hover {
  background: #66b1ff;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}
</style>
