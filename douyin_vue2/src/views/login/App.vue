<template>
  <div class="login-container">
    <div class="login-header">
      <h2>登录二手市场</h2>
      <p>买卖闲置，就上二手市场</p>
    </div>

    <div class="login-tabs">
      <div
        class="tab-item"
        :class="{ active: loginType === 'phone' }"
        @click="loginType = 'phone'"
      >
        手机号登录
      </div>
      <div
        class="tab-item"
        :class="{ active: loginType === 'password' }"
        @click="loginType = 'password'"
      >
        密码登录
      </div>
    </div>

    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <input
          type="tel"
          v-if="loginType === 'phone'"
          v-model="phone"
          placeholder="请输入手机号"
          required
          maxlength="11"
        />
        <input
          type="text"
          v-else
          v-model="username"
          placeholder="请输入用户名或手机号"
          required
        />
      </div>

      <div class="form-group">
        <div class="password-input" v-if="loginType === 'password'">
          <input
            :type="showPassword ? 'text' : 'password'"
            v-model="password"
            placeholder="请输入密码"
            required
          />
          <span class="toggle-password" @click="showPassword = !showPassword">
            {{ showPassword ? '隐藏' : '显示' }}
          </span>
        </div>
        <div class="code-input" v-else>
          <input
            type="text"
            v-model="code"
            placeholder="请输入验证码"
            required
            maxlength="6"
          />
          <button
            type="button"
            class="get-code-btn"
            :disabled="countdown > 0"
            @click="getCode"
          >
            {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
          </button>
        </div>
      </div>

      <div class="forgot-password" v-if="loginType === 'password'">
        <a href="#" @click.prevent="handleForgotPassword">忘记密码？</a>
      </div>

      <button type="submit" class="login-btn">登录</button>

      <div class="other-login">
        <p>其他登录方式</p>
        <div class="login-icons">
          <span class="icon wechat">微信</span>
          <span class="icon qq">QQ</span>
          <span class="icon weibo">微博</span>
        </div>
      </div>

      <div class="register-link">
        <span>还没有账号？</span>
        <a href="#" @click.prevent="handleRegister">立即注册</a>
      </div>
    </form>
  </div>
</template>

<script>
import routerUtils from '../../utils/routerUtils';
import nativeUtils from '../../utils/nativeUtils';
import { post } from '../../utils/apiUtils';

export default {
  name: 'LoginApp',
  data() {
    return {
      loginType: 'password', // 'phone' 或 'password'
      phone: '',
      code: '',
      countdown: 0,
      username: '',
      password: '',
      showPassword: false
    };
  },
  mounted() {
    // 页面加载完成后隐藏标题栏
    if (window.Android) {
      window.Android.hideTitleBar();
    }
  },
  methods: {
    async handleLogin() {
      // 这里可以添加登录逻辑
      if (this.loginType === 'phone') {
        // 手机号登录验证
        if (!/^1[3-9]\d{9}$/.test(this.phone)) {
          nativeUtils.showToast('请输入正确的手机号', 1500);
          return;
        }
        if (!this.code || this.code.length !== 6) {
          nativeUtils.showToast('请输入6位验证码', 1500);
          return;
        }

        // 调用手机号登录API
        try {
          nativeUtils.showLoading('登录中...');
          const response = await post('/newshop/user/login/phone', {
            phone: this.phone,
            code: this.code
          });

          if (response.code === 0) {
            // 请求成功
            nativeUtils.hideLoading();
            nativeUtils.showToast('登录成功', 1500);

            // 保存用户信息和token
            const data = response.data;
            localStorage.setItem('user', JSON.stringify(data.user));
            localStorage.setItem('token', data.token || '');

            // 跳转到主页
            routerUtils.navigateTo('/newshop.html');
          } else {
            // 请求失败
            nativeUtils.hideLoading();
            nativeUtils.showToast(response.message || '登录失败', 1500);
          }
        } catch (error) {
          console.error('登录失败:', error);
          nativeUtils.hideLoading();
          nativeUtils.showToast(error.message || '登录失败', 1500);
        }
      } else {
        // 密码登录验证
        if (!this.username.trim()) {
          nativeUtils.showToast('请输入用户名', 1500);
          return;
        }
        if (!this.password || this.password.length < 6) {
          nativeUtils.showToast('密码不能少于6位', 1500);
          return;
        }

        try {
          // 真实登录API调用
          nativeUtils.showLoading('登录中...');
          const response = await post('/newshop/user/login', {
            username: this.username,
            password: this.password
          });

          // 处理后端返回的原始数据
          if (response.code === 0) {
            // 请求成功
            nativeUtils.hideLoading();
            nativeUtils.showToast('登录成功', 1500);

            // 保存用户信息和token
            const data = response.data;
            localStorage.setItem('user', JSON.stringify(data.user));
            localStorage.setItem('token', data.token || '');

            // 跳转到主页
            routerUtils.navigateTo('/newshop.html');
          } else {
            // 请求失败
            nativeUtils.hideLoading();
            nativeUtils.showToast(response.message || '登录失败', 1500);
          }
        } catch (error) {
          console.error('登录失败:', error);
          nativeUtils.hideLoading();
          nativeUtils.showToast(error.message || '登录失败', 1500);
        }
      }
    },

    async getCode() {
      if (!/^1[3-9]\d{9}$/.test(this.phone)) {
        nativeUtils.showToast('请输入正确的手机号', 1500);
        return;
      }

      try {
        // 调用获取验证码API
        const response = await post('/newshop/user/sendCode', {
          phone: this.phone
        });

        if (response.code === 0) {
          nativeUtils.showToast('验证码已发送，请注意查收', 1500);
          // 开始倒计时
          this.countdown = 60;
          const timer = setInterval(() => {
            this.countdown--;
            if (this.countdown <= 0) {
              clearInterval(timer);
            }
          }, 1000);
        } else {
          nativeUtils.showToast(response.message || '发送失败', 1500);
        }
      } catch (error) {
        console.error('发送验证码失败:', error);
        nativeUtils.showToast('发送失败，请稍后重试', 1500);
      }
    },

    handleForgotPassword() {
      nativeUtils.showToast('忘记密码功能开发中', 1500);
    },

    handleRegister() {
      // 跳转到注册页面
      routerUtils.navigateTo('/newshop/register.html');
    }
  }
};
</script>

<style scoped>
.login-container {
  padding: 20px;
  background-color: #fff;
  min-height: 100vh;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
  padding-top: 40px;
}

.login-header h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.login-header p {
  font-size: 14px;
  color: #666;
}

.login-tabs {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 10px 0;
  font-size: 16px;
  color: #666;
  cursor: pointer;
  position: relative;
}

.tab-item.active {
  color: #ff4400;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 2px;
  background-color: #ff4400;
}

.form-group {
  margin-bottom: 15px;
}

input {
  width: 100%;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  box-sizing: border-box;
  outline: none;
}

input:focus {
  border-color: #ff4400;
}

.password-input,
.code-input {
  position: relative;
  display: flex;
  align-items: center;
}

.password-input input,
.code-input input {
  flex: 1;
  padding-right: 100px;
}

.toggle-password {
  position: absolute;
  right: 15px;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}

.get-code-btn {
  position: absolute;
  right: 5px;
  top: 5px;
  padding: 8px 12px;
  background-color: #fff;
  color: #ff4400;
  border: 1px solid #ff4400;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  outline: none;
}

.get-code-btn:disabled {
  color: #ccc;
  border-color: #ccc;
  cursor: not-allowed;
}

.forgot-password {
  text-align: right;
  margin-bottom: 20px;
}

.forgot-password a {
  color: #666;
  font-size: 14px;
  text-decoration: none;
}

.forgot-password a:hover {
  color: #ff4400;
}

.login-btn {
  width: 100%;
  padding: 15px;
  background-color: #ff4400;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  outline: none;
}

.login-btn:hover {
  background-color: #ff5500;
}

.other-login {
  margin: 30px 0;
  text-align: center;
}

.other-login p {
  color: #999;
  font-size: 14px;
  margin-bottom: 20px;
  position: relative;
}

.other-login p::before,
.other-login p::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 100px;
  height: 1px;
  background-color: #eee;
}

.other-login p::before {
  left: 0;
}

.other-login p::after {
  right: 0;
}

.login-icons {
  display: flex;
  justify-content: center;
  gap: 40px;
}

.icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  cursor: pointer;
}

.icon.wechat {
  background-color: #07c160;
  color: white;
}

.icon.qq {
  background-color: #12b7f5;
  color: white;
}

.icon.weibo {
  background-color: #e6162d;
  color: white;
}

.register-link {
  text-align: center;
  font-size: 14px;
  color: #666;
}

.register-link a {
  color: #ff4400;
  text-decoration: none;
  margin-left: 5px;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
