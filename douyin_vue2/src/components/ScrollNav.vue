<template>
  <div class="navbar-wrapper">
    <div class="navbar-scroll" ref="scrollContainer">
      <div
        class="nav-item"
        v-for="(item, index) in navItems"
        :key="index"
        :style="navItemStyle"
      >
        <div class="icon" :style="iconStyle">
          <img :src="item.icon" alt="icon" :style="imgStyle" />
        </div>
        <div class="label" :style="labelStyle">{{ item.label }}</div>
      </div>
    </div>

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
    },
    iconWidth: {
      type: [String, Number],
      default: '24px'
    },
    iconHeight: {
      type: [String, Number],
      default: '24px'
    },
    labelFontSize: {
      type: [String, Number],
      default: '12px'
    },
    navItemWidth: {
      type: [String, Number],
      default: '60px'
    },
    navItemPadding: {
      type: String,
      default: '8px 0'
    }
  },
  data() {
    return {
      thumbWidth: 0,
      thumbLeft: 0
    };
  },
  computed: {
    iconStyle() {
      return {
        width: this.addUnit(this.iconWidth),
        height: this.addUnit(this.iconHeight),
        margin: '0 auto'
      };
    },
    imgStyle() {
      return {
        width: '100%',
        height: '100%',
        objectFit: 'contain'
      };
    },
    labelStyle() {
      return {
        fontSize: this.addUnit(this.labelFontSize),
        color: '#555',
        marginTop: '4px'
      };
    },
    navItemStyle() {
      return {
        flex: '0 0 auto',
        width: this.addUnit(this.navItemWidth),
        textAlign: 'center',
        padding: this.navItemPadding
      };
    }
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
    },
    addUnit(val) {
      return typeof val === 'number' ? `${val}px` : val;
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
