<template>
  <div class="payment-page">
    <!-- 页面头部 -->
    <div class="header">
      <div class="back-button" @click="goBack">
        <i class="fas fa-arrow-left"></i>
      </div>
      <div class="title">订单支付</div>
      <div class="right"></div>
    </div>

    <!-- 支付金额区域 -->
    <div class="payment-amount-section">
      <p class="amount-label">支付金额</p>
      <div class="amount-value">¥{{ totalAmount.toFixed(2) }}</div>
    </div>

    <!-- 订单信息 -->
    <div class="order-info-section">
      <h3 class="section-title">订单信息</h3>
      <div class="order-info-item">
        <span class="label">订单编号</span>
        <span class="value">{{ orderNumber }}</span>
      </div>
      <div class="order-info-item">
        <span class="label">创建时间</span>
        <span class="value">{{ orderTime }}</span>
      </div>
    </div>

    <!-- 支付方式 -->
    <div class="payment-method-section">
      <h3 class="section-title">支付方式</h3>
      <div 
        v-for="method in paymentMethods" 
        :key="method.code"
        class="payment-method-item"
        :class="{ active: selectedMethod === method.code }"
        @click="selectMethod(method.code)"
      >
        <div class="method-info">
          <i :class="method.icon"></i>
          <span class="method-name">{{ method.name }}</span>
        </div>
        <i class="fas fa-check-circle" v-if="selectedMethod === method.code"></i>
      </div>
    </div>

    <!-- 底部支付按钮 -->
    <div class="payment-footer">
      <button class="btn-pay" @click="submitPayment" :disabled="isProcessing">
        {{ isProcessing ? '处理中...' : '立即支付' }}
      </button>
    </div>

    <!-- 支付结果弹窗 -->
    <div class="payment-result-modal" v-if="showResult">
      <div class="modal-content">
        <div class="result-icon" :class="paymentSuccess ? 'success' : 'fail'">
          <i :class="paymentSuccess ? 'fas fa-check-circle' : 'fas fa-times-circle'"></i>
        </div>
        <p class="result-text">{{ paymentSuccess ? '支付成功' : '支付失败' }}</p>
        <p class="result-message">{{ resultMessage }}</p>
        <button class="btn-confirm" @click="closeResult">确认</button>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js';
import { toast } from 'vant';

