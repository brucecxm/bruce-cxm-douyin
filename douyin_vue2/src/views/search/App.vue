<template>
  <div class="search-page">
    <!-- 顶部搜索栏 -->
    <header class="search-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="search-input-group">
        <i class="search-icon">🔍</i>
        <input
          type="text"
          class="search-input"
          v-model="searchQuery"
          placeholder="搜索你想要的商品"
          @input="handleSearchInput"
          @keyup.enter="performSearch"
        />
        <div class="clear-btn" v-if="searchQuery" @click="clearSearch">×</div>
      </div>
      <div class="header-right" @click="performSearch">
        <span class="search-btn">搜索</span>
      </div>
    </header>

    <!-- 搜索内容区域 -->
    <main class="search-content">
      <!-- 搜索历史和热门搜索（无搜索结果时显示） -->
      <div class="search-suggestions" v-if="!searchResults.length">
        <!-- 搜索历史 -->
        <div class="history-section" v-if="searchHistory.length">
          <div class="section-header">
            <h3 class="section-title">搜索历史</h3>
            <div class="clear-history" @click="clearSearchHistory">
              清除历史
            </div>
          </div>
          <div class="history-tags">
            <span
              class="history-tag"
              v-for="item in searchHistory"
              :key="item"
              @click="searchByKeyword(item)"
            >
              {{ item }}
            </span>
          </div>
        </div>

        <!-- 热门搜索 -->
        <div class="hot-section">
          <h3 class="section-title">热门搜索</h3>
          <div class="hot-tags">
            <span
              class="hot-tag"
              v-for="(item, index) in hotSearch"
              :key="item"
              @click="searchByKeyword(item)"
            >
              <span class="tag-rank">{{ index + 1 }}</span>
              {{ item }}
            </span>
          </div>
        </div>
      </div>

      <!-- 搜索结果 -->
      <div class="search-results" v-else>
        <!-- 筛选和排序区域 -->
        <div class="filter-sort-section">
          <!-- 筛选条件 -->
          <div class="filters-section">
            <div class="filter-item">
              <span class="filter-label">价格范围：</span>
              <div class="price-filter">
                <input
                  type="number"
                  class="price-input"
                  v-model.number="priceFilter.min"
                  placeholder="最低价"
                />
                <span class="price-separator">-</span>
                <input
                  type="number"
                  class="price-input"
                  v-model.number="priceFilter.max"
                  placeholder="最高价"
                />
                <button class="apply-filter-btn" @click="applyFilters">
                  确定
                </button>
                <button class="reset-filter-btn" @click="resetFilters">
                  重置
                </button>
              </div>
            </div>

            <div class="filter-item">
              <span class="filter-label">位置：</span>
              <div class="location-filter">
                <select v-model="locationFilter">
                  <option value="">全部</option>
                  <option value="北京">北京</option>
                  <option value="上海">上海</option>
                  <option value="广州">广州</option>
                  <option value="深圳">深圳</option>
                  <option value="杭州">杭州</option>
                </select>
              </div>
            </div>
          </div>

          <!-- 排序选项 -->
          <div class="sort-options">
            <span
              class="sort-option"
              :class="{ active: sortBy === 'default' }"
              @click="changeSort('default')"
              >默认排序</span
            >
            <span
              class="sort-option"
              :class="{ active: sortBy === 'price_asc' }"
              @click="changeSort('price_asc')"
              >价格从低到高</span
            >
            <span
              class="sort-option"
              :class="{ active: sortBy === 'price_desc' }"
              @click="changeSort('price_desc')"
              >价格从高到低</span
            >
            <span
              class="sort-option"
              :class="{ active: sortBy === 'time' }"
              @click="changeSort('time')"
              >最新发布</span
            >
          </div>
        </div>

        <!-- 搜索结果列表 -->
        <div class="results-list">
          <div
            class="product-item"
            v-for="product in searchResults"
            :key="product.id"
            @click="goToDetail(product.id)"
          >
            <div class="product-image">
              <img :src="product.image" :alt="product.title" />
            </div>
            <div class="product-info">
              <h3 class="product-title">{{ product.title }}</h3>
              <p class="product-price">¥{{ product.price }}</p>
              <p class="product-meta">
                <span class="product-location">{{ product.location }}</span>
                <span class="product-time">{{ product.time }}</span>
              </p>
            </div>
          </div>

          <!-- 无搜索结果时显示 -->
          <div class="no-results" v-if="searchResults.length === 0">
            <div class="no-results-icon">🔍</div>
            <div class="no-results-text">没有找到匹配的商品</div>
            <button class="try-again-btn" @click="clearSearchFilters">
              换个关键词试试
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';
import { get } from '../../utils/apiUtils';

