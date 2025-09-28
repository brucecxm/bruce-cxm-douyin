<template>
  <div class="app-container">
    <h1>主题切换示例</h1>
    <p>
      当前主题：{{ currentTheme === 'theme-light' ? '白天模式' : '夜间模式' }}
    </p>
    <button class="theme-btn" @click="toggleTheme">切换主题</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentTheme: 'theme-light' // 默认白天主题
    };
  },
  mounted() {
    // 初始化时从localStorage读取主题（如果有）
    const savedTheme = localStorage.getItem('appTheme');
    if (savedTheme) {
      this.currentTheme = savedTheme;
      document.documentElement.className = savedTheme; // 应用主题类名到html根元素
    } else {
      // 没有保存的主题，默认应用白天模式
      document.documentElement.className = this.currentTheme;
    }
  },
  methods: {
    toggleTheme() {
      // 切换主题（白天↔夜间）
      this.currentTheme =
        this.currentTheme === 'theme-light' ? 'theme-dark' : 'theme-light';
      // 应用主题到html根元素（影响全局CSS变量）
      document.documentElement.className = this.currentTheme;
      // 保存主题到localStorage，刷新后保持状态
      localStorage.setItem('appTheme', this.currentTheme);
    }
  }
};
</script>
