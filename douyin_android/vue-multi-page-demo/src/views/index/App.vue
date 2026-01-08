<template>
  <div class="second-hand-app">
    <!-- 顶部搜索栏 -->
    <header class="header">
      <div class="search-bar" @click="goToSearch">
        <i class="search-icon">🔍</i>
        <span class="search-placeholder">搜索你想要的商品</span>
      </div>
    </header>

    <!-- 分类导航 -->
    <div class="category-nav">
      <div class="category-item" v-for="category in categories" :key="category.id" @click="filterByCategory(category.id)">
        <div class="category-icon">{{ category.icon }}</div>
        <div class="category-name">{{ category.name }}</div>
      </div>
    </div>

    <!-- 商品列表 -->
    <main class="product-list">
      <div class="product-item" v-for="product in products" :key="product.id" @click="goToDetail(product.id)">
        <div class="product-image">
          <img :src="product.image" :alt="product.title">
        </div>
        <div class="product-info">
          <h3 class="product-title">{{ product.title }}</h3>
          <p class="product-price">¥{{ product.price }}</p>
          <p class="product-location">{{ product.location }}</p>
          <p class="product-time">{{ product.time }}</p>
        </div>
      </div>
    </main>

    <!-- 底部导航栏 -->
    <footer class="bottom-nav">
      <div class="nav-item active" @click="goToHome">
        <i class="nav-icon">🏠</i>
        <span class="nav-text">首页</span>
      </div>
      <div class="nav-item" @click="goToCategories">
        <i class="nav-icon">📋</i>
        <span class="nav-text">分类</span>
      </div>
      <div class="nav-item publish-btn" @click="goToPublish">
        <i class="nav-icon">+</i>
      </div>
      <div class="nav-item" @click="goToMessages">
        <i class="nav-icon">💬</i>
        <span class="nav-text">消息</span>
      </div>
      <div class="nav-item" @click="goToProfile">
        <i class="nav-icon">👤</i>
        <span class="nav-text">我的</span>
      </div>
    </footer>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils'
import routerUtils from '../../utils/routerUtils'
import { get } from '../../utils/apiUtils'

