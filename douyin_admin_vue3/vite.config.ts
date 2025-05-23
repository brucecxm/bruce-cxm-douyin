import fs from 'node:fs' // Node.js 文件系统模块，用于读写文件和目录
import path from 'node:path' // Node.js 路径模块，用于路径处理
import process from 'node:process' // Node.js 进程模块，访问环境变量等
import dayjs from 'dayjs' // 日期时间库，格式化时间
import { defineConfig, loadEnv } from 'vite' // Vite 核心API，定义配置和加载环境变量
import pkg from './package.json' // 读取 package.json 文件内容（版本号、依赖等）
import createVitePlugins from './vite/plugins' // 自定义插件集合函数

// https://vitejs.dev/config/  -- 官方配置文档地址

// Vite 配置导出，支持根据不同环境(mode)和命令(command)返回不同配置
export default defineConfig(({ mode, command }) => {
  // 读取对应模式下的环境变量文件，如 .env.development, .env.production 等
  const env = loadEnv(mode, process.cwd())

  // 全局 scss 资源数组，存放自动引入的 scss 资源内容（通过 @use 导入）
  const scssResources: string[] = []

  // 读取 src/assets/styles/resources 目录下的所有文件，遍历文件名
  fs.readdirSync('src/assets/styles/resources').forEach((dirname) => {
    // 判断是否是文件，如果是则将对应 @use 导入语句加入数组
    if (fs.statSync(`src/assets/styles/resources/${dirname}`).isFile()) {
      scssResources.push(`@use "/src/assets/styles/resources/${dirname}" as *;`)
    }
  })

  // 返回 Vite 配置对象
  return {
    // 开发服务器配置 https://cn.vitejs.dev/config/server-options
    server: {
      open: true, // 启动后自动打开浏览器
      host: true, // 监听所有地址（0.0.0.0），方便局域网访问
      port: 9000, // 服务器端口号
      proxy: { // 代理配置，用于解决跨域
        '/proxy': {
          target: 'http://localhost:9430', // 代理目标地址
          // 仅在启动 serve（开发模式）且环境变量 VITE_OPEN_PROXY 为 'true' 时启用代理
          changeOrigin: command === 'serve' && env.VITE_OPEN_PROXY === 'true',
          // 重写请求路径，去掉请求路径中的 '/proxy' 前缀
          rewrite: path => path.replace(/^\/proxy/, ''),
        },
      },
    },

    // 构建配置 https://cn.vitejs.dev/config/build-options
    build: {
      // 构建输出目录，生产环境输出到 'dist'，其他模式输出到 'dist-模式名' 目录
      outDir: mode === 'production' ? 'dist' : `dist-${mode}`,
      // 是否生成 sourcemap，用于调试，基于环境变量控制
      sourcemap: env.VITE_BUILD_SOURCEMAP === 'true',
    },

    // 全局变量定义，供代码中使用
    define: {
      __SYSTEM_INFO__: JSON.stringify({
        pkg: {
          version: pkg.version, // 项目版本号
          dependencies: pkg.dependencies, // 生产依赖列表
          devDependencies: pkg.devDependencies, // 开发依赖列表
        },
        lastBuildTime: dayjs().format('YYYY-MM-DD HH:mm:ss'), // 最后构建时间，格式化字符串
      }),
    },

    // 自定义插件配置，传入当前模式和是否是构建阶段
    plugins: createVitePlugins(mode, command === 'build'),

    // 路径别名配置
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'), // @ 映射到 src 目录，方便导入
        '#': path.resolve(__dirname, 'src/types'), // # 映射到 src/types 目录，方便类型导入
      },
    },

    // CSS 相关配置
    css: {
      preprocessorOptions: {
        scss: {
          api: 'modern-compiler', // 使用现代编译器模式处理 scss
          additionalData: scssResources.join(''), // 自动在每个 scss 文件开头注入这些全局资源，方便复用变量和 mixin
        },
      },
    },
  }
})
