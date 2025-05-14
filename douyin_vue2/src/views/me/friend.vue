<template>
  <div class="friend">
    <div class="header">
    <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="互关" name="first"></el-tab-pane>
    <el-tab-pane label="关注" name="second"></el-tab-pane>
    <el-tab-pane label="粉丝" name="third"></el-tab-pane>
    <el-tab-pane label="朋友" name="fourth"></el-tab-pane>
  </el-tabs>
    </div>
   <user-info></user-info>
  </div>
  </template>
  <script>
import UserInfo from '../../components/userInfo.vue';
  export default {
    data() {
      return {
        activeName: 'second'
      };
    },
    components: {
        UserInfo
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      }
    }
  };
  </script>
  <style scoped>
.friend{
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction:column;
  align-items:center
}
.header{
  display: flex;
  flex-direction:row;
  justify-content:space-between;

}
  </style>
  