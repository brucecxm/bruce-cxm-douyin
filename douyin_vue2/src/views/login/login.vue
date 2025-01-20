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
                    <select class="country-code">
                        <option value="+86">+86</option>
                        <option value="+5">+5</option>
                        <option value="+1">+1</option>
                        <option value="+7">+7</option>
                        <option value="+6">+6</option>
                    </select>
                    <input type="tel" v-model="loginpojo.username" placeholder="请输入手机号" />
                    <input type="password" v-model="loginpojo.password" placeholder="请输入密码" />
                    <button class="clear-btn" @click="clearPhoneNumber">×</button>
                </div>

                <p class="terms">
                    <input type="checkbox" v-model="isAgreed" /> 已阅读并同意 <a href="#">用户协议</a> 和 <a href="#">隐私政策</a> 以及
                    <a href="#">运营商服务协议</a>
                    ，运营商将对你的手机号进行验证
                </p>
                <button class="login-btn" @click="verifyPhoneNumber">验证并登录</button>
                <div class="login-options">
                    <button @click="switchToPasswordLogin">密码登录</button>
                    <button @click="switchToRegister">切换到注册</button>
                </div>
            </div>

            <div v-if="isRegistering" class="login-form">
                <div class="input-group">
                    <select class="country-code">
                        <option value="+86">+86</option>
                        <option value="+5">+5</option>
                        <option value="+1">+1</option>
                        <option value="+7">+7</option>
                        <option value="+6">+6</option>
                    </select>
                    <input type="tel" v-model="loginpojo.username" placeholder="请输入手机号" />
                    <input type="password" v-model="loginpojo.password" placeholder="请输入密码" />
                    <input type="password" v-model="loginpojo.confirmPassword" placeholder="确认密码" />
                    <button class="clear-btn" @click="clearPhoneNumber">×</button>
                </div>
                <p class="terms">
                    <input type="checkbox" v-model="isAgreed" /> 已阅读并同意 <a href="#">用户协议</a> 和 <a href="#">隐私政策</a> 以及
                    <a href="#">运营商服务协议</a>
                    ，运营商将对你的手机号进行验证
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
import { userLoginService, userInfoService, userRegisterService } from "../../api/user"
import { useTokenStore } from "../../stores/token"

export default {
    name: 'Login',
    data() {
        return {
            loginpojo: {
                username: '',
                password: "",
                confirmPassword: ""  // for registration
            },
            isPasswordLogin: false,
            isAgreed: false, // 协议同意状态
            isRegistering: false, // 是否处于注册页面
        };
    },
    methods: {
        closeScreen() {
            this.$router.go(-1); // 返回上一级
            console.log('关闭界面');
        },
        clearPhoneNumber() {
            this.loginpojo.username = ''; // 清空手机号
            this.loginpojo.password = ''; // 清空密码
            this.loginpojo.confirmPassword = ''; // 清空确认密码
        },
        async verifyPhoneNumber() {
            if (!this.isAgreed) { // 检查用户是否勾选协议
                alert("请勾选用户协议以继续登录。");
                return;
            }

            console.log('验证手机号:', this.loginpojo);
            try {
                const response = await userLoginService(this.loginpojo);
                if (response.data.code === 1) {
                    console.error("登录失败：", error);
                } else {
                    const token = response.data.data;
                    const usertoken = useTokenStore();
                    usertoken.setToken(token);
                    localStorage.setItem('token', token);
                    this.$router.push("/");
                }
            } catch (error) {
                console.error("登录失败：", error);
            }
        },
        switchToPasswordLogin() {
            this.isPasswordLogin = true;
        },
        switchToRegister() {
            this.isRegistering = true;
        },
        switchToLogin() {
            this.isRegistering = false;
        },
        async register() {
            if (!this.isAgreed) { // 检查用户是否勾选协议
                alert("请勾选用户协议以继续注册。");
                return;
            }

            if (this.loginpojo.password !== this.loginpojo.confirmPassword) {
                alert("密码和确认密码不匹配");
                return;
            }

            console.log('注册手机号:', this.loginpojo);
            try {
                const response = await userRegisterService(this.loginpojo);
                if (response.data.code === 1) {
                    alert("注册失败，请稍后再试");
                } else {
                    this.verifyPhoneNumber(); // 注册成功后自动登录
                }
            } catch (error) {
                console.error("注册失败：", error);
            }
        },
        async getinfo() {
            const response = await userInfoService();
            const info = response.data;
        }
    },
};
</script>