export default {
  name: 'HomePage',
  data() {
    return {
      // 静态分类数据
      categories: [
        { id: 1, name: '手机数码', icon: '📱' },
        { id: 2, name: '电脑办公', icon: '💻' },
        { id: 3, name: '家用电器', icon: '📺' },
        { id: 4, name: '服装鞋包', icon: '👔' },
        { id: 5, name: '美妆护肤', icon: '💄' },
        { id: 6, name: '运动户外', icon: '🏃' },
        { id: 7, name: '图书音像', icon: '📚' },
        { id: 8, name: '其他', icon: '📦' }
      ],
      // 动态商品数据
      products: []
    }
  },
  mounted() {
    // 页面加载时设置标题
    nativeUtils.setTitleBar('二手交易 - 首页')
    // 调用API获取商品列表
    this.fetchProducts()
  },
  methods: {
    // 获取商品列表API
    async fetchProducts() {
      try {
        // 显示加载提示
        nativeUtils.showLoading('加载中...')
        
        // 调用真实API获取商品列表
        const response = await get('/product')
        
        // 检查API响应
        if (response.code === 0 && response.data) {
          // API请求成功
          this.products = response.data.map(product => ({
            id: product.id,
            title: product.title,
            price: product.price,
            location: product.location || '未知',
            time: product.createdAt ? this.formatTime(product.createdAt) : '未知',
            image: product.images && product.images.length > 0 ? product.images[0] : 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
          }))
        } else {
          // API返回数据格式不正确，使用mock数据
          console.warn('API返回数据格式不正确，使用mock数据')
          this.loadMockProducts()
        }
      } catch (error) {
        console.error('获取商品列表失败:', error)
        // API请求失败，使用mock数据
        this.loadMockProducts()
      } finally {
        // 隐藏加载提示
        nativeUtils.hideLoading()
      }
    },
    
    // 加载mock商品数据
    loadMockProducts() {
      // 尝试从localStorage获取数据
      const localStorageData = localStorage.getItem('products')
      if (localStorageData) {
        try {
          const products = JSON.parse(localStorageData)
          this.products = products
          nativeUtils.showToast('使用本地缓存数据', 1500)
          return
        } catch (error) {
          console.error('解析本地数据失败:', error)
        }
      }
      
      // 使用默认mock数据
      this.products = [
        {
          id: 1,
          title: 'iPhone 13 Pro 256GB 远峰蓝',
          price: 5999,
          location: '北京 朝阳区',
          time: '2小时前',
          image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 2,
          title: 'MacBook Pro 16英寸 M1 Pro',
          price: 12999,
          location: '上海 浦东新区',
          time: '5小时前',
          image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 3,
          title: 'AirPods Pro 第二代',
          price: 1899,
          location: '广州 天河区',
          time: '1天前',
          image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 4,
          title: '索尼 WH-1000XM5 降噪耳机',
          price: 2599,
          location: '深圳 南山区',
          time: '2天前',
          image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 5,
          title: '小米13 12GB+256GB',
          price: 3999,
          location: '杭州 西湖区',
          time: '3天前',
          image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 6,
          title: '华为Mate50 Pro 昆仑玻璃',
          price: 6999,
          location: '成都 高新区',
          time: '5天前',
          image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        }
      ]
      
      // 保存到localStorage
      try {
        localStorage.setItem('products', JSON.stringify(this.products))
      } catch (error) {
        console.error('保存mock数据到localStorage失败:', error)
      }
      
      nativeUtils.showToast('使用默认数据', 1500)
    },
    // 格式化时间
    formatTime(timeString) {
      const date = new Date(timeString)
      const now = new Date()
      const diff = now - date
      
      const minutes = Math.floor(diff / (1000 * 60))
      const hours = Math.floor(diff / (1000 * 60 * 60))
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))
      
      if (minutes < 60) {
        return `${minutes}分钟前`
      } else if (hours < 24) {
        return `${hours}小时前`
      } else {
        return `${days}天前`
      }
    },
    // 跳转到搜索页
    goToSearch() {
      routerUtils.navigateTo('/search.html')
    },
    // 跳转到商品详情页
    goToDetail(productId) {
      routerUtils.navigateTo(`/detail.html?id=${productId}`)
    },
    // 跳转到发布页
    goToPublish() {
      routerUtils.navigateTo('/publish.html')
    },
    // 跳转到个人中心
    goToProfile() {
      routerUtils.navigateTo('/profile.html')
    },
    // 跳转到首页
    goToHome() {
      routerUtils.navigateTo('/index.html')
    },
    // 跳转到分类页
    goToCategories() {
      console.log('跳转到分类页')
      // 实际项目中跳转到分类页面
    },
    // 跳转到消息页
    goToMessages() {
      routerUtils.navigateTo('/messages.html')
    },
    // 按分类筛选商品
    async filterByCategory(categoryId) {
      console.log('筛选分类:', categoryId)
      try {
        // 显示加载提示
        nativeUtils.showLoading('加载中...')
        
        // 调用API获取对应分类的商品
        const categoryName = this.categories.find(cat => cat.id === categoryId).name
        const response = await get(`/product/category/${categoryName}`)
        
        // 检查API响应
        if (response.code === 0 && response.data) {
          // API请求成功
          this.products = response.data.map(product => ({
            id: product.id,
            title: product.title,
            price: product.price,
            location: product.location || '未知',
            time: product.createdAt ? this.formatTime(product.createdAt) : '未知',
            image: product.images && product.images.length > 0 ? product.images[0] : 'https://via.placeholder.com/200x200?text=商品图片'
          }))
        } else {
          // API返回数据格式不正确
          console.warn('API返回数据格式不正确')
          nativeUtils.showToast('获取分类商品失败', 1500)
        }
      } catch (error) {
        console.error('获取分类商品失败:', error)
        nativeUtils.showToast('获取分类商品失败', 1500)
      } finally {
        // 隐藏加载提示
        nativeUtils.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.second-hand-app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部搜索栏 */
.header {
  background-color: #42b983;
  padding: 10px;
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-bar {
  display: flex;
  align-items: center;
  background-color: white;
  border-radius: 20px;
  padding: 8px 15px;
  cursor: pointer;
}

.search-icon {
  font-size: 16px;
  margin-right: 8px;
  color: #999;
}

.search-placeholder {
  font-size: 14px;
  color: #999;
}

/* 分类导航 */
.category-nav {
  display: flex;
  overflow-x: auto;
  background-color: white;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 15px;
  min-width: 60px;
}

.category-icon {
  font-size: 28px;
  margin-bottom: 5px;
}

.category-name {
  font-size: 12px;
  color: #333;
}

/* 商品列表 */
.product-list {
  flex: 1;
  padding: 10px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.product-item {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s;
}

.product-item:hover {
  transform: translateY(-2px);
}

.product-image {
  width: 100%;
  height: 150px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 10px;
}

.product-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price {
  font-size: 16px;
  color: #ff4444;
  font-weight: bold;
  margin-bottom: 3px;
}

.product-location {
  font-size: 12px;
  color: #999;
  margin-bottom: 2px;
}

.product-time {
  font-size: 11px;
  color: #ccc;
}

/* 底部导航栏 */
.bottom-nav {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background-color: white;
  border-top: 1px solid #eee;
  padding: 10px 0;
  position: sticky;
  bottom: 0;
  height: 60px;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 5px;
  color: #999;
  flex: 1;
}

.nav-item.active {
  color: #ff4400;
}

.nav-icon {
  font-size: 24px;
  margin-bottom: 3px;
}

.nav-text {
  font-size: 12px;
}

/* 发布按钮样式 */
.publish-btn {
  position: relative;
  top: -20px;
  width: 50px;
  height: 50px;
  background-color: #ff4400;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 2px 8px rgba(255, 68, 0, 0.3);
  flex: 0;
}

.publish-btn .nav-icon {
  font-size: 28px;
  margin-bottom: 0;
}

.publish-btn:hover {
  background-color: #ff5500;
  transform: scale(1.05);
  transition: all 0.2s ease;
}
</style>
