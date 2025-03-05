<!-- <template>
    <div class="container">
        <div class="header">
            <input type="text" placeholder="搜索好友" />
            <div class="tabs">
                <div class="tab selected">好友</div>
                <div class="tab">关注</div>
                <div class="tab">粉丝</div>
                <div class="settings"></div>
            </div>
        </div>
        <div class="friends-list">
            <div class="friend-item">
                <img src="../assets/avatar1.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">单曲猫咪</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
            <div class="friend-item">
                <img src="../assets/avatar2.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">dad</div>
                    <div class="status">昨天在线</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
            <div class="friend-item">
                <img src="../assets/avatar3.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">纯情火焰超级无敌plus暴龙</div>
                    <div class="status">10分钟前在线</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
            <div class="friend-item">
                <img src="../assets/avatar4.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">Cuzza</div>
                    <div class="status">昨天在线</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
            <div class="friend-item">
                <img src="../assets/avatar5.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">怎么还没下课!</div>
                    <div class="status">自律的顶端就是享受孤独...</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
            <div class="friend-item">
                <img src="../assets/avatar6.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">羊村灰太狼 (不想做肚子)</div>
                    <div class="status">2006年美国周刊年度风云人物...</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
            <div class="friend-item">
                <img src="../assets/avatar7.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">没吃饱?</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
            <div class="friend-item">
                <img src="../assets/avatar8.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">阳阳欧耶</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
            <div class="friend-item">
                <img src="../assets/avatar9.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">想念念</div>
                    <div class="status">生亦逢春, 散光漫星辰</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
            <div class="friend-item">
                <img src="../assets/avatar10.jpg" alt="avatar" class="avatar" />
                <div class="friend-info">
                    <div class="name">Savage</div>
                    <button class="send-message">发私信</button>
                </div>
            </div>
        </div>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            // Your data here
        };
    },
    methods: {
        // Your methods here
    },
};
</script>
  
<style scoped>
.container {
    width: 414px;
    height: 896px;
    display: flex;
    flex-direction: column;
    background-color: #fff;
}

.header {
    display: flex;
    flex-direction: column;
    padding: 10px;
    background-color: #f8f8f8;
    border-bottom: 1px solid #eaeaea;
}

.header input {
    height: 40px;
    margin-bottom: 10px;
    padding: 5px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.tabs {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.tab {
    flex: 1;
    text-align: center;
    padding: 10px;
    font-size: 16px;
    cursor: pointer;
}

.tab.selected {
    font-weight: bold;
    border-bottom: 2px solid #007bff;
}

.settings {
    width: 24px;
    height: 24px;
    background-image: url('../assets/settings-icon.png');
    background-size: cover;
}

.friends-list {
    flex-grow: 1;
    overflow-y: auto;
}

.friend-item {
    display: flex;
    align-items: center;
    padding: 10px;
    border-bottom: 1px solid #eaeaea;
}

.friend-item .avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 10px;
}

.friend-info {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
}

.friend-info .name {
    font-size: 16px;
    font-weight: bold;
}

.friend-info .status {
    font-size: 14px;
    color: #888;
}

.send-message {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
}
</style>
   -->