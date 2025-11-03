<template>
  <div class="search-bar-shop_top_search">
    <div class="search-input-box-shop_top_search">
      <!-- 放大镜图标 -->
      <svg class="icon-shop_top_search" viewBox="0 0 1024 1024">
        <path
          d="M955.84 911.36L728.96 684.48A381.44 381.44 0 1 0 684.48 728.96l226.88 226.88a32 32 0 0 0 45.44 0 32 32 0 0 0 0-45.44zM448 768a320 320 0 1 1 320-320 320 320 0 0 1-320 320z"
          fill="#999"
        />
      </svg>

      <!-- 输入框 -->
      <input
        type="text"
        v-model="keyword"
        placeholder="女用打泡机"
        class="search-input-shop_top_search"
      />

      <!-- 相机图标 -->
      <svg
        class="icon-shop_top_search camera-shop_top_search"
        viewBox="0 0 1024 1024"
      >
        <path
          d="M512 384a128 128 0 1 0 128 128 128 128 0 0 0-128-128z m416 64h-64v-64a64 64 0 0 0-64-64h-96l-32-64a64 64 0 0 0-57.6-32h-224a64 64 0 0 0-57.6 32l-32 64H224a64 64 0 0 0-64 64v64H96a64 64 0 0 0-64 64v320a64 64 0 0 0 64 64h832a64 64 0 0 0 64-64V512a64 64 0 0 0-64-64zM512 704a192 192 0 1 1 192-192 192 192 0 0 1-192 192z"
          fill="#999"
        />
      </svg>
    </div>

    <!-- 搜索按钮 -->
    <button class="search-btn-shop_top_search" @click="handleSearch">
      搜索
    </button>

    <!-- 购物车图标 -->
    <svg
      class="icon-shop_top_search cart-shop_top_search"
      viewBox="0 0 1024 1024"
    >
      <path
        d="M320 896a64 64 0 1 0 64 64 64 64 0 0 0-64-64z m448 0a64 64 0 1 0 64 64 64 64 0 0 0-64-64z m141.312-704H281.664l-13.504-67.2A64 64 0 0 0 205.44 64H64v64h128l108.8 537.6a64 64 0 0 0 62.4 50.4h448a64 64 0 0 0 62.4-50.4L896 288H384v64h467.52l-54.08 268.8H363.2l-10.24-51.2h499.84v-64H340.48l-6.4-32H896a64 64 0 0 0 62.4-50.4L960 256a64 64 0 0 0-62.688-64z"
        fill="#f44"
      />
    </svg>
  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  data() {
    return {
      keyword: ''
    };
  },
  methods: {
    handleSearch() {
      alert('搜索：' + this.keyword);
    }
  }
};
</script>

<style scoped>
.search-bar-shop_top_search {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background: linear-gradient(to right, #ffd6d6, #ffe3e3);
  border-radius: 24px;
  width: 100%;
  box-sizing: border-box;
}

.search-input-box-shop_top_search {
  display: flex;
  align-items: center;
  flex: 1;
  background: #fff;
  border-radius: 24px;
  padding: 6px 12px;
}

.search-input-shop_top_search {
  border: none;
  outline: none;
  flex: 1;
  font-size: 14px;
  color: #333;
}

.icon-shop_top_search {
  width: 20px;
  height: 20px;
  margin-right: 8px;
}

.camera-shop_top_search {
  margin-left: 4px;
}

.search-btn-shop_top_search {
  background: #f44;
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 6px 14px;
  margin-left: 8px;
  cursor: pointer;
  font-size: 14px;
}

.cart-shop_top_search {
  width: 24px;
  height: 24px;
  margin-left: 10px;
}
</style>
