<template>
  <div class="page1">
    <h1>页面1</h1>
    <p>这是第一个子页面</p>
    <div class="button-group">
      <button @click="navigateToPage('/index')">返回首页</button>
      <button @click="navigateToPage('/page2')">跳转到页面2</button>
      <button @click="callAndroidMethod">调用 Android 方法</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PageOne',
  methods: {
    navigateToPage(pagePath) {
      // 如果是在 Android WebView 中，通过 Android 方法跳转
      if (window.Android && window.Android.navigateToPage) {
        window.Android.navigateToPage(pagePath);
      } else {
        // 否则直接跳转到对应 HTML 文件
        window.location.href = pagePath + '.html';
      }
    },
    callAndroidMethod() {
      if (window.Android && window.Android.showToast) {
        window.Android.showToast('Hello from Page1!');
      } else {
        alert('未检测到 Android 环境，无法调用原生方法');
      }
    }
  }
};
</script>

<style scoped>
.page1 {
  text-align: center;
  padding: 2rem;
}

h1 {
  color: #3498db;
}

.button-group {
  margin-top: 2rem;
}

button {
  margin: 0 1rem;
  padding: 0.5rem 1rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

button:hover {
  background-color: #2980b9;
}
</style>