export default {
  data() {
    return {
      orderId: '',
      totalAmount: 0,
      orderNumber: '',
      orderTime: '',
      selectedMethod: 'wechat',
      isProcessing: false,
      showResult: false,
      paymentSuccess: false,
      resultMessage: '',
      paymentMethods: [
        {
          code: 'wechat',
          name: '微信支付',
          icon: 'fab fa-weixin'
        },
        {
          code: 'alipay',
          name: '支付宝',
          icon: 'fab fa-alipay'
        },
        {
          code: 'balance',
          name: '余额支付',
          icon: 'fas fa-wallet'
        }
      ]
    };
  },
  created() {
    // 获取路由参数
    this.orderId = this.$route.query.orderId || '';
    this.totalAmount = parseFloat(this.$route.query.amount) || 0;
    
    if (this.orderId) {
      this.loadOrderDetail();
    } else {
      toast('订单信息有误');
      setTimeout(() => {
        this.$router.back();
      }, 1500);
    }
  },
  methods: {
    // 返回上一页
    goBack() {
      this.$router.back();
    },
    
    // 加载订单详情
    async loadOrderDetail() {
      try {
        const res = await request.get(`/shop/order/getById/${this.orderId}`);
        
        if (res.code === 200 && res.data) {
          const order = res.data;
          this.orderNumber = order.number || '';
          this.orderTime = order.orderTime ? this.formatDate(order.orderTime) : '';
          // 确保金额一致
          if (order.amount) {
            this.totalAmount = parseFloat(order.amount);
          }
        } else {
          throw new Error('加载订单详情失败');
        }
      } catch (error) {
        console.error('加载订单详情失败:', error);
        toast('加载订单详情失败');
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
    
    // 选择支付方式
    selectMethod(methodCode) {
      this.selectedMethod = methodCode;
    },
    
    // 提交支付
    async submitPayment() {
      if (this.isProcessing) return;
      
      this.isProcessing = true;
      
      try {
        // 构建支付请求数据
        const paymentData = {
          orderId: this.orderId,
          payMethod: this.selectedMethod,
          amount: this.totalAmount
        };
        
        // 这里简化处理，模拟支付请求
        // 在实际项目中，应该调用后端支付接口
        await this.simulatePayment(paymentData);
        
        // 支付成功后更新订单状态
        await this.updateOrderStatus();
        
        // 显示支付成功结果
        this.paymentSuccess = true;
        this.resultMessage = '支付成功，即将跳转到订单列表';
        this.showResult = true;
        
        // 3秒后跳转到订单列表
        setTimeout(() => {
          this.$router.push('/shop/orderlist');
        }, 3000);
        
      } catch (error) {
        console.error('支付失败:', error);
        this.paymentSuccess = false;
        this.resultMessage = error.message || '支付失败，请重试';
        this.showResult = true;
      } finally {
        this.isProcessing = false;
      }
    },
    
    // 模拟支付请求（实际项目中应该调用后端接口）
    simulatePayment(paymentData) {
      return new Promise((resolve, reject) => {
        // 模拟网络请求延迟
        setTimeout(() => {
          // 随机模拟支付成功或失败（90%成功率）
          const success = Math.random() > 0.1;
          
          if (success) {
            resolve({ code: 200 });
          } else {
            reject(new Error('支付处理失败，请稍后重试'));
          }
        }, 2000);
      });
    },
    
    // 更新订单状态
    async updateOrderStatus() {
      try {
        // 调用后端接口更新订单状态为已支付
        const res = await request.post(`/shop/order/updateStatus/${this.orderId}`, {
          status: 2 // 2表示已支付/待发货
        });
        
        if (res.code !== 200) {
          console.warn('更新订单状态失败，但不影响支付流程');
        }
      } catch (error) {
        console.warn('更新订单状态出错，但不影响支付流程:', error);
      }
    },
    
    // 关闭结果弹窗
    closeResult() {
      this.showResult = false;
      
      // 如果支付失败，返回上一页
      if (!this.paymentSuccess) {
        this.$router.back();
      }
    }
  }
};
</script>

<style scoped>
.payment-page {
  background-color: #f5f5f5;
  min-height: 100vh;
  font-family: 'PingFang SC', 'Helvetica Neue', Helvetica, Arial, sans-serif;
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

/* 支付金额区域 */
.payment-amount-section {
  background-color: #fff;
  padding: 80px 20px 30px;
  text-align: center;
  border-bottom: 1px solid #eee;
}

.amount-label {
  font-size: 16px;
  color: #666;
  margin-bottom: 10px;
}

.amount-value {
  font-size: 36px;
  font-weight: 700;
  color: #fe2c55;
}

/* 订单信息区域 */
.order-info-section,
.payment-method-section {
  margin-top: 10px;
  background-color: #fff;
  padding: 15px 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
}

.order-info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.order-info-item:last-child {
  border-bottom: none;
}

.order-info-item .label {
  font-size: 14px;
  color: #666;
}

.order-info-item .value {
  font-size: 14px;
  color: #333;
}

/* 支付方式 */
.payment-method-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.payment-method-item:last-child {
  border-bottom: none;
}

.payment-method-item.active {
  color: #fe2c55;
}

.method-info {
  display: flex;
  align-items: center;
}

.method-info i {
  font-size: 20px;
  margin-right: 10px;
}

.method-name {
  font-size: 16px;
}

.payment-method-item .fa-check-circle {
  font-size: 18px;
}

/* 底部支付按钮 */
.payment-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 15px 20px;
  background-color: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.btn-pay {
  width: 100%;
  padding: 12px;
  background-color: #fe2c55;
  color: #fff;
  border: none;
  border-radius: 25px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-pay:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

/* 支付结果弹窗 */
.payment-result-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  padding: 40px 30px;
  border-radius: 12px;
  text-align: center;
  width: 80%;
  max-width: 320px;
}

.result-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto 20px;
  font-size: 48px;
}

.result-icon.success {
  background-color: rgba(76, 175, 80, 0.1);
  color: #4caf50;
}

.result-icon.fail {
  background-color: rgba(244, 67, 54, 0.1);
  color: #f44336;
}

.result-text {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.result-message {
  font-size: 14px;
  color: #666;
  margin-bottom: 30px;
}

.btn-confirm {
  padding: 10px 30px;
  background-color: #fe2c55;
  color: #fff;
  border: none;
  border-radius: 20px;
  font-size: 16px;
  cursor: pointer;
}

/* 适配不同屏幕 */
@media (max-width: 375px) {
  .amount-value {
    font-size: 32px;
  }
  
  .modal-content {
    width: 90%;
  }
}
</style>