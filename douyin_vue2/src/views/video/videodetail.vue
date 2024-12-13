<template>
    <div class="detailv">
        <videobox-vue></videobox-vue>
        <footer class="footer">
            <div class="music" v-if="music">music</div>
            <div class="auth" v-if="auth">auth</div>
        </footer>
    </div>
</template>

<script>
import videoboxVue from '@/components/videobox.vue'
export default {
    data() {
        return {
            music: false,
            auth: true,
            type: '',
            videoid: ''
        };
    },
    components: {
        videoboxVue
    },
    methods: {
        updateAaa() {
            // 根据 type 的值更新 aaa
            if (this.type === 'music') {
                this.music = true;
            } else if (this.type === 'auth') {
                this.auth = true;
            }
        }
    },
    mounted() {
        // 从路由查询参数中获取 type 和 videoid
        this.type = this.$route.query.type;
        this.videoid = this.$route.query.videoid;

        // 这里可以根据 type 和 videoid 进行其他操作，例如获取视频详情
        console.log('接收到的类型:', this.type);
        console.log('接收到的视频ID:', this.videoid);
    },
    watch: {
        type(newType) {
            // 监听 type 的变化
            this.updateAaa();
        },
        music(newVal) {
            // 如果 music 被设置为 true，确保 auth 为 false
            if (newVal) {
                this.auth = false;
            }
        },
        auth(newVal) {
            // 如果 auth 被设置为 true，确保 music 为 false
            if (newVal) {
                this.music = false;
            }
        }
    }
}
</script>

<style scoped>
.detailv {
    width: 100%;
    height: 100%;
    overflow: hidden;
}

.footer {
    width: 100%;
    height: 5vh;
    position: fixed;
    bottom: 0px;
}
</style>