export default {
  name: 'SearchPage',
  data() {
    return {
      // 搜索查询
      searchQuery: '',
      // 搜索结果
      searchResults: [],
      // 原始搜索结果（用于筛选）
      originalSearchResults: [],
      // 搜索历史
      searchHistory: ['iPhone', 'MacBook', 'AirPods', '小米手机', '华为手表'],
      // 热门搜索
      hotSearch: [
        'iPhone 13',
        'MacBook Pro',
        'AirPods Pro',
        '小米13',
        '华为Mate50',
        '索尼耳机',
        '任天堂Switch'
      ],
      // 排序方式
      sortBy: 'default',
      // 筛选条件
      priceFilter: {
        min: null,
        max: null
      },
      locationFilter: ''
    };
  },
  mounted() {
    // 页面加载时设置标题
    nativeUtils.setTitleBar('搜索');

    // 解析URL参数
    this.parseUrlParams();
  },
  methods: {
    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1);
    },
    // 处理搜索输入
    handleSearchInput() {
      // 实时搜索（预留接口）
      // if (this.searchQuery.length > 0) {
      //   this.performSearch()
      // }
    },

    // 解析URL参数
    parseUrlParams() {
      const urlParams = new URLSearchParams(window.location.search);
      const categoryId = urlParams.get('category');
      const keyword = urlParams.get('keyword');

      if (categoryId) {
        // 分类搜索
        this.searchQuery = keyword || '';
        this.performSearch();
      }
    },
    // 执行搜索
    async performSearch() {
      // 检查URL参数中是否有分类ID
      const urlParams = new URLSearchParams(window.location.search);
      const hasCategory = urlParams.get('category');

      // 如果没有分类且没有搜索关键词，提示用户
      if (!hasCategory && !this.searchQuery.trim()) {
        nativeUtils.showToast('请输入搜索关键词', 1500);
        return;
      }

      // 显示加载提示
      nativeUtils.showLoading('搜索中...');

      try {
        // 调用API获取搜索结果
        const response = await get(
          `/newshop/product/search?keyword=${encodeURIComponent(this.searchQuery)}`
        );

        // 隐藏加载提示
        nativeUtils.hideLoading();

        // 如果有搜索关键词，保存到搜索历史
        if (this.searchQuery.trim()) {
          this.saveToSearchHistory(this.searchQuery);
        }

        // 检查API响应
        if (response.code === 0 && response.data) {
          // API请求成功，处理数据
          this.originalSearchResults = response.data.map((product) => ({
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
          // 应用筛选条件
          this.applyFilters();
        } else {
          // API返回数据格式不正确
          console.warn('API返回数据格式不正确');
          nativeUtils.showToast('搜索失败，请重试', 1500);
          this.originalSearchResults = [];
          this.searchResults = [];
        }
      } catch (error) {
        console.error('搜索失败:', error);
        nativeUtils.hideLoading();
        nativeUtils.showToast('搜索失败，请重试', 1500);
        this.originalSearchResults = [];
        this.searchResults = [];
      }
    },

    // 应用筛选条件
    applyFilters() {
      // 从原始搜索结果开始筛选
      let filteredResults = [...this.originalSearchResults];

      // 价格筛选
      if (this.priceFilter.min !== null && this.priceFilter.min > 0) {
        filteredResults = filteredResults.filter(
          (product) => product.price >= this.priceFilter.min
        );
      }
      if (this.priceFilter.max !== null && this.priceFilter.max > 0) {
        filteredResults = filteredResults.filter(
          (product) => product.price <= this.priceFilter.max
        );
      }

      // 位置筛选
      if (this.locationFilter) {
        filteredResults = filteredResults.filter(
          (product) => product.location === this.locationFilter
        );
      }

      // 保存筛选结果
      this.searchResults = filteredResults;

      // 应用排序
      this.changeSort(this.sortBy);
    },

    // 重置筛选条件
    resetFilters() {
      this.priceFilter = {
        min: null,
        max: null
      };
      this.locationFilter = '';
      this.applyFilters();
    },

    // 清除所有搜索和筛选条件
    clearSearchFilters() {
      this.searchQuery = '';
      this.searchResults = [];
      this.originalSearchResults = [];
      this.resetFilters();
    },
    // 按关键词搜索
    searchByKeyword(keyword) {
      this.searchQuery = keyword;
      this.performSearch();
    },
    // 清除搜索
    clearSearch() {
      this.searchQuery = '';
      this.searchResults = [];
    },
    // 保存搜索历史
    saveToSearchHistory(keyword) {
      // 去重
      const index = this.searchHistory.indexOf(keyword);
      if (index > -1) {
        this.searchHistory.splice(index, 1);
      }
      // 添加到开头
      this.searchHistory.unshift(keyword);
      // 限制最多保存10条
      if (this.searchHistory.length > 10) {
        this.searchHistory.pop();
      }
    },
    // 清除搜索历史
    clearSearchHistory() {
      this.searchHistory = [];
    },
    // 排序搜索结果
    changeSort(sortType) {
      this.sortBy = sortType;
      // 根据排序类型排序搜索结果
      switch (sortType) {
        case 'price_asc':
          this.searchResults.sort((a, b) => a.price - b.price);
          break;
        case 'price_desc':
          this.searchResults.sort((a, b) => b.price - a.price);
          break;
        case 'time':
          // 这里简化处理，实际应根据时间戳排序
          this.searchResults.sort((a, b) => {
            const timeA = this.getTimeValue(a.time);
            const timeB = this.getTimeValue(b.time);
            return timeB - timeA;
          });
          break;
        default:
          // 默认排序，恢复原始顺序
          // 注意：不能调用performSearch()，否则会导致无限循环
          this.searchResults = [...this.originalSearchResults];
      }
    },
    // 将时间字符串转换为数值（简化处理）
    getTimeValue(timeStr) {
      if (timeStr.includes('小时前')) {
        return parseInt(timeStr);
      } else if (timeStr.includes('天前')) {
        return parseInt(timeStr) * 24;
      }
      return 0;
    },
    // 跳转到商品详情页
    goToDetail(productId) {
      routerUtils.navigateTo(`/newshop/detail?id=${productId}`);
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
    }
  }
};
</script>

<style scoped>
.search-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部搜索栏 */
.search-header {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 10px 15px;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left,
.header-right {
  padding: 8px;
  cursor: pointer;
}

.back-icon {
  font-size: 20px;
  color: #333;
}

.search-input-group {
  flex: 1;
  display: flex;
  align-items: center;
  background-color: #f0f0f0;
  border-radius: 20px;
  padding: 0 15px;
  margin: 0 10px;
  position: relative;
}

.search-icon {
  color: #999;
  font-size: 16px;
  margin-right: 8px;
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: 12px 0;
  font-size: 14px;
  outline: none;
}

.clear-btn {
  color: #999;
  font-size: 16px;
  cursor: pointer;
  padding: 5px;
}

.search-btn {
  color: #42b983;
  font-size: 16px;
  font-weight: bold;
}

/* 搜索内容区域 */
.search-content {
  flex: 1;
  padding: 15px;
}

/* 搜索建议区域 */
.search-suggestions {
  background-color: white;
  border-radius: 10px;
  overflow: hidden;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.clear-history {
  font-size: 14px;
  color: #999;
  cursor: pointer;
}

/* 搜索历史标签 */
.history-tags {
  display: flex;
  flex-wrap: wrap;
  padding: 0 15px 15px;
}

.history-tag {
  display: inline-block;
  background-color: #f0f0f0;
  color: #666;
  font-size: 14px;
  padding: 8px 15px;
  border-radius: 15px;
  margin-right: 10px;
  margin-bottom: 10px;
  cursor: pointer;
}

/* 热门搜索 */
.hot-section {
  padding: 0 15px 15px;
}

.hot-tags {
  display: flex;
  flex-direction: column;
}

.hot-tag {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  font-size: 14px;
  color: #333;
}

.hot-tag:last-child {
  border-bottom: none;
}

.tag-rank {
  display: inline-block;
  width: 20px;
  height: 20px;
  line-height: 20px;
  text-align: center;
  background-color: #ff4444;
  color: white;
  border-radius: 50%;
  font-size: 12px;
  margin-right: 15px;
}

.hot-tag:nth-child(2) .tag-rank {
  background-color: #ff8844;
}

.hot-tag:nth-child(3) .tag-rank {
  background-color: #ffcc44;
}

/* 搜索结果 */
.search-results {
  background-color: white;
  border-radius: 10px;
  overflow: hidden;
}

/* 筛选和排序区域 */
.filter-sort-section {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

/* 筛选条件 */
.filters-section {
  margin-bottom: 15px;
}

.filter-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.filter-label {
  font-size: 14px;
  color: #333;
  margin-right: 10px;
  min-width: 60px;
}

/* 价格筛选 */
.price-filter {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.price-input {
  width: 80px;
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
}

.price-input:focus {
  border-color: #42b983;
}

.price-separator {
  margin: 0 8px;
  color: #999;
}

.apply-filter-btn,
.reset-filter-btn {
  margin-left: 10px;
  padding: 6px 15px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.apply-filter-btn {
  background-color: #42b983;
  color: white;
}

.apply-filter-btn:hover {
  background-color: #3aa876;
}

.reset-filter-btn {
  background-color: #f0f0f0;
  color: #666;
}

.reset-filter-btn:hover {
  background-color: #e0e0e0;
}

/* 位置筛选 */
.location-filter select {
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
  background-color: white;
  min-width: 120px;
}

.location-filter select:focus {
  border-color: #42b983;
}

/* 排序选项 */
.sort-options {
  display: flex;
  padding: 10px 0;
  border-top: 1px solid #f0f0f0;
}

.sort-option {
  font-size: 14px;
  color: #333;
  margin-right: 20px;
  cursor: pointer;
  position: relative;
  padding: 5px 0;
}

.sort-option.active {
  color: #42b983;
  font-weight: bold;
}

.sort-option.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #42b983;
}

/* 搜索结果列表 */
.results-list {
  padding: 15px;
}

.product-item {
  display: flex;
  margin-bottom: 20px;
  cursor: pointer;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.product-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.product-image {
  width: 100px;
  height: 100px;
  border-radius: 5px;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
  min-width: 0;
}

.product-title {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price {
  font-size: 18px;
  font-weight: bold;
  color: #ff4444;
  margin-bottom: 5px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.product-location {
  margin-right: 15px;
}

/* 无搜索结果 */
.no-results {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.no-results-icon {
  font-size: 48px;
  color: #ddd;
  margin-bottom: 20px;
}

.no-results-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

.try-again-btn {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
}

.try-again-btn:hover {
  background-color: #3aa876;
}
</style>
