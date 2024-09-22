<template>
    <div class="login">
        <div class="menu">
            <a @click="gohome" class="close">✖</a>
            <a class="help">帮助与设置</a>
        </div>
        <div class="loginform">
            <form @submit.prevent="submitForm">
                <h1>登录发现更多精彩</h1>
                <input type="checkbox" id="agree" v-model="agreed">
                <label for="agree">我已经阅读并同意用户协议和隐私政策已经运营商服务协议</label>
                <br>
                <br>
                <input type="text" id="phone" v-model="loginData.userid" placeholder="请输入手机号">
                <br>
                <br>
                <p style="font-size: 15px;color: rgb(69, 69, 69);">未注册的手机号将自动注册</p>
                <button type="submit" class="loginbutton">验证并登录</button>

            </form>
        </div>
    </div>
</template>
<style scoped>
#phone {
    width: 380px;
    position: absolute;
    height: 50px;
    border: none;
    background-color: rgba(111, 111, 111, 0.1);
    border-radius: 10px;
}

.loginbutton {
    position: absolute;
    width: 380px;
    height: 50px;
    border: none;
    border-radius: 10px;
    background-color: rgb(243, 56, 56);
    color: white;
    font-size: 18px;
}

.loginform {
    margin: 20px;
    position: relative;
    top: 40px;
}

.menu {
    top: 0px;
    height: 30px;
}

.close {
    position: absolute;
    display: block;
    left: 10px;
    line-height: 30px;
}

.help {
    position: absolute;
    display: block;
    right: 10px;
    line-height: 30px;

}
</style>
<script>
import { useTokenStore } from "../../stores/token"
import { userLoginService } from "../../api/user"
import router from '@/router'
export default {
    data() {
        return {
            loginData: {
                userid: "",
                password: ""
            },
            agreed: false,

        }
    },
    methods: {
        gohome() {
            router.push("/")
        },

        async submitForm() {
            // 执行表单验证和其他逻辑
            if (!this.agreed) {
                alert('请先同意用户协议');
                return;
            }

            if (this.loginData.userid.trim() === '' || this.loginData.password.trim() === '') {
                alert('手机号和密码不能为空');
                return;
            }

            // 在这里可以处理提交逻辑，例如使用 fetch 或 axios 发送登录请求
            console.log('手机号:', this.loginData.userid);
            console.log('密码:', this.loginData.password);
            let result = await userLoginService(this.loginData);
            const tokenStore = useTokenStore()
            tokenStore.setToken(result.data)
            router.push("/me")


        }
    }
}
</script>../../shop.js/user