<template>
    <div class="login-screen" style="width: 414px; height: 896px;">
        <div class="header">
            <button class="close-btn" @click="closeScreen">×</button>
            <div class="help-settings">
                <span>帮助与设置</span>
            </div>
        </div>
        <div class="content">
            <p class="notice">登录后即可点赞喜欢的内容</p>
            <div class="login-form">
                <div class="input-group">
                    <select class="country-code">
                        <option value="+86">+86</option>
                    </select>
                    <input type="tel" v-model="loginpojo.username" placeholder="请输入手机号" />
                    <br>
                    <input type="tel" v-model="loginpojo.password" placeholder="请输入密码" />
                    <button class="clear-btn" @click="clearPhoneNumber">×</button>
                </div>
                <p class="terms">
                    <input type="checkbox" /> 已阅读并同意 <a href="#">用户协议</a> 和 <a href="#">隐私政策</a> 以及 <a href="#">运营商服务协议</a>
                    ，运营商将对你的手机号进行验证
                </p>
                <button class="login-btn" @click="verifyPhoneNumber">验证并登录</button>
                <div class="login-options">
                    <button @click="switchToPasswordLogin">密码登录</button>
                    <button>其他方式登录</button>
                </div>
            </div>
            <p class="recovery-text">手机号不用了？ <a href="#">找回账号</a></p>
        </div>
    </div>
</template>
  
<script>
import { userLoginService } from "../../api/user"
import { useTokenStore } from "../../stores/token"
export default {
    name: 'Login',
    data() {
        return {
            loginpojo: {
                username: '',
                password: ""

            },
            isPasswordLogin: false,
        };
    },
    methods: {
        closeScreen() {
            //跳转到上一级
            this.$router.go(-1); // 返回上一级
            console.log('关闭界面');
        },
        clearPhoneNumber() {
            this.phoneNumber = '';
        },
        async verifyPhoneNumber() {
            console.log('验证手机号:', this.loginpojo);
            try {
                const response = await userLoginService(this.loginpojo);

                // 如果cookie中没有token  那么从respond中获取 反之
                // 如果cookie中没有token  那么从respond中获取 反之
                // 如果cookie中没有token  那么从respond中获取 反之
                // 如果cookie中没有token  那么从respond中获取 反之
                // 如果cookie中没有token  那么从respond中获取 反之

                const token = response.data;



                //如果前端使用的是sa-token 那么token就会直接存储在cookie中 而不会出现在responde中 所以要判断一下token到底在哪
                //如果前端使用的是sa-token 那么token就会直接存储在cookie中 而不会出现在responde中 所以要判断一下token到底在哪
                //如果前端使用的是sa-token 那么token就会直接存储在cookie中 而不会出现在responde中 所以要判断一下token到底在哪
                const usertoken = useTokenStore();
                usertoken.setToken(token);
                // 将token存储在localStorage中
                localStorage.setItem('token', token);
                this.$router.push("/");
            } catch (error) {
                console.error("登录失败：", error);
            }
        },
        switchToPasswordLogin() {
            this.isPasswordLogin = true;
        },
    },
};
</script>
  
<style scoped>
.login-screen {
    display: flex;
    flex-direction: column;
    padding: 0 16px;
    box-sizing: border-box;
    background-color: #fff;
    height: 100%;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;
}

.close-btn {
    font-size: 24px;
    background: none;
    border: none;
    cursor: pointer;
}

.help-settings {
    font-size: 14px;
    cursor: pointer;
}

.content {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    text-align: center;
}

.notice {
    font-size: 18px;
    margin-bottom: 20px;
}

.login-form {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.input-group {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    width: 100%;
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px;
}

.country-code {
    border: none;
    background: none;
    font-size: 16px;
    margin-right: 10px;
}

.input-group input {
    border: none;
    outline: none;
    flex-grow: 1;
    font-size: 16px;
}

.clear-btn {
    font-size: 20px;
    background: none;
    border: none;
    cursor: pointer;
}

.terms {
    font-size: 12px;
    color: #888;
    text-align: left;
    margin: 10px 0;
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
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    width: 100%;
    margin-top: 20px;
}

.login-options {
    display: flex;
    justify-content: space-between;
    width: 100%;
    margin-top: 20px;
}

.login-options button {
    background: none;
    border: none;
    color: #007bff;
    cursor: pointer;
    font-size: 14px;
}

.recovery-text {
    font-size: 12px;
    color: #888;
    margin-top: 20px;
}

.recovery-text a {
    color: #007bff;
    text-decoration: none;
}
</style>
  ../../shop.js/user