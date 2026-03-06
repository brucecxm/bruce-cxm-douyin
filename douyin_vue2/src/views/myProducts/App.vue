<template>
  <div class="my-products-page">
    <!-- 顶部导航 -->
    <header class="products-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">我发布的商品</div>
      <div class="header-right" @click="goToPublish">
        <i class="publish-icon">📤</i>
      </div>
    </header>

    <!-- 商品状态筛选 -->
    <div class="product-tabs">
      <div class="tab-item active" @click="switchTab('all')">全部</div>
      <div class="tab-item" @click="switchTab('onsale')">在售</div>
      <div class="tab-item" @click="switchTab('sold')">已卖出</div>
      <div class="tab-item" @click="switchTab('offline')">已下架</div>
    </div>

    <!-- 商品列表 -->
    <div class="products-list">
      <div class="product-item" v-for="product in products" :key="product.id">
        <div class="product-image" @click="goToDetail(product.id)">
          <img :src="product.image" :alt="product.title" />
          <div class="product-status-tag" :class="product.status">
            {{ product.statusText }}
          </div>
        </div>
        <div class="product-info" @click="goToDetail(product.id)">
          <div class="product-title">{{ product.title }}</div>
          <div class="product-stats">
            <span class="stat-item">👁️ {{ product.views }}</span>
            <span class="stat-item">💬 {{ product.chats }}</span>
            <span class="stat-item">❤️ {{ product.favorites }}</span>
          </div>
          <div class="product-price">¥{{ product.price }}</div>
          <div class="product-time">{{ product.publishTime }}</div>
        </div>
        <div class="product-actions">
          <button
            class="action-btn primary"
            v-if="product.status === 'onsale'"
            @click.stop="editProduct(product.id)"
          >
            编辑
          </button>
          <button
            class="action-btn secondary"
            v-if="product.status === 'onsale'"
            @click.stop="offlineProduct(product.id)"
          >
            下架
          </button>
          <button
            class="action-btn primary"
            v-if="product.status === 'offline'"
            @click.stop="onlineProduct(product.id)"
          >
            上架
          </button>
          <button
            class="action-btn secondary"
            v-if="product.status === 'offline'"
            @click.stop="deleteProduct(product.id)"
          >
            删除
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';
import { get } from '../../utils/apiUtils';

