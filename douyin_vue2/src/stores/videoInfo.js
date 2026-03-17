// store/modules/videoInfo.js
const state = () => ({
  videoInfo: localStorage.getItem('videoInfo') || '',
  sharePopupVisible: false
});

const getters = {
  getVideoInfo(state) {
    return state.videoInfo;
  },
  sharePopupVisible(state) {
    return state.sharePopupVisible;
  }
};

const mutations = {
  SET_VIDEO_INFO(state, newVideoInfo) {
    state.videoInfo = newVideoInfo;
  },
  CLEAR_VIDEO_INFO(state) {
    state.videoInfo = '';
  },
  OPEN_SHARE_POPUP(state) {
    state.sharePopupVisible = true;
  },
  CLOSE_SHARE_POPUP(state) {
    state.sharePopupVisible = false;
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
  },
  openSharePopup({ commit }) {
    commit('OPEN_SHARE_POPUP');
  },
  closeSharePopup({ commit }) {
    commit('CLOSE_SHARE_POPUP');
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
