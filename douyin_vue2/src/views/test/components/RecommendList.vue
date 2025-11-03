<template>
  <div
    class="recommend-list"
    @scroll.passive="handleScroll"
    ref="scrollContainer"
  >
    <div class="item" v-for="(item, index) in list" :key="index">
      <!-- 不同类型内容动态展示 -->
      <div v-if="item.type === 'product'" class="card product">
        <img :src="item.img" class="cover" />
        <div class="info">
          <p class="title">{{ item.title }}</p>
          <p class="price">￥{{ item.price }}</p>
        </div>
      </div>

      <div v-else-if="item.type === 'ad'" class="card ad">
        <img :src="item.img" class="cover" />
      </div>

      <div v-else class="card text">
        <p class="desc">{{ item.text }}</p>
      </div>
    </div>

    <div class="loading" v-if="loading">加载中...</div>
    <div class="nomore" v-if="noMore">没有更多了</div>
  </div>
</template>

<script>
export default {
  name: 'RecommendList',
  props: {
    // 外部传入的请求函数，可根据不同页面内容自定义加载逻辑
    fetchData: {
      type: Function,
      required: true
    }
  },
  data() {
    return {
      list: [],
      page: 1,
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
        const res = await this.fetchData(this.page);
        if (res && res.length > 0) {
          this.list.push(...res);
          this.page++;
        } else {
          this.noMore = true;
        }
      } catch (err) {
        console.error('加载数据失败：', err);
      } finally {
        this.loading = false;
      }
    },

    handleScroll() {
      const container = this.$refs.scrollContainer;
      if (!container) return;

      const threshold = 150; // 离底部多少像素时触发加载
      if (
        container.scrollTop + container.clientHeight >=
        container.scrollHeight - threshold
      ) {
        this.loadMore();
      }
    }
  }
};
</script>

<style scoped>
.recommend-list {
  height: 100vh; /* 或由外层容器控制高度 */
  overflow-y: auto;
  background: #fff;
  padding: 10px;
  box-sizing: border-box;
}
.item {
  margin-bottom: 10px;
}
.card {
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
}
.card.product .cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
.card.product .info {
  padding: 6px 8px;
}
.title {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}
.price {
  color: #e1251b;
  font-weight: bold;
}
.card.ad img {
  width: 100%;
  height: 120px;
  object-fit: cover;
}
.loading,
.nomore {
  text-align: center;
  color: #999;
  padding: 15px 0;
}
</style>
