<template>
  <div class="main" @scroll="handleScroll">
    <RecommendList
      :columns="2"
      :shopitems="shopitems"
      :loading="loading"
      :noMore="noMore"
    >
      <template v-slot:default="{ item }">
        <div class="recommend-item">
          <img :src="item.image" class="cover" />
          <div class="title">{{ item.title }}</div>
        </div>
      </template>
    </RecommendList>
  </div>
</template>

<script>
export default {
  data() {
    return {
      shopitems: [],
      page: 1,
      pageSize: 10,
      loading: false,
      noMore: false
    };
  },
  mounted() {
    this.loadMore();
  },
  methods: {
    async loadMore() {
      if (this.loading || this.noMore) return;
      this.loading = true;
      try {
        const newData = await this.fetchData(this.page, this.pageSize);
        if (!newData.length) {
          this.noMore = true;
        } else {
          this.shopitems.push(...newData);
          this.page++;
        }
      } finally {
        this.loading = false;
      }
    },
    async fetchData(page, pageSize) {
      // 你的请求函数或模拟数据
      await new Promise((resolve) => setTimeout(resolve, 500));
      const total = 50;
      const start = (page - 1) * pageSize;
      if (start >= total) return [];
      const end = Math.min(start + pageSize, total);
      return Array.from({ length: end - start }).map((_, i) => ({
        title: `推荐内容 #${start + i + 1}`,
        image: `https://picsum.photos/300/200?random=${start + i + 1}`
      }));
    },
    handleScroll(event) {
      const container = event.target;
      if (
        container.scrollHeight <=
        container.scrollTop + container.clientHeight + 100
      ) {
        this.loadMore();
      }
    }
  }
};
</script>
