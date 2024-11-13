<template>
    <div class="metemp">
        <footer-vue class="footer"></footer-vue>

        <div class="me">
            <memenu v-show="receivedMessage.ismemenushow" class="memenu"></memenu>
            <div :class="receivedMessage.me">
                <div class="backimg">
                    <img :src="User.data.backimg" alt=" no backimg" />
                    <div class="caidan" @click="openmenu"></div>
                    <div class="userinfo">
                        <div class="touxiang">
                            <img :src="User.data.userPic" alt="" />
                        </div>
                        <div class="name">
                            <span>{{ User.data.nickname }}</span>
                        </div>
                    </div>
                </div>

                <div class="frienginfo">
                    <img src="../../assets/me/2.png" alt="" />
                </div>
                <div class="personinfo">
                    <div class="info">
                        <p>{{ User.data.jieshao }}</p>
                        <span>{{ User.data.age }}</span>
                        <span>{{ User.data.location }}</span>
                        <span>{{ User.data.collegt }}</span>
                    </div>
                </div>
                <div class="infonav">
                    <div class="xuanxiang" v-for="(image, index) in navvideo" :key="index">
                        <img :src="image" alt="Image" />
                    </div>
                </div>
                <div class="nav">
                    <ul>
                        <li @click="fetchData('works')">作品</li>
                        <li @click="fetchData('dynamics')">动态</li>
                        <li @click="fetchData('likes')">喜欢</li>
                        <li @click="fetchData('albums')">相册</li>
                    </ul>
                </div>
                <div class="uservideo">
                    <div class="uvideo" v-for="(image, index) in mianvideoarr" :key="index">
                        <img :src="image" alt="Image" style="width: 100%; height: 100%;" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import footerVue from '@/components/footer.vue'
import mainvideo from '@/assets/me/1.png'
import Memenu from "../../components/memenu.vue"
import { userInfoService } from '../../api/user';
import oneImg from '@/assets/me/one.jpg';
import twoImg from '@/assets/me/two.jpg';
import threeImg from '@/assets/me/three.jpg';
import fourImg from '@/assets/me/four.jpg';
import { getmeinfo } from '../../api/me';
import { useTokenStore } from '@/stores/token'
export default {
    data() {
        return {
            videos: [],
            receivedMessage: {
                me: "me",
                ismemenushow: false,


            },
            User: {
                code: "",
                data: {
                    nickname: "默认名字",
                    userPic: "//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-i-0813c001_oYCBO7DFFAYxR4jFA7gwAEqBTmfEIGAfANeokE.jpeg?from=2956013662"
                    , backimg: "//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-i-0813c001_ooSy9lAm2G5DAfrLAAQAjO0bp7lg4eInAaCCGX.jpeg?from=2956013662"
                    , friendnum: "4"
                    , likenumL: "3"
                    , follow: "2"
                    , fan: "1"
                    , jieshao: "自我介绍"
                    , age: "年龄"
                    , location: "城市"
                    , collegt: "学校"

                },
                message: ""
            },
            nav: [oneImg, twoImg, threeImg, fourImg, fourImg],
            mianvideoarr: [mainvideo, mainvideo, mainvideo, mainvideo],
            navvideo: [oneImg, twoImg, threeImg, fourImg, fourImg]

        }
    },
    components: {
        footerVue, Memenu
    },
    methods: {
        openmenu() {
            this.receivedMessage.ismemenushow = true
            this.receivedMessage.me = "metransform"
        },
        gofriend() {
            this.$router.push("/friend")
        },
        fetchData(type) {
            console.log(type)
            //在这里调用实际的 API 请求或处理逻辑
        },

    },
    mounted() {
        // 获取用户令牌存储
        const usertoken = useTokenStore();


        // 获取用户信息的方法
        // 调用获取用户信息的函数
        getmeinfo(usertoken.data.message);

        // 调用获取用户信息的方法
    }


}
</script>


