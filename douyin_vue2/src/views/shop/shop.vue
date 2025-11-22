<template>
  <div class="shop-container">
    <!-- 顶部搜索栏 -->
    <div class="header">
      <div class="search-bar">
        <div class="search-input-wrapper">
          <i class="search-icon">🔍</i>
          <input
            v-model="searchText"
            @focus="showSuggestions = true"
            @blur="hideSuggestions"
            @input="handleSearchInput"
            type="text"
            placeholder="养母狗必备用品 中秋热搜"
          />
        </div>
        <div class="icon-wrapper">
          <i class="camera-icon">📷</i>
          <button @click="searchmeth" class="search-button">搜索</button>
        </div>
      </div>

      <!-- 工具栏 -->
      <div class="tool-bar">
        <div class="tool-item" @click="gotoOrder">
          <i class="tool-icon">📋</i>
          <span class="tool-text">我的订单</span>
        </div>
        <div class="tool-item" @click="gotoRecharge">
          <i class="tool-icon">⚡</i>
          <span class="tool-text">充值中心</span>
        </div>
        <div class="tool-item" @click="gotoExpress">
          <i class="tool-icon">⚡</i>
          <span class="tool-text">小时达</span>
        </div>
        <div class="tool-item" @click="goto足迹">
          <i class="tool-icon">👣</i>
          <span class="tool-text">足迹</span>
        </div>
        <div class="tool-item" @click="gotoAddress">
          <i class="tool-icon">📍</i>
          <span class="tool-text">地址</span>
        </div>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="main-content" @scroll="handleScroll">
      <!-- 超值购区域 -->
      <div class="value-section">
        <div class="value-header">
          <div class="value-title">超值购</div>
          <div class="value-subtitle">万人团</div>
          <div class="value-more">更多 <i class="more-icon">›</i></div>
        </div>

        <div class="value-grid">
          <div
            v-for="(item, index) in valueItems"
            :key="index"
            class="value-item"
            @click="goshopdetail(item)"
          >
            <div class="value-image">
              <img :src="item.img" alt="超值商品" />
              <div v-if="item.discount" class="discount-tag">
                {{ item.discount }}
              </div>
            </div>
            <div class="value-price">¥{{ item.price }}</div>
          </div>
        </div>
      </div>

      <!-- 限时抢购区域 -->
      <div class="flash-sale-section">
        <div class="flash-sale-header">
          <div class="flash-sale-title">低价秒杀</div>
          <div class="flash-sale-subtitle">限时抢购</div>
          <div class="flash-sale-more">更多 <i class="more-icon">›</i></div>
        </div>

        <div class="flash-sale-grid">
          <div
            v-for="(item, index) in flashSaleItems"
            :key="index"
            class="flash-sale-item"
            @click="goshopdetail(item)"
          >
            <div class="flash-sale-image">
              <img :src="item.img" alt="秒杀商品" />
            </div>
            <div class="flash-sale-price">¥{{ item.price }}</div>
          </div>
        </div>
      </div>

      <!-- 国家补贴区域 -->
      <div class="subsidy-section">
        <div class="subsidy-header">
          <div class="subsidy-title">国家补贴</div>
          <div class="subsidy-subtitle">最高减20%</div>
          <div class="subsidy-more">更多 <i class="more-icon">›</i></div>
        </div>

        <div class="subsidy-grid">
          <div
            v-for="(item, index) in subsidyItems"
            :key="index"
            class="subsidy-item"
            @click="goshopdetail(item)"
          >
            <div class="subsidy-image">
              <img :src="item.img" alt="补贴商品" />
              <div class="subsidy-tag">国补{{ item.subsidyRate }}%</div>
            </div>
            <div class="subsidy-price">¥{{ item.price }}</div>
          </div>
        </div>
      </div>

      <!-- 分类导航 -->
      <div class="category-nav">
        <div class="nav-scroll">
          <div
            v-for="(item, index) in categoryItems"
            :key="index"
            class="nav-item"
            :class="{ active: activeCategory === index }"
            @click="handleCategoryClick(index)"
          >
            {{ item.name }}
          </div>
        </div>
      </div>

      <!-- 推荐横幅 -->
      <div class="banner-section">
        <div class="banner-left" @click="gotoPromotion">
          <div class="banner-title">抖音商城中秋</div>
          <div class="banner-desc">限时福利</div>
          <div class="banner-price">175元</div>
          <div class="banner-button">去领取</div>
        </div>
        <div class="banner-right">
          <img
            src="https://picsum.photos/seed/fashion/150/150"
            alt="中秋新款"
          />
          <div class="banner-right-text">中秋新款</div>
        </div>
      </div>

      <!-- 商品列表区域 -->
      <div class="products-section">
        <!-- 商品网格 -->
        <div class="products-grid">
          <div
            v-for="item in shopitems"
            :key="item.shopId"
            @click="goshopdetail(item)"
            class="product-card"
          >
            <div class="product-image">
              <img
                :src="
                  item.shopImg ||
                    'https://picsum.photos/seed/' + item.shopId + '/300/300'
                "
                alt="商品图片"
              />
              <div v-if="item.discount" class="product-discount">
                {{ item.discount }}
              </div>
            </div>
            <div class="product-info">
              <div class="product-name">{{ item.shopName }}</div>
              <div class="product-desc">{{ item.shopDesc }}</div>
              <div class="product-price">¥{{ item.shopPrice }}</div>
              <div class="product-sales">已售{{ item.sales }}件</div>
            </div>
          </div>
        </div>

        <!-- 加载更多 -->
        <div class="loading-more" v-if="loading">正在加载...</div>
        <div class="no-more" v-else-if="noMore">没有更多商品了</div>
      </div>
    </div>

    <!-- 底部导航 -->
    <footer-vue class="footer"></footer-vue>
  </div>
