<template>
    <div class="ssss">
        <div class="header">
            <!-- 搜索输入框 -->
            <div class="search" ref="searchBox">
                <button @click="searchmeth">搜索</button>
                <input v-model="searchText" @focus="showSuggestions = true" @blur="hideSuggestions" type="text"
                    placeholder="输入搜索内容" />
            </div>

            <!-- 显示联想框 -->
            <div class="lian" ref="suggestionBox">
                <ul v-if="showSuggestions && filteredSuggestions.length > 0">
                    <li v-for="(suggestion, index) in filteredSuggestions" :key="index"
                        @click="selectSuggestion(suggestion)">
                        {{ suggestion }}
                    </li>
                </ul>
            </div>
        </div>

        <div class="box-containeraaa" @scroll="handleScroll">
            <!-- <div v-for="(box, index) in boxes" :key="index" class="boxaaa">
                {{ box }}
            </div> -->
            <div class="meum">
                <!-- <div class="box1" v-for="(meum, index) in meumbox" :key="index">
                    <img :src="meum.menuImg" alt="" style="width: 100%; height: 100%;" @click="go(meum.hrefurl)">
                </div> -->
                <scroll-nav :navItems="parentMsg"></scroll-nav>
            </div>

            <div class="nav" style="width: 100%;">
    <GridDisplay
      :itemWidth="80"
      :itemHeight="100"
      :items="items"
      :gap="5"
    />
            </div>

            <div class="main">
    <RecommendList :columns="2" :loadData="fetchData">
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
import { shoplist, searchForDish, liansearchForDish, shoplistpage, getnav } from "@/api/shop"
import ScrollNav from "../../components/ScrollNav.vue"
import { shopmenulist } from "@/api/menu"
import GridDisplay from '@/components/GridDisplay.vue'
import { shopnavlist } from "@/api/menu"
import { useTokenStore } from "@/stores/token"
import footerVue from '@/components/footer.vue'
import img1 from '@/assets/shop/menunav/1.png';
import img2 from '@/assets/shop/menunav/2.png';
import img3 from '@/assets/shop/menunav/3.png';
import img4 from '@/assets/shop/menunav/4.png';
import img5 from '@/assets/shop/menunav/5.png';
import imgn6 from '@/assets/shop/nav/2.png';
import imgn7 from '@/assets/shop/nav/3.png';
import imgn8 from '@/assets/shop/nav/4.png';
import imgn9 from '@/assets/shop/nav/5.png';
import imgn10 from '@/assets/shop/nav/6.png';
import imgm1 from '@/assets/shop/maininfo/1.png';
import imgm2 from '@/assets/shop/maininfo/2.png';
import imgm3 from '@/assets/shop/maininfo/3.png';
import imgm4 from '@/assets/shop/maininfo/4.png';
import RecommendList from '../../components/RecommendList.vue'
export default {
    beforeDestroy() {
        // 移除事件监听
        document.removeEventListener('click', this.handleOutsideClick);
    },
    components: {
        footerVue,
        ScrollNav,
        GridDisplay,
     
        RecommendList,
    },
    data() {
        return {
            items: [
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616408.png', name: '图标1' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616429.png', name: '图标2' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616490.png', name: '图标3' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616554.png', name: '图标4' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616494.png', name: '图标5' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616409.png', name: '图标6' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616426.png', name: '图标7' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616453.png', name: '图标8' },
      ],
               parentMsg: [
        { icon: "🏠", label: "首页" },
        { icon: "🔥", label: "热门" },
        { icon: "🛍", label: "商城" },
        { icon: "💬", label: "消息" },
        { icon: "👤", label: "我的" },
        { icon: "📺", label: "直播" },
        { icon: "🎮", label: "游戏" },
        { icon: "📷", label: "相册" },
      ],
            searchText: '',  // 输入框的内容
            showSuggestions: false,  // 是否显示联想框
            suggestions: ['苹果', '香蕉', '橙子', '葡萄', '西瓜', '草莓', '芒果'],
            searchlian: [{ id: 1, name: "ssadsas" }, { id: 2, name: "ssdasss" }, { id: 3, name: "dsadssss" }],
            search: "",
            isFocused: false,
            // 初始化更多的数据，使其超出视口
            boxes: Array.from({ length: 50 }, (_, index) => index + 1),  // 初始数据为 50 个盒子
            loading: false,  // 加载状态
            meumbox: [
                { name: "超d市", menuImg: img1, hrefurl: "order" },
                { name: "超d市", menuImg: img2, hrefurl: "chongzhi" },
                { name: "超d市", menuImg: img3, hrefurl: "shopmessage" },
                { name: "超d市", menuImg: img4, hrefurl: "" },
                { name: "超d市", menuImg: img5, hrefurl: "" },
                { name: "超d市", menuImg: img5, hrefurl: "" },
                { name: "超d市", menuImg: img5, hrefurl: "" },
            ],
            navbox: [
                { navname: "ss", navImg: imgn6, navhrefurl: "order" },
                { navname: "ss", navImg: imgn7, navhrefurl: "chongzhi" },
                { navname: "ss", navImg: imgn8, navhrefurl: "shopmessage" },
                { navname: "ss", navImg: imgn9, navhrefurl: "order" },
                { navname: "ss", navImg: imgn10, navhrefurl: "" },
                { navname: "ss", navImg: imgn10, navhrefurl: "" },
                { navname: "ss", navImg: imgn10, navhrefurl: "" },
                { navname: "ss", navImg: imgn10, navhrefurl: "" },
                { navname: "ss", navImg: imgn10, navhrefurl: "" },
                { navname: "ss", navImg: imgn10, navhrefurl: "" },
            ],
            mainbox: [
                { image: imgm1, name: "商品1", hrefurl: "/shopdetail/1", shopId: "25" },
                { image: imgm2, name: "商品1", hrefurl: "/shopdetail/1", shopId: "55" },
                { image: imgm3, name: "商品1", hrefurl: "/shopdetail/1", shopId: "66" },
                { image: imgm4, name: "商品1", hrefurl: "/shopdetail/1", shopId: "55" },
                { image: imgm4, name: "商品1", hrefurl: "/shopdetail/1", shopId: "4" },
                { image: imgm4, name: "商品1", hrefurl: "/shopdetail/1", shopId: "5" },
            ],
            pagenum: 1,
            size: 10,
        };
    },
    computed: {
        filteredSuggestions() {
            // 根据输入框内容过滤联想词
            return this.suggestions.filter((suggestion) =>
                suggestion.toLowerCase().includes(this.searchText.toLowerCase())
            );
        },
    },
    mounted() {
        
        // 监听点击外部区域事件
        document.addEventListener('click', this.handleOutsideClick);
        // // 自动发送请求给后端
        shoplist(this.pagenum, this.size).then(shopArr => {
            console.log(shopArr);
            if (shopArr && shopArr.data && shopArr.data.data) {
                // 检查数据是否有效
                this.mainbox = shopArr.data.data.records;
                console.log(this.mainbox);
            } else {
                console.log('获取 shoplist 数据失败: 数据无效');
            }
        }).catch(error => {
            console.log('获取 shoplist 数据出错:', error);
            console.log("获取 shoplist 数据出错");
        });



        // // 自动发送请求给后端
        getnav().then(shopArr => {
            console.log(shopArr);
            if (shopArr && shopArr.data && shopArr.data.data) {
                // 检查数据是否有效
                this.navbox = shopArr.data.data;
                console.log(this.navbox);
            } else {
                console.log('获取 shoplist 数据失败: 数据无效');
            }
        }).catch(error => {
            console.log('获取 shoplist 数据出错:', error);
            console.log("获取 shoplist 数据出错");
        });



        // 自动发送请求给后端
        shopmenulist().then(shopArr => {
            console.log(shopArr);
            if (shopArr && shopArr.data && shopArr.data.data) {
                // 检查数据是否有效
                this.meumbox = shopArr.data.data.records;
                console.log(this.meumbox);
            } else {
                console.log('获取 shopmenulist 数据失败: 数据无效');
            }
        }).catch(error => {
            console.log('获取 shopmenulist 数据出错:', error);
            console.log("获取 shopmenulist 数据出错");
        });

        // 自动发送请求给后端
        shopnavlist().then(shopArr => {
            console.log(shopArr);
            if (shopArr && shopArr.data && shopArr.data.data) {
                // 检查数据是否有效
                this.navbox = shopArr.data.data.records;
                console.log(this.navbox);
            } else {
                console.log('获取 shopnavlist 数据失败: 数据无效');
            }
        }).catch(error => {
            console.log('获取 shopnavlist 数据出错:', error);
            console.log("获取 shopnavlist 数据出错");
        });

    },
    methods: {
        async fetchData(page, pageSize) {
    // 模拟延迟
    await new Promise(resolve => setTimeout(resolve, 500));

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
            this.showSuggestions = false;  // 点击后隐藏联想框
        },
        hideSuggestions() {
            // 失去焦点时隐藏联想框
            setTimeout(() => {
                this.showSuggestions = false;
            }, 100);  // 延迟隐藏，确保点击联想词时不关闭
        },

        // 点击外部区域时关闭联想框
        handleOutsideClick(event) {
            const searchBox = this.$refs.searchBox;
            const suggestionBox = this.$refs.suggestionBox;

            // 如果点击的地方既不是输入框也不是联想框，则关闭联想框
            if (!searchBox.contains(event.target) && !suggestionBox.contains(event.target)) {
                this.showSuggestions = false;
            }
        },

        searchmeth() {
            // 实现搜索操作
            console.log('搜索内容:', this.searchText);
            // 使用查询参数传递搜索内容
            this.$router.push({ path: '/searchshop', query: { search: this.searchText } });
        },
        submitlian(name) {
            this.search = name
        },
        // 滚动事件处理函数
        handleScroll(event) {
            const container = event.target;
            const bottom = container.scrollHeight <= container.scrollTop + container.clientHeight;

            if (bottom && !this.loading) {
                this.loadMoreBoxes();  // 滚动到底部时加载更多数据
            }
        },
        // 请求加载更多盒子数据
        loadMoreBoxes() {
            this.loading = true;  // 开始加载
            // 模拟异步请求
            setTimeout(() => {
                shoplist(this.pagenum, this.size).then(shopArr => {
                    console.log(shopArr);  // 查看返回的数据结构
                    if (shopArr && shopArr.data && shopArr.data.data) {
                        const newData = shopArr.data.data.records;
                        if (Array.isArray(newData)) {
                            console.log('New data:', newData);  // 打印新数据
                            // 确保新数据是有效的，并将其添加到 mainbox
                            this.mainbox.push(...newData);

                            this.pagenum++;
                            console.log(this.mainbox);  // 打印更新后的 mainbox
                        } else {
                            console.log('新数据不是数组:', newData);
                        }
                    } else {
                        console.log('获取 shoplist 数据失败: 数据无效');
                    }
                }).catch(error => {
                    console.log('获取 shoplist 数据出错:', error);
                })
                this.loading = false;  // 加载完成
            }, 1000);
        },
        go(url) {
            // Navigation logic here
            console.log(url);
        },
        goshopdetail(shopId) {
            // Redirect to the product details page
            console.log(shopId);
        },
        go(hrefurl) {

            const usertoken = useTokenStore();
            const id = usertoken.getToken
            this.$router.push({ path: `/${hrefurl}/${id}` });
        },
        handleFocus() {
            this.isFocused = true;  // 输入框获得焦点
        },
        handleBlur() {
            this.isFocused = false;  // 输入框失去焦点
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
        getmenu() {

        },
        getnavone() {
            const respond = getnav()
        },
        getshopinfo() {

        },
        getinputciyu() {
            //获得搜索输入框联想词
        },
        startsearch() {

        },
        gettuijian() {

        }
    },
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
    transition: background-color 0.3s, color 0.3s;
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