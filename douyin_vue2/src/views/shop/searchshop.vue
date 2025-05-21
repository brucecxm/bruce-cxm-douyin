<template>
  <div class="wallet-container">
    <!-- Header with search box -->
    <div class="header">
      <input
        v-model="searchQuery"
        type="text"
        placeholder="搜索商品..."
        class="search-input"
        @keyup.enter="search"
      />
      <button @click="search" class="search-button">搜索</button>
    </div>

    <!-- Product List -->
    <div v-if="products.length" class="product-list">
      <div class="product" v-for="(product, index) in products" :key="index">
        <img :src="product.shop_img" alt="product image" class="product-img" />
        <div class="product-info">
          <h3>{{ product.shop_name }}</h3>
          <p>{{ product.shop_desc }}</p>
          <p>价格：{{ product.shop_price }}元</p>
          <p>详细信息：{{ product.shop_info }}</p>
        </div>
      </div>
    </div>

    <!-- Loading message -->
    <div v-else class="loading">正在加载...</div>
  </div>
</template>

<script>
import { getlikeall } from '@/api/shop';

export default {
  data() {
    return {
      searchQuery: this.$route.query.search || '', // 从 URL 获取初始搜索查询
      products: [], // 商品数据
      current: 1, // 当前页
      size: 10 // 每页显示的商品数量
    };
  },

  mounted() {
    this.search(); // 在页面加载时进行一次搜索
  },

  methods: {
    // 搜索函数
    search() {
      if (!this.searchQuery) return; // 如果没有输入内容，则不进行搜索

      // 调用 API 获取商品列表
      getlikeall(this.searchQuery, this.current, this.size)
        .then((response) => {
          console.log('获取数据成功:', response.data.data); // 打印成功响应
          this.products = response.data.data; // 更新商品数据
        })
        .catch((error) => {
          console.error('请求失败:', error); // 打印错误信息
        });

      // 更新 URL 参数，保持当前搜索状态
      this.$router.push({
        path: '/searchshop',
        query: { search: this.searchQuery }
      });
    }
  }
};
</script>

<style scoped>
.wallet-container {
  width: 100%;
  height: 100%;
  overflow: auto;
  font-family: Arial, sans-serif;
}

.header {
  background-color: #007bff;
  color: white;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-input {
  padding: 8px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  width: 250px;
}

.search-button {
  padding: 8px 16px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #218838;
}

.product-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 20px;
}

.product {
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 8px;
  width: calc(33% - 20px);
  padding: 10px;
  box-sizing: border-box;
}

.product-img {
  max-width: 100%;
  border-radius: 4px;
}

.product-info {
  margin-top: 10px;
}

.product-info h3 {
  font-size: 18px;
  font-weight: bold;
}

.product-info p {
  font-size: 14px;
  color: #555;
}

.loading {
  text-align: center;
  padding: 20px;
  font-size: 18px;
  color: #888;
}
</style>
