<template>
    <div class="order-page">
        <div class="order-header">
            <div class="back-button" @click="goBack">
                <i class="icon-back"></i>
                <i class="fas fa-arrow-left"></i>
            </div>
            <div class="order-title">全部订单</div>
            <div class="manage-auth">授权管理</div>
        </div>
        <div class="order-tabs">
            <div class="tab" :class="{ active: currentTab === 'all' }" @click="currentTab = 'all'">全部</div>
            <div class="tab" :class="{ active: currentTab === 'unpaid' }" @click="currentTab = 'unpaid'">待支付</div>
            <div class="tab" :class="{ active: currentTab === 'unshipped' }" @click="currentTab = 'unshipped'">待发货</div>
            <div class="tab" :class="{ active: currentTab === 'shipped' }" @click="currentTab = 'shipped'">待收货</div>
            <div class="tab" :class="{ active: currentTab === 'reviewed' }" @click="currentTab = 'reviewed'">待评价</div>
        </div>
        <div class="order-list">
            <div class="order-item" v-for="order in filteredOrders" :key="order.id">
                <div class="order-status">{{ order.status }}</div>
                <div class="order-info">
                    <img :src="order.image" alt="Product Image" class="product-image">
                    <div class="product-details">
                        <div class="product-name">{{ order.name }}</div>
                        <div class="order-time">{{ order.time }}</div>
                        <div class="return-policy">7天无理由退货</div>
                    </div>
                    <div class="price-info">
                        <div class="price">{{ order.price }}</div>
                        <div class="quantity">x{{ order.quantity }}</div>
                    </div>
                </div>
                <div class="order-actions">
                    <div class="total-price">合计: {{ order.totalPrice }}</div>
                    <button class="action-button" @click="applyAfterSales(order.id)">申请售后</button>
                    <button class="action-button" @click="viewLogistics(order.id)">查看物流</button>
                    <button class="action-button confirm-button" @click="confirmReceipt(order.id)">确认收货</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            currentTab: 'shipped', // 默认选中“待收货”
            orders: [
                { id: 1, status: '已发货', image: '../../assets/op.jpg', name: '物流状态 已签收', time: '2021-05-30 18:06:37', price: '¥ 299.00', quantity: 1, totalPrice: '¥ 299.00' },
                { id: 2, status: '已发货', image: '../../assets/op.jpg', name: '物流状态 已签收', time: '2021-05-30 18:06:33', price: '¥ 199.00', quantity: 1, totalPrice: '¥ 199.00' },
                // ... more orders
            ],
        };
    },
    computed: {
        filteredOrders() {
            if (this.currentTab === 'all') return this.orders;
            if (this.currentTab === 'shipped') return this.orders.filter(order => order.status === '已发货');
            return [];
        }
    },
    methods: {
        goBack() {
            this.$router.go(-1);
        },
        applyAfterSales(orderId) {
            console.log('Apply after sales for order:', orderId);
        },
        viewLogistics(orderId) {
            console.log('View logistics for order:', orderId);
        },
        confirmReceipt(orderId) {
            console.log('Confirm receipt for order:', orderId);
        }
    }
};
</script>

<style scoped>
/* 页面整体布局 */
.order-page {
    font-family: 'Arial', sans-serif;
    background-color: #f5f5f5;
    height: 100vh;
    display: flex;
    flex-direction: column;
}

/* 头部 */
.order-header {
    background-color: #fff;
    display: flex;
    justify-content: space-between;
    padding: 12px 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    align-items: center;
}

.order-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
}

.manage-auth {
    font-size: 14px;
    color: #ff6a00;
    cursor: pointer;
}

/* Tabs 样式 */
.order-tabs {
    display: flex;
    border-bottom: 1px solid #ddd;
    padding: 12px 20px;
    background-color: #fff;
}

.tab {
    flex: 1;
    text-align: center;
    padding: 12px 0;
    font-size: 16px;
    color: #555;
    cursor: pointer;
    transition: color 0.3s, background-color 0.3s;
    border-radius: 8px;
}

.tab.active {
    color: #fff;
    background-color: #ff6a00;
    font-weight: bold;
}

/* 订单列表 */
.order-list {
    padding: 20px;
    background-color: #fff;
    flex-grow: 1;
    overflow-y: auto;
    border-radius: 12px;
    box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
}

.order-item {
    display: flex;
    flex-direction: column;
    margin-bottom: 15px;
    padding: 20px;
    background-color: #fefefe;
    border-radius: 12px;
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);
    transition: transform 0.2s ease-in-out;
}

.order-item:hover {
    transform: translateY(-5px);
}

.order-status {
    font-size: 14px;
    color: #999;
    margin-bottom: 10px;
}

.order-info {
    display: flex;
    margin-top: 15px;
    padding: 10px 0;
    border-bottom: 1px solid #f5f5f5;
}

.product-image {
    width: 70px;
    height: 70px;
    object-fit: cover;
    border-radius: 8px;
}

.product-details {
    flex: 1;
    margin-left: 15px;
}

.product-name {
    font-size: 16px;
    font-weight: bold;
    color: #333;
}

.order-time {
    font-size: 12px;
    color: #777;
    margin-top: 5px;
}

.return-policy {
    font-size: 12px;
    color: #ff6347;
}

.price-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-left: 10px;
    margin-top: 10px;
}

.price {
    font-size: 16px;
    color: #ff6a00;
    font-weight: bold;
}

.quantity {
    font-size: 14px;
    color: #777;
}

/* 订单操作区 */
.order-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 15px;
}

.total-price {
    font-size: 16px;
    font-weight: bold;
    color: #333;
}

.action-button {
    background-color: #eee;
    color: #333;
    border: none;
    padding: 10px 16px;
    border-radius: 8px;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.3s;
    width: 120px;
    text-align: center;
}

.action-button:hover {
    background-color: #ddd;
}

.confirm-button {
    background-color: #ff6a00;
    color: white;
}

.confirm-button:hover {
    background-color: #e65c00;
}
</style>
