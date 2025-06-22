import { defineStore } from 'pinia';

export const useAdStore = defineStore('ad', {
  state: () => ({
    hasSeenAd: false // 记录是否已看过广告
  }),
  actions: {
    markAdAsSeen() {
      this.hasSeenAd = true;
    }
  }
});
