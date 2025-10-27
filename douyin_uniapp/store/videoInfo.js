// store/modules/videoInfo.js
import { getStorage, setStorage, removeStorage } from '@/common/storage.js';

const state = () => ({
  videoInfo: getStorage('videoInfo', '')
});

const getters = {
  getVideoInfo(state) {
    return state.videoInfo;
  }
};

const mutations = {
  SET_VIDEO_INFO(state, newVideoInfo) {
    state.videoInfo = newVideoInfo;
  },
  CLEAR_VIDEO_INFO(state) {
    state.videoInfo = '';
  }
};

const actions = {
  setVideoInfo({ commit }, newVideoInfo) {
    commit('SET_VIDEO_INFO', newVideoInfo);
    setStorage('videoInfo', newVideoInfo);
  },
  clearVideoInfo({ commit }) {
    commit('CLEAR_VIDEO_INFO');
    removeStorage('videoInfo');
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
