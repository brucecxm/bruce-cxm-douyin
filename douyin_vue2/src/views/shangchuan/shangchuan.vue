<!-- FileUpload.vue -->
<template>
    <div>
        <input type="file" @change="handleFileUpload" />
        <input type="text" placeholder="视频标题" v-model="videoTitle" />
        <input type="text" placeholder="视频文案" v-model="videoContext" />
        <button @click="upload">上传文件</button>
        <!-- <button @click="gohome">gohome</button> -->
    </div>
</template>

<script>
import axios from 'axios';
import { uploadFile } from "@/api/video"
export default {
    data() {
        return {
            selectedFile: null,
            videoTitle: "",
            videoContext: ""
        };
    },
    methods: {
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
/* 添加你的样式 */
</style>