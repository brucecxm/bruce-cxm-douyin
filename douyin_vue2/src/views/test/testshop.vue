<template>
  <div>
    
    <InfiniteList  >
      <template v-slot:header>
    这是自定义头部
  </template>
    </InfiniteList>
  </div>
</template>

<script>
import InfiniteList from './test.vue';

export default {
  components: {
    InfiniteList
  }
};
</script>
