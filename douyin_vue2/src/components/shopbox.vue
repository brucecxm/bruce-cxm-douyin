<template>
  <div class="temp">
    <div class="shop">
      <div class="header">
        <!-- <div class="didian">西安^</div> -->
        <div class="search">
          <button @click="searchmeth">搜索</button>
          <input
            type="text"
            placeholder="蜜雪冰城团购"
            v-model="search"
            @focus="handleFocus"
            @blur="handleBlur"
          />
        </div>
        <div class="lian" v-if="isFocused">
          <ul>
            <li v-for="(lian, index) in searchlian" :key="index">
              {{ lian.name }}
            </li>
            <li>ssss</li>
            <li>ssss</li>
            <li>ssss</li>
            <li>ssss</li>
            <li>ssss</li>
          </ul>
        </div>
      </div>

      <div class="allowscoll">
        <div class="meum">
          <div class="box1" v-for="(meum, index) in meumbox" :key="index">
            <img
              :src="meum.menuImg"
              alt=""
              style="width: 100%; height: 100%"
              @click="go(meum.hrefurl)"
            />
            <!--  <p>{{ meum.name }}</p> -->
          </div>
        </div>

        <div class="nav">
          <div class="box2" v-for="(nav, index) in navbox" :key="index">
            <img :src="nav.navImg" alt="" style="width: 100%; height: 100%" />
            <!-- <p>{{ nav.navname }}</p> -->
          </div>
        </div>

        <div class="main">
          <!-- 给这里的页面传入商品的唯一id  然后对应商品的详情页 -->
          <div
            class="boxm"
            v-for="(item, index) in mainbox"
            :key="index"
            @click="goshopdetail(item.shopId)"
            :style="{ backgroundImage: 'url(' + item.image + ')' }"
          >
            <!-- <p>{{ item.name }}</p> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { shoplist, searchForDish, liansearchForDish, getnav } from '@/api/shop';
