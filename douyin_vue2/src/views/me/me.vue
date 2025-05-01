<template>
    <div class="container">

        <!-- 顶部背景图 -->
        <div class="profile-header">
            <img class="cover-image" :src="userInfo.backImg" alt="背景图" />

            <!-- 用户基本信息区 -->
            <div class="user-info">
                <img class="avatar" :src="userInfo.avatar" alt="头像" />
                <div class="user-name">{{ userInfo.nickname }}</div>
                <div class="user-id">抖音号：{{ userInfo.id }}</div>

                <!-- 用户统计信息 -->
                <div class="user-stats">
                    <div class="stat-item">
                        <div class="num">{{ userInfo.followers || '0' }}</div>
                        <div class="label">粉丝</div>
                    </div>
                    <div class="stat-item">
                        <div class="num">{{ userInfo.following || '0' }}</div>
                        <div class="label">关注</div>
                    </div>
                    <div class="stat-item">
                        <div class="num">{{ userInfo.likes || '0' }}</div>
                        <div class="label">获赞</div>
                    </div>
                </div>

                <div class="edit-profile-btn">编辑主页</div>

                <!-- 个人简介 -->
                <div class="bio">
                    <div>{{ userInfo.jieshao || '暂无个人简介' }}</div>
                    <div class="location">{{ userInfo.city }}</div>
                    <div class="school">{{ userInfo.school || '暂无学校信息' }}</div>
                </div>
            </div>
        </div>

        <!-- 功能按钮区 -->
        <div class="function-bar">
            <div class="function-item">
                <i class="iconfont icon-gouwuche"></i>
                <span>我的购物</span>
            </div>
            <div class="function-item">
                <i class="iconfont icon-lishijilu"></i>
                <span>浏览历史</span>
            </div>
            <div class="function-item">
                <i class="iconfont icon-youxi"></i>
                <span>我的游戏</span>
            </div>
            <div class="function-item">
                <i class="iconfont icon-shoucang"></i>
                <span>我的收藏</span>
            </div>
            <div class="function-item">
                <i class="iconfont icon-gengduo"></i>
                <span>全部动态</span>
            </div>
        </div>

        <!-- 内容标签页 -->
        <div class="content-tabs">
            <div class="tab-header">
                <div class="tab-item active">作品</div>
                <div class="tab-item">时刻</div>
                <div class="tab-item">私密</div>
                <div class="tab-item">推荐</div>
                <div class="tab-item">收藏</div>
                <div class="tab-item">音乐</div>
            </div>

            <!-- 视频列表 -->
            <div class="video-grid">
                <div class="video-item" v-for="(video, index) in videos" :key="index">
                    <img :src="video.cover" alt="视频封面" />
                    <div class="play-count">
                        <i class="iconfont icon-bofang1"></i>
                        <span>{{ video.playCount }}</span>
                    </div>
                </div>
            </div>
        </div>

        <footer-vue class="footer"></footer-vue>
    </div>
</template>

<script>
import footerVue from '@/components/footer.vue'
import { userLoginService, userInfoService, userRegisterService, getVerificationCodeService } from "@/api/user"

export default {
    components: {
        footerVue
    },
    mounted() {
        this.getUserInfo();
    },
    methods: {
        getUserInfo() {
            userInfoService()
                .then(res => {
                    this.userInfo = res.data;
                    console.log(this.userInfo); // 调试输出
                })
                .catch(err => {
                    console.error(err);
                });
        }
    },
    data() {
        return {
            userInfo: {}, // 通过接口返回的用户信息
            videos: [
                { cover: "http://gips0.baidu.com/it/u=1690853528,2506870245&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024", playCount: "512" },
                { cover: "http://gips0.baidu.com/it/u=1690853528,2506870245&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024", playCount: "1693" },
                { cover: "http://gips0.baidu.com/it/u=1690853528,2506870245&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024", playCount: "137" },
                { cover: "http://gips0.baidu.com/it/u=1690853528,2506870245&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024", playCount: "137" },
                { cover: "http://gips0.baidu.com/it/u=1690853528,2506870245&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024", playCount: "137" },
                { cover: "http://gips0.baidu.com/it/u=1690853528,2506870245&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024", playCount: "137" },
                { cover: "http://gips0.baidu.com/it/u=1690853528,2506870245&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024", playCount: "137" },
                { cover: "http://gips0.baidu.com/it/u=1690853528,2506870245&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024", playCount: "137" },
                { cover: "http://gips0.baidu.com/it/u=1690853528,2506870245&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024", playCount: "137" },
            ]
        }
    }
}
</script>

<style scoped>
.container {
    min-height: 100vh;
    background: #f8f8f8;
}

.profile-header {
    position: relative;
    height: 45vh;
}

.cover-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.user-info {
    position: absolute;
    bottom: 20px;
    left: 0;
    right: 0;
    padding: 0 16px;
    color: #fff;
}

.avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    border: 2px solid #fff;
}

.footer {
    background-color: black;
    color: black;
    z-index: 10;
    position: fixed;
    bottom: 0px;
}

.user-name {
    margin-top: 8px;
    font-size: 18px;
    font-weight: bold;
}

.user-id {
    margin-top: 4px;
    font-size: 12px;
    opacity: 0.8;
}

.user-stats {
    display: flex;
    margin-top: 15px;
}

.stat-item {
    flex: 1;
    text-align: center;
}

.num {
    font-size: 18px;
    font-weight: bold;
}

.label {
    font-size: 12px;
    opacity: 0.8;
}

.edit-profile-btn {
    margin-top: 15px;
    padding: 5px 20px;
    border: 1px solid #fff;
    border-radius: 20px;
    display: inline-block;
    font-size: 14px;
}

.bio {
    margin-top: 10px;
    font-size: 14px;
}

.location,
.school {
    margin-top: 4px;
    font-size: 12px;
    opacity: 0.8;
}

.function-bar {
    display: flex;
    padding: 15px 0;
    background: #fff;
}

.function-item {
    flex: 1;
    text-align: center;
    font-size: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.function-item .iconfont {
    font-size: 24px;
    margin-bottom: 4px;
    color: #333;
}

.content-tabs {
    margin-top: 10px;
    background: #fff;
}

.tab-header {
    display: flex;
    border-bottom: 1px solid #eee;
}

.tab-item {
    flex: 1;
    text-align: center;
    padding: 15px 0;
    font-size: 14px;
}

.tab-item.active {
    color: #000;
    font-weight: bold;
    position: relative;
}

.tab-item.active::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 30px;
    height: 2px;
    background: #fe2c55;
}

.video-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 1px;
    background: #eee;
    padding-bottom: 50px;
    /* 为底部导航留出空间 */
}

.video-item {
    position: relative;
    padding-bottom: 100%;
}

.video-item img {
    position: absolute;
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.play-count {
    position: absolute;
    left: 5px;
    bottom: 5px;
    color: #fff;
    font-size: 12px;
    display: flex;
    align-items: center;
}

.play-count .iconfont {
    font-size: 12px;
    margin-right: 2px;
}

.bottom-nav {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    display: flex;
    background: #fff;
    padding: 5px 0;
    border-top: 1px solid #eee;
}

.nav-item {
    flex: 1;
    text-align: center;
    font-size: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.nav-item .iconfont {
    font-size: 20px;
    margin-bottom: 2px;
}

.nav-item.active {
    color: #fe2c55;
}

.nav-item.active .iconfont {
    color: #fe2c55;
}
</style>