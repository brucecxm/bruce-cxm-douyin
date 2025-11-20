<template>
  <div class="wallet-container">
    <!-- 钱包状态加载中 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 已开通钱包 -->
    <div v-else-if="walletEnabled" class="wallet-content">
      <!-- Header Section -->
      <div class="header">
        <div class="balance">余额: ¥{{ balance }}</div>
        <div class="actions">
          <button @click="showRechargeDialog">充值</button>
          <button @click="showWithdrawDialog">提现</button>
        </div>
      </div>

      <!-- Recharge Dialog -->
      <div v-if="showRecharge" class="dialog">
        <div class="dialog-content">
          <h3>充值</h3>
          <input
            type="number"
            v-model="rechargeAmount"
            placeholder="输入充值金额"
          />
          <button @click="recharge">确认充值</button>
          <button @click="closeDialog">取消</button>
        </div>
      </div>

      <!-- Withdraw Dialog -->
      <div v-if="showWithdraw" class="dialog">
        <div class="dialog-content">
          <h3>提现</h3>
          <input
            type="number"
            v-model="withdrawAmount"
            placeholder="输入提现金额"
          />
          <button @click="withdraw">确认提现</button>
          <button @click="closeDialog">取消</button>
        </div>
      </div>

      <!-- Expenditure Details Section -->
      <div class="expenditure">
        <h3>支出明细</h3>
        <div class="item" v-for="item in expenditures" :key="item.id">
          <div>{{ item.date }} - {{ item.description }}</div>
          <div>¥{{ item.amount }}</div>
        </div>
      </div>
    </div>

    <!-- 未开通钱包 -->
    <div v-else class="wallet-not-enabled">
      <div class="wallet-icon-animation">💰</div>
      <h3>开通您的专属钱包</h3>
      <p class="wallet-description">开通钱包后，您可以享受：</p>
      <ul class="wallet-benefits">
        <li class="benefit-item">
          <span class="benefit-icon">💸</span>
          <span>便捷的充值提现</span>
        </li>
        <li class="benefit-item">
          <span class="benefit-icon">⚡</span>
          <span>快速的转账功能</span>
        </li>
        <li class="benefit-item">
          <span class="benefit-icon">🔒</span>
          <span>安全的交易保障</span>
        </li>
      </ul>
      <button class="enable-btn primary-btn" @click="showEnableWalletDialog">
        立即开通
      </button>
      <p class="wallet-note">只需设置6位数字支付密码，轻松开通</p>
    </div>

    <!-- 开通钱包对话框 -->
    <div v-if="showEnableDialog" class="dialog">
      <div class="dialog-content">
        <h3>设置支付密码</h3>
        <div class="wallet-security-tip">
          <span>🔒</span>
          <span>支付密码用于保护您的资金安全，请妥善保管</span>
        </div>
        <p>请设置6位数字支付密码，用于资金安全验证</p>
        <input
          type="password"
          v-model="paymentPassword"
          placeholder="输入支付密码"
          maxlength="6"
          pattern="[0-9]{6}"
          @input="validatePassword"
        />
        <div v-if="paymentPassword" class="password-strength">
          <div class="strength-bar">
            <div
              class="strength-level"
              :class="{
                weak: passwordStrength === 'weak',
                medium: passwordStrength === 'medium',
                strong: passwordStrength === 'strong'
              }"
              :style="{ width: strengthWidth + '%' }"
            ></div>
          </div>
          <span class="strength-text">{{ strengthText }}</span>
        </div>
        <input
          type="password"
          v-model="confirmPassword"
          placeholder="确认支付密码"
          maxlength="6"
          pattern="[0-9]{6}"
        />
        <div
          v-if="confirmPassword && paymentPassword !== confirmPassword"
          class="password-error"
        >
          <span>❌</span>
          <span>两次输入的密码不一致</span>
        </div>
        <div class="error-message" v-if="passwordError">
          {{ passwordError }}
        </div>
        <button @click="enableWallet" :disabled="enablingWallet">
          {{ enablingWallet ? '开通中...' : '确认开通' }}
        </button>
        <button @click="closeEnableDialog">取消</button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  checkWalletStatus,
  enableWallet,
  getWalletBalance,
  rechargeWallet,
  withdrawWallet,
  getWalletTransactions
} from '@/api/wallet';