</template>

<script>
import { getButtonInfo } from '@/api/admin';
import { shoplistPage, getlikeall } from '@/api/shop';
import footerVue from '@/components/footer.vue';
import store from '@/stores';

export default {
  name: 'ShopPage',
  components: {
    footerVue
  },
  data() {
    return {
      // 搜索相关
      searchText: '',
      showSuggestions: false,
      searchSuggestions: [],
      searchHistory: [],

      // 分类相关
      categoryItems: [
        { name: '推荐' },
        { name: '直播精选' },
        { name: '上新' },
        { name: '珠宝古玩' },
        { name: '食品饮料' },
        { name: '数码办公' }
      ],
      activeCategory: 0,

      // 超值购数据
      valueItems: [
        {
          img: 'https://picsum.photos/seed/jacket/100/100',
          price: '115.8',
          discount: '补42元'
        },
        {
          img: 'https://picsum.photos/seed/laptop/100/100',
          price: '2547.2',
          discount: '补336元'
        },
        {
          img: 'https://picsum.photos/seed/camera/100/100',
          price: '2484.55',
          discount: '补2514元'
        },
        {
          img: 'https://picsum.photos/seed/watch/100/100',
          price: '397',
          discount: '补2元'
        }
      ],

      // 秒杀数据
      flashSaleItems: [
        { img: 'https://picsum.photos/seed/dress/100/100', price: '44.99' },
        { img: 'https://picsum.photos/seed/underwear/100/100', price: '16.8' }
      ],

      // 补贴数据
      subsidyItems: [
        {
          img: 'https://picsum.photos/seed/fitness/100/100',
          price: '399',
          subsidyRate: '15'
        },
        {
          img: 'https://picsum.photos/seed/dv/100/100',
          price: '1299',
          subsidyRate: '15'
        }
      ],

      // 商品列表相关
      shopitems: [],
      page: {
        current: 1,
        size: 10
      },
      noMore: false,
      loading: false,
      query: {}
    };
  },
  computed: {
    filteredSuggestions() {
      // 简单的搜索联想实现
      if (!this.searchText) return [];
      return this.searchHistory
        .filter((item) =>
          item.toLowerCase().includes(this.searchText.toLowerCase())
        )
        .slice(0, 5);
    }
  },
  mounted() {
    this.loadMore();
  },
  methods: {
    async loadMore() {
      if (this.loading || this.noMore) return;

      this.loading = true;

      try {
        const params = {
          current: this.page.current,
          size: this.page.size,
          shop: {
            shopName: this.searchText || undefined,
            shopType: this.query.shopType || undefined
          },
          sort: this.query.sort || undefined,
          order: this.query.order || undefined
        };

        const res = await shoplistPage(
          this.page.current,
          this.page.size,
          params
        );

        // 检查响应数据格式
        if (res.data && res.data.shopList) {
          const records = res.data.shopList;

          // 判断是否还有更多
          if (records.length < this.page.size) {
            this.noMore = true;
          }

          // 添加到商品列表
          this.shopitems.push(...records);
          this.page.current++;

          // 保存搜索历史（如果有搜索词）
          if (
            this.searchText &&
            !this.searchHistory.includes(this.searchText)
          ) {
            this.searchHistory.unshift(this.searchText);
            if (this.searchHistory.length > 10) {
              this.searchHistory.pop();
            }
          }
        } else {
          console.error('响应数据格式不正确:', res);
          // 模拟数据填充
          this.fillMockData();
          this.noMore = true;
        }
      } catch (error) {
        console.error('加载商品失败', error);
        // 模拟数据填充
        this.fillMockData();
        this.noMore = true;
      } finally {
        this.loading = false;
      }
    },

    fillMockData() {
      // 填充模拟数据
      const mockData = [
        {
          shopId: 1,
          shopName: '夏季薄款短袖男t恤速干衣',
          shopDesc: '立减5元 近30天低价',
          shopPrice: '1.9',
          shopImg: 'https://picsum.photos/seed/tshirt/300/300',
          sales: 598
        },
        {
          shopId: 2,
          shopName: 'Apple iPhone 15 Pro Max',
          shopDesc: '官方直降 店铺补贴 立省更多',
          shopPrice: '3399',
          shopImg: 'https://picsum.photos/seed/iphone/300/300',
          sales: 1256
        },
        {
          shopId: 3,
          shopName: '智能手表运动健康监测',
          shopDesc: '心率血氧监测 长续航',
          shopPrice: '299',
          shopImg: 'https://picsum.photos/seed/watch2/300/300',
          sales: 342
        },
        {
          shopId: 4,
          shopName: '夏季防晒衣轻薄透气',
          shopDesc: 'UPF50+ 防紫外线',
          shopPrice: '89',
          shopImg: 'https://picsum.photos/seed/sunscreen/300/300',
          sales: 876
        }
      ];
      this.shopitems = [...this.shopitems, ...mockData];
    },

    handleCategoryClick(index) {
      this.activeCategory = index;
      // 重置并重新加载数据
      this.shopitems = [];
      this.page.current = 1;
      this.noMore = false;
      this.loadMore();
    },

    handleSearchInput() {
      if (this.searchText.length > 0) {
        // 模拟搜索建议
        this.searchSuggestions = [
          `${this.searchText}相关商品`,
          `热门${this.searchText}`,
          `推荐${this.searchText}`
        ].filter((item) => item.length > this.searchText.length);
      } else {
        this.searchSuggestions = [];
        delete this.query.shopName;
        this.resetAndLoad();
      }
    },

    async searchmeth() {
      if (!this.searchText) return;

      // 更新查询参数
      this.query.shopName = this.searchText;
      this.resetAndLoad();

      // 保存到搜索历史
      if (!this.searchHistory.includes(this.searchText)) {
        this.searchHistory.unshift(this.searchText);
        if (this.searchHistory.length > 10) {
          this.searchHistory.pop();
        }
      }

      // 隐藏联想框
      this.showSuggestions = false;
    },

    resetAndLoad() {
      // 重置并重新加载数据
      this.shopitems = [];
      this.page.current = 1;
      this.noMore = false;
      this.loadMore();
    },

    hideSuggestions() {
      // 失去焦点时隐藏联想框
      setTimeout(() => {
        this.showSuggestions = false;
      }, 200);
    },

    handleScroll(event) {
      const container = event.target;
      if (
        container.scrollHeight <=
        container.scrollTop + container.clientHeight + 200
      ) {
        this.loadMore();
      }
    },

    goshopdetail(item) {
      // 跳转到商品详情页
      this.$router.push({
        path: `/shopdetail/${item.shopId || Math.floor(Math.random() * 1000)}`
      });
    },

    gotoOrder() {
      console.log('跳转到订单页面');
    },

    gotoRecharge() {
      console.log('跳转到充值中心');
    },

    gotoExpress() {
      console.log('跳转到小时达');
    },

    gotoFootprint() {
      console.log('跳转到足迹');
    },

    gotoAddress() {
      console.log('跳转到地址管理');
    },

    gotoPromotion() {
      console.log('跳转到促销页面');
    },

    // 选择搜索建议
    selectSuggestion(suggestion) {
      this.searchText = suggestion;
      this.showSuggestions = false;
      this.searchmeth();
    }
  }
};
</script>

