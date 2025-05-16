<template>
  <div class="recommend-list">
    <div class="masonry" :style="masonryStyle">
      <div
        class="item"
        v-for="(item, index) in items"
        :key="index"
      >
        <slot :item="item" />
      </div>
    </div>

    <div class="loading" v-if="loading">加载中...</div>
    <div class="no-more" v-if="noMore">没有更多了</div>
  </div>
</template>

<script>
export default {
  name: "RecommendList",
  props: {
    columns: {
      type: Number,
      default: 2
    },
    loadData: {
      type: Function,
      required: true
    }
  },
  data() {
    return {
      items: [],
      page: 1,
      pageSize: 10,
      loading: false,
      noMore: false
    };
  },
  computed: {
    masonryStyle() {
      return {
        columnCount: this.columns,
        columnGap: '10px'
      };
    }
  },
  mounted() {
    this.loadMore();
    window.addEventListener("scroll", this.handleWindowScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleWindowScroll);
  },
  methods: {
    async loadMore() {
      if (this.loading || this.noMore) return;
      this.loading = true;
      try {
        const newData = await this.loadData(this.page, this.pageSize);
        if (!newData.length) {
          this.noMore = true;
        } else {
          this.items.push(...newData);
          this.page++;
        }
      } finally {
        this.loading = false;
      }
    },
    handleWindowScroll() {
      const bottomReached =
        window.innerHeight + window.scrollY >= document.body.offsetHeight - 100;
      if (bottomReached) {
        this.loadMore();
      }
    }
  }
};
</script>

<style scoped>
.recommend-list {
  padding: 10px;
  box-sizing: border-box;
  width: 100%;
  overflow-x: hidden;
}

.masonry {
  column-count: 2;
  column-gap: 10px;
  width: 100%;
  box-sizing: border-box;
}

.item {
  width: 100%;
  display: block;
  margin-bottom: 10px;
  box-sizing: border-box;
  break-inside: avoid;

  white-space: normal;      /* 允许换行 */
  word-wrap: break-word;    /* 遇到长词强制换行 */
  word-break: break-word;   /* 兼容性好 */
}

</style>
