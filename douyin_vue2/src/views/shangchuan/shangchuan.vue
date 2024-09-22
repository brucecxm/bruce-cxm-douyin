<!-- FileUpload.vue -->
<template>
    <div>
        <input type="file" @change="handleFileUpload" />
        <button @click="submitFile">上传文件</button>
        <button @click="gohome">gohome</button>
    </div>
</template>
  
<script>
import axios from 'axios';

export default {
    data() {
        return {
            selectedFile: null,
        };
    },
    methods: {
        gohome() {
            this.$router.push("/")
        },
        handleFileUpload(event) {
            this.selectedFile = event.target.files[0];
        },
        async submitFile() {
            if (!this.selectedFile) {
                alert('请选择一个文件');
                return;
            }

            const formData = new FormData();
            formData.append('file', this.selectedFile);

            try {
                const response = await axios.post('localhost:8080/upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                });
                console.log('上传成功', response.data);
            } catch (error) {
                console.error('上传失败', error);
            }
        },
    },
};
</script>
  
<style scoped>
/* 添加你的样式 */
</style>
  