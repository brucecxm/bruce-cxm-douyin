<template>
  <div class="resume-upload">
    <el-card title="简历上传与管理">
      <el-form :model="uploadForm" ref="uploadForm" label-width="120px">
        <el-form-item label="简历文件">
          <el-upload
            class="upload-demo"
            action=""
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
            :limit="1"
            accept=".docx,.pdf"
          >
            <el-button
slot="trigger"
size="small" type="primary"
            >选取文件</el-button
            >
            <div slot="tip" class="el-upload__tip">
              只能上传docx或pdf文件，单个文件大小不超过50MB
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpload">上传简历</el-button>
          <el-button type="info" @click="handleUpdate">更新简历</el-button>
          <el-button
type="warning"
@click="handleDownload"
          >下载当前简历</el-button
          >
        </el-form-item>
      </el-form>

      <!-- 简历信息展示 -->
      <el-card v-if="resumeInfo" class="mt-20">
        <template #header>
          <div class="card-header">
            <span>当前简历信息</span>
          </div>
        </template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="文件名">{{
            resumeInfo.fileName
          }}</el-descriptions-item>
          <el-descriptions-item label="文件大小">{{
            formatFileSize(resumeInfo.fileSize)
          }}</el-descriptions-item>
          <el-descriptions-item label="上传时间">{{
            resumeInfo.uploadTime
          }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ResumeUpload',
  data() {
    return {
      uploadForm: {
        file: null
      },
      fileList: [],
      resumeInfo: null
    };
  },
  mounted() {
    // 初始化时获取当前简历信息
    this.getResumeInfo();
  },
  methods: {
    // 处理文件选择
    handleFileChange(file, fileList) {
      this.fileList = fileList;
      this.uploadForm.file = file.raw;
    },

    // 上传简历
    handleUpload() {
      if (!this.uploadForm.file) {
        this.$message.error('请选择要上传的文件');
        return;
      }

      this.uploadResume();
    },

    // 更新简历
    handleUpdate() {
      if (!this.uploadForm.file) {
        this.$message.error('请选择要更新的文件');
        return;
      }

      this.uploadResume();
    },

    // 上传简历文件到服务器
    uploadResume() {
      const formData = new FormData();
      formData.append('file', this.uploadForm.file);

      // 这里需要替换为实际的上传接口
      this.$http
        .post('/api/resume/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then((response) => {
          if (response.data.success) {
            this.$message.success('简历上传成功');
            this.getResumeInfo();
          } else {
            this.$message.error('简历上传失败：' + response.data.message);
          }
        })
        .catch((error) => {
          console.error('上传失败:', error);
          this.$message.error('简历上传失败，请重试');
        });
    },

    // 获取当前简历信息
    getResumeInfo() {
      // 这里需要替换为实际的获取简历信息接口
      this.$http
        .get('/api/resume/info')
        .then((response) => {
          if (response.data.success) {
            this.resumeInfo = response.data.data;
          }
        })
        .catch((error) => {
          console.error('获取简历信息失败:', error);
        });
    },

    // 下载当前简历
    handleDownload() {
      window.open('/down/files/陈雪明的简历.docx', '_blank');
    },

    // 格式化文件大小
    formatFileSize(size) {
      if (size < 1024) {
        return size + ' B';
      } else if (size < 1024 * 1024) {
        return (size / 1024).toFixed(2) + ' KB';
      } else if (size < 1024 * 1024 * 1024) {
        return (size / (1024 * 1024)).toFixed(2) + ' MB';
      } else {
        return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB';
      }
    }
  }
};
</script>

<style scoped>
.resume-upload {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
