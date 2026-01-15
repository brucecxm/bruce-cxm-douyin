<template>
  <div class="register-container">
    <div class="register-header">
      <h2>注册二手市场</h2>
      <p>买卖闲置，就上二手市场</p>
    </div>

    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <input
          type="text"
          v-model="username"
          placeholder="请设置用户名"
          required
          maxlength="20"
        />
      </div>

      <div class="form-group">
        <input
type="email"
v-model="email" placeholder="请输入邮箱"
required />
      </div>

      <div class="form-group">
        <div class="password-input">
          <input
            :type="showPassword ? 'text' : 'password'"
            v-model="password"
            placeholder="请设置密码"
            required
            minlength="6"
          />
          <span class="toggle-password" @click="showPassword = !showPassword">
            {{ showPassword ? '隐藏' : '显示' }}
          </span>
        </div>
      </div>

      <div class="form-group">
        <div class="password-input">
          <input
            :type="showConfirmPassword ? 'text' : 'password'"
            v-model="confirmPassword"
            placeholder="请确认密码"
            required
            minlength="6"
          />
          <span
            class="toggle-password"
            @click="showConfirmPassword = !showConfirmPassword"
          >
            {{ showConfirmPassword ? '隐藏' : '显示' }}
          </span>
        </div>
      </div>

      <div class="agreement">
        <input type="checkbox" id="agreement" v-model="agree" required />
        <label for="agreement">
          我已阅读并同意<a
href="#"
@click.prevent="showAgreement"
          >《用户协议》</a
          >和<a href="#" @click.prevent="showPrivacy">《隐私政策》</a>
        </label>
      </div>

      <button type="submit" class="register-btn">注册</button>

      <div class="login-link">
        <span>已有账号？</span>
        <a href="#" @click.prevent="handleLogin">立即登录</a>
      </div>
    </form>
  </div>
</template>

<script>
import { post } from '../../utils/apiUtils';
import routerUtils from '../../utils/routerUtils';
import nativeUtils from '../../utils/nativeUtils';

export default {
  name: 'RegisterApp',
  data() {
    return {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      showPassword: false,
      showConfirmPassword: false,
      agree: false
    };
  },
  mounted() {
    // 页面加载完成后隐藏标题栏
    if (window.Android) {
      window.Android.hideTitleBar();
    }
  },
  methods: {
    async handleRegister() {
      if (this.password !== this.confirmPassword) {
        nativeUtils.showToast('两次输入的密码不一致', 1500);
        return;
      }

      try {
        // 调用注册API
        nativeUtils.showLoading('注册中...');
        const response = await post('/user/register', {
          username: this.username,
          password: this.password,
          email: this.email,
          nickname: this.username // 使用用户名作为默认昵称
        });

        if (response.code === 0) {
          nativeUtils.hideLoading();
          nativeUtils.showToast('注册成功', 1500);

          // 跳转到登录页面
          routerUtils.navigateTo('/login.html');
        } else {
          nativeUtils.hideLoading();
          nativeUtils.showToast(response.message || '注册失败', 1500);
        }
      } catch (error) {
        nativeUtils.hideLoading();
        nativeUtils.showToast(error.message || '注册失败', 1500);
      }
    },

    showAgreement() {
      nativeUtils.showToast('用户协议', 1500);
    },

    showPrivacy() {
      nativeUtils.showToast('隐私政策', 1500);
    },

    handleLogin() {
      routerUtils.navigateTo('/login.html');
    }
  }
};
</script>

<style scoped>
.register-container {
  padding: 20px;
  background-color: #fff;
  min-height: 100vh;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
  padding-top: 40px;
}

.register-header h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.register-header p {
  font-size: 14px;
  color: #666;
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

.code-input,
.password-input {
  position: relative;
  display: flex;
  align-items: center;
}

.code-input input,
.password-input input {
  flex: 1;
  padding-right: 100px;
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

.toggle-password {
  position: absolute;
  right: 15px;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}

.agreement {
  display: flex;
  align-items: center;
  margin: 20px 0;
  font-size: 14px;
  color: #666;
}

.agreement input[type='checkbox'] {
  width: auto;
  margin-right: 10px;
}

.agreement a {
  color: #ff4400;
  text-decoration: none;
}

.agreement a:hover {
  text-decoration: underline;
}

.register-btn {
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

.register-btn:hover {
  background-color: #ff5500;
}

.login-link {
  text-align: center;
  margin-top: 30px;
  font-size: 14px;
  color: #666;
}

.login-link a {
  color: #ff4400;
  text-decoration: none;
  margin-left: 5px;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
