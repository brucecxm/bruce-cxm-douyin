<template>
    <div class="product-page">
        <!-- Header -->
        <header :class="{ 'sticky': isSticky }">
            <div class="icons">
                <div class="icon arrow" @click="goBack"></div>
                <div class="icon heart"></div>
                <div class="icon share"></div>
            </div>
        </header>

        <!-- Carousel -->
        <div class="carousel">
            <img src="../../assets/op.jpg" alt="Product Image 1">
        </div>

        <!-- Product Info -->
        <div class="product-info">
            <div class="price">
                <span class="original-price">¥28</span>
                <span class="discounted-price">¥26</span>
            </div>
            <div class="product-name">
                时尚复古碎花连衣裙
            </div>
            <div class="additional-info">
                <span>包邮</span>
                <span>48小时内发货</span>
                <span>退货保障</span>
            </div>
        </div>

        <!-- Shop Info -->
        <div class="shop-info">
            <img src="../../assets/op.jpg" alt="Shop Avatar" class="touxiang">
            <div class="shop-details">
                <span class="shop-name">皖巷坊</span>
                <span class="shop-description">粉丝数：4.0万</span>
                <button>进入店铺</button>
            </div>
        </div>

        <!-- Shop Recommendations -->
        <div class="recommendations">
            <h3>店铺推荐</h3>
            <div class="recommendation-item" v-for="item in recommendations" :key="item.id">
                <img src="../../assets/op.jpg" :alt="item.name" class="recommendation-img">
                <div class="item-info">
                    <span class="item-price">{{ item.price }}</span>
                    <span class="item-name">{{ item.name }}</span>
                </div>
            </div>
        </div>

        <!-- Reviews -->
        <div class="reviews">
            <h3>商品评价</h3>
            <div class="review" v-for="review in reviews" :key="review.id">
                <div class="reviewer-info">
                    <img src="../../assets/op.jpg" alt="Reviewer Avatar" class="reviewer-avatar">
                    <span>{{ review.name }}</span>
                </div>
                <div class="review-content">
                    {{ review.content }}
                </div>
            </div>
        </div>

        <!-- Product Details -->
        <div class="product-details">
            <p>------商品详情页--------</p>
            <img src="../../assets/op.jpg" alt="Detail Image 1" class="detail-img">
            <img src="../../assets/op.jpg" alt="Detail Image 2" class="detail-img">
        </div>

        <!-- Bottom Box -->
        <div class="buy-box" v-if="showBuyBox">

            <p @click="showBuyBox = false">{{ ">" }}</p>
        </div>

        <!-- Footer -->
        <footer>
            <div class="footer-icons">
                <i class="fas fa-home"></i>
                <i class="fas fa-shopping-cart"></i>
                <i class="fas fa-user"></i>
            </div>
            <div class="footer-buttons">
                <button class="add-cart">加入购物车</button>
                <button class="buy-now" @click="showBuyBox = true">立即购买</button>
            </div>
        </footer>
    </div>
</template>
<script>
import shopinfodetail from '@/components/shop/shopinfodetail.vue';
export default {
    comments:{

    },
    data() {
        return {
            isSticky: false,
            recommendations: [
                { id: 1, price: '¥47.83', name: 'Recommendation 1' },
                { id: 2, price: '¥49.99', name: 'Recommendation 2' },
            ],
            reviews: [
                { id: 1, name: 'Reviewer 1', content: 'Great product!' },
                { id: 2, name: 'Reviewer 2', content: 'Loved it!' },
            ],
            showBuyBox: false, // 控制盒子的显示与隐藏
            itemId: null,
        };
    },
    methods: {
        goBack() {
            this.$router.go(-1); // 返回上一页
        },
        handleScroll() {
            this.isSticky = window.scrollY > 0;
        },
        fetchItemDetails(id) {
            // 根据 ID 请求商品详情数据的逻辑
        },
    },
    mounted() {
        window.addEventListener('scroll', this.handleScroll);
    },
    beforeDestroy() {
        window.removeEventListener('scroll', this.handleScroll);
    },
    created() {
        this.itemId = this.$route.params.id;
        this.fetchItemDetails(this.itemId);
    },
};
</script>

<style scoped>
/* Reset some default styles for full screen */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

/* 使页面高度达到100% */
html,
body,
.product-page {
    height: 100%;
    width: 100%;
}

/* Global Styles */
body {
    font-family: 'PingFang SC', sans-serif;
    background-color: #f5f5f5;
    overflow-x: hidden;
    /* 禁止水平滚动条 */
}

/* Product Page */
.product-page {
    display: flex;
    flex-direction: column;
    height: 100%;
}

