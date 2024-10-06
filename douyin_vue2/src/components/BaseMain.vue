<template>
    <div class="home">

        <div class="main">
            <div class="content">
                <h2>{{ videoarr.data[videonum].nickname }}</h2>
                <p>{{ videoarr.data[videonum].videoComment }}</p>
            </div>
            <div class="aside">
                <ul>
                    <li class="touxiang" @click="goauth">
                        <img :src="videoarr.data[videonum].avatar" alt="noimg" style="width: 60px;height: 60px;">
                    </li>
                    <li class="dianzan" @click="goLogin">
                        <p class="num">{{ behavior.like }}</p>
                    </li>

                    <li class="pinglun" @click="sendMessageToParent">
                        <p class="num">{{ behavior.pingnum }}</p>
                    </li>
                    <li class="shoucang">
                        <p class="num">{{ behavior.collection }}</p>
                    </li>
                    <li class="zhuanfa">
                        <p class="num">{{ behavior.share }}</p>
                    </li>
                    <li class="yinyue" @click="gomusic">
                        <img :src="videoarr.data[videonum].musicAvatar" alt="noimg" style="width: 60px;height: 60px;">
                    </li>
                </ul>
            </div>
            <video :src="videoarr.data[videonum].videoUrl" ref="videoEl" preload="true" loop x5-video-player-type="h5-page"
                x5-video-player-fullscreen="false" webkit-playsinline="true" x5-playsinline="true" playsinline="true"
                autoplay @touchstart="handleTouchStart" @touchmove="handleTouchMove" @touchend="handleTouchEnd">
                <p>您的浏览器不支持 video 标签。</p>
            </video>
        </div>
    </div>
</template>
   
<script>
import { homegetVideo, homegetVideomore, homegetVideocontent } from "../api/video.js"
import { eventBus } from '../main'; // 根据实际的路径导入事件总线
import { VideoIdStore } from "../stores/token"
export default {
    props: {
        updateMessage: {
            type: Function,
            required: true,
        },
    },

    name: "BaseMain",
    data() {
        return {
            videoarr: {
                code: "0",
                data: [
                    {
                        authId: "101",
                        musicId: "1001",
                        videoId: "1",
                        videoUrl: "https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH",
                        avatar: "https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662",
                        musicAvatar: "https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662",
                        nickname: "前端实力昵称",
                        videoComment: "不是后端传过来的数据 前端中默认的数据"
                    }
                ],
                msg: "执行成功"
            },

            videonum: 0,
            startY: 0,
            endY: 0,
            //评论数据 暂时不是后端传过来的  视频数据是后端传过来的
            one: {
                code: "",
                data: [],
                msg: ""
            },
            // pingnum: "",

            behavior: {
                pingnum: 0,
                like: 0,
                collection: 0,
                share: 0

            }

        };
    },
    created() {
        console.log("created: 实例创建完成后调用");
    },
    methods: {
        goLogin() {
            // Navigate to '/free-video' route
            this.$router.push('/login');
        },
        goauth() {
            this.$router.push('/auth');
        },
        gomusic() {
            // Navigate to '/free-video' route
            this.$router.push('/music');
        },


        sendMessageToParent() {
            this.updateMessage(true);
            const videoIdStore = VideoIdStore();
            videoIdStore.setVideoId(this.videoarr.data[this.videonum].videoId);

            console.log("Video ID: ", videoIdStore.getVideoId); // 打印 videoId
            const params = { videoId: videoIdStore.getVideoId }
            homegetVideocontent(params).then(videoArr => {
                this.behavior.pingnum = videoArr.data.length

                this.one = videoArr
                console.log("ssdsdsd", videoArr)
                eventBus.$emit('eventName', this.one);
            }).catch(error => {
                console.error('获取视频出错:', error);
                // 可以适当处理错误
            });
            //把评论数据传给评论组件
            // 在需要发送数据的地方触发事件，并传递数据


        },



        handleTouchStart(event) {
            this.startY = event.touches[0].clientY;
            const videoIdStore = VideoIdStore();
            videoIdStore.setVideoId(this.videoarr.data[this.videonum].videoId);
            console.log("Video ID: ", videoIdStore.getVideoId); // 打印 videoId
        },
        handleTouchMove(event) {
            this.endY = event.touches[0].clientY;
        },
        handleTouchEnd() {
            const deltaY = this.endY - this.startY;
            if (deltaY > 30) {
                this.handleSwipeDown();
            } else if (deltaY < -30) {
                this.handleSwipeUp();
            }
        },
        handleSwipeUp() {

            if (this.videonum < this.videoarr.data.length - 1) {
                this.videonum++;
            } else {
                this.videonum = 0; // 循环播放
                homegetVideomore().then(videoArr => {
                    this.videoarr = videoArr;
                }).catch(error => {
                    console.error('出错:', error);
                    // 可以适当处理错误
                });
            }
            const videoIdStore = VideoIdStore();
            videoIdStore.setVideoId(this.videoarr.data[this.videonum].videoId);

            console.log("Video ID: ", videoIdStore.getVideoId); // 打印 videoId
            const params = { videoId: videoIdStore.getVideoId }
            homegetVideocontent(params).then(videoArr => {
                this.behavior.pingnum = videoArr.data.length

                this.one = videoArr
                console.log("ssdsdsd", videoArr)
                eventBus.$emit('eventName', this.one);
            }).catch(error => {
                console.error('获取视频出错:', error);
                // 可以适当处理错误
            });
            //把评论数据传给评论组件
            // 在需要发送数据的地方触发事件，并传递数据

            this.$refs.videoEl.load();
            this.$refs.videoEl.play();
        },
        handleSwipeDown() {
            if (this.videonum > 0) {
                this.videonum--;
            } else {
                this.videonum = this.videoarr.data.length - 1; // 循环播放
            }
            const videoIdStore = VideoIdStore();
            videoIdStore.setVideoId(this.videoarr.data[this.videonum].videoId);

            console.log("Video ID: ", videoIdStore.getVideoId); // 打印 videoId
            const params = { videoId: videoIdStore.getVideoId }
            homegetVideocontent(params).then(videoArr => {
                this.behavior.pingnum = videoArr.data.length

                this.one = videoArr
                console.log("ssdsdsd", videoArr)
                eventBus.$emit('eventName', this.one);
            }).catch(error => {
                console.error('获取视频出错:', error);
                // 可以适当处理错误
            });
            //把评论数据传给评论组件
            // 在需要发送数据的地方触发事件，并传递数据

            this.$refs.videoEl.load();
            this.$refs.videoEl.play();
        },
    },
    mounted() {



        // 自动发送请求给后端
        homegetVideo().then(videoArr => {
            console.log(videoArr)
            this.videoarr = videoArr;
        }).catch(error => {
            console.error('获取视频出错:', error);
            // 可以适当处理错误
        });

        //要把生命周期吃透  才能解决此问题   这里是因为videoid这里获取不到  导致第一个页面无法请求到正确的评论数
        //要把生命周期吃透  才能解决此问题   这里是因为videoid这里获取不到  导致第一个页面无法请求到正确的评论数
        //要把生命周期吃透  才能解决此问题   这里是因为videoid这里获取不到  导致第一个页面无法请求到正确的评论数
        //要把生命周期吃透  才能解决此问题   这里是因为videoid这里获取不到  导致第一个页面无法请求到正确的评论数
        //要把生命周期吃透  才能解决此问题   这里是因为videoid这里获取不到  导致第一个页面无法请求到正确的评论数
        //要把生命周期吃透  才能解决此问题   这里是因为videoid这里获取不到  导致第一个页面无法请求到正确的评论数
        //要把生命周期吃透  才能解决此问题   这里是因为videoid这里获取不到  导致第一个页面无法请求到正确的评论数
        //要把生命周期吃透  才能解决此问题   这里是因为videoid这里获取不到  导致第一个页面无法请求到正确的评论数
        // const videoIdStore = VideoIdStore();
        // videoIdStore.setVideoId(this.videoarr.data[this.videonum].videoId);
        // const params = { videoId: videoIdStore.getVideoId }
        // homegetVideocontent(params).then(videoArr => {
        //     this.pingnum = videoArr.data.length
        //     this.one = videoArr
        //     console.log("ssdsdsd", videoArr)
        // }).catch(error => {
        //     console.error('获取视频出错:', error);
        //     // 可以适当处理错误
        // });





    },
    beforeDestroy() {
        this.$refs.videoEl.pause();
    }


};
</script>
  
