<template>
    <div class="metemp">
        <footer-vue class="footer"></footer-vue>

        <div class="me">
            <memenu v-show="receivedMessage.ismemenushow" class="memenu"></memenu>
            <div :class="['me-content', receivedMessage.me]">
                <div class="backimg">
                    <img :src="User.data.backimg" alt="no backimg" />
                    <div class="caidan" @click="openmenu"></div>
                    <div class="userinfo">
                        <div class="touxiang">
                            <img :src="User.data.userPic" alt="" style="width: 26vw;height: 36vw;" />
                        </div>
                        <div class="name" style="color: white;">
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
            receivedMessage: {
                me: "me",
                ismemenushow: false,
            },
            User: {
                data: {
                    nickname: "默认名字",
                    userPic: "//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-i-0813c001_oYCBO7DFFAYxR4jFA7gwAEqBTmfEIGAfANeokE.jpeg?from=2956013662",
                    backimg: "//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-i-0813c001_ooSy9lAm2G5DAfrLAAQAjO0bp7lg4eInAaCCGX.jpeg?from=2956013662",
                    jieshao: "自我介绍",
                    age: "年龄",
                    location: "城市",
                    collegt: "学校"
                },
            },
            navvideo: [oneImg, twoImg, threeImg, fourImg],
            mianvideoarr: [mainvideo, mainvideo, mainvideo, mainvideo]
        };
    },
    components: { footerVue, Memenu },
    methods: {
        openmenu() {
            this.receivedMessage.ismemenushow = true;
            this.receivedMessage.me = "metransform";
            this.$nextTick(() => {
                this.$el.querySelector('.memenu').classList.add('active');
            });
        },
        closemenu() {
            this.receivedMessage.ismemenushow = false;
            this.receivedMessage.me = "";
            this.$nextTick(() => {
                this.$el.querySelector('.memenu').classList.remove('active');
            });
        },
        fetchData(type) {
            console.log(type);
        },
    },
    mounted() {
        const usertoken = useTokenStore();
        getmeinfo(usertoken.data.message);
    }
};
</script>

<style scoped>
.name {
    transform: translateY(2vh);
}

/* General Styles */
.metemp {
    width: 100%;
    height: 100%;
    overflow: hidden;
    position: relative;
}

/* Main content area */
.me {
    width: 100%;
    height: 100%;
    overflow-y: auto;
    position: relative;
    transition: transform 0.5s ease-in-out;
}

/* Sidebar menu (memenu) */
.memenu {
    position: absolute;
    top: 0;
    right: -80%;
    width: 80%;
    height: 100%;
    transition: right 0.5s ease-in-out;
    z-index: 10;
}

.memenu.active {
    right: 0;
}

/* Content area when menu is active */
.me-content {
    transition: transform 0.5s ease-in-out;
}

.me-content.metransform {
    transform: translateX(80%);
}

/* User info */
.userinfo {
    position: absolute;
    top: 50px;
    width: 60%;
}

.touxiang {
    width: 26vw;
    height: 26vw;
    border-radius: 13vw;
    overflow: hidden;
    border: 3px solid white;
    display: inline-block;
    transform: translateX(10vw);
    transform: translateY(3vh);
}

/* Nav bar */
.nav ul {
    list-style: none;
    display: flex;
    overflow-x: auto;
    padding: 0;
    white-space: nowrap;
    font-size: 0.6rem;
}

.nav li {
    margin-right: 43px;
    line-height: 50px;
}

/* Remove scrollbars */
.nav ul::-webkit-scrollbar {
    display: none;
}

.nav ul {
    -ms-overflow-style: none;
    scrollbar-width: none;
}

/* Video thumbnails */
.uvideo {
    width: 48%;
    height: 40vh;
    background-color: rgba(0, 0, 0, 0.1);
    margin: 2px;
    float: left;
}

/* Background image */
.backimg {
    width: 100%;
    height: 35vh;
    background-color: rgba(255, 255, 255, 0.5);
    position: relative;
    overflow: hidden;

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

/* User info text */
.personinfo {
    font-size: 0.5rem;
    border-radius: 30px 30px 0px 30px;
    width: 100%;
    background-color: white;
}

/* Footer */
.footer {
    background-color: black;
    color: black;
    position: fixed;
    bottom: 0;
    width: 100%;
    z-index: 10;
}

/* Other utility styles */
.xuanxiang {
    min-width: 80px;
    height: 80px;
    margin-right: 10px;
    margin-top: 10px;
}

.infonav {
    display: flex;
    flex-shrink: 0;
    overflow-x: auto;
    white-space: nowrap;
    overflow-x: hidden;
    overflow-y: hidden;
}

span {
    background-color: rgba(137, 8, 211, 0.1);
    margin-left: 10px;
}
</style>
