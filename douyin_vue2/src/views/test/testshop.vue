<template>
  <div class="live-room">
    <!-- 视频区域 -->
    <div class="video-container">
      <div id="local-video" v-if="isHost"></div>
      <div id="remote-video" v-else></div>
      <div class="live-info">
        <h3>{{ liveInfo.title }}</h3>
        <p>观众: {{ viewerCount }}</p>
      </div>
    </div>

    <!-- 聊天区域 -->
    <div class="chat-container">
      <div class="message-list">
        <div class="message" v-for="(msg, index) in messages" :key="index">
          <span class="username">{{ msg.username }}:</span>
          <span class="content">{{ msg.content }}</span>
        </div>
      </div>
      <div class="input-area">
        <input
          v-model="messageContent"
          @keyup.enter="sendMessage"
          placeholder="发送消息..."
        />
        <button @click="sendMessage">发送</button>
      </div>
    </div>

    <!-- 礼物区域 -->
    <div class="gift-container">
      <div
        class="gift-item"
        v-for="(gift, index) in gifts"
        :key="index"
        @click="sendGift(gift)"
      >
        <img :src="gift.image" alt="礼物" />
        <span>{{ gift.name }} - {{ gift.price }}金币</span>
      </div>
    </div>

    <!-- 主播控制区 -->
    <div class="host-controls" v-if="isHost">
      <button @click="toggleCamera">切换摄像头</button>
      <button @click="toggleMic">切换麦克风</button>
      <button @click="endLive">结束直播</button>
    </div>
  </div>
</template>

<script>
import AgoraRTC from 'agora-rtc-sdk-ng';

