<template>
    <div class="container" @mousedown="startDrag" @mouseup="stopDrag" @mousemove="drag" @mouseleave="stopDrag"
        @touchstart="startDrag" @touchend="stopDrag" @touchmove="drag"
        :style="{ width: boxWidth, height: boxHeight * 0.1 + 'vh' }">


        <div v-for="(box, index) in boxes" :key="index" class="box"
            :style="{ top: box.top * 0.1 + 'vh', backgroundColor: box.color, width: boxWidth, height: boxHeight * 0.1 + 'vh' }">
            <!-- 用div包一下 避免父组件中absolute对子组件flex布局的影响 -->
            <div class="videoasideone">
                <videoaside-vue :video-data="videoboxdata[index]"></videoaside-vue>

            </div>

            <div class="videoarticle">
                <div class="position">
                    1天前
                </div>

                <videoarticle-vue></videoarticle-vue>
            </div>

            <video ref="videos" class="videoone" :src="videoboxdata[index].videoUrl" preload="true" loop
                x5-video-player-type="h5-page" x5-video-player-fullscreen="false" webkit-playsinline="true"
                x5-playsinline="true" playsinline="true">
                <p>您的浏览器不支持 video 标签。</p>
            </video>



        </div>



    </div>
</template>

<script>
import videoasideVue from './videoaside.vue';
import videoarticleVue from './videoarticle.vue';
import { homegetVideo, homegetVideomore, homegetVideocontent } from "../api/video"
export default {
    components: {
        videoasideVue,
        videoarticleVue
    },
    watch: {

    },
    data() {
        return {
            // 使用props传递数据 使用props的数据在组件中是单向传输的 但是我可以通过请求异步修改数据库中的数据 就可以了
            videoboxdata: [
                {
                    videoUrl: "https://v26-web-prime.douyinvod.com/video/tos/cn/tos-cn-ve-15/ooRPEIAtasBw4j9iWbBZOQAGQXa7BFiwPoIWs/?a=6383&ch=0&cr=0&dr=0&er=0&cd=0%7C0%7C0%7C0&cv=1&br=1376&bt=1376&cs=0&ds=4&ft=AQHeBxbkRR0s~1C62Dv2Nc.xBiGNbLeFc2sU_4OjdCeJNv7TGW&mime_type=video_mp4&qs=0&rc=Nmk0ODo6NjtoNjM4Ozc4ZkBpM3c3OGo5cnRwdTMzNGkzM0AzNmMyLy4xNl8xNS4zNmIzYSM0MGRwMmQ0MzVgLS1kLTBzcw%3D%3D&btag=80000e00030000&cquery=100b&dy_q=1726993944&expire=1726997786&feature_id=46a7bb47b4fd1280f3d3825bf2b29388&l=2024092216322471F0DEB02CE76903FBF4&ply_type=4&policy=4&signature=63a0e466f23de0df6ceeb7132cf81be2&tk=webid&webid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82-d2d651a1efc95dd260bf0cc906e1ce4c",
                    videoArticle: "",
                    username: "",
                    userAvatar: "",
                    likeNum: "1",
                    commentNum: "10",
                    shareNum: "100",
                    collectNum: "1000",
                    musicAvatar: "",
                    musicName: ""
                },
                {
                    videoUrl: "https://v26-web-prime.douyinvod.com/video/tos/cn/tos-cn-ve-15/ooRPEIAtasBw4j9iWbBZOQAGQXa7BFiwPoIWs/?a=6383&ch=0&cr=0&dr=0&er=0&cd=0%7C0%7C0%7C0&cv=1&br=1376&bt=1376&cs=0&ds=4&ft=AQHeBxbkRR0s~1C62Dv2Nc.xBiGNbLeFc2sU_4OjdCeJNv7TGW&mime_type=video_mp4&qs=0&rc=Nmk0ODo6NjtoNjM4Ozc4ZkBpM3c3OGo5cnRwdTMzNGkzM0AzNmMyLy4xNl8xNS4zNmIzYSM0MGRwMmQ0MzVgLS1kLTBzcw%3D%3D&btag=80000e00030000&cquery=100b&dy_q=1726993944&expire=1726997786&feature_id=46a7bb47b4fd1280f3d3825bf2b29388&l=2024092216322471F0DEB02CE76903FBF4&ply_type=4&policy=4&signature=63a0e466f23de0df6ceeb7132cf81be2&tk=webid&webid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82-d2d651a1efc95dd260bf0cc906e1ce4c",

                    videoArticle: "",
                    username: "",
                    userAvatar: "",
                    likeNum: "2",
                    commentNum: "20",
                    shareNum: "200",
                    collectNum: "2000",
                    musicAvatar: "",
                    musicName: ""
                },
                {
                    videoUrl: "https://v26-web-prime.douyinvod.com/video/tos/cn/tos-cn-ve-15/ooRPEIAtasBw4j9iWbBZOQAGQXa7BFiwPoIWs/?a=6383&ch=0&cr=0&dr=0&er=0&cd=0%7C0%7C0%7C0&cv=1&br=1376&bt=1376&cs=0&ds=4&ft=AQHeBxbkRR0s~1C62Dv2Nc.xBiGNbLeFc2sU_4OjdCeJNv7TGW&mime_type=video_mp4&qs=0&rc=Nmk0ODo6NjtoNjM4Ozc4ZkBpM3c3OGo5cnRwdTMzNGkzM0AzNmMyLy4xNl8xNS4zNmIzYSM0MGRwMmQ0MzVgLS1kLTBzcw%3D%3D&btag=80000e00030000&cquery=100b&dy_q=1726993944&expire=1726997786&feature_id=46a7bb47b4fd1280f3d3825bf2b29388&l=2024092216322471F0DEB02CE76903FBF4&ply_type=4&policy=4&signature=63a0e466f23de0df6ceeb7132cf81be2&tk=webid&webid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82-d2d651a1efc95dd260bf0cc906e1ce4c",


                    videoArticle: "",
                    username: "",
                    userAvatar: "",
                    likeNum: "3",
                    commentNum: "30",
                    shareNum: "300",
                    collectNum: "3000",
                    musicAvatar: "",
                    musicName: ""
                },
                {
                    videoUrl: "https://v26-web-prime.douyinvod.com/video/tos/cn/tos-cn-ve-15/ooRPEIAtasBw4j9iWbBZOQAGQXa7BFiwPoIWs/?a=6383&ch=0&cr=0&dr=0&er=0&cd=0%7C0%7C0%7C0&cv=1&br=1376&bt=1376&cs=0&ds=4&ft=AQHeBxbkRR0s~1C62Dv2Nc.xBiGNbLeFc2sU_4OjdCeJNv7TGW&mime_type=video_mp4&qs=0&rc=Nmk0ODo6NjtoNjM4Ozc4ZkBpM3c3OGo5cnRwdTMzNGkzM0AzNmMyLy4xNl8xNS4zNmIzYSM0MGRwMmQ0MzVgLS1kLTBzcw%3D%3D&btag=80000e00030000&cquery=100b&dy_q=1726993944&expire=1726997786&feature_id=46a7bb47b4fd1280f3d3825bf2b29388&l=2024092216322471F0DEB02CE76903FBF4&ply_type=4&policy=4&signature=63a0e466f23de0df6ceeb7132cf81be2&tk=webid&webid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82-d2d651a1efc95dd260bf0cc906e1ce4c",


                    videoArticle: "",
                    username: "",
                    userAvatar: "",
                    likeNum: "4",
                    commentNum: "40",
                    shareNum: "400",
                    collectNum: "4000",
                    musicAvatar: "",
                    musicName: ""
                },

            ],
            isDragging: false,
            startY: 0,
            currentY: 0,
            boxWidth: "100%",
            activeIndex: null,
            // 因为受下面函数的影响  如果boxheight的数值太小  滑动会不流畅 因此我用了900 上面乘以0.01
            boxHeight: 950,
            boxes: [],
        };
    },
    created() {

        this.boxes = [
            { color: "black", top: 0 },
            { color: "black", top: 1 * this.boxHeight },
            { color: "black", top: 2 * this.boxHeight },
            { color: "black", top: 3 * this.boxHeight },
        ];
    },
    mounted() {

        this.updateVideoPlayback();
    },
    methods: {



        startDrag(event) {
            event.preventDefault();
            this.isDragging = true;
            this.startY = this.getEventClientY(event) - this.currentY;
        },
        stopDrag(event) {
            event.preventDefault();
            this.isDragging = false;
            this.updateBoxPositionBasedOnCurrentY();
            this.updateVideoPlayback();
        },
        drag(event) {
            if (this.isDragging) {
                let newY = this.getEventClientY(event) - this.startY;
                newY = Math.max(-(this.boxHeight * (this.boxes.length - 1)), Math.min(newY, 0));
                this.currentY = newY;
                this.updateBoxesPosition();
                this.updateVideoPlayback(); // 添加这一行
            }
        },
        getEventClientY(event) {
            return event.type.startsWith("touch") ? event.touches[0].clientY : event.clientY;
        },
        updateBoxesPosition() {
            this.boxes.forEach((box, index) => {
                box.top = this.currentY + index * this.boxHeight;
            });
        },
        updateBoxPositionBasedOnCurrentY() {
            const nearestIndex = Math.round(Math.abs(this.currentY) / this.boxHeight);
            this.currentY = -nearestIndex * this.boxHeight;
            this.updateBoxesPosition();
        },
        updateVideoPlayback() {
            const containerHeight = this.$el.clientHeight; // 获取容器的实际高度
            const threshold = 0.9; // 设置一个阈值，例如90%可见才算完全可见

            this.$refs.videos.forEach((video, index) => {
                const box = this.boxes[index];
                const boxTop = box.top;
                const boxBottom = boxTop + this.boxHeight * 0.1; // 因为你的高度使用了 * 0.1 的缩放

                // 计算视频在视口中可见的比例
                const visibleHeight = Math.min(containerHeight, boxBottom) - Math.max(0, boxTop);
                const visibleRatio = visibleHeight / (this.boxHeight * 0.1);

                // 如果视频盒子几乎完全在视口内（根据阈值），播放视频，否则暂停
                if (visibleRatio >= threshold) {
                    video.play();
                } else {
                    video.pause();
                }
            });
        },

        go(index) {
            this.activeIndex = index;
            const newY = -index * this.boxHeight;
            this.currentY = newY;
            this.updateBoxesPosition();
            this.updateVideoPlayback();
        },
    },
};
</script>

<style scoped>
.position {
    /* color: white; */
    color: rgba(255, 255, 255, 0.7);
    font-size: 0.4rem;
    width: 5rem;
    height: 1rem;
    transform: translateY(-1.8rem) translateX(2.3rem);
}

.videoasideone {
    display: block;
    z-index: 5;
    position: absolute;
    /* 侧边栏的位置 */
    right: 0;
    bottom: 6vh;
    margin-right: 4vw;
}

.videoarticle {
    display: block;
    z-index: 5;
    position: absolute;
    /* 侧边栏的位置 */
    left: 0;
    bottom: 2vh;
}

.videoone {

    display: block;
    z-index: 10;
    object-fit: cover;
    margin: 0 auto;
    width: 100%;
    height: 100%;
}

.container {
    display: flex;
    flex-direction: column;
    position: relative;
    height: 90vh;

}

.box {
    position: absolute;
    transition: top 0.3s ease;

}
</style>
