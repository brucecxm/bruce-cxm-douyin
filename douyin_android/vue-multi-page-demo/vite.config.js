import { defineConfig } from 'vite';
import vue2 from '@vitejs/plugin-vue2';

const { fileURLToPath, URL } = require('url');

export default defineConfig({
  plugins: [
    vue2()
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: true,
    proxy: {
      '/api': {
        target: 'http://1.94.131.16:7400',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        logLevel: 'info'
      },
      '/ws': {
        target: 'ws://1.94.131.16:7400',
        ws: true,
        changeOrigin: true,
        logLevel: 'info'
      }
    }
  },
  build: {
    outDir: 'dist',
    minify: 'terser',
    sourcemap: false,
    emptyOutDir: true,
    chunkSizeWarningLimit: 1500,
    terserOptions: {
      compress: {
        drop_console: false,
        drop_debugger: true
      }
    }
  }
});
