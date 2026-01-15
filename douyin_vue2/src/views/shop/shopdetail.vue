<template>
  <div class="product-detail-page">
    <!-- 顶部轮播图 -->
    <div class="carousel">
      <img
        v-for="(img, index) in images"
        :key="index"
        :src="img"
        alt="商品图"
      />
    </div>

    <!-- 商品核心信息 -->
    <div class="product-brief">
      <div class="price-box">
        <span class="discount-price">¥{{ product.priceDiscount }}</span>
        <span class="original-price">¥{{ product.priceOriginal }}</span>
      </div>
      <div class="product-title">{{ product.title }}</div>
      <div class="tags">
        <span class="tag" v-for="(tag, i) in product.tags" :key="i">{{
          tag
        }}</span>
      </div>
    </div>

    <!-- 店铺信息 -->
    <div class="shop-info">
      <img class="touxiang" :src="shop.avatar" alt="店铺头像" />
      <div class="shop-details">
        <div class="shop-name">{{ shop.name }}</div>
        <div class="shop-description">{{ shop.description }}</div>
        <button @click="goShop">进店逛逛</button>
      </div>
    </div>

    <!-- 图文详情 -->
    <div class="detail-content" v-html="product.detailHtml"></div>
    <!-- 底部弹窗遮罩 -->
    <div v-if="showBuyPopup" class="popup-mask" @click="closeBuyPopup"></div>

    <!-- 底部弹窗 -->
    <div v-if="showBuyPopup" class="buy-popup">
      <h3>请选择商品规格</h3>
      <div class="option-group">
        <div class="option-label">颜色：</div>
        <div class="option-values">
          <span
            v-for="(color, index) in productOptions.colors"
            :key="index"
            :class="{ selected: selectedOptions.color === color }"
            @click="selectOption('color', color)"
          >
            {{ color }}
          </span>
        </div>
      </div>
      <div class="option-group">
        <div class="option-label">尺码：</div>
        <div class="option-values">
          <span
            v-for="(size, index) in productOptions.sizes"
            :key="index"
            :class="{ selected: selectedOptions.size === size }"
            @click="selectOption('size', size)"
          >
            {{ size }}
          </span>
        </div>
      </div>

      <button v-if="showmai" class="btn confirm-buy" @click="confirmBuy">
        购买
      </button>
      <button v-if="!showmai" class="btn confirm-buy" @click="confirmAddCar">
        加入购物车
      </button>
    </div>
    <!-- 底部操作栏 -->
    <footer>
      <div class="cart-icon" @click="goCart">
        <img
          src="https://img.icons8.com/ios-filled/24/fe2c55/shopping-cart.png"
        />
        <span class="cart-count">{{ cartCount }}</span>
      </div>
      <!-- 两个按钮中只显示一个，根据 actionType 判断 -->
      <button
        v-if="actionType === 'buy'"
        class="btn confirm-buy"
        @click="confirmBuy"
      >
        购买
      </button>
      <button v-else class="btn confirm-buy" @click="confirmAddCar">
        加入购物车
      </button>
    </footer>
  </div>
</template>

<script>
import request from '@/utils/request.js';
import { searchForDish } from '@/api/shop.js';

