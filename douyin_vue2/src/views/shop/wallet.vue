<template>
    <div class="wallet-container">

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
                <input type="number" v-model="rechargeAmount" placeholder="输入充值金额" />
                <button @click="recharge">确认充值</button>
                <button @click="closeDialog">取消</button>
            </div>
        </div>

        <!-- Withdraw Dialog -->
        <div v-if="showWithdraw" class="dialog">
            <div class="dialog-content">
                <h3>提现</h3>
                <input type="number" v-model="withdrawAmount" placeholder="输入提现金额" />
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
</template>

<script>
export default {
    data() {
        return {
            balance: 500, // Initial balance
            showRecharge: false,
            showWithdraw: false,
            rechargeAmount: 0,
            withdrawAmount: 0,
            expenditures: [
                { id: 1, date: '2025-01-20', description: '购买商品 A', amount: 100 },
                { id: 2, date: '2025-01-18', description: '购买商品 B', amount: 50 },
                // More expenditure records...
            ],
        };
    },
    mounted() {

    },
    methods: {
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
        },

        // Handle recharge functionality
        recharge() {
            if (this.rechargeAmount > 0) {
                this.balance += this.rechargeAmount;
                this.rechargeAmount = 0;
                this.closeDialog();
            }
        },

        // Handle withdraw functionality
        withdraw() {
            if (this.withdrawAmount > 0 && this.withdrawAmount <= this.balance) {
                this.balance -= this.withdrawAmount;
                this.withdrawAmount = 0;
                this.closeDialog();
            } else {
                alert('提现金额不足或无效');
            }
        },
    }
};
</script>

<style scoped>
.wallet-container {
    width: 100%;
    height: 100%;
    overflow: auto;
    font-family: Arial, sans-serif;
}

.header {
    background-color: #007BFF;
    color: white;
    padding: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.balance {
    font-size: 18px;
    font-weight: bold;
}

.actions button {
    margin-left: 10px;
    padding: 5px 10px;
    background-color: #28a745;
    color: white;
    border: none;
    cursor: pointer;
}

.actions button:nth-child(2) {
    background-color: #dc3545;
}

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
}

.dialog-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    text-align: center;
}

.dialog input {
    padding: 8px;
    margin: 10px 0;
    width: 200px;
}

.dialog button {
    margin-top: 10px;
    padding: 5px 10px;
    background-color: #007BFF;
    color: white;
    border: none;
    cursor: pointer;
}

.dialog button:nth-child(2) {
    background-color: #dc3545;
}

.expenditure {
    margin-top: 20px;
}

.expenditure .item {
    margin-bottom: 10px;
    padding: 10px;
    border: 1px solid #ccc;
    display: flex;
    justify-content: space-between;
}
</style>
