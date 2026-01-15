<template>
  <div class="favorites-page">
    <!-- 顶部导航 -->
    <header class="favorites-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">我的收藏</div>
      <div
        class="header-right"
        @click="manageFavorites"
        v-if="activeFilter !== 'recent'"
      >
        <span class="manage-btn">管理</span>
      </div>
    </header>

    <!-- 筛选标签 -->
    <div class="filter-tabs">
      <div class="tab-item active" @click="switchFilter('all')">全部</div>
      <div class="tab-item" @click="switchFilter('recent')">最近浏览</div>
      <div class="tab-item" @click="switchFilter('price_asc')">
        价格从低到高
      </div>
      <div class="tab-item" @click="switchFilter('price_desc')">
        价格从高到低
      </div>
    </div>

    <!-- 收藏商品列表 -->
    <div class="favorites-list">
      <div class="product-item" v-for="product in favorites" :key="product.id">
        <div class="product-image" @click="goToDetail(product.id)">
          <img :src="product.image" :alt="product.title" />
          <div
            class="favorite-btn"
            :class="{ active: activeFilter !== 'recent' }"
            @click.stop="toggleFavorite(product.id)"
          >
            {{ activeFilter === 'recent' ? '🤍' : '❤️' }}
          </div>
        </div>
        <div class="product-info" @click="goToDetail(product.id)">
          <div class="product-title">{{ product.title }}</div>
          <div class="product-desc">{{ product.desc }}</div>
          <div class="product-meta">
            <div class="product-price">¥{{ product.price }}</div>
            <div class="product-time">{{ product.time }}</div>
          </div>
        </div>
        <div
          class="product-actions"
          v-if="isManaging && activeFilter !== 'recent'"
        >
          <div
            class="checkbox"
            :class="{ checked: selectedProducts.includes(product.id) }"
            @click.stop="toggleSelect(product.id)"
          >
            <span v-if="selectedProducts.includes(product.id)">✓</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 管理操作栏 -->
    <footer class="management-bar" v-if="isManaging">
      <div class="select-all">
        <div
          class="checkbox"
          :class="{ checked: isAllSelected }"
          @click="toggleSelectAll"
        >
          <span v-if="isAllSelected">✓</span>
        </div>
        <span class="select-text">全选</span>
      </div>
      <div class="action-buttons">
        <button class="action-btn delete-btn" @click="deleteSelected">
          删除
        </button>
        <button class="action-btn share-btn" @click="shareSelected">
          分享
        </button>
      </div>
    </footer>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';
import { get, post, del } from '../../utils/apiUtils';

