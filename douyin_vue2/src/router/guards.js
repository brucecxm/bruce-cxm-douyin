// src/router/guards.js
import store from '@/stores'; // 注意：this.$store 在这里不能用，必须显式引入

// 登录认证守卫
export const authGuard = (to, from, next) => {
  const roles = store.getters['auth/getRoles'];
  if (!roles || roles.length === 0) {
    next({ path: '/login', query: { redirect: to.fullPath } });
    return false;
  }
  return true;
};

// 角色守卫
export const roleGuard = (requiredRoles) => {
  return (to, from, next) => {
    const roles = store.getters['auth/getRoles'];
    if (!requiredRoles.some((role) => roles.includes(role))) {
      // 角色不足，再检查权限
      if (to.meta.permissions) {
        const permissions = store.getters['auth/getPermissions'];
        if (!to.meta.permissions.some((perm) => permissions.includes(perm))) {
          console.warn(
            `权限不足: 需要 ${to.meta.permissions}，实际 ${permissions}`
          );
          next({ path: '/403' });
          return false;
        }
      } else {
        console.warn(`角色不足: 需要 ${requiredRoles}，实际 ${roles}`);
        next({ path: '/403' });
        return false;
      }
    }
    return true;
  };
};

// 权限守卫
export const permissionGuard = (requiredPermissions) => {
  return (to, from, next) => {
    const permissions = store.getters['auth/getPermissions'];
    if (!requiredPermissions.some((perm) => permissions.includes(perm))) {
      console.warn(
        `权限不足: 需要 ${requiredPermissions}，实际 ${permissions}`
      );
      next({ path: '/403' });
      return false;
    }
    return true;
  };
};
