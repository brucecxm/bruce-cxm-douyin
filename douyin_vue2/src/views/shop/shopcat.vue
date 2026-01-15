<template>
  <div class="cart-page">
    <h2 class="title">购物车</h2>
    <div v-if="cartItems.length === 0" class="empty-cart">
      购物车空空如也，快去逛逛吧~
    </div>
    <div v-else>
      <div v-for="item in cartItems" :key="item.id" class="cart-item">
        <img class="item-img" :src="item.image" alt="商品图" />
        <div class="item-info">
          <div class="item-title">{{ item.title }}</div>
          <div class="item-price">¥{{ item.price.toFixed(2) }}</div>
          <div class="item-qty">
            <button @click="decreaseQty(item)">-</button>
            <input
              type="text"
              v-model.number="item.quantity"
              @input="onQtyInput(item)"
            />
            <button @click="increaseQty(item)">+</button>
          </div>
        </div>
        <button class="btn-delete" @click="removeItem(item.id)">×</button>
      </div>

      <div class="cart-footer">
        <div class="total">
          合计: <span class="total-price">¥{{ totalPrice.toFixed(2) }}</span>
        </div>
        <button
          class="btn-checkout"
          :disabled="cartItems.length === 0"
          @click="checkout"
        >
          去结算
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js';

export default {
  data() {
    return {
      cartItems: []
    };
  },
  computed: {
    totalPrice() {
      return this.cartItems.reduce(
        (sum, item) => sum + item.amount * item.number,
        0
      );
    }
  },
  created() {
    this.loadCartItems();
  },
  methods: {
    // 加载购物车列表
    async loadCartItems() {
      try {
        const res = await request.get('/shop/shoppingCart/list');
        if (res.code === 200 && res.data) {
          // 处理购物车数据，确保字段名匹配
          this.cartItems = res.data.map((item) => ({
            id: item.id,
            image: item.image || 'https://img.yzcdn.cn/vant/apple-1.jpg',
            title: item.name || '商品名称',
            price: item.amount || 0, // 为了保持原有计算逻辑
            amount: item.amount || 0, // 实际后端字段
            quantity: item.number || 1, // 为了保持原有计算逻辑
            number: item.number || 1, // 实际后端字段
            dishId: item.dishId,
            dishFlavor: item.dishFlavor || ''
          }));
        }
      } catch (error) {
        console.error('加载购物车列表失败:', error);
        this.$toast('加载购物车失败，请重试');
      }
    },

    // 增加商品数量
    async increaseQty(item) {
      try {
        item.number++;
        item.quantity = item.number; // 同步更新显示字段
        await this.updateCartItem(item);
      } catch (error) {
        console.error('增加数量失败:', error);
        item.number--;
        item.quantity = item.number;
        this.$toast('操作失败，请重试');
      }
    },

    // 减少商品数量
    async decreaseQty(item) {
      if (item.number <= 1) return;

      try {
        item.number--;
        item.quantity = item.number; // 同步更新显示字段
        await this.updateCartItem(item);
      } catch (error) {
        console.error('减少数量失败:', error);
        item.number++;
        item.quantity = item.number;
        this.$toast('操作失败，请重试');
      }
    },

    // 手动输入数量
    async onQtyInput(item) {
      if (item.quantity < 1 || isNaN(item.quantity)) {
        item.quantity = 1;
      }

      // 转换为整数
      item.quantity = parseInt(item.quantity);

      try {
        item.number = item.quantity;
        await this.updateCartItem(item);
      } catch (error) {
        console.error('更新数量失败:', error);
        // 恢复原来的数量
        item.quantity = item.number;
        this.$toast('操作失败，请重试');
      }
    },

    // 更新购物车商品
    async updateCartItem(item) {
      try {
        const updateData = {
          id: item.id,
          number: item.number
        };

        const res = await request.post(
          '/shop/shoppingCart/updateNumber',
          updateData
        );

        if (res.code !== 200) {
          throw new Error('更新失败');
        }
      } catch (error) {
        console.error('更新购物车商品失败:', error);
        throw error;
      }
    },

    // 删除购物车商品
    async removeItem(id) {
      try {
        // 显示确认对话框
        if (!(await this.$confirm('确定要从购物车中删除该商品吗？'))) {
          return;
        }

        const res = await request.delete(`/shop/shoppingCart/delete/${id}`);

        if (res.code === 200) {
          this.cartItems = this.cartItems.filter((item) => item.id !== id);
          this.$toast(res.msg || '删除成功');
        } else {
          throw new Error(res.msg || '删除失败');
        }
      } catch (error) {
        console.error('删除购物车商品失败:', error);
        this.$toast(error.message || '删除失败，请重试');
      }
    },

    // 去结算
    checkout() {
      if (this.cartItems.length === 0) {
        this.$toast('购物车为空，请先添加商品');
        return;
      }

      // 构建订单数据
      const orderData = {
        cartItems: this.cartItems,
        totalPrice: this.totalPrice
      };

      // 将订单数据存储到localStorage，方便订单页面获取
      localStorage.setItem('orderData', JSON.stringify(orderData));

      // 跳转到订单页面，确保路径与路由配置一致
      this.$router.push('/shop/order');
    }
  }
};
</script>