<style scoped>
/* 全局样式 */
.shop-container {
  background-color: #f5f5f5;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 顶部搜索栏 */
.header {
  background-color: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 15px;
}

.search-input-wrapper {
  flex: 1;
  position: relative;
  background-color: #f0f0f0;
  border-radius: 20px;
  padding: 0 15px;
  display: flex;
  align-items: center;
}

.search-icon {
  margin-right: 8px;
  color: #999;
  font-size: 16px;
}

.search-input-wrapper input {
  flex: 1;
  border: none;
  background: transparent;
  outline: none;
  padding: 10px 0;
  font-size: 14px;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
}

.camera-icon {
  font-size: 20px;
  cursor: pointer;
}

.search-button {
  background-color: #ff4757;
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: #ff3838;
}

/* 工具栏 */
.tool-bar {
  display: flex;
  justify-content: space-around;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.tool-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  cursor: pointer;
}

.tool-icon {
  font-size: 24px;
}

.tool-text {
  font-size: 12px;
  color: #666;
}

/* 主内容区域 */
.main-content {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 60px;
}

/* 超值购区域 */
.value-section {
  background-color: #fff;
  padding: 15px;
  margin-bottom: 10px;
}

.value-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.value-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-right: 5px;
}

.value-subtitle {
  background-color: #ff4757;
  color: #fff;
  font-size: 12px;
  padding: 2px 4px;
  border-radius: 2px;
  margin-right: auto;
}

