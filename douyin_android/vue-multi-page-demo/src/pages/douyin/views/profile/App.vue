<template>
  <div class="profile-page">
    <!-- 顶部导航 -->
    <header class="profile-header">
      <div class="header-title">我的</div>
      <div class="header-right" @click="goToSettings">
        <i class="settings-icon">⚙️</i>
      </div>
    </header>

    <!-- 用户信息卡片 -->
    <div class="user-card" @click="goToEditProfile">
      <div class="user-avatar">
        <img :src="userInfo.avatar" :alt="userInfo.nickname">
      </div>
      <div class="user-info">
        <div class="user-nickname">{{ userInfo.nickname }}</div>
        <div class="user-phone">{{ userInfo.phone }}</div>
      </div>
      <div class="card-arrow">→</div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-section">
      <div class="stat-item" @click="goToMyProducts">
        <div class="stat-number">{{ stats.myProducts }}</div>
        <div class="stat-label">我的商品</div>
      </div>
      <div class="stat-item" @click="goToMyOrders">
        <div class="stat-number">{{ stats.myOrders }}</div>
        <div class="stat-label">我的订单</div>
      </div>
      <div class="stat-item" @click="goToFavorites">
        <div class="stat-number">{{ stats.favorites }}</div>
        <div class="stat-label">我的收藏</div>
      </div>
      <div class="stat-item" @click="goToCart">
        <div class="stat-number">{{ stats.cart }}</div>
        <div class="stat-label">购物车</div>
      </div>
      <div class="stat-item" @click="goToMessages">
        <div class="stat-number">{{ stats.messages }}</div>
        <div class="stat-label">我的消息</div>
      </div>
    </div>

    <!-- 功能列表 -->
    <div class="function-list">
      <div class="function-group">
        <div class="function-item" @click="goToMyProducts">
          <div class="function-icon">📦</div>
          <div class="function-text">我发布的商品</div>
          <div class="function-arrow">→</div>
        </div>
        <div class="function-item" @click="goToMyOrders">
          <div class="function-icon">📋</div>
          <div class="function-text">我的订单</div>
          <div class="function-arrow">→</div>
        </div>
        <div class="function-item" @click="goToFavorites">
          <div class="function-icon">❤️</div>
          <div class="function-text">我的收藏</div>
          <div class="function-arrow">→</div>
        </div>
        <div class="function-item" @click="goToCart">
          <div class="function-icon">🛒</div>
          <div class="function-text">购物车</div>
          <div class="function-arrow">→</div>
        </div>
      </div>

      <div class="function-group">
        <div class="function-item" @click="goToAddressBook">
          <div class="function-icon">📍</div>
          <div class="function-text">收货地址</div>
          <div class="function-arrow">→</div>
        </div>
        <div class="function-item" @click="goToCustomerService">
          <div class="function-icon">💬</div>
          <div class="function-text">客服中心</div>
          <div class="function-arrow">→</div>
        </div>
        <div class="function-item" @click="goToHelpCenter">
          <div class="function-icon">❓</div>
          <div class="function-text">帮助中心</div>
          <div class="function-arrow">→</div>
        </div>
      </div>

      <div class="function-group">
        <div class="function-item" @click="goToSettings">
          <div class="function-icon">⚙️</div>
          <div class="function-text">设置</div>
          <div class="function-arrow">→</div>
        </div>
        <div class="function-item" @click="logout">
          <div class="function-icon" style="color: #ff4444;">🚪</div>
          <div class="function-text" style="color: #ff4444;">退出登录</div>
          <div class="function-arrow" style="color: #ff4444;">→</div>
        </div>
      </div>
    </div>

    <!-- 版本信息 -->
    <div class="version-info">
      <span class="version-text">版本号：{{ appVersion }}</span>
    </div>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils'
import routerUtils from '../../utils/routerUtils'

