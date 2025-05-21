import { defineConfig } from 'vite';
import legacy from '@vitejs/plugin-legacy';
import vue2 from '@vitejs/plugin-vue2';

const { fileURLToPath, URL } = require('url');

export default defineConfig(({ mode }) => {
  // 这里根据 mode 来加载不同的配置
  if (mode === 'development') {
    return {
      plugins: [
        vue2(),
        legacy({
          targets: ['ie >= 11'],
          additionalLegacyPolyfills: ['regenerator-runtime/runtime']
        })
      ],
      build: {
        outDir: 'dist' // 开发环境输出到默认的 'build' 目录
      },
      resolve: {
        alias: {
          '@': fileURLToPath(new URL('./src', import.meta.url))
        }
      },
      server: {
        proxy: {
          '/api': {
            target: 'http://localhost:9573',
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, '')
          }
        }
      }
    };
  } else if (mode === 'production') {
    return {
      plugins: [
        vue2(),
        legacy({
          targets: ['ie >= 11'],
          additionalLegacyPolyfills: ['regenerator-runtime/runtime']
        })
      ],
      build: {
        outDir: '/usr/share/nginx/html/dist' // 生产环境输出到指定的 CentOS 目录
      },
      resolve: {
        alias: {
          '@': fileURLToPath(new URL('./src', import.meta.url))
        }
      },
      server: {
        proxy: {
          '/api': {
            target: 'http://localhost:5177',
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, '')
          }
        }
      }
    };
  }
});
