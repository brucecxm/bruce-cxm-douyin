// 引入 Pinia 中的 defineStore 方法，用于定义一个状态管理 Store
import { defineStore } from "pinia";

// 使用 defineStore 定义一个名为 videoInfo 的 Store
export const videoInfoStore = defineStore({
  // 设置该 store 的唯一 ID（名称），在调试和开发工具中用于标识
  id: "videoInfo",

  // state 定义该 store 中存储的数据（状态）
  state: () => ({
    // videoInfo 用于存储视频信息，这里默认从 localStorage 中读取（持久化）
    // 如果本地没有存储，则默认设置为空字符串
    videoInfo: localStorage.getItem("videoInfo") || "",
  }),

  // actions 中定义操作 state 的方法，可以包含异步操作（类似 Vuex 中的 actions + mutations）
  actions: {
    // 设置 videoInfo 的值，同时更新 localStorage（实现持久化）
    setvideoInfo(newToken) {
      this.videoInfo = newToken; // 更新 store 中的状态
      localStorage.setItem("videoInfo", newToken); // 保存到本地存储
    },

    // 清空 videoInfo 的值，同时移除本地存储
    clearvideoInfo() {
      this.videoInfo = ""; // 清空状态
      localStorage.removeItem("videoInfo"); // 删除本地存储
    },
  },

  // getters 是计算属性，用于从 state 派生出新的数据（只读）
  getters: {
    // 获取当前的 videoInfo 状态
    getvideoInfo(state) {
      return state.videoInfo;
    },
  },
});
