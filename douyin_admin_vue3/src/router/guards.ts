import type { Router, RouteRecordRaw } from 'vue-router'
import useKeepAliveStore from '@/store/modules/keepAlive'
import useMenuStore from '@/store/modules/menu'
import useRouteStore from '@/store/modules/route'
import useSettingsStore from '@/store/modules/settings'
import useUserStore from '@/store/modules/user'
import { useNProgress } from '@vueuse/integrations/useNProgress'
import { asyncRoutes, asyncRoutesByFilesystem } from './routes'
import '@/assets/styles/nprogress.css'

/**
 * 设置路由守卫，处理权限控制、路由生成、以及页面重定向等逻辑
 */
function setupRoutes(router: Router) {
  router.beforeEach(async (to, _from, next) => {
    const settingsStore = useSettingsStore() // 获取设置存储
    const userStore = useUserStore() // 获取用户存储
    const routeStore = useRouteStore() // 获取路由存储
    const menuStore = useMenuStore() // 获取菜单存储

    // 如果用户已登录
    if (userStore.isLogin) {
      // 如果已经根据权限生成并注册了动态路由
      if (routeStore.isGenerate) {
        // 如果菜单不是单栏模式，则根据当前路径设置选中状态
        settingsStore.settings.menu.mode !== 'single' && menuStore.setActived(to.path)

        // 如果已经登录，且尝试访问登录页，强制跳转到主页
        if (to.name === 'login') {
          next({
            path: settingsStore.settings.home.fullPath,
            replace: true,
          })
        }
        // 如果未开启主页，但进入主页，则跳转到侧边栏第一个模块
        else if (!settingsStore.settings.home.enable && to.fullPath === settingsStore.settings.home.fullPath) {
          if (menuStore.sidebarMenus.length > 0) {
            next({
              path: menuStore.sidebarMenusFirstDeepestPath,
              replace: true,
            })
          }
          // 如果找不到任何符合条件的模块，进入主页
          else {
            next()
          }
        }
        // 正常访问页面
        else {
          next()
        }
      }
      else {
        try {
          // 如果启用了权限控制，获取用户权限
          settingsStore.settings.app.enablePermission && await userStore.getPermissions()

          // 根据配置生成动态路由
          switch (settingsStore.settings.app.routeBaseOn) {
            case 'frontend':
              routeStore.generateRoutesAtFront(asyncRoutes) // 前端生成路由
              break
            case 'backend':
              await routeStore.generateRoutesAtBack() // 后端生成路由
              break
            case 'filesystem':
              routeStore.generateRoutesAtFilesystem(asyncRoutesByFilesystem) // 文件系统生成路由
              // 如果使用文件系统生成路由，则需要手动生成导航菜单
              switch (settingsStore.settings.menu.baseOn) {
                case 'frontend':
                  menuStore.generateMenusAtFront() // 前端生成菜单
                  break
                case 'backend':
                  await menuStore.generateMenusAtBack() // 后端生成菜单
                  break
              }
              break
          }
          // 注册并记录动态路由
          const removeRoutes: (() => void)[] = []
          routeStore.flatRoutes.forEach((route) => {
            if (!/^(?:https?:|mailto:|tel:)/.test(route.path)) {
              removeRoutes.push(router.addRoute(route as RouteRecordRaw))
            }
          })
          if (settingsStore.settings.app.routeBaseOn !== 'filesystem') {
            routeStore.flatSystemRoutes.forEach((route) => {
              removeRoutes.push(router.addRoute(route as RouteRecordRaw))
            })
          }
          routeStore.setCurrentRemoveRoutes(removeRoutes) // 设置需要移除的路由
        }
        catch {}
        // 动态路由生成并注册后，重新进入当前路由
        next({
          path: to.path,
          query: to.query,
          replace: true,
        })
      }
    }
    else {
      // 如果用户未登录，跳转到登录页
      if (to.name !== 'login') {
        next({
          name: 'login',
          query: {
            redirect: to.fullPath !== settingsStore.settings.home.fullPath ? to.fullPath : undefined,
          },
        })
      }
      else {
        next()
      }
    }
  })
}

/**
 * 设置进度条
 */
function setupProgress(router: Router) {
  const { isLoading } = useNProgress() // 获取进度条状态
  router.beforeEach((_to, _from, next) => {
    const settingsStore = useSettingsStore()
    if (settingsStore.settings.app.enableProgress) {
      isLoading.value = true // 启动进度条
    }
    next()
  })
  router.afterEach(() => {
    const settingsStore = useSettingsStore()
    if (settingsStore.settings.app.enableProgress) {
      isLoading.value = false // 完成进度条
    }
  })
}

/**
 * 设置页面标题
 */
function setupTitle(router: Router) {
  router.afterEach((to) => {
    const settingsStore = useSettingsStore()
    if (settingsStore.settings.app.routeBaseOn !== 'filesystem') {
      // 如果使用前端或后端路由生成，则根据面包屑获取页面标题
      settingsStore.setTitle(to.meta.breadcrumbNeste?.at(-1)?.title ?? to.meta.title)
    }
    else {
      // 如果是文件系统路由，则直接使用设置的标题
      settingsStore.setTitle(to.meta.title)
    }
  })
}

/**
 * 设置页面缓存
 */
function setupKeepAlive(router: Router) {
  router.afterEach((to, from) => {
    const keepAliveStore = useKeepAliveStore()
    // 如果当前页面需要缓存，则将页面组件的名称存入缓存状态
    if (to.meta.cache) {
      const componentName = to.matched.at(-1)?.components?.default.name
      if (componentName) {
        keepAliveStore.add(componentName)
      }
      else {
        // 如果组件没有名称，提示警告
        console.warn('[Fantastic-admin] 该页面组件未设置组件名，会导致缓存失效，请检查')
      }
    }
    // 判断离开页面时是否需要清除缓存
    if (from.meta.cache) {
      const componentName = from.matched.at(-1)?.components?.default.name
      if (componentName) {
        // 判断是否根据缓存规则清除缓存
        switch (typeof from.meta.cache) {
          case 'string':
            if (from.meta.cache !== to.name) {
              keepAliveStore.remove(componentName)
            }
            break
          case 'object':
            if (!from.meta.cache.includes(to.name as string)) {
              keepAliveStore.remove(componentName)
            }
            break
        }
        // 判断 noCache 设置，清除指定页面的缓存
        if (from.meta.noCache) {
          switch (typeof from.meta.noCache) {
            case 'string':
              if (from.meta.noCache === to.name) {
                keepAliveStore.remove(componentName)
              }
              break
            case 'object':
              if (from.meta.noCache.includes(to.name as string)) {
                keepAliveStore.remove(componentName)
              }
              break
          }
        }
        // 如果进入的是 reload 页面，则清除离开页面的缓存
        if (to.name === 'reload') {
          keepAliveStore.remove(componentName)
        }
      }
    }
  })
}

/**
 * 其他设置，如页面滚动恢复至顶部
 */
function setupOther(router: Router) {
  router.afterEach(() => {
    document.documentElement.scrollTop = 0 // 页面滚动到顶部
  })
}

/**
 * 汇总并导出所有路由守卫设置
 */
export default function setupGuards(router: Router) {
  setupRoutes(router)
  setupProgress(router)
  setupTitle(router)
  setupKeepAlive(router)
  setupOther(router)
}
