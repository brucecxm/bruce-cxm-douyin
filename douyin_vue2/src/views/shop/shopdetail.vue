<template>
    <div class="product-page">
        <!-- Header -->
        <header :class="{ 'sticky': isSticky }">
            <div class="header-content">
                <i class="fas fa-arrow-left"></i>
                <i class="fas fa-heart"></i>
                <i class="fas fa-share-alt"></i>
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
                时尚复古碎花连衣裙...
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
                <img src="../../assets/op.jpg" :alt="item.name" style="width: 100px;height: 100px;">
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
                    <img src="../../assets/op.jpg" alt="Reviewer Avatar" style="width: 100px;height: 100px;">
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
            <img src="../../assets/op.jpg" alt="Detail Image 1">
            <img src="../../assets/op.jpg" alt="Detail Image 2">
        </div>

        <!-- Footer -->
        <footer>
            <div class="footer-icons">
                <i class="fas fa-home"></i>
                <i class="fas fa-shopping-cart"></i>
                <i class="fas fa-user"></i>
            </div>
            <div class="footer-buttons">
                <button>加入购物车</button>
                <button>立即购买</button>
            </div>
        </footer>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            isSticky: false,
            recommendations: [
                { id: 1, image: '../../assets/op.jpg', price: '¥47.83', name: 'Recommendation 1' },
                { id: 2, image: '../../assets/op.jpg', price: '¥49.99', name: 'Recommendation 2' },
                // more recommendation items...
            ],
            reviews: [
                { id: 1, avatar: '../../assets/op.jpg', name: 'Reviewer 1', content: 'Great product!' },
                { id: 2, avatar: '../../assets/op.jpg', name: 'Reviewer 2', content: 'Loved it!' },
                // more reviews...
            ]
        };
    },
    methods: {
        handleScroll() {
            this.isSticky = window.scrollY > 0;
        }
    },
    mounted() {
        window.addEventListener('scroll', this.handleScroll);
    },
    beforeDestroy() {
        window.removeEventListener('scroll', this.handleScroll);
    }
}
</script>
  
<style scoped>
.touxiang {
    width: 100px;
    height: 100px;
    border-radius: 50px;
}

.product-page {
    width: 414px;
}

/* Header */
header {
    position: absolute;
    top: 0;
    width: 100%;
    background-color: transparent;
    transition: background-color 0.3s;
}

header.sticky {
    position: fixed;
    background-color: white;
}

.header-content {
    display: flex;
    justify-content: space-between;
    padding: 10px;
}

/* Carousel */
.carousel {
    width: 100%;
}

.carousel img {
    width: 100%;
}

/* Product Info */
.product-info {
    padding: 10px;
}

.price {
    display: flex;
    align-items: baseline;
}

.original-price {
    text-decoration: line-through;
    margin-right: 10px;
}

.discounted-price {
    color: red;
}

.additional-info {
    margin-top: 10px;
}

/* Shop Info */
.shop-info {
    display: flex;
    align-items: center;
    padding: 10px;
}

.shop-details {
    margin-left: 10px;
}

/* Recommendations */
.recommendations {
    padding: 10px;
}

.recommendation-item {
    display: flex;
    align-items: center;
    margin-top: 10px;
}

.item-info {
    margin-left: 10px;
}

/* Reviews */
.reviews {
    padding: 10px;
}

.review {
    margin-top: 10px;
}

.reviewer-info {
    display: flex;
    align-items: center;
}

.review-content {
    margin-top: 5px;
}

/* Product Details */
.product-details {
    padding: 10px;
}

.product-details img {
    width: 100%;
    margin-top: 10px;
}

/* Footer */
footer {
    position: fixed;
    bottom: 0;
    width: 100%;
    background-color: white;
    display: flex;
    justify-content: space-between;
    padding: 10px;
}

.footer-icons {
    display: flex;
    align-items: center;
}

.fas {
    font-size: 30px;
    margin-left: 20px;
}

.footer-buttons button {
    margin-left: 10px;
    width: 100px;
    height: 50px;
    margin-right: 10px;
}
</style>
  