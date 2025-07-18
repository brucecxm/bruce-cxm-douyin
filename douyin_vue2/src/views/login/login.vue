<template>
  <div class="login-screen">
    <div class="header">
      <button class="close-btn" @click="closeScreen">×</button>
      <div class="help-settings">
        <span>帮助与设置</span>
      </div>
    </div>
    <div class="content">
      <p class="notice">登录后即可点赞喜欢的内容</p>
      <div v-if="!isRegistering" class="login-form">
        <div class="input-group">
          <input
            type="text"
            v-model="loginpojo.username"
            @blur="validateEmail"
            placeholder="请输入用户名"
          />
          <input
            v-if="!isPasswordLogin"
            type="password"
            v-model="loginpojo.password"
            placeholder="请输入密码"
          />
        </div>
        <div class="input-group" v-if="isPasswordLogin">
          <input
            type="text"
            v-model="emailVerificationCode"
            placeholder="请输入邮箱发来的验证码"
          />
          <button
            @click="getEmailVerificationCode"
            :disabled="isCodeSent"
            class="get-code-btn"
          >
            {{ countdown > 0 ? countdown + '秒' : '获取验证码' }}
          </button>
        </div>
        <div class="input-group" v-if="!isPasswordLogin">
          <input
            type="text"
            v-model="verificationCode"
            placeholder="请输入验证码"
          />
          <button
            @click="getVerificationCode"
            :disabled="isCodeSent"
            class="get-code-btn"
          >
            {{ countdown > 0 ? countdown + '秒' : '获取验证码' }}
          </button>
          <img :src="captchaUrl" alt="验证码" v-if="isShow" />
        </div>

        <p class="terms">
          <input type="checkbox" v-model="isAgreed" />
          已阅读并同意协议，运营商将对你的手机号进行验证
        </p>
        <button class="login-btn" @click="verifyPhoneNumber">验证并登录</button>
        <div class="login-options">
          <button @click="switchToPasswordLogin">邮箱登录</button>
          <button @click="switchToRegister">切换到注册</button>
        </div>
      </div>

      <div v-if="isRegistering" class="login-form">
        <div class="input-group">
          <input
            type="text"
            v-model="loginpojo.username"
            @blur="validateEmail"
            placeholder="请输入用户名"
          />
          <input
            type="password"
            v-model="loginpojo.password"
            placeholder="请输入密码"
          />
          <input
            type="password"
            v-model="loginpojo.confirmPassword"
            placeholder="确认密码"
          />
        </div>
        <div class="input-group">
          <input
            type="text"
            v-model="verificationCode"
            placeholder="请输入验证码"
          />
          <button
            @click="getVerificationCode"
            :disabled="isCodeSent"
            class="get-code-btn"
          >
            {{ countdown > 0 ? countdown + '秒' : '获取验证码' }}
          </button>
          <img :src="captchaUrl" alt="验证码" v-if="isShow" />
        </div>

        <p class="terms">
          <input type="checkbox" v-model="isAgreed" />
          已阅读并同意协议，运营商将对你的手机号进行验证
        </p>
        <button class="login-btn" @click="register">注册并登录</button>
        <div class="login-options">
          <button @click="switchToLogin">切换到登录</button>
        </div>
      </div>

      <p class="recovery-text">手机号不用了？ <a href="#">找回账号</a></p>
    </div>
  </div>
</template>

<script>
import {
  userLoginService,
  userRegisterService,
  getEmailVerificationCodeService,
  getVerificationCodeService
} from '../../api/user';

