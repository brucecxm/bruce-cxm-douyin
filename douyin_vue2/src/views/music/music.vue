<template>
    <div class="music">
        <div class="header">
            <i class="fas fa-arrow-left" @click="goBack"></i>

            <!-- <label>zhuanfa</label> -->
        </div>
        <div class="musicbody">
            <div class="imgbox"><img
                    src="https://gips2.baidu.com/it/u=1651586290,17201034&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f600_800"
                    alt="" width="100%"></div>
            <div class="geming">{{ name }}创作的原神</div>
            <div class="zuozhe">
            </div>
            <div class="qu"></div>
            <div class="shoucang"></div>
        </div>
        <div class="wanfa" v-if="false">

        </div>
        <div class="videobody">
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%" @click="govideodetail(11)"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>


        </div>
    </div>
</template>
<script>
export default {
    created() {
        this.musicid = this.$route.params.musicid;
        console.log(this.musicid);
        this.fetchItemDetails(this.musicid);
    },
    data() {
        return {
            musicid: "1",
            is_wanfa: false,
            name: "歌曲作者名字",
            videobox: {
                imgurl: "https://gips2.baidu.com/it/u=1651586290,17201034&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f600_800",
                linkurl: ""
            }

        }
    },

    methods: {
        goBack() {
            this.$router.go(-1); // 返回上一页
        },
        gohome() {
            // 这里可以是跳转回主页的逻辑
            this.$router.push('/');
        },
        govideodetail(videoid) {
            //点击盒子 要导航到视频播放界面 就是要把视频的参数传上过去 就是linkurl
            //也许要写一个界面  还是前端没设计好  哪些用组件 哪些是页面
            this.$router.push(`/videodetail?type=music&videoid=${videoid}`);
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
    }
};
</script>

<style scoped>
.box {
    width: 31.2vw;
    height: 160px;
    background-color: rgba(190, 40, 40, 0.1);
    float: left;
    margin: 0.2vw;
    transform: translateX(10px);
    overflow: hidden;
}

.videobody {
    width: 100%;
    margin: 0 auto;
    overflow: hidden;
    padding: 0px;
}

.wanfa {
    height: 100px;
    background-color: rgba(0, 0, 0, 0.1);
    position: relative;

}

.header {
    padding: 10px;
    height: 40px;
    display: flex;
    justify-content: space-between;
    font-size: 0.5rem;
    margin-left: 0px;
}

.geming {
    width: 200px;
    height: 50px;
    position: absolute;
    top: 10px;
    left: 150px;
}

.imgbox {
    width: 110px;
    height: 110px;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.1);
    position: absolute;
    left: 10px;
    top: 10px;
    overflow: hidden;
}

.qu {
    width: 40vw;
    height: 6vh;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.1);
    float: left;
    position: absolute;
    bottom: 10px;
    left: 10px;
    background-image: url(../../assets/music/qu.jpg);
    background-size: cover;
}

.shoucang {
    width: 40vw;
    height: 6vh;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.1);
    float: left;
    position: absolute;
    bottom: 10px;
    right: 10px;
    background-image: url(../../assets/music/share.jpg);
    background-size: cover;

}

.musicbody {
    height: 200px;
    position: relative;
    font-size: 0.65rem;
}
</style>