export default {
  name: 'MyProductsPage',
  data() {
    return {
      activeTab: 'all',
      products: [],
      originalProducts: [] // 用于筛选的原始数据
    };
  },
  mounted() {
    // 设置页面标题
    nativeUtils.setTitleBar('我发布的商品');
    // 加载商品数据
    this.loadProducts();
  },
  methods: {
    // 从API或localStorage加载商品数据
    loadProducts() {
      try {
        nativeUtils.showLoading('加载中...');

        // 获取当前用户信息
        const userInfo = localStorage.getItem('userInfo');
        if (userInfo) {
          const user = JSON.parse(userInfo);
          const userId = user.id;

          // 尝试从API获取数据
          get(`/newshop/product/user/${userId}`)
            .then((response) => {
              if (response.code === 0) {
                // API请求成功，转换数据格式以适配前端
                this.originalProducts = response.data.map((product) => ({
                  id: product.id,
                  title: product.title,
                  price: product.price,
                  publishTime: product.createdAt,
                  image:
                    product.images && product.images.length > 0
                      ? product.images[0]
                      : 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200',
                  views: Math.floor(Math.random() * 200) + 10,
                  chats: Math.floor(Math.random() * 30) + 5,
                  favorites: Math.floor(Math.random() * 20) + 2,
                  status: product.status || 'onsale',
                  statusText:
                    product.status === 'onsale'
                      ? '在售'
                      : product.status === 'sold'
                        ? '已卖出'
                        : '已下架'
                }));
                this.applyFilter();
              } else {
                // API请求失败，使用localStorage数据
                this.loadFromLocalStorage();
              }
              nativeUtils.hideLoading();
            })
            .catch((error) => {
              console.error('加载商品失败:', error);
              // API请求失败，使用localStorage数据
              this.loadFromLocalStorage();
              nativeUtils.hideLoading();
            });
        } else {
          // 用户未登录，跳转登录页
          nativeUtils.hideLoading();
          nativeUtils.showToast('请先登录', 1500);
          setTimeout(() => {
            routerUtils.navigateTo('/login.html');
          }, 1500);
        }
      } catch (error) {
        console.error('加载商品失败:', error);
        this.loadFromLocalStorage();
        nativeUtils.hideLoading();
      }
    },

    // 从localStorage加载商品数据
    loadFromLocalStorage() {
      try {
        const productsStr = localStorage.getItem('myProducts');
        if (productsStr) {
          this.originalProducts = JSON.parse(productsStr);
        } else {
          // 如果没有数据，使用默认数据
          this.originalProducts = [
            {
              id: 1,
              title: 'iPhone 13 Pro 256GB 远峰蓝',
              price: 5999,
              publishTime: '2025-12-25 10:30',
              image: 'https://via.placeholder.com/200x200?text=iPhone+13+Pro',
              views: 128,
              chats: 15,
              favorites: 8,
              status: 'onsale',
              statusText: '在售'
            },
            {
              id: 2,
              title: 'MacBook Pro 16英寸 M1 Pro',
              price: 12999,
              publishTime: '2025-12-24 15:20',
              image: 'https://via.placeholder.com/200x200?text=MacBook+Pro',
              views: 89,
              chats: 8,
              favorites: 5,
              status: 'sold',
              statusText: '已卖出'
            },
            {
              id: 3,
              title: 'AirPods Pro 第二代',
              price: 1899,
              publishTime: '2025-12-23 09:15',
              image: 'https://via.placeholder.com/200x200?text=AirPods+Pro',
              views: 215,
              chats: 23,
              favorites: 12,
              status: 'onsale',
              statusText: '在售'
            },
            {
              id: 4,
              title: '索尼 WH-1000XM5 降噪耳机',
              price: 2599,
              publishTime: '2025-12-20 14:45',
              image: 'https://via.placeholder.com/200x200?text=Sony+WH-1000XM5',
              views: 156,
              chats: 12,
              favorites: 7,
              status: 'offline',
              statusText: '已下架'
            }
          ];
          // 保存到localStorage
          localStorage.setItem(
            'myProducts',
            JSON.stringify(this.originalProducts)
          );
        }
        this.applyFilter();
      } catch (error) {
        console.error('加载本地商品数据失败:', error);
        this.originalProducts = [];
        this.products = [];
      }
    },

    // 应用筛选条件
    applyFilter() {
      let filtered = [...this.originalProducts];

      switch (this.activeTab) {
        case 'onsale':
          filtered = filtered.filter((product) => product.status === 'onsale');
          break;
        case 'sold':
          filtered = filtered.filter((product) => product.status === 'sold');
          break;
        case 'offline':
          filtered = filtered.filter((product) => product.status === 'offline');
          break;
        default:
          // 全部
          break;
      }

      this.products = filtered;
    },

    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1);
    },

    // 跳转到发布页面
    goToPublish() {
      routerUtils.navigateTo('/publish.html');
    },

    // 切换筛选标签
    switchTab(tab) {
      this.activeTab = tab;
      this.applyFilter();
    },

    // 跳转到商品详情页
    goToDetail(productId) {
      routerUtils.navigateTo(`/detail?id=${productId}`);
    },

    // 编辑商品
    editProduct(productId) {
      routerUtils.navigateTo(`/publish.html?id=${productId}`);
    },

    // 下架商品
    offlineProduct(productId) {
      if (confirm('确定要下架这个商品吗？')) {
        this.updateProductStatus(productId, 'offline');
      }
    },

    // 上架商品
    onlineProduct(productId) {
      if (confirm('确定要上架这个商品吗？')) {
        this.updateProductStatus(productId, 'onsale');
      }
    },

    // 删除商品
    deleteProduct(productId) {
      if (confirm('确定要删除这个商品吗？删除后不可恢复。')) {
        // 从原始数据中删除
        const index = this.originalProducts.findIndex(
          (item) => item.id === productId
        );
        if (index > -1) {
          this.originalProducts.splice(index, 1);
          // 保存到localStorage
          localStorage.setItem(
            'myProducts',
            JSON.stringify(this.originalProducts)
          );
          // 重新应用筛选
          this.applyFilter();
          nativeUtils.showToast('商品已删除', 1500);
        }
      }
    },

    // 更新商品状态
    updateProductStatus(productId, status) {
      try {
        // 这里应该调用API更新状态
        // 模拟API请求
        setTimeout(() => {
          // 更新本地数据
          const product = this.originalProducts.find(
            (item) => item.id === productId
          );
          if (product) {
            product.status = status;
            product.statusText =
              status === 'onsale'
                ? '在售'
                : status === 'sold'
                  ? '已卖出'
                  : '已下架';
            // 保存到localStorage
            localStorage.setItem(
              'myProducts',
              JSON.stringify(this.originalProducts)
            );
            // 重新应用筛选
            this.applyFilter();
            nativeUtils.showToast('商品状态已更新', 1500);
          }
        }, 500);
      } catch (error) {
        console.error('更新商品状态失败:', error);
        nativeUtils.showToast('操作失败，请重试', 1500);
      }
    }
  }
};
</script>

<style scoped>
.my-products-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 顶部导航 */
.products-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-icon,
.publish-icon {
  font-size: 20px;
  color: #333;
  cursor: pointer;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

/* 商品状态筛选 */
.product-tabs {
  display: flex;
  background-color: white;
  padding: 15px 0;
  overflow-x: auto;
  border-bottom: 1px solid #eee;
}

.tab-item {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #666;
  padding: 0 10px;
  cursor: pointer;
  white-space: nowrap;
}

.tab-item.active {
  color: #42b983;
  font-weight: bold;
  position: relative;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -16px;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  background-color: #42b983;
}

/* 商品列表 */
.products-list {
  padding: 10px;
}

.product-item {
  background-color: white;
  border-radius: 10px;
  margin-bottom: 15px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding: 15px;
  display: flex;
  flex-direction: column;
}

/* 商品图片 */
.product-image {
  position: relative;
  margin-bottom: 15px;
  cursor: pointer;
}

.product-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 10px;
}

/* 商品状态标签 */
.product-status-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: bold;
  color: white;
}

.product-status-tag.onsale {
  background-color: #42b983;
}

.product-status-tag.sold {
  background-color: #999;
}

.product-status-tag.offline {
  background-color: #ff4444;
}

/* 商品信息 */
.product-info {
  margin-bottom: 15px;
  cursor: pointer;
}

.product-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-stats {
  display: flex;
  margin-bottom: 10px;
}

.stat-item {
  font-size: 12px;
  color: #999;
  margin-right: 15px;
}

.product-price {
  font-size: 18px;
  font-weight: bold;
  color: #ff4444;
  margin-bottom: 5px;
}

.product-time {
  font-size: 12px;
  color: #ccc;
}

/* 商品操作 */
.product-actions {
  display: flex;
  justify-content: flex-end;
}

.action-btn {
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  margin-left: 10px;
  border: 1px solid #ddd;
  background-color: white;
}

.action-btn.primary {
  background-color: #42b983;
  color: white;
  border-color: #42b983;
}

.action-btn.secondary {
  color: #666;
}
</style>
