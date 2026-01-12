<template>
  <div style="width: 100%; height: 100%">
    <div class="coupon-header" style="background-color: #fff8e1; padding: 10px">
      <ul
        style="
          list-style-type: disc;
          padding-left: 20px;
          margin: 0;
          font-size: 0.5rem;
        "
      >
        <li>每种优惠券每月只能领取一次。</li>
        <li>优惠券领取后有效期1天,请在有需要时领取。</li>
      </ul>
    </div>
    <div v-for="coupon in coupons" :key="coupon.id" class="coupon-item">
      <div class="coupon-icon">
        <span style="font-size: 1rem; color: #f50057">¥</span>
      </div>
      <div class="coupon-details">
        <span class="coupon-amount" style="font-size: 0.5rem; color: #f50057">{{
          coupon.amount
        }}</span>
        <span class="coupon-description" style="font-size: 0.5rem">{{
          coupon.description
        }}</span>
      </div>
      <button class="coupon-button">立即领取</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      coupons: [
        { id: 1, amount: 50, description: '满699减50品类券' },
        { id: 2, amount: 25, description: '满399减25品类券' },
        { id: 3, amount: 15, description: '满299减15全场券' },
        { id: 3, amount: 15, description: '满299减15全场券' },
        { id: 3, amount: 15, description: '满299减15全场券' },
        { id: 3, amount: 15, description: '满299减15全场券' },
        { id: 3, amount: 15, description: '满299减15全场券' },
        { id: 3, amount: 15, description: '满299减15全场券' },
        { id: 3, amount: 15, description: '满299减15全场券' },
        { id: 4, amount: 10, description: '满199减10全场券' },
        { id: 5, amount: 5, description: '满99减5全场券' }
      ]
    };
  }
};
</script>

<style scoped>
.coupon-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #eee;
  width: calc(100% - 30px);
  /* Account for padding */
  margin: 0 15px;
  /* Add margin for spacing */
}

.coupon-icon {
  width: 50px;
  height: 50px;
  border: 2px solid #f50057;
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
}

.coupon-details {
  flex-grow: 1;
}

.coupon-amount {
  font-weight: bold;
  margin-right: 5px;
}

.coupon-button {
  background-color: #f50057;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
}

.coupon-header {
  border-bottom: 1px solid #eee;
}
</style>