export default {
  name: 'FavoritesPage',
  data() {
    return {
      isManaging: false,
      selectedProducts: [],
      activeFilter: 'all',
      favorites: [],
      originalFavorites: [], // 用于筛选的原始数据
      recentViews: [], // 最近浏览记录
      isLoadingRecent: false // 加载状态
    };
  },
  computed: {
    isAllSelected() {
      return (
        this.selectedProducts.length === this.favorites.length &&
        this.favorites.length > 0
      );
    }
  },
  mounted() {
    // 设置页面标题
    nativeUtils.setTitleBar('我的收藏');
    // 加载收藏数据
    this.loadFavorites();
    // 加载最近浏览记录
    this.loadRecentViews();
  },
  methods: {
    // 从API加载收藏数据
    loadFavorites() {
      try {
        nativeUtils.showLoading('加载中...');

        // 获取当前用户信息
        const userInfo = localStorage.getItem('user');
        if (userInfo) {
          const user = JSON.parse(userInfo);
          const userId = user.id;

          // 调用API获取收藏列表
          get(`/favorite/user/${userId}`)
            .then((response) => {
              if (response.code === 0) {
                // API请求成功，转换数据格式
                this.originalFavorites = response.data.map((favorite) => ({
                  id: favorite.product.id,
                  title: favorite.product.title,
                  desc: favorite.product.description || '暂无描述',
                  price: favorite.product.price,
                  time: this.formatTime(favorite.createdAt),
                  image:
                    favorite.product.images &&
                    favorite.product.images.length > 0
                      ? favorite.product.images[0]
                      : 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200',
                  createdAt: new Date(favorite.createdAt).getTime()
                }));
              } else {
                // API请求失败，使用空数组
                this.originalFavorites = [];
              }
              // 初始筛选
              this.applyFilter();
              nativeUtils.hideLoading();
            })
            .catch((error) => {
              console.error('加载收藏数据失败:', error);
              this.originalFavorites = [];
              this.favorites = [];
              nativeUtils.hideLoading();
            });
        } else {
          // 用户未登录，跳转登录页
          nativeUtils.hideLoading();
          nativeUtils.showToast('请先登录', 1500);
          setTimeout(() => {
            routerUtils.navigateTo('/newshop/login.html');
          }, 1500);
        }
      } catch (error) {
        console.error('加载收藏数据失败:', error);
        this.originalFavorites = [];
        this.favorites = [];
        nativeUtils.hideLoading();
      }
    },

    // 加载最近浏览记录
    async loadRecentViews() {
      try {
        this.isLoadingRecent = true;

        // 获取当前用户信息
        const userInfo = localStorage.getItem('user');
        if (!userInfo) {
          this.isLoadingRecent = false;
          return;
        }

        const user = JSON.parse(userInfo);
        const userId = user.id;

        // 调用API获取最近浏览记录
        const response = await get(`/recent-view/${userId}`);

        if (response.code === 0) {
          // API请求成功，转换数据格式
          this.recentViews = response.data.map((view) => ({
            id: view.product.id,
            title: view.product.title,
            desc: view.product.description || '暂无描述',
            price: view.product.price,
            time: this.formatTime(view.viewedAt),
            image:
              view.product.images && view.product.images.length > 0
                ? view.product.images[0]
                : 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200',
            createdAt: new Date(view.viewedAt).getTime()
          }));
        } else {
          // API请求失败，使用空数组
          this.recentViews = [];
        }
      } catch (error) {
        console.error('加载最近浏览记录失败:', error);
        this.recentViews = [];
      } finally {
        this.isLoadingRecent = false;
      }
    },

    // 格式化时间
    formatTime(dateString) {
      const now = new Date();
      const date = new Date(dateString);
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

    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1);
    },

    // 切换管理模式
    manageFavorites() {
      this.isManaging = !this.isManaging;
      this.selectedProducts = [];
    },

    // 切换筛选条件
    switchFilter(filter) {
      this.activeFilter = filter;
      this.applyFilter();
    },

    // 应用筛选条件
    applyFilter() {
      // 如果是最近浏览，直接显示最近浏览记录
      if (this.activeFilter === 'recent') {
        // 确保最近浏览记录已经加载
        if (this.recentViews.length === 0 && !this.isLoadingRecent) {
          this.loadRecentViews();
        }
        this.favorites = [...this.recentViews];
      } else {
        // 否则显示收藏记录，并应用排序
        let filtered = [...this.originalFavorites];

        switch (this.activeFilter) {
          case 'price_asc':
            // 按价格从低到高排序
            filtered.sort((a, b) => a.price - b.price);
            break;
          case 'price_desc':
            // 按价格从高到低排序
            filtered.sort((a, b) => b.price - a.price);
            break;
          default:
            // 全部，按创建时间排序
            filtered.sort((a, b) => b.createdAt - a.createdAt);
        }

        this.favorites = filtered;
      }
    },

    // 切换收藏状态
    toggleFavorite(productId) {
      // 获取当前用户信息
      const userInfo = localStorage.getItem('user');
      if (userInfo) {
        const user = JSON.parse(userInfo);
        const userId = user.id;

        nativeUtils.showLoading('处理中...');

        // 检查当前是否在最近浏览标签下
        if (this.activeFilter === 'recent') {
          // 如果是最近浏览，添加收藏
          post('/favorite', {
            userId: userId,
            productId: productId
          })
            .then((response) => {
              nativeUtils.hideLoading();
              if (response.code === 0) {
                nativeUtils.showToast('收藏成功', 1500);
                // 重新加载收藏列表
                this.loadFavorites();
              } else {
                nativeUtils.showToast(response.message || '收藏失败', 1500);
              }
            })
            .catch((error) => {
              console.error('收藏失败:', error);
              nativeUtils.hideLoading();
              nativeUtils.showToast('收藏失败，请重试', 1500);
            });
        } else {
          // 否则取消收藏
          del(`/favorite?userId=${userId}&productId=${productId}`)
            .then((response) => {
              if (response.code === 0) {
                // 重新加载收藏列表
                this.loadFavorites();
                nativeUtils.showToast('已取消收藏', 1500);
              } else {
                nativeUtils.hideLoading();
                nativeUtils.showToast(response.message || '取消收藏失败', 1500);
              }
            })
            .catch((error) => {
              console.error('取消收藏失败:', error);
              nativeUtils.hideLoading();
              nativeUtils.showToast('取消收藏失败，请重试', 1500);
            });
        }
      }
    },

    // 跳转到商品详情页
    goToDetail(productId) {
      routerUtils.navigateTo(`/detail.html?id=${productId}`);
    },

    // 切换商品选择状态
    toggleSelect(productId) {
      const index = this.selectedProducts.indexOf(productId);
      if (index > -1) {
        this.selectedProducts.splice(index, 1);
      } else {
        this.selectedProducts.push(productId);
      }
    },

    // 切换全选状态
    toggleSelectAll() {
      if (this.isAllSelected) {
        this.selectedProducts = [];
      } else {
        this.selectedProducts = this.favorites.map((product) => product.id);
      }
    },

    // 删除选中的商品
    deleteSelected() {
      if (this.selectedProducts.length === 0) {
        nativeUtils.showToast('请选择要删除的商品', 1500);
        return;
      }

      // 获取当前用户信息
      const userInfo = localStorage.getItem('user');
      if (userInfo) {
        const user = JSON.parse(userInfo);
        const userId = user.id;

        nativeUtils.showLoading('删除中...');

        // 批量删除收藏
        const deletePromises = this.selectedProducts.map((productId) => {
          return del(`/favorite?userId=${userId}&productId=${productId}`);
        });

        Promise.all(deletePromises)
          .then((responses) => {
            // 检查所有请求是否都成功
            const allSuccess = responses.every(
              (response) => response.code === 0
            );
            if (allSuccess) {
              // 重新加载收藏列表
              this.loadFavorites();
              nativeUtils.showToast(
                `删除了${this.selectedProducts.length}个商品`,
                1500
              );
            } else {
              nativeUtils.hideLoading();
              nativeUtils.showToast('部分商品删除失败', 1500);
            }
            this.selectedProducts = [];
            this.isManaging = false;
          })
          .catch((error) => {
            console.error('批量删除收藏失败:', error);
            nativeUtils.hideLoading();
            nativeUtils.showToast('删除失败，请重试', 1500);
          });
      }
    },

    // 分享选中的商品
    shareSelected() {
      if (this.selectedProducts.length === 0) {
        nativeUtils.showToast('请选择要分享的商品', 1500);
        return;
      }

      // 实际开发中可以调用分享API
      nativeUtils.showToast(
        `分享了${this.selectedProducts.length}个商品`,
        1500
      );
      this.selectedProducts = [];
    }
  }
};
</script>

