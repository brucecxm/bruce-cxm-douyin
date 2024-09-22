<template>
    <div class="home">
        <BaseHeader customClass="custom-header"></BaseHeader>

        <div class="jingbody" @click="handleClick">
            <div class="comment" v-for="(comment, index) in comments" :key="index">
                <img :src="comment.image" alt="" class="comment-image">
                <div class="biaoti">
                    <p>{{ comment.title }}</p>
                </div>
                <div class="authinfo">
                    <div class="touxiang">
                        <img :src="comment.authorImage" alt="" class="author-image">
                    </div>
                    <div class="name">
                        <p>{{ comment.authorName }}</p>
                    </div>
                    <div class="like">{{ comment.likeText }}</div>
                </div>
            </div>
        </div>

        <BaseFooter></BaseFooter>
    </div>
</template>

<script>
import BaseFooter from '../../components/BaseFooter.vue';
import BaseHeader from '../../components/BaseHeader.vue';
import imageSrc from '../../assets/op.jpg';

export default {
    data() {
        return {
            comments: [
                {
                    image: imageSrc,
                    title: '和电话我等哈我和刘德华哇好多礼物达瓦合理的辣味和',
                    authorImage: imageSrc,
                    authorName: '给所',
                    likeText: '爱心',
                },
                {
                    image: imageSrc,
                    title: '这是第二条评论的标题',
                    authorImage: imageSrc,
                    authorName: '作者二',
                    likeText: '喜欢',
                },
                {
                    image: imageSrc,
                    title: '这是第三条评论的标题',
                    authorImage: imageSrc,
                    authorName: '作者三',
                    likeText: '赞',
                },
                {
                    image: imageSrc,
                    title: '这是第四条评论的标题',
                    authorImage: imageSrc,
                    authorName: '作者四',
                    likeText: '爱心',
                },
                {
                    image: imageSrc,
                    title: '这是第五条评论的标题',
                    authorImage: imageSrc,
                    authorName: '作者五',
                    likeText: '喜欢',
                },
                {
                    image: imageSrc,
                    title: '这是第六条评论的标题',
                    authorImage: imageSrc,
                    authorName: '作者六',
                    likeText: '赞',
                },
            ]
        };
    },
    components: {
        BaseFooter,
        BaseHeader,
    },
    methods: {
        handleClick(event) {
            console.log("Clicked on:", event.target);
            this.goarticledetail();
        },
        goarticledetail() {
            console.log("Navigating to article detail...");
            this.$router.push("/articledetail");
        }
    }
};
</script>

<style scoped>
.touxiang {
    width: 20px;
    height: 20px;
    border-radius: 10px;
    overflow: hidden;
}

.userinfo {
    width: 200px;
    overflow: hidden;
    display: flex;
    flex-direction: row;
}

.biaoti {
    float: left;
}

.comment {
    display: flex;
    flex-direction: column;
    width: calc(50% - 6px);
    /* Assuming you want 2 comments per row with a small margin */
    margin: 3px;
    background-color: red;
    overflow: hidden;
}

.comment-image {
    width: 100%;
    height: auto;
}

.author-image {
    width: 20px;
    height: 30px;
}

.jingbody {
    display: flex;
    flex-wrap: wrap;
    width: 100%;
    overflow: hidden;
    z-index: 5;
}

::v-deep .footer {
    background-color: white;
    color: black;
}

::v-deep .jiahao {
    background-image: url(../../assets/home/加号黑.png);
}

.home {
    width: 100%;
    background-color: white;
}

.custom-header {
    background-color: blue !important;
    /* 修改背景颜色为蓝色 */
    /* 添加其他样式 */
}
</style>
