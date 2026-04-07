<template>
  <div class="file-transfer-page">
    <!-- 上传模块 -->
    <el-card shadow="hover" class="transfer-card">
      <div class="header">
        <h2>📤 大文件断点续传 - 上传</h2>
      </div>

      <div class="upload-area">
        <label class="upload-label">
          <div class="upload-placeholder" :class="{ 'has-file': uploadFile }">
            <i class="el-icon-upload"></i>
            <div v-if="!uploadFile" class="upload-text">
              点击上传或拖拽文件到此处
            </div>
            <div v-else class="file-info">
              <div class="file-name">{{ uploadFile.name }}</div>
              <div class="file-size">{{ formatFileSize(uploadFile.size) }}</div>
            </div>
            <input
              type="file"
              class="hidden-input"
              @change="handleFileChange"
            />
          </div>
        </label>
      </div>

      <div v-if="uploadFile" class="progress-section">
        <el-progress
          :percentage="uploadProgress"
          :status="uploadProgress === 100 ? 'success' : 'active'"
        ></el-progress>
        <div class="progress-text">
          {{ uploadedChunks.length }}/{{ totalUploadChunks }} 分片已上传
          <span v-if="uploading">（上传中...）</span>
          <span
            v-else-if="
              uploadedChunks.length > 0 &&
                uploadedChunks.length < totalUploadChunks
            "
          >（已暂停）</span
          >
          <span
            v-else-if="
              uploadedChunks.length === totalUploadChunks &&
                uploadProgress === 100
            "
          >（上传完成）</span
          >
        </div>
      </div>

      <div class="button-group">
        <el-button
          type="primary"
          icon="el-icon-video-play"
          @click="startUpload"
          :disabled="!uploadFile || uploading"
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
            uploading ||
              !uploadFile ||
              uploadedChunks.length === totalUploadChunks
          "
        >恢复上传</el-button
        >
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click="resetUpload"
          v-if="uploadFile"
        >重置上传</el-button
        >
      </div>
    </el-card>

    <!-- 下载模块 -->
    <el-card shadow="hover" class="transfer-card">
      <div class="header">
        <h2>📥 大文件断点续传 - 下载</h2>
      </div>

      <el-input
        v-model="downloadFileName"
        placeholder="请输入下载文件名（与上传一致）"
        clearable
        class="input-file"
      ></el-input>

      <div class="button-group">
        <el-button
          type="primary"
          @click="startDownload"
          :disabled="isDownloading"
        >
          开始下载
        </el-button>
        <el-button
          type="warning"
          @click="pauseDownload"
          :disabled="!isDownloading"
        >
          暂停下载
        </el-button>
        <el-button
          type="danger"
          @click="resetDownload"
          :disabled="isDownloading"
        >
          重置下载
        </el-button>
      </div>

      <el-progress
        :percentage="downloadProgress"
        status="active"
        :text-inside="true"
        :stroke-width="18"
        style="margin-top: 20px"
      ></el-progress>
      <div class="status-text">{{ downloadStatus }}</div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'FileTransfer',
  data() {
    return {
      // ========== 上传相关 ==========
      uploadFile: null,
      uploadChunkSize: 6 * 1024 * 1024,
      totalUploadChunks: 0,
      fileHash: '',
      uploadedChunks: [],
      uploading: false,
      uploadCancelToken: null,
      currentUploadChunk: 0,
      uploadProgress: 0,

      // ========== 下载相关 ==========
      downloadFileName: '',
      downloadChunkSize: 50 * 1024 * 1024,
      totalFileSize: 0,
      totalDownloadChunks: 0,
      downloadedChunks: new Set(),
      downloadProgress: 0,
      isDownloading: false,
      downloadStatus: '等待下载',
      downloadCancelTokens: new Map(),
      chunksBuffer: []
    };
  },
  mounted() {
    // 断网自动续传
    window.addEventListener('online', () => {
      if (
        !this.uploading &&
        this.uploadFile &&
        this.uploadedChunks.length < this.totalUploadChunks
      ) {
        this.resumeUpload();
      }
    });
  },
  methods: {
    // ========================== 上传方法 ==========================
    handleFileChange(e) {
      this.uploadFile = e.target.files[0];
      this.totalUploadChunks = Math.ceil(
        this.uploadFile.size / this.uploadChunkSize
      );
      this.fileHash = this.uploadFile.name + '_' + this.uploadFile.size;
      this.downloadFileName = this.uploadFile.name; // 自动填充下载文件名
    },

    async startUpload() {
      if (!this.uploadFile) return;
      const { data } = await axios.get('/api/video/upload/check', {
        params: { fileHash: this.fileHash }
      });
      this.uploadedChunks = data.uploaded || [];
      this.uploading = true;
      this.currentUploadChunk = 0;
      this.uploadChunks();
    },

    pauseUpload() {
      if (this.uploadCancelToken) this.uploadCancelToken.cancel('用户暂停');
      this.uploading = false;
    },

    resumeUpload() {
      this.uploading = true;
      this.uploadChunks();
    },

    async uploadChunks() {
      while (this.currentUploadChunk < this.totalUploadChunks) {
        if (!this.uploading) break;
        if (this.uploadedChunks.includes(this.currentUploadChunk)) {
          this.updateUploadProgress();
          this.currentUploadChunk++;
          continue;
        }

        const start = this.currentUploadChunk * this.uploadChunkSize;
        const end = Math.min(
          this.uploadFile.size,
          start + this.uploadChunkSize
        );
        const chunk = this.uploadFile.slice(start, end);

        const form = new FormData();
        form.append('file', chunk);
        form.append('index', this.currentUploadChunk);
        form.append('total', this.totalUploadChunks);
        form.append('fileName', this.uploadFile.name);
        form.append('fileHash', this.fileHash);

        this.uploadCancelToken = axios.CancelToken.source();

        try {
          await axios.post('/api/video/upload/chunk', form, {
            cancelToken: this.uploadCancelToken.token
          });
          this.uploadedChunks.push(this.currentUploadChunk);
          this.updateUploadProgress();
          this.currentUploadChunk++;
        } catch (err) {
          if (!axios.isCancel(err)) console.error('分片上传失败', err);
          return;
        }
      }

      if (this.uploadedChunks.length === this.totalUploadChunks) {
        await axios.post('/api/video/upload/merge', {
          fileName: this.uploadFile.name,
          fileHash: this.fileHash,
          total: this.totalUploadChunks
        });
        this.$message.success('✅ 上传并合并完成');
        this.uploading = false;
      }
    },

    updateUploadProgress() {
      this.uploadProgress = Math.min(
        100,
        Math.round((this.uploadedChunks.length / this.totalUploadChunks) * 100)
      );
    },

    resetUpload() {
      this.uploadFile = null;
      this.totalUploadChunks = 0;
      this.fileHash = '';
      this.uploadedChunks = [];
      this.uploading = false;
      this.uploadCancelToken = null;
      this.currentUploadChunk = 0;
      this.uploadProgress = 0;
    },

    formatFileSize(size) {
      if (size < 1024) return size + ' B';
      if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB';
      if (size < 1024 * 1024 * 1024)
        return (size / (1024 * 1024)).toFixed(2) + ' MB';
      return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB';
    },

    // ========================== 下载方法 ==========================
    async startDownload() {
      if (!this.downloadFileName) return this.$message.warning('请输入文件名');
      this.downloadStatus = '获取文件信息...';
      try {
        const { data } = await axios.get(
          `/api/down/files/${this.downloadFileName}/info`
        );
        if (!data.exists) {
          this.downloadStatus = '文件不存在';
          return;
        }
        this.totalFileSize = data.fileSize;
        this.totalDownloadChunks = Math.ceil(
          this.totalFileSize / this.downloadChunkSize
        );
        this.restoreDownloadProgress();
      } catch (e) {
        this.downloadStatus = '获取信息失败';
        return;
      }

      this.isDownloading = true;
      this.downloadStatus = '下载中...';
      this.chunksBuffer = [];
      this.downloadWithConcurrency();
    },

    restoreDownloadProgress() {
      const key = `download_${this.downloadFileName}`;
      const saved = localStorage.getItem(key);
      if (saved) this.downloadedChunks = new Set(JSON.parse(saved));
    },

    saveDownloadProgress() {
      const key = `download_${this.downloadFileName}`;
      localStorage.setItem(key, JSON.stringify([...this.downloadedChunks]));
    },

    async downloadWithConcurrency() {
      const concurrency = 3;
      let current = 0,
        active = 0;
      const next = async () => {
        if (current >= this.totalDownloadChunks || !this.isDownloading) {
          if (this.downloadedChunks.size === this.totalDownloadChunks)
            this.mergeFile();
          return;
        }
        if (this.downloadedChunks.has(current)) {
          current++;
          next();
          return;
        }
        const idx = current++;
        active++;
        await this.downloadChunk(idx);
        active--;
        next();
      };
      for (let i = 0; i < concurrency; i++) next();
    },

    async downloadChunk(index) {
      const start = index * this.downloadChunkSize;
      const end = Math.min(
        start + this.downloadChunkSize - 1,
        this.totalFileSize - 1
      );
      const source = axios.CancelToken.source();
      this.downloadCancelTokens.set(index, source);
      try {
        const res = await axios.get(
          `/api/down/files/${this.downloadFileName}`,
          {
            headers: { Range: `bytes=${start}-${end}` },
            responseType: 'blob',
            cancelToken: source.token
          }
        );
        this.downloadedChunks.add(index);
        this.chunksBuffer[index] = res.data;
        this.saveDownloadProgress();
        this.downloadProgress = Math.round(
          (this.downloadedChunks.size / this.totalDownloadChunks) * 100
        );
      } catch (e) {}
      this.downloadCancelTokens.delete(index);
    },

    async mergeFile() {
      this.isDownloading = false;
      this.downloadStatus = '合并文件...';
      const blob = new Blob(this.chunksBuffer);
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = this.downloadFileName;
      a.click();
      URL.revokeObjectURL(url);
      this.downloadStatus = '✅ 下载完成';
      this.downloadProgress = 100;
    },

    pauseDownload() {
      this.isDownloading = false;
      this.downloadStatus = '已暂停';
      this.downloadCancelTokens.forEach((s) => s.cancel());
      this.downloadCancelTokens.clear();
    },

    resetDownload() {
      this.pauseDownload();
      this.downloadedChunks.clear();
      localStorage.removeItem(`download_${this.downloadFileName}`);
      this.downloadProgress = 0;
      this.downloadStatus = '已重置';
      this.chunksBuffer = [];
    }
  }
};
</script>

<style scoped>
.file-transfer-page {
  max-width: 600px;
  margin: 40px auto;
  padding: 0 20px;
}
.transfer-card {
  margin-bottom: 30px;
  padding: 24px;
  border-radius: 12px;
}
.header {
  text-align: center;
  margin-bottom: 20px;
}
.upload-area {
  display: flex;
  justify-content: center;
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
  transition: all 0.3s;
}
.upload-placeholder:hover {
  border-color: #409eff;
  background: #f5faff;
}
.upload-placeholder i {
  font-size: 32px;
  color: #c0c4cc;
  margin-bottom: 10px;
}
.upload-placeholder.has-file i {
  color: #67c23a;
}
.upload-text,
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
  text-align: center;
  margin-top: 6px;
}
.button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}
.input-file {
  margin-bottom: 18px;
}
.status-text {
  text-align: center;
  color: #555;
  margin-top: 12px;
  font-size: 14px;
}
</style>
