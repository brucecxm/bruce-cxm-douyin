const state = () => ({
  // 安全初始化角色列表，处理可能的JSON解析错误
  roles: (() => {
    try {
      const storedRoles = localStorage.getItem('roles');
      // 确保解析后的数据是数组
      return storedRoles ? JSON.parse(storedRoles) : [];
    } catch (error) {
      console.error('Failed to parse roles from localStorage:', error);
      // 解析失败时清空无效数据并返回空数组
      localStorage.removeItem('roles');
      return [];
    }
  })(),

  // 安全初始化权限列表
  permissions: (() => {
    try {
      const storedPermissions = localStorage.getItem('permissions');
      return storedPermissions ? JSON.parse(storedPermissions) : [];
    } catch (error) {
      console.error('Failed to parse permissions from localStorage:', error);
      localStorage.removeItem('permissions');
      return [];
    }
  })()
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
    localStorage.setItem('roles', JSON.stringify(state.roles));
  },

  SET_PERMISSIONS(state, permissions) {
    state.permissions = Array.isArray(permissions) ? permissions : [];
    localStorage.setItem('permissions', JSON.stringify(state.permissions));
  },

  CLEAR_AUTH(state) {
    state.roles = [];
    state.permissions = [];
    localStorage.removeItem('roles');
    localStorage.removeItem('permissions');
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
