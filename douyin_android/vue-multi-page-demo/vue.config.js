const { defineConfig } = require('@vue/cli-service')
const fs = require('fs')
const path = require('path')

module.exports = defineConfig({
  transpileDependencies: true,
  pages: {
    index: {
      entry: 'src/pages/index/main.js',
      template: 'public/index.html',
      filename: 'index.html',
      title: '首页'
    },
    page1: {
      entry: 'src/pages/page1/main.js',
      template: 'public/index.html',
      filename: 'page1.html',
      title: '页面1'
    },
    page2: {
      entry: 'src/pages/page2/main.js',
      template: 'public/index.html',
      filename: 'page2.html',
      title: '页面2'
    }
  },
  configureWebpack: {
    plugins: [
      {
        apply: (compiler) => {
          compiler.hooks.afterEmit.tap('GenerateRouteConfig', (compilation) => {
            const routes = [
              { path: '/index', file: 'index.html', title: '首页' },
              { path: '/page1', file: 'page1.html', title: '页面1' },
              { path: '/page2', file: 'page2.html', title: '页面2' }
            ]

            const routeConfigPath = path.join(compiler.outputPath, 'routes.json')
            fs.writeFileSync(routeConfigPath, JSON.stringify(routes, null, 2))
            console.log(`Generated route config at: ${routeConfigPath}`)
          })
        }
      }
    ]
  }
})
