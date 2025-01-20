import type { RouteRecordRaw } from 'vue-router'

function Layout() {
  return import('@/layouts/index.vue')
}
const routes: RouteRecordRaw = {
  path: '/multilevel_menu_example',
  component: Layout,
  redirect: '/multilevel_menu_example/page',
  name: 'multilevelMenuExample',
  meta: {
    title: '抖音管理',
    icon: 'i-heroicons-solid:menu-alt-3',
  },
  children: [
    {
      path: 'page',
      name: 'multilevelMenuExample1',
      component: () => import('@/views/multilevel_menu_example/page.vue'),
      meta: {
        title: '视频管理',
      },
    },
    {
      path: 'level2_mall', // Changed to a unique path for Mall Management
      name: 'multilevelMenuExample2',
      redirect: '/multilevel_menu_example/level2_mall/page',
      meta: {
        title: '商城管理',
      },
      children: [
        {
          path: 'page',
          name: 'multilevelMenuExample2-1',
          component: () => import('@/views/multilevel_menu_example/level2/page.vue'),
          meta: {
            title: '商品管理',
          },
        },
        {
          path: 'level3',
          name: 'multilevelMenuExample2-2',
          redirect: '/multilevel_menu_example/level2_mall/level3/page1',
          meta: {
            title: '商家管理',
          },
          children: [
            {
              path: 'page1',
              name: 'multilevelMenuExample2-2-1',
              component: () => import('@/views/multilevel_menu_example/level2/level3/page1.vue'),
              meta: {
                title: '流量管理',
              },
            },
            {
              path: 'page2',
              name: 'multilevelMenuExample2-2-2',
              component: () => import('@/views/multilevel_menu_example/level2/level3/page2.vue'),
              meta: {
                title: '供应商管理',
              },
            },
            {
              path: 'page3',
              name: 'multilevelMenuExample2-2-3',
              component: () => import('@/views/multilevel_menu_example/level2/level3/page3.vue'),
              meta: {
                title: '好友',
              },
            },
          ],
        },
      ],
    },
    {
      path: 'level2_friends', // Changed to a unique path for Friend Management
      name: 'multilevelMenuExample3',
      redirect: '/multilevel_menu_example/level2_friends/page',
      meta: {
        title: '好友管理',
      },
      children: [
        {
          path: 'page',
          name: 'multilevelMenuExample3-1',
          component: () => import('@/views/multilevel_menu_example/level2/page.vue'),
          meta: {
            title: '关注管理',
          },
        },
        {
          path: 'level3',
          name: 'multilevelMenuExample3-2',
          redirect: '/multilevel_menu_example/level2_friends/level3/page1',
          meta: {
            title: '关系管理',
          },
          children: [
            {
              path: 'page1',
              name: 'multilevelMenuExample3-2-1',
              component: () => import('@/views/multilevel_menu_example/level2/level3/page1.vue'),
              meta: {
                title: '以及删除的好友',
              },
            },
            {
              path: 'page2',
              name: 'multilevelMenuExample3-2-2',
              component: () => import('@/views/multilevel_menu_example/level2/level3/page2.vue'),
              meta: {
                title: '拉黑的好友',
              },
            },
            {
              path: 'page3',
              name: 'multilevelMenuExample3-2-3',
              component: () => import('@/views/multilevel_menu_example/level2/level3/page3.vue'),
              meta: {
                title: '好友',
              },
            },
          ],
        },
      ],
    },
  ],
}

export default routes
