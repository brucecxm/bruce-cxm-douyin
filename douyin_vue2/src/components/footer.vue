<template>
  <div class="footer">
    <div
      class="nav home"
      :class="{ active: selected === 'home' }"
      @click="(select('home'), gohome())"
    >
      首页
    </div>
    <div
      class="nav shop"
      :class="{ active: selected === 'shop' }"
      @click="(select('shop'), goshop())"
    >
      商城
    </div>
    <div
      class="nav add"
      :class="{ active: selected === 'add' }"
      @click="(select('add'), goshangchuan())"
    ></div>
    <div
      class="nav message"
      :class="{ active: selected === 'message' }"
      @click="(select('message'), goxiaoxi())"
    >
      消息
    </div>
    <div
      class="nav me"
      :class="{ active: selected === 'me' }"
      @click="(select('me'), goLogin())"
    >
      我
    </div>
  </div>
</template>
<script>
import store from '@/stores';

export default {
  data() {
    return {
      selected: '' // 当前选中的选项
    };
  },
  methods: {
    select(option) {
      this.selected = option;
      this.$emit('selection-changed', option); // 向父组件传递数据
    },
    checkLoginAndGo(path) {
      const token = store.state.token.token || localStorage.getItem('token');
      if (!token) {
        this.$router.push('/login');
      } else {
        this.$router.push(path);
      }
    },
    goLogin() {
      this.checkLoginAndGo('/me');
    },
    gome() {
      this.checkLoginAndGo('/me');
    },
    goshop() {
      this.checkLoginAndGo('/shop');
    },
    goxiaoxi() {
      this.checkLoginAndGo('/xiaoxi');
    },
    goshangchuan() {
      this.checkLoginAndGo('/shangchuan');
    },
    gohome() {
      this.$router.push('/');
    }
  }
};
</script>

<style scoped>
.add {
  width: 1rem;
  height: 0.8rem;
  background-image: url(../assets/home/加号.png);
  background-size: 1rem, 0.8rem;
}

.footer {
  width: 100%;
  height: 5vh;
  background-color: black;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.nav {
  font-size: 0.5rem;
  color: rgba(255, 255, 255, 0.9);
  margin: 0.6rem 0.7rem;
  /* 将左右的 margin 调整为更小的值 */
  cursor: pointer;
  /* 添加鼠标光标效果 */
  transition: color 0.3s ease;
  /* 平滑过渡效果 */
}

.nav.active {
  color: #f5f5f5;
  /* 选中时的颜色 */
}
</style>