/* Header */
header {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    background-color: transparent;
    transition: background-color 0.3s ease;
    z-index: 10;
}

header.sticky {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background-color: #fff;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
    color: #333;
    /* 确保图标的颜色 */
    display: flex;
    justify-content: space-between;
    padding: 15px 20px;
}

.header-content i {
    font-size: 20px;
    color: #333;
}

/* Carousel */
.carousel img {
    width: 100%;
    height: 300px;
    /* 定高 */
    object-fit: cover;
    border-radius: 10px;
}

/* Product Info */
.product-info {
    padding: 15px;
}

.price {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.original-price {
    text-decoration: line-through;
    color: #888;
    margin-right: 10px;
}

.discounted-price {
    color: #e64a19;
    font-size: 18px;
    font-weight: bold;
}

.product-name {
    font-size: 16px;
    margin-bottom: 10px;
}

.additional-info {
    font-size: 14px;
    color: #999;
}

/* Bottom Box */
.buy-box {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: #fff;
    height: 80%;
    z-index: 20;
    box-shadow: 0 -4px 8px rgba(0, 0, 0, 0.1);
    transform: translateY(100%);
    animation: slideUp 0.5s forwards;
    /* 平滑从下到上的动画 */
}

/* 动画效果 */
@keyframes slideUp {
    0% {
        transform: translateY(100%);
    }

    100% {
        transform: translateY(0);
    }
}

.additional-info span {
    margin-right: 15px;
}

/* Shop Info */
.shop-info {
    display: flex;
    align-items: center;
    padding: 15px;
    background-color: #fff;
    border-radius: 10px;
    margin: 10px 0;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.touxiang {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    margin-right: 10px;
}

.shop-details {
    flex: 1;
}

.shop-name {
    font-size: 16px;
    font-weight: bold;
}

.shop-description {
    font-size: 14px;
    color: #888;
}

button {
    padding: 8px 16px;
    background-color: #ff6a00;
    color: #fff;
    border: none;
    border-radius: 20px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

button:hover {
    background-color: #e65c00;
}

/* Recommendations */
.recommendations {
    padding: 15px;
    background-color: #fff;
    border-radius: 10px;
    margin-top: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.recommendation-item {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}

.recommendation-img {
    width: 80px;
    height: 80px;
    border-radius: 8px;
    object-fit: cover;
}

.item-info {
    margin-left: 15px;
}

.item-price {
    color: #e64a19;
    font-size: 16px;
}

.item-name {
    font-size: 14px;
    color: #555;
}

/* Reviews */
.reviews {
    padding: 15px;
}

.review {
    margin-bottom: 15px;
    background-color: #fff;
    border-radius: 10px;
    padding: 15px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.reviewer-info {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.reviewer-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 10px;
}

.review-content {
    font-size: 14px;
    color: #555;
}

/* Product Details */
.product-details {
    padding: 15px;
}

.detail-img {
    width: 100%;
    border-radius: 10px;
    margin-top: 10px;
}

/* Footer */
footer {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: #fff;
    display: flex;
    justify-content: space-between;
    padding: 15px 20px;
    box-shadow: 0 -4px 8px rgba(0, 0, 0, 0.1);
}

.footer-icons {
    display: flex;
    align-items: center;
}

.fas {
    font-size: 28px;
    margin-left: 20px;
    color: #333;
}

.footer-buttons {
    display: flex;
    align-items: center;
}

.add-cart,
.buy-now {
    padding: 10px 20px;
    background-color: #ff6a00;
    color: white;
    border-radius: 30px;
    font-size: 16px;
    margin-left: 10px;
    transition: background-color 0.3s;
}

.add-cart:hover,
.buy-now:hover {
    background-color: #e65c00;
}





.header-content {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;
    padding: 10px;
    font-family: Arial, sans-serif;
}

.icons {
    display: flex;
    gap: 20px;
    justify-content: space-between;
}

.icon {
    width: 30px;
    height: 30px;
    position: relative;
    background-color: transparent;
    display: flex;
    justify-content: center;
    align-items: center;
}

/* Arrow Icon */
.icon.arrow::before {
    content: '<';
    font-size: 30px;
    font-weight: bold;
    color: #333;
}

/* Heart Icon */
.icon.heart::before {
    content: '\2665';
    /* Unicode for heart */
    font-size: 30px;
    color: #ff6a00;
}

/* Share Icon */
.icon.share::before {
    content: '\2194';
    /* Unicode for left-right arrow (share icon) */
    font-size: 30px;
    color: #4caf50;
}
</style>