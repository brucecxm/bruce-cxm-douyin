import path from 'node:path'
import process from 'node:process'
// 引入 Vue 相关插件
import vueLegacy from '@vitejs/plugin-legacy' // 兼容旧浏览器插件
import vue from '@vitejs/plugin-vue' // Vue 单文件组件支持
import vueJsx from '@vitejs/plugin-vue-jsx' // Vue JSX 支持

import boxen from 'boxen' // 终端打印美化框
import picocolors from 'picocolors' // 终端颜色打印

import Unocss from 'unocss/vite' // 原子化CSS框架插件
import autoImport from 'unplugin-auto-import/vite' // 自动导入插件
import TurboConsole from 'unplugin-turbo-console/vite' // 开发时控制台打印优化插件
import components from 'unplugin-vue-components/vite' // 按需自动注册Vue组件插件

import { loadEnv, type PluginOption } from 'vite' // Vite API
import AppLoading from 'vite-plugin-app-loading' // App启动loading页面插件
import Archiver from 'vite-plugin-archiver' // 构建产物归档插件
import banner from 'vite-plugin-banner' // 构建产物添加版权banner插件
import { compression } from 'vite-plugin-compression2' // 代码压缩插件
import { envParse, parseLoadedEnv } from 'vite-plugin-env-parse' // 环境变量解析插件
import { vitePluginFakeServer } from 'vite-plugin-fake-server' // 假数据接口插件
import Pages from 'vite-plugin-pages' // 文件系统路由插件
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons' // SVG图标管理插件
import VueDevTools from 'vite-plugin-vue-devtools' // Vue开发者工具插件
import Layouts from 'vite-plugin-vue-meta-layouts' // Vue页面布局插件

// 创建 Vite 插件列表的工厂函数
export default function createVitePlugins(mode: string, isBuild = false) {
  // 读取环境变量并解析成类型安全的格式
  const viteEnv = parseLoadedEnv(loadEnv(mode, process.cwd()))

  // 初始化插件数组
  const vitePlugins: (PluginOption | PluginOption[])[] = [
    // Vue 支持
    vue(),

    // Vue JSX 支持
    vueJsx(),

    // 旧浏览器兼容插件，polyfill部分ES新特性
    vueLegacy({
      renderLegacyChunks: false, // 不单独打包兼容代码块
      modernPolyfills: [
        'es.array.at',
        'es.array.find-last',
        'es.object.has-own',
      ],
    }),

    // 开启 Vue Devtools (基于环境变量)
    viteEnv.VITE_OPEN_DEVTOOLS && VueDevTools(),

    // 解析环境变量并生成类型声明
    envParse({
      dtsPath: 'src/types/env.d.ts',
    }),

    // 自动导入 Vue、Vue Router 和 Pinia 的 API，避免重复导入
    autoImport({
      imports: [
        'vue',
        'vue-router',
        'pinia',
      ],
      dts: './src/types/auto-imports.d.ts', // 生成类型声明文件
      dirs: [
        './src/utils/composables/**', // 自动导入自定义hooks等
      ],
    }),

    // 自动按需注册 Vue 组件，避免手动import
    components({
      globs: [
        'src/ui/components/*/index.vue',
        'src/components/*/index.vue',
      ],
      dts: './src/types/components.d.ts',
    }),

    // UnoCSS 原子化CSS支持
    Unocss(),

    // SVG图标插件，管理svg图标成 symbol
    createSvgIconsPlugin({
      iconDirs: [path.resolve(process.cwd(), 'src/assets/icons/')],
      symbolId: 'icon-[dir]-[name]',
      svgoOptions: isBuild, // 生产环境开启svgo优化
    }),

    // 假数据接口插件，方便开发调试接口，支持生产环境模拟数据（可选）
    vitePluginFakeServer({
      logger: !isBuild,
      include: 'src/mock',
      infixName: false,
      enableProd: isBuild && viteEnv.VITE_BUILD_MOCK,
    }),

    // 页面布局插件，自动给页面套默认布局
    Layouts({
      defaultLayout: 'index',
    }),

    // 文件系统路由，自动生成路由规则
    Pages({
      dirs: 'src/views',
      exclude: [
        '**/components/**/*.vue', // 排除组件目录
      ],
    }),

    // 代码压缩插件（gzip和brotli压缩）
    viteEnv.VITE_BUILD_COMPRESS?.split(',').includes('gzip') && compression(),
    viteEnv.VITE_BUILD_COMPRESS?.split(',').includes('brotli') && compression({
      exclude: [/\.(br)$/, /\.(gz)$/],
      algorithm: 'brotliCompress',
    }),

    // 构建产物归档插件，根据环境变量决定是否启用
    viteEnv.VITE_BUILD_ARCHIVE && Archiver({
      archiveType: viteEnv.VITE_BUILD_ARCHIVE,
    }),

    // App启动时显示loading页面
    AppLoading('loading.html'),

    // 优化开发时console输出插件
    TurboConsole(),

    // 构建产物添加版权信息banner
    banner(`
/**
 * 由 Fantastic-admin 提供技术支持
 * Powered by Fantastic-admin
 * https://fantastic-admin.hurui.me
 */
    `),

    // 自定义插件，根据环境变量注入调试工具
    {
      name: 'vite-plugin-debug-plugin',
      enforce: 'pre', // 优先执行
      transform: (code, id) => {
        if (/src\/main.ts$/.test(id)) {
          if (viteEnv.VITE_APP_DEBUG_TOOL === 'eruda') {
            code = code.concat(`
              import eruda from 'eruda'
              eruda.init()
            `)
          }
          else if (viteEnv.VITE_APP_DEBUG_TOOL === 'vconsole') {
            code = code.concat(`
              import VConsole from 'vconsole'
              new VConsole()
            `)
          }
          return {
            code,
            map: null,
          }
        }
      },
    },

    // 自定义插件，注入禁用浏览器开发者工具代码
    {
      name: 'vite-plugin-disable-devtool',
      enforce: 'pre',
      transform: (code, id) => {
        if (/src\/main.ts$/.test(id)) {
          if (viteEnv.VITE_APP_DISABLE_DEVTOOL) {
            code = code.concat(`
              import DisableDevtool from 'disable-devtool'
              DisableDevtool()
            `)
          }
          return {
            code,
            map: null,
          }
        }
      },
    },

    // 开发环境启动时，终端输出项目信息的插件
    {
      name: 'vite-plugin-terminal-info',
      apply: 'serve', // 只在dev服务器生效
      async buildStart() {
        const { bold, green, cyan, bgGreen, underline } = picocolors
        // eslint-disable-next-line no-console
        console.log(
          boxen(
            `${bold(green(`由 ${bgGreen('Fantastic-admin')} 驱动`))}\n\n${underline('https://fantastic-admin.hurui.me')}\n\n当前使用：${cyan('基础版')}`,
            {
              padding: 1,
              margin: 1,
              borderStyle: 'double',
              textAlignment: 'center',
            },
          ),
        )
      },
    },
  ]

  return vitePlugins
}
