import store from '@/stores';

// 登录认证守卫
export const authGuard = (to, from, next) => {
  // 登录页本身不需要认证，直接放行
  if (to.path === '/login') {
    next();
    return;
  }

  const roles = store.getters['auth/getRoles'];
  if (!roles || roles.length === 0) {
    // 未登录，跳转到登录页并记录要跳转的页面
    next({ path: '/login', query: { redirect: to.fullPath } });
  } else {
    // 已登录，允许访问
    next();
  }
};

// 角色守卫
export const roleGuard = (requiredRoles) => {
  return (to, from, next) => {
    const roles = store.getters['auth/getRoles'];

    // 检查是否拥有所需角色
    if (requiredRoles.some((role) => roles.includes(role))) {
      // 拥有所需角色，继续导航
      next();
    } else {
      // 角色不足，检查是否有其他权限可以访问
      if (to.meta.permissions) {
        const permissions = store.getters['auth/getPermissions'];
        if (to.meta.permissions.some((perm) => permissions.includes(perm))) {
          next();
        } else {
          console.warn(
            `权限不足: 需要 ${to.meta.permissions}，实际拥有 ${permissions}`
          );
          next({ path: '/403' });
        }
      } else {
        console.warn(
          `角色不足: 需要 ${requiredRoles} 中的至少一个，实际拥有 ${roles}`
        );
        next({ path: '/403' });
      }
    }
  };
};

// 权限守卫
export const permissionGuard = (requiredPermissions) => {
  return (to, from, next) => {
    const permissions = store.getters['auth/getPermissions'];

    // 检查是否拥有所需权限
    if (requiredPermissions.some((perm) => permissions.includes(perm))) {
      // 拥有所需权限，允许访问
      next();
    } else {
      console.warn(
        `权限不足: 需要 ${requiredPermissions} 中的至少一个，实际拥有 ${permissions}`
      );
      next({ path: '/403' });
    }
  };
};
