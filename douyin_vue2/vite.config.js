import { defineConfig } from 'vite';
import legacy from '@vitejs/plugin-legacy';
import vue2 from '@vitejs/plugin-vue2';
import fs from 'fs';

const { fileURLToPath, URL } = require('url');

// 自定义日志插件
function viteLoggerPlugin() {
  return {
    name: 'vite-logger',
    configResolved(config) {
      console.log(`[Vite] 配置已解析 - 模式: ${config.mode}`);
    },
    buildStart() {
      console.log('[Vite] 开始构建...');
    },
    buildEnd() {
      console.log('[Vite] 构建完成!');
    },
    handleHotUpdate({ file, modules }) {
      console.log(`[HMR] 文件变更: ${file}`);
      console.log(`[HMR] 受影响的模块数量: ${modules.length}`);
      return modules;
    }
  };
}

export default defineConfig(({ mode }) => {
  // 定义httpsOptions，根据模式动态设置
  const httpsOptions = {
    key: fs.readFileSync(
      'C:/Users/Administrator/Desktop/tools/127.0.0.1+4-key.pem'
    ),
    cert: fs.readFileSync(
      'C:/Users/Administrator/Desktop/tools/127.0.0.1+4.pem'
    )
  };

  const commonConfig = {
    plugins: [
      vue2(),
      legacy({
        targets: ['ie >= 11'],
        additionalLegacyPolyfills: ['regenerator-runtime/runtime']
      }),
      viteLoggerPlugin() // 添加自定义日志插件
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    logLevel: 'info', // 设置日志级别：'info', 'warn', 'error', 'silent'
    clearScreen: false, // 不清除控制台
    server: {
      host: true,
      logLevel: 'info', // 服务器日志级别
      https: httpsOptions, // 开启 https，使用证书
      proxy: {},
      watch: {
        // 监听时忽略 node_modules 和 dist（避免无意义的文件监控消耗内存）
        ignored: ['**/node_modules/**', '**/dist/**']
      }
    }
  };

  if (mode === 'development') {
    return {
      ...commonConfig,
      build: {
        outDir: 'dist',
        minify: false, // 开发环境不压缩
        sourcemap: true, // 生成源码映射
        emptyOutDir: true, // 清空输出目录
        reportCompressedSize: true, // 报告压缩大小
        chunkSizeWarningLimit: 1500, // 块大小警告限制
        rollupOptions: {
          onwarn(warning, warn) {
            console.log(`[Rollup Warning] ${warning.message}`);
            warn(warning);
          }
        }
      },
      server: {
        ...commonConfig.server,
        proxy: {
          '/api': {
            target: 'http://localhost:9430',
            // target: 'https://116.196.98.171:9430',
            changeOrigin: true,
            ws: true, // ✅ 加上这行支持 WebSocket 代理
            rewrite: (path) => path.replace(/^\/api/, ''),
            logLevel: 'debug' // 代理日志级别
          }
        }
      },
      optimizeDeps: {
        // 排除不需要预构建的依赖（已是 ESM 格式或体积较大的库）
        exclude: [
          '@fortawesome/fontawesome-svg-core',
          '@fortawesome/free-solid-svg-icons',
          'hls.js',
          '@videojs/http-streaming',
          'leaflet'
        ],
        // 强制预构建某些依赖（避免重复处理）
        include: [
          'axios/dist/axios.min.js',
          'vuex',
          'element-ui',
          'vant',
          'video.js',
          'vue-infinite-scroll'
        ],
        esbuildOptions: {
          // 增加 esbuild 日志
          logLevel: 'info'
        }
      }
    };
  } else if (mode === 'production') {
    return {
      ...commonConfig,
      build: {
        outDir: '/usr/share/nginx/html/dist',
        minify: 'terser', // 使用 terser 压缩
        sourcemap: false, // 生产环境不生成源码映射
        emptyOutDir: true,
        reportCompressedSize: true,
        chunkSizeWarningLimit: 1500,
        rollupOptions: {
          onwarn(warning, warn) {
            console.log(`[Rollup Warning] ${warning.message}`);
            warn(warning);
          }
        },
        terserOptions: {
          compress: {
            drop_console: false, // 保留 console 语句
            drop_debugger: true
          }
        }
      },
      server: {
        ...commonConfig.server,
        proxy: {
          '/api': {
            target: 'http://localhost:7456',
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, ''),
            logLevel: 'info'
          }
        }
      },
      optimizeDeps: {
        // 排除不需要预构建的依赖
        exclude: [],
        // 强制预构建某些依赖
        include: [
          'axios/dist/axios.min.js',
          'vuex',
          'element-ui',
          'vant',
          'video.js',
          'vue-infinite-scroll'
        ]
      }
    };
  }
});
