<template>
  <div :style="userInfoStyle"></div>
</template>

<script>
export default {
 data() {
   return {
     userInfoStyle: {
       height: '100vh',
       backgroundColor: 'red',
       width: '100vw',
       display: 'flex',
       flexDirection: 'column',
       alignItems: 'center'
     }
   };
 }
};
</script>

<style scoped>
</style>
