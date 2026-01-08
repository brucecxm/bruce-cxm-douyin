<template>
  <div class="cart-page">
    <!-- 顶部导航 -->
    <header class="cart-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">购物车</div>
      <div class="header-right" @click="clearCart">
        <span class="clear-text">清空</span>
      </div>
    </header>

    <!-- 购物车内容区域 -->
    <div class="cart-content">
      <!-- 购物车为空时 -->
      <div class="cart-empty" v-if="cartItems.length === 0">
        <div class="empty-icon">🛒</div>
        <div class="empty-text">购物车是空的</div>
        <button class="go-shopping-btn" @click="goToIndex">去逛逛</button>
      </div>
      
      <!-- 购物车商品列表 -->
      <div class="cart-items" v-else>
        <div class="cart-item" v-for="item in cartItems" :key="item.id">
          <div class="item-checkbox">
            <input type="checkbox" v-model="item.selected">
          </div>
          <div class="item-image">
            <img :src="item.image" :alt="item.title" @click="goToDetail(item.id)">
          </div>
          <div class="item-info">
            <div class="item-title" @click="goToDetail(item.id)">{{ item.title }}</div>
            <div class="item-tags">
              <span class="tag" v-for="tag in item.tags" :key="tag">{{ tag }}</span>
            </div>
            <div class="item-price">¥{{ item.price }}</div>
            <div class="item-actions">
              <div class="quantity-control">
                <button class="quantity-btn minus" @click="decreaseQuantity(item)">-</button>
                <input type="number" class="quantity-input" v-model.number="item.quantity" min="1">
                <button class="quantity-btn plus" @click="increaseQuantity(item)">+</button>
              </div>
              <button class="delete-btn" @click="removeItem(item)">删除</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部结算栏 -->
    <footer class="cart-footer" v-if="cartItems.length > 0">
      <div class="select-all">
        <input type="checkbox" v-model="selectAll">
        <span class="select-all-text">全选</span>
      </div>
      <div class="total-section">
        <div class="total-text">合计：</div>
        <div class="total-price">¥{{ totalPrice }}</div>
      </div>
      <button class="checkout-btn" @click="checkout">
        结算 ({{ selectedCount }})
      </button>
    </footer>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils'
import routerUtils from '../../utils/routerUtils'

export default {
  name: 'CartPage',
  data() {
    return {
      cartItems: [],
      selectAll: false
    }
  },
  computed: {
    // 总价格
    totalPrice() {
      return this.cartItems
        .filter(item => item.selected)
        .reduce((total, item) => total + (item.price * item.quantity), 0)
        .toFixed(2)
    },
    // 选中商品数量
    selectedCount() {
      return this.cartItems.filter(item => item.selected).length
    }
  },
  mounted() {
    // 页面加载时设置标题
    nativeUtils.setTitleBar('购物车')
    // 加载购物车数据
    this.loadCartItems()
  },
  watch: {
    // 监听购物车数据变化，保存到本地存储
    cartItems: {
      deep: true,
      handler(newVal) {
        localStorage.setItem('cart', JSON.stringify(newVal))
      }
    },
    // 监听全选状态变化
    selectAll(newVal) {
      this.cartItems.forEach(item => {
        item.selected = newVal
      })
    }
  },
  methods: {
    // 加载购物车数据
    loadCartItems() {
      try {
        const cartStr = localStorage.getItem('cart')
        if (cartStr) {
          this.cartItems = JSON.parse(cartStr).map(item => ({
            ...item,
            selected: true // 默认选中
          }))
        }
      } catch (error) {
        console.error('加载购物车失败:', error)
        nativeUtils.showToast('加载购物车失败', 1500)
      }
    },
    
    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1)
    },
    
    // 跳转到首页
    goToIndex() {
      routerUtils.navigateTo('/index.html')
    },
    
    // 跳转到商品详情
    goToDetail(productId) {
      routerUtils.navigateTo(`/detail.html?id=${productId}`)
    },
    
    // 增加商品数量
    increaseQuantity(item) {
      item.quantity++
    },
    
    // 减少商品数量
    decreaseQuantity(item) {
      if (item.quantity > 1) {
        item.quantity--
      }
    },
    
    // 删除商品
    removeItem(item) {
      const index = this.cartItems.findIndex(i => i.id === item.id)
      if (index > -1) {
        this.cartItems.splice(index, 1)
        nativeUtils.showToast('已删除', 1500)
      }
    },
    
    // 清空购物车
    clearCart() {
      if (this.cartItems.length === 0) return
      
      if (confirm('确定要清空购物车吗？')) {
        this.cartItems = []
        localStorage.removeItem('cart')
        this.selectAll = false
        nativeUtils.showToast('购物车已清空', 1500)
      }
    },
    
    // 结算
    checkout() {
      const selectedItems = this.cartItems.filter(item => item.selected)
      if (selectedItems.length === 0) {
        nativeUtils.showToast('请选择要结算的商品', 1500)
        return
      }
      
      // 实际项目中替换为真实结算逻辑
      nativeUtils.showToast('结算功能开发中', 1500)
    }
  }
}
</script>

<style scoped>
.cart-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部导航 */
.cart-header {
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

.clear-text {
  font-size: 14px;
  color: #ff4444;
  cursor: pointer;
}

/* 购物车内容区域 */
.cart-content {
  flex: 1;
  padding: 10px;
}

/* 购物车为空时 */
.cart-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
  text-align: center;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 20px;
  color: #ddd;
}

.empty-text {
  font-size: 16px;
  color: #999;
  margin-bottom: 20px;
}

.go-shopping-btn {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 25px;
  font-size: 14px;
  cursor: pointer;
}

.go-shopping-btn:hover {
  background-color: #3aa876;
}

/* 购物车商品列表 */
.cart-items {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-checkbox {
  margin-right: 15px;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 5px;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
}

.item-info {
  flex: 1;
  min-width: 0;
}

.item-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}

.item-tags {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 5px;
}

.tag {
  display: inline-block;
  background-color: #f0f0f0;
  color: #666;
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 10px;
  margin-right: 5px;
  margin-bottom: 5px;
}

.item-price {
  font-size: 16px;
  font-weight: bold;
  color: #ff4444;
  margin-bottom: 10px;
}

.item-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 数量控制 */
.quantity-control {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.quantity-btn {
  width: 30px;
  height: 30px;
  border: none;
  background-color: #f5f5f5;
  color: #333;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.quantity-btn:hover {
  background-color: #e0e0e0;
}

.quantity-input {
  width: 50px;
  height: 30px;
  border: none;
  text-align: center;
  font-size: 14px;
  outline: none;
}

.delete-btn {
  background-color: white;
  color: #ff4444;
  border: 1px solid #ff4444;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

.delete-btn:hover {
  background-color: #fff5f5;
}

/* 底部结算栏 */
.cart-footer {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 15px 20px;
  border-top: 1px solid #eee;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
}

.select-all {
  display: flex;
  align-items: center;
  margin-right: 20px;
  cursor: pointer;
}

.select-all-text {
  margin-left: 5px;
  font-size: 14px;
  color: #333;
}

.total-section {
  flex: 1;
  display: flex;
  align-items: center;
}

.total-text {
  font-size: 14px;
  color: #666;
  margin-right: 10px;
}

.total-price {
  font-size: 20px;
  font-weight: bold;
  color: #ff4444;
}

.checkout-btn {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 25px;
  font-size: 14px;
  cursor: pointer;
  min-width: 120px;
  text-align: center;
}

.checkout-btn:hover {
  background-color: #3aa876;
}
</style>