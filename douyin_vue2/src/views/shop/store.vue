<template>
  <div class="shop-page">
    <!-- 顶部店铺信息 -->
    <div class="shop-header">
      <img class="shop-cover" :src="shop.cover" alt="店铺封面" />
      <div class="shop-info">
        <img class="shop-avatar" :src="shop.avatar" alt="店铺头像" />
        <div class="shop-name">{{ shop.name }}</div>
        <div class="shop-desc">{{ shop.description }}</div>
        <div class="shop-stats">
          <div class="stat-item">
            <div class="stat-count">{{ shop.fans }}</div>
            <div class="stat-label">粉丝</div>
          </div>
          <div class="stat-item">
            <div class="stat-count">{{ shop.following }}</div>
            <div class="stat-label">关注</div>
          </div>
        </div>
        <button
          :class="['btn-follow', isFollowed ? 'followed' : '']"
          @click="toggleFollow"
        >
          {{ isFollowed ? '已关注' : '+ 关注' }}
        </button>
      </div>
    </div>

    <!-- Tab 切换 -->
    <div class="tab-bar">
      <div
        class="tab-item"
        :class="{ active: activeTab === 'products' }"
        @click="activeTab = 'products'"
      >
        商品
      </div>
      <div
        class="tab-item"
        :class="{ active: activeTab === 'dynamics' }"
        @click="activeTab = 'dynamics'"
      >
        动态
      </div>
    </div>

    <!-- 商品列表 -->
    <div v-if="activeTab === 'products'" class="products-list">
      <div
        v-for="product in products"
        :key="product.id"
        class="product-card"
        @click="goProduct(product.id)"
      >
        <img :src="product.image" alt="商品图" />
        <div class="product-info">
          <div class="product-title">{{ product.title }}</div>
          <div class="product-price">¥{{ product.price }}</div>
        </div>
      </div>
    </div>

    <!-- 动态列表 -->
    <div v-if="activeTab === 'dynamics'" class="dynamics-list">
      <div v-for="dynamic in dynamics" :key="dynamic.id" class="dynamic-card">
        <div class="dynamic-content">{{ dynamic.content }}</div>
        <img
          v-if="dynamic.image"
          class="dynamic-image"
          :src="dynamic.image"
          alt="动态图片"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'products',
      isFollowed: false,
      shop: {
        cover: 'https://img.yzcdn.cn/vant/cat.jpeg',
        avatar: 'https://img.yzcdn.cn/vant/cat.jpeg',
        name: '时尚美妆旗舰店',
        description: '优质产品，正品保证，售后无忧',
        fans: 12500,
        following: 120
      },
      products: [
        {
          id: 1,
          image: 'https://img.yzcdn.cn/vant/apple-1.jpg',
          title: '复古碎花连衣裙',
          price: 68
        },
        {
          id: 2,
          image: 'https://img.yzcdn.cn/vant/apple-2.jpg',
          title: '夏季透气T恤',
          price: 38
        },
        {
          id: 3,
          image: 'https://img.yzcdn.cn/vant/apple-3.jpg',
          title: '百搭牛仔裤',
          price: 88
        },
        {
          id: 4,
          image: 'https://img.yzcdn.cn/vant/apple-1.jpg',
          title: '时尚运动鞋',
          price: 158
        }
      ],
      dynamics: [
        {
          id: 1,
          content: '新品上架，欢迎选购！',
          image: 'https://img.yzcdn.cn/vant/apple-2.jpg'
        },
        {
          id: 2,
          content: '夏日搭配推荐，清爽又时尚。',
          image: ''
        },
        {
          id: 3,
          content: '感谢大家的支持，粉丝破万啦！',
          image: 'https://img.yzcdn.cn/vant/apple-3.jpg'
        }
      ]
    };
  },
  methods: {
    toggleFollow() {
      this.isFollowed = !this.isFollowed;
    },
    goProduct(id) {
      alert(`跳转商品详情页，商品ID: ${id}`);
    }
  }
};
</script>

<style scoped>
.shop-page {
  font-family: 'PingFang SC', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  background-color: #fff;
  color: #333;
  min-height: 100vh;
}

/* 店铺头部 */
.shop-header {
  position: relative;
  width: 100%;
  background: #fefefe;
  box-shadow: 0 2px 6px rgb(254 44 85 / 0.15);
}

.shop-cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;
}

.shop-info {
  position: relative;
  top: -50px;
  padding: 0 20px 20px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.shop-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid white;
  object-fit: cover;
  box-shadow: 0 0 8px rgba(254, 44, 85, 0.4);
}

.shop-name {
  font-size: 22px;
  font-weight: 700;
  margin-top: 10px;
}

.shop-desc {
  font-size: 14px;
  color: #666;
  margin-top: 6px;
}

.shop-stats {
  display: flex;
  margin-top: 12px;
  gap: 40px;
  justify-content: center;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-count {
  font-weight: 700;
  font-size: 18px;
  color: #fe2c55;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

/* 关注按钮 */
.btn-follow {
  margin-top: 15px;
  background: #fe2c55;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 40px;
  font-weight: 600;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s ease;
  user-select: none;
}

.btn-follow.followed {
  background: #999;
}

/* Tab 栏 */
.tab-bar {
  display: flex;
  justify-content: center;
  border-bottom: 1px solid #eee;
  margin-top: 20px;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 12px 0;
  font-weight: 600;
  font-size: 16px;
  cursor: pointer;
  color: #666;
  position: relative;
  user-select: none;
}

.tab-item.active {
  color: #fe2c55;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 30%;
  width: 40%;
  height: 3px;
  background: #fe2c55;
  border-radius: 3px 3px 0 0;
}

/* 商品列表 */
.products-list {
  display: flex;
  flex-wrap: wrap;
  padding: 12px 10px;
  gap: 12px;
  justify-content: space-between;
}

.product-card {
  width: 48%;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 0 8px rgb(0 0 0 / 0.05);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.product-card:hover {
  transform: translateY(-6px);
}

.product-card img {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.product-info {
  padding: 10px 12px;
}

.product-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  line-height: 1.3;
  height: 36px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price {
  margin-top: 6px;
  font-weight: 700;
  font-size: 16px;
  color: #fe2c55;
}

/* 动态列表 */
.dynamics-list {
  padding: 10px 20px;
}

.dynamic-card {
  background: #fff;
  padding: 12px;
  margin-bottom: 12px;
  border-radius: 12px;
  box-shadow: 0 0 8px rgb(0 0 0 / 0.05);
  user-select: none;
}

.dynamic-content {
  font-size: 14px;
  color: #555;
  margin-bottom: 8px;
  line-height: 1.5;
}

.dynamic-image {
  width: 100%;
  border-radius: 10px;
  object-fit: cover;
}
</style>
