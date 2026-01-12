// store/modules/userInfo.js
const state = () => ({
  userInfo: (() => {
    try {
      const storedUserInfo = localStorage.getItem('userInfo');
      return storedUserInfo ? JSON.parse(storedUserInfo) : {};
    } catch (error) {
      console.error('Failed to parse userInfo from localStorage:', error);
      localStorage.removeItem('userInfo');
      return {};
    }
  })()
});

const mutations = {
  SET_USER_INFO(state, info) {
    state.userInfo = info;
    // 同步保存到localStorage
    localStorage.setItem('userInfo', JSON.stringify(info));
  },
  CLEAR_USER_INFO(state) {
    state.userInfo = {};
    // 同时清除localStorage中的数据
    localStorage.removeItem('userInfo');
  }
};

const actions = {
  setUserInfo({ commit }, info) {
    commit('SET_USER_INFO', info);
  },
  clearUserInfo({ commit }) {
    commit('CLEAR_USER_INFO');
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
};
