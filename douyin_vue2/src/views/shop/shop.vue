<template>
  <div class="ssss">
    <div class="header">
      <!-- 搜索输入框 -->
      <div class="search" ref="searchBox">
        <button @click="searchmeth">搜索</button>
        <input
          v-model="searchText"
          @focus="showSuggestions = true"
          @blur="hideSuggestions"
          type="text"
          placeholder="输入搜索内容"
        />
      </div>

      <!-- 显示联想框 -->
      <div class="lian" ref="suggestionBox">
        <ul v-if="showSuggestions && filteredSuggestions.length > 0">
          <li
            v-for="(suggestion, index) in filteredSuggestions"
            :key="index"
            @click="selectSuggestion(suggestion)"
          >
            {{ suggestion }}
          </li>
        </ul>
      </div>
    </div>

    <div class="box-containeraaa" @scroll="handleScroll">
      <div class="meum">
        <scroll-nav :navItems="parentMsg"></scroll-nav>
      </div>

      <div class="nav" style="width: 100%">
        <GridDisplay
          :itemWidth="80"
          :itemHeight="100"
          :items="items"
          :gap="5"
        />
      </div>

      <div class="main">
        <RecommendList
          :columns="2"
          :items="shopitems"
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
    </div>

    <footer-vue class="footer"></footer-vue>
  </div>
</template>

