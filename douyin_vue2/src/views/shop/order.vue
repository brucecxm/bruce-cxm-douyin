<template>
  <div class="order-page">
    <!-- 收货地址 -->
    <section class="address-section" @click="editAddress">
      <div v-if="address">
        <div class="name-phone">
          <span class="name">{{ address.name }}</span>
          <span class="phone">{{ address.phone }}</span>
        </div>
        <div class="address-text">{{ address.fullAddress }}</div>
      </div>
      <div v-else class="no-address">请选择收货地址</div>
      <div class="arrow">›</div>
    </section>

    <!-- 商品列表 -->
    <section class="goods-section">
      <div v-for="(item, index) in cartItems" :key="index" class="goods-item">
        <img :src="item.image" alt="商品图" />
        <div class="info">
          <div class="name">{{ item.name }}</div>
          <div class="specs">{{ item.specs }}</div>
          <div class="price-count">
            <span class="price">¥{{ item.price.toFixed(2) }}</span>
            <span class="count">x{{ item.quantity }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 价格汇总 -->
    <section class="summary-section">
      <div class="row">
        <span>商品总价</span>
        <span>¥{{ totalGoodsPrice.toFixed(2) }}</span>
      </div>
      <div class="row">
        <span>运费</span>
        <span>{{
          shippingFee === 0 ? '免运费' : '¥' + shippingFee.toFixed(2)
        }}</span>
      </div>
      <div class="row">
        <span>优惠</span>
        <span class="discount">-¥{{ discount.toFixed(2) }}</span>
      </div>
      <div class="row total">
        <span>实付款</span>
        <span>¥{{ finalPrice.toFixed(2) }}</span>
      </div>
    </section>

    <!-- 备注 -->
    <section class="remark-section">
      <textarea
        placeholder="给卖家留言（选填）"
        v-model="orderRemark"
        rows="3"
      ></textarea>
    </section>

    <!-- 密码弹窗（带动画） -->
    <transition name="slide-up">
      <div v-if="showPasswordModal" class="password-modal-overlay">
        <div class="password-modal">
          <div class="modal-header">
            <div class="title">请输入支付密码</div>
            <div class="subtitle">转账金额</div>
            <div class="amount-display">
              ￥{{ parseFloat(amount).toFixed(2) }}
            </div>
          </div>

          <!-- 密码框 -->
          <div class="password-box">
            <div class="pwd-input" v-for="i in 6" :key="i">
              <div class="dot" v-if="password.length >= i"></div>
            </div>
          </div>

          <!-- 数字键盘 -->
          <div class="keyboard">
            <div
              class="key"
              v-for="key in keys"
              :key="key.label"
              @click="pressKey(key)"
            >
              {{ key.label }}
            </div>
          </div>

          <div class="cancel" @click="resetPasswordModal">取消</div>
        </div>
      </div>
    </transition>
    <!-- 底部支付按钮 -->
    <footer class="footer-pay">
      <button :disabled="!address" @click="submitOrder" v-if="noshow">
        提交订单
      </button>
    </footer>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from 'axios';
import { useUserInfoStore } from '@/stores/userInfo';
export default {
  data() {
    return {
      noshow: true,
      fromUser: '',
      toUser: '',
      amount: '',
      password: '',
      showPasswordModal: false,
      stomp: null,
      keys: [
        { label: '1' },
        { label: '2' },
        { label: '3' },
        { label: '4' },
        { label: '5' },
        { label: '6' },
        { label: '7' },
        { label: '8' },
        { label: '9' },
        { label: '' },
        { label: '0' },
        { label: '删除' }
      ],
      address: {
        name: '张三',
        phone: '13800138000',
        fullAddress: '北京市朝阳区望京街道123号望京SOHO'
      },
      cartItems: [
        {
          image: 'https://example.com/prod1.jpg',
          name: '时尚连衣裙',
          specs: '红色 / M',
          price: 159.9,
          quantity: 1
        },
        {
          image: 'https://example.com/prod2.jpg',
          name: '简约手表',
          specs: '黑色表带',
          price: 299.0,
          quantity: 1
        }
      ],
      shippingFee: 0,
      discount: 20,
      orderRemark: ''
    };
  },
  computed: {
    totalGoodsPrice() {
      return this.cartItems.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0
      );
    },
    finalPrice() {
      const pay = this.totalGoodsPrice + this.shippingFee - this.discount;
      return pay > 0 ? pay : 0;
    }
  },
  methods: {
    openPasswordModal() {
      if (!this.amount || parseFloat(this.amount) <= 0) {
        this.$message.error('请输入有效的转账金额');
        return;
      }
      this.showPasswordModal = true;
    },
    pressKey(key) {
      if (key.label === '删除') {
        this.password = this.password.slice(0, -1);
      } else if (key.label && this.password.length < 6) {
        this.password += key.label;
      }

      if (this.password.length === 6) {
        this.submitTransfer();
      }
    },
    submitTransfer() {
      const { fromUser, toUser, amount, password } = this;
      axios
        .post('/api/shop/transfer', null, {
          params: {
            fromUserId: fromUser,
            toUserId: toUser,
            amount,
            password
          }
        })
        .then((res) => {
          this.$message.success('转账成功' + res.data);
          this.resetPasswordModal();
        })
        .catch((err) => {
          this.$message.error(
            '转账失败: ' + (err.response?.data || '服务器错误')
          );
          this.noshow = true;
          this.resetPasswordModal();
        });
    },
    resetPasswordModal() {
      this.password = '';
      this.showPasswordModal = false;
    },
    editAddress() {
      // 弹出地址选择页，简化用prompt模拟
      const name = prompt('收货人姓名', this.address?.name || '');
      if (!name) return;
      const phone = prompt('联系电话', this.address?.phone || '');
      if (!phone) return;
      const fullAddress = prompt('详细地址', this.address?.fullAddress || '');
      if (!fullAddress) return;
      this.address = { name, phone, fullAddress };
    },
    submitOrder() {
      if (!this.address) {
        alert('请填写收货地址');
        return;
      }
      this.amount = 1;
      if (!this.amount || parseFloat(this.amount) <= 0) {
        this.$message.error('请输入有效的转账金额');
        return;
      }
      this.noshow = false;
      this.showPasswordModal = true;
    }
  },
  mounted() {
    const userInfo = useUserInfoStore();
    const userInfoMap = userInfo.userInfo;
    this.fromUser = userInfoMap.userId || '';
    this.toUser = this.$route.query.toUser || '';

    const socket = new SockJS('http://localhost:7426/ws');
    this.stomp = Stomp.over(socket);
    this.stomp.connect({}, () => {
      this.stomp.subscribe('/topic/transfer/' + this.fromUser, (msg) => {
        this.$message.success('转账成功: ' + msg.body);
      });
    });
  }
};
</script>

