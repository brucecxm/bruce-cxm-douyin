<template>
  <div class="settings-page">
    <!-- 顶部导航 -->
    <header class="settings-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">设置</div>
    </header>

    <!-- 设置列表 -->
    <div class="settings-list">
      <!-- 账号与安全 -->
      <div class="setting-section">
        <div class="section-title">账号与安全</div>
        <div class="setting-item" @click="goToAccountSecurity">
          <div class="setting-icon">🔐</div>
          <div class="setting-text">账号与安全</div>
          <div class="setting-arrow">→</div>
        </div>
        <div class="setting-item" @click="goToBindPhone">
          <div class="setting-icon">📱</div>
          <div class="setting-text">绑定手机</div>
          <div class="setting-value">{{ userInfo.phone }}</div>
          <div class="setting-arrow">→</div>
        </div>
        <div class="setting-item" @click="goToChangePassword">
          <div class="setting-icon">🔑</div>
          <div class="setting-text">修改密码</div>
          <div class="setting-arrow">→</div>
        </div>
      </div>

      <!-- 隐私设置 -->
      <div class="setting-section">
        <div class="section-title">隐私设置</div>
        <div class="setting-item" @click="goToPrivacy">
          <div class="setting-icon">👁️</div>
          <div class="setting-text">隐私设置</div>
          <div class="setting-arrow">→</div>
        </div>
        <div class="setting-item" @click="goToBlacklist">
          <div class="setting-icon">🚫</div>
          <div class="setting-text">黑名单</div>
          <div class="setting-arrow">→</div>
        </div>
      </div>

      <!-- 通知设置 -->
      <div class="setting-section">
        <div class="section-title">通知设置</div>
        <div class="setting-item">
          <div class="setting-icon">🔔</div>
          <div class="setting-text">消息通知</div>
          <div class="toggle-switch">
            <input
              type="checkbox"
              class="toggle-checkbox"
              v-model="settings.notifications"
            />
            <div class="toggle-slider"></div>
          </div>
        </div>
        <div class="setting-item">
          <div class="setting-icon">📢</div>
          <div class="setting-text">系统通知</div>
          <div class="toggle-switch">
            <input
              type="checkbox"
              class="toggle-checkbox"
              v-model="settings.systemNotifications"
            />
            <div class="toggle-slider"></div>
          </div>
        </div>
      </div>

      <!-- 通用设置 -->
      <div class="setting-section">
        <div class="section-title">通用设置</div>
        <div class="setting-item" @click="goToLanguage">
          <div class="setting-icon">🌐</div>
          <div class="setting-text">语言</div>
          <div class="setting-value">简体中文</div>
          <div class="setting-arrow">→</div>
        </div>
        <div class="setting-item" @click="goToClearCache">
          <div class="setting-icon">🗑️</div>
          <div class="setting-text">清除缓存</div>
          <div class="setting-value">{{ cacheSize }}</div>
          <div class="setting-arrow">→</div>
        </div>
        <div class="setting-item" @click="goToWebSocketTest">
          <div class="setting-icon">🖥️</div>
          <div class="setting-text">WebSocket测试</div>
          <div class="setting-arrow">→</div>
        </div>
        <div class="setting-item" @click="goToAbout">
          <div class="setting-icon">ℹ️</div>
          <div class="setting-text">关于我们</div>
          <div class="setting-value">v{{ appVersion }}</div>
          <div class="setting-arrow">→</div>
        </div>
      </div>

      <!-- 退出登录 -->
      <div class="logout-section">
        <button class="logout-btn" @click="logout">退出登录</button>
      </div>
    </div>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';

