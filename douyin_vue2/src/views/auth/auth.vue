<template>
    <div class="auth">
        <div class="backimg">
            <img :src="auth.backimg" alt="no backimg">
            <div class="fanhui" @click="gohome">
                <label style="font-size: 1rem;">{{ '<' }}</label>
            </div>
            <div class="touxiang">
                <img :src="auth.touimg" alt="头像">
            </div>
        </div>
        <div class="personinfo">
            <div class="authnum">
                <span>{{ auth.like }}获赞</span>
                <span>{{ auth.follow }}关注</span>
                <span>{{ auth.fan }}粉丝</span>
            </div>
            <div class="city" style="font-size: 0.6rem;background-color: rgba(0,0,0,0.1); width: 10vw;">
                <span>{{ auth.city }}</span>
            </div>
            <div class="followbutt" style="border-radius: 2vw;">+ 关注</div>
            <div class="videonum">{{ videonunm }}</div>
        </div>
        <div class="uservideo">
            <div class="vbox" v-for="(videoboxtemp, index) in videobox" :key="index"
                :style="{ backgroundImage: 'url(' + videoboxtemp.videoimgurl + ')' }"
                @click="govideodetail(videoboxtemp.videoimgurl)">
            </div>
        </div>
    </div>
</template>

<script>
import { homegetVideo, homegetVideomore, homegetVideocontent, getauthinfo } from "@/api/video"
export default {
    created() {
        // 在组件创建时获取传递的 ID
        this.userId = this.$route.params.userId;
        console.log(this.userId)
        // 你可以在这里根据 ID 请求商品详情数据
        this.fetchItemDetails(this.userId);
    },
    data() {
        return {
            userId: "",
            videonunm: "10000",
            auth: {
                like: "1",
                follow: "1",
                fan: "1",
                city: "陕西",
                touimg: "http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960",
                backimg: "http://gips2.baidu.com/it/u=1674525583,3037683813&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024"
            },
            videobox: [
                {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                },
                {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                },
                {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                }, {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                }, {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                }, {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                }, {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                }, {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                }, {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                }, {
                    videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
                    videohref: "",
                    videolike: ""
                }
            ]
        };
    },
    methods: {
        govideodetail(videoid) {
            //点击盒子 要导航到视频播放界面 就是要把视频的参数传上过去 就是linkurl
            //也许要写一个界面  还是前端没设计好  哪些用组件 哪些是页面
            this.$router.push(`/videodetail?type=auth&videoid=${videoid}`);
        },
        gohome() {
            this.$router.push('/');
        },
        fetchItemDetails(id) {
            console.log(id + "sssssss");
            if (id !== null && id !== undefined) {
                getauthinfo({ userId: id }) // 确保传递的参数是对象，并且属性名与后端相匹配
                    .then(videoArr => {
                        console.log('返回的数据:', videoArr); // 调试输出
                        this.auth = videoArr.authinfo; // 确保这些属性存在
                        this.videobox = videoArr.videobox; // 确保这些属性存在
                    })
                    .catch(error => {
                        console.error('获取视频出错:', error);
                        console.log("获取视频出错  videobox中的");
                    });
            } else {
                console.error('用户 ID 无效');
            }
        }

    }
};
</script>

<style scoped>
.auth {
    width: 100%;
    height: 100%;
}

.backimg {
    width: 100%;
    height: 30vh;
    position: relative;
    z-index: 1;
}

.backimg img {
    width: 100%;
    height: 30vh;
}

.backimg .caidan {
    width: 20vw;
    height: 5vh;
    background-color: rgba(222, 56, 128, 0.5);
    position: absolute;
    right: 10vw;
    top: 20vh;
}

.fanhui {
    left: 5vw;
    top: 5vh;
    width: 36px;
    height: 36px;
    position: absolute;
    left: 20px;
    top: 50px;
    border-radius: 18px;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    /* 使用 flexbox */
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
    font-size: 1.5rem;
    /* 调整字体大小 */
    color: white;
    /* 字体颜色 */
}


.touxiang {
    width: 30vw;
    height: 30vw;
    border-radius: 50%;
    overflow: hidden;
    position: absolute;
    bottom: 10px;
    left: 10px;
    background-color: rgb(217, 25, 25);
    /* Optional fallback color */
}

.touxiang img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 50%;
}

.personinfo {
    width: 100%;
    height: 20vh;
    position: relative;
    background-color: white;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    z-index: 2;
    margin-top: -1vh;
}

.authnum {
    font-size: 0.8rem;
}

.authnum span {
    margin: 1vw;
}

.city {
    font-size: 0.6rem;
    background-color: rgba(0, 0, 0, 0.1);
    width: 10vw;
}

.followbutt {
    width: 90vw;
    height: 5vh;
    background-color: red;
    margin: 0 auto;
    display: flex;
    /* 使用 flexbox */
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
    border-radius: 2vw;
    /* 保持圆角 */
    color: white;
    font-size: 0.7rem;
}

.videonum {
    position: absolute;
    bottom: 0;
    left: 0;
    font-size: 0.7rem;
}

.uservideo {
    width: 100%;
    overflow: hidden;
}

.vbox {
    width: 33vw;
    height: 20vh;
    background-color: rgb(217, 25, 25);
    /* Fallback color in case the image doesn't load */
    background-size: cover;
    background-position: center;
    float: left;
    margin: 0.01rem;
}
</style>
