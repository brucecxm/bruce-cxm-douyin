<template>
  <iframe :src="detairurl" width="600" height="400" frameborder="0"></iframe>
</template>

<script>
export default {
  data() {
    return {
      detairurl: 'https://www.baidu.com'
    };
  },

  methods: {}
};
</script>
