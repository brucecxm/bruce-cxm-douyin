<template>
  <div class="section">
    <p style="font-size: 0.5rem">猜你想搜</p>
    <div class="guess-list" :style="computedListStyle">
      <span
        style="text-align: left"
        v-for="(item, index) in guesses"
        :key="index"
        class="guess-item"
        :style="itemStyle"
      >
        {{ item }}
      </span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    guesses: {
      type: Array,
      default: () => []
    },
    columns: {
      type: Number,
      default: 3
    },
    itemStyle: {
      type: Object,
      default: () => ({})
    },
    listStyle: {
      type: Object,
      default: () => ({})
    }
  },
  computed: {
    computedListStyle() {
      return {
        display: 'grid',
        gridTemplateColumns: `repeat(${this.columns}, 1fr)`,
        gap: '6px',
        ...this.listStyle
      };
    }
  }
};
</script>

<style scoped>
.guess-item {
  background: #fff;
  padding: 6px 10px;
  border-radius: 12px;
  font-size: 14px;
  border: 1px solid #eee;
  text-align: center;
}
</style>
