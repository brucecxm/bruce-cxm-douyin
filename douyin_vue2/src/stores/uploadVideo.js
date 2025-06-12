// src/stores/videoStore.js
import { defineStore } from 'pinia';

export const useVideoStore = defineStore('videoStore', {
  state: () => ({
    videos: {}
  }),
  actions: {
    addVideo(videoId, blob, metadata = {}) {
      const url = URL.createObjectURL(blob);
      this.videos[videoId] = {
        blob,
        url,
        metadata,
        timestamp: new Date().toISOString()
      };
      return videoId;
    },
    getVideo(videoId) {
      return this.videos[videoId];
    },
    removeVideo(videoId) {
      const video = this.videos[videoId];
      if (video) {
        URL.revokeObjectURL(video.url);
        delete this.videos[videoId];
      }
    },
    clearAllVideos() {
      Object.values(this.videos).forEach((video) => {
        URL.revokeObjectURL(video.url);
      });
      this.videos = {};
    }
  }
});
