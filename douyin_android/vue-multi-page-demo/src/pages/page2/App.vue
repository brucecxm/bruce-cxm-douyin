<template>
  <div class="page2">
    <h1>页面2</h1>
    <p>这是第二个子页面</p>
    <div class="button-group">
      <button @click="navigateToPage('/index')">返回首页</button>
      <button @click="navigateToPage('/page1')">跳转到页面1</button>
      <button @click="callAndroidMethod">调用 Android 方法</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PageTwo',
  methods: {
    navigateToPage(pagePath) {
      // 如果是在 Android WebView 中，通过 Android 方法跳转
      if (window.Android && window.Android.navigateToPage) {
        window.Android.navigateToPage(pagePath)
      } else {
        // 否则直接跳转到对应 HTML 文件
        window.location.href = pagePath + '.html'
      }
    },
    callAndroidMethod() {
      if (window.Android && window.Android.showToast) {
        window.Android.showToast('Hello from Page2!')
      } else {
        alert('未检测到 Android 环境，无法调用原生方法')
      }
    }
  }
}
</script>

<style scoped>
.page2 {
  text-align: center;
  padding: 2rem;
}

h1 {
  color: #e74c3c;
}

.button-group {
  margin-top: 2rem;
}

button {
  margin: 0 1rem;
  padding: 0.5rem 1rem;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

button:hover {
  background-color: #c0392b;
}
</style>
