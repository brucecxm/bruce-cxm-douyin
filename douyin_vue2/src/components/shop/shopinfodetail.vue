<template>
    <div class="header">
        <!-- 地址选择栏 -->
        <div class="address">
            <span>选择地址:</span>
            <select v-model="selectedAddress">
                <option v-for="(address, index) in addressList" :key="index" :value="address">
                    {{ address }}
                </option>
            </select>
        </div>

        <!-- 商品信息 -->
        <div class="product-info">
            <div class="product-image">
                <img :src="product.image" alt="Product Image" />
            </div>
            <div class="product-details">
                <h2>{{ product.name }}</h2>
                <p>{{ product.description }}</p>
                <p class="price">价格: ¥{{ product.price }}</p>
            </div>
        </div>

        <!-- 商品分类选择 -->
        <div class="product-options">
            <div class="option">
                <span>选择颜色:</span>
                <select v-model="selectedColor">
                    <option v-for="(color, index) in product.colors" :key="index" :value="color">
                        {{ color }}
                    </option>
                </select>
            </div>
            <div class="option">
                <span>选择尺寸:</span>
                <select v-model="selectedSize">
                    <option v-for="(size, index) in product.sizes" :key="index" :value="size">
                        {{ size }}
                    </option>
                </select>
            </div>
        </div>

        <!-- 添加到购物车按钮 -->
        <button @click="addToCart">加入购物车</button>
    </div>
</template>

<script>
export default {
    data() {
        return {
            selectedAddress: '', // 当前选中的地址
            addressList: ['北京', '上海', '广州', '深圳'], // 地址列表
            selectedColor: '', // 当前选中的颜色
            selectedSize: '', // 当前选中的尺寸
            product: {
                name: '苹果手机',
                description: '最新款苹果手机，超高性能与完美设计。',
                price: 6999,
                image: 'http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960', // 商品图片路径
                colors: ['黑色', '白色', '蓝色', '红色'],
                sizes: ['64GB', '128GB', '256GB'],
            }, // 商品信息
        };
    },
    methods: {
        addToCart() {
            // 实现添加到购物车的操作
            console.log('商品已加入购物车:', this.product.name, this.selectedColor, this.selectedSize, this.selectedAddress);
        },
    },
};
</script>

<style scoped>
.header {
    height: 80%;
    overflow: hidden;
}

/* 地址选择栏样式 */
.address {
    margin: 1.5vh 0;
    padding: 0.8vh 1.5vw;
    background-color: #f2f2f2;
    border-radius: 8px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.address select {
    margin-left: 12px;
    padding: 6px 10px;
    border-radius: 5px;
    border: 1px solid #ddd;
    background-color: #fff;
    font-size: 1rem;
    transition: border 0.3s;
}

.address select:hover,
.address select:focus {
    border-color: #007bff;
}

/* 商品信息展示样式 */
.product-info {
    display: flex;
    margin-top: 3vh;
    border-bottom: 1px solid #ddd;
    padding-bottom: 12px;
    justify-content: space-between;
}

.product-image img {
    width: 35vw;
    height: auto;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.product-details {
    margin-left: 20px;
    flex: 1;
}

.product-details h2 {
    font-size: 1.7rem;
    color: #333;
    margin-bottom: 8px;
}

.product-details p {
    font-size: 1.1rem;
    color: #555;
    line-height: 1.6;
}

.price {
    font-size: 1.3rem;
    color: #e74c3c;
    font-weight: bold;
}

/* 商品分类选择样式 */
.product-options {
    margin-top: 2vh;
}

.option {
    margin-bottom: 1.5vh;
    display: flex;
    align-items: center;
}

.option span {
    font-size: 1.2rem;
    margin-right: 12px;
    width: 100px;
    color: #333;
}

.option select {
    padding: 6px 10px;
    border-radius: 5px;
    border: 1px solid #ddd;
    font-size: 1rem;
    transition: border 0.3s;
}

.option select:hover,
.option select:focus {
    border-color: #007bff;
}

/* 按钮样式 */
button {
    margin-top: 3vh;
    padding: 12px 25px;
    background-color: #007bff;
    color: white;
    font-size: 1.1rem;
    font-weight: bold;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    width: 100%;
    transition: background-color 0.3s ease-in-out;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

button:hover {
    background-color: #0056b3;
}

button:focus {
    outline: none;
}
</style>
