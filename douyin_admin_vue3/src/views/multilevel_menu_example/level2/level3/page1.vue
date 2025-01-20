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
    const res = await VideoApi.getAllVideo(1, 10)
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
  <div>
    <!-- Page Title -->
    <FaPageMain>
      多级导航2-2-1
    </FaPageMain>

    <!-- Video List Display -->
    <div v-if="videos.length">
      <div v-for="(video, index) in videos" :key="video.videoid" class="video-item">
        <img :src="video.userAvatar" alt="User Avatar" class="user-avatar">
        <div class="video-info">
          <h4>{{ video.username }}</h4>
          <p>{{ video.video_comment }}</p>
          <video
            :src="video.videoUrl" controls
            x5-video-player-type="h5-page" x5-video-player-fullscreen="false" webkit-playsinline="true"
            x5-playsinline="true" playsinline="true"
            preload="true" loop
          />

          <div class="video-stats">
            <span>Likes: {{ video.likeNum }}</span>
            <span>Comments: {{ video.commentNum }}</span>
            <span>Shares: {{ video.shareNum }}</span>
          </div>
          <button @click="editVideo(index)">
            编辑
          </button>
          <button @click="deleteVideo(index)">
            删除
          </button>
        </div>
      </div>
    </div>
    <div v-else>
      <p>暂无视频数据</p>
    </div>

    <!-- Add Video Form -->
    <div>
      <h3>添加视频</h3>
      <form @submit.prevent="addVideo">
        <input v-model="newVideo.username" placeholder="用户名" required>
        <input v-model="newVideo.video_comment" placeholder="视频描述" required>
        <input v-model="newVideo.videoUrl" placeholder="视频链接" required>
        <input v-model="newVideo.userAvatar" placeholder="用户头像链接" required>
        <button type="submit">
          添加视频
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.video-item {
  border: 1px solid #ddd;
  padding: 16px;
  margin-bottom: 16px;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.video-info {
  margin-left: 10px;
}

.video-stats {
  margin-top: 10px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

input {
  padding: 8px;
  margin: 5px 0;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 10px;
  background-color: #5cb85c;
  border: none;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #4cae4c;
}
</style>
