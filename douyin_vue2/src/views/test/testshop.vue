<template>
    <div>
        <input type="file" @change="handleFileChange" />
        <button @click="uploadFile">上传</button>
    </div>
</template>
<script>
import axios from "axios";
import SparkMD5 from "spark-md5";

axios.defaults.baseURL = "http://127.0.0.1:9573/api";

export default {
    data() {
        return {
            file: null,
            chunkSize: 2 * 1024 * 1024,
            hash: "",
        };
    },
    methods: {
        async calculateHash(file) {
            return new Promise((resolve) => {
                const spark = new SparkMD5.ArrayBuffer();
                const reader = new FileReader();
                reader.readAsArrayBuffer(file);
                reader.onload = (e) => {
                    spark.append(e.target.result);
                    resolve(spark.end());
                };
            });
        },

        async handleFileChange(e) {
            // ✅ 修正为获取 File 对象
            this.file = e.target.files;
            this.hash = await this.calculateHash(this.file);
        },

        async uploadFile() {
            if (!this.file) {
                alert("请先选择文件！");
                return;
            }

            const chunks = Math.ceil(this.file.size / this.chunkSize);
            const { data } = await axios.get(`/upload/check?hash=${this.hash}`);
            const uploadedChunks = data.uploaded || [];

            for (let i = 0; i < chunks; i++) {
                if (uploadedChunks.includes(i)) continue;

                const formData = new FormData();
                const chunk = this.file.slice(
                    i * this.chunkSize,
                    (i + 1) * this.chunkSize
                );
                formData.append("file", chunk);
                formData.append("hash", this.hash);
                formData.append("index", i);

                await axios.post("/upload/chunk", formData, {
                    headers: { "Content-Type": "multipart/form-data" },
                });
            }

            await axios.post("/upload/merge", {
                hash: this.hash,
                filename: this.file.name,
            });
        },
    },
};
</script>
