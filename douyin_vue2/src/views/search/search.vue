<template>
  <div class="search-container">
    <!-- 头部搜索栏 -->
    <header class="search-header">
      <div class="header-left">
        <button class="back-btn" @click="goBack">
          <font-awesome-icon icon="arrow-left" class="icon-back"></font-awesome-icon>
        </button>
      </div>
      <div class="search-input-wrapper">
        <input
          type="text"
          class="search-input"
          placeholder="搜索"
          v-model="searchText"
          @input="handleSearchInput"
        />
        <font-awesome-icon icon="camera" class="icon-camera" @click="openCamera"></font-awesome-icon>
      </div>
      <div class="header-right">
        <div class="search-btn" @click="handleSearch">搜索</div>
      </div>
    </header>

    <!-- 搜索建议/结果区域 -->
    <div class="search-content">
      <!-- 历史记录 -->
      <div class="history-section" v-if="showHistory">
        <div class="history-header">
          <h3>历史记录</h3>
          <button class="clear-btn" @click="clearHistory">清空</button>
        </div>
        <ul class="history-list">
          <li
            v-for="(item, index) in historyList"
            :key="index"
            class="history-item"
            @click="selectHistory(item)"
          >
            <span class="history-text">{{ item }}</span>
            <button class="delete-item" @click.stop="deleteHistory(index)">
              <font-awesome-icon icon="times" class="icon-delete"></font-awesome-icon>
            </button>
          </li>
        </ul>
        <button class="more-history" @click="viewMoreHistory">
          查看更多历史
        </button>
      </div>

      <!-- 猜你想搜 -->
      <div class="guess-section" v-if="showGuess">
        <div class="guess-header">
          <h3>猜你想搜</h3>
          <div class="guess-actions">
            <button class="refresh-btn" @click="refreshGuess">换一换</button>
            <button class="more-btn"><font-awesome-icon icon="ellipsis-v" class="icon-more"></font-awesome-icon></button>
          </div>
        </div>
        <div class="guess-tags">
          <span
            v-for="(tag, index) in guessTags"
            :key="index"
            class="guess-tag"
            @click="searchTag(tag)"
          >
            {{ tag }}
          </span>
        </div>
      </div>

      <!-- 热搜推荐 -->
      <div class="hot-section" v-if="showHot">
        <div class="hot-header">
          <h3>热搜榜</h3>
        </div>
        <div class="hot-list">
          <div
            v-for="(item, index) in hotList"
            :key="index"
            class="hot-item"
            @click="searchTag(item.keyword)"
          >
            <span class="hot-rank">{{ index + 1 }}</span>
            <span class="hot-title">{{ item.keyword }}</span>
            <span class="hot-count">{{ item.count }}</span>
          </div>
        </div>
      </div>

      <!-- 猜你想看 -->
      <div class="recommend-section">
        <div class="recommend-tabs">
          <button
            v-for="tab in recommendTabs"
            :key="tab.key"
            class="tab-btn"
            :class="{ active: activeTab === tab.key }"
            @click="switchTab(tab.key)"
          >
            {{ tab.name }}
          </button>
        </div>

        <div class="video-grid">
          <div
            v-for="(video, index) in videoList"
            :key="index"
            class="video-item"
          >
            <div class="video-cover">
              <img :src="video.cover" :alt="video.title" />
              <span class="video-duration">{{ video.duration }}</span>
            </div>
            <div class="video-info">
              <h4 class="video-title">{{ video.title }}</h4>
              <div class="video-stats">
                <span class="like-count">{{ video.likes }}</span>
                <span class="comment-count">{{ video.comments }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<script>
export default {
  name: 'Search',
  data() {
    return {
      searchText: '',
      showHistory: true,
      showGuess: true,
      showHot: true,
      historyList: ['大学生放假回家头发像流浪汉', '幸福的小羊羊'],
      guessTags: [
        '大学生放假回家头发...',
        '韦东奕照片',
        '抖音月付怎么关闭功能',
        '韦神',
        '成年人一天只打游戏...',
        '蜜雪冰城'
      ],
      hotList: [{ keyword: '唐虎便利团购', count: '1亿元' }],
      recommendTabs: [
        { key: 'hot', name: '抖音热搜' },
        { key: 'local', name: '同城榜' },
        { key: 'live', name: '直播' },
        { key: 'group', name: '团购' }
      ],
      activeTab: 'hot',
      videoList: [
        {
          cover: 'https://picsum.photos/200/300?random=1',
          title: '马克龙终究还是认怂了！',
          duration: '01:23',
          likes: '105',
          comments: '12'
        },
        {
          cover: 'https://picsum.photos/200/300?random=2',
          title: '这个女生咋了 发生了什么事',
          duration: '00:45',
          likes: '73',
          comments: '8'
        },
        {
          cover: 'https://picsum.photos/200/300?random=3',
          title: '马斯克终究还是认怂了！',
          duration: '02:15',
          likes: '234',
          comments: '31'
        },
        {
          cover: 'https://picsum.photos/200/300?random=4',
          title: 'Grok被禁止生成色情深伪图',
          duration: '01:08',
          likes: '156',
          comments: '22'
        }
      ],

      activeNav: 'search'
    };
  },
  methods: {
    goBack() {
      this.$router.back();
    },
    handleSearchInput() {
      // 处理搜索输入
    },
    handleSearch() {
      // 处理搜索
      if (this.searchText && !this.historyList.includes(this.searchText)) {
        this.historyList.unshift(this.searchText);
        if (this.historyList.length > 10) {
          this.historyList.pop();
        }
      }
    },
    openCamera() {
      // 打开相机
    },
    selectHistory(item) {
      this.searchText = item;
      this.handleSearch();
    },
    deleteHistory(index) {
      this.historyList.splice(index, 1);
    },
    clearHistory() {
      this.historyList = [];
    },
    viewMoreHistory() {
      // 查看更多历史
    },
    refreshGuess() {
      // 刷新猜你想搜
    },
    searchTag(tag) {
      this.searchText = tag;
      this.handleSearch();
    },
    switchTab(tabKey) {
      this.activeTab = tabKey;
    },
    switchNav(navKey) {
      this.activeNav = navKey;
      // 导航切换逻辑
      switch(navKey) {
      case 'home':
        this.$router.push('/');
        break;
      case 'publish':
        // 发布逻辑
        break;
      case 'message':
        this.$router.push('/message');
        break;
      case 'profile':
        this.$router.push('/profile');
        break;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.search-container {
  display: flex;
  flex-direction: column;
  height: 100vh;

  // 头部搜索栏
  .search-header {
    display: flex;
    align-items: center;
    padding: 10px 15px;
    background-color: white;

    .header-left {
      width: 40px;
      .back-btn {
        background: none;
        border: none;
        font-size: 20px;
        cursor: pointer;
        padding: 5px;
      }
    }

    .search-input-wrapper {
      flex: 1;
      display: flex;
      align-items: center;
      background-color: #f0f0f0;
      padding: 8px 15px;
      position: relative;

      .search-input {
        flex: 1;
        border: none;
        background: transparent;
        outline: none;
        font-size: 14px;
      }

      .icon-camera {
        margin-left: 10px;
        font-size: 18px;
        cursor: pointer;
      }
    }

    .header-right {
    display:flex;
      width: 60px;
      .search-btn {
        background: none;
        border: none;
        font-size: 14px;
        cursor: pointer;
        padding: 5px;
        font-weight: 500;
      }
    }
  }

  // 搜索内容区域
  .search-content {
    flex: 1;
    overflow-y: auto;
    padding: 15px;

    // 历史记录
    .history-section {
      background-color: white;
      border-radius: 8px;
      padding: 15px;
      margin-bottom: 15px;

      .history-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        h3 {
          font-size: 16px;
          font-weight: 500;
          color: #333;
          margin: 0;
        }

        .clear-btn {
          background: none;
          border: none;
          font-size: 14px;
          cursor: pointer;
          color: #999;
        }
      }

      .history-list {
        list-style: none;
        padding: 0;
        margin: 0 0 15px 0;

        .history-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 10px 0;
          border-bottom: 1px solid #f0f0f0;

          &:last-child {
            border-bottom: none;
          }

          .history-text {
            font-size: 14px;
            color: #333;
            cursor: pointer;
          }

          .delete-item {
            background: none;
            border: none;
            font-size: 16px;
            cursor: pointer;
            color: #999;
            padding: 5px;
          }
        }
      }

      .more-history {
        display: block;
        width: 100%;
        background: none;
        border: none;
        font-size: 14px;
        cursor: pointer;
        color: #1677ff;
        padding: 10px 0;
        text-align: center;
      }
    }

    // 猜你想搜
    .guess-section {
      background-color: white;
      border-radius: 8px;
      padding: 15px;
      margin-bottom: 15px;

      .guess-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        h3 {
          font-size: 16px;
          font-weight: 500;
          color: #333;
          margin: 0;
        }

        .guess-actions {
          display: flex;
          align-items: center;

          .refresh-btn {
            background: none;
            border: none;
            font-size: 14px;
            cursor: pointer;
            color: #666;
            margin-right: 15px;
          }

          .more-btn {
            background: none;
            border: none;
            font-size: 18px;
            cursor: pointer;
            color: #999;
            padding: 5px;
          }
        }
      }

      .guess-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;

        .guess-tag {
          background-color: #f0f0f0;
          border-radius: 15px;
          padding: 8px 15px;
          font-size: 14px;
          color: #333;
          cursor: pointer;
          transition: all 0.2s;

          &:hover {
            background-color: #e0e0e0;
          }
        }
      }
    }

    // 热搜榜
    .hot-section {
      background-color: white;
      border-radius: 8px;
      padding: 15px;
      margin-bottom: 15px;

      .hot-header {
        margin-bottom: 15px;

        h3 {
          font-size: 16px;
          font-weight: 500;
          color: #333;
          margin: 0;
        }
      }

      .hot-list {
        .hot-item {
          display: flex;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #f0f0f0;
          cursor: pointer;

          &:last-child {
            border-bottom: none;
          }

          .hot-rank {
            width: 20px;
            font-size: 14px;
            font-weight: 500;
            color: #ff4d4f;
            margin-right: 12px;
          }

          .hot-title {
            flex: 1;
            font-size: 14px;
            color: #333;
          }

          .hot-count {
            font-size: 12px;
            color: #999;
          }
        }
      }
    }

    // 猜你想看
    .recommend-section {
      background-color: white;
      border-radius: 8px;
      padding: 15px;

      .recommend-tabs {
        display: flex;
        margin-bottom: 15px;
        border-bottom: 1px solid #f0f0f0;

        .tab-btn {
          flex: 1;
          background: none;
          border: none;
          font-size: 14px;
          cursor: pointer;
          padding: 10px 0;
          color: #666;
          position: relative;

          &.active {
            color: #1677ff;
            font-weight: 500;

            &::after {
              content: '';
              position: absolute;
              bottom: -1px;
              left: 50%;
              transform: translateX(-50%);
              width: 20px;
              height: 2px;
              background-color: #1677ff;
            }
          }
        }
      }

      .video-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 15px;

        .video-item {
          cursor: pointer;

          .video-cover {
            position: relative;
            width: 100%;
            aspect-ratio: 2/3;
            border-radius: 8px;
            overflow: hidden;

            img {
              width: 100%;
              height: 100%;
              object-fit: cover;
            }

            .video-duration {
              position: absolute;
              bottom: 8px;
              right: 8px;
              background-color: rgba(0, 0, 0, 0.6);
              color: white;
              font-size: 12px;
              padding: 2px 6px;
              border-radius: 10px;
            }
          }

          .video-info {
            margin-top: 8px;

            .video-title {
              font-size: 14px;
              color: #333;
              margin: 0 0 5px 0;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
              overflow: hidden;
              text-overflow: ellipsis;
            }

            .video-stats {
              display: flex;
              gap: 15px;
              font-size: 12px;
              color: #999;
            }
          }
        }
      }
    }
  }

  // 底部导航栏
  .search-nav {
    display: flex;
    justify-content: space-around;
    align-items: center;
    background-color: white;
    border-top: 1px solid #f0f0f0;
    padding: 8px 0;

    .nav-btn {
      display: flex;
      flex-direction: column;
      align-items: center;
      background: none;
      border: none;
      cursor: pointer;
      padding: 5px;
      color: #666;

      i {
        font-size: 20px;
        margin-bottom: 3px;
      }

      span {
        font-size: 12px;
      }

      &.active {
        color: #1677ff;
      }
    }
  }
}
</style>
