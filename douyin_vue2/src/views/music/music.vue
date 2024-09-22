<template>
    <div class="music">
        <div class="header">
            <label @click="gohome">返回</label>
            <label>--------------------------</label>
            <label>zhuanfa</label>
        </div>
        <div class="musicbody">
            <div class="imgbox"><img src="https://tencentcdn.production.link3.cc/profile_images/1722150064686" alt=""
                    width="100%"></div>
            <div class="geming">{{ name }}创作的原神</div>
            <div class="zuozhe">
            </div>
            <div class="qu"></div>
            <div class="shoucang"></div>
        </div>
        <div class="wanfa">

        </div>
        <div class="videobody">
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%" @click="govideodetail"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>
            <div class="box"><img :src="videobox.imgurl" alt="" height="100%"></div>


        </div>
    </div>
</template>
<script>
export default {
    data() {
        return {
            name: "歌曲作者名字",
            videobox: {
                imgurl: "https://tencentcdn.production.link3.cc/profile_images/1722150064686",
                linkurl: ""
            }

        }
    },

    methods: {
        gohome() {
            // 这里可以是跳转回主页的逻辑
            this.$router.push('/');
        },
        govideodetail() {
            //点击盒子 要导航到视频播放界面 就是要把视频的参数传上过去 就是linkurl
            //也许要写一个界面  还是前端没设计好  哪些用组件 哪些是页面
            this.$router.push('/videodetail');

        }
    }
};
</script>

<style scoped>
.box {
    width: 100px;
    height: 160px;
    background-color: rgba(190, 40, 40, 0.1);
    float: left;
    margin: 10px;
    transform: translateX(10px);
    overflow: hidden;
}

.videobody {
    width: 380px;
    margin: 0 auto;
    overflow: hidden;
}

.wanfa {
    height: 100px;
    background-color: rgba(0, 0, 0, 0.1);
    position: relative;

}

.header {
    height: 40px;
}

.geming {
    width: 200px;
    height: 50px;
    background-color: rgba(0, 0, 0, 0.1);
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
}

.qu {
    width: 180px;
    height: 50px;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.1);
    float: left;
    position: absolute;
    bottom: 10px;
    left: 10px;
    background-image: url(../../assets/music/qu.jpg);

}

.shoucang {
    width: 180px;
    height: 50px;
    border-radius: 20px;
    background-color: rgba(0, 0, 0, 0.1);
    float: left;
    position: absolute;
    bottom: 10px;
    right: 10px;
    background-image: url(../../assets/music/share.jpg);

}

.musicbody {
    height: 200px;
    position: relative;
}
</style>