<template>
  <div class="cart-container">
    <div class="header" style="font-size: 0.7rem">
      <span class="title">购物车</span>
      <span class="edit">编辑</span>
    </div>
    <div class="shipping-info" style="font-size: 0.5rem">
      <span>再购 ¥ 66 可免邮费哦~</span>
      <span class="make-up">凑单 ></span>
    </div>
    <div
      v-for="item in cartItems"
      :key="item.id"
      class="cart-item"
      style="font-size: 0.5rem"
    >
      >
      <div class="select-circle"></div>
      <div class="item-image">
        <img :src="item.image" alt="Product Image" />
      </div>
      <div class="item-details">
        <div class="item-name">{{ item.name }}</div>
        <div class="item-specs">{{ item.specs }}</div>
        <div class="item-price">¥ {{ item.price }}</div>
      </div>
      <div class="quantity-control">
        <button @click="decrementQuantity(item)">-</button>
        <span>{{ item.quantity }}</span>
        <button @click="incrementQuantity(item)">+</button>
      </div>
    </div>

    <div class="footer" style="font-size: 0.8rem">
      <div class="selected-items">
        <div class="select-circle"></div>
        <span>已选 ({{ selectedCount }})</span>
      </div>
      <div class="total-price">合计: ¥ {{ totalPrice }}</div>
      <button class="checkout-button">下单</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      cartItems: [
        {
          id: 1,
          image: '../../assets/op.jpg',
          price: 666,
          name: '商品名称写这里',
          specs: '粉丝, 22码',
          quantity: 1
        },
        {
          id: 2,
          image: '../../assets/op.jpg',
          price: 666,
          name: '商品名称写这里',
          specs: '粉丝, 22码',
          quantity: 1
        },
        {
          id: 3,
          image: '../../assets/op.jpg',
          price: 666,
          name: '商品名称写这里',
          specs: '粉丝, 22码',
          quantity: 1
        },
        {
          id: 4,
          image: '../../assets/op.jpg',
          price: 666,
          name: '商品名称写这里',
          specs: '粉丝, 22码',
          quantity: 1
        }
        // ... more cart items
      ]
    };
  },
  computed: {
    selectedCount() {
      return this.cartItems.length; //  Since all are selected in the image
    },
    totalPrice() {
      return this.cartItems.reduce(
        (total, item) => total + item.price * item.quantity,
        0
      );
    }
  },
  methods: {
    incrementQuantity(item) {
      item.quantity++;
    },
    decrementQuantity(item) {
      if (item.quantity > 1) {
        item.quantity--;
      }
    }
  }
};
</script>

<style scoped>
.cart-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  /* Make content flow vertically */
}

.header {
  display: flex;
  justify-content: space-between;
  /* Align title and edit button */
  padding: 10px;
}

.shipping-info {
  display: flex;
  justify-content: space-between;
  padding: 10px;
}

.cart-item {
  display: flex;
  align-items: center;
  /* Vertically align items */
  padding: 10px;
  border-bottom: 1px solid #eee;
  /* Add a separator between items */
}

.item-image {
  width: 50px;
  height: 50px;
}

.item-image img {
  width: 100%;
  height: 100%;
}

.item-details {
  margin-left: 10px;
  /* Space between image and details */
}

.quantity-control {
  margin-left: auto;
  /* Push quantity controls to the right */
}

.footer {
  position: fixed;
  /* Stick the footer to the bottom */
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: #f0f0f0;
  /* Example background color */
  padding: 10px;
  display: flex;
  justify-content: space-between;
}

.checkout-button {
  background-color: #4885ed;
  /* Example button color */
  color: white;
  padding: 10px 20px;
  border: none;
}

.select-circle {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  border: 1px solid #ccc;
  margin-right: 10px;
}
</style>