export default {
  name: 'Login',
  data() {
    return {
      loginType: 'password', //
      loginpojo: {
        username: '',
        password: '',
        confirmPassword: '' // for registration
      },
      emailVerificationCode: '', // 用于保存验证码
      verificationCode: '', // 用于保存验证码
      emailPattern: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$',
      isPasswordLogin: false,
      isAgreed: false, // 协议同意状态
      isRegistering: false, // 是否处于注册页面
      countryCodes: [], // 用来存储读取的国家代码
      selectedCode: '+86', // 用来存储选择的代码
      countdown: 0, // 用于倒计时
      isCodeSent: false, // 是否已发送验证码
      isShow: false, // 是否为浅色模式
      captchaUrl: '' // 用来绑定到 <img>
    };
  },
  mounted() {},

  methods: {
    validateEmail() {
      const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if (!emailRegex.test(this.loginpojo.username)) {
        this.$message({
          message: '请输入有效的邮箱地址',
          type: 'warning'
        });
      }
    },

    closeScreen() {
      this.$router.go(-1);
      console.log('关闭界面');
    },

    clearPhoneNumber() {
      this.loginpojo.username = '';
      this.loginpojo.password = '';
      this.loginpojo.confirmPassword = '';
    },
    async getEmailVerificationCode() {
      if (!this.loginpojo.username) {
        this.$message({
          message: '请输入邮箱',
          type: 'warning'
        });
        return;
      }
      try {
        const response = await getEmailVerificationCodeService({
          email: this.loginpojo.username
        });

        if (response.data.code === 1) {
          this.$message({
            message: '获取验证码失败，请稍后再试',
            type: 'error'
          });
          return;
        }

        this.isCodeSent = true;
        this.countdown = 60;
        const timer = setInterval(() => {
          if (this.countdown === 0) {
            clearInterval(timer);
            this.isCodeSent = false;
          } else {
            this.countdown--;
          }
        }, 1000);
      } catch (error) {
        console.error('获取验证码失败：', error);
      }
    },
    async getVerificationCode() {
      if (!this.loginpojo.username) {
        this.$message({
          message: '请输入邮箱',
          type: 'warning'
        });
        return;
      }
      try {
        const response = await getVerificationCodeService(
          this.loginpojo.username
        );

        if (response.data.code === 1) {
          this.$message({
            message: '获取验证码失败，请稍后再试',
            type: 'error'
          });
          return;
        }

        const blob = new Blob([response.data], { type: 'image/png' });
        this.captchaUrl = URL.createObjectURL(blob);
        this.isShow = true;
        this.isCodeSent = true;
        this.countdown = 60;
        const timer = setInterval(() => {
          if (this.countdown === 0) {
            clearInterval(timer);
            this.isCodeSent = false;
          } else {
            this.countdown--;
          }
        }, 1000);
      } catch (error) {
        console.error('获取验证码失败：', error);
      }
    },

    async verifyPhoneNumber() {
      if (!this.isAgreed) {
        this.$message({
          message: '请勾选用户协议以继续登录。',
          type: 'warning'
        });
        return;
      }

      console.log('验证手机号:', this.loginpojo);

      try {
        const response = await userLoginService({
          ...this.loginpojo,
          verificationCode: this.verificationCode,
          loginType: this.loginType,
          emailVerificationCode: this.emailVerificationCode
        });
        debugger;

        if (response.data.code === 1) {
          this.$message({
            message: response.data.msg || '登录失败',
            type: 'error'
          });
        } else {
          this.$store
            .dispatch('userInfo/setUserInfo', response.data.data.userInfo)
            .then(() => {
              debugger;
              console.log(
                '存入后的 userInfo:',
                this.$store.state.userInfo.userInfo
              );
            });
          debugger;
          console.log(
            '存入后的 userInfo:',
            this.$store.state.userInfo.userInfo
          );
          const token = response.data.data.token;
          // ✅ 使用 Vuex 设置 token
          this.$store.dispatch('token/setToken', token);

          localStorage.setItem('token', token); // 如不再需要，也可去掉
          this.$router.push('/');
        }
      } catch (error) {
        console.error('登录失败：', error);
        this.$message({
          message: '登录请求失败，请检查网络或稍后重试。',
          type: 'error'
        });
      }
    },
    switchToPasswordLogin() {
      this.isPasswordLogin = true;
      this.loginType = 'email';
    },
    switchToRegister() {
      this.isRegistering = true;
    },
    switchToLogin() {
      this.isRegistering = false;
    },

    async register() {
      if (!this.isAgreed) {
        this.$message({
          message: '请勾选用户协议以继续注册。',
          type: 'warning'
        });
        return;
      }

      if (this.loginpojo.password !== this.loginpojo.confirmPassword) {
        this.$message({
          message: '密码和确认密码不匹配',
          type: 'warning'
        });
        return;
      }

      if (!this.verificationCode) {
        this.$message({
          message: '请输入验证码',
          type: 'warning'
        });
        return;
      }

      console.log('注册手机号:', this.loginpojo);
      try {
        const response = await userRegisterService({
          ...this.loginpojo,
          verificationCode: this.verificationCode
        });
        if (response.data.code === 1) {
          this.$message({
            message: '注册失败，请稍后再试',
            type: 'error'
          });
        } else {
          this.verifyPhoneNumber(); // 注册成功后自动登录
        }
      } catch (error) {
        console.error('注册失败：', error);
        this.$message({
          message: '注册请求失败，请稍后重试。',
          type: 'error'
        });
      }
    }
  }
};
</script>
<style scoped>
.login-screen {
  display: flex;
  flex-direction: column;
  padding: 0 16px;
  background: #fff;
  height: 100vh;
  position: relative;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.close-btn {
  font-size: 28px;
  color: #666;
  background: none;
  border: none;
  padding: 8px;
}

.help-settings span {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.content {
  flex: 1;
  padding: 20px 16px;
}

.notice {
  font-size: 22px;
  color: #333;
  margin: 24px 0;
  font-weight: 500;
}

.login-form {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
}

.input-group {
  margin-bottom: 16px;
  position: relative;
}

.input-group input {
  width: 100%;
  height: 48px;
  padding: 0 16px;
  border: 1px solid #eee;
  border-radius: 8px;
  font-size: 16px;
  background: #f8f8f8;
  margin: 8px 0;
  transition: all 0.2s;
}

.input-group input:focus {
  background: #fff;
  border-color: #ff4757;
  outline: none;
}

.get-code-btn {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  height: 36px;
  padding: 0 12px;
  background: #fff;
  border: 1px solid #ff4757;
  border-radius: 6px;
  color: #ff4757;
  font-size: 14px;
  transition: all 0.2s;
}

.get-code-btn:disabled {
  background: #f8f8f8;
  border-color: #eee;
  color: #999;
}

.terms {
  font-size: 12px;
  color: #666;
  line-height: 1.6;
  margin: 24px 0;
  display: flex;
  align-items: center;
}

.terms input[type='checkbox'] {
  margin-right: 8px;
  width: 16px;
  height: 16px;
}

.terms a {
  color: #ff4757;
  text-decoration: none;
}

.login-btn {
  width: 100%;
  height: 48px;
  background: linear-gradient(45deg, #ff4757, #ff6b81);
  color: white;
  border: none;
  border-radius: 24px;
  font-size: 16px;
  font-weight: 500;
  margin: 16px 0;
  box-shadow: 0 4px 12px rgba(255, 71, 87, 0.2);
  transition: all 0.2s;
}

.login-btn:active {
  transform: scale(0.98);
}

.login-options {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 24px;
}

.login-options button {
  background: none;
  border: none;
  color: #ff4757;
  font-size: 14px;
  font-weight: 500;
  padding: 8px;
}

.recovery-text {
  font-size: 12px;
  color: #666;
  margin-top: 24px;
}

.recovery-text a {
  color: #ff4757;
  text-decoration: none;
}

/* 验证码图片样式 */
.input-group img {
  width: 120px;
  height: 40px;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  border-radius: 4px;
  border: 1px solid #eee;
}

/* 注册表单调整 */
.login-form.register-form .input-group input {
  margin: 6px 0;
}

@media (max-width: 375px) {
  .input-group input {
    height: 44px;
    font-size: 14px;
  }

  .login-btn {
    height: 44px;
    font-size: 15px;
  }
}
</style>