<script>
import { getButtonInfo } from '@/api/admin';
import ScrollNav from '../../components/ScrollNav.vue';
import GridDisplay from '@/components/GridDisplay.vue';
import { useTokenStore } from '@/stores/token';
import footerVue from '@/components/footer.vue';
import RecommendList from '../../components/RecommendList.vue';
export default {
  beforeDestroy() {
    // 移除事件监听
    document.removeEventListener('click', this.handleOutsideClick);
  },
  components: {
    footerVue,
    ScrollNav,
    GridDisplay,
    RecommendList
  },
  data() {
    return {
      items: [
        {
          icon: 'https://cdn-icons-png.flaticon.com/512/616/616408.png',
          name: '图标1'
        }
      ],

      parentMsg: [{ icon: '🏠', label: '首页' }],
      searchText: '', // 输入框的内容
      showSuggestions: false, // 是否显示联想框
      suggestions: ['苹果', '香蕉', '橙子', '葡萄', '西瓜', '草莓', '芒果'],
      searchlian: [
        { id: 1, name: 'ssadsas' },
        { id: 2, name: 'ssdasss' },
        { id: 3, name: 'dsadssss' }
      ],
      search: '',
      isFocused: false,
      // 初始化更多的数据，使其超出视口
      boxes: Array.from({ length: 50 }, (_, index) => index + 1), // 初始数据为 50 个盒子
      loading: false, // 加载状态
      shopitems: [],
      page: 1,
      pageSize: 10,
      noMore: false
    };
  },
  computed: {
    filteredSuggestions() {
      // 根据输入框内容过滤联想词
      return this.suggestions.filter((suggestion) =>
        suggestion.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }
  },
  mounted() {
    document.addEventListener('click', this.handleOutsideClick);
    this.InitMenu('shop_home_menu');
    this.InitMenu('shop_home_nav');
    this.loadMore();
  },
  methods: {
    handleScroll(event) {
      const container = event.target;
      if (
        container.scrollHeight <=
        container.scrollTop + container.clientHeight + 200
      ) {
        this.loadMore();
      }
    },
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
    InitMenu(type) {
      const menu = {
        menuType: type,
        status: 1
      };
      getButtonInfo(menu)
        .then((res) => {
          if (type === 'shop_home_menu') {
            this.parentMsg = res.data.data.map((item) => ({
              icon: item.menuImg,
              label: item.menuName
            }));
          } else {
            this.items = res.data.data.map((item) => ({
              icon: item.menuImg,
              name: item.menuName
            }));
          }
        })
        .catch((error) => {
          console.error('获取按钮信息失败:', error);
        });
    },
    async fetchData(page, pageSize) {
      // 模拟延迟
      await new Promise((resolve) => setTimeout(resolve, 500));

      // 模拟总数据量为 50 条
      const total = 50;
      const start = (page - 1) * pageSize;
      const end = Math.min(start + pageSize, total);

      if (start >= total) return [];

      const data = Array.from({ length: end - start }).map((_, index) => {
        const id = start + index + 1;
        return {
          title: `推荐内容 #${id}`,
          image: `https://picsum.photos/300/200?random=${id}`
        };
      });

      return data;
    },
    selectSuggestion(suggestion) {
      // 点击某个联想词时更新输入框的内容
      this.searchText = suggestion;

      // 可选：如果需要根据其他逻辑来决定是否关闭联想框，可以去掉此行
      // 例如：点击搜索后可保留联想框状态
      this.showSuggestions = false; // 点击后隐藏联想框
    },
    hideSuggestions() {
      // 失去焦点时隐藏联想框
      setTimeout(() => {
        this.showSuggestions = false;
      }, 100); // 延迟隐藏，确保点击联想词时不关闭
    },

    // 点击外部区域时关闭联想框
    handleOutsideClick(event) {
      const searchBox = this.$refs.searchBox;
      const suggestionBox = this.$refs.suggestionBox;

      // 如果点击的地方既不是输入框也不是联想框，则关闭联想框
      if (
        !searchBox.contains(event.target) &&
        !suggestionBox.contains(event.target)
      ) {
        this.showSuggestions = false;
      }
    },

    searchmeth() {
      // 实现搜索操作
      console.log('搜索内容:', this.searchText);
      // 使用查询参数传递搜索内容
      this.$router.push({
        path: '/searchshop',
        query: { search: this.searchText }
      });
    },
    submitlian(name) {
      this.search = name;
    },

    go(hrefurl) {
      const usertoken = useTokenStore();
      const id = usertoken.getToken;
      this.$router.push({ path: `/${hrefurl}/${id}` });
    },
    handleFocus() {
      this.isFocused = true; // 输入框获得焦点
    },
    handleBlur() {
      this.isFocused = false; // 输入框失去焦点
    },
    // searchmeth() {

    //     // 自动发送请求给后端
    //     searchForDish(this.search).then(shopArr => {
    //         console.log(shopArr.data)
    //     }).catch(error => {
    //         console.log('获取视频出错:', error);
    //         console.log("获取视频出错  videobox中的")
    //     });
    // },
    goshopdetail(id) {
      this.$router.push({ path: `/shopdetail/${id}` });
    },
    getmenu() {},
    getnavone() {},
    getshopinfo() {},
    getinputciyu() {
      //获得搜索输入框联想词
    },
    startsearch() {},
    gettuijian() {}
  }
};
</script>

<style scoped>
.box-containeraaa {
  height: 100vh;
  /* 高度可以根据需要调整 */
  overflow-y: scroll;
  border: 1px solid #ccc;
}

.boxaaa {
  padding: 10px;
  border: 1px solid #ccc;
  margin: 5px;
  background-color: #f9f9f9;
}

.loadingaaa {
  text-align: center;
  padding: 10px;
  color: #888;
}

.lian {
  z-index: 10;
  background-color: #f8f9fa;
  /* 背景色 */
  border-radius: 8px;
  /* 圆角 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  /* 阴影效果 */
  padding: 10px 15px;
  /* 内边距 */
  max-width: 300px;
  /* 最大宽度 */
  width: 100%;
  /* 宽度自适应 */
  font-family: 'Arial', sans-serif;
  /* 字体 */
  text-align: left;
}

.lian ul {
  list-style-type: none;
  /* 去除默认的列表样式 */
  padding: 0;
  margin: 0;
}

.lian li {
  padding: 8px;
  /* 每个列表项的内边距 */
  font-size: 14px;
  /* 字体大小 */
  color: #333;
  /* 字体颜色 */
  cursor: pointer;
  /* 鼠标指针效果 */
  transition:
    background-color 0.3s,
    color 0.3s;
  /* 平滑过渡 */
}

.lian li:hover {
  background-color: #f0f0f0;
  /* 悬停背景色 */
  color: #007bff;
  /* 悬停字体颜色 */
}

html,
body {
  height: 100%;
  /* 确保 html 和 body 占满全高 */
  overflow: hidden;
  /* 禁止页面滑动 */
}

.shop {
  width: 100vw;
  height: 100vh;
  display: block;
  position: relative;
  overflow: hidden;
  background-color: white;
}

.allowscoll {
  height: 100vh;
  overflow-y: scroll;
  /* 允许垂直滚动 */
}

/* 隐藏滚动条 */
.allowscoll::-webkit-scrollbar {
  display: none;
  /* 对于 Webkit 浏览器（如 Chrome, Safari） */
}

.allowscoll {
  -ms-overflow-style: none;
  /* 对于 Internet Explorer 和 Edge */
  scrollbar-width: none;
  /* 对于 Firefox */
}

.boxm {
  width: 40vw;
  height: 30vh;
  background-color: green;
  margin: 2vw;
  background-size: cover;
  /* 或者使用 contain */
  background-position: center;
  /* 使背景居中 */
  background-repeat: no-repeat;
  /* 防止背景图片重复 */
  cursor: pointer;
  /* 鼠标悬停时的指针效果 */
}

.main {
  width: 95vw;
  margin: 0 auto;
  overflow-x: hidden; /* 禁止横向滚动 */
}

.recommend-item {
  width: 100%;
  box-sizing: border-box;
  break-inside: avoid;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.cover {
  width: 100%;
  display: block;
  height: auto;
}

.title {
  padding: 8px;
  font-size: 16px;
}

.nav img,
p {
  width: 10vw;
  display: block;
  transform: translateX(3vw);
  font-size: 3vw;
  color: rgb(47, 47, 47);
  line-height: 0px;
}

.header {
  overflow: hidden;
  position: absolute;
  top: 0px;
  width: 100%;
  background-color: white;
  z-index: 9;
}

.search input {
  width: 50%;
  height: 3vh;
  margin-left: 5vw;
  border: none;
  display: block;
  float: left;
  outline: none;
  margin-top: 0.8vh;
}

.search {
  border: solid 2px rgb(233, 111, 111);
  overflow: hidden;
  border-radius: 10px;
  margin-top: 1vh;
  width: 95%;
  float: right;
  margin: 5px;
}

.search button {
  display: block;
  float: right;
  height: 3vh;
  width: 15vw;
  border: none;
  background-color: red;
  color: white;
  border-radius: 4vw;
  margin: 1vw;
}

.didian {
  width: 10vw;
  line-height: 20vw;
  font-size: 5vw;
  margin-left: 1vw;
  margin-right: 0;
  float: left;
}

.box1 {
  height: 20vw;
  width: 20vw;
  margin: 2px;
  flex-shrink: 0;
  /* 阻止元素缩小 */
  background-size: cover;
}

.box2 {
  width: 18vw;
  height: 18vw;
  margin: 1px;
}

.nav {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  align-content: center;
  justify-content: center;
  margin-top: 1vh;
}

.meum {
  margin-top: 7vh;
  display: flex;
  flex-direction: row;
  /* 水平方向排列子元素 */
  align-items: center;
  /* 垂直居中对齐子元素 */
  justify-content: flex-start;
  /* 左对齐子元素 */
  overflow-x: auto;
  /* 启用水平滚动 */
  overflow-y: hidden;
  /* 隐藏垂直滚动 */
  white-space: nowrap;
  /* 防止子元素换行 */
}

.meum::-webkit-scrollbar {
  width: 0;
  /* 隐藏滚动条的宽度 */
  background: transparent;
  /* 背景透明 */
}

.footer {
  background-color: black;
  color: black;
  z-index: 10;
  position: absolute;
  bottom: 0px;
}

.jiahao {
  background-image: url(../../assets/home/加号黑.png);
}
</style>
