// store/modules/videoInfo.js
const state = () => ({
  videoInfo: localStorage.getItem('videoInfo') || ''
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
    localStorage.setItem('videoInfo', newVideoInfo);
  },
  clearVideoInfo({ commit }) {
    commit('CLEAR_VIDEO_INFO');
    localStorage.removeItem('videoInfo');
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
