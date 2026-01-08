<template>
  <div class="orders-page">
    <!-- 顶部导航 -->
    <header class="orders-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">我的订单</div>
    </header>

    <!-- 订单状态筛选 -->
    <div class="order-tabs">
      <div class="tab-item active" @click="switchTab('all')">全部</div>
      <div class="tab-item" @click="switchTab('pending_payment')">待付款</div>
      <div class="tab-item" @click="switchTab('pending_shipping')">待发货</div>
      <div class="tab-item" @click="switchTab('pending_receipt')">待收货</div>
      <div class="tab-item" @click="switchTab('completed')">已完成</div>
      <div class="tab-item" @click="switchTab('cancelled')">已取消</div>
    </div>

    <!-- 订单列表 -->
    <div class="orders-list">
      <div class="order-item" v-for="order in orders" :key="order.id" @click="goToOrderDetail(order.id)">
        <!-- 订单头部 -->
        <div class="order-header">
          <div class="order-status">{{ order.statusText }}</div>
          <div class="order-time">{{ order.time }}</div>
        </div>

        <!-- 订单商品 -->
        <div class="order-products">
          <div class="product-item">
            <div class="product-image">
              <img :src="order.product.image" :alt="order.product.title">
            </div>
            <div class="product-info">
              <div class="product-title">{{ order.product.title }}</div>
              <div class="product-specs">{{ order.product.specs }}</div>
              <div class="product-price">¥{{ order.product.price }} × {{ order.product.quantity }}</div>
            </div>
          </div>
        </div>

        <!-- 订单金额 -->
        <div class="order-amount">
          <div class="amount-label">实付款</div>
          <div class="amount-value">¥{{ order.totalAmount }}</div>
        </div>

        <!-- 订单操作 -->
        <div class="order-actions">
          <button class="action-btn secondary" v-if="order.status === 'pending_payment'" @click.stop="cancelOrder(order.id)">取消订单</button>
          <button class="action-btn primary" v-if="order.status === 'pending_payment'" @click.stop="payOrder(order.id)">立即付款</button>
          <button class="action-btn primary" v-if="order.status === 'pending_receipt'" @click.stop="confirmReceipt(order.id)">确认收货</button>
          <button class="action-btn secondary" v-if="order.status === 'completed'" @click.stop="viewLogistics(order.id)">查看物流</button>
          <button class="action-btn primary" v-if="order.status === 'completed'" @click.stop="leaveComment(order.id)">评价</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils'
import routerUtils from '../../utils/routerUtils'
import { get, put } from '../../utils/apiUtils'

