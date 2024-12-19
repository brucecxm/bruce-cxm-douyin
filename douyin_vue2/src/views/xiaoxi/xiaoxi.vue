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

        <div class="commentbody">
            <div class="comment" @click="goxiaodetail" v-for="(userinfo, index ) in userinfos" :key="index">
                <div class="touxiang">
                    <img :src=userinfo.avatar alt="" />
                </div>
                <div class="neirong">
                    <span class="username">{{ userinfo.username }}</span>
                    <span class="message">{{ userinfo.latermessage }}</span>
                </div>
            </div>
        </div>

        <footer-vue class="footer"></footer-vue>
    </div>
</template>

<script>
import caidan from '@/components/caidan.vue';
import footerVue from '@/components/footer.vue';

export default {
    components: {
        footerVue, caidan
    },
    data() {
        return {
            userinfos: [
                {
                    username: "互动消息",
                    avatar: "//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-i-0813_fdbb0995a81d4bccaac82bbf3d3a6470.jpeg?from=2956013662",
                    latermessage: "前端默认的数据 最新一条聊天记录"
                },
                {
                    username: "张三",
                    avatar: "//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-i-0813_fdbb0995a81d4bccaac82bbf3d3a6470.jpeg?from=2956013662",
                    latermessage: "前端默认的数据 最新一条聊天记录"
                },
            ],
            caidancss: {
                position: 'absolute',
                right: '100vw',
                bottom: '0px',
                transition: 'right 0.5s ease',
                height: '100vh',
            },
        };
    },
    methods: {
        goxiaodetail(id) {
            this.$router.push({ path: `/xiaodetail/${id}` });
        },
        showcaidanone() {
            this.caidancss.right = this.caidancss.right === "100vw" ? "20vw" : "100vw";
            this.$set(this.boxvideocss, 'transform', this.boxvideocss.transform === "translateX(0px)" ? "translateX(80vw)" : "translateX(0px)");
        }
    }
}
</script>

<style scoped>
.xiaoxi {
    overflow: hidden;
    font-family: 'Arial', sans-serif;
    background-color: #f4f6f9;
    color: #333;
    min-height: 100vh;
}

.header {
    background-color: #fff;
    width: 100%;
    height: 50px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 10;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.header img {
    height: 30px;
    cursor: pointer;
}

.commentbody {
    margin-top: 60px;
    overflow-y: auto;
    height: calc(100vh - 60px - 50px);
    /* Adjust based on header and footer height */
    padding: 10px;
}

.comment {
    display: flex;
    align-items: center;
    background-color: #fff;
    border-radius: 8px;
    margin-bottom: 10px;
    padding: 10px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: all 0.3s ease;
}

.comment:hover {
    background-color: #f9f9f9;
}

.touxiang img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    object-fit: cover;
}

.neirong {
    margin-left: 15px;
    flex: 1;
}

.username {
    font-weight: bold;
    color: #333;
    font-size: 16px;
}

.message {
    display: block;
    color: #777;
    font-size: 14px;
    margin-top: 5px;
}

.footer {
    background-color: #222;
    color: #fff;
    z-index: 10;
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    padding: 10px 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
}

.footer-vue {
    display: flex;
    justify-content: center;
    align-items: center;
}

footer-vue img {
    height: 20px;
    margin: 0 10px;
}

::v-deep .jiahao {
    background-image: url(../../assets/home/加号黑.png);
}

/* Hide scrollbar */
.commentbody::-webkit-scrollbar {
    display: none;
}
</style>