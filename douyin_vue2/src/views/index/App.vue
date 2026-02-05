<template>
  <div class="second-hand-app">
    <!-- 顶部搜索栏 -->
    <header class="header">
      <div class="search-bar" @click="goToSearch">
        <i class="search-icon-left">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M10 10m-7 0a7 7 0 1 0 14 0a7 7 0 1 0 -14 0" />
            <path d="M21 21l-6 -6" />
          </svg>
        </i>
        <span class="search-placeholder">搜索你想要的商品</span>
        <i class="search-icon-right">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M6 19m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" />
            <path d="M17 19m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" />
            <path d="M17 17h-11v-14h-2" />
            <path d="M6 5l14 1l-1 7h-13" />
          </svg>
        </i>
        <span class="search-button">搜索</span>
      </div>
      <i class="search-icon-father">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path
            d="M5 7h1a2 2 0 0 0 2 -2a1 1 0 0 1 1 -1h6a1 1 0 0 1 1 1a2 2 0 0 0 2 2h1a2 2 0 0 1 2 2v9a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2v-9a2 2 0 0 1 2 -2"
          />
          <path d="M9 13a4 4 0 1 0 8 0a4 4 0 0 0 -8 0" />
        </svg>
      </i>
    </header>

    <!-- 顶部菜单栏 -->
    <div class="top-menu">
      <div class="menu-item active" @click="goToHome">
        <i class="menu-icon">🏠</i>
        <span class="menu-text">首页</span>
      </div>
      <div class="menu-item" @click="goToCategories">
        <i class="menu-icon">📋</i>
        <span class="menu-text">分类</span>
      </div>
      <div class="menu-item" @click="goToPublish">
        <i class="menu-icon">+</i>
        <span class="menu-text">发布</span>
      </div>
      <div class="menu-item" @click="goToMessages">
        <i class="menu-icon">💬</i>
        <span class="menu-text">消息</span>
      </div>
      <div class="menu-item" @click="goToProfile">
        <i class="menu-icon">👤</i>
        <span class="menu-text">我的</span>
      </div>
    </div>

    <!-- 分类导航 -->
    <div class="category-nav">
      <div
        class="category-item"
        v-for="category in categories"
        :key="category.id"
        @click="filterByCategory(category.id)"
      >
        <div class="category-icon">{{ category.icon }}</div>
        <div class="category-name">{{ category.name }}</div>
      </div>
    </div>

    <!-- 商品列表 -->
    <main class="product-list">
      <div
        class="product-item"
        v-for="product in products"
        :key="product.id"
        @click="goToDetail(product.id)"
      >
        <div class="product-image">
          <img :src="product.image" :alt="product.title" />
        </div>
        <div class="product-info">
          <h3 class="product-title">{{ product.title }}</h3>
          <p class="product-price">¥{{ product.price }}</p>
          <p class="product-location">{{ product.location }}</p>
          <p class="product-time">{{ product.time }}</p>
        </div>
      </div>
    </main>

    <!-- 底部导航 -->
    <footer-vue></footer-vue>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';
import { get } from '../../utils/apiUtils';
import footerVue from '../../components/footer.vue';

