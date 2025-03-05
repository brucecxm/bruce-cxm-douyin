<template>
    <!-- 充值页面组件 -->
    <div class="recharge-page">
        <!-- 页面头部 -->
        <div class="recharge-header">
            <!-- 返回按钮，点击时调用 goBack 方法 -->
            <div class="back-button" @click="goBack">
                <i class="icon-back"></i>
                <i class="fas fa-arrow-left"></i>
            </div>
            <!-- 页面标题 -->
            <div class="recharge-title">充值中心</div>
            <!-- 帮助中心，点击时可以跳转到帮助页面 -->
            <div class="help-center">帮助中心</div>
        </div>

        <!-- 充值金额选择部分 -->
        <div class="recharge-amount">
            <div class="amount-title">请选择充值金额</div>
            <div class="amount-options">
                <!-- 各个充值金额选项，点击时调用 selectAmount 方法 -->
                <div class="amount-option" :class="{ active: selectedAmount === 100 }" @click="selectAmount(100)">
                    ¥100
                </div>
                <div class="amount-option" :class="{ active: selectedAmount === 200 }" @click="selectAmount(200)">
                    ¥200
                </div>
                <div class="amount-option" :class="{ active: selectedAmount === 500 }" @click="selectAmount(500)">
                    ¥500
                </div>
                <div class="amount-option" :class="{ active: selectedAmount === 1000 }" @click="selectAmount(1000)">
                    ¥1000
                </div>
            </div>
        </div>

        <!-- 支付方式选择部分 -->
        <div class="payment-method">
            <div class="payment-title">选择支付方式</div>
            <div class="payment-options">
                <!-- 支付宝支付选项 -->
                <div class="payment-option" :class="{ active: selectedPayment === 'alipay' }"
                    @click="selectPayment('alipay')">
                    <!-- <img src="../../assets/alipay-logo.png" alt="Alipay" class="payment-logo" /> -->
                    <span class="payment-name">支付宝</span>
                </div>
                <!-- 微信支付选项，微信图标部分已被注释 -->
                <div class="payment-option" :class="{ active: selectedPayment === 'wechat' }"
                    @click="selectPayment('wechat')">
                    <!-- <img src="../../assets/wechat-logo.png" alt="WeChat" class="payment-logo" /> -->
                    <span class="payment-name">微信支付</span>
                </div>
                <!-- 银行卡支付选项 -->
                <div class="payment-option" :class="{ active: selectedPayment === 'bank' }"
                    @click="selectPayment('bank')">
                    <!-- <img src="../../assets/bank-logo.png" alt="Bank" class="payment-logo" /> -->
                    <span class="payment-name">银行卡</span>
                </div>
            </div>
        </div>

        <!-- 确认充值按钮 -->
        <div class="recharge-button">
            <button @click="confirmRecharge">确认充值</button>
        </div>

        <!-- 充值记录部分 -->
        <div class="recharge-records">
            <div class="record-title">充值记录</div>
            <!-- 循环显示充值记录 -->
            <div v-for="record in rechargeRecords" :key="record.id" class="recharge-record">
                <!-- 显示充值时间 -->
                <div class="record-time">{{ record.time }}</div>
                <!-- 显示充值金额 -->
                <div class="record-amount">{{ record.amount }}</div>
                <!-- 显示充值状态 -->
                <div class="record-status">{{ record.status }}</div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            // 默认选择的充值金额为100元
            selectedAmount: 100,
            // 默认选择的支付方式为支付宝
            selectedPayment: 'alipay',
            // 假设的充值记录数据
            rechargeRecords: [
                { id: 1, time: '2021-10-15 14:30', amount: '¥100', status: '成功' },
                { id: 2, time: '2021-10-14 10:20', amount: '¥200', status: '成功' },
                // 更多记录...
            ],
        };
    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.go(-1);
        },
        // 选择充值金额
        selectAmount(amount) {
            this.selectedAmount = amount;
        },
        // 选择支付方式
        selectPayment(paymentMethod) {
            this.selectedPayment = paymentMethod;
        },
        // 确认充值
        confirmRecharge() {
            // 打印当前选择的充值金额和支付方式
            console.log(`已选择充值金额：¥${this.selectedAmount}，支付方式：${this.selectedPayment}`);
        },
    },
};
</script>

<style scoped>
/* 页面样式 */
.recharge-page {
    font-family: 'Arial', sans-serif;
    background-color: #f5f5f5;
    height: 100vh;
    display: flex;
    flex-direction: column;
}

/* 头部样式 */
.recharge-header {
    background-color: #fff;
    display: flex;
    justify-content: space-between;
    padding: 12px 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    align-items: center;
}

/* 标题样式 */
.recharge-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
}

/* 帮助中心样式 */
.help-center {
    font-size: 14px;
    color: #ff6a00;
    cursor: pointer;
}

/* 充值金额选择部分 */
.recharge-amount {
    padding: 20px;
    background-color: #fff;
    margin-top: 10px;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

/* 金额标题样式 */
.amount-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
}

/* 金额选项容器样式 */
.amount-options {
    display: flex;
    justify-content: space-around;
}

/* 单个金额选项样式 */
.amount-option {
    font-size: 16px;
    color: #333;
    padding: 10px 20px;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.3s;
}

/* 选中的金额项样式 */
.amount-option.active {
    background-color: #ff6a00;
    color: white;
}

/* 支付方式选择部分 */
.payment-method {
    padding: 20px;
    background-color: #fff;
    margin-top: 10px;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

/* 支付方式标题样式 */
.payment-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
}

/* 支付方式选项容器样式 */
.payment-options {
    display: flex;
    justify-content: space-around;
}

/* 单个支付方式选项样式 */
.payment-option {
    display: flex;
    align-items: center;
    font-size: 14px;
    color: #333;
    padding: 10px 15px;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.3s;
}

/* 选中的支付方式样式 */
.payment-option.active {
    background-color: #ff6a00;
    color: white;
}

/* 支付方式logo样式 */
.payment-logo {
    width: 30px;
    height: 30px;
    margin-right: 10px;
}

/* 充值按钮样式 */
.recharge-button {
    padding: 20px;
    text-align: center;
}

.recharge-button button {
    background-color: #ff6a00;
    color: white;
    border: none;
    padding: 12px 24px;
    font-size: 16px;
    border-radius: 8px;
    cursor: pointer;
    width: 100%;
}

/* 按钮悬浮效果 */
.recharge-button button:hover {
    background-color: #e65c00;
}

/* 充值记录部分 */
.recharge-records {
    margin-top: 20px;
    background-color: #fff;
    padding: 15px;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

/* 记录标题样式 */
.record-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 15px;
}

/* 单条记录样式 */
.recharge-record {
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    color: #555;
    margin-bottom: 10px;
}

/* 记录时间样式 */
.recharge-record .record-time {
    color: #999;
}

/* 记录状态样式 */
.recharge-record .record-status {
    color: #28a745;
    font-weight: bold;
}
</style>
