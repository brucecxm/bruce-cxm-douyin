<template>
  <div class="all">
    <header>
      <slot name="header"></slot>
    </header>
    <div
      class="infinite-list-wrapper"
      v-infinite-scroll="loadMore"
      :infinite-scroll-disabled="loading || noMoreData"
      :infinite-scroll-distance="10"
    >
      <ul
        class="infinite-list"
        :style="{
          gap: gap + 'px',
          gridTemplateColumns: `repeat(${itemsPerRow}, ${typeof boxWidth === 'number' ? boxWidth + 'px' : boxWidth})`
        }"
      >
        <li
          class="infinite-list-item"
          v-for="(item, index) in list"
          :key="index"
          :style="{
            height:
              typeof boxHeight === 'number' ? boxHeight + 'px' : boxHeight,
            lineHeight:
              typeof boxHeight === 'number' ? boxHeight + 'px' : boxHeight
          }"
        >
          <!-- 作用域插槽，传递item和index给父组件 -->
          <slot name="item" :item="item" :index="index">
            {{ item }}
          </slot>
        </li>
      </ul>
      <div class="loading-text" v-if="loading">加载中...</div>
      <div class="loading-text" v-if="noMoreData">没有更多数据了</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    itemInfo: {
      // 这里你传入的数据
      type: Array,
      required: true
    },
    itemsPerRow: {
      type: Number,
      default: 3
    },
    boxWidth: {
      type: [Number, String],
      default: 100
    },
    boxHeight: {
      type: [Number, String],
      default: 100
    },
    gap: {
      type: Number,
      default: 10
    },
    loading: {
      type: Boolean,
      default: false
    },
    noMoreData: {
      type: Boolean,
      default: false
    },
    append: {
      type: Boolean,
      default: true // 默认累加
    }
  },
  data() {
    return {
      list: []
    };
  },
  watch: {
    itemInfo: {
      immediate: true,
      handler(newVal) {
        // 根据 append 决定是否累加
        if (this.append) {
          this.list = [...this.list, ...newVal];
        } else {
          this.list = [...newVal];
        }
      }
    }
  },
  methods: {
    loadMore() {
      if (this.loading || this.noMoreData) return;
      // 通知父组件加载更多
      this.$emit('loadMore');
    }
  }
};
</script>

<style scoped>
/* 样式保持不变 */
.infinite-list-wrapper {
  height: 100vh;
  overflow: auto;
  border: 1px solid #ccc;
}

.infinite-list {
  display: grid;
  padding: 10px;
  list-style: none;
  margin: 0;
}

.infinite-list-item {
  background-color: #f0f0f0;
  border-radius: 6px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #333;
}

.loading-text {
  text-align: center;
  padding: 10px;
  color: #888;
}
</style>
