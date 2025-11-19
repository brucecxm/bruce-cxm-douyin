<template>
  <div class="order-list-page">
    <!-- 页面头部 -->
    <div class="header">
      <div class="back-button" @click="goBack">
        <i class="fas fa-arrow-left"></i>
      </div>
      <div class="title">我的订单</div>
      <div class="right"></div>
    </div>

    <!-- 订单状态筛选 -->
    <div class="status-tabs">
      <div
        class="tab"
        :class="{ active: currentStatus === 'all' }"
        @click="switchStatus('all')"
      >
        全部
      </div>
      <div
        class="tab"
        :class="{ active: currentStatus === 'pending' }"
        @click="switchStatus('pending')"
      >
        待付款
      </div>
      <div
        class="tab"
        :class="{ active: currentStatus === 'paid' }"
        @click="switchStatus('paid')"
      >
        待发货
      </div>
      <div
        class="tab"
        :class="{ active: currentStatus === 'shipped' }"
        @click="switchStatus('shipped')"
      >
        待收货
      </div>
      <div
        class="tab"
        :class="{ active: currentStatus === 'completed' }"
        @click="switchStatus('completed')"
      >
        已完成
      </div>
    </div>

    <!-- 订单列表 -->
    <div class="order-list">
      <div v-for="order in filteredOrders" :key="order.id" class="order-item">
        <!-- 店铺信息 -->
        <div class="shop-info">
          <div class="shop-name">{{ order.shopName }}</div>
          <div class="shop-avatar">
            <img :src="order.shopAvatar" alt="店铺头像" />
          </div>
        </div>

        <!-- 订单商品列表 -->
        <div class="order-products">
          <div
            v-for="(product, index) in order.products"
            :key="index"
            class="product-item"
          >
            <img :src="product.image" alt="商品图片" />
            <div class="product-info">
              <div class="product-title">{{ product.name }}</div>
              <div class="product-specs">{{ product.specs }}</div>
              <div class="product-price-count">
                <span class="price">¥{{ product.price.toFixed(2) }}</span>
                <span class="count">x{{ product.quantity }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 订单总价 -->
        <div class="order-total">
          共{{ order.totalQuantity }}件商品 合计：
          <span class="total-price">¥{{ order.totalPrice.toFixed(2) }}</span>
        </div>

        <!-- 订单状态和操作 -->
        <div class="order-status-action">
          <div class="order-status" :class="order.status">
            {{ getStatusText(order.status) }}
          </div>
          <div class="order-actions">
            <button
              v-if="order.status === 'pending'"
              class="action-btn cancel"
              @click="cancelOrder(order.id)"
            >
              取消订单
            </button>
            <button
              v-if="order.status === 'pending'"
              class="action-btn pay"
              @click="payOrder(order.id, order.totalPrice)"
            >
              去支付
            </button>
            <button
              v-if="order.status === 'shipped'"
              class="action-btn receive"
              @click="confirmReceipt(order.id)"
            >
              确认收货
            </button>
            <button
              v-if="order.status === 'completed'"
              class="action-btn review"
              @click="reviewOrder(order.id)"
            >
              评价
            </button>
            <button
              v-if="order.status !== 'pending'"
              class="action-btn view"
              @click="viewOrderDetail(order.id)"
            >
              查看详情
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="filteredOrders.length === 0" class="empty-order">
      <img src="https://img.yzcdn.cn/vant/empty.png" alt="空订单" />
      <p>暂无相关订单</p>
      <button class="go-shopping" @click="goShopping">去逛逛</button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js';
import { Toast } from 'vant';
export default {
  data() {
    return {
      currentStatus: 'all', // 默认显示全部订单
      orders: [],
      userId: '',
      // 后端Integer状态值到前端字符串状态值的映射
      statusMap: {
        1: 'pending', // 待付款
        2: 'paid', // 待派送
        3: 'shipped', // 已派送/待收货
        4: 'completed', // 已完成
        5: 'cancelled' // 已取消
      },
      // 前端字符串状态值到后端Integer状态值的映射
      reverseStatusMap: {
        pending: 1,
        paid: 2,
        shipped: 3,
        completed: 4,
        cancelled: 5
      }
    };
  },
  created() {
    this.loadUserInfo();
    this.loadOrderList();
  },
  computed: {
    // 根据当前选中的状态筛选订单
    filteredOrders() {
      if (this.currentStatus === 'all') {
        return this.orders;
      }
      return this.orders.filter((order) => order.status === this.currentStatus);
    }
  },
  methods: {
    // 返回上一页
    goBack() {
      this.$router.back();
    },

    // 加载用户信息
    loadUserInfo() {
      try {
        // 从Vuex获取用户信息
        const userInfoMap = this.$store.state.userInfo.userInfo;
        this.userId = userInfoMap?.userId || '';
      } catch (error) {
        console.error('加载用户信息失败:', error);
      }
    },

    // 加载订单列表
    async loadOrderList() {
      try {
        const res = await request.get('/shop/order/list', {
          params: {
            userId: this.userId
          }
        });

        if (res.code === 200 && res.data) {
          this.orders = res.data.map((order) => ({
            id: order.id,
            orderNo: order.number || '', // 后端字段名为number
            shopName: order.shopName || '店铺名称',
            shopAvatar:
              order.shopAvatar || 'https://img.yzcdn.cn/vant/cat.jpeg',
            // 将后端Integer状态值转换为前端字符串状态值
            status: order.status ? this.statusMap[order.status] : 'pending',
            createTime: order.orderTime
              ? this.formatDate(order.orderTime)
              : new Date().toLocaleString(),
            totalPrice: order.amount || 0, // 后端字段名为amount
            totalQuantity: order.totalQuantity || 0,
            products:
              order.orderDetails?.map((item) => ({
                image: item.image || 'https://img.yzcdn.cn/vant/apple-1.jpg',
                name: item.name || '商品名称', // 后端字段名为name
                specs: item.dishFlavor || '标准规格',
                price: item.amount || 0,
                quantity: item.number || 1
              })) || []
          }));
        } else {
          // 如果没有订单数据，使用模拟数据
          this.setDefaultOrderData();
        }
      } catch (error) {
        console.error('加载订单列表失败:', error);
        Toast('加载订单列表失败，请重试');
        // 使用模拟数据
        this.setDefaultOrderData();
      }
    },

    // 格式化日期
    formatDate(dateStr) {
      const date = new Date(dateStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },

    // 设置默认订单数据（模拟数据）
    setDefaultOrderData() {
      this.orders = [
        {
          id: 1,
          orderNo: 'DOUYIN' + Date.now(),
          shopName: '美丽衣坊旗舰店',
          shopAvatar: 'https://img.yzcdn.cn/vant/cat.jpeg',
          status: 'completed',
          createTime: '2024-01-15 12:30',
          totalPrice: 299.8,
          totalQuantity: 3,
          products: [
            {
              image: 'https://img.yzcdn.cn/vant/apple-1.jpg',
              name: '时尚碎花连衣裙',
              specs: '红色 / M',
              price: 159.9,
              quantity: 1
            },
            {
              image: 'https://img.yzcdn.cn/vant/apple-2.jpg',
              name: '夏季透气T恤',
              specs: '白色 / L',
              price: 69.95,
              quantity: 2
            }
          ]
        },
        {
          id: 2,
          orderNo: 'DOUYIN' + (Date.now() - 10000),
          shopName: '运动户外专营店',
          shopAvatar: 'https://img.yzcdn.cn/vant/dog.jpeg',
          status: 'pending',
          createTime: '2024-01-16 15:45',
          totalPrice: 129.9,
          totalQuantity: 1,
          products: [
            {
              image: 'https://img.yzcdn.cn/vant/apple-3.jpg',
              name: '运动跑鞋',
              specs: '黑色 / 42',
              price: 129.9,
              quantity: 1
            }
          ]
        },
        {
          id: 3,
          orderNo: 'DOUYIN' + (Date.now() - 20000),
          shopName: '数码电子商城',
          shopAvatar: 'https://img.yzcdn.cn/vant/bird.jpeg',
          status: 'shipped',
          createTime: '2024-01-14 10:20',
          totalPrice: 349.0,
          totalQuantity: 1,
          products: [
            {
              image: 'https://img.yzcdn.cn/vant/apple-4.jpg',
              name: '智能手表',
              specs: '黑色',
              price: 349.0,
              quantity: 1
            }
          ]
        }
      ];
    },

    // 切换订单状态筛选
    async switchStatus(status) {
      this.currentStatus = status;
      // 状态切换时重新加载订单数据
      await this.loadPageOrders(1, 10, status);
    },

    // 加载分页订单数据
    async loadPageOrders(current, size, status) {
      try {
        // 将前端字符串状态值转换为后端Integer状态值
        const statusValue =
          status === 'all' ? null : this.reverseStatusMap[status];

        Toast.loading('加载中...', { duration: 0 }); // 显示加载动画

        const res = await request.get('/shop/order/page', {
          params: {
            current: current || 1,
            size: size || 10,
            status: statusValue,
            userId: this.userId
          }
        });

        Toast.clear(); // 清除加载动画

        if (res.code === 200 && res.data && res.data.records) {
          // 处理分页数据，将后端数据转换为前端所需格式
          this.orders = res.data.records.map((order) => ({
            id: order.id,
            orderNo: order.number || '', // 后端字段名为number
            shopName: order.shopName || '店铺名称',
            shopAvatar:
              order.shopAvatar || 'https://img.yzcdn.cn/vant/cat.jpeg',
            // 将后端Integer状态值转换为前端字符串状态值
            status: order.status ? this.statusMap[order.status] : 'pending',
            createTime: order.orderTime
              ? this.formatDate(order.orderTime)
              : new Date().toLocaleString(),
            totalPrice: order.amount || 0, // 后端字段名为amount
            totalQuantity: order.totalQuantity || 0,
            products:
              order.orderDetails?.map((item) => ({
                image: item.image || 'https://img.yzcdn.cn/vant/apple-1.jpg',
                name: item.name || '商品名称', // 后端字段名为name
                specs: item.dishFlavor || '标准规格',
                price: item.amount || 0,
                quantity: item.number || 1
              })) || []
          }));
        } else {
          // 如果没有订单数据，显示空状态
          this.orders = [];
          // 在没有真实数据时使用模拟数据
          if (!res.data || res.data.records.length === 0) {
            this.setDefaultOrderData();
          }
        }
      } catch (error) {
        console.error('加载分页订单数据失败:', error);
        Toast('加载订单数据失败，请重试');
        // 加载失败时使用模拟数据
        this.setDefaultOrderData();
      }
    },

    // 获取订单状态文本
    getStatusText(status) {
      const statusMap = {
        pending: '待付款',
        paid: '待发货',
        shipped: '待收货',
        completed: '已完成',
        cancelled: '已取消'
      };
      return statusMap[status] || '未知状态';
    },

    // 取消订单
    async cancelOrder(orderId) {
      try {
        // 显示确认对话框
        if (!(await this.$confirm('确定要取消该订单吗？'))) {
          return;
        }

        Toast.loading('取消中...', { duration: 0 }); // 显示加载动画

        const res = await request.post(`/shop/order/cancel/${orderId}`);

        Toast.clear(); // 清除加载动画

        if (res.code === 200) {
          Toast(res.msg || '订单已取消');
          // 更新订单状态
          const order = this.orders.find((o) => o.id === orderId);
          if (order) {
            order.status = 'cancelled';
          }
          // 如果当前在筛选状态下，需要重新加载列表以更新筛选结果
          if (this.currentStatus !== 'all') {
            await this.loadPageOrders(1, 10, this.currentStatus);
          }
        } else {
          throw new Error(res.msg || '取消失败');
        }
      } catch (error) {
        console.error('取消订单失败:', error);
        Toast(error.message || '取消订单失败，请重试');
      }
    },

    // 支付订单
    payOrder(orderId, amount) {
      // 跳转到支付页面或打开支付弹窗
      this.$router.push({
        path: '/shop/payment',
        query: {
          orderId: orderId,
          amount: amount
        }
      });
    },

    // 确认收货
    async confirmReceipt(orderId) {
      try {
        // 显示确认对话框
        if (!(await this.$confirm('确认已收到商品吗？确认后订单将完成。'))) {
          return;
        }

        Toast.loading('确认中...', { duration: 0 }); // 显示加载动画

        const res = await request.post(`/shop/order/confirm/${orderId}`);

        Toast.clear(); // 清除加载动画

        if (res.code === 200) {
          Toast(res.msg || '已确认收货');
          // 更新订单状态
          const order = this.orders.find((o) => o.id === orderId);
          if (order) {
            order.status = 'completed';
          }
          // 如果当前在筛选状态下，需要重新加载列表以更新筛选结果
          if (this.currentStatus !== 'all') {
            await this.loadPageOrders(1, 10, this.currentStatus);
          }
        } else {
          throw new Error(res.msg || '确认失败');
        }
      } catch (error) {
        console.error('确认收货失败:', error);
        Toast(error.message || '确认收货失败，请重试');
      }
    },

    // 评价订单
    reviewOrder(orderId) {
      // 跳转到评价页面
      Toast('评价功能开发中');
    },

    // 查看订单详情
    viewOrderDetail(orderId) {
      // 跳转到订单详情页面
      Toast('订单详情功能开发中');
    },

    // 去逛逛
    goShopping() {
      this.$router.push('/shop');
    }
  }
};
</script>

<style scoped>
.order-list-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 页面头部 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background-color: #fff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.back-button {
  font-size: 18px;
  color: #333;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.right {
  width: 24px;
}

/* 订单状态筛选 */
.status-tabs {
  display: flex;
  justify-content: space-around;
  background-color: #fff;
  padding: 10px 0;
  margin-top: 52px;
  border-bottom: 1px solid #eee;
  position: fixed;
  top: 52px;
  left: 0;
  right: 0;
  z-index: 99;
}

.tab {
  font-size: 14px;
  color: #666;
  padding: 5px 10px;
  cursor: pointer;
}

.tab.active {
  color: #fe2c55;
  font-weight: 600;
  position: relative;
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: -11px;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  background-color: #fe2c55;
}

/* 订单列表 */
.order-list {
  padding: 110px 0 20px;
}

.order-item {
  background-color: #fff;
  margin: 0 10px 10px;
  border-radius: 8px;
  overflow: hidden;
}

/* 店铺信息 */
.shop-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.shop-name {
  font-size: 14px;
  color: #333;
}

.shop-avatar img {
  width: 24px;
  height: 24px;
  border-radius: 50%;
}

/* 订单商品列表 */
.order-products {
  padding: 12px 16px;
}

.product-item {
  display: flex;
  margin-bottom: 12px;
}

.product-item:last-child {
  margin-bottom: 0;
}

.product-item img {
  width: 80px;
  height: 80px;
  border-radius: 6px;
  object-fit: cover;
}

.product-info {
  flex: 1;
  margin-left: 12px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-title {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-specs {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.product-price-count {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.price {
  font-size: 14px;
  color: #fe2c55;
  font-weight: 600;
}

.count {
  font-size: 12px;
  color: #666;
}

/* 订单总价 */
.order-total {
  padding: 10px 16px;
  text-align: right;
  font-size: 14px;
  color: #666;
  border-top: 1px solid #f0f0f0;
}

.total-price {
  color: #fe2c55;
  font-weight: 600;
  margin-left: 5px;
}

/* 订单状态和操作 */
.order-status-action {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 16px;
  border-top: 1px solid #f0f0f0;
}

.order-status {
  font-size: 14px;
  font-weight: 600;
}

.order-status.pending {
  color: #ff9800;
}

.order-status.paid {
  color: #2196f3;
}

.order-status.shipped {
  color: #4caf50;
}

.order-status.completed {
  color: #999;
}

.order-status.cancelled {
  color: #666;
}

.order-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 5px 15px;
  border: 1px solid #ddd;
  background-color: #fff;
  border-radius: 15px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
}

.action-btn.cancel {
  color: #999;
}

.action-btn.pay {
  color: #fe2c55;
  border-color: #fe2c55;
}

.action-btn.receive,
.action-btn.review {
  color: #fe2c55;
  border-color: #fe2c55;
}

.action-btn.view {
  color: #666;
  border-color: #ddd;
}

/* 空状态 */
.empty-order {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}

.empty-order img {
  width: 120px;
  height: 120px;
  margin-bottom: 16px;
}

.empty-order p {
  font-size: 14px;
  color: #999;
  margin-bottom: 20px;
}

.go-shopping {
  padding: 8px 24px;
  background-color: #fe2c55;
  color: #fff;
  border: none;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
}

/* 适配不同屏幕 */
@media (max-width: 375px) {
  .status-tabs {
    font-size: 13px;
  }

  .product-item img {
    width: 70px;
    height: 70px;
  }
}
</style>
