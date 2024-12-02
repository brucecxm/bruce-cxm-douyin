<template>
    <div class="videoaside">
        <div class="touxiang avatar" @click="goauth(101)">
            <img src="//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_8fd03d785381b8f1dabc166e71f16626.jpeg?from=2956013662"
                alt="avatar" style="width: 1.4rem;height: 1.4rem;">
        </div>
        <div class="aside like" @click="toggleLike" :class="{ 'liked': isLiked }"></div>
        <div class="num">{{ videoData.likeNum }}</div>
        <div class="aside comment" @click="toggleComment" :class="{ 'clicked': isCommentClicked }"></div>
        <div class="num">{{ videoData.commentNum }}</div>
        <div class="aside collect" @click="toggleCollect" :class="{ 'collected': isCollected }"></div>
        <div class="num">{{ videoData.collectNum }}</div>
        <div class="aside share" @click="toggleShare" :class="{ 'clicked': isShareClicked }"></div>
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
            userId: "1",
            isLiked: false, // 用于控制是否已点赞
            isCollected: false, // 用于控制是否已收藏
            isCommentClicked: false, // 用于控制评论图标点击状态
            isShareClicked: false // 用于控制分享图标点击状态
        };
    },
    methods: {
        showcomment() {
            eventBus.$emit('messageSent', true);
        },
        toggleComment() {
            this.isCommentClicked = true; // 设置评论图标点击状态
            setTimeout(() => {
                this.isCommentClicked = false; // 0.5秒后恢复状态
            }, 500);
            this.showcomment(); // 调用评论方法
        },
        test() {
            console.log("test");
        },
        toggleLike() {
            this.isLiked = !this.isLiked; // 切换点赞状态
            this.behaviour('like');
        },
        toggleCollect() {
            this.isCollected = !this.isCollected; // 切换收藏状态
            this.behaviour('collect');
        },
        toggleShare() {
            this.isShareClicked = true; // 设置分享图标点击状态
            setTimeout(() => {
                this.isShareClicked = false; // 0.5秒后恢复状态
            }, 500);
            this.behaviour('share'); // 调用分享方法
        },
        behaviour(name) {
            console.log(name);
            console.log('behaviour called');
        },
        goauth(userId) {
            this.$router.push({ path: `/auth/${userId}` });
        },

        gomusic() {
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
}

.aside {
    width: 1rem;
    height: 1rem;
    position: relative;
    /* 为了定位心形图标 */
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
    overflow: hidden;
}

.music {
    border-radius: 0.7rem;
    overflow: hidden;
}

/* 点赞图标 */
.like {
    background-image: url(../assets/home/点赞.png);
    background-size: 1rem 1rem;
    transition: transform 0.3s;
    /* 添加过渡效果 */
}

.like.liked {
    background-image: url(../assets/home/红心.png);
    /* 替换为红心图标 */
    transform: scale(1.2);
    /* 放大效果 */
}

/* 收藏图标 */
.collect {
    background-image: url(../assets/home/收藏.png);
    background-size: 1rem 1rem;
    transition: transform 0.3s, background-color 0.3s;
    /* 添加过渡效果 */
}

.collect.collected {
    background-image: url(../assets/home/黄星.png);
    /* 替换为黄星图标 */
    transform: scale(1.2);
    /* 放大效果 */
}

/* 评论图标 */
.comment {
    background-image: url(../assets/home/评论.png);
    background-size: 1rem 1rem;
    transition: transform 0.3s;
    /* 添加过渡效果 */
}

.comment.clicked {
    transform: scale(1.2);
    /* 放大效果 */
}

/* 分享图标 */
.share {
    background-image: url(../assets/home/转发.png);
    background-size: 1rem 1rem;
    transition: transform 0.3s;
    /* 添加过渡效果 */
}

.share.clicked {
    transform: scale(1.2);
    /* 放大效果 */
}
</style>