.value-more {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
}

.more-icon {
  margin-left: 2px;
}

.value-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.value-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.value-image {
  position: relative;
  width: 100%;
  padding-bottom: 100%; /* 正方形 */
  overflow: hidden;
}

.value-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.discount-tag {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: #ff4757;
  color: #fff;
  font-size: 10px;
  padding: 2px 4px;
  border-radius: 2px;
}

.value-price {
  margin-top: 5px;
  font-size: 14px;
  font-weight: bold;
  color: #ff4757;
}

/* 限时抢购区域 */
.flash-sale-section {
  background-color: #fff;
  padding: 15px;
  margin-bottom: 10px;
}

.flash-sale-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.flash-sale-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-right: 5px;
}

.flash-sale-subtitle {
  background-color: #ff7f50;
  color: #fff;
  font-size: 12px;
  padding: 2px 4px;
  border-radius: 2px;
  margin-right: auto;
}

.flash-sale-more {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
}

.flash-sale-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.flash-sale-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.flash-sale-image {
  width: 100%;
  padding-bottom: 100%; /* 正方形 */
  overflow: hidden;
  position: relative;
}

.flash-sale-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.flash-sale-price {
  margin-top: 5px;
  font-size: 14px;
  font-weight: bold;
  color: #ff4757;
}

