// store/modules/ad.js
const state = () => ({
  hasSeenAd: false
});

const mutations = {
  MARK_AD_AS_SEEN(state) {
    state.hasSeenAd = true;
  }
};

const actions = {
  markAdAsSeen({ commit }) {
    commit('MARK_AD_AS_SEEN');
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
};
