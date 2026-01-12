<template>
  <div class="search-page">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <div class="input-wrapper">
        <svg
          class="icon-search"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="1234"
          width="18"
          height="18"
        >
          <path
            d="M426.666667 128a298.666667 298.666667 0 1 1 0 597.333333 298.666667 298.666667 0 0 1 0-597.333333z m0 85.333333a213.333333 213.333333 0 1 0 0 426.666667 213.333333 213.333333 0 0 0 0-426.666667zM682.666667 682.666667l192 192-60.373333 60.373333-192-192z"
            fill="#999"
          />
        </svg>
        <input
          type="text"
          v-model="keyword"
          placeholder="卡特莲感光粉膏"
          @keyup.enter="search"
        />
      </div>

      <button class="btn-search" @click="search">搜索</button>
      <button v-if="showSearchResult" class="btn-clear" @click="clearSearch">
        清除搜索
      </button>
    </div>

    <div class="searchitem" v-if="!showSearchResult">
      <!-- 历史记录 -->
      <HistoryList
        :history="historyList"
        @clear="historyList = []"
        @remove-item="removeItem"
      />

      <!-- 猜你喜欢 -->
      <GuessList
        :guesses="guessList"
        :columns="2"
        :itemStyle="{
          backgroundColor: '#fff',
          color: '#111',
          borderRadius: '20px',
          padding: '6px 14px',
          fontSize: '14px',
          boxShadow: '0 1px 4px rgba(0,0,0,0.1)',
          cursor: 'pointer',
          userSelect: 'none',
          transition: 'background-color 0.3s'
        }"
        :listStyle="{ gap: '12px', padding: '12px 0' }"
      />

      <!-- 广告 -->
      <AdBanner @go="goAd" />

      <!-- 分类 -->
      <div class="fenlei">
        <p class="section-title">分类</p>
        <GridDisplay
          :itemWidth="80"
          :itemHeight="100"
          :items="items"
          :gap="10"
          :itemStyle="{
            backgroundColor: '#fff',
            borderRadius: '12px',
            boxShadow: '0 2px 8px rgba(0,0,0,0.05)',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: '13px',
            color: '#222',
            cursor: 'pointer',
            userSelect: 'none',
            transition: 'transform 0.2s ease'
          }"
        />
      </div>
    </div>

    <!-- 搜索结果区域 -->
    <div v-if="showSearchResult" class="search-result" style="margin-top: 20px">
      <h3 class="result-title">搜索结果（关键词：{{ keyword }}）</h3>
      <RecommendList
        :columns="2"
        :items="searchResults"
        :loading="loading"
        :noMore="noMore"
        :itemStyle="{
          height: '320px',
          backgroundColor: '#fff',
          borderRadius: '16px',
          padding: '12px',
          boxShadow: '0 4px 12px rgba(0,0,0,0.06)'
        }"
      >
        <template v-slot:default="{ item }">
          <div class="recommend-item">
            <img
              :src="item.image"
              class="cover"
              :alt="item.title"
              :style="{
                width: '100%',
                height: '260px',
                objectFit: 'cover',
                borderRadius: '12px',
                boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
              }"
            />
            <div
              class="title"
              :style="{
                fontSize: '15px',
                marginTop: '8px',
                textAlign: 'center',
                fontWeight: '600',
                color: '#222',
                whiteSpace: 'nowrap',
                textOverflow: 'ellipsis',
                overflow: 'hidden'
              }"
              :title="item.title"
            >
              {{ item.title }}
            </div>
          </div>
        </template>
      </RecommendList>
    </div>
  </div>
</template>

<script>
// 组件引用保持不变
import GridDisplay from '@/components/GridDisplay.vue';
import HistoryList from '@/components/HistoryList.vue';
import GuessList from '@/components/GuessList.vue';
import AdBanner from '@/components/AdBanner.vue';
import RecommendList from '../../components/RecommendList.vue';

export default {
  components: {
    HistoryList,
    GuessList,
    GridDisplay,
    AdBanner,
    RecommendList
  },
  data() {
    return {
      keyword: '',
      showSearchResult: false,
      loading: false,
      noMore: false,
      searchResults: [
        {
          image: 'https://picsum.photos/seed/1/300/200',
          title: '美白面膜套装'
        },
        {
          image: 'https://picsum.photos/seed/1/300/200',
          title: '美白面膜套装'
        },
        {
          image: 'https://picsum.photos/seed/1/300/200',
          title: '美白面膜套装'
        }
        // ...其他数据
      ],
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
        // ...其他分类
      ]
    };
  },
  methods: {
    removeItem(index) {
      this.historyList.splice(index, 1);
    },
    search() {
      if (!this.keyword.trim()) {
        alert('请输入搜索关键词');
        return;
      }
      this.loading = true;
      this.showSearchResult = true;

      setTimeout(() => {
        // 模拟搜索结果，过滤示例
        this.searchResults = this.searchResults.filter((item) =>
          item.title.includes(this.keyword)
        );
        this.loading = false;
        this.noMore = true;
      }, 1000);
    },
    clearSearch() {
      this.keyword = '';
      this.showSearchResult = false;
      this.searchResults = [];
    },
    goAd() {
      alert('跳转广告页或外链...');
    }
  }
};
</script>

<style scoped>
.search-page {
  padding: 16px 20px 40px;
  font-family: 'PingFang SC', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  background-color: #fff;
  color: #222;
  min-height: 100vh;
}

/* 搜索栏 */
.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  background-color: #fff;
  z-index: 10;
}

.input-wrapper {
  flex: 1;
  position: relative;
  display: flex;
  align-items: center;
}

.input-wrapper input {
  width: 100%;
  padding: 10px 40px 10px 36px;
  font-size: 16px;
  border: 1.5px solid #eee;
  border-radius: 30px;
  outline: none;
  transition: border-color 0.3s;
}

.input-wrapper input:focus {
  border-color: #fe2c55;
  box-shadow: 0 0 8px rgba(254, 44, 85, 0.3);
}

.icon-search {
  position: absolute;
  left: 12px;
  fill: #bbb;
  pointer-events: none;
}

.btn-search,
.btn-clear {
  background-color: #fe2c55;
  border: none;
  color: #fff;
  font-weight: 600;
  font-size: 15px;
  padding: 10px 16px;
  border-radius: 22px;
  cursor: pointer;
  box-shadow: 0 4px 8px rgb(254 44 85 / 0.3);
  transition:
    background-color 0.3s ease,
    box-shadow 0.3s ease;
  user-select: none;
}

.btn-search:hover,
.btn-clear:hover {
  background-color: #ff466e;
  box-shadow: 0 6px 12px rgb(255 70 110 / 0.5);
}

/* 搜索item区域 */
.searchitem {
  margin-top: 20px;
}

/* 分类标题 */
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #666;
  margin-bottom: 12px;
}

/* 推荐结果 */
.search-result {
  margin-top: 20px;
}

.result-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 14px;
  color: #333;
}

.recommend-item {
  cursor: pointer;
  transition:
    transform 0.25s ease,
    box-shadow 0.25s ease;
  border-radius: 16px;
}

.recommend-item:hover {
  transform: translateY(-6px);
  box-shadow: 0 8px 20px rgba(254, 44, 85, 0.15);
}

.cover {
  border-radius: 16px;
  display: block;
  user-select: none;
  pointer-events: none;
}
</style>
