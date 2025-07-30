// store/modules/auth/index.js
const state = () => ({
  // 存储用户角色列表，从本地存储获取或初始化为空数组
  roles: JSON.parse(localStorage.getItem('roles')) || [],
  // 存储用户权限列表，从本地存储获取或初始化为空数组
  permissions: JSON.parse(localStorage.getItem('permissions')) || []
});

// 获取器：用于获取状态数据
const getters = {
  // 获取角色列表
  getRoles: (state) => state.roles,
  // 获取权限列表
  getPermissions: (state) => state.permissions
};

// 变更：用于同步修改状态
const mutations = {
  // 设置角色列表并保存到本地存储
  SET_ROLES(state, roles) {
    state.roles = roles;
    localStorage.setItem('roles', JSON.stringify(roles));
  },

  // 设置权限列表并保存到本地存储
  SET_PERMISSIONS(state, permissions) {
    state.permissions = permissions;
    localStorage.setItem('permissions', JSON.stringify(permissions));
  },

  // 清除角色和权限数据
  CLEAR_AUTH(state) {
    state.roles = [];
    state.permissions = [];
    localStorage.removeItem('roles');
    localStorage.removeItem('permissions');
  }
};

// 动作：用于异步操作和提交变更
const actions = {
  // 登录时调用，设置用户角色
  loginSetRoles({ commit }, roles) {
    commit('SET_ROLES', roles);
  },

  // 登录时调用，设置用户权限
  loginSetPermissions({ commit }, permissions) {
    commit('SET_PERMISSIONS', permissions);
  },

  // 登出时调用，清除用户权限数据
  logoutClearAuth({ commit }) {
    commit('CLEAR_AUTH');
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