export default {
  data() {
    return {
      showBuyPopup: false,
      actionType: 'cart', // 默认显示加入购物车按钮
      images: [],
      product: {
        priceDiscount: 0,
        priceOriginal: 0,
        title: '',
        tags: [],
        detailHtml: ''
      },
      shop: {
        avatar: '',
        name: '',
        description: ''
      },
      cartCount: 0,
      productOptions: {
        colors: [],
        sizes: []
      },
      selectedOptions: {
        color: null,
        size: null
      },
      productId: 0
    };
  },
  created() {
    // 从路由参数中获取商品ID
    this.productId = this.$route.params.id || 1;
    this.loadProductData();
    this.loadCartCount();
  },
  methods: {
    // 加载商品详情数据
    async loadProductData() {
      try {
        // 获取商品详情（使用searchForDish接口，因为这是现有可用的接口）
        // 这里假设商品名称和ID相关联，可以根据实际情况调整
        const res = await searchForDish(this.productId.toString());

        if (res.code === 200 && res.data) {
          const productData = res.data;
          // 映射商品数据
          this.product = {
            priceDiscount: productData.price || 0,
            priceOriginal: productData.price || 0, // 可以根据实际情况设置原价
            title: productData.name || '商品名称',
            tags: ['包邮', '48小时内发货', '退货保障'], // 这里可以从后端获取标签
            detailHtml: `
              <h3>产品详情</h3>
              <p>${productData.description || '这是一款优质商品，品质保证。'}</p>
              ${productData.image ? `<img src="${productData.image}" style="width:100%;margin-top:10px;" />` : ''}
              <p>更多详细信息请咨询客服。</p>
            `
          };

          // 设置商品图片
          this.images = productData.image
            ? [productData.image]
            : ['https://img.yzcdn.cn/vant/apple-1.jpg'];

          // 设置店铺信息
          this.shop = {
            avatar: productData.shopImg || 'https://img.yzcdn.cn/vant/cat.jpeg',
            name: productData.shopName || '店铺名称',
            description: '品质保证，7天无理由退换货'
          };
        } else {
          // 如果接口调用失败，使用默认数据
          this.setDefaultProductData();
        }

        // 设置商品选项（这里应该从后端获取，暂时使用静态数据）
        this.productOptions = {
          colors: ['红色', '蓝色', '绿色'],
          sizes: ['S', 'M', 'L', 'XL']
        };
      } catch (error) {
        console.error('加载商品数据失败:', error);
        this.setDefaultProductData();
      }
    },

    // 设置默认商品数据
    setDefaultProductData() {
      this.product = {
        priceDiscount: 26,
        priceOriginal: 28,
        title: '时尚复古碎花连衣裙',
        tags: ['包邮', '48小时内发货', '退货保障'],
        detailHtml: `
          <h3>产品详情</h3>
          <p>这款碎花连衣裙采用高品质面料，设计时尚，适合春夏季穿着。</p>
          <img src="https://img.yzcdn.cn/vant/apple-1.jpg" style="width:100%;margin-top:10px;" />
          <p>优雅的剪裁，舒适的穿着感，是您的最佳选择！</p>
        `
      };
      this.images = [
        'https://img.yzcdn.cn/vant/apple-1.jpg',
        'https://img.yzcdn.cn/vant/apple-2.jpg',
        'https://img.yzcdn.cn/vant/apple-3.jpg'
      ];
      this.shop = {
        avatar: 'https://img.yzcdn.cn/vant/cat.jpeg',
        name: '美丽衣坊旗舰店',
        description: '品质保证，7天无理由退换货'
      };
    },

    // 加载购物车数量
    async loadCartCount() {
      try {
        // 这里应该调用获取购物车数量的接口
        // 暂时使用模拟数据
        this.cartCount = 0;
      } catch (error) {
        console.error('加载购物车数量失败:', error);
      }
    },

    // 打开弹窗选择规格
    openOptionPopup(actionType) {
      this.actionType = actionType;
      this.showBuyPopup = true;
      // 默认选中第一项
      if (this.productOptions.colors.length > 0) {
        this.selectedOptions.color = this.productOptions.colors[0];
      }
      if (this.productOptions.sizes.length > 0) {
        this.selectedOptions.size = this.productOptions.sizes[0];
      }
    },

    // 购买按钮点击
    buyNow() {
      this.openOptionPopup('buy');
    },

    // 加入购物车按钮点击
    addToCart() {
      this.openOptionPopup('cart');
    },

    // 关闭弹窗
    closeBuyPopup() {
      this.showBuyPopup = false;
    },

    // 选择商品规格
    selectOption(type, value) {
      this.selectedOptions[type] = value;
    },

    // 确认购买
    confirmBuy() {
      if (!this.selectedOptions.color || !this.selectedOptions.size) {
        this.$toast('请选择颜色和尺码');
        return;
      }

      this.showBuyPopup = false;

      // 跳转到订单页面，携带选择参数
      this.$router.push({
        path: '/order',
        query: {
          productId: this.productId,
          color: this.selectedOptions.color,
          size: this.selectedOptions.size,
          price: this.product.priceDiscount,
          name: this.product.title
        }
      });
    },

    // 确认加入购物车
    async confirmAddCar() {
      if (!this.selectedOptions.color || !this.selectedOptions.size) {
        this.$toast('请选择颜色和尺码');
        return;
      }

      try {
        // 构建购物车数据
        const cartData = {
          dishId: this.productId, // 这里假设商品ID就是dishId
          name: this.product.title,
          image: this.images[0] || '',
          dishFlavor: `${this.selectedOptions.color},${this.selectedOptions.size}`,
          number: 1,
          amount: this.product.priceDiscount
        };

        // 调用添加到购物车的接口
        const res = await request.post('/shop/shoppingCart/add', cartData);

        if (res.code === 200) {
          this.showBuyPopup = false;
          this.cartCount++;
          this.$toast('加入购物车成功');
        } else {
          this.$toast('加入购物车失败，请重试');
        }
      } catch (error) {
        console.error('加入购物车失败:', error);
        this.$toast('加入购物车失败，请重试');
      }
    },

    // 跳转到店铺页面
    goShop() {
      this.$router.push('/store');
    },

    // 跳转到购物车页面
    goCart() {
      this.$router.push('/shopcat');
    }
  }
};
</script>

