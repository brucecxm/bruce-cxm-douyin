<template>

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
            ],
            itemId: null // 用于存储传递的 ID
        };
    },
    methods: {
        handleScroll() {
            this.isSticky = window.scrollY > 0;
        },
        fetchItemDetails(id) {
            // 根据 ID 请求商品详情数据的逻辑
        }
    },

</script>

<style scoped></style>