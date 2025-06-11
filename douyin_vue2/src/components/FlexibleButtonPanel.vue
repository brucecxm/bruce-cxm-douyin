<template>
  <div
    class="btn-panel"
    :style="{
      display: 'grid',
      gridTemplateColumns: computedGridColumns,
      gap: `${gap}px`
    }"
  >
    <template v-for="(item, rawIndex) in items">
      <div
        v-if="item.show !== false"
        :key="rawIndex"
        class="btn-item"
        :class="{ disabled: item.disabled }"
        :style="getBtnStyle(item, rawIndex)"
        @click="handleClick(rawIndex, item)"
        @mousedown="handlePressStart(rawIndex)"
        @touchstart="handlePressStart(rawIndex)"
        @mouseup="handlePressEnd(rawIndex, false)"
        @mouseleave="handlePressEnd(rawIndex, false)"
        @touchend="handlePressEnd(rawIndex, false)"
        @touchcancel="handlePressEnd(rawIndex, false)"
      >
        <!-- 点击波纹动画 -->
        <span
          class="ripple"
          v-if="ripples[rawIndex]"
          :style="ripples[rawIndex]"
        ></span>

        <!-- 插槽优先渲染 -->
        <slot :name="'btn-' + rawIndex" v-bind="{ item, index: rawIndex }">
          <img
            v-if="item.icon"
            :src="item.icon"
            class="btn-icon"
            :style="{ width: `${iconSize}px`, height: `${iconSize}px` }"
          />
          <div
            class="btn-text"
            :style="{
              fontSize: `${item.fontSize || fontSize}px`,
              fontWeight: item.bold ? 'bold' : 'normal'
            }"
          >
            {{ item.text }}
          </div>
        </slot>
      </div>
    </template>
  </div>
</template>

<script>
export default {
  name: 'FlexibleButtonPanel',
  props: {
    items: { type: Array, required: true },
    cols: { type: Number, default: 0 },
    gap: { type: Number, default: 12 },
    fontSize: { type: Number, default: 14 },
    iconSize: { type: Number, default: 20 },
    longPressDelay: { type: Number, default: 600 }
  },
  data() {
    return {
      activeIndex: null,
      ripples: [],
      longPressTimer: null
    };
  },
  computed: {
    computedCols() {
      if (this.cols > 0) return this.cols;
      return this.items.filter((item) => item.show !== false && !item.width)
        .length;
    },
    computedGridColumns() {
      const visibleItems = this.items.filter((item) => item.show !== false);
      return visibleItems.map((item) => item.width || '1fr').join(' ');
    }
  },
  methods: {
    handleClick(index, item) {
      if (item.disabled) return;
      this.$emit('btnClick', { index, item });
    },
    handlePressStart(index) {
      if (this.items[index].disabled) return;
      this.activeIndex = index;
      this.longPressTimer = setTimeout(() => {
        this.$emit('btnLongPress', { index, item: this.items[index] });
        this.longPressTimer = null;
      }, this.longPressDelay);
      this.createRipple(index);
    },
    handlePressEnd(index) {
      if (this.longPressTimer) {
        clearTimeout(this.longPressTimer);
        this.longPressTimer = null;
      }
      this.activeIndex = null;
    },
    createRipple(index) {
      const size = 100;
      this.$set(this.ripples, index, {
        width: `${size}px`,
        height: `${size}px`,
        left: '50%',
        top: '50%',
        opacity: '0.3'
      });
      setTimeout(() => this.$set(this.ripples, index, null), 300);
    },
    getBtnStyle(item, index) {
      const isActive = this.activeIndex === index;
      const bgc = isActive
        ? this.darkenColor(item.bgColor || '#333', 0.1)
        : item.bgColor || '#333';
      return {
        backgroundColor: bgc,
        borderRadius: `${item.borderRadius || 12}px`,
        color: item.textColor || '#fff',
        opacity: item.disabled ? 0.5 : 1,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        flexDirection: 'column',
        cursor: item.disabled ? 'not-allowed' : 'pointer',
        position: 'relative',
        overflow: 'hidden',
        transition: 'background-color 0.2s',
        padding: `${item.padding || '6px 10px'}`
      };
    },
    darkenColor(hex, f = 0.1) {
      let r = parseInt(hex.slice(1, 3), 16);
      let g = parseInt(hex.slice(3, 5), 16);
      let b = parseInt(hex.slice(5, 7), 16);
      r = Math.max(0, Math.floor(r * (1 - f)));
      g = Math.max(0, Math.floor(g * (1 - f)));
      b = Math.max(0, Math.floor(b * (1 - f)));
      return `rgb(${r},${g},${b})`;
    }
  }
};
</script>

<style scoped>
.btn-panel {
  width: 100%;
}
.btn-item {
  user-select: none;
}
.btn-icon {
  margin-bottom: 4px;
}
.disabled {
  pointer-events: none;
}
.ripple {
  position: absolute;
  border-radius: 50%;
  background-color: #fff;
  transform: translate(-50%, -50%) scale(0);
  animation: ripple-effect 0.3s forwards;
}
@keyframes ripple-effect {
  to {
    transform: translate(-50%, -50%) scale(2);
    opacity: 0;
  }
}
</style>
