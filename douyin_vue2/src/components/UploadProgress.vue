<template>
  <div v-if="visible" class="upload-progress-overlay">
    <div class="upload-progress-container">
      <div class="upload-progress-header">
        <h3>{{ title }}</h3>
        <button class="close-btn" @click="$emit('close')">&times;</button>
      </div>
      <div class="upload-progress-body">
        <div class="progress-bar-container">
          <div class="progress-bar" :style="{ width: progress + '%' }"></div>
        </div>
        <div class="progress-text">{{ progressText }}</div>
        <div class="upload-info" v-if="fileName">{{ fileName }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UploadProgress',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    progress: {
      type: Number,
      default: 0
    },
    title: {
      type: String,
      default: '上传中'
    },
    fileName: {
      type: String,
      default: ''
    },
    progressText: {
      type: String,
      default: ''
    }
  },
  emits: ['close']
}
</script>

<style scoped>
.upload-progress-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.upload-progress-container {
  background-color: white;
  border-radius: 12px;
  width: 90%;
  max-width: 400px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.upload-progress-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #f9f9f9;
}

.upload-progress-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.close-btn:hover {
  background-color: #f0f0f0;
  color: #333;
}

.upload-progress-body {
  padding: 20px;
}

.progress-bar-container {
  width: 100%;
  height: 8px;
  background-color: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 16px;
}

.progress-bar {
  height: 100%;
  background-color: #ff2c55;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  text-align: center;
  margin-bottom: 8px;
}

.upload-info {
  font-size: 14px;
  color: #666;
  text-align: center;
  word-break: break-all;
}
</style>