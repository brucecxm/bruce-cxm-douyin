// src/stores/userInfo.js
import { defineStore } from 'pinia';
export const useUserInfoStore = defineStore('userInfo', {
  state: () => ({
    userInfo: {} // 初始化为一个空对象
  }),
  actions: {
    setUserInfo(info) {
      this.userInfo = info; // 把 info 存进去
    },
    clearUserInfo() {
      this.userInfo = {}; // 清空
    }
  }
});