<style scoped>
.num {
    transform: translate(-2px, 10px);
    text-align: center;
}


.caidan {
    background-image: url(../assets/home/菜单.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateY(20px);
}

.fangdajing {
    background-image: url(../assets/home/放大镜.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateY(20px);
}

.yinyue {
    width: 60px;
    height: 60px;
    border-radius: 30px;
    background-size: cover;
    transform: translateX(-35px);
    background-color: rgb(210, 27, 27);
    overflow: hidden;
}

.zhuanfa {
    background-image: url(../assets/home/转发.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateX(-20px);
}

.shoucang {
    background-image: url(../assets/home/收藏.png);
    width: 31px;
    height: 30px;
    background-size: cover;
    transform: translateX(-20px);
}

.pinglun {
    background-image: url(../assets/home/评论.png);
    width: 31px;
    height: 30px;
    background-size: cover;
    transform: translateX(-20px);
}

.dianzan {
    background-image: url(../assets/home/点赞.png);
    width: 33px;
    height: 30px;
    background-size: cover;
    transform: translateX(-20px);
}

.touxiang {
    width: 60px;
    height: 60px;
    border-radius: 30px;
    background-size: cover;
    transform: translateX(-40px);
    background-color: rgb(180, 23, 23);
    overflow: hidden;
    border: solid white 3px;
}

video {
    height: 830px;
    width: 414px;
}

.aside ul {
    margin: 0px;
    position: absolute;
    left: 0px;
    top: 0px;
    z-index: 2;
}

.aside li {
    margin-left: 20px;
    margin-bottom: 40px;
}

.content {
    width: 350px;
    height: 130px;
    position: absolute;
    left: 0px;
    bottom: 0px;
}

.aside {
    height: 400px;
    width: 50px;
    position: absolute;
    right: 0px;
    bottom: 120px;
}

li {
    float: left;
    line-height: 73px;
    margin-right: 25px;
    margin-left: 25px;
}

ul {
    left: 0px;
    position: absolute;
    list-style: none;
    padding: 0;
    margin: 0 auto;
}

.main {
    background-color: rgba(0, 0, 0, 1);
    width: 100%;
    height: 830px;
    position: relative;
    color: white;
}

.home {
    display: flex;
    background-color: black;
    flex-direction: column;
    justify-content: space-between;
    height: 896px;
    align-items: center;
}

* {
    margin: 0px;
}
</style>