export default {
  name: 'OrdersPage',
  data() {
    return {
      activeTab: 'all',
      orders: [],
      originalOrders: [] // 用于筛选的原始数据
    }
  },
  mounted() {
    // 设置页面标题
    nativeUtils.setTitleBar('我的订单')
    // 加载订单数据
    this.loadOrders()
  },
  methods: {
    // 从API或localStorage加载订单数据
    loadOrders() {
      try {
        nativeUtils.showLoading('加载中...')
        
        // 获取当前用户信息
        const userInfo = localStorage.getItem('user')
        if (userInfo) {
          const user = JSON.parse(userInfo)
          const userId = user.id
          
          // 尝试从API获取数据
          get(`/order/user/${userId}`)
            .then(response => {
              if (response.code === 0) {
                // API请求成功
                this.originalOrders = response.data.map(order => ({
                  ...order,
                  statusText: this.getStatusText(order.status),
                  product: {
                    ...order.product,
                    image: order.product.images && order.product.images.length > 0 ? order.product.images[0] : 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
                  }
                }))
                // 保存到localStorage
                localStorage.setItem('orders', JSON.stringify(this.originalOrders))
                this.applyFilter()
              } else {
                // API请求失败，使用localStorage数据
                this.loadFromLocalStorage()
              }
              nativeUtils.hideLoading()
            })
            .catch(error => {
              console.error('加载订单失败:', error)
              // API请求失败，使用localStorage数据
              this.loadFromLocalStorage()
              nativeUtils.hideLoading()
            })
        } else {
          // 用户未登录，跳转登录页
          nativeUtils.hideLoading()
          nativeUtils.showToast('请先登录', 1500)
          setTimeout(() => {
            routerUtils.navigateTo('/login.html')
          }, 1500)
        }
      } catch (error) {
        console.error('加载订单失败:', error)
        this.loadFromLocalStorage()
        nativeUtils.hideLoading()
      }
    },
    
    // 从localStorage加载订单数据
    loadFromLocalStorage() {
      try {
        const ordersStr = localStorage.getItem('orders')
        if (ordersStr) {
          this.originalOrders = JSON.parse(ordersStr)
          this.applyFilter()
          nativeUtils.showToast('使用本地缓存数据', 1500)
          return
        }
      } catch (error) {
        console.error('解析本地数据失败:', error)
      }
      
      // 使用默认mock数据
      this.originalOrders = [
        {
          id: 1,
          status: 'pending_payment',
          statusText: '待付款',
          time: '2025-12-25 10:30',
          totalAmount: 5999,
          product: {
            id: 1,
            title: 'iPhone 13 Pro 256GB 远峰蓝',
            specs: '颜色: 远峰蓝 | 内存: 256GB',
            price: 5999,
            quantity: 1,
            image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
          }
        },
        {
          id: 2,
          status: 'pending_shipping',
          statusText: '待发货',
          time: '2025-12-24 15:20',
          totalAmount: 1899,
          product: {
            id: 3,
            title: 'AirPods Pro 第二代',
            specs: '版本: 国行',
            price: 1899,
            quantity: 1,
            image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
          }
        },
        {
          id: 3,
          status: 'pending_receipt',
          statusText: '待收货',
          time: '2025-12-23 09:15',
          totalAmount: 3999,
          product: {
            id: 4,
            title: '小米13 12GB+256GB',
            specs: '颜色: 黑色 | 内存: 12GB+256GB',
            price: 3999,
            quantity: 1,
            image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
          }
        },
        {
          id: 4,
          status: 'completed',
          statusText: '已完成',
          time: '2025-12-20 14:45',
          totalAmount: 2599,
          product: {
            id: 5,
            title: '索尼 WH-1000XM5 降噪耳机',
            specs: '颜色: 黑色',
            price: 2599,
            quantity: 1,
            image: 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
          }
        }
      ]
      
      // 保存到localStorage
      try {
        localStorage.setItem('orders', JSON.stringify(this.originalOrders))
      } catch (error) {
        console.error('保存订单数据失败:', error)
      }
      
      this.applyFilter()
      nativeUtils.showToast('使用默认数据', 1500)
    },
    
    // 应用筛选条件
    applyFilter() {
      let filtered = [...this.originalOrders]
      
      switch (this.activeTab) {
        case 'pending_payment':
          filtered = filtered.filter(order => order.status === 'pending_payment')
          break
        case 'pending_shipping':
          filtered = filtered.filter(order => order.status === 'pending_shipping')
          break
        case 'pending_receipt':
          filtered = filtered.filter(order => order.status === 'pending_receipt')
          break
        case 'completed':
          filtered = filtered.filter(order => order.status === 'completed')
          break
        case 'cancelled':
          filtered = filtered.filter(order => order.status === 'cancelled')
          break
        default:
          // 全部
          break
      }
      
      this.orders = filtered
    },
    
    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1)
    },
    
    // 获取订单状态文本
    getStatusText(status) {
      const statusMap = {
        'pending_payment': '待付款',
        'pending_shipping': '待发货',
        'pending_receipt': '待收货',
        'completed': '已完成',
        'cancelled': '已取消'
      }
      return statusMap[status] || '未知状态'
    },
    
    // 切换筛选标签
    switchTab(tab) {
      this.activeTab = tab
      this.applyFilter()
    },
    
    // 跳转到订单详情页
    goToOrderDetail(orderId) {
      routerUtils.navigateTo(`/orderDetail.html?id=${orderId}`)
    },
    
    // 取消订单
    cancelOrder(orderId) {
      if (confirm('确定要取消这个订单吗？')) {
        // 调用API取消订单
        nativeUtils.showLoading('取消中...')
        put(`/order/${orderId}/status?status=cancelled`)
          .then(response => {
            if (response.code === 0) {
              // API请求成功
              // 更新本地数据
              const order = this.originalOrders.find(item => item.id === orderId)
              if (order) {
                order.status = 'cancelled'
                order.statusText = this.getStatusText('cancelled')
                // 保存到localStorage
                localStorage.setItem('orders', JSON.stringify(this.originalOrders))
                // 重新应用筛选
                this.applyFilter()
              }
              nativeUtils.hideLoading()
              nativeUtils.showToast('订单已取消', 1500)
            } else {
              nativeUtils.hideLoading()
              nativeUtils.showToast(response.message || '取消失败', 1500)
            }
          })
          .catch(error => {
            console.error('取消订单失败:', error)
            nativeUtils.hideLoading()
            nativeUtils.showToast('取消失败，请重试', 1500)
          })
      }
    },
    
    // 立即付款
    payOrder() {
      nativeUtils.showToast('立即付款功能开发中', 1500)
    },
    
    // 确认收货
    confirmReceipt(orderId) {
      if (confirm('确定要确认收货吗？')) {
        // 调用API确认收货
        nativeUtils.showLoading('确认中...')
        put(`/order/${orderId}/status?status=completed`)
          .then(response => {
            if (response.code === 0) {
              // API请求成功
              // 更新本地数据
              const order = this.originalOrders.find(item => item.id === orderId)
              if (order) {
                order.status = 'completed'
                order.statusText = this.getStatusText('completed')
                // 保存到localStorage
                localStorage.setItem('orders', JSON.stringify(this.originalOrders))
                // 重新应用筛选
                this.applyFilter()
              }
              nativeUtils.hideLoading()
              nativeUtils.showToast('已确认收货', 1500)
            } else {
              nativeUtils.hideLoading()
              nativeUtils.showToast(response.message || '确认失败', 1500)
            }
          })
          .catch(error => {
            console.error('确认收货失败:', error)
            nativeUtils.hideLoading()
            nativeUtils.showToast('确认失败，请重试', 1500)
          })
      }
    },
    
    // 查看物流
    viewLogistics() {
      nativeUtils.showToast('查看物流功能开发中', 1500)
    },
    
    // 评价
    leaveComment() {
      nativeUtils.showToast('评价功能开发中', 1500)
    }
  }
}
</script>

