<template>
    <div class="pinglunqu">
        <button @click="sendMessage()" class="close-button">关闭评论区</button>

        <!-- 评论内容展示 -->
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

        <!-- 发布评论的输入框 -->
        <div class="comment-input">
            <textarea v-model="newComment" placeholder="写下你的评论..." class="input-textarea"></textarea>
            <button @click="submitComment" class="submit-button">发布</button>
        </div>
    </div>
</template>

<script>
import { eventBus } from '../main.js'; // 导入事件总线
import { getcomment ,homegetVideocontent} from '@/api/video'

export default {
    name: "Pinglunqu",
    data() {
        return {
            commentsData: [ // 示例评论数据
                { userId: 1, avatar: 'https://via.placeholder.com/50', username: '张三', comment: '这是一条评论内容，真的很棒！', likes: 15 },
                { userId: 2, avatar: 'https://via.placeholder.com/50', username: '李四', comment: '这条评论也很有意思！', likes: 10 },
                { userId: 3, avatar: 'https://via.placeholder.com/50', username: '王五', comment: '哈哈，这太搞笑了！', likes: 25 },
            ],
            newComment: '', // 用于绑定输入框的评论内容
            videoData: {}
        };
    },
    methods: {
        sendMessage() {

            console.log("评论区已关闭");
            eventBus.$emit('messageSent', false); // 关闭评论区
        },
        likeComment(index) {
            this.commentsData[index].likes++; // 增加点赞量
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
        },
        getcommentone() {
            
         const videoid=    this.videoData.videoid
            const one = { videoid: videoid}; // 获取当前视频的 videoid
        

            getcomment(one) // 调用 API 获取评论数据
                .then(videoArr => {
                    
                    this.commentsData = videoArr.data.data; // 将评论数据保存到组件的 data 中
                })
                .catch(error => {
                    console.error('获取视频评论出错:', error);
                });
        },
        submitComment() {
            if (this.newComment.trim() !== '') {
                const newCommentObj = {
                    userId: Date.now(), // 临时使用当前时间戳作为用户 ID（模拟）
                    avatar: 'https://via.placeholder.com/50',
                    username: '新用户',
                    comment: this.newComment,
                    likes: 0,
                };

                // 将新评论添加到评论列表
                this.commentsData.unshift(newCommentObj); // 在顶部显示新评论
                this.newComment = ''; // 清空输入框
                console.log('发布评论:', newCommentObj);
            } else {
                console.log('评论内容不能为空');
            }
        }
    },
    created() {
        // eventBus.$on('eventName', (data) => {
        //     console.log(data);
        //     this.receivedData = data;
        // });
    },
    mounted() {
        debugger
        eventBus.$on('messageSent_videoid', (msg) => {
            
        
            debugger
            this.videoData = msg.videoInfo;
            console.log(this.videoData);
        this.getcommentone();

        });
    },
    beforeDestroy() {
        eventBus.$off('messageSent_videoid');
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

/* 评论区关闭按钮 */
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

/* 评论内容 */
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

/* 发布评论输入框样式 */
.comment-input {
    position: fixed;
    bottom: 0;
    width: 100%;
    background-color: white;
    padding: 15px;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    border-top: 1px solid #ddd;
}

.input-textarea {
    width: 80%;
    height: 40px;
    padding: 10px;
    font-size: 14px;
    border-radius: 5px;
    border: 1px solid #ccc;
    resize: none;
}

.submit-button {
    width: 15%;
    padding: 10px;
    margin-left: 10px;
    background-color: #f04e23;
    color: white;
    border-radius: 5px;
    font-size: 14px;
    border: none;
    cursor: pointer;
}

.submit-button:hover {
    background-color: #e03b18;
}
</style>

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