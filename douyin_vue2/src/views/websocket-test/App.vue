<template>
  <div id="app">
    <WebSocketTest />
  </div>
</template>

<script>
import WebSocketTest from './websocket-test.vue';

export default {
  name: 'App',
  components: {
    WebSocketTest
  }
};
</script>

<style>
/* 全局样式可以在这里添加 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
}

#app {
  width: 100%;
  height: 100vh;
}
</style>
