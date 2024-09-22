<template>
    <div class="menu">
        <ul>
            <li>
                <i class="fas fa-shopping-cart"></i>
                我的订单
            </li>
            <li>
                <i class="fas fa-wallet"></i>
                我的钱包
                <span class="balance">零钱6.5元</span>
            </li>
            <li>
                <i class="fas fa-qrcode"></i>
                我的二维码
            </li>
            <li>
                <i class="fas fa-history"></i>
                查看历史
            </li>
            <li>
                <i class="fas fa-globe"></i>
                离线模式
            </li>
            <li>
                <i class="fas fa-clock"></i>
                稍后再看
            </li>
            <li>
                <i class="fas fa-user-circle"></i>
                抖音创作者中心
            </li>
            <li>
                <i class="fas fa-puzzle-piece"></i>
                小程序
            </li>
            <li>
                <i class="fas fa-hand-holding-heart"></i>
                抖音公益
            </li>
            <li>
                <i class="fas fa-child"></i>
                青少年模式
            </li>
            <li>
                <i class="fas fa-headset"></i>
                我的客服
            </li>
            <li>
                <i class="fas fa-cog"></i>
                设置
            </li>
            <li>
                <i class="fas fa-info-circle"></i>
                个人信息收集清单
            </li>
            <li>
                <i class="fas fa-share-alt"></i>
                第三方信息共享清单
            </li>
        </ul>
        <div class="more-functions">
            更多功能
        </div>
    </div>
</template>
<script>
export default {
    name: 'MenuComponent',
    data() {
        return {

        };
    },
    methods: {
        fanhui() {
            // 要传一个参数给home主界面
            eventBus.$emit('memenu', this.tanchuang); // 发送事件和数据
        },

    }
};

</script>
<style scoped>
.menu {
    width: 320px;
    margin: 0 auto;
    background-color: #fff;
    height: 896px;
}

.menu ul {
    list-style-type: none;
    padding: 0;
    margin: 0;
}

.menu li {
    display: flex;
    align-items: center;
    padding: 10px 20px;
    border-bottom: 1px solid #e0e0e0;
    font-size: 16px;
}

.menu li i {
    margin-right: 10px;
    font-size: 18px;
}

.balance {
    margin-left: auto;
    background-color: #f0f0f0;
    padding: 2px 5px;
    border-radius: 10px;
}

.more-functions {
    text-align: center;
    padding: 10px;
    font-size: 16px;
    color: #007bff;
    cursor: pointer;
}
</style>
  