// store/testDataMap.js （新建此文件）
const state = {
  // 用对象模拟Map结构，存储键值对
  data: {}
};

const mutations = {
  // 添加/更新数据（key存在则覆盖，不存在则新增）
  SET_TEST_DATA(state, { key, value }) {
    state.data[key] = value;
  },
  // 删除指定key的数据
  REMOVE_TEST_DATA(state, key) {
    delete state.data[key];
  },
  // 清空所有测试数据
  CLEAR_TEST_DATA_MAP(state) {
    state.data = {};
  }
};

const actions = {
  // 同步添加数据
  setTestData({ commit }, payload) {
    commit('SET_TEST_DATA', payload);
  },
  // 异步添加数据（示例：模拟API请求后存储）
  async setTestDataAsync({ commit }, payload) {
    try {
      // 模拟异步操作
      await new Promise((resolve) => setTimeout(resolve, 500));
      commit('SET_TEST_DATA', payload);
      return true;
    } catch (error) {
      console.error('异步存储失败:', error);
      return false;
    }
  },
  // 异步删除数据
  async removeTestDataAsync({ commit }, key) {
    await new Promise((resolve) => setTimeout(resolve, 300));
    commit('REMOVE_TEST_DATA', key);
  }
};

const getters = {
  // 获取指定key的数据
  getTestData: (state) => (key) => state.data[key],
  // 获取所有测试数据
  getAllTestData: (state) => state.data,
  // 判断key是否存在
  hasTestData: (state) => (key) =>
    Object.prototype.hasOwnProperty.call(state.data, key),
  // 获取数据总数
  getTestDataCount: (state) => Object.keys(state.data).length
};

export default {
  namespaced: true, // 开启命名空间，避免模块间冲突
  state,
  mutations,
  actions,
  getters
};