<style scoped>
.login-screen {
    display: flex;
    flex-direction: column;
    padding: 0 4vw;
    /* 使用 vw 进行水平内边距 */
    box-sizing: border-box;
    background-color: #fff;
    height: 100vh;
    /* 使用 vh 进行高度设置 */
    width: 100vw;
    /* 使用 vw 进行宽度设置 */
    overflow-x: hidden;
    /* 去掉水平滚动条 */
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 2vh 0;
    /* 使用 vh 进行上下内边距 */
}

.close-btn {
    font-size: 3vw;
    /* 使用 vw 进行字体大小 */
    background: none;
    border: none;
    cursor: pointer;
}

.help-settings {
    font-size: 2vw;
    /* 使用 vw 进行字体大小 */
    cursor: pointer;
}

.content {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    text-align: center;
    overflow-y: auto;
    /* 允许纵向滚动 */
}

.notice {
    font-size: 4vw;
    /* 使用 vw 进行字体大小 */
    margin-bottom: 2vh;
    /* 使用 vh 进行下边距 */
}

.login-form {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    /* 确保表单宽度为100% */
}

.input-group {
    display: flex;
    align-items: center;
    margin-bottom: 2vh;
    /* 使用 vh 进行下边距 */
    width: 100%;
    /* 确保输入组宽度为100% */
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 2vh;
    /* 使用 vh 进行内边距 */
    box-sizing: border-box;
    /* 确保内边距和边框不影响总宽度 */
}

.country-code {
    border: none;
    background: none;
    font-size: 2.5vw;
    /* 使用 vw 进行字体大小 */
    margin-right: 1vw;
    /* 使用 vw 进行右边距 */
}

.input-group input {
    border: none;
    outline: none;
    flex-grow: 1;
    font-size: 2.5vw;
    /* 使用 vw 进行字体大小 */
}

.clear-btn {
    font-size: 3vw;
    /* 使用 vw 进行字体大小 */
    background: none;
    border: none;
    cursor: pointer;
}

.terms {
    font-size: 2vw;
    /* 使用 vw 进行字体大小 */
    color: #888;
    text-align: left;
    margin: 2vh 0;
    /* 使用 vh 进行上下边距 */
}

.terms a {
    color: #007bff;
    text-decoration: none;
}

.terms input {
    margin-right: 5px;
}

.login-btn {
    background-color: #ff5252;
    color: white;
    border: none;
    padding: 2vh 0;
    /* 使用 vh 进行上下内边距 */
    border-radius: 5px;
    cursor: pointer;
    width: 100%;
    margin-top: 2vh;
    /* 使用 vh 进行上边距 */
    font-size: 3vw;
    /* 使用 vw 进行字体大小 */
}

.login-options {
    display: flex;
    justify-content: space-between;
    width: 100%;
    margin-top: 2vh;
    /* 使用 vh 进行上边距 */
}

.login-options button {
    background: none;
    border: none;
    color: #007bff;
    cursor: pointer;
    font-size: 2vw;
    /* 使用 vw 进行字体大小 */
}

.recovery-text {
    font-size: 2vw;
    /* 使用 vw 进行字体大小 */
    color: #888;
    margin-top: 2vh;
    /* 使用 vh 进行上边距 */
}

.recovery-text a {
    color: #007bff;
    text-decoration: none;
}
</style>
