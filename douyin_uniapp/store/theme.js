// store/modules/theme.js
import { getStorage, setStorage, removeStorage } from '@/common/storage.js';

const state = () => ({
  theme: getStorage('theme', 'light'),
  token: getStorage('token', '')
});

const getters = {
  getTheme(state) {
    return state.theme;
  },
  getToken(state) {
    return state.token;
  }
};

const mutations = {
  SET_THEME(state, newTheme) {
    state.theme = newTheme;
  },
  SET_TOKEN(state, newToken) {
    state.token = newToken;
  },
  CLEAR_TOKEN(state) {
    state.token = '';
  }
};

const actions = {
  setTheme({ commit }, newTheme) {
    commit('SET_THEME', newTheme);
    setStorage('theme', newTheme);
  },
  setToken({ commit }, newToken) {
    commit('SET_TOKEN', newToken);
    setStorage('token', newToken);
  },
  clearToken({ commit }) {
    commit('CLEAR_TOKEN');
    removeStorage('token');
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
