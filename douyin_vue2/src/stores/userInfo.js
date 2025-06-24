// store/modules/userInfo.js
const state = () => ({
  userInfo: {}
});

const mutations = {
  SET_USER_INFO(state, info) {
    state.userInfo = info;
  },
  CLEAR_USER_INFO(state) {
    state.userInfo = {};
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
