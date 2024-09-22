<template>
    <div class="home">
        <div class='header'>
            <div class="caidan"></div>
            <ul class="nav">
                <li :class="{ active: activeIndex === 0 }" @click="selectTab(0)">经验</li>
                <li :class="{ active: activeIndex === 1 }" @click="selectTab(1)">推荐</li>
                <li :class="{ active: activeIndex === 2 }" @click="selectTab(2)">西安</li>
                <li :class="{ active: activeIndex === 3 }" @click="selectTab(3)">直播</li>
                <li :class="{ active: activeIndex === 4 }" @click="selectTab(4)">关注</li>
                <li :class="{ active: activeIndex === 5 }" @click="selectTab(5)">推荐</li>
            </ul>
            <div class="fangdajing"></div>
        </div>
    </div>
</template>

<script>
import { eventBus } from '../main'; // 导入全局事件总线
export default {
    name: 'BaseHeader',
    props: {
        customClass: {
            type: String,
            default: ""
        }
    },
    data() {
        return {
            activeIndex: 0,
            tanchuang: {
                home: "homeone",
                istanchuangshow: true,
            }
        }
    },
    methods: {
        gotanchuang() {
            // 要传一个参数给home主界面
            eventBus.$emit('message-from-one', this.tanchuang); // 发送事件和数据
        },
        selectTab(index) {
            this.activeIndex = index;
            switch (index) {
                case 0:
                    this.gofreevideo();
                    break;
                case 1:
                    this.gointroduce();
                    break;
                case 2:
                    this.goxian();
                    break;
                case 3:
                    this.goLive();
                    break;
                case 4:
                    this.goFollow();
                    break;
                case 5:
                    this.goRecommend();
                    break;
                default:
                    break;
            }
        },
        gofreevideo() {
            // Navigate to '/free-video' route
            this.$router.push('/freevideo');
        },
        gosearch() {
            this.$router.push("/search")
        },
        gointroduce() {
            console.log("Navigating to introduce...");
        },
        goxian() {
            console.log("Navigating to Xian...");
        },
        goLive() {
            console.log("Navigating to live...");
        },
        goFollow() {
            console.log("Navigating to follow...");
        },
        goRecommend() {
            console.log("Navigating to recommend...");
        }
    }
}
</script>

<style scoped>
.fangdajing {
    float: right;
    background-image: url(../assets/home/放大镜.png);
    width: 30px;
    height: 30px;
    background-size: cover;
    transform: translateY(-55px);
    background-color: black;
}

.caidan {
    background-image: url(../assets/home/菜单.png);
    width: 25px;
    height: 20px;
    background-size: cover;
    transform: translateY(25px);
    float: left;
}

li {
    display: inline-block;
    /* 显示为内联块元素 */
    line-height: 73px;
    margin-right: 25px;
    margin-left: 25px;
    cursor: pointer;
    position: relative;
    transition: color 0.3s;
}

li.active {
    font-size: large;
    /* 修改选中项的字体颜色 */
}

li.active::after {
    content: '';
    display: block;
    width: 100%;
    height: 2px;
    background-color: black;
    /* 下划线的颜色 */
    position: absolute;
    bottom: 0;
    left: 0;
    bottom: 20px;
    /* 向下移动5px */
}

.header {
    width: 100%;
    height: 70px;
    position: fixed;
    top: 0;
    background-color: black;
    color: white;
    z-index: 2;
}

.nav {
    list-style-type: none;
    /* 去除列表默认样式 */
    padding: 0;
    margin: 0;
    white-space: nowrap;
    /* 不换行 */
    overflow-x: auto;
    /* 横向滚动 */
}

/* 隐藏滚动条但保留滚动功能 */
.nav::-webkit-scrollbar {
    display: none;
    /* 隐藏滚动条 */
}

.home {
    display: flex;
    background-color: black;
    flex-direction: column;
    justify-content: space-between;
    height: 66px;
    align-items: center;
}

* {
    margin: 0;
    padding: 0;
}

/* 添加自定义样式 */
.custom-header {
    background-color: white !important;
    /* 修改背景颜色为蓝色 */
    color: black !important;
    /* 修改字体颜色为黄色 */
}
</style>
