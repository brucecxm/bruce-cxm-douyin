<template>
  <div class="search-page">
    <header class="header">
      <i class="fas fa-chevron-left" @click="gohome"></i>
      <input
        type="text"
        placeholder="搜索"
        v-model="searchQuery"
        @input="handleInput"
        @keyup.enter="executeSearch"
        ref="searchInput"
      />
      <i class="fas fa-search" @click="executeSearch"></i>
    </header>
    <main>
      <!-- 搜索建议 -->
      <div v-if="showSuggestions && suggestions.length > 0" class="suggestions">
        <p>你是不是想搜:</p>
        <ul class="suggestion-list">
          <li
            v-for="(suggestion, index) in suggestions"
            :key="index"
            @click="selectSuggestion(suggestion)"
          >
            <i class="fas fa-search"></i>
            {{ suggestion }}
          </li>
        </ul>
      </div>

      <!-- 热门搜索和搜索历史 -->
      <div v-else-if="!searchQuery" class="hot-searches">
        <!-- 搜索历史 -->
        <div class="history-section" v-if="searchHistory.length">
          <div class="section-header">
            <h3>搜索历史</h3>
            <span @click="clearSearchHistory">清除</span>
          </div>
          <ul class="history-list">
            <li
              v-for="(item, index) in searchHistory"
              :key="index"
              @click="selectSuggestion(item)"
            >
              <i class="fas fa-history"></i>
              {{ item }}
            </li>
          </ul>
        </div>
        <!-- 热门搜索 -->
        <div class="hot-section">
          <div class="hot-header">
            <h3>热门搜索</h3>
            <span @click="refreshHotSearches">换一换</span>
          </div>
          <ul class="hot-list">
            <li
              v-for="(item, index) in hotSearches"
              :key="index"
              @click="selectSuggestion(item.keyword)"
            >
              <span class="rank">{{ index + 1 }}</span>
              <span class="keyword">{{ item.keyword }}</span>
              <span v-if="item.trending" class="trending">{{
                item.trending
              }}</span>
            </li>
          </ul>
        </div>
      </div>

      <!-- 搜索结果 -->
      <div
        v-else-if="searchQuery && searchResults.length > 0"
        class="search-results"
      >
        <h3>搜索结果</h3>
        <ul class="result-list">
          <li
            v-for="(result, index) in searchResults"
            :key="result.videoId || index"
            @click="goToVideoDetail(result)"
          >
            <div class="result-item">
              <div class="result-cover" v-if="result.cover">
                <img :src="result.cover" :alt="result.title" />
              </div>
              <div class="result-info">
                <p class="result-title">{{ result.title || result.content }}</p>
                <div class="result-stats">
                  <span>{{ result.likeNum || 0 }}赞</span>
                  <span>{{ result.commentNum || 0 }}评论</span>
                  <span>{{ result.shareNum || 0 }}分享</span>
                </div>
                <div class="result-user">
                  <span class="username">{{
                    result.username || '未知用户'
                  }}</span>
                </div>
              </div>
            </div>
          </li>
        </ul>
        <!-- 加载更多 -->
        <div v-if="hasMore" class="load-more" @click="loadMore">
          <span>加载更多</span>
        </div>
      </div>

      <!-- 无结果提示 -->
      <div
        v-else-if="searchQuery && searchResults.length === 0"
        class="no-results"
      >
        <p>暂无搜索结果</p>
      </div>
    </main>
  </div>
</template>

<script>
import {
  getSearchSuggestions,
  getHotSearches,
  searchVideos
} from '@/api/video.js';

