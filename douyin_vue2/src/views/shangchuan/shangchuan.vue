<!-- FileUpload.vue -->
<template>
    <div class="bruce-main">
        <el-upload
  class="upload-demo"
  action="https://jsonplaceholder.typicode.com/posts/"
  :on-preview="handlePreview"
  :on-remove="handleRemove"
  :before-remove="beforeRemove"
  multiple
  :limit="3"
  :on-exceed="handleExceed"
  :file-list="fileList">
  <el-button size="small" type="primary">点击上传</el-button>
  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
</el-upload>
    </div>
</template>

<script>
import { uploadFile } from "@/api/video"
export default {
    data() {
        return {
            selectedFile: null,
            videoTitle: "",
            videoContext: "",
            fileList: [ ]
        };
    },
    methods: {
        handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
        gohome() {
            this.$router.push("/")
        },
        handleFileUpload(event) {
            this.selectedFile = event.target.files[0];
        },
        upload() {
            const myMap = new Map();
            myMap.set('videoContext', this.videoContext);
            myMap.set('videoTitle', this.videoTitle);
            uploadFile(this.selectedFile, myMap)
        }

    },
};
</script>

<style scoped>
.bruce-main{
    display: flex;
    background-color: rgba(0,0,0,0.1);
}

</style>