<style scoped>
/* .frienginfo {
    width: 100%;
    height: 4vh;
} */

.userinfo {
    position: absolute;
    top: 50px;
    width: 60%;
}

.touxiang {
    width: 130px;
    height: 130px;
    border-radius: 800px;
    overflow: hidden;
    border: 3px solid white;
    display: inline-block;
}


.xuanxiang {
    min-width: 80px;
    /* 设置最小宽度 */
    height: 80px;
    margin-right: 10px;
    margin-top: 10px;
}

.infonav {
    display: flex;
    flex-shrink: 0;
    /* 禁止缩小 */
    overflow-x: auto;
    /* 允许水平滚动 */
    white-space: nowrap;
    /* 不换行 */

    overflow-x: hidden;
    /* 隐藏水平滚动条 */
    overflow-y: hidden;
    /* 隐藏水平滚动条 */
    overflow-y: hidden;
    /* 禁止垂直滚动 */
}


span {
    background-color: rgba(137, 8, 211, 0.1);
    margin-left: 10px;
}

.memenu {
    position: relative;
    top: 0px;
    right: 0px;
    z-index: 6;
}

.uvideo {
    width: 48%;
    height: 40vh;
    background-color: rgba(0, 0, 0, 0.1);
    margin: 2px;
    float: left;
}

.info {
    width: 100%;
}

.asidebutton {
    position: absolute;
    right: 0px;
    background-color: rgb(11, 11, 11);
    width: 200px;
    height: 50px;
    top: 20px;
}

.nav {
    width: 414px;
    height: 50px;
    background-color: rgba(255, 255, 255, 0.1);
    color: black;
    font-size: 0.6rem;
    user-select: none;
    /* 禁用文本选择 */
    overflow-x: hidden;
    /* 隐藏水平滚动条 */
    position: relative;
    /* 使其相对定位 */
}

.nav ul {
    list-style: none;
    margin: 0px;
    margin-left: 0px;
    display: flex;
    /* 使用 flexbox 布局 */
    overflow-x: auto;
    /* 允许水平滚动 */
    padding: 0;
    /* 去掉默认内边距 */
    white-space: nowrap;
    /* 不换行 */
}

.nav li {
    flex: 0 0 auto;
    /* 不允许缩小和增长 */
    margin-right: 43px;
    line-height: 50px;
}

/* 隐藏滚动条的样式 */
.nav ul::-webkit-scrollbar {
    display: none;
    /* 隐藏 Chrome 和 Safari 的滚动条 */
}

.nav ul {
    -ms-overflow-style: none;
    /* IE 和 Edge 隐藏滚动条 */
    scrollbar-width: none;
    /* Firefox 隐藏滚动条 */
}


.metemp {
    width: 100%;
    height: 100%;

    overflow: hidden;
    /* 隐藏水平滚动条 */
}




.personinfo {
    font-size: 0.5rem;
    border-radius: 30px 30px 0px 30px;
    width: 100%;
    background-color: white;
}

.backimg {
    width: 414px;
    height: 300px;
    background-color: rgba(255, 255, 255, 0.5);
    position: relative;

}

.backimg img {
    width: 414px;
    height: 300px;
}

.backimg .caidan {
    width: 150px;
    height: 50px;
    background-color: rgba(222, 56, 128, 0.5);
    position: absolute;
    right: 20px;
    top: 50px;
    z-index: 5;
}

.metransform {
    width: 414px;
    background-color: rgb(0, 0, 0);
    overflow: hidden;
    position: relative;
    right: 320px;
}





.me {
    width: 100%;
    height: 100%;
    overflow-y: auto;
    /* 允许垂直滚动 */
    overflow-x: hidden;
    /* 隐藏水平滚动条 */
}


.footer {
    background-color: black;
    color: black;
    z-index: 10;
    position: fixed;
    bottom: 0px;

}
</style>