<template>
  <el-card class="file-download-card">
    <div class="header">
      <span>大文件分片下载</span>
    </div>

    <el-input
      v-model="fileName"
      placeholder="请输入下载文件名（如 large_file.iso）"
      clearable
      class="input-file-name"
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
      <el-button type="danger" @click="resetDownload" :disabled="isDownloading">
        重置
      </el-button>
    </div>

    <el-progress
      :percentage="progress"
      status="active"
      :text-inside="true"
      :stroke-width="18"
      style="margin-top: 20px"
    ></el-progress>

    <div class="status-text">{{ status }}</div>
  </el-card>
</template>

<script>
import axios from 'axios';

export default {
  name: 'FileDownload',
  data() {
    return {
      fileName: '',
      fileUrl: '/api/down/files',
      fileInfoUrl: '/api/down/files',

      totalSize: 0,
      totalChunks: 0,
      chunkSize: 50 * 1024 * 1024,

      downloadedChunks: new Set(),
      progress: 0,
      isDownloading: false,
      status: '等待下载',

      cancelTokens: new Map(),
      chunksData: []
    };
  },
  methods: {
    async initDownload() {
      this.status = '获取文件信息...';
      try {
        const res = await axios.get(
          `${this.fileInfoUrl}/${this.fileName}/info`
        );
        if (!res.data.exists) {
          this.status = '文件不存在';
          return false;
        }
        this.totalSize = res.data.fileSize;
        this.totalChunks = Math.ceil(this.totalSize / this.chunkSize);
        this.restoreDownloadedChunks();
        return true;
      } catch (e) {
        this.status = '获取文件信息失败';
        return false;
      }
    },
    restoreDownloadedChunks() {
      const saved = localStorage.getItem(`downloaded_${this.fileName}`);
      if (saved) {
        const chunks = JSON.parse(saved);
        this.downloadedChunks = new Set(chunks);
      }
    },
    saveDownloadedChunks() {
      localStorage.setItem(
        `downloaded_${this.fileName}`,
        JSON.stringify([...this.downloadedChunks])
      );
    },
    async startDownload() {
      if (!this.fileName) {
        this.$message.warning('请输入文件名');
        return;
      }
      if (!(await this.initDownload())) return;
      this.isDownloading = true;
      this.status = '下载中...';
      this.chunksData = [];

      const concurrency = 3;
      let currentChunk = 0;
      let active = 0;

      const downloadNext = async () => {
        if (currentChunk >= this.totalChunks) {
          if (this.downloadedChunks.size === this.totalChunks) {
            this.finishDownload();
          }
          return;
        }
        if (this.downloadedChunks.has(currentChunk)) {
          currentChunk++;
          downloadNext();
          return;
        }
        const chunkIndex = currentChunk++;
        active++;
        this.downloadChunk(chunkIndex)
          .then(() => {
            active--;
            if (active < concurrency && this.isDownloading) downloadNext();
          })
          .catch(() => {
            active--;
            if (this.isDownloading) downloadNext();
          });
        if (active < concurrency && this.isDownloading) downloadNext();
      };

      for (let i = 0; i < Math.min(concurrency, this.totalChunks); i++) {
        downloadNext();
      }
    },
    async downloadChunk(chunkIndex) {
      const start = chunkIndex * this.chunkSize;
      const end = Math.min(start + this.chunkSize - 1, this.totalSize - 1);
      const source = axios.CancelToken.source();
      this.cancelTokens.set(chunkIndex, source);

      try {
        const res = await axios.get(`${this.fileUrl}/${this.fileName}`, {
          headers: { Range: `bytes=${start}-${end}` },
          responseType: 'blob',
          cancelToken: source.token
        });
        this.downloadedChunks.add(chunkIndex);
        this.saveDownloadedChunks();
        this.chunksData[chunkIndex] = res.data;
        this.updateProgress();
      } catch (e) {
        if (!axios.isCancel(e)) console.error(e);
      } finally {
        this.cancelTokens.delete(chunkIndex);
      }
    },
    updateProgress() {
      this.progress = Math.round(
        (this.downloadedChunks.size / this.totalChunks) * 100
      );
    },
    async finishDownload() {
      this.isDownloading = false;
      this.status = '合并文件并下载...';
      const blob = new Blob(this.chunksData);
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = this.fileName;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
      this.status = '下载完成';
      this.progress = 100;
    },
    pauseDownload() {
      this.isDownloading = false;
      this.status = '已暂停';
      this.cancelTokens.forEach((source) => source.cancel('暂停下载'));
      this.cancelTokens.clear();
    },
    resetDownload() {
      this.pauseDownload();
      this.downloadedChunks.clear();
      localStorage.removeItem(`downloaded_${this.fileName}`);
      this.progress = 0;
      this.status = '已重置';
      this.chunksData = [];
    }
  }
};
</script>

<style scoped>
.file-download-card {
  max-width: 400px;
  margin: 30px auto;
}
.header {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
}
.input-file-name {
  width: 100%;
  margin-bottom: 15px;
}
.button-group {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}
.status-text {
  text-align: center;
  color: #555;
  margin-top: 10px;
}
</style>
