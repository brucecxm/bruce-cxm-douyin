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
        return {};
    },
    methods: {
        toggleMenu() {
            this.$el.classList.toggle('open');
        },
    },
};
</script>

<style scoped>
.menu {
    width: 100%;
    max-width: 320px;
    margin: 0 auto;
    background-color: #fff;
    height: 100%;
    position: absolute;
    top: 0;
    right: -100%;
    /* Initially hidden off-screen */
    transition: transform 0.3s ease-in-out;
}

/* Menu Items Styling */
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

/* More Function Styling */
.more-functions {
    text-align: center;
    padding: 10px;
    font-size: 16px;
    color: #007bff;
    cursor: pointer;
}

/* Responsive Design */

/* For Mobile and Tablet (Max-width 768px) */
@media (max-width: 768px) {
    .menu {
        width: 80%;
        /* Take up 80% of screen width on small devices */
        right: -80%;
        /* Initially hidden off-screen */
    }

    .menu.open {
        right: 0;
        /* Slide in when 'open' class is added */
    }

    .menu li {
        font-size: 14px;
        /* Smaller font size for smaller screens */
        padding: 12px;
        /* Add padding for better tap targets */
    }

    .more-functions {
        font-size: 14px;
    }
}

/* For Desktop and Larger Screens (Min-width 769px) */
@media (min-width: 769px) {
    .menu {
        position: relative;
        max-width: 320px;
        margin-left: 0;
        right: 0;
        /* Always visible on larger screens */
    }

    .menu li {
        font-size: 18px;
        /* Larger font size for desktop */
        padding: 15px 30px;
    }

    .more-functions {
        font-size: 18px;
    }
}
</style>
