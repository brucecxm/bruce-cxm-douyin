// store/modules/token.js
const state = () => ({
  token: localStorage.getItem('token') || ''
});

const getters = {
  getToken(state) {
    return state.token;
  }
};

const mutations = {
  SET_TOKEN(state, newToken) {
    state.token = newToken;
  },
  CLEAR_TOKEN(state) {
    state.token = '';
  }
};

const actions = {
  setToken({ commit }, newToken) {
    commit('SET_TOKEN', newToken);
    localStorage.setItem('token', newToken);
  },
  clearToken({ commit }) {
    commit('CLEAR_TOKEN');
    localStorage.removeItem('token');
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
