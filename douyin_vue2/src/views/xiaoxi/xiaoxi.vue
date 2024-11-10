<template>
    <div class="xiaoxi">


        <caidan :style="caidancss"></caidan>

        <div class="forposition">
            <div class="header">
                <img src="../../assets/xiaoxi/header/1.png" alt="" @click="showcaidanone">
                <img src="../../assets/xiaoxi/header/2.png" alt="">
                <img src="../../assets/xiaoxi/header/3.png" alt="">

            </div>
        </div>

        <!-- <div class="navbody">
            <ul class="nav">
                <li><img :src=userinfo.avatar alt="" style="width: 200px;height: 100px; "></li>



            </ul>
        </div> -->
        <div class="commentbody">
            <!-- <div class="comment" @click="goxiaodetail">
                <div class="touxiang">
                    <img :src=userinfo.avatar alt="" style="width: 100px;height: 100px;">
                </div>
                <div class="neirong">
                    <h4>{{ userinfo.username }}</h4>
                    <h6>{{ userinfo.latermessage }}</h6>
                </div>
            </div> -->

            <img src="../../assets/xiaoxi/main/1.png" alt="" style="width: 100%;" @click="goxiaodetail('brucebbb')">
            <img src="../../assets/xiaoxi/main/2.png" alt="" style="width: 100%; " @click="goxiaodetail('bruceccc')">
            <img src="../../assets/xiaoxi/main/2.png" alt="" style="width: 100%;">
            <img src="../../assets/xiaoxi/main/3.png" alt="" style="width: 100%;">
            <img src="../../assets/xiaoxi/main/3.png" alt="" style="width: 100%;">
            <img src="../../assets/xiaoxi/main/3.png" alt="" style="width: 100%;">
            <img src="../../assets/xiaoxi/main/4.png" alt="" style="width: 100%;">
            <img src="../../assets/xiaoxi/main/5.png" alt="" style="width: 100%;">
            <img src="../../assets/xiaoxi/main/5.png" alt="" style="width: 100%;">
            <img src="../../assets/xiaoxi/main/5.png" alt="" style="width: 100%;">
            <img src="../../assets/xiaoxi/main/5.png" alt="" style="width: 100%;">
        </div>

        <footer-vue class="footer"></footer-vue>
    </div>
</template>
<script>
import caidan from '@/components/caidan.vue';

import footerVue from '@/components/footer.vue'
export default {
    components: {
        footerVue, caidan
    },
    data() {
        return {
            userinfo: {
                username: "前端默认的数据 用户名",
                avatar: "//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-i-0813_fdbb0995a81d4bccaac82bbf3d3a6470.jpeg?from=2956013662",
                latermessage: "前端默认的数据 最新一条聊天记录"
            }, caidancss: {
                position: 'absolute',
                right: '100vw',
                bottom: '0px',
                transition: 'right 0.5s ease',
                height: '100vh', /* 控制位置变化的动画 */


            },
        }

    },
    methods: {
        goxiaodetail(id) {
            this.$router.push({ path: `/xiaodetail/${id}` })
        },

        showcaidanone() {
            // 更新 caidancss 的位置
            this.caidancss.right = this.caidancss.right === "100vw" ? "20vw" : "100vw";

            // 使用 Vue 的 $set 方法来触发响应式系统检测到 transform 属性变化
            this.$set(this.boxvideocss, 'transform', this.boxvideocss.transform === "translateX(0px)" ? "translateX(80vw)" : "translateX(0px)");
        }
    }

}
</script>

<style scoped>
.commentbody {
    margin-top: 5vh;
    overflow-y: auto;
    /* 允许垂直滚动 */
    height: calc(100vh - 5vh - 50px);
    /* 根据 header 和 footer 的高度调整 */
}

.xiaoxi {
    overflow: hidden;

}

/* 隐藏所有滚动条 */
.commentbody::-webkit-scrollbar {
    display: none;
    /* 隐藏滚动条 */
}

::v-deep .jiahao {
    background-image: url(../../assets/home/加号黑.png);
}

.header {
    background-color: white;
    width: 100%;
    height: 5vh;
    display: flex;
    flex-wrap: nowrap;
    justify-content: space-between;
    z-index: 10;
    position: fixed;
    top: 0;
}

.fas {
    flex-direction: row;
    align-content: center;
}

.touxiang {
    width: 50px;
    height: 50px;
    border-radius: 25px;
    background-color: rgba(213, 17, 17, 0.1);
    float: left;
    margin-right: 20px;
    overflow: hidden;
}

.comment {
    margin-top: 10px;
}

ul {
    list-style: none;
    overflow: scroll;
    white-space: nowrap;
    /* 不换行 */
    overflow-x: auto;
    /* 横向滚动 */
    width: 414px;
    margin-left: 0px;
    transform: translateX(-50px);
}

.navbody {
    width: 414px;
}

.nav::-webkit-scrollbar {
    display: none;
    /* 隐藏滚动条 */
}

li {
    display: inline-block;
    margin-left: 20px;
    width: 80px;
    height: 80px;
    border-radius: 40px;
    background-color: rgba(0, 0, 0, 0.1);
    overflow: hidden;
}

.footer {
    background-color: black;
    color: black;
    z-index: 10;
    position: fixed;
    bottom: 0px;
}

::v-deep .footer {
    background-color: black;
    color: black;
}
</style>