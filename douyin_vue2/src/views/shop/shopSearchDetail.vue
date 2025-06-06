<template>
  <div class="search-page">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <input type="text" v-model="keyword" placeholder="卡特莲感光粉膏" />
      <button @click="search">搜索</button>
    </div>

    <HistoryList
      :history="historyList"
      @clear="historyList = []"
      @remove-item="removeItem"
    />

    <GuessList
      :guesses="guessList"
      :columns="2"
      :itemStyle="{ backgroundColor: '#f0f0f0', color: '#333' }"
      :listStyle="{ gap: '10px' }"
    />

    <AdBanner @go="goAd" />

    <div class="fenlei">
      <p style="font-size: 0.5rem">分类</p>
      <GridDisplay
:itemWidth="80"
:itemHeight="100" :items="items"
:gap="5" />
    </div>
  </div>
</template>

<script>
import GridDisplay from '@/components/GridDisplay.vue';
import HistoryList from '@/components/HistoryList.vue';
import GuessList from '@/components/GuessList.vue';
import AdBanner from '@/components/AdBanner.vue';

export default {
  components: {
    HistoryList,
    GuessList,
    GridDisplay,
    AdBanner
  },
  data() {
    return {
      keyword: '',
      historyList: ['啊', '啊啊啊', '啊啊啊啊', '啊啊啊啊啊', '啊啊啊啊啊啊'],
      guessList: [
        '卡特莲感光粉膏',
        '制冰模具 🔥',
        '一整天不脱妆的粉底膏',
        '老头刀',
        '笔记本电脑支架 🔥',
        '粮农里黑',
        '女生必备化妆品 🔥',
        '三星s20ultra壳',
        '新手化妆用品 🔥',
        '花千碗粉饼',
        '折叠午休躺椅',
        '下眼睫毛笔'
      ],
      items: [
        {
          icon: '//m11.360buyimg.com/babel/s135x168_jfs/t1/279467/9/27186/7197/680b3d21F5e7b0177/825fe2ad693faa42.png.webp',
          name: '煤气灶'
        }
        // 其他重复项略
      ]
    };
  },
  methods: {
    removeItem(index) {
      this.historyList.splice(index, 1);
    },
    search() {
      console.log('搜索关键词：', this.keyword);
    },
    goAd() {
      alert('跳转广告页或外链...');
    }
  }
};
</script>

<style scoped>
.search-page {
  padding: 16px;
  font-family: Arial, sans-serif;
  background: #f8f8f8;
}
.search-bar {
  display: flex;
  margin-bottom: 12px;
}
.search-bar input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px 0 0 4px;
}
.search-bar button {
  padding: 8px 12px;
  background: pink;
  border: none;
  border-radius: 0 4px 4px 0;
}
</style>
