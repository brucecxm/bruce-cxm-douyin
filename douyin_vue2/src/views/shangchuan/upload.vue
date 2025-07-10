<template>
  <div class="upload-page">
    <el-card shadow="hover" class="upload-card">
      <div class="header">
        <h2>大文件断点续传上传</h2>
      </div>

      <!-- 上传区域 -->
      <div class="upload-area">
        <label class="upload-label">
          <div class="upload-placeholder" :class="{ 'has-file': file }">
            <i class="el-icon-upload"></i>
            <div v-if="!file" class="upload-text">点击上传或拖拽文件到此处</div>
            <div v-else class="file-info">
              <div class="file-name">{{ file.name }}</div>
              <div class="file-size">{{ formatFileSize(file.size) }}</div>
            </div>
            <input
              type="file"
              class="hidden-input"
              @change="handleFileChange"
            />
          </div>
        </label>
      </div>

      <!-- 进度条 -->
      <div v-if="file" class="progress-section">
        <el-progress
          :percentage="progress"
          :status="progress === 100 ? 'success' : 'active'"
        ></el-progress>
        <div class="progress-text">
          {{ uploadedChunks.length }}/{{ totalChunks }} 分片已上传
          <span v-if="uploading">（上传中...）</span>
          <span
            v-else-if="
              uploadedChunks.length > 0 && uploadedChunks.length < totalChunks
            "
          >（已暂停）</span
          >
          <span
            v-else-if="
              uploadedChunks.length === totalChunks && progress === 100
            "
          >（上传完成）</span
          >
        </div>
      </div>

      <!-- 按钮组 -->
      <div class="button-group">
        <el-button
          type="primary"
          icon="el-icon-video-play"
          @click="startUpload"
          :disabled="!file || uploading"
        >开始上传</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-video-pause"
          @click="pauseUpload"
          :disabled="!uploading"
        >暂停上传</el-button
        >
        <el-button
          type="success"
          icon="el-icon-refresh"
          @click="resumeUpload"
          :disabled="
            uploading || !file || uploadedChunks.length === totalChunks
          "
        >恢复上传</el-button
        >
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click="resetUpload"
          v-if="file"
        >重置上传</el-button
        >
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      file: null,
      chunkSize: 6 * 1024 * 1024,
      totalChunks: 0,
      fileHash: '',
      uploadedChunks: [],
      uploading: false,
      cancelTokenSource: null,
      currentChunkIndex: 0,
      progress: 0
    };
  },
  mounted() {
    // 断网恢复自动续传
    window.addEventListener('online', () => {
      if (
        !this.uploading &&
        this.file &&
        this.uploadedChunks.length < this.totalChunks
      ) {
        this.resumeUpload();
      }
    });
  },
  methods: {
    handleFileChange(e) {
      this.file = e.target.files[0];
      this.totalChunks = Math.ceil(this.file.size / this.chunkSize);
      this.fileHash = this.file.name + '_' + this.file.size; // 简易hash
    },

    async startUpload() {
      if (!this.file) return;

      const res = await axios.get('api/upload/check', {
        params: { fileHash: this.fileHash }
      });
      this.uploadedChunks = res.data.uploaded || [];

      this.uploading = true;
      this.currentChunkIndex = 0;
      this.uploadChunks();
    },

    pauseUpload() {
      if (this.cancelTokenSource) {
        this.cancelTokenSource.cancel('用户暂停上传');
      }
      this.uploading = false;
    },

    resumeUpload() {
      this.uploading = true;
      this.uploadChunks();
    },

    async uploadChunks() {
      while (this.currentChunkIndex < this.totalChunks) {
        if (!this.uploading) break;

        if (this.uploadedChunks.includes(this.currentChunkIndex)) {
          this.updateProgress();
          this.currentChunkIndex++;
          continue;
        }

        const start = this.currentChunkIndex * this.chunkSize;
        const end = Math.min(this.file.size, start + this.chunkSize);
        const chunk = this.file.slice(start, end);

        const formData = new FormData();
        formData.append('file', chunk);
        formData.append('index', this.currentChunkIndex);
        formData.append('total', this.totalChunks);
        formData.append('fileName', this.file.name);
        formData.append('fileHash', this.fileHash);

        this.cancelTokenSource = axios.CancelToken.source();

        try {
          await axios.post('api/upload/chunk', formData, {
            cancelToken: this.cancelTokenSource.token,
            onUploadProgress: (progressEvent) => {
              const chunkProgress = progressEvent.loaded / progressEvent.total;
              this.progress = Math.min(
                100,
                Math.round(
                  ((this.currentChunkIndex + chunkProgress) /
                    this.totalChunks) *
                    100
                )
              );
            }
          });
          this.uploadedChunks.push(this.currentChunkIndex);
          this.updateProgress();
          this.currentChunkIndex++;
        } catch (error) {
          if (axios.isCancel(error)) {
            console.log('上传已暂停', error.message);
            return;
          } else {
            console.error('上传失败，重试中...', error);
            return;
          }
        }
      }

      if (this.uploadedChunks.length === this.totalChunks) {
        await axios.post('api/upload/merge', {
          fileName: this.file.name,
          fileHash: this.fileHash,
          total: this.totalChunks
        });
        this.$message.success('上传并合并完成！');
        this.uploading = false;
      }
    },

    updateProgress() {
      this.progress = Math.min(
        100,
        Math.round((this.uploadedChunks.length / this.totalChunks) * 100)
      );
    },

    formatFileSize(size) {
      if (size < 1024) return size + ' B';
      else if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB';
      else if (size < 1024 * 1024 * 1024)
        return (size / (1024 * 1024)).toFixed(2) + ' MB';
      else return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB';
    },

    resetUpload() {
      this.file = null;
      this.chunkSize = 2 * 1024 * 1024;
      this.totalChunks = 0;
      this.fileHash = '';
      this.uploadedChunks = [];
      this.uploading = false;
      this.cancelTokenSource = null;
      this.currentChunkIndex = 0;
      this.progress = 0;
    }
  }
};
</script>

<style scoped>
.upload-page {
  max-width: 500px;
  margin: 40px auto;
}

.upload-card {
  padding: 20px;
  border-radius: 12px;
}

.header {
  text-align: center;
  margin-bottom: 20px;
}

.upload-area {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}

.upload-label {
  width: 100%;
}

.upload-placeholder {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  padding: 40px 20px;
  text-align: center;
  cursor: pointer;
  transition:
    border-color 0.3s,
    background-color 0.3s;
}

.upload-placeholder:hover {
  border-color: #409eff;
  background-color: #f5faff;
}

.upload-placeholder i {
  font-size: 32px;
  color: #c0c4cc;
  margin-bottom: 10px;
}

.upload-placeholder.has-file i {
  color: #67c23a;
}

.upload-text {
  font-size: 14px;
  color: #606266;
}

.file-info {
  font-size: 14px;
  color: #606266;
}

.file-name {
  font-weight: 500;
  word-break: break-all;
}

.file-size {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.hidden-input {
  display: none;
}

.progress-section {
  margin-bottom: 20px;
}

.progress-text {
  font-size: 13px;
  color: #606266;
  margin-top: 5px;
  text-align: center;
}

.button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}
</style>