export default {
  name: 'HomePage',
  components: {
    footerVue
  },
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
    };
  },
  mounted() {
    // 页面加载时设置标题
    nativeUtils.setTitleBar('二手交易 - 首页');
    // 调用API获取商品列表
    this.fetchProducts();
  },
  methods: {
    // 获取商品列表API
    async fetchProducts() {
      try {
        // 显示加载提示
        nativeUtils.showLoading('加载中...');

        // 调用真实API获取商品列表
        const response = await get('/newshop/product');

        // 检查API响应
        if (response.code === 0 && response.data) {
          // API请求成功
          this.products = response.data.map((product) => ({
            id: product.id,
            title: product.title,
            price: product.price,
            location: product.location || '未知',
            time: product.createdAt
              ? this.formatTime(product.createdAt)
              : '未知',
            image:
              product.images && product.images.length > 0
                ? product.images[0]
                : 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
          }));
        } else {
          // API返回数据格式不正确，使用mock数据
          console.warn('API返回数据格式不正确，使用mock数据');
          this.loadMockProducts();
        }
      } catch (error) {
        console.error('获取商品列表失败:', error);
        // API请求失败，使用mock数据
        this.loadMockProducts();
      } finally {
        // 隐藏加载提示
        nativeUtils.hideLoading();
      }
    },

    // 加载mock商品数据
    loadMockProducts() {
      // 尝试从localStorage获取数据
      const localStorageData = localStorage.getItem('products');
      if (localStorageData) {
        try {
          const products = JSON.parse(localStorageData);
          this.products = products;
          nativeUtils.showToast('使用本地缓存数据', 1500);
          return;
        } catch (error) {
          console.error('解析本地数据失败:', error);
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
          image:
            'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 2,
          title: 'MacBook Pro 16英寸 M1 Pro',
          price: 12999,
          location: '上海 浦东新区',
          time: '5小时前',
          image:
            'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 3,
          title: 'AirPods Pro 第二代',
          price: 1899,
          location: '广州 天河区',
          time: '1天前',
          image:
            'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 4,
          title: '索尼 WH-1000XM5 降噪耳机',
          price: 2599,
          location: '深圳 南山区',
          time: '2天前',
          image:
            'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 5,
          title: '小米13 12GB+256GB',
          price: 3999,
          location: '杭州 西湖区',
          time: '3天前',
          image:
            'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        },
        {
          id: 6,
          title: '华为Mate50 Pro 昆仑玻璃',
          price: 6999,
          location: '成都 高新区',
          time: '5天前',
          image:
            'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
        }
      ];

      // 保存到localStorage
      try {
        localStorage.setItem('products', JSON.stringify(this.products));
      } catch (error) {
        console.error('保存mock数据到localStorage失败:', error);
      }

      nativeUtils.showToast('使用默认数据', 1500);
    },
    // 格式化时间
    formatTime(timeString) {
      const date = new Date(timeString);
      const now = new Date();
      const diff = now - date;

      const minutes = Math.floor(diff / (1000 * 60));
      const hours = Math.floor(diff / (1000 * 60 * 60));
      const days = Math.floor(diff / (1000 * 60 * 60 * 24));

      if (minutes < 60) {
        return `${minutes}分钟前`;
      } else if (hours < 24) {
        return `${hours}小时前`;
      } else {
        return `${days}天前`;
      }
    },
    // 跳转到搜索页
    goToSearch() {
      routerUtils.navigateTo('/newshop/search.html');
    },
    // 跳转到商品详情页
    goToDetail(productId) {
      routerUtils.navigateTo(`/newshop/detail?id=${productId}`);
    },
    // 跳转到发布页
    goToPublish() {
      routerUtils.navigateTo('/newshop/publish.html');
    },
    // 跳转到个人中心
    goToProfile() {
      routerUtils.navigateTo('/newshop/profile.html');
    },
    // 跳转到首页
    goToHome() {
      routerUtils.navigateTo('/newshop');
    },
    // 跳转到分类页
    goToCategories() {
      console.log('跳转到分类页');
      // 实际项目中跳转到分类页面
    },
    // 跳转到消息页
    goToMessages() {
      routerUtils.navigateTo('/newshop/messages.html');
    },
    // 按分类筛选商品
    async filterByCategory(categoryId) {
      console.log('筛选分类:', categoryId);
      try {
        // 显示加载提示
        nativeUtils.showLoading('加载中...');

        // 调用API获取对应分类的商品
        const categoryName = this.categories.find(
          (cat) => cat.id === categoryId
        ).name;
        const response = await get(`/newshop/product/category/${categoryName}`);

        // 检查API响应
        if (response.code === 0 && response.data) {
          // API请求成功
          this.products = response.data.map((product) => ({
            id: product.id,
            title: product.title,
            price: product.price,
            location: product.location || '未知',
            time: product.createdAt
              ? this.formatTime(product.createdAt)
              : '未知',
            image:
              product.images && product.images.length > 0
                ? product.images[0]
                : 'https://via.placeholder.com/200x200?text=商品图片'
          }));
        } else {
          // API返回数据格式不正确
          console.warn('API返回数据格式不正确');
          nativeUtils.showToast('获取分类商品失败', 1500);
        }
      } catch (error) {
        console.error('获取分类商品失败:', error);
        nativeUtils.showToast('获取分类商品失败', 1500);
      } finally {
        // 隐藏加载提示
        nativeUtils.hideLoading();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
// 变量定义
$primary-color: #42b983;
$text-primary: #333;
$text-secondary: #666;
$text-light: #999;
$text-lighter: #ccc;
$border-color: #eee;
$danger-color: #ff4444;
$background-color: #f5f5f5;
$white: #fff;

.second-hand-app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: $background-color;

  // 顶部搜索栏
  .header {
    background-color: $primary-color;
    padding: 10px;
    position: sticky;
    display: flex;
    justify-content: space-between;
    top: 0;
    z-index: 100;

    .search-icon-father {
      margin-left: 0px;
    }
    .search-bar {
      display: flex;
      align-items: center;
      background-color: $white;
      flex: 1;
      border-radius: 20px;
      padding: 8px 15px;
      cursor: pointer;
      justify-content: space-between;

      .search-icon-left {
        font-size: 16px;
        margin-right: 8px;
        color: $text-light;
      }

      .search-icon-right {
        font-size: 16px;
        margin-right: 8px;
        color: $text-light;
      }

      .search-placeholder {
        font-size: 14px;
        color: $text-light;
        flex: 1;
      }
      .search-button {
        height: 100%;
        width: 50px;
        background-color: red;
        text-align: center;
        border-radius: 20px;
        font-size: 15px;
        color: $white;
      }
    }
  }

  // 顶部菜单栏
  .top-menu {
    display: flex;
    justify-content: space-around;
    align-items: center;
    background-color: $white;
    border-bottom: 1px solid $border-color;
    padding: 10px 0;
    position: sticky;
    top: 54px;
    z-index: 90;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

    .menu-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      cursor: pointer;
      padding: 8px 15px;
      color: $text-secondary;
      transition: all 0.2s ease;
      flex: 1;
      max-width: 80px;

      &:hover {
        color: $primary-color;
        background-color: rgba($primary-color, 0.05);
        border-radius: 8px;
      }

      &.active {
        color: $primary-color;
        font-weight: bold;
      }

      .menu-icon {
        font-size: 20px;
        margin-bottom: 4px;
      }

      .menu-text {
        font-size: 12px;
        line-height: 1.2;
      }
    }
  }

  // 分类导航
  .category-nav {
    display: flex;
    overflow-x: auto;
    background-color: $white;
    padding: 15px 0;
    border-bottom: 1px solid $border-color;

    .category-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      margin: 0 15px;
      min-width: 60px;

      .category-icon {
        font-size: 28px;
        margin-bottom: 5px;
      }

      .category-name {
        font-size: 12px;
        color: $text-primary;
      }
    }
  }

  // 商品列表
  .product-list {
    flex: 1;
    padding: 10px;
    padding-bottom: 70px;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;

    .product-item {
      background-color: $white;
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: transform 0.2s;

      &:hover {
        transform: translateY(-2px);
      }

      .product-image {
        width: 100%;
        height: 150px;
        overflow: hidden;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }

      .product-info {
        padding: 10px;

        .product-title {
          font-size: 14px;
          font-weight: bold;
          color: $text-primary;
          margin-bottom: 5px;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .product-price {
          font-size: 16px;
          color: $danger-color;
          font-weight: bold;
          margin-bottom: 3px;
        }

        .product-location {
          font-size: 12px;
          color: $text-light;
          margin-bottom: 2px;
        }

        .product-time {
          font-size: 11px;
          color: $text-lighter;
        }
      }
    }
  }
}
</style>