<style scoped>
.cart-page {
  max-width: 480px;
  margin: 0 auto;
  padding: 10px 15px;
  background: #fff;
  font-family: 'PingFang SC', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  color: #333;
  min-height: 100vh;
}

.title {
  text-align: center;
  font-weight: 700;
  font-size: 22px;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
  color: #fe2c55;
}

.empty-cart {
  margin-top: 80px;
  text-align: center;
  color: #999;
  font-size: 16px;
}

.cart-item {
  display: flex;
  align-items: center;
  margin: 15px 0;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
  position: relative;
}

.item-img {
  width: 100px;
  height: 100px;
  border-radius: 12px;
  object-fit: cover;
  box-shadow: 0 4px 12px rgb(254 44 85 / 0.1);
  flex-shrink: 0;
}

.item-info {
  flex: 1;
  margin-left: 15px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100px;
}

.item-title {
  font-size: 16px;
  font-weight: 600;
  color: #222;
  line-height: 1.3;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-price {
  font-size: 18px;
  font-weight: 700;
  color: #fe2c55;
}

.item-qty {
  margin-top: 6px;
  display: flex;
  align-items: center;
}

.item-qty button {
  width: 28px;
  height: 28px;
  border: none;
  background-color: #fe2c55;
  color: white;
  font-weight: 700;
  font-size: 20px;
  border-radius: 50%;
  cursor: pointer;
  user-select: none;
  line-height: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.3s ease;
}

.item-qty button:hover {
  background-color: #d81e3f;
}

.item-qty input {
  width: 40px;
  text-align: center;
  margin: 0 8px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
  outline: none;
  padding: 4px 0;
  -moz-appearance: textfield;
}

.item-qty input::-webkit-inner-spin-button,
.item-qty input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* 删除按钮 */
.btn-delete {
  position: absolute;
  right: 0;
  top: 10px;
  border: none;
  background: none;
  font-size: 26px;
  color: #ccc;
  cursor: pointer;
  transition: color 0.3s ease;
  user-select: none;
}

.btn-delete:hover {
  color: #fe2c55;
}

/* 结算栏 */
.cart-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  max-width: 480px;
  background: white;
  padding: 12px 20px;
  box-shadow: 0 -2px 12px rgb(254 44 85 / 0.2);
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-sizing: border-box;
  z-index: 1000;
}

.total {
  font-size: 18px;
  font-weight: 700;
  color: #333;
}

.total-price {
  color: #fe2c55;
  margin-left: 6px;
}

.btn-checkout {
  background-color: #fe2c55;
  color: white;
  border: none;
  padding: 10px 40px;
  font-size: 16px;
  font-weight: 700;
  border-radius: 30px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  user-select: none;
}

.btn-checkout:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.btn-checkout:hover:not(:disabled) {
  background-color: #d81e3f;
}
</style>
