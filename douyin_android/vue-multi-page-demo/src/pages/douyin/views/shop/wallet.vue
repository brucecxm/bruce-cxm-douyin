<template>
  <div class="wallet-container">
    <!-- 钱包状态加载中 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 已开通钱包 -->
    <div v-else-if="walletEnabled" class="wallet-content">
      <!-- 顶部蓝色背景区域 -->
      <div class="top-blue-section">
        <div class="header-bar">
          <h2>**明的钱包</h2>
          <div class="header-actions">
            <span class="edit-btn">补全资料提升限额</span>
            <i class="icon-more"></i>
            <i class="icon-settings"></i>
          </div>
        </div>

        <!-- 余额展示 -->
        <div class="balance-section">
          <div class="balance-info">
            <div class="balance-label">总资产(元)</div>
            <div class="balance-amount">¥{{ balance.toFixed(2) }}</div>
            <div class="balance-safe">
              <i class="icon-safe"></i>
              <span>安全保障中</span>
            </div>
          </div>
          <i class="icon-arrow"></i>
        </div>

        <!-- 快捷操作按钮 -->
        <div class="quick-actions">
          <button class="action-btn primary-btn" @click="showRechargeDialog">
            充值
          </button>
          <button class="action-btn secondary-btn" @click="showWithdrawDialog">
            提现
          </button>
        </div>
      </div>

      <!-- 资产卡片 -->
      <div class="asset-cards">
        <div class="asset-item">
          <span class="asset-icon diamond-icon">💎</span>
          <span class="asset-value">{{ diamonds || 0 }}</span>
          <span class="asset-label">钻石</span>
        </div>
        <div class="asset-item">
          <span class="asset-icon coin-icon">💰</span>
          <span class="asset-value">{{ coins || 0 }}</span>
          <span class="asset-label">金币</span>
        </div>
        <div class="asset-item">
          <span class="asset-icon coupon-icon">🎫</span>
          <span class="asset-value">{{ coupons || 0 }}</span>
          <span class="asset-label">优惠券</span>
        </div>
        <div class="asset-item">
          <span class="asset-icon card-icon">💳</span>
          <span class="asset-value">{{ bankCards || 0 }}</span>
          <span class="asset-label">银行卡</span>
        </div>
      </div>

      <!-- 交易记录 -->
      <div class="transaction-section">
        <div class="section-header">
          <h3>账单</h3>
          <div class="transaction-more" @click="viewAllTransactions">
            <span>全部</span>
            <i class="icon-arrow-right"></i>
          </div>
        </div>
        <div
          class="transaction-item"
          v-for="item in transactions"
          :key="item.id"
        >
          <div class="transaction-info">
            <div
              class="transaction-icon"
              :class="getTransactionIcon(item.type)"
            ></div>
            <div class="transaction-details">
              <div class="transaction-title">{{ item.description }}</div>
              <div class="transaction-time">
                {{ formatTime(item.createTime) }}
              </div>
            </div>
          </div>
          <div
            class="transaction-amount"
            :class="
              item.type === 'TRANSFER_IN' ? 'amount-income' : 'amount-outcome'
            "
          >
            {{ item.type === 'TRANSFER_IN' ? '+' : '-' }}¥{{
              item.amount.toFixed(2)
            }}
          </div>
        </div>
        <div v-if="transactions.length === 0" class="empty-transactions">
          <p>暂无交易记录</p>
        </div>
      </div>

      <!-- 服务网格 -->
      <div class="services-grid">
        <div class="service-item" @click="navigateTo('loan')">
          <div class="service-icon loan-icon">
            <i class="icon-loan"></i>
          </div>
          <span class="service-name">放心借</span>
        </div>
        <div class="service-item" @click="navigateTo('payment')">
          <div class="service-icon payment-icon">
            <i class="icon-payment"></i>
          </div>
          <span class="service-name">抖音支付</span>
        </div>
        <div class="service-item" @click="navigateTo('recharge')">
          <div class="service-icon recharge-icon">
            <i class="icon-recharge"></i>
          </div>
          <span class="service-name">手机充值</span>
        </div>
        <div class="service-item" @click="navigateTo('insurance')">
          <div class="service-icon insurance-icon">
            <i class="icon-insurance"></i>
          </div>
          <span class="service-name">我的保障</span>
        </div>
        <div class="service-item" @click="navigateTo('phoneBill')">
          <div class="service-icon phone-bill-icon">
            <i class="icon-phone-bill"></i>
          </div>
          <span class="service-name">抖音信用卡</span>
        </div>
        <div class="service-item" @click="navigateTo('utilities')">
          <div class="service-icon utilities-icon">
            <i class="icon-utilities"></i>
          </div>
          <span class="service-name">生活缴费</span>
        </div>
        <div class="service-item" @click="navigateTo('ride')">
          <div class="service-icon ride-icon">
            <i class="icon-ride"></i>
          </div>
          <span class="service-name">打车</span>
        </div>
        <div class="service-item" @click="navigateTo('more')">
          <div class="service-icon more-icon">
            <i class="icon-more-services"></i>
          </div>
          <span class="service-name">更多服务</span>
        </div>
      </div>

      <!-- 活动区域 -->
      <div class="activity-section">
        <div class="activity-item">
          <div class="activity-icon">💳</div>
          <div class="activity-info">
            <div class="activity-title">放心借</div>
            <div class="activity-desc">领额度收20元</div>
          </div>
          <button class="activity-btn">收下</button>
        </div>
        <div class="activity-item">
          <div class="activity-icon">⭐</div>
          <div class="activity-info">
            <div class="activity-title">抖音支付积分</div>
            <div class="activity-desc">100积分兑3元</div>
          </div>
          <div class="activity-more">
            <span>积分明细</span>
            <i class="icon-arrow-right"></i>
          </div>
        </div>
        <div class="activity-item">
          <div class="activity-icon">🎁</div>
          <div class="activity-info">
            <div class="activity-title">天天领音符</div>
            <div class="activity-desc">连续签到得奖励</div>
          </div>
          <button class="activity-btn">立即签到</button>
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
      diamonds: 0,
      coins: 0,
      coupons: 16,
      bankCards: 2,
      showRecharge: false,
      showWithdraw: false,
      showEnableDialog: false,
      rechargeAmount: 0,
      withdrawAmount: 0,
      paymentPassword: '',
      confirmPassword: '',
      passwordError: '',
      transactions: [],
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

        const transactionsResponse = await getWalletTransactions({
          page: 1,
          pageSize: 5
        });
        if (
          transactionsResponse.data.list &&
          transactionsResponse.data.list.length > 0
        ) {
          this.transactions = transactionsResponse.data.list;
        } else {
          // 模拟数据
          this.transactions = [
            {
              id: 1,
              type: 'TRANSFER_OUT',
              description: '聊天转账退款成功',
              amount: 200,
              createTime: new Date().getTime() - 14 * 24 * 60 * 60 * 1000
            },
            {
              id: 2,
              type: 'TRANSFER_IN',
              description: '抖音支付充值',
              amount: 100,
              createTime: new Date().getTime() - 2 * 24 * 60 * 60 * 1000
            },
            {
              id: 3,
              type: 'TRANSFER_OUT',
              description: '购买商品',
              amount: 56.8,
              createTime: new Date().getTime() - 5 * 24 * 60 * 60 * 1000
            }
          ];
        }
      } catch (error) {
        console.error('加载钱包数据失败:', error);
        this.$message.error('加载钱包数据失败');
      }
    },

    // 格式化时间
    formatTime(timestamp) {
      const now = new Date();
      const date = new Date(timestamp);
      const diffDays = Math.floor((now - date) / (1000 * 60 * 60 * 24));

      if (diffDays === 0) {
        return '今天';
      } else if (diffDays === 1) {
        return '昨天';
      } else if (diffDays < 7) {
        return `${diffDays}天前`;
      } else {
        return `${date.getMonth() + 1}月${date.getDate()}日`;
      }
    },

    // 获取交易类型图标
    getTransactionIcon(type) {
      if (type === 'TRANSFER_IN') return 'income-icon';
      return 'outcome-icon';
    },

    // 查看全部交易记录
    viewAllTransactions() {
      this.$message.info('查看全部交易记录');
    },

    // 导航到服务页面
    navigateTo(service) {
      this.$message.info(`跳转到${service}服务`);
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
/* 全局样式 */
.wallet-container {
  width: 100%;
  min-height: 100vh;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue',
    Arial, sans-serif;
  background-color: #f5f5f5;
  color: #333;
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
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-top: 4px solid #1677ff;
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

/* 顶部蓝色背景区域 */
.top-blue-section {
  background: linear-gradient(135deg, #1677ff, #0050c9);
  padding: 16px;
  border-radius: 0 0 20px 20px;
  box-shadow: 0 4px 12px rgba(22, 119, 255, 0.2);
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  color: white;
}

.header-bar h2 {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.edit-btn {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
  padding: 4px 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
}

/* 余额展示 */
.balance-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.balance-info {
  flex: 1;
}

.balance-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 6px;
}

.balance-amount {
  font-size: 32px;
  font-weight: 700;
  color: white;
  margin-bottom: 8px;
}

.balance-safe {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
}

.balance-safe i {
  margin-right: 4px;
}

/* 快捷操作按钮 */
.quick-actions {
  display: flex;
  gap: 12px;
}

.action-btn {
  flex: 1;
  padding: 10px 0;
  border: none;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.primary-btn {
  background-color: white;
  color: #1677ff;
}

.primary-btn:hover {
  background-color: rgba(255, 255, 255, 0.9);
}

.secondary-btn {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
}

.secondary-btn:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

/* 资产卡片 */
.asset-cards {
  display: flex;
  justify-content: space-between;
  padding: 16px;
  background-color: white;
  margin: -10px 16px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.asset-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.asset-icon {
  font-size: 24px;
  margin-bottom: 6px;
}

.asset-value {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 4px;
}

.asset-label {
  font-size: 12px;
  color: #666;
}

/* 交易记录 */
.transaction-section {
  background-color: white;
  margin: 0 16px 16px;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-header h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.transaction-more {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #999;
}

.transaction-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.transaction-item:last-child {
  border-bottom: none;
}

.transaction-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.transaction-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 18px;
}

.income-icon {
  background-color: #e6f7ff;
  color: #1890ff;
}

.outcome-icon {
  background-color: #fff7e6;
  color: #fa8c16;
}

.transaction-details {
  flex: 1;
}

.transaction-title {
  font-size: 14px;
  margin-bottom: 4px;
}

.transaction-time {
  font-size: 12px;
  color: #999;
}

.transaction-amount {
  font-size: 16px;
  font-weight: 600;
}

.amount-income {
  color: #1890ff;
}

.amount-outcome {
  color: #333;
}

.empty-transactions {
  text-align: center;
  padding: 20px 0;
  color: #999;
}

/* 服务网格 */
.services-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  padding: 16px;
  background-color: white;
  margin: 0 16px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.service-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 8px 0;
}

.service-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  font-size: 24px;
}

.loan-icon {
  background-color: #fff2f0;
  color: #ff4d4f;
}

.payment-icon {
  background-color: #f6ffed;
  color: #52c41a;
}

.recharge-icon {
  background-color: #e6f7ff;
  color: #1890ff;
}

.insurance-icon {
  background-color: #f9f0ff;
  color: #722ed1;
}

.phone-bill-icon {
  background-color: #fff7e6;
  color: #fa8c16;
}

.utilities-icon {
  background-color: #f0f5ff;
  color: #2f54eb;
}

.ride-icon {
  background-color: #f6ffed;
  color: #52c41a;
}

.more-icon {
  background-color: #f0f0f0;
  color: #666;
}

.service-name {
  font-size: 12px;
  color: #333;
}

/* 活动区域 */
.activity-section {
  padding: 0 16px 16px;
}

.activity-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: white;
  padding: 16px;
  border-radius: 12px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 20px;
  background-color: #f0f5ff;
}

.activity-info {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
}

.activity-desc {
  font-size: 12px;
  color: #666;
}

.activity-btn {
  padding: 6px 16px;
  background-color: #1677ff;
  color: white;
  border: none;
  border-radius: 16px;
  font-size: 12px;
  cursor: pointer;
}

.activity-more {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #999;
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
  background-color: #1677ff;
  color: white;
}

.dialog button:first-of-type:hover {
  background-color: #0958d9;
}

.dialog button:last-of-type {
  background-color: #f5f5f5;
  color: #666;
}

.dialog button:last-of-type:hover {
  background-color: #e8e8e8;
}

/* 图标样式 - 使用Unicode图标或CSS绘制简单图标 */
.icon-safe::before {
  content: '🔒';
  margin-right: 4px;
}

.icon-arrow::before {
  content: '›';
  font-size: 20px;
  color: white;
}

.icon-more::before {
  content: '⋮';
  font-size: 18px;
  color: white;
}

.icon-settings::before {
  content: '⚙';
  font-size: 16px;
  color: white;
}

.icon-arrow-right::before {
  content: '›';
  margin-left: 4px;
  font-size: 16px;
}

.icon-loan::before {
  content: '💳';
}
.icon-payment::before {
  content: '💰';
}
.icon-recharge::before {
  content: '📱';
}
.icon-insurance::before {
  content: '🛡️';
}
.icon-phone-bill::before {
  content: '📲';
}
.icon-utilities::before {
  content: '⚡';
}
.icon-ride::before {
  content: '🚕';
}
.icon-more-services::before {
  content: '⋯';
}
</style>
