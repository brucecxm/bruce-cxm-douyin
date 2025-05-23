import type { RouteRecordRaw } from 'vue-router'

const Layout = () => import('@/layouts/index.vue')

const routes: RouteRecordRaw = {
  path: '/multilevel_menu_example',
  component: Layout,
  redirect: '/multilevel_menu_example/video_management',
  name: 'multilevelMenuExample',
  meta: {
    title: '抖音管理',
    icon: 'i-heroicons-solid:menu-alt-3',
  },
  children: [
    // 视频管理
    {
      path: 'video_management',
      name: 'videoManagement',
      component: () => import('@/views/multilevel_menu_example/page.vue'),
      meta: {
        title: '视频管理',
      },
    },

    // 菜单管理
    {
      path: 'menu_management',
      name: 'menuManagement',
      redirect: '/multilevel_menu_example/mall_management/menu_management',
      meta: {
        title: '菜单管理',
      },
      children: [
        {
          path: 'product_management',
          name: 'productManagement',
          component: () => import('@/views/menu/navMenu/page.vue'),
          meta: {
            title: '导航菜单管理',
          },
        },

      ],
    },
    // 系统管理
    {
      path: 'system_management',
      name: 'systemManagement',
      redirect: '/multilevel_menu_example/system_management/config_management',
      meta: {
        title: '系统管理',
      },
      children: [
        {
          path: 'config_management',
          name: 'configManagement',
          component: () => import('@/views/multilevel_menu_example/level2/page.vue'),
          meta: {
            title: '配置管理',
          },
        },
        {
          path: 'permission_management',
          name: 'permissionManagement',
          component: () => import('@/views/multilevel_menu_example/level2/page.vue'),
          meta: {
            title: '权限管理',
          },
        },
        {
          path: 'role_management',
          name: 'roleManagement',
          component: () => import('@/views/multilevel_menu_example/level2/page.vue'),
          meta: {
            title: '角色管理',
          },
        },
      ],
    },
    // 好友管理

  ],
}

export default routes
