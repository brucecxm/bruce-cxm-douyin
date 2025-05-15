<template>
  <div class="navbar-wrapper">
    <div class="navbar-scroll" ref="scrollContainer">
      <div class="nav-item" v-for="(item, index) in navItems" :key="index">
        <div class="icon">{{ item.icon }}</div>
        <div class="label">{{ item.label }}</div>
      </div>
    </div>

    <!-- 居中、窄宽滚动条轨道 -->
    <div class="scrollbar-track">
      <div
        class="scrollbar-thumb"
        :style="{ width: thumbWidth + '%', left: thumbLeft + '%' }"
      ></div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ScrollableNavbar",
  props: {
    navItems: String
  },
  data() {
    return {
     
      thumbWidth: 0,
      thumbLeft: 0,
    };
  },
  mounted() {
    this.updateThumb();
    this.$refs.scrollContainer.addEventListener("scroll", this.updateThumb);
    window.addEventListener("resize", this.updateThumb);
  },
  beforeDestroy() {
    this.$refs.scrollContainer.removeEventListener("scroll", this.updateThumb);
    window.removeEventListener("resize", this.updateThumb);
  },
  methods: {
    updateThumb() {
      const container = this.$refs.scrollContainer;
      const visibleWidth = container.clientWidth;
      const totalWidth = container.scrollWidth;
      const scrollLeft = container.scrollLeft;

      // 滚动区域在居中 50% 中显示，滚动条也映射在这 50% 中
      const trackVisibleRatio = 0.5; // 居中一半

      this.thumbWidth = (visibleWidth / totalWidth) * 100 * trackVisibleRatio;
      this.thumbLeft =
        (scrollLeft / (totalWidth - visibleWidth)) * (100 * trackVisibleRatio);
    },
  },
};
</script>

<style scoped>
.navbar-wrapper {
  width: 100%;
  overflow: hidden;
  position: relative;
}

.navbar-scroll {
  display: flex;
  overflow-x: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE */
}
.navbar-scroll::-webkit-scrollbar {
  display: none;
}

.nav-item {
  flex: 0 0 auto;
  width: 60px;
  text-align: center;
  padding: 8px 0;
}

.icon {
  font-size: 20px;
}
.label {
  font-size: 12px;
  color: #555;
  margin-top: 4px;
}

/* 滚动条轨道，只占容器宽度的中间一部分 */
.scrollbar-track {
  width: 50%;
  height: 4px;
  margin: 4px auto 0;
  background-color: transparent;
  position: relative;
}

.scrollbar-thumb {
  position: absolute;
  height: 2px;
  background-color: #888;
  border-radius: 1px;
  top: 1px;
  transition: left 0.1s ease;
}
</style>
