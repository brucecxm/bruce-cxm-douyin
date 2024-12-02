<template>
    <div class="page-container">
        <div class="header">
            <div class="rating">评分: 7.73</div>
            <div class="stats">
                <span>点赞 2</span>
                <span>评论 4</span>
            </div>
        </div>
        <div class="recommendations">
            <div class="recommendation" v-for="item in recommendations" :key="item.id">
                <img :src="item.image" alt="">
                <div>{{ item.name }}</div>
                <div>{{ item.price }}</div>
            </div>
        </div>
        <div class="reviews">
            <div class="review" v-for="review in reviews" :key="review.id">
                <img :src="review.avatar" alt="">
                <div>{{ review.name }}</div>
                <div>{{ review.content }}</div>
            </div>
        </div>
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
                // 更多推荐商品...
            ],
            reviews: [
                { id: 1, avatar: '../../assets/op.jpg', name: 'Reviewer 1', content: 'Great product!' },
                { id: 2, avatar: '../../assets/op.jpg', name: 'Reviewer 2', content: 'Loved it!' },
                // 更多用户评论...
            ],
            itemId: null
        };
    },
    methods: {
        handleScroll() {
            this.isSticky = window.scrollY > 0;
        },
        fetchItemDetails(id) {
            // 根据 ID 请求商品详情数据的逻辑
        }
    }
}
</script>

<style scoped>
.page-container {
    width: 100%;
    height: 100%;
    overflow: auto;
}

.header {
    background-color: #007BFF;
    color: white;
    padding: 10px;
}

.rating,
.stats {
    margin: 5px;
}

.recommendation,
.review {
    border: 1px solid #ccc;
    margin: 10px;
    padding: 10px;
}

img {
    width: 50px;
    height: 50px;
}
</style>