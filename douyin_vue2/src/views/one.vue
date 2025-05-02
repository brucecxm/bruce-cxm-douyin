<!-- <template>
    <div class="one">

        <tan-chuang class="tanchuang" v-show="receivedMessage.istanchuangshow"></tan-chuang>

        <div :class="receivedMessage.home">
            <pinglunqu class="lpinglunqu" v-show="pinglunisvisable" :updateMessage="updateMessage"></pinglunqu>

            <BaseHeader v-show="!pinglunisvisable"></BaseHeader>

            <BaseMain :updateMessage="updateMessage"></BaseMain>
            <BaseFooter v-show="!pinglunisvisable"></BaseFooter>
        </div>
    </div>
</template>
  -->
<!-- <script>
import BaseFooter from '../components/BaseFooter.vue'
import BaseHeader from '../components/BaseHeader.vue'
import BaseMain from '../components/BaseMain.vue'
import Pinglunqu from '../components/Pinglunqu.vue'
import TanChuang from "../components/TanChuang.vue"
import { homegetVideo } from '../api/video'
import { eventBus } from '../main'; // 导入全局事件总线
import { VideoIdStore } from "../stores/token"
export default {
    created() {
        eventBus.$on('message-from-one', this.handleMessage); // 订阅事件
    },
    data() {
        return {
            receivedMessage: {
                home: "home",
                istanchuangshow: false,
            },
            userID: 1,
            pinglunisvisable: false,
        }
    },
    components: {
        BaseFooter,
        BaseHeader,
        BaseMain,
        Pinglunqu,
        TanChuang

    },

    methods: {
        handleMessage(message) {
            this.receivedMessage = message; // 处理接收到的消息
        },

        updateMessage(newMessage) {
            this.pinglunisvisable = newMessage;
        }

    },


}
</script> -->
<!-- 
 
<style scoped>
.tanchuang {
    position: absolute;
    left: 0px;
}

.lpinglunqu {
    position: absolute;
    left: 0px;
    bottom: 0px;
    z-index: 10;
}


.caidan {
    background-image: url(../assets/home/菜单.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateY(20px)
}

.fangdajing {
    background-image: url(../assets/home/放大镜.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateY(20px)
}

.yinyue {
    background-image: url(../assets/home/评论.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateX(-10px)
}

.zhuanfa {
    background-image: url(../assets/home/转发.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateX(-10px)
}

.shoucang {
    background-image: url(../assets/home/收藏.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateX(-10px)
}

.pinglun {
    background-image: url(../assets/home/评论.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateX(-10px)
}

.dianzan {
    background-image: url(../assets/home/点赞.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateX(-10px)
}

.touxiang {
    background-image: url(../assets/home/点赞.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateX(-10px)
}

.jiahao {
    background-image: url(../assets/home/加号.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateY(20px)
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
}

.aside li {
    margin-left: 20px;
    margin-bottom: 55px;
}

.content {
    width: 350px;
    height: 130px;
    /* background-color: rgba(0, 0, 0, 0.5); */
    position: absolute;
    left: 0px;
    bottom: 0px;
}

.aside {
    height: 400px;
    width: 50px;
    /* background-color: rgba(0, 0, 0, 0.5); */
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

.header li {
    float: left;
    line-height: 73px;
    margin-left: 10px;
}

ul {
    left: 0px;
    position: absolute;
    list-style: none;
    /* 去除所有列表的默认标志 */
    padding: 0;
    /* 去除默认的列表项内边距 */
    margin: 0 auto;
}

.footer li {
    line-height: 66px;
}

.header {
    /* background-color: rgba(0, 0, 0, 0.5); */
    width: 100%;
    height: 70px;
    position: fixed;
    top: 0px;
    color: white;
    z-index: 2;

}

.footer {
    background-color: rgba(0, 0, 0, 0.1);
    width: 414px;
    height: 66px;
    position: fixed;
    bottom: 0px;
    color: white;
}


.main {
    background-color: rgba(0, 0, 0, 1);
    width: 100%;
    height: 830px;
    position: relative;
    color: white;
}

.main img {
    width: 414px;
    height: 830px;
}

.home {
    display: flex;
    background-color: black;
    flex-direction: column;
    justify-content: space-between;
    height: 896px;
    align-items: center;
    position: relative;

}

.homeone {
    display: flex;
    background-color: black;
    flex-direction: column;
    justify-content: space-between;
    height: 896px;
    align-items: center;
    position: relative;
    left: 330px;

}

.one {
    overflow: hidden;
}

* {
    margin: 0px;
}
</style>
 -->
