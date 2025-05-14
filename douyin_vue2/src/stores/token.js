// stores/token.js
import { defineStore } from "pinia";

export const useTokenStore = defineStore({
  id: "token",
  state: () => ({
    token: localStorage.getItem("token") || "", // 从 localStorage 加载 token
  }),
  actions: {
    setToken(newToken) {
      this.token = newToken;
      localStorage.setItem("token", newToken); // 同步到 localStorage
    },
    clearToken() {
      this.token = "";
      localStorage.removeItem("token"); // 清除 localStorage 中的 token
    },
  },
  getters: {
    getToken(state) {
      return state.token;
    },
  },
});
//
// export const VideoIdStore = defineStore({
//   id: "VideoId",
//   state: () => ({
//     videoId: "",
//   }),
//   actions: {
//     setVideoId(newVideoId) {
//       this.videoId = newVideoId;
//     },
//   },
//   getters: {
//     getVideoId(state) {
//       return state.videoId;
//     },
//   },
// });
