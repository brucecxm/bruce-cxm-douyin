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
   
  },
  methods: {

  }
};
</script>