<style scoped>
.orders-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.orders-header {
  display: flex;
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
  margin-left: 20px;
}

/* 订单状态筛选 */
.order-tabs {
  display: flex;
  background-color: white;
  padding: 15px 0;
  overflow-x: auto;
  border-bottom: 1px solid #eee;
}

.tab-item {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #666;
  padding: 0 10px;
  cursor: pointer;
  white-space: nowrap;
}

.tab-item.active {
  color: #42b983;
  font-weight: bold;
  position: relative;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -16px;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  background-color: #42b983;
}

/* 订单列表 */
.orders-list {
  padding: 10px;
}

.order-item {
  background-color: white;
  border-radius: 10px;
  margin-bottom: 15px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

/* 订单头部 */
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.order-status {
  font-size: 16px;
  font-weight: bold;
  color: #ff4444;
}

.order-time {
  font-size: 12px;
  color: #999;
}

/* 订单商品 */
.order-products {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.product-item {
  display: flex;
}

.product-image {
  width: 80px;
  height: 80px;
  border-radius: 5px;
  overflow: hidden;
  margin-right: 15px;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
}

.product-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-specs {
  font-size: 12px;
  color: #999;
  margin-bottom: 5px;
}

.product-price {
  font-size: 14px;
  color: #333;
  font-weight: bold;
}

/* 订单金额 */
.order-amount {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.amount-label {
  font-size: 14px;
  color: #666;
  margin-right: 10px;
}

.amount-value {
  font-size: 16px;
  font-weight: bold;
  color: #ff4444;
}

/* 订单操作 */
.order-actions {
  display: flex;
  justify-content: flex-end;
  padding: 15px;
}

.action-btn {
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  margin-left: 10px;
  border: 1px solid #ddd;
  background-color: white;
}

.action-btn.primary {
  background-color: #42b983;
  color: white;
  border-color: #42b983;
}

.action-btn.secondary {
  color: #666;
}
</style>
