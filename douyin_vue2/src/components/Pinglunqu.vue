<template>
    <div class="pinglunqu">
        <button @click="sendMessage()" class="close-button">关闭评论区</button>
        <div class="comment" v-for="(ping, index) in commentsData" :key="ping.userId">
            <div class="touxiangqu">
                <div class="touxiang" @click="goToUserProfile(ping.userId)">
                    <img :src="ping.avatar" alt="头像" />
                </div>
            </div>
            <div class="xiaoxi">
                <p class="name">{{ ping.username }}</p>
                <p class="comment-text">{{ ping.comment }}</p>
                <div class="actions">
                    <span class="action" @click="likeComment(index)">❤️ {{ ping.likes }}</span>
                    <span class="action" @click="replyComment(index)">回复</span>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { eventBus } from '../main.ts'; // 导入事件总线

export default {
    name: "Pinglunqu",
    data() {
        return {
            commentsData: [ // 示例评论数据
                {
                    userId: 1,
                    avatar: 'https://via.placeholder.com/50', // 用户头像地址
                    username: '张三',
                    comment: '这是一条评论内容，真的很棒！',
                    likes: 15, // 点赞数
                },
                {
                    userId: 2,
                    avatar: 'https://via.placeholder.com/50',
                    username: '李四',
                    comment: '这条评论也很有意思！',
                    likes: 10,
                },
                {
                    userId: 3,
                    avatar: 'https://via.placeholder.com/50',
                    username: '王五',
                    comment: '哈哈，这太搞笑了！',
                    likes: 25,
                },
            ],
            video_id: ""
        };
    },
    methods: {
        sendMessage() {
            console.log("评论区已关闭");
            eventBus.$emit('messageSent', false); // 关闭评论区
        },
        likeComment(index) {
            // 增加点赞量
            this.commentsData[index].likes++;
            console.log(`点赞评论 ${index}`);
        },
        replyComment(index) {
            console.log(`回复评论 ${index}`);
            // 在此处处理回复逻辑
        },
        goToUserProfile(userId) {
            console.log(`跳转到用户 ${userId} 的主页`);
            // 跳转到用户主页的逻辑可以是：
            // this.$router.push({ name: 'userProfile', params: { userId } });
            // 或者使用其他跳转方法
        },
    },
    created() {
        eventBus.$on('eventName', (data) => {
            console.log(data);
            this.receivedData = data;
        });
        eventBus.$on('messageSent_videoid', (msg) => {
            this.video_id = msg;
        });
        console.log("Ssssssssssss" + this.video_id)

    },
    mounted() {
        eventBus.$on('messageSent_videoid', (msg) => {
            this.video_id = msg;
        });
    },
    beforeDestroy() {
        eventBus.$off('messageSent_videoid'); // 组件销毁前移除事件监听
        eventBus.$off('eventName'); // 组件销毁前移除事件监听
    },
};
</script>
<style scoped>
.pinglunqu {
    width: 100vw;
    height: 60vh;
    background-color: #f9f9f9;
    border-radius: 10px 10px 0 0;
    padding: 20px;
    box-sizing: border-box;
    overflow-y: auto;
}

.close-button {
    background-color: #f04e23;
    color: white;
    padding: 10px 20px;
    border-radius: 30px;
    font-size: 14px;
    border: none;
    cursor: pointer;
    margin-bottom: 20px;
}

.comment {
    display: flex;
    margin-bottom: 20px;
    padding: 10px;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.touxiangqu {
    width: 50px;
    height: 50px;
    margin-right: 15px;
}

.touxiang {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    overflow: hidden;
    cursor: pointer;
}

.touxiang img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.xiaoxi {
    flex: 1;
}

.name {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 5px;
}

.comment-text {
    font-size: 14px;
    line-height: 1.6;
    color: #333;
    margin-bottom: 10px;
}

.actions {
    display: flex;
    align-items: center;
}

.action {
    font-size: 14px;
    margin-right: 15px;
    color: #ff7a59;
    cursor: pointer;
}

.action:hover {
    text-decoration: underline;
}
</style>