/* 国家补贴区域 */
.subsidy-section {
  background-color: #fff;
  padding: 15px;
  margin-bottom: 10px;
}

.subsidy-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.subsidy-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-right: 5px;
}

.subsidy-subtitle {
  background-color: #2ed573;
  color: #fff;
  font-size: 12px;
  padding: 2px 4px;
  border-radius: 2px;
  margin-right: auto;
}

.subsidy-more {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
}

.subsidy-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.subsidy-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.subsidy-image {
  position: relative;
  width: 100%;
  padding-bottom: 100%; /* 正方形 */
  overflow: hidden;
}

.subsidy-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.subsidy-tag {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: #2ed573;
  color: #fff;
  font-size: 10px;
  padding: 2px 4px;
  border-radius: 2px;
}

.subsidy-price {
  margin-top: 5px;
  font-size: 14px;
  font-weight: bold;
  color: #ff4757;
}

/* 分类导航 */
.category-nav {
  background-color: #fff;
  padding: 10px 0;
  margin-bottom: 10px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.nav-scroll {
  display: flex;
  overflow-x: auto;
  overflow-y: hidden;
  white-space: nowrap;
  padding: 0 10px;
}

.nav-scroll::-webkit-scrollbar {
  display: none;
}

.nav-item {
  padding: 5px 15px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  position: relative;
}

.nav-item.active {
  color: #ff4757;
  font-weight: 500;
}

.nav-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  background-color: #ff4757;
  border-radius: 1px;
}

/* 推荐横幅 */
.banner-section {
  display: flex;
  background-color: #fff;
  margin: 0 10px 10px 10px;
  border-radius: 8px;
  overflow: hidden;
  height: 120px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.banner-left {
  flex: 1;
  background: linear-gradient(135deg, #ff7b7b, #ff4757);
  color: #fff;
  padding: 15px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  cursor: pointer;
}

.banner-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.banner-desc {
  font-size: 14px;
  opacity: 0.9;
}

.banner-price {
  font-size: 24px;
  font-weight: bold;
  margin: 5px 0;
}

.banner-button {
  background-color: #fff;
  color: #ff4757;
  font-size: 12px;
  padding: 5px 10px;
  border-radius: 15px;
  align-self: flex-start;
}

.banner-right {
  width: 100px;
  position: relative;
}

.banner-right img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-right-text {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.6);
  color: #fff;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

/* 商品列表区域 */
.products-section {
  background-color: #fff;
  padding: 10px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.product-card {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition:
    transform 0.3s,
    box-shadow 0.3s;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.product-image {
  position: relative;
  width: 100%;
  padding-bottom: 100%; /* 正方形 */
  overflow: hidden;
}

.product-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-discount {
  position: absolute;
  top: 0;
  right: 0;
  background-color: #ff4757;
  color: #fff;
  font-size: 10px;
  padding: 2px 6px;
  border-bottom-left-radius: 4px;
}

.product-info {
  padding: 10px;
}

.product-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 5px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 40px;
}

.product-desc {
  font-size: 12px;
  color: #ff7f50;
  margin-bottom: 5px;
}

.product-price {
  font-size: 16px;
  font-weight: 600;
  color: #ff4757;
  margin-bottom: 3px;
}

.product-sales {
  font-size: 12px;
  color: #999;
}

/* 加载更多 */
.loading-more,
.no-more {
  text-align: center;
  padding: 15px;
  color: #999;
  font-size: 14px;
}

/* 底部导航 */
.footer {
  position: fixed;
  bottom: 0;
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 8px;
  }
}
</style>