export default {
  name: 'SettingsPage',
  data() {
    return {
      userInfo: {
        phone: '138****8888'
      },
      settings: {
        notifications: true,
        systemNotifications: true
      },
      cacheSize: '23.5MB',
      appVersion: '1.0.0'
    };
  },
  mounted() {
    // 设置页面标题
    nativeUtils.setTitleBar('设置');
    // 加载用户信息
    this.loadUserInfo();
    // 加载设置
    this.loadSettings();
  },
  methods: {
    // 从localStorage加载用户信息
    loadUserInfo() {
      try {
        const userStr = localStorage.getItem('user');
        if (userStr) {
          const user = JSON.parse(userStr);
          this.userInfo = {
            ...user,
            phone: user.phone ? this.formatPhone(user.phone) : '未绑定'
          };
        }
      } catch (error) {
        console.error('加载用户信息失败:', error);
      }
    },

    // 格式化手机号显示
    formatPhone(phone) {
      return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
    },

    // 加载设置
    loadSettings() {
      try {
        const settingsStr = localStorage.getItem('settings');
        if (settingsStr) {
          this.settings = JSON.parse(settingsStr);
        }
      } catch (error) {
        console.error('加载设置失败:', error);
      }
    },

    // 保存设置
    saveSettings() {
      try {
        localStorage.setItem('settings', JSON.stringify(this.settings));
        nativeUtils.showToast('设置已保存', 1500);
      } catch (error) {
        console.error('保存设置失败:', error);
      }
    },

    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1);
    },

    // 跳转到账号与安全页面
    goToAccountSecurity() {
      nativeUtils.showToast('账号与安全页面开发中', 1500);
    },

    // 跳转到绑定手机页面
    goToBindPhone() {
      nativeUtils.showToast('绑定手机页面开发中', 1500);
    },

    // 跳转到修改密码页面
    goToChangePassword() {
      nativeUtils.showToast('修改密码页面开发中', 1500);
    },

    // 跳转到隐私设置页面
    goToPrivacy() {
      nativeUtils.showToast('隐私设置页面开发中', 1500);
    },

    // 跳转到黑名单页面
    goToBlacklist() {
      nativeUtils.showToast('黑名单页面开发中', 1500);
    },

    // 跳转到语言设置页面
    goToLanguage() {
      nativeUtils.showToast('语言设置页面开发中', 1500);
    },

    // 清除缓存
    goToClearCache() {
      if (confirm('确定要清除缓存吗？')) {
        // 模拟清除缓存
        nativeUtils.showLoading('清除中...');
        setTimeout(() => {
          this.cacheSize = '0MB';
          nativeUtils.hideLoading();
          nativeUtils.showToast('缓存已清除', 1500);
        }, 1000);
      }
    },

    // 跳转到关于我们页面
    goToAbout() {
      nativeUtils.showToast('关于我们页面开发中', 1500);
    },

    // 跳转到WebSocket测试页面
    goToWebSocketTest() {
      routerUtils.navigateTo('/newshop/websocket-test.html');
    },

    // 退出登录
    logout() {
      if (confirm('确定要退出登录吗？')) {
        try {
          // 清除localStorage数据
          localStorage.removeItem('user');
          localStorage.removeItem('token');
          localStorage.removeItem('cart');
          localStorage.removeItem('favorites');

          // 跳转到登录页面
          routerUtils.navigateTo('/newshop/login.html');
        } catch (error) {
          console.error('退出登录失败:', error);
          routerUtils.navigateTo('/newshop/login.html');
        }
      }
    }
  }
};
</script>

<style scoped>
.settings-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 顶部导航 */
.settings-header {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-icon {
  font-size: 20px;
  color: #333;
  cursor: pointer;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-left: 20px;
}

/* 设置列表 */
.settings-list {
  padding: 15px;
}

/* 设置分组 */
.setting-section {
  background-color: white;
  border-radius: 10px;
  margin-bottom: 15px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 14px;
  color: #999;
  padding: 15px 15px 10px;
}

/* 设置项 */
.setting-item {
  display: flex;
  align-items: center;
  padding: 15px;
  cursor: pointer;
  border-top: 1px solid #f0f0f0;
}

.setting-item:first-child {
  border-top: none;
}

.setting-icon {
  font-size: 20px;
  margin-right: 15px;
  width: 24px;
  text-align: center;
}

.setting-text {
  flex: 1;
  font-size: 16px;
  color: #333;
}

.setting-value {
  font-size: 14px;
  color: #999;
  margin-right: 10px;
}

.setting-arrow {
  font-size: 16px;
  color: #999;
}

/* 开关组件 */
.toggle-switch {
  position: relative;
  width: 40px;
  height: 20px;
}

.toggle-checkbox {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: 0.4s;
  border-radius: 20px;
}

.toggle-slider:before {
  position: absolute;
  content: '';
  height: 16px;
  width: 16px;
  left: 2px;
  bottom: 2px;
  background-color: white;
  transition: 0.4s;
  border-radius: 50%;
}

.toggle-checkbox:checked + .toggle-slider {
  background-color: #42b983;
}

.toggle-checkbox:checked + .toggle-slider:before {
  transform: translateX(20px);
}

/* 退出登录按钮 */
.logout-section {
  padding: 20px;
}

.logout-btn {
  width: 100%;
  padding: 12px;
  background-color: #ff4444;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #ff6666;
}
</style>
