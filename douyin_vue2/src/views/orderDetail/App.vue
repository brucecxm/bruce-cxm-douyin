<template>
  <div class="order-detail-page">
    <!-- 顶部导航 -->
    <header class="detail-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">订单详情</div>
      <div class="header-right">
        <i class="more-icon">⋮</i>
      </div>
    </header>

    <!-- 订单状态卡片 -->
    <div class="status-card">
      <div class="status-icon">📦</div>
      <div class="status-info">
        <div class="status-text">{{ order.statusText }}</div>
        <div class="status-desc">{{ order.statusDesc }}</div>
      </div>
      <div class="status-actions">
        <button
          class="action-btn primary"
          v-if="order.status === 'pending_payment'"
          @click="payOrder"
        >
          立即付款
        </button>
        <button
          class="action-btn primary"
          v-if="order.status === 'pending_receipt'"
          @click="confirmReceipt"
        >
          确认收货
        </button>
        <button
          class="action-btn secondary"
          v-if="order.status === 'completed'"
          @click="viewLogistics"
        >
          查看物流
        </button>
      </div>
    </div>

    <!-- 物流信息 -->
    <div
      class="logistics-section"
      v-if="
        order.status === 'pending_shipping' ||
          order.status === 'pending_receipt' ||
          order.status === 'completed'
      "
    >
      <div class="section-title">物流信息</div>
      <div class="logistics-content">
        <div class="logistics-item">
          <div class="logistics-dot active"></div>
          <div class="logistics-info">
            <div class="logistics-time">{{ order.logistics.latestTime }}</div>
            <div class="logistics-desc">{{ order.logistics.latestDesc }}</div>
          </div>
        </div>
        <div
          class="logistics-item"
          v-for="(item, index) in order.logistics.history"
          :key="index"
        >
          <div class="logistics-dot"></div>
          <div class="logistics-info">
            <div class="logistics-time">{{ item.time }}</div>
            <div class="logistics-desc">{{ item.desc }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 订单商品 -->
    <div class="product-section">
      <div class="section-title">商品信息</div>
      <div class="product-item" @click="goToProductDetail(order.product.id)">
        <div class="product-image">
          <img :src="order.product.image" :alt="order.product.title" />
        </div>
        <div class="product-info">
          <div class="product-title">{{ order.product.title }}</div>
          <div class="product-specs">{{ order.product.specs }}</div>
          <div class="product-price">
            ¥{{ order.product.price }} × {{ order.product.quantity }}
          </div>
        </div>
      </div>
    </div>

    <!-- 订单信息 -->
    <div class="order-info-section">
      <div class="section-title">订单信息</div>
      <div class="info-item">
        <div class="info-label">订单编号</div>
        <div class="info-value">{{ order.orderNo }}</div>
      </div>
      <div class="info-item">
        <div class="info-label">下单时间</div>
        <div class="info-value">{{ order.createTime }}</div>
      </div>
      <div class="info-item">
        <div class="info-label">支付方式</div>
        <div class="info-value">{{ order.paymentMethod }}</div>
      </div>
      <div class="info-item">
        <div class="info-label">支付时间</div>
        <div class="info-value">{{ order.payTime || '未支付' }}</div>
      </div>
    </div>

    <!-- 收货信息 -->
    <div class="address-section">
      <div class="section-title">收货信息</div>
      <div class="address-card">
        <div class="address-icon">📍</div>
        <div class="address-info">
          <div class="address-name-phone">
            {{ order.address.receiver || order.address.name || '未设置收货人' }}
            {{ order.address.phone || '未设置手机号' }}
          </div>
          <div class="address-detail">
            {{ order.address.province || '' }} {{ order.address.city || '' }}
            {{ order.address.district || '' }} {{ order.address.detail || '' }}
          </div>
        </div>
      </div>
    </div>

    <!-- 价格明细 -->
    <div class="price-section">
      <div class="section-title">价格明细</div>
      <div class="price-item">
        <div class="price-label">商品总价</div>
        <div class="price-value">
          ¥{{ order.product.price * order.product.quantity }}
        </div>
      </div>
      <div class="price-item">
        <div class="price-label">运费</div>
        <div class="price-value">¥{{ order.shippingFee }}</div>
      </div>
      <div class="price-item">
        <div class="price-label">优惠</div>
        <div class="price-value">-¥{{ order.discount }}</div>
      </div>
      <div class="price-item total">
        <div class="price-label">实付款</div>
        <div class="price-value total-value">¥{{ order.totalAmount }}</div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <footer
      class="bottom-actions"
      v-if="
        order.status === 'pending_payment' ||
          order.status === 'pending_receipt' ||
          order.status === 'completed'
      "
    >
      <button
        class="action-btn secondary"
        v-if="order.status === 'pending_payment'"
        @click="cancelOrder"
      >
        取消订单
      </button>
      <button
        class="action-btn primary"
        v-if="order.status === 'pending_payment'"
        @click="payOrder"
      >
        立即付款
      </button>
      <button
        class="action-btn secondary"
        v-if="order.status === 'pending_receipt'"
        @click="contactSeller"
      >
        联系卖家
      </button>
      <button
        class="action-btn primary"
        v-if="order.status === 'pending_receipt'"
        @click="confirmReceipt"
      >
        确认收货
      </button>
      <button
        class="action-btn secondary"
        v-if="order.status === 'completed'"
        @click="contactSeller"
      >
        联系卖家
      </button>
      <button
        class="action-btn primary"
        v-if="order.status === 'completed'"
        @click="leaveComment"
      >
        评价
      </button>
    </footer>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';
import { get } from '../../utils/apiUtils';

export default {
  name: 'OrderDetailPage',
  data() {
    return {
      order: {
        statusText: '加载中',
        statusDesc: '',
        status: '',
        product: {},
        address: {},
        logistics: {}
      },
      orderId: ''
    };
  },
  mounted() {
    // 设置页面标题
    nativeUtils.setTitleBar('订单详情');
    // 获取URL参数中的订单ID
    this.orderId = this.getUrlParam('id');
    // 加载订单详情
    this.loadOrderDetail();
  },
  methods: {
    // 从URL中获取参数
    getUrlParam(name) {
      const urlParams = new URLSearchParams(window.location.search);
      return urlParams.get(name);
    },

    // 从API或localStorage加载订单详情
    loadOrderDetail() {
      if (!this.orderId) {
        nativeUtils.showToast('订单ID无效', 1500);
        routerUtils.navigateBack(1);
        return;
      }

      try {
        nativeUtils.showLoading('加载中...');

        // 尝试从API获取数据
        get(`/order/${this.orderId}`)
          .then((response) => {
            if (response.code === 0) {
              // API请求成功
              let orderData = response.data;
              // 添加必要的属性
              orderData.statusText = this.getStatusText(orderData.status);
              orderData.statusDesc = this.getStatusDesc(orderData.status);
              // 确保product、address、logistics属性存在
              orderData.product = orderData.product || {};
              orderData.address = orderData.address || {};
              orderData.logistics = orderData.logistics || {};

              // 处理地址字段映射，确保前端需要的字段存在
              if (orderData.address) {
                // 统一地址字段名，处理可能的字段差异
                orderData.address.name =
                  orderData.address.receiver || orderData.address.name || '';
                orderData.address.phone = orderData.address.phone || '';
                orderData.address.province = orderData.address.province || '';
                orderData.address.city = orderData.address.city || '';
                orderData.address.district = orderData.address.district || '';
                orderData.address.detail = orderData.address.detail || '';
              }

              // 处理物流信息
              if (orderData.logistics) {
                orderData.logistics.latestTime =
                  orderData.logistics.latestTime || '';
                orderData.logistics.latestDesc =
                  orderData.logistics.latestDesc || '';
                orderData.logistics.history = orderData.logistics.history || [];
              }

              // 处理商品信息
              if (orderData.product) {
                orderData.product.id = orderData.product.id || '';
                orderData.product.title = orderData.product.title || '';
                orderData.product.specs = orderData.product.specs || '';
                orderData.product.price = orderData.product.price || 0;
                orderData.product.quantity = orderData.product.quantity || 1;
                orderData.product.image =
                  orderData.product.image ||
                  'https://via.placeholder.com/150x150?text=商品图片';
              }

              // 处理订单信息
              orderData.orderNo =
                orderData.orderNo ||
                `20251225${String(orderData.id || this.orderId).padStart(6, '0')}`;
              orderData.createTime = orderData.createTime || '';
              orderData.payTime = orderData.payTime || '';
              orderData.shippingFee = orderData.shippingFee || 0;
              orderData.discount = orderData.discount || 0;
              orderData.totalAmount = orderData.totalAmount || 0;
              orderData.paymentMethod =
                orderData.paymentMethod || '未选择支付方式';

              this.order = orderData;
            } else {
              // API请求失败，使用localStorage数据
              this.loadFromLocalStorage();
            }
            nativeUtils.hideLoading();
          })
          .catch((error) => {
            console.error('加载订单详情失败:', error);
            // API请求失败，使用localStorage数据
            this.loadFromLocalStorage();
            nativeUtils.hideLoading();
          });
      } catch (error) {
        console.error('加载订单详情失败:', error);
        this.loadFromLocalStorage();
        nativeUtils.hideLoading();
      }
    },

    // 从localStorage加载订单详情
    loadFromLocalStorage() {
      try {
        const ordersStr = localStorage.getItem('orders');
        if (ordersStr) {
          const orders = JSON.parse(ordersStr);
          const foundOrder = orders.find((order) => order.id == this.orderId);
          if (foundOrder) {
            // 补全订单详情信息
            this.order = {
              ...foundOrder,
              orderNo: `20251225${String(foundOrder.id).padStart(6, '0')}`,
              statusDesc: this.getStatusDesc(foundOrder.status),
              createTime: foundOrder.time + ':00',
              payTime: foundOrder.time + ':30',
              shippingFee: 0,
              discount: 0,
              paymentMethod: '支付宝支付',
              address: {
                name: '张三',
                phone: '138****8888',
                province: '北京市',
                city: '北京市',
                district: '朝阳区',
                detail: 'XX街道XX小区XX号楼XX单元XX室'
              },
              logistics: {
                latestTime: foundOrder.time + ':35',
                latestDesc: '您的订单已提交，等待商家处理',
                history: [
                  {
                    time: foundOrder.time + ':30',
                    desc: '您已完成支付，等待商家处理'
                  },
                  {
                    time: foundOrder.time + ':00',
                    desc: '您已提交订单，等待支付'
                  }
                ]
              }
            };
            nativeUtils.showToast('使用本地缓存数据', 1500);
            return;
          }
        }
      } catch (error) {
        console.error('解析本地数据失败:', error);
      }

      // 使用默认mock数据
      this.order = {
        id: this.orderId,
        orderNo: `20251225${String(this.orderId).padStart(6, '0')}`,
        status: 'pending_shipping',
        statusText: '待发货',
        statusDesc: '卖家正在准备发货，请耐心等待',
        createTime: '2025-12-25 10:30:00',
        payTime: '2025-12-25 10:35:00',
        totalAmount: 5999,
        shippingFee: 0,
        discount: 0,
        paymentMethod: '支付宝支付',
        product: {
          id: 1,
          title: 'iPhone 13 Pro 256GB 远峰蓝',
          specs: '颜色: 远峰蓝 | 内存: 256GB',
          price: 5999,
          quantity: 1,
          image: 'https://via.placeholder.com/150x150?text=iPhone+13+Pro'
        },
        address: {
          name: '张三',
          phone: '138****8888',
          province: '北京市',
          city: '北京市',
          district: '朝阳区',
          detail: 'XX街道XX小区XX号楼XX单元XX室'
        },
        logistics: {
          latestTime: '2025-12-25 11:00:00',
          latestDesc: '您的订单已提交，等待商家处理',
          history: [
            {
              time: '2025-12-25 10:35:00',
              desc: '您已完成支付，等待商家处理'
            },
            {
              time: '2025-12-25 10:30:00',
              desc: '您已提交订单，等待支付'
            }
          ]
        }
      };

      nativeUtils.showToast('使用默认数据', 1500);
    },

    // 获取订单状态文本
    getStatusText(status) {
      const statusMap = {
        pending_payment: '待付款',
        pending_shipping: '待发货',
        pending_receipt: '待收货',
        completed: '已完成',
        cancelled: '已取消'
      };
      return statusMap[status] || '未知状态';
    },

    // 获取订单状态描述
    getStatusDesc(status) {
      const statusMap = {
        pending_payment: '请尽快完成支付，逾期订单将自动取消',
        pending_shipping: '卖家正在准备发货，请耐心等待',
        pending_receipt: '商品已发货，请注意查收',
        completed: '订单已完成，感谢您的购买',
        cancelled: '订单已取消'
      };
      return statusMap[status] || '';
    },

    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1);
    },

    // 跳转到商品详情页
    goToProductDetail(productId) {
      routerUtils.navigateTo(`/detail.html?id=${productId}`);
    },

    // 立即付款
    payOrder() {
      // 跳转到转账页面，传递订单ID
      routerUtils.navigateTo(`/newshop/transfer.html?orderId=${this.orderId}`);
    },

    // 取消订单
    cancelOrder() {
      if (confirm('确定要取消这个订单吗？')) {
        // 调用API取消订单
        nativeUtils.showLoading('取消中...');
        setTimeout(() => {
          // 更新本地数据
          this.order.status = 'cancelled';
          this.order.statusText = '已取消';
          this.order.statusDesc = this.getStatusDesc('cancelled');
          nativeUtils.hideLoading();
          nativeUtils.showToast('订单已取消', 1500);
        }, 1000);
      }
    },

    // 确认收货
    confirmReceipt() {
      if (confirm('确定要确认收货吗？')) {
        // 调用API确认收货
        nativeUtils.showLoading('确认中...');
        setTimeout(() => {
          // 更新本地数据
          this.order.status = 'completed';
          this.order.statusText = '已完成';
          this.order.statusDesc = this.getStatusDesc('completed');
          nativeUtils.hideLoading();
          nativeUtils.showToast('已确认收货', 1500);
        }, 1000);
      }
    },

    // 联系卖家
    contactSeller() {
      nativeUtils.showToast('联系卖家功能开发中', 1500);
    },

    // 评价
    leaveComment() {
      nativeUtils.showToast('评价功能开发中', 1500);
    },

    // 查看物流
    viewLogistics() {
      nativeUtils.showToast('查看物流功能开发中', 1500);
    }
  }
};
</script>

