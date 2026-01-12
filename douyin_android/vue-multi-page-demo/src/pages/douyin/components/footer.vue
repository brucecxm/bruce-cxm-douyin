<template>
  <div class="footer" :class="{ 'light-theme': isLightTheme }">
    <div
      class="nav home"
      :class="{ active: selected === 'home', 'light-text': isLightTheme }"
      @click="handleNavClick('home', gohome)"
    >
      首页
    </div>
    <div
      class="nav shop"
      :class="{ active: selected === 'shop', 'light-text': isLightTheme }"
      @click="handleNavClick('shop', goshop)"
    >
      商城
    </div>
    <div
      class="nav add"
      :class="{ active: selected === 'add', 'light-text': isLightTheme }"
      @click="handleNavClick('add', goshangchuan)"
      :style="addButtonStyle"
    ></div>
    <div
      class="nav message"
      :class="{ active: selected === 'message', 'light-text': isLightTheme }"
      @click="handleNavClick('message', goxiaoxi)"
    >
      消息
    </div>
    <div
      class="nav me"
      :class="{ active: selected === 'me', 'light-text': isLightTheme }"
      @click="handleNavClick('me', goLogin)"
    >
      我
    </div>
  </div>
</template>
<script>
import store from '@/stores';
// 导入图片资源
import plusIcon from '@/assets/home/加号.png';
import plusBlackIcon from '@/assets/home/加号黑.png';

export default {
  data() {
    return {
      selected: '' // 当前选中的选项
    };
  },
  computed: {
    isLightTheme() {
      // 当选中商城、消息或我的时，返回true，启用浅色主题
      return ['shop', 'message', 'me'].includes(this.selected);
    },
    addButtonStyle() {
      // 根据当前主题动态返回背景图片样式
      return {
        backgroundImage: `url(${this.isLightTheme ? plusBlackIcon : plusIcon})`,
        backgroundSize: '1rem, 0.8rem',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center'
      };
    }
  },
  methods: {
    select(option) {
      this.selected = option;
      this.$emit('selection-changed', option); // 向父组件传递数据
    },
    // 统一处理导航点击事件，确保先更新状态再执行页面跳转
    handleNavClick(option, action) {
      this.select(option);
      // 使用nextTick确保DOM更新后再执行页面跳转
      this.$nextTick(() => {
        action();
      });
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
  },
  // 添加mounted钩子，根据当前路由初始化选中状态
  mounted() {
    // 根据当前路由路径设置初始选中项
    const path = this.$route.path;
    if (path === '/') {
      this.selected = 'home';
    } else if (path === '/shop') {
      this.selected = 'shop';
    } else if (path === '/shangchuan') {
      this.selected = 'add';
    } else if (path === '/xiaoxi') {
      this.selected = 'message';
    } else if (path === '/me') {
      this.selected = 'me';
    }
  }
};
</script>

<style scoped>
.add {
  width: 1rem;
  height: 0.8rem;
  /* 背景图片通过:style动态绑定 */
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer {
  width: 100%;
  height: 5vh;
  background-color: black;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  transition: background-color 0.3s ease;
}

.footer.light-theme {
  background-color: white;
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

.nav.light-text {
  color: rgba(0, 0, 0, 0.9);
}

.nav.active {
  color: #f5f5f5;
  /* 选中时的颜色 */
}

.nav.light-text.active {
  color: #000000;
  /* 浅色主题下选中时的颜色 */
}
</style>
