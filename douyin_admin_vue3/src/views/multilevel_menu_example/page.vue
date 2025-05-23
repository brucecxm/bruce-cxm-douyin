<route lang="yaml">
  meta:
    title: 视频管理
</route>

<script setup>
import VideoApi from '@/api/video/video' // Import API request methods
import { onMounted, ref } from 'vue'

// Video list state
const videos = ref([])

// New video data state
const newVideo = ref({
  username: '',
  video_comment: '',
  videoUrl: '',
  userAvatar: '',
  likeNum: 0,
  commentNum: 0,
  shareNum: 0,
  videoid: Date.now(),
})

// Fetch videos on mounted
onMounted(async () => {
  try {
    const res = await VideoApi.getAllVideo(1, 10, 'w')
    videos.value = res || [] // Assuming response data contains an array of videos
  }
  catch (error) {
    console.error('请求失败:', error)
  }
})

// Add video
function addVideo() {
  const videoToAdd = { ...newVideo.value, videoid: Date.now() }
  videos.value.push(videoToAdd)
  newVideo.value = { username: '', video_comment: '', videoUrl: '', userAvatar: '', likeNum: 0, commentNum: 0, shareNum: 0, videoid: Date.now() }
  alert('视频已添加！')
}

// Edit video
function editVideo(index) {
  const video = videos.value[index]
  // Populate form with current video data
  newVideo.value = { ...video }
}

// Delete video
function deleteVideo(index) {
  const confirmed = confirm('确认删除该视频吗？')
  if (confirmed) {
    videos.value.splice(index, 1)
    alert('视频已删除！')
  }
}
</script>

<template>
  <div class="page-container">
    <!-- Page Title -->
    <FaPageMain>
      视频管理
    </FaPageMain>

    <!-- Video List Display -->
    <section v-if="videos.length" class="video-list">
      <div v-for="(video, index) in videos" :key="video.videoid" class="video-item">
        <img :src="video.userAvatar" alt="用户头像" class="user-avatar">
        <div class="video-content">
          <h4 class="username">
            {{ video.username }}
          </h4>
          <p class="video-comment">
            {{ video.video_comment }}
          </p>
          <video
            :src="video.videoUrl"
            controls
            preload="metadata"
            class="video-player"
            playsinline
            webkit-playsinline
            x5-playsinline
            x5-video-player-type="h5-page"
            x5-video-player-fullscreen="false"
            loop
          />

          <div class="video-stats">
            <span><i class="icon-like" /> {{ video.likeNum }}</span>
            <span><i class="icon-comment" /> {{ video.commentNum }}</span>
            <span><i class="icon-share" /> {{ video.shareNum }}</span>
          </div>
          <div class="video-actions">
            <button class="btn-edit" @click="editVideo(index)">
              编辑
            </button>
            <button class="btn-delete" @click="deleteVideo(index)">
              删除
            </button>
          </div>
        </div>
      </div>
    </section>
    <section v-else class="empty-state">
      <p>暂无视频数据</p>
    </section>

    <!-- Add Video Form -->
    <section class="add-video-section">
      <h3>添加视频</h3>
      <form class="add-video-form" @submit.prevent="addVideo">
        <input v-model="newVideo.username" placeholder="用户名" required>
        <input v-model="newVideo.video_comment" placeholder="视频描述" required>
        <input v-model="newVideo.videoUrl" placeholder="视频链接" required>
        <input v-model="newVideo.userAvatar" placeholder="用户头像链接" required>
        <button type="submit" class="btn-submit">
          添加视频
        </button>
      </form>
    </section>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 900px;
  margin: 20px auto;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
  padding: 0 16px;
}

/* 视频列表 */
.video-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 40px;
}

.video-item {
  display: flex;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 16px;
  background: #fff;
  box-shadow: 0 2px 6px rgb(0 0 0 / 0.05);
  transition: box-shadow 0.3s ease;
}

.video-item:hover {
  box-shadow: 0 4px 12px rgb(0 0 0 / 0.15);
}

.user-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 20px;
  flex-shrink: 0;
  border: 2px solid #4caf50;
}

.video-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: 700;
  font-size: 1.2rem;
  margin: 0 0 8px 0;
  color: #2c3e50;
}

.video-comment {
  font-size: 0.95rem;
  color: #555;
  margin-bottom: 10px;
}

.video-player {
  width: 100%;
  max-height: 300px;
  border-radius: 8px;
  outline: none;
  background: #000;
  margin-bottom: 12px;
}

/* 视频统计 */
.video-stats {
  display: flex;
  gap: 24px;
  font-size: 0.9rem;
  color: #777;
  margin-bottom: 12px;
}

.video-stats span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.icon-like::before {
  content: "👍";
}

.icon-comment::before {
  content: "💬";
}

.icon-share::before {
  content: "🔗";
}

/* 操作按钮 */
.video-actions {
  display: flex;
  gap: 12px;
}

.btn-edit,
.btn-delete {
  cursor: pointer;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
}

.btn-edit {
  background-color: #2196f3;
  color: white;
}

.btn-edit:hover {
  background-color: #1976d2;
}

.btn-delete {
  background-color: #f44336;
  color: white;
}

.btn-delete:hover {
  background-color: #c62828;
}

/* 空状态 */
.empty-state {
  text-align: center;
  font-size: 1.1rem;
  color: #999;
  margin: 40px 0;
}

/* 添加视频表单 */
.add-video-section {
  background: #f9f9f9;
  padding: 20px 24px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgb(0 0 0 / 0.05);
}

.add-video-section h3 {
  margin-bottom: 16px;
  color: #4caf50;
  font-weight: 700;
}

.add-video-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.add-video-form input {
  padding: 10px 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.add-video-form input:focus {
  border-color: #4caf50;
  outline: none;
  box-shadow: 0 0 6px #4caf50aa;
}

.btn-submit {
  background-color: #4caf50;
  color: white;
  font-weight: 700;
  border: none;
  padding: 12px 0;
  border-radius: 30px;
  cursor: pointer;
  font-size: 1.1rem;
  transition: background-color 0.3s ease;
}

.btn-submit:hover {
  background-color: #388e3c;
}
</style>