<style scoped>
.favorites-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 顶部导航 */
.favorites-header {
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

.back-icon {
  font-size: 20px;
  color: #333;
  cursor: pointer;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.manage-btn {
  color: #42b983;
  font-size: 16px;
  cursor: pointer;
}

/* 筛选标签 */
.filter-tabs {
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

/* 收藏商品列表 */
.favorites-list {
  padding: 10px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.product-item {
  background-color: white;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative;
}

/* 商品图片 */
.product-image {
  position: relative;
  width: 100%;
  height: 150px;
  overflow: hidden;
  cursor: pointer;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 收藏按钮 */
.favorite-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 24px;
  cursor: pointer;
  transition: transform 0.2s;
  z-index: 10;
}

.favorite-btn.active {
  color: #ff4444;
}

.favorite-btn:hover {
  transform: scale(1.1);
}

/* 商品信息 */
.product-info {
  padding: 10px;
  cursor: pointer;
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
  line-height: 1.4;
}

.product-desc {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-price {
  font-size: 16px;
  font-weight: bold;
  color: #ff4444;
}

.product-time {
  font-size: 11px;
  color: #ccc;
}

/* 管理模式 */
.product-actions {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 10;
}

.checkbox {
  width: 20px;
  height: 20px;
  border: 2px solid #ddd;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  background-color: white;
}

.checkbox.checked {
  background-color: #42b983;
  border-color: #42b983;
  color: white;
}

/* 管理操作栏 */
.management-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  padding: 15px;
  border-top: 1px solid #eee;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
}

.select-all {
  display: flex;
  align-items: center;
}

.select-all .checkbox {
  margin-right: 10px;
}

.select-text {
  font-size: 14px;
  color: #333;
}

.action-buttons {
  display: flex;
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

.delete-btn {
  background-color: #ff4444;
  color: white;
  border-color: #ff4444;
}

.share-btn {
  background-color: #42b983;
  color: white;
  border-color: #42b983;
}
</style>
