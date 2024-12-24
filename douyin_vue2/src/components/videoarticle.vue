<template>
    <div class="videoarticle" @click="testone">
        <div class="username" @click="goauth(videoData.userid)"> {{ videoData.username }}</div>
        <div div class=" article">{{ videoData.video_comment }}</div>
    </div>
</template>

<script>

export default {
    props: {
        videoData: Object
    },
    data() {
        return {
            article: "感谢穿越火线感谢穿越火线感谢穿越火线",
            username: "你好"
        };
    },
    methods: {
        testone() {
            console.log("sssssss")
        },
        goauth(userId) {
            this.$router.push({ path: `/auth/${userId}` });
        },
    }

};
</script>

<style scoped>
.videoarticle {
    width: 75vw;
    max-height: 20vh;
    color: white;
}

.article {
    font-size: 0.5rem;
    margin-left: 0.5rem;

}

.username {
    font-size: 0.6rem;
    margin-bottom: 0.3rem;
    margin-left: 0.5rem;
}
</style>
