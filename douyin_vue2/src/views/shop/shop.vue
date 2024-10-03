<template>
    <div class="temp">
        <div class="shop">
            <div class="header">
                <div class="didian">西安^</div>
                <div class="search">
                    <button>搜索</button>
                    <input type="text" placeholder="蜜雪冰城团购">
                </div>
            </div>

            <div class="meum">
                <div class="box1" v-for="(meum, index) in meumbox" :key="index">
                    <img :src="meum.imgurl" alt="" style="width: 100%; height: auto;">

                    <p>{{ meum.name }}</p>
                </div>
            </div>





            <div class="nav">
                <div class="box" v-for="(nav, index) in navbox" :key="index">
                    <img :src="nav.navimgurl" alt="">
                    <p>附近美食</p>
                </div>
            </div>

            <div class="main">
                <div class="boxm" v-for="(item, index) in mainbox" :key="index" @click="goshopdetail(index + 1)"
                    :style="{ backgroundImage: 'url(' + item.imgurl + ')' }">
                    <p>{{ item.name }}</p>
                </div>
            </div>
        </div>

        <footer-vue class="footer"></footer-vue>
    </div>
</template>

<script>
import footerVue from '@/components/footer.vue'
export default {
    components: {
        footerVue
    },
    data() {
        return {
            meumbox: [
                { name: "超市", imgurl: "https://gimg3.baidu.com/search/src=http%3A%2F%2Fpics4.baidu.com%2Ffeed%2F0df431adcbef7609fa79f5ac9d0714c27dd99ef8.jpeg%40f_auto%3Ftoken%3Dd546ae637a8c6fad135ffc7d40889a7b&refer=http%3A%2F%2Fwww.baidu.com&app=2021&size=f360,240&n=0&g=0n&q=75&fmt=auto?sec=1728061200&t=32343368505ab2dc1a5d4214c5181515", hrefurl: "" },
                { name: "超市", imgurl: "", hrefurl: "" },
                { name: "超市", imgurl: "", hrefurl: "" },
                { name: "超市", imgurl: "", hrefurl: "" },
                { name: "超市", imgurl: "", hrefurl: "" },
                { name: "超市", imgurl: "", hrefurl: "" },
            ],
            navbox: [
                { navname: "", navimgurl: "https://gimg3.baidu.com/search/src=http%3A%2F%2Fpics4.baidu.com%2Ffeed%2F0df431adcbef7609fa79f5ac9d0714c27dd99ef8.jpeg%40f_auto%3Ftoken%3Dd546ae637a8c6fad135ffc7d40889a7b&refer=http%3A%2F%2Fwww.baidu.com&app=2021&size=f360,240&n=0&g=0n&q=75&fmt=auto?sec=1728061200&t=32343368505ab2dc1a5d4214c5181515", navhrefurl: "" },
                { navname: "", navimgurl: "", navhrefurl: "" },
                { navname: "", navimgurl: "", navhrefurl: "" },
                { navname: "", navimgurl: "", navhrefurl: "" },
                { navname: "", navimgurl: "", navhrefurl: "" },
                { navname: "", navimgurl: "", navhrefurl: "" },
                { navname: "", navimgurl: "", navhrefurl: "" },
                { navname: "", navimgurl: "", navhrefurl: "" },
            ],
            mainbox: [
                { imgurl: "https://gimg3.baidu.com/search/src=http%3A%2F%2Fpics4.baidu.com%2Ffeed%2F0df431adcbef7609fa79f5ac9d0714c27dd99ef8.jpeg%40f_auto%3Ftoken%3Dd546ae637a8c6fad135ffc7d40889a7b&refer=http%3A%2F%2Fwww.baidu.com&app=2021&size=f360,240&n=0&g=0n&q=75&fmt=auto?sec=1728061200&t=32343368505ab2dc1a5d4214c5181515", name: "商品1", hrefurl: "/shopdetail/1" },
                { imgurl: "url2.jpg", name: "商品2", hrefurl: "/shopdetail/2" },
                { imgurl: "url3.jpg", name: "商品3", hrefurl: "/shopdetail/3" },
                { imgurl: "url4.jpg", name: "商品4", hrefurl: "/shopdetail/4" },
                { imgurl: "url5.jpg", name: "商品5", hrefurl: "/shopdetail/5" },
                { imgurl: "url6.jpg", name: "商品6", hrefurl: "/shopdetail/6" },
            ]
        }
    },
    methods: {
        goshopdetail(id) {
            this.$router.push({ path: `/shopdetail/${id}` });
        }
    }
}
</script>



<style  scoped>
.boxm {
    width: 40vw;
    height: 30vh;
    background-color: green;
    margin: 2vw;
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
}

.search input {
    width: 70vw;
    height: 3vh;
    border: none;
    border-radius: 5vh;
    display: block;
    float: right;
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

.search {
    border: solid 2px rgb(233, 111, 111);
    overflow: hidden;
    border-radius: 10px;
    margin-top: 1vh;
    width: 340px;
    float: right;
    margin-right: 0px;
    height: 4vh;
}

.box1 {
    height: 76px;
    width: 100px;
    margin: 2px;
    flex-shrink: 0;
    /* 阻止元素缩小 */
    /* 使溢出部分可见 */
    background-size: cover;
}

.box {
    width: 20vw;
    height: 20vw;
    margin: 1px;

}

.nav {
    width: 100%;
    height: 180px;
    display: flex;
    flex-wrap: wrap;
    align-content: center;
    justify-content: center;
}

.meum {
    width: 100%;
    height: 80px;
    flex-direction: row;
    display: flex;
    align-content: center;
    justify-content: center;
    overflow: scroll;

}

.meum::-webkit-scrollbar {
    display: none;
    /* 对 WebKit 隐藏滚动条 */
}

.header {
    width: 100%;
    height: 80px;

}

.shop {
    width: 100vw;
    height: 100vh;
    display: block;
    position: relative;
    overflow: scroll;
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

</style>