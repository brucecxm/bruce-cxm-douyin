import { defineConfig } from 'vite';
import legacy from '@vitejs/plugin-legacy';
import vue2 from '@vitejs/plugin-vue2';
import fs from 'fs';

const { fileURLToPath, URL } = require('url');

export default defineConfig(({ mode }) => {
  if (mode === 'development') {
    // const httpsOptions = {
    //   key: fs.readFileSync(
    //     'C:/Users/Administrator/Desktop/tools/127.0.0.1+4-key.pem'
    //   ),
    //   cert: fs.readFileSync(
    //     'C:/Users/Administrator/Desktop/tools/127.0.0.1+4.pem'
    //   )
    // };
    return {
      plugins: [
        vue2(),
        legacy({
          targets: ['ie >= 11'],
          additionalLegacyPolyfills: ['regenerator-runtime/runtime']
        })
      ],
      build: {
        outDir: 'dist'
      },
      resolve: {
        alias: {
          '@': fileURLToPath(new URL('./src', import.meta.url))
        }
      },
      server: {
        host: true,
        // https: httpsOptions, // 开启 https，使用证书
        proxy: {
          '/api': {
            target: 'http://localhost:7456',
            // target: 'http://116.196.98.171:9430',
            changeOrigin: true,
            ws: true, // ✅ 加上这行支持 WebSocket 代理
            rewrite: (path) => path.replace(/^\/api/, '')
          }
        }
      }
    };
  } else if (mode === 'production') {
    // const httpsOptions = {
    //   key: fs.readFileSync(
    //     'C:/Users/Administrator/Desktop/tools/127.0.0.1+4-key.pem'
    //   ),
    //   cert: fs.readFileSync(
    //     'C:/Users/Administrator/Desktop/tools/127.0.0.1+4.pem'
    //   )
    // };
    return {
      plugins: [
        vue2(),
        legacy({
          targets: ['ie >= 11'],
          additionalLegacyPolyfills: ['regenerator-runtime/runtime']
        })
      ],
      build: {
        outDir: '/usr/share/nginx/html/dist'
      },
      resolve: {
        alias: {
          '@': fileURLToPath(new URL('./src', import.meta.url))
        }
      },
      server: {
        host: true,
        proxy: {
          '/api': {
            target: 'http://localhost:5177',
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, '')
          }
        }
      }
      // 生产环境一般由nginx等代理做HTTPS，不用在Vite里开https
    };
  }
});