export default {
  data() {
    return {
      loading: true,
      walletEnabled: false,
      balance: 0,
      showRecharge: false,
      showWithdraw: false,
      showEnableDialog: false,
      rechargeAmount: 0,
      withdrawAmount: 0,
      paymentPassword: '',
      confirmPassword: '',
      passwordError: '',
      expenditures: [],
      enablingWallet: false,
      passwordStrength: 'weak',
      strengthWidth: 33,
      strengthText: '弱'
    };
  },
  mounted() {
    // 页面加载时检查钱包状态
    this.checkWalletStatus();
  },
  methods: {
    // 检查钱包状态
    async checkWalletStatus() {
      try {
        this.loading = true;
        const response = await checkWalletStatus();
        this.walletEnabled = response.data.enabled;
        if (this.walletEnabled) {
          await this.loadWalletData();
        }
      } catch (error) {
        console.error('检查钱包状态失败:', error);
        this.$message.error('检查钱包状态失败，请稍后重试');
      } finally {
        this.loading = false;
      }
    },

    // 加载钱包数据
    async loadWalletData() {
      try {
        const balanceResponse = await getWalletBalance();
        this.balance = balanceResponse.data.balance || 0;

        const transactionsResponse = await getWalletTransactions();
        if (
          transactionsResponse.data.list &&
          transactionsResponse.data.list.length > 0
        ) {
          this.expenditures = transactionsResponse.data.list.map((item) => ({
            id: item.id,
            date: item.date,
            description:
              item.description || (item.type === 'recharge' ? '充值' : '提现'),
            amount: item.amount
          }));
        }
      } catch (error) {
        console.error('加载钱包数据失败:', error);
        this.$message.error('加载钱包数据失败');
      }
    },

    // 显示开通钱包对话框
    showEnableWalletDialog() {
      this.showEnableDialog = true;
    },

    // 关闭开通钱包对话框
    closeEnableDialog() {
      this.showEnableDialog = false;
      this.paymentPassword = '';
      this.confirmPassword = '';
      this.passwordError = '';
    },

    // 验证密码强度
    validatePassword() {
      if (!this.paymentPassword || this.paymentPassword.length < 6) {
        this.passwordStrength = 'weak';
        this.strengthWidth = 33;
        this.strengthText = '弱';
        return;
      }

      // 简单的密码强度判断
      const hasRepeated = /(\d)\1{2,}/.test(this.paymentPassword); // 连续重复
      const hasSequential =
        /012|123|234|345|456|567|678|789|987|876|765|654|543|432|321|210/.test(
          this.paymentPassword
        ); // 连续数字

      if (
        hasRepeated ||
        hasSequential ||
        this.paymentPassword === '123456' ||
        this.paymentPassword === '654321'
      ) {
        this.passwordStrength = 'weak';
        this.strengthWidth = 33;
        this.strengthText = '弱';
      } else if (new Set(this.paymentPassword).size > 4) {
        this.passwordStrength = 'strong';
        this.strengthWidth = 100;
        this.strengthText = '强';
      } else {
        this.passwordStrength = 'medium';
        this.strengthWidth = 66;
        this.strengthText = '中';
      }
    },

    // 开通钱包
    async enableWallet() {
      // 验证密码
      if (
        !this.paymentPassword ||
        this.paymentPassword.length !== 6 ||
        !/^\d+$/.test(this.paymentPassword)
      ) {
        this.passwordError = '请输入6位数字支付密码';
        return;
      }

      if (this.paymentPassword !== this.confirmPassword) {
        this.passwordError = '两次输入的密码不一致';
        return;
      }

      this.enablingWallet = true;
      try {
        await enableWallet({ paymentPassword: this.paymentPassword });
        this.$message.success('钱包开通成功');
        this.closeEnableDialog();
        // 重新检查钱包状态
        this.checkWalletStatus();
      } catch (error) {
        console.error('开通钱包失败:', error);
        this.$message.error('开通钱包失败，请稍后重试');
      } finally {
        this.enablingWallet = false;
      }
    },

    // Show recharge dialog
    showRechargeDialog() {
      this.showRecharge = true;
    },

    // Show withdraw dialog
    showWithdrawDialog() {
      this.showWithdraw = true;
    },

    // Close any dialog
    closeDialog() {
      this.showRecharge = false;
      this.showWithdraw = false;
      this.rechargeAmount = 0;
      this.withdrawAmount = 0;
    },

    // Handle recharge functionality
    async recharge() {
      if (this.rechargeAmount <= 0) {
        this.$message.error('请输入有效的充值金额');
        return;
      }

      try {
        await rechargeWallet({ amount: this.rechargeAmount });
        this.$message.success('充值成功');
        this.closeDialog();
        await this.loadWalletData();
      } catch (error) {
        console.error('充值失败:', error);
        this.$message.error('充值失败，请稍后重试');
      }
    },

    // Handle withdraw functionality
    async withdraw() {
      if (this.withdrawAmount <= 0) {
        this.$message.error('请输入有效的提现金额');
        return;
      }

      if (this.withdrawAmount > this.balance) {
        this.$message.error('提现金额不能大于余额');
        return;
      }

      try {
        await withdrawWallet({ amount: this.withdrawAmount });
        this.$message.success('提现申请已提交');
        this.closeDialog();
        await this.loadWalletData();
      } catch (error) {
        console.error('提现失败:', error);
        this.$message.error('提现失败，请稍后重试');
      }
    }
  }
};
</script>

