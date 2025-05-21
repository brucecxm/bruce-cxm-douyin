<template>
  <div class="search-page">
    <header class="header">
      <i class="fas fa-chevron-left" @click="gohome"></i>
      <input type="text" placeholder="人体捐献要求给钱" v-model="searchQuery" />
      <i class="fas fa-search" @click="gosearchdetail"></i>
    </header>
    <main>
      <div class="suggestions">
        <p>你是不是想搜:</p>
        <ul class="suggestion-list">
          <li v-for="(suggestion, index) in suggestions" :key="index">
            {{ suggestion }}
          </li>
        </ul>
      </div>

      <nav class="tabs">
        <span class="active" @click="setTab(0)">抖音热榜</span>
        <span @click="setTab(1)">音视频榜</span>
        <span @click="setTab(2)">百科榜</span>
        <span @click="setTab(3)">问答榜</span>
        <span @click="setTab(4)">博物馆榜</span>
        <span @click="setTab(5)">影视榜</span>
      </nav>

      <transition name="fade" mode="out-in">
        <ul class="hot-list" :key="currentTab">
          <li v-for="(item, index) in currentHotList" :key="index">
            <span>{{ index + 1 }}</span>
            <p>{{ item.title }}</p>
            <i v-if="item.new" class="fas fa-bolt"></i>
            <span class="views">{{ item.views }}万</span>
          </li>
        </ul>
      </transition>
    </main>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchQuery: '',
      suggestions: [
        '安慰捐一次精补偿多少钱',
        '黑料不打烊黑',
        '磁吸共振滑率高吗',
        '底色梦纹',
        '单曲猫咪',
        '植物大战僵尸教学'
      ],
      hotLists: [
        [
          // 抖音热榜
          { title: '中共中央政治局第十五次集体学习', views: 1130.5, new: true },
          { title: '武汉暴雨江水上涨', views: 1109.6, new: true },
          { title: '美国罕见批评“茶党”伙伴印度', views: 1110.7, new: true }
          // More items...
        ],
        [
          // 音视频榜
          { title: '音视频内容一', views: 1050, new: false },
          { title: '音视频内容二', views: 1250, new: false }
          // More items...
        ],
        [
          // 百科榜
          { title: '百科内容一', views: 900, new: true },
          { title: '百科内容二', views: 1000, new: false }
          // More items...
        ]
        // More categories...
      ],
      currentTab: 0
    };
  },
  computed: {
    currentHotList() {
      return this.hotLists[this.currentTab];
    }
  },
  methods: {
    gohome() {
      this.$router.push('/');
    },
    gosearchdetail() {
      this.$router.push('/searchdetail');
    },
    setTab(tabIndex) {
      this.currentTab = tabIndex;
    }
  }
};
</script>

<style scoped>
.suggestions {
  padding: 10px;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
}

.suggestions p {
  margin: 0;
  font-weight: bold;
}

.suggestion-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  padding: 0;
  list-style-type: none;
}

.suggestion-list li {
  padding: 5px 0;
}

.search-page {
  width: 414px;
  height: 896px;
  margin: 0 auto;
  background-color: #f8f8f8;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
}

.header i {
  font-size: 0.5rem;
}

.header input {
  flex-grow: 1;
  margin: 0 10px;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 0.5rem;
}

.suggestions {
  padding: 10px;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
  font-size: 0.5rem;
}

.suggestions p {
  margin: 0;
  font-weight: bold;
}

.tabs {
  display: flex;
  overflow-x: auto;
  padding: 10px 0;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
}

.tabs span {
  padding: 10px 20px;
  cursor: pointer;
  font-size: 0.5rem;
  white-space: nowrap;
}

.tabs .active {
  border-bottom: 2px solid #007bff;
}

.tabs::-webkit-scrollbar {
  display: none;
}

.hot-list {
  padding: 10px;
  list-style-type: none;
  background-color: #fff;
  transition: opacity 0.3s ease;
}

.hot-list li {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.hot-list li span {
  font-weight: bold;
  font-size: 0.5rem;
}

.hot-list li p {
  flex-grow: 1;
  margin: 0 10px;
  font-size: 0.5rem;
}

.hot-list li .fas {
  color: #ff0000;
}

.hot-list li .views {
  color: #999;
  font-size: 0.5rem;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
