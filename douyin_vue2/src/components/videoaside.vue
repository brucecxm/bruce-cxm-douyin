<template>
    <div class="videoaside">
        <div class="touxiang avatar" @click="goauth">
            <img src="//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_8fd03d785381b8f1dabc166e71f16626.jpeg?from=2956013662"
                alt="avatar" style="width: 1.4rem;height: 1.4rem;">
        </div>
        <div class="aside like" @click="behaviour('like')"></div>
        <div class="num">{{ videoData.likeNum }}</div>
        <div class="aside comment" @click="showcomment"></div>
        <div class="num" @click="test">{{ videoData.commentNum }}</div>
        <div class="aside collect" @click="test"></div>
        <div class="num">{{ videoData.collectNum }}</div>
        <div class="aside share" @click="behaviour('share')"></div>
        <div class="num">{{ videoData.shareNum }}</div>
        <div class="touxiang music" @click="gomusic">
            <img src="//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_8fd03d785381b8f1dabc166e71f16626.jpeg?from=2956013662"
                alt="music" style="width: 1.4rem;height: 1.4rem;">
        </div>
    </div>
</template>
 
<script>
import { useTokenStore } from "../stores/token";
import { eventBus } from '../main.ts'; // 导入事件总线

export default {
    props: {
        videoData: Object
    },
    data() {
        return {

        };
    },
    methods: {
        showcomment() {
            eventBus.$emit('messageSent', true);

        },
        test() {
            console.log("test")
        },
        behaviour(name) {
            console.log(name)
            console.log('behaviour called')
        },
        goauth() {
            this.$router.push('/auth');
        },
        gomusic() {
            // Navigate to '/free-video' route
            this.$router.push('/music');
        },

    }

};
</script>

<style scoped>
.touxiang {
    width: 1.4rem;
    height: 1.4rem;
}

.num {
    color: white;
    font-size: 0.4rem;
    margin-top: -0.3rem;
    /* 调整此值以控制向上移动的距离 */

}

.aside {
    width: 1rem;
    height: 1rem;

}

/* 父组件的absolute布局影响到了子组件的flex布局 */
.videoaside {
    display: flex;
    flex-direction: column;
    width: 2rem;
    color: rgb(81, 67, 67);
    height: 12rem;
    justify-content: space-between;
    align-items: center;

}

.avatar {
    border-radius: 0.7rem;
    /* background-image: url(); */
    overflow: hidden;

}

.music {
    border-radius: 0.7rem;
    overflow: hidden;
}

.like {
    background-image: url(../assets/home/点赞.png);
    background-size: 1rem 1rem;
}

.comment {
    background-image: url(../assets/home/评论.png);
    background-size: 1rem 1rem;
}

.collect {
    background-image: url(../assets/home/收藏.png);
    background-size: 1rem 1rem;
}

.share {
    background-image: url(../assets/home/转发.png);
    background-size: 1rem 1rem;
}
</style>
