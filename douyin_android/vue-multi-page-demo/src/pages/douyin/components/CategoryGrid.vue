<template>
  <div class="section">
    <h3>常用分类</h3>
    <div class="category-grid">
      <div
        class="category-item"
        v-for="(item, index) in categories"
        :key="index"
      >
        <img :src="item.img" alt="" />
        <p>{{ item.name }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    categories: Array
  }
};
</script>

<style scoped>
.category-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
.category-item {
  width: 70px;
  text-align: center;
}
.category-item img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 4px;
}
</style>