<style scoped>
.order-detail-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 顶部导航 */
.detail-header {
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

.back-icon,
.more-icon {
  font-size: 20px;
  color: #333;
  cursor: pointer;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

/* 状态卡片 */
.status-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
  padding: 30px 20px;
  margin: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.status-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.status-info {
  text-align: center;
  margin-bottom: 20px;
}

.status-text {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.status-desc {
  font-size: 14px;
  color: #999;
}

/* 物流信息 */
.logistics-section {
  background-color: white;
  margin: 0 15px 15px;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.logistics-content {
  padding: 15px;
}

.logistics-item {
  display: flex;
  margin-bottom: 20px;
  position: relative;
}

.logistics-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: #ddd;
  margin-right: 15px;
  margin-top: 5px;
  position: relative;
}

.logistics-dot.active {
  background-color: #42b983;
  box-shadow: 0 0 0 4px rgba(66, 185, 131, 0.2);
}

.logistics-dot::before {
  content: '';
  position: absolute;
  top: 12px;
  left: 50%;
  transform: translateX(-50%);
  width: 1px;
  height: calc(100% + 20px);
  background-color: #ddd;
}

.logistics-item:last-child .logistics-dot::before {
  display: none;
}

.logistics-info {
  flex: 1;
}

.logistics-time {
  font-size: 12px;
  color: #999;
  margin-bottom: 5px;
}

.logistics-desc {
  font-size: 14px;
  color: #333;
}

/* 商品信息 */
.product-section {
  background-color: white;
  margin: 0 15px 15px;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.product-item {
  display: flex;
  padding: 15px;
  cursor: pointer;
}

.product-image {
  width: 100px;
  height: 100px;
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
  font-size: 16px;
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
  font-size: 14px;
  color: #999;
  margin-bottom: 10px;
}

.product-price {
  font-size: 16px;
  font-weight: bold;
  color: #ff4444;
}

/* 订单信息和收货信息 */
.order-info-section,
.address-section {
  background-color: white;
  margin: 0 15px 15px;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 14px;
  color: #666;
}

.info-value {
  font-size: 14px;
  color: #333;
  text-align: right;
}

/* 收货信息卡片 */
.address-card {
  display: flex;
  align-items: flex-start;
  padding: 15px;
}

.address-icon {
  font-size: 20px;
  margin-right: 15px;
  margin-top: 5px;
}

.address-info {
  flex: 1;
}

.address-name-phone {
  font-size: 16px;
  color: #333;
  margin-bottom: 5px;
}

.address-detail {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

/* 价格明细 */
.price-section {
  background-color: white;
  margin: 0 15px 80px;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.price-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.price-item.total {
  border-bottom: none;
  font-weight: bold;
}

.price-label {
  font-size: 14px;
  color: #666;
}

.price-value {
  font-size: 14px;
  color: #333;
}

.price-value.total-value {
  font-size: 18px;
  color: #ff4444;
}

/* 底部操作栏 */
.bottom-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background-color: white;
  padding: 15px;
  border-top: 1px solid #eee;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
}

.action-btn {
  padding: 10px 20px;
  border-radius: 25px;
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
