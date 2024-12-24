<template>
    <div class="recharge-page">
        <div class="recharge-header">
            <div class="back-button" @click="goBack">
                <i class="icon-back"></i>
                <i class="fas fa-arrow-left"></i>
            </div>
            <div class="recharge-title">充值中心</div>
            <div class="help-center">帮助中心</div>
        </div>

        <div class="recharge-amount">
            <div class="amount-title">请选择充值金额</div>
            <div class="amount-options">
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

        <div class="payment-method">
            <div class="payment-title">选择支付方式</div>
            <div class="payment-options">
                <div class="payment-option" :class="{ active: selectedPayment === 'alipay' }"
                    @click="selectPayment('alipay')">
                    <img src="../../assets/alipay-logo.png" alt="Alipay" class="payment-logo" />
                    <span class="payment-name">支付宝</span>
                </div>
                <div class="payment-option" :class="{ active: selectedPayment === 'wechat' }"
                    @click="selectPayment('wechat')">
                    <img src="../../assets/wechat-logo.png" alt="WeChat" class="payment-logo" />
                    <span class="payment-name">微信支付</span>
                </div>
                <div class="payment-option" :class="{ active: selectedPayment === 'bank' }"
                    @click="selectPayment('bank')">
                    <img src="../../assets/bank-logo.png" alt="Bank" class="payment-logo" />
                    <span class="payment-name">银行卡</span>
                </div>
            </div>
        </div>

        <div class="recharge-button">
            <button @click="confirmRecharge">确认充值</button>
        </div>

        <div class="recharge-records">
            <div class="record-title">充值记录</div>
            <div v-for="record in rechargeRecords" :key="record.id" class="recharge-record">
                <div class="record-time">{{ record.time }}</div>
                <div class="record-amount">{{ record.amount }}</div>
                <div class="record-status">{{ record.status }}</div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            selectedAmount: 100, // 默认选择100元
            selectedPayment: 'alipay', // 默认选择支付宝
            rechargeRecords: [
                { id: 1, time: '2021-10-15 14:30', amount: '¥100', status: '成功' },
                { id: 2, time: '2021-10-14 10:20', amount: '¥200', status: '成功' },
                // 更多记录...
            ],
        };
    },
    methods: {
        goBack() {
            this.$router.go(-1); // 返回上一页
        },
        selectAmount(amount) {
            this.selectedAmount = amount; // 选择充值金额
        },
        selectPayment(paymentMethod) {
            this.selectedPayment = paymentMethod; // 选择支付方式
        },
        confirmRecharge() {
            // 处理确认充值逻辑
            console.log(`已选择充值金额：¥${this.selectedAmount}，支付方式：${this.selectedPayment}`);
        },
    },
};
</script>

<style scoped>
.recharge-page {
    font-family: 'Arial', sans-serif;
    background-color: #f5f5f5;
    height: 100vh;
    display: flex;
    flex-direction: column;
}

.recharge-header {
    background-color: #fff;
    display: flex;
    justify-content: space-between;
    padding: 12px 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    align-items: center;
}

.recharge-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
}

.help-center {
    font-size: 14px;
    color: #ff6a00;
    cursor: pointer;
}

.recharge-amount {
    padding: 20px;
    background-color: #fff;
    margin-top: 10px;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.amount-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
}

.amount-options {
    display: flex;
    justify-content: space-around;
}

.amount-option {
    font-size: 16px;
    color: #333;
    padding: 10px 20px;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.amount-option.active {
    background-color: #ff6a00;
    color: white;
}

.payment-method {
    padding: 20px;
    background-color: #fff;
    margin-top: 10px;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.payment-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
}

.payment-options {
    display: flex;
    justify-content: space-around;
}

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

.payment-option.active {
    background-color: #ff6a00;
    color: white;
}

.payment-logo {
    width: 30px;
    height: 30px;
    margin-right: 10px;
}

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

.recharge-button button:hover {
    background-color: #e65c00;
}

.recharge-records {
    margin-top: 20px;
    background-color: #fff;
    padding: 15px;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.record-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 15px;
}

.recharge-record {
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    color: #555;
    margin-bottom: 10px;
}

.recharge-record .record-time {
    color: #999;
}

.recharge-record .record-status {
    color: #28a745;
    font-weight: bold;
}
</style>