<style scoped>
/* 弹窗遮罩 */
.popup-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 999;
}

/* 弹窗主体 */
.buy-popup {
  height: 60vh;
  position: fixed;
  font-size: 0.5rem;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20px;
  border-top-left-radius: 15px;
  border-top-right-radius: 15px;
  z-index: 1000;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.15);
}

/* 规格选择 */
.option-group {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}
.option-label {
  width: 60px;
  font-weight: 600;
}
.option-values span {
  display: inline-block;
  padding: 6px 12px;
  margin-right: 10px;
  border: 1px solid #ccc;
  border-radius: 15px;
  cursor: pointer;
  user-select: none;
  transition: all 0.3s;
}
.option-values span.selected {
  border-color: #fe2c55;
  background-color: #fe2c55;
  color: white;
}

/* 购买按钮 */
.confirm-buy {
  width: 100%;
  background: #fe2c55;
  border: none;
  color: #fff;
  font-weight: 700;
  font-size: 18px;
  border-radius: 25px;
  padding: 12px 0;
  cursor: pointer;
  transition: background 0.3s;
}
.confirm-buy:hover {
  background: #e02a4e;
}

.product-detail-page {
  background-color: #f9f9f9;
  padding-bottom: 60px; /* 底部按钮高度 */
  font-family: 'PingFang SC', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  color: #333;
}

/* 轮播图 */
.carousel {
  width: 100%;
  height: 100vw;
  overflow: hidden;
  position: relative;
}
.carousel img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

/* 商品信息 */
.product-brief {
  background-color: #fff;
  border-radius: 20px 20px 0 0;
  margin-top: -20px;
  padding: 16px 20px;
  position: relative;
  z-index: 2;
}

.price-box {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.discount-price {
  font-size: 22px;
  font-weight: 700;
  color: #fe2c55;
}

.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}

.product-title {
  font-size: 18px;
  font-weight: 500;
  margin-top: 10px;
  line-height: 1.4;
}

.tags {
  margin-top: 12px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.tag {
  font-size: 12px;
  padding: 3px 10px;
  background-color: #f5f5f5;
  border-radius: 15px;
  color: #666;
  user-select: none;
}

/* 店铺信息 */
.shop-info {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  background: #fff;
  margin-top: 10px;
  border-radius: 10px;
}

.touxiang {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.shop-details {
  margin-left: 12px;
  flex: 1;
}

.shop-name {
  font-size: 16px;
  font-weight: 600;
}

.shop-description {
  font-size: 12px;
  color: #888;
  margin-top: 6px;
}

.shop-details button {
  margin-top: 8px;
  background: linear-gradient(90deg, #fe2c55, #ff6680);
  border: none;
  color: #fff;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  cursor: pointer;
  transition: background 0.3s;
}
.shop-details button:hover {
  background: linear-gradient(90deg, #ff4c75, #ff7a96);
}

/* 图文详情 */
.detail-content {
  background: #fff;
  margin-top: 10px;
  padding: 15px 20px;
  border-radius: 10px;
  color: #555;
  font-size: 14px;
  line-height: 1.6;
}

.detail-content h3 {
  margin-bottom: 10px;
  font-weight: 600;
}

/* 底部操作栏 */
footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  display: flex;
  align-items: center;
  padding: 8px 15px;
  border-top: 1px solid #eee;
  z-index: 999;
  gap: 10px;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.05);
}

.cart-icon {
  position: relative;
  width: 40px;
  height: 40px;
  cursor: pointer;
}
.cart-icon img {
  width: 24px;
  height: 24px;
  filter: invert(36%) sepia(82%) saturate(3297%) hue-rotate(328deg)
    brightness(96%) contrast(101%);
}
.cart-count {
  position: absolute;
  top: -5px;
  right: -5px;
  background: #fe2c55;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  font-weight: 700;
  user-select: none;
}

.btn {
  flex: 1;
  border: none;
  color: #fff;
  font-weight: 700;
  font-size: 16px;
  border-radius: 30px;
  padding: 12px 0;
  cursor: pointer;
  transition: background 0.3s;
  user-select: none;
}

.add-cart {
  background: #fe2c55;
}
.add-cart:hover {
  background: #e02a4e;
}

.buy-now {
  background: linear-gradient(90deg, #fe2c55, #ff6680);
}
.buy-now:hover {
  background: linear-gradient(90deg, #ff4c75, #ff7a96);
}
</style>
