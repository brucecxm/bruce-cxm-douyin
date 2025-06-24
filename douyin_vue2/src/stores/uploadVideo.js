import Vue from 'vue';

const state = () => ({
  videos: {}  // 以 videoId 作为 key，存储视频对象
});

const mutations = {
  ADD_VIDEO(state, { videoId, videoData }) {
    // Vue.set 保证响应式
    Vue.set(state.videos, videoId, videoData);
  },
  REMOVE_VIDEO(state, videoId) {
    Vue.delete(state.videos, videoId);
  },
  CLEAR_ALL_VIDEOS(state) {
    Object.values(state.videos).forEach(video => {
      URL.revokeObjectURL(video.url);
    });
    state.videos = {};
  }
};

const actions = {
  addVideo({ commit }, { videoId, blob, metadata }) {
    const url = URL.createObjectURL(blob);
    const videoData = {
      blob,
      url,
      metadata,
      timestamp: new Date().toISOString()
    };
    commit('ADD_VIDEO', { videoId, videoData });
  },
  removeVideo({ commit, state }, videoId) {
    const video = state.videos[videoId];
    if (video) {
      URL.revokeObjectURL(video.url);
      commit('REMOVE_VIDEO', videoId);
    }
  },
  clearAllVideos({ commit }) {
    commit('CLEAR_ALL_VIDEOS');
  }
};

const getters = {
  getVideo: (state) => (videoId) => {
    return state.videos[videoId];
  },
  getAllVideos: (state) => {
    return state.videos;
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
};
