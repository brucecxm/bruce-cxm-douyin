<template>
  <div class="parent-container">
    <!-- 3. 在模板中使用SearchBar组件 -->
    <SearchBar />
    <!-- 其他父组件内容 -->
  </div>
</template>

<script>
// 1. 引入SearchBar组件（注意文件路径要正确，比如SearchBar.vue和ParentView.vue在同一目录下）
import SearchBar from './components/search-bar-shop_top_search.vue';

export default {
  name: 'ParentView',
  // 2. 注册组件
  components: {
    SearchBar
  }
  // 父组件的其他逻辑...
};
</script>

<style scoped>
.parent-container {
  /* 父组件样式 */
}
</style>
