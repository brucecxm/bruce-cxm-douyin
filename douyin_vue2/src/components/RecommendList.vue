<!-- RecommendList.vue -->
<template>
  <div class="recommend-list">
    <div class="masonry" :style="masonryStyle">
      <div class="item" v-for="(item, index) in items" :key="index">
        <slot :item="item" />
      </div>
    </div>

    <div class="loading" v-if="loading">加载中...</div>
    <div class="no-more" v-if="noMore">没有更多了</div>
  </div>
</template>

<script>
export default {
  name: 'RecommendList',
  props: {
    columns: {
      type: Number,
      default: 2
    },
    items: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    },
    noMore: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    masonryStyle() {
      return {
        columnCount: this.columns,
        columnGap: '10px'
      };
    }
  }
};
</script>