export default {
  data() {
    return {
      searchQuery: '',
      suggestions: [],
      showSuggestions: false,
      hotSearches: [],
      searchResults: [],
      currentPage: 1,
      pageSize: 10,
      hasMore: true,
      loading: false,
      searchHistory: []
    };
  },
  mounted() {
    this.getHotSearchesData();
    this.loadSearchHistory();
  },
  methods: {
    gohome() {
      this.$router.push('/');
    },

    // 处理输入事件，获取搜索建议
    async handleInput() {
      if (this.searchQuery.trim().length > 0) {
        try {
          const res = await getSearchSuggestions(this.searchQuery);
          this.suggestions = res.data || [];
          this.showSuggestions = true;
        } catch (error) {
          console.error('获取搜索建议失败:', error);
          this.suggestions = [];
        }
      } else {
        this.suggestions = [];
        this.showSuggestions = false;
      }
    },

    // 执行搜索
    async executeSearch() {
      if (this.searchQuery.trim().length === 0) return;

      this.showSuggestions = false;
      this.currentPage = 1;
      this.searchResults = [];
      this.hasMore = true;

      // 保存搜索历史
      this.saveSearchHistory(this.searchQuery);

      await this.loadSearchResults();
    },

    // 加载搜索历史
    loadSearchHistory() {
      const history = localStorage.getItem('searchHistory');
      if (history) {
        this.searchHistory = JSON.parse(history);
      }
    },

    // 保存搜索历史
    saveSearchHistory(keyword) {
      // 去重
      const index = this.searchHistory.indexOf(keyword);
      if (index > -1) {
        this.searchHistory.splice(index, 1);
      }
      // 限制最多保存10条
      if (this.searchHistory.length >= 10) {
        this.searchHistory.pop();
      }
      // 添加到最前面
      this.searchHistory.unshift(keyword);
      // 保存到localStorage
      localStorage.setItem('searchHistory', JSON.stringify(this.searchHistory));
    },

    // 清除搜索历史
    clearSearchHistory() {
      this.searchHistory = [];
      localStorage.removeItem('searchHistory');
    },

    // 加载搜索结果
    async loadSearchResults() {
      if (this.loading) return;

      this.loading = true;
      try {
        const res = await searchVideos(
          this.searchQuery,
          this.currentPage,
          this.pageSize
        );
        const data = res.data || res;

        if (this.currentPage === 1) {
          this.searchResults = data;
        } else {
          this.searchResults = [...this.searchResults, ...data];
        }

        // 判断是否还有更多数据
        this.hasMore = data.length >= this.pageSize;
        this.currentPage++;
      } catch (error) {
        console.error('搜索失败:', error);
      } finally {
        this.loading = false;
      }
    },

    // 加载更多
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.loadSearchResults();
      }
    },

    // 选择搜索建议
    selectSuggestion(keyword) {
      this.searchQuery = keyword;
      this.showSuggestions = false;
      this.executeSearch();
    },

    // 获取热门搜索
    async getHotSearchesData() {
      try {
        const res = await getHotSearches();
        this.hotSearches = res.data || [];
      } catch (error) {
        console.error('获取热门搜索失败:', error);
        // 失败时使用默认数据
        this.hotSearches = [
          { keyword: '热门视频', trending: '↑ 123' },
          { keyword: '最新音乐', trending: '↑ 45' },
          { keyword: '搞笑段子', trending: '↓ 67' },
          { keyword: '美食教程', trending: '↑ 89' },
          { keyword: '旅游攻略', trending: '↑ 32' }
        ];
      }
    },

    // 刷新热门搜索
    refreshHotSearches() {
      this.getHotSearchesData();
    },

    // 跳转到视频详情
    goToVideoDetail(result) {
      if (result.videoId) {
        this.$router.push(`/video/${result.videoId}`);
      } else {
        console.error('缺少视频ID');
      }
    }
  }
};
</script>

<style scoped>
.search-page {
  width: 414px;
  min-height: 896px;
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
  font-size: 0.8rem;
  color: #333;
  cursor: pointer;
}

.header input {
  flex-grow: 1;
  margin: 0 10px;
  padding: 8px 12px;
  border: 1px solid #ccc;
  border-radius: 20px;
  font-size: 0.8rem;
  background-color: #f5f5f5;
  outline: none;
}

