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
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:7456",
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
