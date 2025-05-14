<template>
  <div :style="userInfoStyle">
<div class="avatar" :style="avatarStyle"></div>

  </div>
</template>

<script>
export default {
 data() {
   return {
     userInfoStyle: {
       height: '10vh',
       backgroundColor: 'red',
       width: '100vw',
       display: 'flex',
       flexDirection: 'column',
       alignItems: 'center'
     },
     avatarStyle:{
        width: '10vw',
        height: '10vw',
        backgroundColor: 'blue',
        borderRadius: '50%',
       backgroundColor: 'blue',

        marginTop: '5vh'
     }
   };
 }
};
</script>

<style scoped>
</style>
