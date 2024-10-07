<template>
    <div class="pinglunqu">
        <button @click="sendMessage()">关闭评论区</button>
        <div class="comment" v-for="(ping, index) in receivedData.data" :key="index">
            <div class="touxiangqu">
                <div class="touxiang">
                </div>
            </div>
            <div class="xiaoxi">
                <p>name</p>
                <p>{{ ping.comment }}</p>
                <p>ip 回复 爱心</p>
                <p>展开回复</p>
            </div>
        </div>

    </div>
</template>
<script>
import { eventBus } from '../main.ts'; // 导入事件总线

export default {
    name: "Pinglunqu",
    props: {

    },
    data() {
        return {
            receivedData: {

            },// 声明一个变量来接收事件总线传递的数据
        }
    },
    methods: {
        sendMessage() {
            console.log("sssss");
            // alert("按钮被点击了！");
            eventBus.$emit('messageSent', false);

        },

    },
    created() {
        eventBus.$on('eventName', (one) => {
            console.log(one)
            this.receivedData = one
        });
    }
}
</script>

<style scoped>
.touxiangqu {
    width: 50px;
    height: 50px;
    float: left;
}

.xiaoxi {
    width: 360px;
    float: right;
    word-wrap: break-word;

}

.touxiang {
    width: 20vw;
    height: 20vw;
    border-radius: 25px;
    background-color: rgba(255, 44, 44, 0.5);
    z-index: 3;
    float: left;
}

.comment {
    width: 100vw;
    overflow: hidden;
}

.pinglunqu {
    width: 100vw;
    height: 60vh;
    background-color: white;
    border-radius: 10px 10px 0 0;
}
</style>