.header input:focus {
  background-color: #fff;
  border-color: #007bff;
}

/* 搜索建议样式 */
.suggestions {
  padding: 10px;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
  font-size: 0.8rem;
}

.suggestions p {
  margin: 0 0 10px 0;
  font-weight: bold;
  color: #666;
}

.suggestion-list {
  padding: 0;
  margin: 0;
  list-style-type: none;
}

.suggestion-list li {
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: background-color 0.2s;
}

.suggestion-list li:hover {
  background-color: #f5f5f5;
}

.suggestion-list li i {
  margin-right: 10px;
  color: #999;
  font-size: 0.7rem;
}

/* 热门搜索样式 */
.hot-searches {
  background-color: #fff;
  padding: 10px;
}

/* 搜索历史样式 */
.history-section {
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.section-header h3 {
  margin: 0;
  font-size: 0.9rem;
  font-weight: bold;
}

.section-header span {
  font-size: 0.7rem;
  color: #007bff;
  cursor: pointer;
}

.history-list {
  padding: 0;
  margin: 0;
  list-style-type: none;
}

.history-list li {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.history-list li:last-child {
  border-bottom: none;
}

.history-list li:hover {
  background-color: #f5f5f5;
}

.history-list li i {
  margin-right: 15px;
  color: #999;
  font-size: 0.7rem;
  width: 20px;
  text-align: center;
}

/* 热门搜索样式 */
.hot-section {
  margin-bottom: 20px;
}

.hot-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.hot-header h3 {
  margin: 0;
  font-size: 0.9rem;
  font-weight: bold;
}

.hot-header span {
  font-size: 0.7rem;
  color: #007bff;
  cursor: pointer;
}

.hot-list {
  padding: 0;
  margin: 0;
  list-style-type: none;
}

.hot-list li {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.hot-list li:last-child {
  border-bottom: none;
}

.hot-list li:hover {
  background-color: #f5f5f5;
}

.hot-list .rank {
  font-weight: bold;
  font-size: 1rem;
  margin-right: 15px;
  width: 20px;
  text-align: center;
}

.hot-list li:nth-child(1) .rank,
.hot-list li:nth-child(2) .rank,
.hot-list li:nth-child(3) .rank {
  color: #ff4757;
}

.hot-list .keyword {
  flex-grow: 1;
  font-size: 0.85rem;
}

.hot-list .trending {
  font-size: 0.7rem;
  color: #ff4757;
}

/* 搜索结果样式 */
.search-results {
  background-color: #fff;
  padding: 10px;
}

.search-results h3 {
  margin: 0 0 15px 0;
  font-size: 0.9rem;
  font-weight: bold;
}

.result-list {
  padding: 0;
  margin: 0;
  list-style-type: none;
}

.result-item {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.result-item:hover {
  background-color: #f5f5f5;
}

.result-cover {
  width: 120px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 10px;
  flex-shrink: 0;
}

.result-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.result-info {
  flex-grow: 1;
  min-width: 0;
}

.result-title {
  margin: 0 0 8px 0;
  font-size: 0.85rem;
  line-height: 1.4;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.result-stats {
  display: flex;
  gap: 15px;
  margin-bottom: 5px;
  font-size: 0.7rem;
  color: #999;
}

.result-user {
  font-size: 0.7rem;
  color: #666;
}

.result-user .username {
  font-weight: bold;
}

/* 加载更多样式 */
.load-more {
  text-align: center;
  padding: 15px;
  color: #007bff;
  font-size: 0.8rem;
  cursor: pointer;
  background-color: #f5f5f5;
  margin-top: 10px;
  border-radius: 5px;
}

.load-more:hover {
  background-color: #e9ecef;
}

/* 无结果样式 */
.no-results {
  text-align: center;
  padding: 50px 20px;
  color: #999;
  font-size: 0.9rem;
  background-color: #fff;
}
</style>
