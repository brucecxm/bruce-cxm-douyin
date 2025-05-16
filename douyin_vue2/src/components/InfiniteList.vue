<template>
  <div
  class="infinite-list-wrapper"
  v-infinite-scroll="loadMore"
  :infinite-scroll-disabled="loading || noMoreData"
  :infinite-scroll-distance="10"
>
<header>
    <slot name="header">默认头部</slot>
  </header>
  <ul class="infinite-list">
    <li class="infinite-list-item" v-for="item in list" :key="item">
      {{ item }}
    </li>
  </ul>
  <div class="loading-text" v-if="loading">加载中...</div>
  <div class="loading-text" v-if="noMoreData">没有更多数据了</div>
</div>

</template>

<script>
export default {
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
        newItems.push(`第 ${this.count} 条数据`);
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
        if (container && container.scrollHeight <= container.clientHeight && !this.noMoreData) {
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
list-style: none;
padding: 0;
margin: 0;
}

.infinite-list-item {
padding: 10px;
border-bottom: 1px solid #eee;
}

.loading-text {
text-align: center;
padding: 10px;
color: #888;
}
</style>