<style scoped>
.order-page {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  background: #fff;
  color: #333;
  padding-bottom: 70px;
}

/* 收货地址 */
.address-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 15px;
  border-bottom: 1px solid #eee;
  font-size: 14px;
  cursor: pointer;
  background: #fff;
}

.address-section .name-phone {
  font-weight: 600;
  margin-bottom: 6px;
}

.address-section .address-text {
  color: #666;
  font-size: 13px;
  max-width: 70vw;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.address-section .no-address {
  color: #999;
}

.address-section .arrow {
  font-size: 22px;
  color: #ccc;
  user-select: none;
}

/* 商品列表 */
.goods-section {
  margin-top: 10px;
  border-top: 8px solid #f5f5f7;
  background: #fff;
}

.goods-item {
  display: flex;
  padding: 12px 15px;
  border-bottom: 1px solid #eee;
}

.goods-item img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
  margin-right: 12px;
}

.info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.name {
  font-size: 16px;
  font-weight: 600;
  color: #222;
}

.specs {
  color: #999;
  font-size: 13px;
  margin-top: 6px;
}

.price-count {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.price {
  color: #fe2c55;
  font-weight: 700;
  font-size: 16px;
}

.count {
  color: #666;
  font-size: 14px;
}

/* 价格汇总 */
.summary-section {
  margin-top: 12px;
  background: #fff;
  padding: 10px 15px;
  border-top: 8px solid #f5f5f7;
  font-size: 14px;
  color: #555;
}

.summary-section .row {
  display: flex;
  justify-content: space-between;
  margin: 6px 0;
}

.summary-section .discount {
  color: #fe2c55;
}

.summary-section .total {
  font-weight: 700;
  font-size: 18px;
  color: #222;
}

/* 备注 */
.remark-section {
  margin-top: 10px;
  padding: 10px 15px;
  background: #fff;
}

.remark-section textarea {
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 8px 12px;
  font-size: 14px;
  resize: none;
  box-sizing: border-box;
  color: #555;
}

/* 底部支付按钮 */
.footer-pay {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-top: 1px solid #eee;
  padding: 10px 15px;
  box-sizing: border-box;
  z-index: 1000;
}

.footer-pay button {
  width: 100%;
  background-color: #fe2c55;
  border: none;
  color: white;
  font-size: 18px;
  font-weight: 700;
  border-radius: 30px;
  padding: 12px 0;
  cursor: pointer;
  user-select: none;
  transition: background-color 0.3s;
}

.footer-pay button:disabled {
  background-color: #f8b6c0;
  cursor: not-allowed;
}

.footer-pay button:not(:disabled):hover {
  background-color: #d91a47;
}
.transfer-page {
  padding: 20px;
  font-family: 'PingFang SC', sans-serif;
  background: #f2f2f2;
  min-height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  font-size: 18px;
  margin-bottom: 20px;
}

.back {
  margin-right: 10px;
  font-size: 22px;
  cursor: pointer;
}

.title {
  font-weight: bold;
}

.user-info {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.user {
  display: flex;
  align-items: center;
}

.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
}

.user-text {
  margin-left: 10px;
}

.label {
  color: #999;
  font-size: 12px;
}

.value {
  font-size: 16px;
  font-weight: bold;
}

.arrow {
  font-size: 22px;
  margin: 0 10px;
}

.amount-input-section {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 30px 0;
}

.currency {
  font-size: 30px;
  font-weight: bold;
}

.amount-input {
  border: none;
  border-bottom: 2px solid #333;
  font-size: 30px;
  width: 60%;
  text-align: center;
  margin-left: 10px;
  background: transparent;
  outline: none;
}

.submit-btn {
  width: 100%;
  background: linear-gradient(90deg, #ff416c, #ff4b2b);
  border: none;
  border-radius: 30px;
  color: white;
  padding: 14px;
  font-size: 16px;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(255, 65, 108, 0.3);
}

/* ===== 弹窗样式 ===== */
.password-modal-overlay {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: white;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  padding: 20px 16px 40px;
  box-shadow: 0 -5px 20px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.modal-header {
  text-align: center;
  margin-bottom: 20px;
}

.modal-header .title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 6px;
}

.modal-header .subtitle {
  font-size: 12px;
  color: #888;
}

.amount-display {
  font-size: 28px;
  font-weight: bold;
  color: #ff4b2b;
  margin-top: 6px;
}

/* 密码框 */
.password-box {
  display: flex;
  justify-content: space-between;
  margin: 16px 0;
  padding: 0 20px;
}

.pwd-input {
  width: 40px;
  height: 40px;
  border: 1px solid #ccc;
  border-radius: 6px;
  position: relative;
}

.dot {
  width: 10px;
  height: 10px;
  background: #333;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

/* 数字键盘 */
.keyboard {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  padding: 0 10px;
}

.key {
  background: #f0f0f0;
  border-radius: 10px;
  height: 48px;
  line-height: 48px;
  text-align: center;
  font-size: 18px;
  cursor: pointer;
  user-select: none;
  transition: background 0.2s;
}

.key:active {
  background: #ddd;
}

.cancel {
  margin-top: 18px;
  text-align: center;
  color: #999;
  font-size: 14px;
  cursor: pointer;
}

/* 动画 */
.slide-up-enter-active {
  animation: slideUp 0.3s ease-out forwards;
}
.slide-up-leave-active {
  animation: slideDown 0.3s ease-in forwards;
}
@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0%);
  }
}
@keyframes slideDown {
  from {
    transform: translateY(0%);
  }
  to {
    transform: translateY(100%);
  }
}
</style>
