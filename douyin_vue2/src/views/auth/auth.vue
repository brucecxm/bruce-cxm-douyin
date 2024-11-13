<template>
    <div class="auth">
        <div class="backimg">
            <img :src="auth.backimg" alt="no backimg">
            <div class="caidan"></div>
            <div class="fanhui" @click="gohome"></div>
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
            <div class="followbutt" style="border-radius: 2vw;"></div>
            <div class="videonum">{{ videonunm }}</div>
        </div>
        <div class="uservideo">
            <div class="vbox" v-for="(videoboxtemp, index) in videobox" :key="index"
                :style="{ backgroundImage: 'url(' + videoboxtemp.videoimgurl + ')' }">
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
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
                }
            ]
        };
    },
    methods: {
        gohome() {
            this.$router.push('/');
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
    width: 50px;
    height: 50px;
    position: absolute;
    left: 20px;
    top: 50px;
    background-color: red;
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
    width: 80vw;
    height: 5vh;
    background-color: red;
    margin: 0 auto;
}

.videonum {
    position: absolute;
    bottom: 0;
    left: 0;
    padding: 0.5rem;
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
