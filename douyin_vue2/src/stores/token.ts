// stores/token.js
import { defineStore } from "pinia";

export const useTokenStore = defineStore({
  id: "token",
  state: () => ({
    token: "",
  }),
  actions: {
    setToken(newToken: any) {
      this.token = newToken;
    },
  },
  getters: {
    getToken: (state) => state.token,
  },
});

export const VideoIdStore = defineStore({
  id: "VideoId",
  state: () => ({
    videoId: "",
  }),
  actions: {
    setVideoId(newVideoId: any) {
      this.videoId = newVideoId;
    },
  },
  getters: {
    getVideoId: (state) => state.videoId,
  },
});
