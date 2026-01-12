<template>
  <transition :name="transitionName">
    <div class="popup-mask" @click.self="handleClose">
      <div class="popup-content" :style="popupStyle" @click.stop>
        <slot />
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  props: {
    direction: {
      type: String,
      default: 'bottom'
    },
    width: {
      type: String,
      default: '300px'
    },
    height: {
      type: String,
      default: '300px'
    },
    isEdge: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    popupStyle() {
      const style = {
        position: 'absolute',
        background: '#fff'
      };
      const { direction, isEdge, width, height } = this;

      if (direction === 'top' || direction === 'bottom') {
        style.height = height;
        style.width = width;
        style.left = isEdge ? '0' : '50%';
        style.transform = isEdge ? '' : 'translateX(-50%)';
        style[direction] = '0';
      } else {
        style.width = width;
        style.height = height;
        style.top = isEdge ? '0' : '50%';
        style.transform = isEdge ? '' : 'translateY(-50%)';
        style[direction] = '0';
      }

      return style;
    },
    transitionName() {
      return `slide-${this.direction}`;
    }
  },
  methods: {
    handleClose() {
      this.$emit('close');
    }
  }
};
</script>

<style scoped>
.popup-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: 999;
}

.popup-content {
  transition: all 0.3s ease;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  overflow: auto;
}

/* 动画定义 */
.slide-top-enter-active,
.slide-top-leave-active,
.slide-bottom-enter-active,
.slide-bottom-leave-active,
.slide-left-enter-active,
.slide-left-leave-active,
.slide-right-enter-active,
.slide-right-leave-active {
  transition: all 0.3s ease;
}

.slide-top-enter,
.slide-top-leave-to {
  transform: translateY(-100%);
}

.slide-bottom-enter,
.slide-bottom-leave-to {
  transform: translateY(100%);
}

.slide-left-enter,
.slide-left-leave-to {
  transform: translateX(-100%);
}

.slide-right-enter,
.slide-right-leave-to {
  transform: translateX(100%);
}
</style>