import { shopmenulist } from '@/api/menu';
import { shopnavlist } from '@/api/menu';
import { useTokenStore } from '@/stores/token';
import footerVue from '@/components/footer.vue';
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
export default {
  components: {
    footerVue
  },
  data() {
    return {
      search: '',
      meumbox: [
        { name: '超d市', menuImg: img1, hrefurl: 'order' },
        { name: '超d市', menuImg: img2, hrefurl: 'chongzhi' },
        { name: '超d市', menuImg: img3, hrefurl: 'shopmessage' },
        { name: '超d市', menuImg: img4, hrefurl: '' },
        { name: '超d市', menuImg: img5, hrefurl: '' },
        { name: '超d市', menuImg: img5, hrefurl: '' },
        { name: '超d市', menuImg: img5, hrefurl: '' }
      ],
      navbox: [
        { navname: 'ss', navImg: imgn6, navhrefurl: 'order' },
        { navname: 'ss', navImg: imgn7, navhrefurl: 'chongzhi' },
        { navname: 'ss', navImg: imgn8, navhrefurl: 'shopmessage' },
        { navname: 'ss', navImg: imgn9, navhrefurl: 'order' },
        { navname: 'ss', navImg: imgn10, navhrefurl: '' },
        { navname: 'ss', navImg: imgn10, navhrefurl: '' },
        { navname: 'ss', navImg: imgn10, navhrefurl: '' },
        { navname: 'ss', navImg: imgn10, navhrefurl: '' },
        { navname: 'ss', navImg: imgn10, navhrefurl: '' },
        { navname: 'ss', navImg: imgn10, navhrefurl: '' }
      ],
      mainbox: [
        { image: imgm1, name: '商品1', hrefurl: '/shopdetail/1', shopId: '25' },
        { image: imgm2, name: '商品1', hrefurl: '/shopdetail/1', shopId: '55' },
        { image: imgm3, name: '商品1', hrefurl: '/shopdetail/1', shopId: '66' },
        { image: imgm4, name: '商品1', hrefurl: '/shopdetail/1', shopId: '55' },
        { image: imgm4, name: '商品1', hrefurl: '/shopdetail/1', shopId: '4' },
        { image: imgm4, name: '商品1', hrefurl: '/shopdetail/1', shopId: '5' }
      ],
      searchlian: [],
      isFocused: false, // 用于表示输入框是否被选中
      pagenum: 1
    };
  },
  mounted() {
    // // 自动发送请求给后端
    shoplist()
      .then((shopArr) => {
        console.log(shopArr);
        if (shopArr && shopArr.data && shopArr.data.data) {
          // 检查数据是否有效
          this.mainbox = shopArr.data.data;
          console.log(this.mainbox);
        } else {
          console.error('获取 shoplist 数据失败: 数据无效');
        }
      })
      .catch((error) => {
        console.error('获取 shoplist 数据出错:', error);
        console.log('获取 shoplist 数据出错');
      });

    // // 自动发送请求给后端
    getnav()
      .then((shopArr) => {
        console.log(shopArr);
        if (shopArr && shopArr.data && shopArr.data.data) {
          // 检查数据是否有效
          this.navbox = shopArr.data.data;
          console.log(this.navbox);
        } else {
          console.error('获取 shoplist 数据失败: 数据无效');
        }
      })
      .catch((error) => {
        console.error('获取 shoplist 数据出错:', error);
        console.log('获取 shoplist 数据出错');
      });
    // shoplistpage(this.pagenum, 10).then(shopArr => {
    //     console.log(shopArr);
    //     if (shopArr && shopArr.data && shopArr.data.data) {
    //         // 检查数据是否有效
    //         this.mainbox = shopArr.data.data;
    //         console.log(this.mainbox);
    //     } else {
    //         console.error('获取 shoplist 数据失败: 数据无效');
    //     }
    // }).catch(error => {
    //     console.error(error);  // Handle any error
    // });

    // 自动发送请求给后端
    shopmenulist()
      .then((shopArr) => {
        console.log(shopArr);
        if (shopArr && shopArr.data && shopArr.data.data) {
          // 检查数据是否有效
          this.meumbox = shopArr.data.data.records;
          console.log(this.meumbox);
        } else {
          console.error('获取 shopmenulist 数据失败: 数据无效');
        }
      })
      .catch((error) => {
        console.error('获取 shopmenulist 数据出错:', error);
        console.log('获取 shopmenulist 数据出错');
      });

    // 自动发送请求给后端
    shopnavlist()
      .then((shopArr) => {
        console.log(shopArr);
        if (shopArr && shopArr.data && shopArr.data.data) {
          // 检查数据是否有效
          this.navbox = shopArr.data.data.records;
          console.log(this.navbox);
        } else {
          console.error('获取 shopnavlist 数据失败: 数据无效');
        }
      })
      .catch((error) => {
        console.error('获取 shopnavlist 数据出错:', error);
        console.log('获取 shopnavlist 数据出错');
      });
  },
  watch: {
    search(newValue, oldValue) {
      // 自动发送请求给后端
      liansearchForDish(this.search)
        .then((shopArr) => {
          console.log(shopArr.data.data);
          this.searchlian = shopArr.data.data;
          this.lianopen = true;
        })
        .catch((error) => {
          console.error('获取视频出错:', error);
          console.log('获取视频出错  videobox中的');
        });
    }
  },
  methods: {
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
    searchmeth() {
      // 自动发送请求给后端
      searchForDish(this.search)
        .then((shopArr) => {
          console.log(shopArr.data);
        })
        .catch((error) => {
          console.error('获取视频出错:', error);
          console.log('获取视频出错  videobox中的');
        });
    },
    goshopdetail(id) {
      this.$router.push({ path: `/shopdetail/${id}` });
    },
    getmenu() {},
    getnavone() {
      const respond = getnav();
    },
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
.lian {
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

.temp {
  background-color: white;
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
  /* 确保 shop 不会滚动 */
  background-color: white;
  transform: translateY(5vh);
}

.allowscoll {
  overflow-y: scroll;
  /* 允许垂直滚动 */
  height: calc(100vh - 50px);
  /* 设置合适的高度，减去 header 的高度 */
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
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-content: center;
  margin: 0 auto;
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
  background-image: url(../assets/home/加号黑.png);
}
</style>
