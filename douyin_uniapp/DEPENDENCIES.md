# 项目依赖说明

## 📦 已添加的依赖

### 核心框架依赖
- **vue**: ^2.7.16 - Vue.js 核心框架
- **vuex**: ^3.6.2 - 状态管理
- **vue-router**: ^3.6.5 - 路由管理
- **vue-demi**: ^0.14.6 - Vue 版本兼容层

### UI 和图标依赖
- **element-ui**: ^2.15.14 - Element UI 组件库
- **element-china-area-data**: ^6.1.0 - 中国地区数据
- **@fortawesome/fontawesome-free**: ^6.5.2 - FontAwesome 图标库
- **@fortawesome/fontawesome-svg-core**: ^6.5.2 - FontAwesome 核心
- **@fortawesome/free-solid-svg-icons**: ^6.5.2 - FontAwesome 图标
- **@fortawesome/vue-fontawesome**: ^2.0.10 - Vue FontAwesome 组件

### 网络请求和通信
- **axios**: ^0.19.2 - HTTP 请求库
- **socket.io-client**: ^4.8.1 - Socket.IO 客户端
- **sockjs-client**: ^1.6.1 - SockJS 客户端
- **vue-socket.io**: ^3.0.10 - Vue Socket.IO 插件
- **vue-websocket**: ^0.2.3 - Vue WebSocket 插件
- **stompjs**: ^2.3.3 - STOMP 协议客户端

### 音视频处理
- **video.js**: ^7.20.3 - 视频播放器
- **videojs-contrib-hls**: ^5.15.0 - HLS 支持
- **@videojs/http-streaming**: ^2.14.1 - HTTP 流媒体
- **hls.js**: ^1.6.7 - HLS.js 库
- **agora-rtc-sdk-ng**: ^4.23.4 - 声网 RTC SDK

### 地图和位置
- **leaflet**: ^1.9.4 - Leaflet 地图库

### 工具库
- **uuid**: ^11.1.0 - UUID 生成器
- **jwt-decode**: ^4.0.0 - JWT 解码器
- **spark-md5**: ^3.0.2 - MD5 哈希
- **vue-infinite-scroll**: ^2.0.2 - 无限滚动
- **@vue/composition-api**: ^1.7.2 - Vue 组合式 API

### 开发工具依赖
- **vite**: ^3.2.11 - 构建工具
- **eslint**: ^8.57.1 - 代码检查
- **prettier**: ^3.5.3 - 代码格式化
- **sass**: ^1.79.4 - Sass 预处理器

## ⚠️ 兼容性说明

### uni-app 限制
1. **Element UI**: 在 uni-app 中可能不完全兼容，建议使用 uni-ui 组件
2. **Vue Router**: uni-app 有自己的路由系统，可能不需要 vue-router
3. **Socket.IO**: 在小程序平台可能有限制
4. **Video.js**: 在移动端可能需要特殊配置

### 平台兼容性
- **H5**: 大部分依赖可用
- **小程序**: 部分依赖可能不兼容
- **App**: 需要原生插件支持

## 🚀 安装命令

```bash
# 安装所有依赖
npm install

# 或者使用 yarn
yarn install
```

## 📝 使用建议

1. **渐进式引入**: 建议先安装核心依赖，再逐步添加其他功能
2. **平台测试**: 在不同平台测试依赖的兼容性
3. **替代方案**: 对于不兼容的依赖，寻找 uni-app 原生替代方案

## 🔧 配置说明

某些依赖可能需要额外配置：
- **Vite**: 需要配置 vite.config.js
- **ESLint**: 需要配置 .eslintrc.js
- **Prettier**: 需要配置 .prettierrc



