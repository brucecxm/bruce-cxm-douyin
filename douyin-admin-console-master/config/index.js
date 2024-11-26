"use strict";
// 模板版本：1.2.6
// 查看文档：http://vuejs-templates.github.io/webpack

const path = require("path"); // 引入 Node.js 的路径模块，用于解析路径

module.exports = {
  // 开发环境配置
  dev: {
    // 路径相关配置
    assetsSubDirectory: "static", // 静态资源目录（相对于output.path）
    assetsPublicPath: "/", // 静态资源的公共路径
    proxyTable: {
      "/api": {
        // 匹配以 /api 开头的请求
        target: "http://localhost:9430", // 后端接口的基础地址
        changeOrigin: true, // 是否修改请求的源（避免跨域问题）
        pathRewrite: { "^/api": "/admin" }, // 将 /api 替换为空字符串
        secure: false // 如果是 https 接口，需要配置为 false
      }
    }, // 配置代理，用于解决开发环境的跨域问题
    // 开发服务器的各种设置
    host: "localhost", // 开发服务器主机名，可通过 process.env.HOST 覆盖
    port: 9428, // 开发服务器端口号，可通过 process.env.PORT 覆盖
    autoOpenBrowser: true, // 是否在启动开发服务器时自动打开浏览器
    errorOverlay: true, // 是否在浏览器中显示错误覆盖层
    notifyOnErrors: false, // 是否在系统通知栏提示错误信息
    poll: false, // 文件监听轮询（用于某些无法自动更新的系统）

    // 是否使用 ESLint 进行代码检查
    useEslint: false, // 设置为 true 时启用 ESLint 检查
    showEslintErrorsInOverlay: false, // 是否在浏览器错误覆盖层中显示 ESLint 错误

    /**
     * 源映射（Source Maps）
     */
    devtool: "cheap-source-map", // 用于开发环境的 Source Map 类型，便于调试

    // 是否开启 CSS 的 Source Map（默认关闭）
    cssSourceMap: false // 一些情况下，CSS Source Map 的相对路径可能有问题
  },

  // 生产环境配置
  build: {
    // index.html 模板路径
    index: path.resolve(__dirname, "../dist/index.html"),

    // 构建输出路径
    assetsRoot: path.resolve(__dirname, "../dist"), // 输出文件的根目录
    assetsSubDirectory: "static", // 静态资源文件的子目录

    /**
     * 设置资源的公共路径
     * 部署到子路径时（如 GitHub Pages），需要根据实际情况设置此项
     * 如果你的项目部署在 https://foo.github.io/bar/，则 assetsPublicPath 应设置为 "/bar/"
     * 通常情况下，设置为 '/' 即可
     */
    assetsPublicPath: "/",

    /**
     * 源映射（Source Maps）
     */
    productionSourceMap: false, // 是否开启生产环境的 Source Map（通常关闭以提高性能）
    devtool: "source-map", // 生产环境的 Source Map 类型

    // 是否开启 Gzip 压缩（默认关闭）
    // 如果需要开启，请先安装插件：npm install --save-dev compression-webpack-plugin
    productionGzip: false, // 是否启用 gzip 压缩
    productionGzipExtensions: ["js", "css"], // 需要压缩的文件类型

    // 构建完成后是否生成 Bundle 分析报告
    // 使用以下命令生成报告：npm run build --report
    bundleAnalyzerReport: process.env.npm_config_report || false, // 是否启用 Bundle 分析

    // 构建时是否生成分析报告，可通过 npm run build:prod --generate_report 启用
    generateAnalyzerReport: process.env.npm_config_generate_report || false // 是否启用额外的 Bundle 分析报告
  }
};