export default {
  name: 'ProfilePage',
  data() {
    return {
      // 用户信息
      userInfo: {
        avatar: 'https://via.placeholder.com/100x100?text=Avatar',
        nickname: '二手交易用户',
        phone: '138****8888'
      },
      // 统计数据
      stats: {
        myProducts: 3,
        myOrders: 5,
        favorites: 12,
        messages: 2,
        cart: 0
      },
      // 应用版本
      appVersion: 'v1.0.0'
    }
  },
  mounted() {
    // 页面加载时设置标题
    nativeUtils.setTitleBar('个人中心')
    // 从localStorage获取用户信息
    this.getUserInfoFromStorage()
    // 获取购物车数据
    this.updateCartCount()
  },
  methods: {
    // 从localStorage获取用户信息
    getUserInfoFromStorage() {
      try {
        const userStr = localStorage.getItem('user')
        if (userStr) {
          const user = JSON.parse(userStr)
          this.userInfo = {
            avatar: user.avatar || 'https://via.placeholder.com/100x100?text=Avatar',
            nickname: user.nickname || '二手交易用户',
            phone: user.phone || '138****8888'
          }
        }
      } catch (error) {
        console.error('从localStorage获取用户信息失败:', error)
      }
    },
    
    // 更新购物车数量
    updateCartCount() {
      try {
        const cartStr = localStorage.getItem('cart')
        if (cartStr) {
          const cart = JSON.parse(cartStr)
          // 计算购物车商品总数量
          this.stats.cart = cart.reduce((total, item) => total + item.quantity, 0)
        } else {
          this.stats.cart = 0
        }
      } catch (error) {
        console.error('获取购物车数据失败:', error)
        this.stats.cart = 0
      }
    },
    
    // 跳转到设置页面
    goToSettings() {
      routerUtils.navigateTo('/settings.html')
    },
    
    // 跳转到编辑个人资料页面
    goToEditProfile() {
      nativeUtils.showToast('编辑个人资料页面开发中', 1500)
    },
    
    // 跳转到我的商品页面
    goToMyProducts() {
      routerUtils.navigateTo('/myProducts.html')
    },
    
    // 跳转到我的订单页面
    goToMyOrders() {
      routerUtils.navigateTo('/orders.html')
    },
    
    // 跳转到我的收藏页面
    goToFavorites() {
      routerUtils.navigateTo('/favorites.html')
    },
    
    // 跳转到我的消息页面
    goToMessages() {
      routerUtils.navigateTo('/messages.html')
    },
    
    // 跳转到购物车页面
    goToCart() {
      routerUtils.navigateTo('/cart.html')
    },
    
    // 跳转到收货地址页面
    goToAddressBook() {
      routerUtils.navigateTo('/address.html')
    },
    
    // 跳转到客服中心
    goToCustomerService() {
      nativeUtils.showToast('客服中心开发中', 1500)
    },
    
    // 跳转到帮助中心
    goToHelpCenter() {
      nativeUtils.showToast('帮助中心开发中', 1500)
    },
    
    // 退出登录
    logout() {
      nativeUtils.showLoading('退出中...')
      setTimeout(() => {
        // 清除localStorage中的用户信息和token
        localStorage.removeItem('user')
        localStorage.removeItem('token')
        localStorage.removeItem('userId')
        localStorage.removeItem('userNickname')
        localStorage.removeItem('userAvatar')
        
        nativeUtils.hideLoading()
        nativeUtils.showToast('已退出登录', 1500)
        routerUtils.navigateTo('/login.html')
      }, 1500)
    }
  }
}
</script>

<style scoped>
.profile-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部导航 */
.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #42b983;
  color: white;
  padding: 15px 20px;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
}

.settings-icon {
  font-size: 20px;
  cursor: pointer;
}

/* 用户信息卡片 */
.user-card {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 20px;
  margin: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.user-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info {
  flex: 1;
}

.user-nickname {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.user-phone {
  font-size: 14px;
  color: #999;
}

.card-arrow {
  color: #999;
  font-size: 18px;
}

/* 统计信息 */
.stats-section {
  display: flex;
  background-color: white;
  padding: 20px 0;
  margin: 0 15px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-wrap: wrap;
}

.stat-item {
  flex: 1;
  min-width: 20%;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 0 10px;
  box-sizing: border-box;
}

.stat-number {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 12px;
  color: #999;
  text-align: center;
  word-break: break-word;
}

/* 功能列表 */
.function-list {
  margin: 15px;
}

.function-group {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  margin-bottom: 15px;
  overflow: hidden;
}

.function-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.function-item:last-child {
  border-bottom: none;
}

.function-icon {
  font-size: 20px;
  color: #666;
  margin-right: 15px;
  width: 24px;
  text-align: center;
}

.function-text {
  flex: 1;
  font-size: 16px;
  color: #333;
}

.function-arrow {
  color: #999;
  font-size: 18px;
}

/* 版本信息 */
.version-info {
  text-align: center;
  padding: 20px;
  color: #999;
  font-size: 14px;
  margin-top: auto;
}
</style>
