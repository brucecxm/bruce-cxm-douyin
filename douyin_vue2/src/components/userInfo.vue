<template>
  <div :style="message" class="userinfo">
    <!-- 包裹头像和在线指示点 -->
    <div
      class="avatar-wrapper"
      :style="{ width: message.height, height: message.height }"
    >
      <div
        class="avatar"
        :style="{
          width: message.height,
          height: message.height,
          backgroundImage: 'url(' + avatar + ')',
          backgroundSize: 'cover',
          backgroundPosition: 'center'
        }"
      ></div>
      <!-- 仅当状态为“在线”时显示绿色指示点 -->
      <div v-if="message.status === '在线'" class="online-dot"></div>
    </div>

    <!-- 用户名与状态 -->
    <div class="UserAndStatus" :style="{ height: message.height }">
      <div class="username">{{ message.username }}</div>
      <div class="status">{{ message.status }}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    message: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      avatar:
        'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280'
    };
  }
};
</script>

<style scoped>
.userinfo {
  width: 100vw;
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
