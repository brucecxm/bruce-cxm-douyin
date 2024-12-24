<template>
    <div class="auth">
        <div class="backimg">
            <img :src="auth.backing" alt="no backimg">
            <div class="fanhui" @click="gohome">
                <label>{{ '<' }}</label>
            </div>
            <div class="touxiang">
                <img :src="auth.touimg" alt="头像">
            </div>
        </div>
        <div class="personinfo">
            <div class="authnum">
                <span>{{ auth.like }} 获赞</span>
                <span>{{ auth.follow }} 关注</span>
                <span>{{ auth.fan }} 粉丝</span>
            </div>
            <div class="city">
                <span>{{ auth.city }}</span>
            </div>
            <div class="authinfo">信息介绍</div>
            <div class="followbutt">+ 关注</div>
            <div class="videonum">{{ videonunm }} 个视频</div>
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
import { homegetVideo, homegetVideomore, homegetVideocontent, getauthinfo, getauthzzz } from "@/api/video";

export default {
    created() {
        this.userId = this.$route.params.userId;
        console.log(this.userId);
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
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" },
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" },
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" },
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" },
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" },
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" },
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" },
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" },
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" },
                { videoimgurl: "http://gips3.baidu.com/it/u=1821127123,1149655687&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280", videohref: "" }
            ]
        };
    },
    methods: {
        govideodetail(videoid) {
            this.$router.push(`/videodetail?type=auth&videoid=${videoid}`);
        },
        gohome() {
            this.$router.push('/');
        },
        fetchItemDetails(id) {
            if (id) {
                getauthzzz(id)
                    .then(result => {
                        console.log(result)
                        this.auth = result.data.auth
                        this.videobox = result.data.videobox
                    })
                    .catch(error => {
                        console.error('获取认证信息出错:', error);
                    });
            } else {
                console.error('用户 ID 无效');
            }
        }
    },

};
</script>

<style scoped>
/* 确保页面占满屏幕 */
html,
body {
    height: 100%;
    margin: 0;
    padding: 0;
}

.auth {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
    background-color: #f8f8f8;
}

.backimg {
    width: 100%;
    height: 40vh;
    position: relative;
    z-index: 1;
}

.backimg img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.fanhui {
    position: absolute;
    left: 5vw;
    top: 5vh;
    width: 40px;
    height: 40px;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    color: white;
    font-size: 1.2rem;
    cursor: pointer;
}

.touxiang {
    width: 30vw;
    height: 30vw;
    border-radius: 50%;
    overflow: hidden;
    position: absolute;
    bottom: 10px;
    left: 10px;
    border: 3px solid white;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.touxiang img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.personinfo {
    width: 100%;
    height: 30%;
    position: relative;
    background-color: white;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    z-index: 2;
    margin-top: -5vh;
    padding: 15px;
    box-sizing: border-box;
}

.authnum {
    font-size: 0.6rem;
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
}

.authnum span {
    font-weight: bold;
    color: #333;
}

.city {
    font-size: 0.6rem;
    background-color: rgba(0, 0, 0, 0.1);
    width: 15vw;
    padding: 5px;
    border-radius: 15px;
    text-align: center;
    margin-top: 10px;
}

.authinfo {
    font-size: 0.7rem;
    color: #666;
    margin-top: 15px;
}

.followbutt {
    width: 90%;
    height: 5vh;
    background-color: #fe1e1e;
    margin: 15px auto;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 30px;
    color: white;
    font-size: 0.8rem;
    cursor: pointer;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.videonum {
    font-size: 0.5rem;
    margin-top: 15px;
    color: #333;
    margin-bottom: 0px;
    position: absolute;
    bottom: 0px;
}

.uservideo {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    padding: 5px;
    box-sizing: border-box;

}

.vbox {
    width: 33%;
    height: 30vh;
    background-color: #e1e1e1;
    background-size: cover;
    background-position: center;
    border-radius: 10px;
    margin-bottom: 10px;
    position: relative;
    cursor: pointer;
    box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
}

.vbox:hover {
    opacity: 0.8;
    transition: opacity 0.3s;
}
</style>
