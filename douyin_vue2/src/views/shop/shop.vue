<!-- 使用的flex布局 -->

<template>
    <div class="shop">
        <div class="header">
            <div class="didian">西安^</div>
            <div class="search">
                <button>搜索</button>
                <input type="text" placeholder="蜜雪冰城团购">
            </div>
        </div>
        <div class="meum">
            <div class="box1"></div>
            <div class="box1"></div>
            <div class="box1"></div>
            <div class="box1"></div>
            <div class="box1"></div>
            <div class="box1"></div>

        </div>
        <div class="nav">
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>
            <div class="box">
                <img src="../../assets/shop/附近美食.png" alt="">
                <p>附近美食</p>
            </div>

        </div>
        <div class="main">
            <div class="boxm" @click="goshopdetail"></div>
            <div class="boxm"></div>
            <div class="boxm"></div>
            <div class="boxm"></div>
        </div>
        <base-footer class=".footer"></base-footer>
    </div>
</template>
<style  scoped>
.boxm {
    width: 180px;
    height: 300px;
    background-color: green;
    margin: 5px;
}

.main {
    width: 400px;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-content: center;
    margin: 0 auto;
}

.nav img,
p {
    width: 55px;
    display: block;
    transform: translateX(10px);
    font-size: 12px;
    color: rgb(47, 47, 47);
    line-height: 0px;
}

.header {
    overflow: hidden;
}

.search input {
    width: 250px;
    height: 50px;
    border: none;
    border-radius: 10px;
    display: block;
    float: right;
}

.search button {
    display: block;
    float: right;
    height: 40px;
    width: 70px;
    border: none;
    background-color: red;
    color: white;
    border-radius: 10px;
    margin: 5px;

}


.didian {
    width: 66px;
    line-height: 66px;
    font-size: 20px;
    margin-left: 5px;
    margin-right: 0px;
    float: left;
}

.search {
    border: solid 2px rgb(233, 111, 111);
    overflow: hidden;
    border-radius: 10px;
    margin-top: 10px;
    width: 340px;
    float: right;
    margin-right: 0px;
}

.box1 {
    height: 76px;
    width: 100px;
    margin: 2px;
    flex-shrink: 0;
    /* 阻止元素缩小 */
    /* 使溢出部分可见 */
    background-image: url(../../assets/shop/亲子.png);
    background-size: cover;
}

.box {
    width: 80px;
    height: 80px;
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
    display: flex;
    flex-direction: column;
}

::v-deep .footer {
    background-color: white;
    color: black;

}

::v-deep .jiahao {
    background-image: url(../../assets/home/加号黑.png);

}
</style>

</style>
<script>
import { getcai } from "../../api/video.js"
import BaseFooter from '@/components/BaseFooter.vue'
export default {
    components: {
        BaseFooter
    },
    data() {
        return {
        }
    },
    methods: {
        goshopdetail() {
            this.$router.push("/shopdetail")
        }

    }



}
</script>