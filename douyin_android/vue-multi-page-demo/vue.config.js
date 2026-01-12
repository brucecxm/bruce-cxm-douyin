const { defineConfig } = require('@vue/cli-service')
const fs = require('fs')
const path = require('path')

// 清空目录函数
function emptyDir(dirPath) {
  if (fs.existsSync(dirPath)) {
    // 读取目录下的所有文件和子目录
    const files = fs.readdirSync(dirPath)
    files.forEach(file => {
      const filePath = path.join(dirPath, file)
      // 判断是否为目录
      if (fs.statSync(filePath).isDirectory()) {
        // 递归清空子目录
        emptyDir(filePath)
        // 删除子目录
        fs.rmdirSync(filePath)
      } else {
        // 删除文件
        fs.unlinkSync(filePath)
      }
    })
  }
}

// 复制目录函数
function copyDir(srcDir, destDir) {
  // 如果目标目录不存在，创建它
  if (!fs.existsSync(destDir)) {
    fs.mkdirSync(destDir, { recursive: true })
  }

  // 读取源目录下的所有文件和子目录
  const files = fs.readdirSync(srcDir)
  files.forEach(file => {
    const srcPath = path.join(srcDir, file)
    const destPath = path.join(destDir, file)

    // 判断是否为目录
    if (fs.statSync(srcPath).isDirectory()) {
      // 递归复制子目录
      copyDir(srcPath, destPath)
    } else {
      // 复制文件
      fs.copyFileSync(srcPath, destPath)
    }
  })
}

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://1.94.131.16:7400',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      },
      '/ws': {
        target: 'ws://1.94.131.16:7400',
        ws: true,
        changeOrigin: true
      }
    }
  },
  pages: {
    index: {
      entry: 'src/pages/douyin/main.js',
      template: 'public/index.html',
      filename: 'index.html',
      title: '抖音'
    },
    originalIndex: {
      entry: 'src/pages/index/main.js',
      template: 'public/index.html',
      filename: 'original-index.html',
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
    },
    login: {
      entry: 'src/pages/login/main.js',
      template: 'public/index.html',
      filename: 'login.html',
      title: '登录'
    },
    register: {
      entry: 'src/pages/register/main.js',
      template: 'public/index.html',
      filename: 'register.html',
      title: '注册'
    },
    categories: {
      entry: 'src/pages/categories/main.js',
      template: 'public/index.html',
      filename: 'categories.html',
      title: '分类'
    },
    address: {
      entry: 'src/pages/address/main.js',
      template: 'public/index.html',
      filename: 'address.html',
      title: '收货地址'
    },
    detail: {
      entry: 'src/pages/detail/main.js',
      template: 'public/index.html',
      filename: 'detail.html',
      title: '商品详情'
    },
    publish: {
      entry: 'src/pages/publish/main.js',
      template: 'public/index.html',
      filename: 'publish.html',
      title: '发布商品'
    },
    profile: {
      entry: 'src/pages/profile/main.js',
      template: 'public/index.html',
      filename: 'profile.html',
      title: '个人中心'
    },
    search: {
      entry: 'src/pages/search/main.js',
      template: 'public/index.html',
      filename: 'search.html',
      title: '搜索'
    },
    orders: {
      entry: 'src/pages/orders/main.js',
      template: 'public/index.html',
      filename: 'orders.html',
      title: '我的订单'
    },
    orderDetail: {
      entry: 'src/pages/orderDetail/main.js',
      template: 'public/index.html',
      filename: 'orderDetail.html',
      title: '订单详情'
    },
    favorites: {
      entry: 'src/pages/favorites/main.js',
      template: 'public/index.html',
      filename: 'favorites.html',
      title: '我的收藏'
    },
    messages: {
      entry: 'src/pages/messages/main.js',
      template: 'public/index.html',
      filename: 'messages.html',
      title: '消息中心'
    },
    chat: {
      entry: 'src/pages/chat/main.js',
      template: 'public/index.html',
      filename: 'chat.html',
      title: '聊天'
    },
    myProducts: {
      entry: 'src/pages/myProducts/main.js',
      template: 'public/index.html',
      filename: 'myProducts.html',
      title: '我的发布'
    },
    settings: {
      entry: 'src/pages/settings/main.js',
      template: 'public/index.html',
      filename: 'settings.html',
      title: '设置'
    },
    cart: {
      entry: 'src/pages/cart/main.js',
      template: 'public/index.html',
      filename: 'cart.html',
      title: '购物车'
    },
    websocketTest: {
      entry: 'src/pages/websocket-test/main.js',
      template: 'public/index.html',
      filename: 'websocket-test.html',
      title: 'WebSocket测试'
    }
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src/pages/douyin'),
        '@root': path.resolve(__dirname, 'src'),
        '@douyin': path.resolve(__dirname, 'src/pages/douyin')
      },
      fallback: {
        'net': false,
        'tls': false,
        'fs': false
      }
    },
    plugins: [
      {
        apply: (compiler) => {
          compiler.hooks.afterEmit.tap('GenerateRouteConfig', () => {
            const routes = [
              { path: '/', file: 'index.html', title: '抖音' },
              { path: '/index', file: 'index.html', title: '抖音' },
              { path: '/original-index', file: 'original-index.html', title: '首页' },
              { path: '/page1', file: 'page1.html', title: '页面1' },
              { path: '/page2', file: 'page2.html', title: '页面2' },
              { path: '/login', file: 'login.html', title: '登录' },
              { path: '/register', file: 'register.html', title: '注册' },
              { path: '/categories', file: 'categories.html', title: '分类' },
              { path: '/address', file: 'address.html', title: '收货地址' },
              { path: '/detail', file: 'detail.html', title: '商品详情' },
              { path: '/publish', file: 'publish.html', title: '发布商品' },
              { path: '/profile', file: 'profile.html', title: '个人中心' },
              { path: '/search', file: 'search.html', title: '搜索' },
              { path: '/orders', file: 'orders.html', title: '我的订单' },
              { path: '/orderDetail', file: 'orderDetail.html', title: '订单详情' },
              { path: '/favorites', file: 'favorites.html', title: '我的收藏' },
              { path: '/messages', file: 'messages.html', title: '消息中心' },
              { path: '/chat', file: 'chat.html', title: '聊天' },
              { path: '/myProducts', file: 'myProducts.html', title: '我的发布' },
              { path: '/settings', file: 'settings.html', title: '设置' },
              { path: '/cart', file: 'cart.html', title: '购物车' },
              { path: '/websocket-test', file: 'websocket-test.html', title: 'WebSocket测试' }
            ]

            const routeConfigPath = path.join(compiler.outputPath, 'routes.json')
            fs.writeFileSync(routeConfigPath, JSON.stringify(routes, null, 2))
            console.log(`Generated route config at: ${routeConfigPath}`)
          })
        }
      },
      {
        apply: (compiler) => {
          compiler.hooks.afterEmit.tap('CopyToAssets', () => {
            // 目标文件夹路径
            const targetDir = path.resolve(__dirname, '../pros/app/src/main/assets')
            // 打包输出文件夹
            const outputDir = compiler.outputPath

            console.log(`\n=== 开始复制打包文件到 assets 目录 ===`)
            console.log(`目标目录: ${targetDir}`)
            console.log(`打包目录: ${outputDir}`)

            try {
              // 1. 清空目标目录
              emptyDir(targetDir)
              console.log(`✓ 已清空目标目录`)

              // 2. 复制打包文件到目标目录
              copyDir(outputDir, targetDir)
              console.log(`✓ 已成功复制所有打包文件`)
              console.log(`=== 复制完成 ===\n`)
            } catch (error) {
              console.error(`✗ 复制文件失败: ${error.message}`)
              console.log(`=== 复制失败 ===\n`)
            }
          })
        }
      }
    ]
  }
})
