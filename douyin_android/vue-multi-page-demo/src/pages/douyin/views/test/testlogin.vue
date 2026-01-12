<template>
  <div class="shop_top_search">
    <div class="search">
      <div class="search_img"></div>
      <div class="cammera"></div>
      <div class="search_button"></div>
    </div>
    <div class="shop_cart"></div>
  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  data() {
    return {};
  },
  methods: {}
};
</script>

<style scoped>
.shop_top_search {
  width: 100%;
  height: 1.5rem;
  background-color: blue;
}
.shop_top_search {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.search {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: blue;
}
.shop_cart {
  width: 1rem;
  height: 1rem;
  background-color: red;
}
</style>
