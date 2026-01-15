<template>
  <div class="categories-container">
    <!-- 顶部搜索栏 -->
    <header class="header">
      <div class="search-bar" @click="goToSearch">
        <i class="search-icon">🔍</i>
        <span class="search-placeholder">搜索你想要的商品</span>
      </div>
    </header>

    <!-- 分类内容区域 -->
    <div class="categories-content">
      <!-- 左侧主分类 -->
      <div class="main-categories">
        <div
          class="main-category-item"
          v-for="category in mainCategories"
          :key="category.id"
          :class="{ active: activeMainCategory === category.id }"
          @click="selectMainCategory(category.id)"
        >
          {{ category.name }}
        </div>
      </div>

      <!-- 右侧子分类 -->
      <div class="sub-categories">
        <div
          class="sub-category-section"
          v-for="section in currentSubCategories"
          :key="section.name"
        >
          <div class="section-title">{{ section.name }}</div>
          <div class="sub-category-grid">
            <div
              class="sub-category-item"
              v-for="sub in section.subcategories"
              :key="sub.id"
              @click="goToCategoryProducts(sub.id, sub.name)"
            >
              <div class="sub-category-icon">{{ sub.icon }}</div>
              <div class="sub-category-name">{{ sub.name }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部导航栏 -->
    <footer class="bottom-nav">
      <div class="nav-item" @click="goToHome">
        <i class="nav-icon">🏠</i>
        <span class="nav-text">首页</span>
      </div>
      <div class="nav-item active" @click="goToCategories">
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
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';

export default {
  name: 'CategoriesApp',
  data() {
    return {
      // 静态主分类数据
      mainCategories: [
        { id: 1, name: '手机数码' },
        { id: 2, name: '电脑办公' },
        { id: 3, name: '家用电器' },
        { id: 4, name: '服装鞋包' },
        { id: 5, name: '美妆护肤' },
        { id: 6, name: '运动户外' },
        { id: 7, name: '图书音像' },
        { id: 8, name: '母婴用品' },
        { id: 9, name: '家居家装' },
        { id: 10, name: '汽车用品' },
        { id: 11, name: '奢侈品' },
        { id: 12, name: '其他' }
      ],
      // 静态子分类数据
      subCategories: {
        1: [
          {
            name: '手机',
            subcategories: [
              { id: 101, name: 'iPhone', icon: '📱' },
              { id: 102, name: '华为', icon: '📱' },
              { id: 103, name: '小米', icon: '📱' },
              { id: 104, name: 'OPPO', icon: '📱' },
              { id: 105, name: 'vivo', icon: '📱' },
              { id: 106, name: '三星', icon: '📱' },
              { id: 107, name: '其他品牌', icon: '📱' }
            ]
          },
          {
            name: '数码配件',
            subcategories: [
              { id: 111, name: '耳机', icon: '🎧' },
              { id: 112, name: '手机壳', icon: '📱' },
              { id: 113, name: '充电宝', icon: '🔋' },
              { id: 114, name: '数据线', icon: '🔌' },
              { id: 115, name: '充电器', icon: '🔌' },
              { id: 116, name: '贴膜', icon: '📱' }
            ]
          },
          {
            name: '相机',
            subcategories: [
              { id: 121, name: '单反相机', icon: '📷' },
              { id: 122, name: '微单相机', icon: '📷' },
              { id: 123, name: '拍立得', icon: '📷' },
              { id: 124, name: '相机配件', icon: '📷' }
            ]
          }
        ],
        2: [
          {
            name: '笔记本电脑',
            subcategories: [
              { id: 201, name: 'MacBook', icon: '💻' },
              { id: 202, name: 'ThinkPad', icon: '💻' },
              { id: 203, name: 'Dell', icon: '💻' },
              { id: 204, name: 'HP', icon: '💻' },
              { id: 205, name: '小米', icon: '💻' },
              { id: 206, name: '其他', icon: '💻' }
            ]
          },
          {
            name: '平板',
            subcategories: [
              { id: 211, name: 'iPad', icon: '📱' },
              { id: 212, name: '华为平板', icon: '📱' },
              { id: 213, name: '小米平板', icon: '📱' },
              { id: 214, name: '其他平板', icon: '📱' }
            ]
          },
          {
            name: '办公设备',
            subcategories: [
              { id: 221, name: '显示器', icon: '🖥️' },
              { id: 222, name: '键盘', icon: '⌨️' },
              { id: 223, name: '鼠标', icon: '🖱️' },
              { id: 224, name: '打印机', icon: '🖨️' }
            ]
          }
        ],
        3: [
          {
            name: '大家电',
            subcategories: [
              { id: 301, name: '电视', icon: '📺' },
              { id: 302, name: '冰箱', icon: '🧊' },
              { id: 303, name: '洗衣机', icon: '🧺' },
              { id: 304, name: '空调', icon: '❄️' },
              { id: 305, name: '热水器', icon: '🚿' }
            ]
          },
          {
            name: '生活电器',
            subcategories: [
              { id: 311, name: '微波炉', icon: '🥘' },
              { id: 312, name: '烤箱', icon: '🍕' },
              { id: 313, name: '电饭煲', icon: '🍚' },
              { id: 314, name: '吸尘器', icon: '🧹' },
              { id: 315, name: '空气净化器', icon: '💨' }
            ]
          }
        ]
      },
      activeMainCategory: 1
    };
  },
  mounted() {
    // 页面加载时设置标题
    nativeUtils.setTitleBar('二手交易 - 分类');
  },
  computed: {
    // 当前选中主分类下的子分类
    currentSubCategories() {
      return this.subCategories[this.activeMainCategory] || [];
    }
  },
  methods: {
    // 跳转到搜索页
    goToSearch() {
      routerUtils.navigateTo('/search.html');
    },

    // 选择主分类
    selectMainCategory(categoryId) {
      this.activeMainCategory = categoryId;
    },

    // 跳转到分类商品列表
    goToCategoryProducts(subCategoryId, subCategoryName) {
      console.log(`跳转到${subCategoryName}商品列表，ID:${subCategoryId}`);
      // 实际项目中跳转到分类商品页面
      routerUtils.navigateTo(
        `/search.html?category=${subCategoryId}&keyword=${subCategoryName}`
      );
    },

    // 跳转到首页
    goToHome() {
      routerUtils.navigateTo('/index.html');
    },

    // 跳转到分类页
    goToCategories() {
      // 已经在分类页，无需跳转
    },

    // 跳转到发布页
    goToPublish() {
      routerUtils.navigateTo('/publish.html');
    },

    // 跳转到消息页
    goToMessages() {
      routerUtils.navigateTo('/messages.html');
    },

    // 跳转到个人中心
    goToProfile() {
      routerUtils.navigateTo('/profile.html');
    }
  }
};
</script>

<style scoped>
.categories-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部搜索栏 */
.header {
  background-color: #ff4400;
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

/* 分类内容区域 */
.categories-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 左侧主分类 */
.main-categories {
  width: 100px;
  background-color: #fafafa;
  overflow-y: auto;
  border-right: 1px solid #eee;
}

.main-category-item {
  padding: 15px 10px;
  text-align: center;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
}

.main-category-item.active {
  background-color: white;
  color: #ff4400;
  font-weight: bold;
  position: relative;
}

.main-category-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background-color: #ff4400;
}

/* 右侧子分类 */
.sub-categories {
  flex: 1;
  background-color: white;
  overflow-y: auto;
  padding: 15px;
}

.sub-category-section {
  margin-bottom: 25px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  padding-left: 5px;
}

.sub-category-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
}

.sub-category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 10px 5px;
  border-radius: 8px;
  transition: background-color 0.2s ease;
}

.sub-category-item:hover {
  background-color: #f5f5f5;
}

.sub-category-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.sub-category-name {
  font-size: 13px;
  color: #666;
  text-align: center;
  word-break: break-word;
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
