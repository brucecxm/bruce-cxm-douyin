<template>
  <div class="navbar-wrapper">
    <div class="navbar-scroll" ref="scrollContainer">
      <div class="nav-item" v-for="(item, index) in navItems" :key="index">
        <div class="icon">
          <img :src="item.icon" alt="icon" />
        </div>
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
  name: 'ScrollableNavbar',
  props: {
    navItems: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      thumbWidth: 0,
      thumbLeft: 0
    };
  },
  mounted() {
    this.updateThumb();
    this.$refs.scrollContainer.addEventListener('scroll', this.updateThumb);
    window.addEventListener('resize', this.updateThumb);
  },
  beforeDestroy() {
    this.$refs.scrollContainer.removeEventListener('scroll', this.updateThumb);
    window.removeEventListener('resize', this.updateThumb);
  },
  methods: {
    updateThumb() {
      const container = this.$refs.scrollContainer;
      const visibleWidth = container.clientWidth;
      const totalWidth = container.scrollWidth;
      const scrollLeft = container.scrollLeft;

      const trackVisibleRatio = 0.5;

      this.thumbWidth = (visibleWidth / totalWidth) * 100 * trackVisibleRatio;
      this.thumbLeft =
        (scrollLeft / (totalWidth - visibleWidth)) * (100 * trackVisibleRatio);
    }
  }
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
  scrollbar-width: none;
  -ms-overflow-style: none;
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
  width: 24px;
  height: 24px;
  margin: 0 auto;
}
.icon img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.label {
  font-size: 12px;
  color: #555;
  margin-top: 4px;
}

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
