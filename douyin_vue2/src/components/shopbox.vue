<template>
    <div class="video-page">
        <div class="main-video-box">
            <video class="video-player" controls>
                <source src="video-source.mp4" type="video/mp4">
                Your browser does not support the video tag.
            </video>
            <div class="video-info">
                <h2 class="video-title">视频标题</h2>
                <p class="user-info">用户名 • 关注数</p>
            </div>
        </div>
        <div class="video-grid">
            <div class="video-item" v-for="item in 6" :key="item">
                <img class="thumbnail" src="thumbnail.jpg" alt="视频缩略图">
                <div class="video-details">
                    <p class="video-title">相关视频标题</p>
                    <p class="user-info">用户名</p>
                    <p class="likes">点赞数</p>
                </div>
            </div>
        </div>
    </div>
</template>
  
<script>
export default {
    name: 'VideoPage',
    data() {
        return {
            // 这里可以添加需要的数据
        };
    },
    // 其他选项如methods, computed等可以在这里添加
};
</script>
  
<style scoped>
.video-page {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
}

.main-video-box {
    margin-bottom: 20px;
}

.video-player {
    width: 100%;
    max-height: 500px;
    object-fit: cover;
}

.video-info {
    margin-top: 10px;
}

.video-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
}

.video-item {
    border: 1px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
}

.thumbnail {
    width: 100%;
    height: 150px;
    object-fit: cover;
}

.video-details {
    padding: 10px;
}

.video-title {
    font-weight: bold;
    margin-bottom: 5px;
}

.user-info,
.likes {
    font-size: 0.9em;
    color: #666;
}
</style>
  