<style scoped>
.wallet-container {
  width: 100%;
  min-height: 100vh;
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
}

/* 加载中样式 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 80vh;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 已开通钱包样式 */
.wallet-content {
  padding: 20px;
}

.header {
  background-color: #007bff;
  color: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.balance {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 15px;
}

.actions {
  display: flex;
  gap: 15px;
}

.actions button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: background-color 0.3s;
}

.actions button:first-child {
  background-color: #28a745;
  color: white;
}

.actions button:first-child:hover {
  background-color: #218838;
}

.actions button:nth-child(2) {
  background-color: #dc3545;
  color: white;
}

.actions button:nth-child(2):hover {
  background-color: #c82333;
}

/* 未开通钱包样式 */
.wallet-not-enabled {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 80vh;
  text-align: center;
  padding: 40px;
  background-color: #fafafa;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.wallet-not-enabled:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.wallet-icon-animation {
  font-size: 80px;
  margin-bottom: 20px;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%,
  20%,
  50%,
  80%,
  100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-20px);
  }
  60% {
    transform: translateY(-10px);
  }
}

.wallet-not-enabled h3 {
  color: #333;
  margin-bottom: 15px;
  font-size: 24px;
}

.wallet-description {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

.wallet-benefits {
  list-style: none;
  padding: 0;
  margin: 20px 0 30px;
}

.benefit-item {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 10px 0;
  font-size: 14px;
  color: #666;
}

.benefit-icon {
  font-size: 18px;
  margin-right: 8px;
}

.primary-btn {
  margin-top: 20px;
  padding: 12px 40px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.primary-btn:hover {
  background-color: #0056b3;
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.3);
  transform: translateY(-2px);
}

.wallet-note {
  margin-top: 15px;
  font-size: 12px;
  color: #999;
}

/* 密码强度样式 */
.wallet-security-tip {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  background-color: #e3f2fd;
  border-radius: 4px;
  margin-bottom: 20px;
  font-size: 14px;
  color: #1976d2;
}

.wallet-security-tip span:first-child {
  margin-right: 8px;
}

.password-strength {
  margin-top: 8px;
}

.strength-bar {
  height: 4px;
  background-color: #eee;
  border-radius: 2px;
  overflow: hidden;
}

.strength-level {
  height: 100%;
  transition: all 0.3s ease;
  border-radius: 2px;
}

.strength-level.weak {
  background-color: #f44336;
}

.strength-level.medium {
  background-color: #ff9800;
}

.strength-level.strong {
  background-color: #4caf50;
}

.strength-text {
  font-size: 12px;
  margin-top: 4px;
  color: #999;
  display: block;
}

.password-error {
  display: flex;
  align-items: center;
  margin-top: 8px;
  color: #f44336;
  font-size: 12px;
}

.password-error span:first-child {
  margin-right: 4px;
}

/* 对话框样式 */
.dialog {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.dialog-content {
  background-color: white;
  padding: 30px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.2);
  width: 90%;
  max-width: 400px;
}

.dialog-content h3 {
  margin-bottom: 15px;
  color: #333;
}

.dialog-content p {
  color: #666;
  margin-bottom: 20px;
}

.dialog input {
  padding: 12px;
  margin: 10px 0;
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.error-message {
  color: #dc3545;
  margin: 10px 0;
  font-size: 14px;
}

.dialog button {
  margin: 10px 5px;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.dialog button:first-of-type {
  background-color: #007bff;
  color: white;
}

.dialog button:first-of-type:hover {
  background-color: #0056b3;
}

.dialog button:last-of-type {
  background-color: #6c757d;
  color: white;
}

.dialog button:last-of-type:hover {
  background-color: #5a6268;
}

/* 支出明细样式 */
.expenditure {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.expenditure h3 {
  color: #333;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.expenditure .item {
  margin-bottom: 15px;
  padding: 15px;
  border-radius: 4px;
  background-color: #f9f9f9;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: background-color 0.3s;
}

.expenditure .item:hover {
  background-color: #f0f0f0;
}
</style>
