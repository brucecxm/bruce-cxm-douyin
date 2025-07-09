<template>
  <div class="login-screen">
    <!-- header -->
    <div class="header">
      <button class="close-btn" @click="closeScreen">×</button>
      <div class="help-settings"><span>帮助与设置</span></div>
    </div>

    <div class="content">
      <p class="notice">登录后即可点赞喜欢的内容</p>

      <!-- 登录 -->
      <div v-if="!isRegistering" class="login-form">
        <div class="input-group">
          <input
            type="text"
            v-model="loginPojo.userName"
            placeholder="请输入用户名"
          />
          <input
            v-if="!isPasswordLogin"
            type="password"
            v-model="loginPojo.password"
            placeholder="请输入密码"
          />
        </div>

        <div class="input-group" v-if="isPasswordLogin">
          <input
            type="text"
            v-model="emailVerificationCode"
            placeholder="请输入邮箱验证码"
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
          <img
            :src="captchaUrl"
            @click="getVerificationCode"
            alt="验证码"
            v-if="showCaptcha"
          />
        </div>

        <p class="terms">
          <input type="checkbox" v-model="isAgreed" />已阅读并同意协议
        </p>
        <button class="login-btn" @click="verifyLogin">验证并登录</button>

        <div class="login-options">
          <button @click="switchLoginType">
            {{ isPasswordLogin ? '密码登录' : '邮箱登录' }}
          </button>
          <button @click="switchToRegister">切换到注册</button>
        </div>
      </div>

      <!-- 注册 -->
      <div v-if="isRegistering" class="login-form">
        <div class="input-group">
          <input
            type="text"
            v-model="registerPojo.userName"
            placeholder="请输入用户名"
          />
          <input
            type="password"
            v-model="registerPojo.password"
            placeholder="请输入密码"
          />
          <input
            type="password"
            v-model="registerPojo.confirmPassword"
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
          <img
            :src="captchaUrl"
            @click="getVerificationCode"
            alt="验证码"
            v-if="showCaptcha"
          />
        </div>

        <p class="terms">
          <input type="checkbox" v-model="isAgreed" />已阅读并同意协议
        </p>
        <button class="login-btn" @click="register">注册并登录</button>

        <div class="login-options">
          <button @click="switchToLogin">切换到登录</button>
        </div>
      </div>

      <p class="recovery-text">手机号不用了？<a href="#">找回账号</a></p>
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
      loginPojo: {
        userName: '',
        password: ''
      },
      registerPojo: {
        userName: '',
        password: '',
        confirmPassword: ''
      },
      emailVerificationCode: '',
      verificationCode: '',
      isPasswordLogin: false,
      isAgreed: false,
      isRegistering: false,
      countdown: 0,
      isCodeSent: false,
      showCaptcha: false,
      captchaUrl: ''
    };
  },
  computed: {
    enableValidation() {
      console.log(
        'VITE_ENABLE_REGISTER_VALIDATION:',
        import.meta.env.VITE_ENABLE_REGISTER_VALIDATION
      );
      const flag = import.meta.env.VITE_ENABLE_REGISTER_VALIDATION !== 'false';
      return flag;
      // flag 为 true 时跳过校验，返回 false
      // flag 为 false 时启用校验，返回 true
    }
  },
  methods: {
    closeScreen() {
      this.$router.go(-1);
    },
    startCountdown() {
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
    },
    async getEmailVerificationCode() {
      if (!this.loginPojo.userName) {
        this.$message({ message: '请输入邮箱', type: 'warning' });
        return;
      }
      try {
        const res = await getEmailVerificationCodeService({
          email: this.loginPojo.userName
        });
        if (res.data.code === 1) {
          this.$message({ message: '获取验证码失败', type: 'error' });
          return;
        }
        this.startCountdown();
      } catch (error) {
        console.error(error);
        this.$message({ message: '验证码获取失败', type: 'error' });
      }
    },
    async getVerificationCode() {
      if (!this.loginPojo.userName && !this.registerPojo.userName) {
        this.$message({ message: '请输入用户名', type: 'warning' });
        return;
      }
      try {
        const res = await getVerificationCodeService({
          userName: this.isRegistering
            ? this.registerPojo.userName
            : this.loginPojo.userName,
          uuid: Date.now()
        });
        if (res.data.data.type !== 'image/png') {
          // 将 blob 错误流转为正常 JSON
          const reader = new FileReader();
          reader.readAsText(res.data.data, 'utf-8');
          reader.onload = () => {
            try {
              const json = JSON.parse(reader.result);
              this.$message({
                message: json.msg || '验证码获取失败',
                type: 'error'
              });
            } catch (e) {
              this.$message({ message: '验证码获取失败', type: 'error' });
            }
          };
          return;
        }
        const blob = new Blob([res.data.data], { type: 'image/png' });
        this.captchaUrl = URL.createObjectURL(blob);
        this.showCaptcha = true;
        this.startCountdown();
      } catch (error) {
        console.error(error);
        this.$message({ message: '验证码获取失败', type: 'error' });
      }
    },
    validateRegisterInput() {
      debugger;
      if (!this.enableValidation) return null; // 校验关闭时直接通过

      const { userName, password, confirmPassword } = this.registerPojo;

      // 账号校验：仅要求4-20位字符
      if (!userName) return '请输入账号';
      if (!/^.{4,20}$/.test(userName)) return '账号需4-20位字符';

      // 密码校验：仅要求6-20位，包含至少一种字母和一种数字
      if (!password) return '请输入密码';
      if (!/^(?=.*[a-zA-Z])(?=.*\d).{6,20}$/.test(password))
        return '密码需6-20位，包含字母和数字';

      // 确认密码
      if (password !== confirmPassword) return '两次输入的密码不一致';

      // 验证码校验
      if (!this.verificationCode) return '请输入验证码';
      if (!/^\d{4,6}$/.test(this.verificationCode)) return '验证码格式不正确';

      // 用户协议
      if (!this.isAgreed) return '请勾选用户协议';

      return null; // 验证通过
    },
    async register() {
      const errorMsg = this.validateRegisterInput();
      if (errorMsg) {
        this.$message({ message: errorMsg, type: 'warning' });
        return;
      }
      debugger;
      try {
        const res = await userRegisterService({
          ...this.registerPojo,
          verificationCode: this.verificationCode,
          isCheck: this.enableValidation
        });
        debugger;
        if (res.code === 1) {
          this.$message({ message: res.msg || '注册失败', type: 'error' });
        } else {
          this.isRegistering = false;
          this.loginPojo.userName = this.registerPojo.userName;
          this.loginPojo.password = this.registerPojo.password;
          this.verifyLogin();
        }
      } catch (error) {
        console.error(error);
        this.$message({ message: '注册失败', type: 'error' });
      }
    },
    validateLoginInput() {
      if (!this.enableValidation) return null; // 校验关闭时直接通过
      const { userName, password } = this.loginPojo;
      if (!userName) return '请输入账号';
      if (!password && !this.isPasswordLogin) return '请输入密码';
      if (!this.isAgreed) return '请勾选用户协议';
      return null;
    },
    async verifyLogin() {
      const errorMsg = this.validateLoginInput();
      if (errorMsg) {
        this.$message({ message: errorMsg, type: 'warning' });
        return;
      }
      try {
        const res = await userLoginService({
          ...this.loginPojo,
          verificationCode: this.verificationCode,
          emailVerificationCode: this.emailVerificationCode,
          loginType: this.isPasswordLogin ? 'email' : 'password',
          isCheck: this.enableValidation
        });
        if (res.code === 1) {
          this.$message({ message: res.msg || '登录失败', type: 'error' });
        } else {
          this.$store.dispatch('userInfo/setUserInfo', res.data.userInfo);
          this.$store.dispatch('token/setToken', res.data.token);
          localStorage.setItem('token', res.data.token);
          this.$router.push('/');
        }
      } catch (error) {
        console.error(error);
        // 把下面的这个步骤放到响应拦截器去做
        // 把下面的这个步骤放到响应拦截器去做
        // 把下面的这个步骤放到响应拦截器去做
        // this.$message({ message: '登录失败，请稍后重试', type: 'error' });
      }
    },
    switchLoginType() {
      this.isPasswordLogin = !this.isPasswordLogin;
    },
    switchToRegister() {
      this.isRegistering = true;
    },
    switchToLogin() {
      this.isRegistering = false;
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
  width: 90%;
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
