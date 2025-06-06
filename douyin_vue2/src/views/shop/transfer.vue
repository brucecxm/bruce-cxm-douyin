<template>
  <div class="transfer-page">
    <!-- 顶部 -->
    <div class="header">
      <span class="back" @click="$router.go(-1)">←</span>
      <span class="title">转账</span>
    </div>

    <!-- 用户信息展示 -->
    <div class="user-info">
      <div class="user">
        <img class="avatar" src="https://via.placeholder.com/60" />
        <div class="user-text">
          <div class="label">付款人</div>
          <div class="value">ID: {{ fromUser }}</div>
        </div>
      </div>
      <span class="arrow">➡</span>
      <div class="user">
        <img class="avatar" src="https://via.placeholder.com/60" />
        <div class="user-text">
          <div class="label">收款人</div>
          <div class="value">ID: {{ toUser }}</div>
        </div>
      </div>
    </div>

    <!-- 金额输入 -->
    <div class="amount-input-section">
      <div class="currency">￥</div>
      <input
        type="number"
        class="amount-input"
        v-model="amount"
        placeholder="请输入转账金额"
      />
    </div>

    <!-- 按钮 -->
    <button class="submit-btn" @click="openPasswordModal">立即转账</button>

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
      ]
    };
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
          this.resetPasswordModal();
        });
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
