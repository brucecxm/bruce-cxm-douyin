// store/index.js
import Vue from 'vue';
import Vuex from 'vuex';

import ad from './adStore';
import message from './messageStore';
import token from './token';
import videoStore from './uploadVideo';
import userInfo from './userInfo';
import videoInfo from './videoInfo';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    ad,
    message,
    token,
    videoStore,
    userInfo,
    videoInfo
  }
});
