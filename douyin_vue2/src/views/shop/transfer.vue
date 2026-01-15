<template>
  <div class="transfer-page">
    <!-- 顶部 -->
    <div class="header">
      <span class="back" @click="$router.go(-1)">←</span>
      <span class="title">转账</span>
    </div>

    <!-- 转账成功页面 -->
    <div v-if="showSuccessPage" class="success-page">
      <div class="success-content">
        <!-- 成功图标 -->
        <div class="success-icon">
          <svg width="80" height="80" viewBox="0 0 24 24" fill="#ff4757">
            <path
              d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"
            />
          </svg>
        </div>

        <!-- 成功文案 -->
        <div class="success-text">
          <div class="success-title">转账已发出，等待单曲猫咪收款</div>
          <div class="success-amount">
            ￥{{ parseFloat(amount || 0).toFixed(2) }}
          </div>
          <div class="refund-info">24小时对方未收款，将退回原账户</div>
        </div>

        <!-- 转账信息 -->
        <div class="transfer-info">
          <div class="info-item">
            <span class="info-label">转账附言</span>
            <span class="info-value">{{ remark || '无' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">转账时间</span>
            <span class="info-value">{{ transferTime }}</span>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <button class="secondary-btn" @click="viewTransferDetails">
            查看转账消息
          </button>
          <button class="primary-btn" @click="transferAgain">再次转账</button>
        </div>
      </div>

      <!-- 底部信息 -->
      <div class="footer-info">
        <span>转账说明 2025-抖音支付</span>
      </div>
    </div>

    <!-- 用户信息展示 -->
    <div class="recipient-info">
      <div class="avatar-container">
        <img
          class="avatar"
          src="https://via.placeholder.com/80"
          alt="用户头像"
        />
        <div class="verify-badge">已实名</div>
      </div>
      <div class="user-details">
        <div class="nickname">单曲猫咪</div>
        <div class="user-id">ID: {{ toUser || '000000' }}</div>
      </div>
    </div>

    <!-- 转账金额区域 -->
    <div class="transfer-section">
      <div class="section-title">转账金额</div>
      <div class="amount-container">
        <span class="currency-symbol">￥</span>
        <input
          type="number"
          class="amount-input"
          v-model="amount"
          placeholder="0.00"
          inputmode="decimal"
        />
      </div>
    </div>

    <!-- 转账附言区域 -->
    <div class="remark-section">
      <div class="section-title">转账附言</div>
      <input
        type="text"
        class="remark-input"
        v-model="remark"
        placeholder="选填，最多20字"
        maxlength="20"
      />
    </div>

    <!-- 到账提示 -->
    <div class="arrival-info">
      <i class="icon-clock"></i>
      <span>实时到账</span>
    </div>

    <!-- 确认转账按钮 -->
    <button class="confirm-btn" @click="openPasswordModal">确认转账</button>

    <!-- 密码弹窗（带动画） -->
    <transition name="slide-up">
      <div v-if="showPasswordModal" class="password-modal-overlay">
        <div class="password-modal">
          <!-- 弹窗头部 -->
          <div class="modal-header">
            <div class="close-btn" @click="resetPasswordModal">✕</div>
            <div class="modal-title">抖音支付</div>
            <div class="empty-space"></div>
          </div>

          <!-- 金额展示 -->
          <div class="amount-section">
            <div class="pay-amount">
              ￥{{ parseFloat(amount || 0).toFixed(2) }}
            </div>
          </div>

          <!-- 付款方式 -->
          <div class="payment-method">
            <div class="method-label">付款方式</div>
            <div class="method-detail">
              <img
                src="data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23ff4757'><path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z'/></svg>"
                alt="银行卡"
                class="bank-icon"
              />
              <span class="bank-name">招商银行储蓄卡(0052)</span>
              <span class="arrow-right">▸</span>
            </div>
          </div>

          <!-- 密码输入框 -->
          <div class="pwd-input-section">
            <div class="pwd-container">
              <input
                type="password"
                v-model="password"
                maxlength="6"
                class="hidden-input"
                readonly
              />
              <div class="pwd-display">
                <div class="pwd-item" v-for="i in 6" :key="i">
                  <div class="pwd-dot" v-if="password.length >= i"></div>
                </div>
              </div>
            </div>
          </div>

          <!-- 数字键盘 -->
          <div class="numeric-keyboard">
            <div class="key-row">
              <div
                class="key"
                v-for="i in 3"
                :key="i"
                @click="inputPassword(i)"
              >
                {{ i }}
              </div>
            </div>
            <div class="key-row">
              <div
                class="key"
                v-for="i in 3"
                :key="i + 3"
                @click="inputPassword(i + 3)"
              >
                {{ i + 3 }}
              </div>
            </div>
            <div class="key-row">
              <div
                class="key"
                v-for="i in 3"
                :key="i + 6"
                @click="inputPassword(i + 6)"
              >
                {{ i + 6 }}
              </div>
            </div>
            <div class="key-row">
              <div class="key empty-key"></div>
              <div class="key" @click="inputPassword(0)">0</div>
              <div class="key delete-key" @click="deletePassword">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="#333">
                  <path
                    d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"
                  />
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from 'axios';
import { mapState } from 'vuex';

import { mapActions } from 'vuex';
export default {
  computed: {
    ...mapState('userInfo', ['userInfo'])
  },
  data() {
    return {
      fromUser: '',
      toUser: '',
      orderId: '',
      amount: '',
      remark: '',
      password: '',
      showPasswordModal: false,
      transferSuccess: false,
      showSuccessPage: false,
      transferTime: '',
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
      ]
    };
  },
  mounted() {
    // 从 Vuex 中获取 userInfo
    const userInfoMap = this.userInfo;
    console.log(this.userInfo);
    this.fromUser = userInfoMap.id || '';
    // 优先取路由动态参数 id，不存在则分割 URL 路径取最后一段，默认 ''
    this.toUser =
      this.$route.params.id ||
      this.$route.path.split('/').filter(Boolean).pop() ||
      '';

    // 获取订单ID参数
    this.orderId = this.$route.query.orderId || '';
    console.log('订单ID:', this.orderId);

    const socket = new SockJS('http://localhost:7456/ws');
    this.stomp = Stomp.over(socket);

    this.stomp.connect({}, () => {
      this.stomp.subscribe('/topic/transfer/' + this.fromUser, (msg) => {
        this.$message.success('转账成功: ' + msg.body);
      });
    });
  },

  methods: {
    ...mapActions('userInfo', ['setUserInfo', 'clearUserInfo']),

    openPasswordModal() {
      if (!this.amount || parseFloat(this.amount) <= 0) {
        this.$message.error('请输入有效的转账金额');
        return;
      }
      this.showPasswordModal = true;
    },

    // 输入密码
    inputPassword(value) {
      if (this.password.length < 6) {
        this.password += value.toString();

        // 密码输入完成自动提交
        if (this.password.length === 6) {
          setTimeout(() => {
            this.submitTransfer();
          }, 300);
        }
      }
    },

    // 删除密码
    deletePassword() {
      if (this.password.length > 0) {
        this.password = this.password.slice(0, -1);
      }
    },

    submitTransfer() {
      const { fromUser, toUser, amount, password, orderId } = this;
      axios
        .post('/api/shop/transfer', null, {
          params: {
            fromUserId: fromUser,
            toUserId: toUser,
            amount,
            password,
            orderId: orderId // 传递订单ID
          }
        })
        .then((res) => {
          this.$message.success('转账成功' + res.data);
          this.resetPasswordModal();
          // 设置转账成功状态和时间
          this.transferSuccess = true;
          this.transferTime = this.formatDateTime(new Date());
          setTimeout(() => {
            // 显示成功页面
            this.showSuccessPage = true;
          }, 500);
        })
        .catch((err) => {
          this.$message.error(
            '转账失败: ' + (err.response?.data || '服务器错误')
          );
          this.resetPasswordModal();
        });
    },

    // 格式化日期时间
    formatDateTime(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },

    // 查看转账详情
    viewTransferDetails() {
      // 查看转账详情的逻辑
      console.log('查看转账详情');
    },

    // 再次转账
    transferAgain() {
      this.showSuccessPage = false;
      this.amount = '';
      this.remark = '';
      this.transferSuccess = false;
    },
    resetPasswordModal() {
      this.password = '';
      this.showPasswordModal = false;
    }
  }
};
</script>

<style scoped>
.transfer-page {
  padding: 0;
  font-family:
    -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Helvetica Neue', Arial,
    sans-serif;
  background: #f5f5f5;
  min-height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  background: #fff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  margin-bottom: 0;
  position: relative;
}

.back {
  position: absolute;
  left: 16px;
  margin-right: 0;
  font-size: 22px;
  color: #333;
  cursor: pointer;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.recipient-info {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  padding: 30px 0;
  background: #fff;
  margin-bottom: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.avatar-container {
  position: relative;
  margin-bottom: 12px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid #f0f0f0;
  object-fit: cover;
}

.verify-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  background: linear-gradient(135deg, #ff4757 0%, #ff6b81 100%);
  color: white;
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 12px;
  border: 2px solid white;
  font-weight: 500;
}

.user-details {
  text-align: center;
}

.nickname {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 4px;
  color: #333;
}

.user-id {
  font-size: 14px;
  color: #999;
}

.transfer-section,
.remark-section {
  background: #fff;
  padding: 16px;
  margin-bottom: 1px;
  box-shadow: 0 1px 0 rgba(0, 0, 0, 0.03);
}

.section-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
  font-weight: 500;
}

.amount-container {
  display: flex;
  align-items: baseline;
}

.currency-symbol {
  font-size: 28px;
  color: #333;
  margin-right: 6px;
  font-weight: 600;
}

.amount-input {
  border: none;
  outline: none;
  font-size: 42px;
  font-weight: 600;
  color: #333;
  width: calc(100% - 30px);
  background: transparent;
  padding: 0;
  letter-spacing: -1px;
}

.amount-input::placeholder {
  color: #d9d9d9;
  letter-spacing: 0;
}

.remark-input {
  width: 100%;
  border: none;
  outline: none;
  font-size: 16px;
  color: #333;
  background: transparent;
  padding: 0;
  letter-spacing: 0.5px;
}

.remark-input::placeholder {
  color: #d9d9d9;
}

.arrival-info {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px 0;
  font-size: 14px;
  color: #999;
  background: #fff;
  margin-bottom: 20px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.icon-clock {
  display: inline-block;
  width: 14px;
  height: 14px;
  background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z"/></svg>')
    no-repeat center;
  background-size: contain;
  margin-right: 5px;
}

.confirm-btn {
  width: calc(100% - 32px);
  margin: 20px 16px;
  background: linear-gradient(135deg, #ff4757 0%, #ff6b81 100%);
  border: none;
  border-radius: 25px;
  color: white;
  padding: 16px;
  font-size: 17px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 6px 16px rgba(255, 71, 87, 0.3);
  transition: all 0.3s ease;
}

.confirm-btn:active {
  transform: scale(0.98);
  box-shadow: 0 3px 8px rgba(255, 71, 87, 0.2);
}

/* 输入框聚焦时的样式优化 */
.amount-input:focus {
  caret-color: #ff4757;
}

.remark-input:focus {
  caret-color: #ff4757;
}

/* 数字键盘样式优化 */
.key {
  width: 30%;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 22px;
  color: #333;
  border-radius: 50%;
  background-color: #fff;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.15s ease;
  font-weight: 500;
}

.key:active {
  background-color: #f8f8f8;
  transform: scale(0.97);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* 成功页面按钮样式优化 */
.primary-btn {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #ff4757 0%, #ff6b81 100%);
  border: none;
  border-radius: 25px;
  font-size: 17px;
  font-weight: 600;
  color: #fff;
  cursor: pointer;
  box-shadow: 0 6px 16px rgba(255, 71, 87, 0.3);
  transition: all 0.3s ease;
}

.primary-btn:active {
  transform: scale(0.98);
  box-shadow: 0 3px 8px rgba(255, 71, 87, 0.2);
}

.secondary-btn {
  width: 100%;
  padding: 16px;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 25px;
  font-size: 17px;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  transition: all 0.3s ease;
}

.secondary-btn:active {
  background-color: #f8f8f8;
  transform: scale(0.98);
}

/* 弹窗样式微调 */
.pwd-item {
  width: 42px;
  height: 42px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
}

.pwd-dot {
  width: 16px;
  height: 16px;
  background-color: #333;
  border-radius: 50%;
  animation: pulse 0.3s ease;
}

@keyframes pulse {
  0% {
    transform: scale(0.8);
    opacity: 0.7;
  }
  50% {
    transform: scale(1.1);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

/* ===== 弹窗样式 ===== */
.password-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: flex-end;
  z-index: 1000;
}

.password-modal {
  width: 100%;
  background-color: white;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  padding: 0;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 弹窗头部 */
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 0;
}

.close-btn {
  font-size: 20px;
  color: #666;
  width: 24px;
  height: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-title {
  font-size: 17px;
  font-weight: 600;
  color: #333;
}

.empty-space {
  width: 24px;
}

/* 金额展示 */
.amount-section {
  padding: 20px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pay-amount {
  font-size: 30px;
  font-weight: 600;
  color: #333;
}

/* 付款方式 */
.payment-method {
  padding: 0 16px 16px;
}

.method-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.method-detail {
  display: flex;
  align-items: center;
  background-color: #f9f9f9;
  padding: 12px 16px;
  border-radius: 8px;
}

.bank-icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.bank-name {
  flex: 1;
  font-size: 15px;
  color: #333;
}

.arrow-right {
  font-size: 16px;
  color: #999;
}

/* 密码输入区域 */
.pwd-input-section {
  padding: 20px 16px;
  background-color: #f9f9f9;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.pwd-container {
  position: relative;
  width: 100%;
}

.hidden-input {
  position: absolute;
  opacity: 0;
  z-index: -1;
}

.pwd-display {
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 300px;
  margin: 0 auto;
}

.pwd-item {
  width: 40px;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.05);
}

.pwd-dot {
  width: 14px;
  height: 14px;
  background-color: #333;
  border-radius: 50%;
}

/* 数字键盘 */
.numeric-keyboard {
  display: flex;
  flex-direction: column;
  padding: 10px 0;
}

.key-row {
  display: flex;
  justify-content: space-between;
  padding: 5px 20px;
}

.key {
  width: 30%;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  color: #333;
  border-radius: 50%;
  background-color: #fff;
  border: none;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  transition: all 0.1s ease;
}

.key:active {
  background-color: #f0f0f0;
  transform: scale(0.98);
}

.empty-key {
  opacity: 0;
}

.delete-key {
  background-color: #fff;
}

/* 转账成功页面样式 */
.success-page {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #fff;
  z-index: 1001;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.success-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 40px;
}

.success-icon {
  margin-bottom: 20px;
}

.success-text {
  text-align: center;
  margin-bottom: 40px;
}

.success-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.success-amount {
  font-size: 36px;
  font-weight: 600;
  color: #ff4757;
  margin-bottom: 10px;
}

.refund-info {
  font-size: 14px;
  color: #999;
}

.transfer-info {
  width: 100%;
  background-color: #f9f9f9;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 40px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 15px;
  color: #666;
}

.info-value {
  font-size: 15px;
  color: #333;
}

.action-buttons {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.secondary-btn {
  width: 100%;
  padding: 14px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  cursor: pointer;
}

.primary-btn {
  width: 100%;
  padding: 14px;
  background-color: #ff4757;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(255, 71, 87, 0.3);
}

.footer-info {
  text-align: center;
  font-size: 12px;
  color: #999;
  padding: 20px 0;
}

/* 动画 */
.slide-up-enter-active,
.slide-up-leave-active {
  transition: transform 0.3s cubic-bezier(0.25, 0.1, 0.25, 1);
}

.slide-up-enter,
.slide-up-leave-to {
  transform: translateY(100%);
}

/* 成功页面动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.success-page {
  animation: fadeIn 0.5s ease-out;
}
</style>
