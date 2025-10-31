<template>
  <div :style="[computedMessageStyle, containerStyle]" class="userinfo">
    <!-- 包裹头像和在线指示点 -->
    <div
      class="avatar-wrapper"
      :style="[avatarWrapperStyle, { width: computedHeight, height: computedHeight }]"
    >
      <div
        class="avatar"
        :style="[
          avatarInnerStyle,
          {
            width: computedHeight,
            height: computedHeight,
            backgroundImage: 'url(' + avatar + ')',
            backgroundSize: 'cover',
            backgroundPosition: 'center'
          }
        ]"
      ></div>
      <!-- 仅当状态为“在线”时显示绿色指示点 -->
      <div v-if="message.status === '在线'" class="online-dot" :style="onlineDotStyle"></div>
    </div>

    <!-- 用户名与状态 -->
    <div class="UserAndStatus" :style="[userAndStatusStyle, { height: computedHeight }]">
      <div class="username" :style="usernameStyle">{{ message.username }}</div>
      <div class="status" :style="statusStyle">{{ message.status }}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    message: {
      type: Object,
      required: true
    },
    containerStyle: {
      type: Object,
      default: () => ({})
    },
    avatarWrapperStyle: {
      type: Object,
      default: () => ({})
    },
    avatarInnerStyle: {
      type: Object,
      default: () => ({})
    },
    onlineDotStyle: {
      type: Object,
      default: () => ({})
    },
    userAndStatusStyle: {
      type: Object,
      default: () => ({})
    },
    usernameStyle: {
      type: Object,
      default: () => ({})
    },
    statusStyle: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      avatar:
        'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
      // 默认宽高值
      defaultWidth: '100vw',
      defaultHeight: '60px'
    };
  },
  computed: {
    // 计算高度，优先使用message中的值，否则使用默认值
    computedHeight() {
      return this.message.height || this.defaultHeight;
    },
    // 计算整体样式，处理宽度默认值
    computedMessageStyle() {
      return {
        width: this.message.width || this.defaultWidth
      };
    }
  },
  created() {
    console.log(this.message);
  }
};
</script>

<style scoped>
.userinfo {
  display: flex;
  flex-direction: row;
  padding: 0px;
  margin: 5px 0;
}

.avatar-wrapper {
  position: relative;
  margin-left: 2%;
}

.avatar {
  border-radius: 50%;
}

.online-dot {
  position: absolute;
  right: 2px;
  bottom: 2px;
  width: 10px;
  height: 10px;
  background-color: #4caf50; /* 绿色 */
  border: 2px solid white;
  border-radius: 50%;
  box-shadow: 0 0 2px rgba(0, 0, 0, 0.3);
}

.UserAndStatus {
  width: 70%;
  margin-left: 2%;
  overflow: hidden;
  padding: 0px;
}

.username {
  width: 70%;
  height: 60%;
  font-size: 60%;
}

.status {
  width: 70%;
  height: 40%;
  font-size: 60%;
}
</style>
