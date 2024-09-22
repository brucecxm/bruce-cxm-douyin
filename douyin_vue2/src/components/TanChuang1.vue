<template>
    <div class="tanchuang">
        <button @click="fanhui">返回</button>
        <div class="logo"></div>
        <div class="changyong">
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
        </div>
        <div class="shenghuo">
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
            <div class="box"></div>
        </div>
    </div>
</template>

<script>
import { eventBus } from '../main'; // 导入全局事件总线
export default {
    name: 'Tanchuang',

    data() {
        return {
            tanchuang: {
                home: "home",
                istanchuangshow: false,

            }
        }
    },
    methods: {
        fanhui() {
            // 要传一个参数给home主界面
            eventBus.$emit('message-from-one', this.tanchuang); // 发送事件和数据
        }
    }
}
</script>



<style scoped>
.box {
    width: 80px;
    height: 80px;
    background-color: red;
    margin: 10px;
    border-radius: 10px;
}

.shenghuo {
    width: 300px;
    height: 300px;
    margin: 0 auto;
    background-color: white;
    border-radius: 10px;
    margin: 10px;
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
}

.changyong {
    width: 300px;
    height: 300px;
    margin: 0 auto;
    background-color: white;
    border-radius: 10px;
    margin: 10px;
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
}

.logo {
    width: 100px;
    height: 100px;
    background-image: url(../assets/douyin.png);
    position: relative;
    background-size: cover;

}

.tanchuang {
    width: 330px;
    height: 896px;
    z-index: 5;
    display: flex;
    flex-direction: column;
    background-color: rgba(0, 0, 0, 0.1);
}
</style>

