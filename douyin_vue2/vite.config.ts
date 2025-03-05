import { defineConfig } from "vite";
import legacy from "@vitejs/plugin-legacy";
import vue2 from "@vitejs/plugin-vue2";

const { fileURLToPath, URL } = require("url");

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue2(),
    legacy({
      targets: ["ie >= 11"],
      additionalLegacyPolyfills: ["regenerator-runtime/runtime"],
    }),
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  build: {
    target: "esnext",  // 设置目标为 esnext，支持异步生成器函数
  },
  server: {
    host: '0.0.0.0',  // 允许外部访问
    port: 5173,        // 使用的端口号
    open: true,         // 启动时自动打开浏览器
    proxy: {
      "/api": {
        target: "http://localhost:9430",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
      // "/websocket": {
      //   target: "ws://localhost:9998/ws",
      //   changeOrigin: true,
      //   ws: true, // 开启 WebSocket 代理
      //   rewrite: (path) => path.replace(/^\/websocket/, ""),
      // },
    },
  },
});
