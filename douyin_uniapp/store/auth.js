import { getStorage, setStorage, removeStorage } from '@/common/storage.js';

const state = () => ({
  // 安全初始化角色列表，处理可能的JSON解析错误
  roles: getStorage('roles', []),

  // 安全初始化权限列表
  permissions: getStorage('permissions', [])
});

const getters = {
  getRoles: (state) => state.roles,
  getPermissions: (state) => state.permissions,
  // 新增：检查是否有某个角色
  hasRole: (state) => (role) => state.roles.includes(role),
  // 新增：检查是否有某个权限
  hasPermission: (state) => (permission) =>
    state.permissions.includes(permission)
};

const mutations = {
  SET_ROLES(state, roles) {
    // 确保存储的是数组
    state.roles = Array.isArray(roles) ? roles : [];
    setStorage('roles', state.roles);
  },

  SET_PERMISSIONS(state, permissions) {
    state.permissions = Array.isArray(permissions) ? permissions : [];
    setStorage('permissions', state.permissions);
  },

  CLEAR_AUTH(state) {
    state.roles = [];
    state.permissions = [];
    removeStorage('roles');
    removeStorage('permissions');
  }
};

const actions = {
  loginSetRoles({ commit }, roles) {
    commit('SET_ROLES', roles);
  },

  loginSetPermissions({ commit }, permissions) {
    commit('SET_PERMISSIONS', permissions);
  },

  logoutClearAuth({ commit }) {
    commit('CLEAR_AUTH');
  },

  // 新增：动态添加角色
  addRole({ commit, state }, role) {
    if (!state.roles.includes(role)) {
      const newRoles = [...state.roles, role];
      commit('SET_ROLES', newRoles);
    }
  },

  // 新增：动态添加权限
  addPermission({ commit, state }, permission) {
    if (!state.permissions.includes(permission)) {
      const newPermissions = [...state.permissions, permission];
      commit('SET_PERMISSIONS', newPermissions);
    }
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