export default {
  data() {
    return {
      // 直播信息
      liveInfo: {
        title: '直播标题',
        hostId: '',
        viewers: []
      },
      // 状态
      isHost: false,
      isConnected: false,
      viewerCount: 0,
      // 音视频客户端
      rtc: {
        client: null,
        localAudioTrack: null,
        localVideoTrack: null
      },
      // 聊天相关
      messages: [],
      messageContent: '',
      // 礼物相关
      gifts: [
        { id: 1, name: '鲜花', price: 10, image: 'flower.png' },
        { id: 2, name: '火箭', price: 500, image: 'rocket.png' }
      ]
    };
  },

  mounted() {
    this.initRTC();
    this.initWebSocket();
  },

  beforeDestroy() {
    this.leaveChannel();
  },

  methods: {
    // 初始化RTC客户端
    async initRTC() {
      this.rtc.client = AgoraRTC.createClient({ mode: 'rtc', codec: 'vp8' });

      // 设置事件监听
      this.rtc.client.on('user-published', this.handleUserPublished);
      this.rtc.client.on('user-unpublished', this.handleUserUnpublished);

      // 加入频道
      await this.joinChannel();
    },

    // 加入频道
    async joinChannel() {
      try {
        // 获取token (实际应用中应该从后端获取)
        const token = await this.$http.get('/api/live/token', {
          params: {
            channelName: this.liveInfo.channelName,
            userId: this.$store.state.user.id
          }
        });

        // 加入频道
        await this.rtc.client.join(
          'YOUR_APP_ID',
          this.liveInfo.channelName,
          token.data,
          this.$store.state.user.id
        );

        // 如果是主播，创建并发布本地音视频轨道
        if (this.isHost) {
          this.rtc.localAudioTrack =
            await AgoraRTC.createMicrophoneAudioTrack();
          this.rtc.localVideoTrack = await AgoraRTC.createCameraVideoTrack();

          // 显示本地视频
          this.rtc.localVideoTrack.play('local-video');

          // 发布本地音视频轨道
          await this.rtc.client.publish([
            this.rtc.localAudioTrack,
            this.rtc.localVideoTrack
          ]);
        }

        this.isConnected = true;
      } catch (error) {
        console.error('加入频道失败:', error);
      }
    },

    // 处理用户发布音视频
    async handleUserPublished(user, mediaType) {
      await this.rtc.client.subscribe(user, mediaType);

      if (mediaType === 'video') {
        const remoteVideoTrack = user.videoTrack;
        remoteVideoTrack.play('remote-video');
      }

      if (mediaType === 'audio') {
        const remoteAudioTrack = user.audioTrack;
        remoteAudioTrack.play();
      }
    },

    // 处理用户取消发布音视频
    handleUserUnpublished(user) {
      console.log('用户取消发布:', user.uid);
    },

    // 离开频道
    async leaveChannel() {
      if (this.rtc.localAudioTrack) {
        this.rtc.localAudioTrack.stop();
        this.rtc.localAudioTrack.close();
      }

      if (this.rtc.localVideoTrack) {
        this.rtc.localVideoTrack.stop();
        this.rtc.localVideoTrack.close();
      }

      if (this.rtc.client) {
        await this.rtc.client.leave();
      }

      this.isConnected = false;
    },

    // 初始化WebSocket连接
    initWebSocket() {
      const ws = new WebSocket(
        `ws://${window.location.host}/api/live/ws/${this.liveInfo.channelName}`
      );

      ws.onopen = () => {
        console.log('WebSocket连接已建立');
      };

      ws.onmessage = (event) => {
        const message = JSON.parse(event.data);
        this.processMessage(message);
      };

      ws.onclose = () => {
        console.log('WebSocket连接已关闭');
      };

      this.ws = ws;
    },

    // 处理接收到的消息
    processMessage(message) {
      switch (message.type) {
        case 'CHAT':
          this.messages.push({
            username: message.username,
            content: message.content
          });
          break;
        case 'GIFT':
          // 处理礼物消息
          break;
        case 'VIEWER_COUNT':
          this.viewerCount = message.count;
          break;
      }
    },

    // 发送聊天消息
    sendMessage() {
      if (!this.messageContent.trim()) return;

      const message = {
        type: 'CHAT',
        username: this.$store.state.user.username,
        content: this.messageContent
      };

      this.ws.send(JSON.stringify(message));
      this.messageContent = '';
    },

    // 发送礼物
    sendGift(gift) {
      const message = {
        type: 'GIFT',
        username: this.$store.state.user.username,
        giftId: gift.id,
        giftName: gift.name,
        price: gift.price
      };

      this.ws.send(JSON.stringify(message));
    },

    // 切换摄像头
    toggleCamera() {
      if (this.rtc.localVideoTrack) {
        this.rtc.localVideoTrack.setEnabled(
          !this.rtc.localVideoTrack.isEnabled()
        );
      }
    },

    // 切换麦克风
    toggleMic() {
      if (this.rtc.localAudioTrack) {
        this.rtc.localAudioTrack.setEnabled(
          !this.rtc.localAudioTrack.isEnabled()
        );
      }
    },

    // 结束直播
    endLive() {
      // 向后端发送结束直播请求
      this.$http
        .post('/api/live/end', {
          liveId: this.liveInfo.id
        })
        .then(() => {
          this.leaveChannel();
          this.$router.push('/');
        });
    }
  }
};
</script>

<style scoped>
.live-room {
  display: flex;
  height: 100vh;
}

.video-container {
  flex: 3;
  background-color: #000;
  position: relative;
}

#local-video,
#remote-video {
  width: 100%;
  height: 100%;
}

.live-info {
  position: absolute;
  top: 10px;
  left: 10px;
  color: white;
  background-color: rgba(0, 0, 0, 0.5);
  padding: 5px 10px;
  border-radius: 5px;
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  border-left: 1px solid #ddd;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}

.message {
  margin-bottom: 10px;
}

.username {
  font-weight: bold;
  margin-right: 5px;
}

.input-area {
  display: flex;
  padding: 10px;
  border-top: 1px solid #ddd;
}

.input-area input {
  flex: 1;
  padding: 5px;
  margin-right: 10px;
}

.gift-container {
  display: flex;
  overflow-x: auto;
  padding: 10px;
  border-top: 1px solid #ddd;
}

.gift-item {
  margin-right: 10px;
  cursor: pointer;
  text-align: center;
}

.gift-item img {
  width: 50px;
  height: 50px;
  margin-bottom: 5px;
}

.host-controls {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
}

.host-controls button {
  margin: 0 5px;
  padding: 8px 15px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
