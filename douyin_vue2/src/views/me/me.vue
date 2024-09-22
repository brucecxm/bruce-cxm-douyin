<template>
    <div class="me">
        <memenu v-show="receivedMessage.ismemenushow" class="memenu"></memenu>
        <div :class="receivedMessage.me">
            <div class="backimg">
                <img :src=User.data.backimg alt=" no backimg">
                <div class="caidan" @click="openmenu"></div>
            </div>
            <div class="personinfo">
                <div class="touxiang"><img :src="User.data.userPic" alt=""></div>
                <div class="name" style="transform: translateY(-150px);">
                    <h1>{{ User.data.nickname }}</h1>
                </div>
                <div class="info">
                    <p>点击添加介绍 让大家认识你</p>
                    <span>21岁</span>
                    <span>陕西西安</span>
                    <span>添加学校标签</span>
                    <div class="infonav">
                        <div class="xuanxiang" v-for="(image, index) in navvideo" :key="index">
                            <img :src="image" alt="Image">
                            <!-- <img :src="image" alt="Image" style="width: 100px;"> -->
                        </div>
                    </div>
                    .
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

                <div class="uvideo" v-for="(image, index) in nav" :key="index">
                    <img :src="image" alt="Image">
                    <!-- <img :src="image" alt="Image" style="width: 100px;"> -->
                </div>


            </div>
            <base-footer></base-footer>
        </div>

    </div>
</template>

<script>
import BaseFooter from '../../components/BaseFooter.vue'
import Memenu from "../../components/memenu.vue"
import { userInfoService } from '../../api/user';
import oneImg from '@/assets/me/one.jpg';
import twoImg from '@/assets/me/two.jpg';
import threeImg from '@/assets/me/three.jpg';
import fourImg from '@/assets/me/four.jpg';
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
                },
                message: ""
            },
            nav: [oneImg, twoImg, threeImg, fourImg],
            navvideo: [oneImg, twoImg, threeImg, fourImg]

        }
    },
    components: {
        BaseFooter, Memenu
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

}
</script>


<style scoped>
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
    width: 200px;
    height: 350px;
    background-color: rgba(0, 0, 0, 0.1);
    margin: 2px;
    float: left;
}

.info {
    width: 414px;
    height: 150px;
    position: absolute;
    top: 150px;
}

.asidebutton {
    position: absolute;
    right: 0px;
    background-color: rgb(11, 11, 11);
    width: 200px;
    height: 50px;
    top: 20px;
}

.touxiang {
    width: 130px;
    height: 130px;
    border-radius: 800px;
    background-color: rgb(217, 25, 25);
    overflow: hidden;
    transform: translateY(-150px);
    border: 3px solid white;
}

.uservideo {
    width: 414px;
}

.nav {
    width: 414px;
    height: 50px;
    background-color: rgba(255, 255, 255, 0.1);
    color: black;
    font-size: 25px;
    user-select: none;
    /* 禁用文本选择 */
}

.nav ul {
    list-style: none;
    margin: 0px;
    margin-left: 0px;
}

.nav li {
    float: left;
    margin-right: 43px;
    transform: translateX(-4px);
    line-height: 50px;
}


.personinfo {
    border-radius: 30px 30px 0px 30px;
    width: 414px;
    height: 350px;
    background-color: white;
    position: relative;
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
    width: 414px;
    background-color: white;
    overflow: hidden;
}
</style>