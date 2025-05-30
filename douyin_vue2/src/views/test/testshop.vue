<template>
  <div>
    <danmu
      :width="'100%'"
      :height="'400px'"
      :defaultSpeed="8"
      :maxDanmuCount="15"
      ref="danmuComponent"
    />

    <!-- 输入框 -->
    <input
      v-model="danmuText"
      @keyup.enter="sendDanmu"
      placeholder="请输入弹幕"
    />
  </div>
</template>

<script>
import Danmu from '../../components/Danmu.vue';

export default {
  components: {
    Danmu
  },
  data() {
    return {
      danmuText: ''
    };
  },
  methods: {
    sendDanmu() {
      if (this.danmuText.trim()) {
        // 触发弹幕组件中的sendDanmu方法
        this.$refs.danmuComponent.sendDanmu(this.danmuText);
        this.danmuText = ''; // 清空输入框
      }
    }
  }
};
</script>
