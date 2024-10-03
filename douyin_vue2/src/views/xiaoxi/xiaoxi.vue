<template>
    <div class="xiaoxi">
        <div class="header">
            <i class="fas fa-caret-left"></i>

            <i class="fas fa-camera"></i>

            <i class="fas fa-search"></i>

        </div>
        <div class="navbody">
            <ul class="nav">
                <li><img :src=userinfo.avatar alt="" style="width: 200px;height: 100px; ">
                </li>


            </ul>
        </div>
        <div class="commentbody">
            <div class="comment" @click="goxiaodetail">
                <div class="touxiang">
                    <img :src=userinfo.avatar alt="" style="width: 100px;height: 100px;">
                </div>
                <div class="neirong">
                    <h4>{{ userinfo.username }}</h4>
                    <h6>{{ userinfo.latermessage }}</h6>
                </div>
            </div>


        </div>
        <footer-vue class="footer"></footer-vue>
    </div>
</template>
<script>
import footerVue from '@/components/footer.vue'
export default {
    components: {
        footerVue
    },
    data() {
        return {
            userinfo: {
                username: "前端默认的数据 用户名",
                avatar: "//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-i-0813_fdbb0995a81d4bccaac82bbf3d3a6470.jpeg?from=2956013662",
                latermessage: "前端默认的数据 最新一条聊天记录"
            }
        }

    },
    methods: {
        goxiaodetail() {
            this.$router.push("/xiaodetail")
        }
    }

}
</script>

<style scoped>
::v-deep .footer {
    background-color: white;
    color: black;

}

::v-deep .jiahao {
    background-image: url(../../assets/home/加号黑.png);

}

.header {
    height: 50px;
    background-color: rgba(0, 0, 0, 0.1);
    font-size: 30px;
    line-height: 50px;
    display: flex;
    flex-wrap: nowrap;
    justify-content: space-between
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
    position: absolute;
    bottom: 0px;

}
</style>