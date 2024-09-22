<template>
    <div class="home">

        <div class="footer">
            <ul>
                <li @click="gohome">首页</li>
                <li @click="goshop">商城</li>
                <li class="jiahao" @click="goshangchuan"></li>
                <li @click="goxiaoxi">消息</li>
                <li @click="gome">我</li>
            </ul>
        </div>

    </div>
</template>

<script>
import { useTokenStore } from "../stores/token";

export default {
    name: 'BaseFooter',
    data() {
        return {
        }
    },

    methods: {
        gome() {
            //先判断一下有没有登录  如果没有登录 则跳转到登录界面  如果有登录  则跳转到对应界面
            //先判断一下有没有登录  如果没有登录 则跳转到登录界面  如果有登录  则跳转到对应界面
            const tokenStore = useTokenStore();
            const token = tokenStore.token
            if (token === null || token === undefined || token === "") {
                this.$router.push('/login');

            } else {
                this.$router.push('/me');

            }

            // Navigate to '/free-video' route
        },
        gohome() {
            // Navigate to '/free-video' route
            this.$router.push('/');
        },
        goshop() {
            //先判断一下有没有登录  如果没有登录 则跳转到登录界面  如果有登录  则跳转到对应界面
            //先判断一下有没有登录  如果没有登录 则跳转到登录界面  如果有登录  则跳转到对应界面
            const tokenStore = useTokenStore();
            const token = tokenStore.token
            if (token === null || token === undefined || token === "") {
                this.$router.push('/login');

            } else {
                this.$router.push('/shop');

            }
            // Navigate to '/free-video' route

        },
        goxiaoxi() {
            //先判断一下有没有登录  如果没有登录 则跳转到登录界面  如果有登录  则跳转到对应界面
            //先判断一下有没有登录  如果没有登录 则跳转到登录界面  如果有登录  则跳转到对应界面
            const tokenStore = useTokenStore();
            const token = tokenStore.token
            if (token === null || token === undefined || token === "") {
                this.$router.push('/login');

            } else {
                this.$router.push('/xiaoxi');

            }

        },
        goshangchuan() {
            //先判断一下有没有登录  如果没有登录 则跳转到登录界面  如果有登录  则跳转到对应界面
            //先判断一下有没有登录  如果没有登录 则跳转到登录界面  如果有登录  则跳转到对应界面
            const tokenStore = useTokenStore();
            const token = tokenStore.token
            if (token === null || token === undefined || token === "") {
                this.$router.push('/login');

            } else {
                this.$router.push('/shangchuan');

            }

        }
    }

}
</script>


<style scoped>
.jiahao {
    background-image: url(../assets/home/加号.png);
    width: 31px;
    height: 25px;
    background-size: cover;
    transform: translateY(20px)
}

video {
    height: 830px;
    width: 414px;
}

.aside ul {
    margin: 0px;
    position: absolute;
    left: 0px;
    top: 0px;
}

.aside li {
    margin-left: 20px;
    margin-bottom: 55px;
}

.content {
    width: 350px;
    height: 130px;
    /* background-color: rgba(0, 0, 0, 0.5); */
    position: absolute;
    left: 0px;
    bottom: 0px;
}

.aside {
    height: 400px;
    width: 50px;
    /* background-color: rgba(0, 0, 0, 0.5); */
    position: absolute;
    right: 0px;
    bottom: 120px;
}

li {
    float: left;
    line-height: 73px;
    margin-right: 25px;
    margin-left: 25px;
}

.header li {
    float: left;
    line-height: 73px;
    margin-left: 10px;
}

ul {
    left: 0px;
    position: absolute;
    list-style: none;
    /* 去除所有列表的默认标志 */
    padding: 0;
    /* 去除默认的列表项内边距 */
    margin: 0 auto;
}

.footer li {
    line-height: 66px;
}

.header {
    /* background-color: rgba(0, 0, 0, 0.5); */
    width: 100%;
    height: 70px;
    position: fixed;
    top: 0px;
    color: white;
    z-index: 2;

}

.footer {
    background-color: rgba(0, 0, 0);
    width: 414px;
    height: 66px;
    position: fixed;
    bottom: 0px;
    color: white;
}


.main {
    background-color: rgba(0, 0, 0, 1);
    width: 100%;
    height: 830px;
    position: relative;
    color: white;
}

.main img {
    width: 414px;
    height: 830px;
}

.home {
    background-color: black;
    justify-content: space-between;
    height: 66px;
    align-items: center;
    position: absolute;
}


* {
    margin: 0px;
}
</style>


