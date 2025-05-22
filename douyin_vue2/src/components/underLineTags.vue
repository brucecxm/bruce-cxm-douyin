<template>
  <div class="videoLikeInfo">
    <div class="nav-bar">
      <div
        v-for="(item, index) in navItems"
        :key="index"
        :class="['nav-item', { active: activeIndex === index }]"
        @click="selectTab(index)"
        ref="navItems"
      >
        {{ item }}
      </div>
      <div
        class="underline"
        :style="{
          width: underlineStyle.width + 'px',
          transform: `translateX(${underlineStyle.left}px)`
        }"
      ></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UnderLineTags',
  props: {
    navItems: {
      type: Array,
      required: true
    },
    activeIndex: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      underlineStyle: {
        width: 0,
        left: 0
      }
    };
  },
  watch: {
    activeIndex() {
      this.updateUnderline();
    }
  },
  mounted() {
    this.updateUnderline();
    window.addEventListener('resize', this.updateUnderline);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateUnderline);
  },
  methods: {
    selectTab(index) {
      if (index === this.activeIndex) return;
      this.$emit('update:activeIndex', index); // 支持 .sync
      this.$emit('change', index); // 通知父组件
    },
    updateUnderline() {
      this.$nextTick(() => {
        const navBarWidth = this.$el.querySelector('.nav-bar').clientWidth;
        const itemWidth = navBarWidth / this.navItems.length;
        this.underlineStyle.width = itemWidth;
        this.underlineStyle.left = itemWidth * this.activeIndex;
      });
    }
  }
};
</script>

<style scoped>
.nav-bar {
  width: 100%;
  display: flex;
  position: relative;
  border-bottom: 1px solid #ddd;
  box-sizing: border-box;
}

.nav-item {
  flex: 1;
  text-align: center;
  padding: 10px 0;
  cursor: pointer;
  font-size: 16px;
  color: #666;
  transition: color 0.3s;
  white-space: nowrap;
}

.nav-item.active {
  color: #000;
  font-weight: bold;
}

.underline {
  position: absolute;
  bottom: 0;
  height: 2px;
  background-color: black;
  transition:
    transform 0.3s ease,
    width 0.3s ease;
}
</style>
