<template>
  <div class="search-container">
    <!-- 外层容器：包含搜索栏和购物车，使用弹性布局对齐 -->
    <div class="search-header-wrapper">
      <!-- 搜索栏：仅保留搜索相关元素 -->
      <div class="search-bar">
        <!-- 搜索图标 -->
        <svg
          class="icon-search"
          width="16"
          height="16"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M11 19C15.4183 19 19 15.4183 19 11C19 6.58172 15.4183 3 11 3C6.58172 3 3 6.58172 3 11C3 15.4183 6.58172 19 11 19Z"
            stroke="#999"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M21 21L16.65 16.65"
            stroke="#999"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>

        <!-- 搜索输入框 -->
        <input
          type="text"
          v-model="searchText"
          placeholder="苹果iPhone 13手机壳"
          class="search-input"
        />

        <!-- 相机图标 -->
        <button class="camera-btn">
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M23 19V5C23 3.89543 22.1046 3 21 3H3C1.89543 3 1 3.89543 1 5V19C1 20.1046 1.89543 21 3 21H21C22.1046 21 23 20.1046 23 19Z"
              stroke="#999"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
            <path
              d="M12 17C14.7614 17 17 14.7614 17 12C17 9.23858 14.7614 7 12 7C9.23858 7 7 9.23858 7 12C7 14.7614 9.23858 17 12 17Z"
              stroke="#999"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
            <path
              d="M8.5 10.5L15.5 15.5"
              stroke="#999"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
          </svg>
        </button>

        <!-- 搜索按钮 -->
        <button class="search-btn">搜索</button>
      </div>

      <!-- 购物车：移到.search-bar外部，与搜索栏同级 -->
      <button class="cart-btn">
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M3 3H5L5.4 5M5.4 5H21L17 13H7M5.4 5L7 13M7 13L5.8 18H17.5"
            stroke="#999"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <circle cx="8.5" cy="19.5" r="1.5" fill="#999" />
          <circle cx="17.5" cy="19.5" r="1.5" fill="#999" />
        </svg>
        <span class="badge">临馨</span>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchText: '苹果iPhone 13手机壳'
    };
  }
};
</script>

<style scoped>
.search-container {
  width: 100%;
  box-sizing: border-box;
  padding: 8px 10px; /* 保留外层间距 */
  background-color: #ff9aa9;
}

/* 新增外层包装：控制搜索栏和购物车的布局 */
.search-header-wrapper {
  display: flex;
  align-items: center;
  gap: 8px; /* 搜索栏与购物车之间的间距 */
}

/* 搜索栏：移除原有的购物车相关样式，保持搜索功能完整性 */
.search-bar {
  display: flex;
  align-items: center;
  background-color: #f8f8f8;
  border-radius: 20px;
  height: 38px;
  padding: 0 5px;
  box-sizing: border-box;
  overflow: hidden; /* 防止内容溢出圆角 */
  /* 让搜索栏占满剩余宽度，购物车固定大小 */
  flex: 1;
  min-width: 0;
}

/* 图标统一设置不可收缩 */
.icon-search,
.camera-btn,
.search-btn {
  flex-shrink: 0;
}

.icon-search {
  margin: 0 6px 0 10px; /* 左侧间距减小 */
}

.search-input {
  flex: 1; /* 输入框自适应剩余宽度 */
  min-width: 0; /* 关键：允许输入框在窄屏时收缩到最小 */
  border: none;
  outline: none;
  background: transparent;
  font-size: 14px;
  padding: 0 5px;
  box-sizing: border-box;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis; /* 文字过长时显示省略号 */
}

.camera-btn {
  width: 34px;
  height: 100%;
  border: none;
  background: transparent;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 2px;
}

.search-btn {
  width: 65px;
  height: 30px;
  background-color: #ff2d4b;
  color: white;
  border: none;
  border-radius: 15px;
  font-size: 13px;
  cursor: pointer;
  margin: 0 4px;
}

/* 购物车：保持原有样式，调整为与搜索栏同级的布局 */
.cart-btn {
  position: relative;
  width: 36px;
  height: 38px; /* 与搜索栏高度一致，确保垂直对齐 */
  border: none;
  background: transparent;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  /* 固定大小，不随屏幕收缩 */
  flex-shrink: 0;
}

.badge {
  position: absolute;
  top: -2px;
  right: -2px;
  background-color: #ff2d4b;
  color: white;
  font-size: 10px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 小屏幕适配（如手机） */
@media (max-width: 375px) {
  .search-btn {
    width: 55px; /* 进一步缩小搜索按钮 */
    font-size: 12px;
  }
  .cart-btn {
    width: 32px;
  }
  .icon-search {
    margin-left: 8px;
  }
  /* 小屏幕下减小搜索栏与购物车的间距 */
  .search-header-wrapper {
    gap: 4px;
  }
}
</style>
