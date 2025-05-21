<template>
  <div class="settings" style="width: 414px; height: 896px">
    <div class="header">
      <button class="back-btn" @click="goBack">
        <i class="fas fa-chevron-left"></i>
      </button>
      <span class="title">设置</span>
    </div>
    <div class="search-bar">
      <input type="text" placeholder="搜索" />
    </div>
    <div class="settings-group">
      <div
        class="settings-item"
        v-for="(item, index) in accountSettings"
        :key="index"
      >
        >
        <i :class="item.icon"></i>
        <span>{{ item.label }}</span>
        <i class="fas fa-chevron-right"></i>
      </div>
    </div>
    <div class="settings-group">
      <div
        class="settings-item"
        v-for="(item, index) in generalSettings"
        :key="index"
      >
        <i :class="item.icon"></i>
        <span>{{ item.label }}</span>
        <i class="fas fa-chevron-right"></i>
      </div>
    </div>
    <div class="settings-group">
      <div
        class="settings-item"
        v-for="(item, index) in aboutSettings"
        :key="index"
      >
        >
        <i :class="item.icon"></i>
        <span>{{ item.label }}</span>
        <i class="fas fa-chevron-right"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Settings',
  data() {
    return {
      accountSettings: [
        { label: '账号与安全', icon: 'fas fa-user-circle' },
        { label: '隐私设置', icon: 'fas fa-lock' },
        { label: '支付设置', icon: 'fas fa-wallet' }
      ],
      generalSettings: [
        { label: '通用设置', icon: 'fas fa-cogs' },
        { label: '通知设置', icon: 'fas fa-bell' },
        { label: '聊天设置', icon: 'fas fa-comments' },
        { label: '播放设置', icon: 'fas fa-play-circle' },
        { label: '动态壁纸', icon: 'fas fa-image' },
        { label: '背景设置', icon: 'fas fa-paint-brush' },
        { label: '长辈模式', icon: 'fas fa-users' },
        { label: '字体大小', icon: 'fas fa-font' },
        { label: '清理缓存', icon: 'fas fa-trash' }
      ],
      aboutSettings: [
        { label: '反馈与帮助', icon: 'fas fa-pencil-alt' },
        { label: '了解与管理广告推送', icon: 'fas fa-ad' },
        { label: '抖音视听中心', icon: 'fas fa-video' },
        { label: '资质证照', icon: 'fas fa-certificate' },
        { label: '用户协议', icon: 'fas fa-file-contract' },
        { label: '隐私政策及儿童隐私政策', icon: 'fas fa-user-secret' },
        { label: '应用权限', icon: 'fas fa-shield-alt' },
        { label: '个人信息管理', icon: 'fas fa-user-cog' },
        { label: '开源软件声明', icon: 'fas fa-code-branch' },
        { label: '关于抖音', icon: 'fas fa-info-circle' }
      ]
    };
  },
  methods: {
    goBack() {
      this.$router.push('/');
    }
  }
};
</script>

<style scoped>
.settings {
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.header {
  display: flex;
  align-items: center;
  padding: 16px;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
}

.back-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  margin-right: 10px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.search-bar {
  padding: 10px;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
}

.search-bar input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
}

.settings-group {
  margin-top: 10px;
  background-color: #fff;
  border-top: 1px solid #e0e0e0;
}

.settings-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid #e0e0e0;
  cursor: pointer;
}

.settings-item i {
  font-size: 18px;
  margin-right: 10px;
}

.settings-item span {
  flex-grow: 1;
  font-size: 16px;
}
</style>
