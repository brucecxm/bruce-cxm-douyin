<template>
  <div
    class="grid-display"
    :style="{
      gap: gap + 'px',
      gridTemplateColumns: `repeat(auto-fill, minmax(${itemWidth}px, 1fr))`,
      gridAutoRows: itemHeight + 'px'
    }"
  >
    <div v-for="(item, index) in items" :key="index" class="grid-item">
      <img :src="item.icon" alt="" class="icon" />
      <div class="icon-name">{{ item.name }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GridDisplay',
  props: {
    items: {
      type: Array,
      required: true
    },
    itemWidth: {
      type: Number,
      required: true
    },
    itemHeight: {
      type: Number,
      required: true
    },
    gap: {
      type: Number,
      default: 0
    }
  }
};
</script>

<style scoped>
.grid-display {
  display: grid;
  width: 100%;
  box-sizing: border-box;
  overflow-x: hidden; /* 防止横向滚动 */
}

.grid-item {
  display: flex;
  flex-direction: column; /* 上下排列 */
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  padding: 8px 0;
}

.icon {
  width: 40px;
  height: 40px;
  object-fit: contain;
  margin-bottom: 6px;
}

.icon-name {
  font-size: 14px;
  color: #333;
}
</style>
