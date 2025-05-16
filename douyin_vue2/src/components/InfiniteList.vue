<template>
  <div
    class="infinite-list-wrapper"
    v-infinite-scroll="loadMore"
    :infinite-scroll-disabled="loading || noMoreData"
    :infinite-scroll-distance="10"
  >
    <header>
      <slot name="header"></slot>
    </header>
    <ul
      class="infinite-list"
      :style="{
        gap: gap + 'px',
        gridTemplateColumns: `repeat(${itemsPerRow}, ${typeof boxWidth === 'number' ? boxWidth + 'px' : boxWidth})`
      }"
    >
      <li
        class="infinite-list-item"
        v-for="item in list"
        :key="item"
        :style="{
          height: typeof boxHeight === 'number' ? boxHeight + 'px' : boxHeight,
          lineHeight: typeof boxHeight === 'number' ? boxHeight + 'px' : boxHeight
        }"
      >
        {{ item }}
      </li>
    </ul>
    <div class="loading-text" v-if="loading">加载中...</div>
    <div class="loading-text" v-if="noMoreData">没有更多数据了</div>
  </div>
</template>

<script>
export default {
  props: {
    itemInfo: {
      required: true
    },
    itemsPerRow: {
      type: Number,
      default: 3
    },
    boxWidth: {
      type: [Number, String], // 支持数字或字符串（带单位）
      default: 100
    },
    boxHeight: {
      type: [Number, String], // 支持数字或字符串（带单位）
      default: 100
    },
    gap: {
      type: Number,
      default: 10
    }
  },
  data() {
    return {
      list: [],
      count: 0,
      loading: false,
      noMoreData: false
    };
  },
  created() {
    this.autoLoadUntilScrollable();
  },
  methods: {
    loadMore() {
      if (this.loading || this.noMoreData) return;

      this.loading = true;

      setTimeout(() => {
        const newItems = [];
        for (let i = 0; i < 10; i++) {
          this.count++;
          newItems.push(`第 ${this.count} 个盒子`);
        }

        this.list = this.list.concat(newItems);

        if (this.count >= 50) {
          this.noMoreData = true;
        }

        this.loading = false;
      }, 1000);
    },
    autoLoadUntilScrollable() {
      const tryLoad = () => {
        this.$nextTick(() => {
          const container = this.$el.querySelector('.infinite-list-wrapper');
          if (
            container &&
            container.scrollHeight <= container.clientHeight &&
            !this.noMoreData
          ) {
            this.loadMore();
            tryLoad(); // 继续尝试直到撑满容器
          }
        });
      };
      tryLoad();
    }
  }
};
</script>

<style scoped>
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
  /* 文字垂直居中 */
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #333;
  /* 宽度由grid-template-columns控制 */
}

.loading-text {
  text-align: center;
  padding: 10px;
  color: #888;
}
</style>
