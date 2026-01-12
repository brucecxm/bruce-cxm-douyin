<template>
  <div class="ad-banner">
    <img
      src="https://via.placeholder.com/300x60?text=抖音商城App抽红包"
      alt="广告"
    />
    <button class="go-btn" @click="$emit('go')">去抽奖</button>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.ad-banner {
  display: flex;
  align-items: center;
  background: #fff;
  padding: 10px;
  border-radius: 8px;
  margin: 16px 0;
  justify-content: space-between;
}
.go-btn {
  padding: 6px 10px;
  background: #ff5c5c;
  color: white;
  border: none;
  border-radius: 4px;
}
</style>
