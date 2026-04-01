<template>
  <div class="not-implemented-popup" @click="close">
    <div class="popup-content" @click.stop>
      <span>功能还未实现</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotImplementedPopup',
  methods: {
    close() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>
.not-implemented-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.popup-content {
  background-color: rgba(255, 255, 255, 0.9);
  padding: 20px 30px;
  border-radius: 8px;
  font-size: 16px;
  color: #666;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>