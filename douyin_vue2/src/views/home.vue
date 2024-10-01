<template>
    <div class="homecontain">
        <caidan :style="caidancss" :onChildEvent="showcaidanone"></caidan>

        <div class="home" :style="boxvideocss">
            <div class="headercontain">
                <!-- <button @click="showcaidanone"></button> -->

                <header-vue class="header" :activeIndex="activeIndex" @update:index="updateActiveIndex">
                </header-vue>
            </div>



            <!-- 容器div，包含鼠标和触摸事件监听 -->
            <div class="videomain" @mousedown="startDrag" @mouseup="stopDrag" @mousemove="drag" @mouseleave="stopDrag"
                @touchstart="startDrag" @touchend="stopDrag" @touchmove="drag">
                <!-- 循环渲染每个box，设置样式 -->
                <div v-for="( box, index ) in  boxes " :key="index" class="box"
                    :style="{ left: box.left * 0.1 + 'vw', backgroundColor: box.color, width: boxWidth * 0.1 + 'vw', height: boxHeight + 'vh' }">
                    <!-- <div class="videobox"><videobox-vue></videobox-vue></div> -->
                    <div class="videobox"><all-box-vue :boxtype="boxes[index].boxtest"></all-box-vue></div>
                </div>
            </div>


            <footer-vue></footer-vue>
        </div>
    </div>
</template>
<script>
import caidan from '../components/caidan.vue';
import AllBoxVue from '@/components/AllBox.vue'
import footerVue from '../components/footer.vue'
import headerVue from '../components/header.vue'
import videoboxVue from '../components/videobox.vue'
export default {
    data() {
        return {
            isDragging: false, // 是否正在拖动的标志
            startX: 0, // 拖动的起始X位置
            currentX: 0, // 当前的X位置
            boxWidth: 1000, // 每个box的宽度
            activeIndex: 0, // 默认高亮推荐项
            boxHeight: 95,
            boxes: [], // box的数据，包括颜色和左边距

            caidancss: {
                position: 'absolute',
                right: '100vw',
                bottom: '0px',
                transition: 'right 0.5s ease',
                height: '100vh', /* 控制位置变化的动画 */


            },
            boxvideocss: {
                transform: 'translateX(0px)',
                overflow: 'hidden',
                transition: 'transform 0.5s ease' /* 控制位置变化的动画 */
            }
        }
    },

    components: {
        videoboxVue,
        headerVue,
        footerVue,
        AllBoxVue,
        caidan
    },
    created() {
        // 初始化 boxes 的数据，包括颜色和左边距
        this.boxes = [
            { color: "red", left: 0, boxtest: "推荐" },
            { color: "blue", left: this.boxWidth, boxtest: "同城" },
            { color: "yellow", left: 2 * this.boxWidth, boxtest: "关注" },
            { color: "pink", left: 3 * this.boxWidth, boxtest: "直播" },
            { color: "pink", left: 4 * this.boxWidth, boxtest: "同城" },
            { color: "pink", left: 5 * this.boxWidth, boxtest: "同城" },
            { color: "pink", left: 6 * this.boxWidth, boxtest: "同城" },
            { color: "pink", left: 7 * this.boxWidth, boxtest: "同城" },
            { color: "pink", left: 8 * this.boxWidth, boxtest: "同城" },
        ];

    },
    methods: {
        updateActiveIndex(index) {
            this.activeIndex = index;
            this.go(index);
        },
        startDrag(event) {
            event.preventDefault();
            this.isDragging = true;
            this.startX = this.getEventClientX(event) - this.currentX;
        },
        stopDrag(event) {
            event.preventDefault();
            this.isDragging = false;
            this.updateBoxPositionBasedOnCurrentX();

        },
        drag(event) {
            if (this.isDragging) {
                let newX = this.getEventClientX(event) - this.startX;
                newX = Math.max(-(this.boxWidth * (this.boxes.length - 1)), Math.min(newX, 0));
                this.currentX = newX;
                this.updateBoxesPosition();


                // 计算当前索引并更新
                const nearestIndex = Math.round(Math.abs(this.currentX) / this.boxWidth);
                this.activeIndex = nearestIndex; // 更新 activeIndex

                // 将索引传递给子组件
                this.$emit('update:index', nearestIndex);

            }
        },
        getEventClientX(event) {
            return event.type.startsWith("touch") ? event.touches[0].clientX : event.clientX;
        },
        updateBoxesPosition() {
            this.boxes.forEach((box, index) => {
                box.left = this.currentX + index * this.boxWidth;
            });
        },
        updateBoxPositionBasedOnCurrentX() {
            const nearestIndex = Math.round(Math.abs(this.currentX) / this.boxWidth);
            this.currentX = -nearestIndex * this.boxWidth;
            this.updateBoxesPosition();
        },

        go(index) {
            this.activeIndex = index;
            const newX = -index * this.boxWidth;
            this.currentX = newX;
            this.updateBoxesPosition();

            // 同时将索引传递给子组件
            this.$emit('update:index', index);
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
.homecontain {
    overflow-x: hidden;
    /* 禁止水平滚动 */
}

.home {
    display: flex;
    width: 100%;
    height: 100%;
    flex-direction: column;
    background-color: black;
    overflow: hidden;
}


.headercontain {
    position: absolute;
    top: 0px;
    z-index: 10;
}

/* ****************************** */




.videomain {

    display: flex;
    /* 使用flex布局 */
    width: 100vw;
    height: 95vh;
    position: relative;
    /* 相对定位 */
    overflow: hidden;

}



.box {

    position: absolute;
    /* 绝对定位 */
    transition: left 0.3s ease;
    /* 位置变化的过渡效果 */
    overflow: hidden;
}


/* 视频播放区域 */
.videobox {
    width: 100vw;
    background-color: yellow;
}

/* ****************************** */
</style>