<template>
  <view class="header">
    <view class="menu" :style="menuStyle" @click="triggerParentEvent"></view>
    <scroll-view class="nav" ref="nav" scroll-x="true" show-scrollbar="false" 
                 @touchstart="onTouchStart" @touchmove="onTouchMove" @touchend="onTouchEnd">
      <view v-for="(item, index) in navItems" :key="index" :class="{
        'nav-item': true,
        active: activeIndex === index,
        'black-text': isBlack,
        'white-text': !isBlack
      }" @click="changeIndex(index)" ref="navItem">
        {{ item }}
      </view>
      <view class="underline-container">
        <view class="underline" ref="underline" v-show="underlineVisible"></view>
      </view>
    </scroll-view>
    <view class="search" :style="searchStyle" @click="gosearch"></view>
  </view>
</template>

<script>
export default {
  name: 'headerComponent',
  props: {
    activeIndex: {
      type: Number,
      default: 0
    },
    onChildEvent: {
      type: Function,
      required: true
    }
  },

  data() {
    return {
      navItems: [
        '推荐',
        '同城',
        '关注',
        '直播',
        '经验',
        '商城',
        '团购',
        '精选',
        '热点'
      ],
      underlineVisible: true,
      isBlack: false,
      // 图片路径使用相对路径，更符合uni-app规范
      menuStyle: {
        backgroundImage: 'url(../../assets/home/menu.png)'
      },
      searchStyle: {
        backgroundImage: 'url(../../assets/home/search.png)'
      },
      // 触摸相关变量
      isDragging: false,
      startX: 0,
      scrollLeft: 0
    };
  },
  methods: {
    gosearch() {
      // 使用uni-app的路由API
      uni.navigateTo({
        url: '/pages/search/search'
      });
    },
    triggerParentEvent() {
      // 调用父组件的方法
      this.onChildEvent();
    },
    changeIndex(index) {
      this.$emit('update:index', index);
      this.updateUnderline();

      // 根据选中的导航项改变样式
      const specialItems = ['商城', '团购', '经验', '精选', '热点'];
      if (specialItems.includes(this.navItems[index])) {
        this.isBlack = true;
        this.menuStyle = {
          backgroundImage: 'url(../../assets/home/menu_black.png)'
        };
        this.searchStyle = {
          backgroundImage: 'url(../../assets/home/search_black.png)'
        };
      } else {
        this.isBlack = false;
        this.menuStyle = {
          backgroundImage: 'url(../../assets/home/menu.png)'
        };
        this.searchStyle = {
          backgroundImage: 'url(../../assets/home/search.png)'
        };
      }
    },
    updateUnderline() {
      // 获取组件实例的$nextTick方法
      this.$nextTick(() => {
        const underline = this.$refs.underline;
        const navItems = this.$refs.navItem;
        
        // 添加安全检查
        if (!underline || !navItems || !Array.isArray(navItems) || navItems.length === 0) {
          return;
        }

        // 确保 activeIndex 在有效范围内
        const validIndex = Math.max(0, Math.min(this.activeIndex, navItems.length - 1));
        const activeItem = navItems[validIndex];

        if (activeItem) {
          const nav = this.$refs.nav;
          // 使用uni-app兼容的方式获取元素尺寸
          const itemWidth = uni.createSelectorQuery().in(this).select(`.nav-item:nth-child(${validIndex + 1})`).boundingClientRect().exec(res => {
            if (res && res[0]) {
              const rect = res[0];
              underline.style.left = `${rect.left - nav.offsetLeft}px`;
              underline.style.width = `${rect.width}px`;
            }
          });

          // 滚动到当前选中项
          this.scrollToActiveItem(validIndex);
        }
      });
    },
    scrollToActiveItem(index) {
      const nav = this.$refs.nav;
      const navItems = this.$refs.navItem;
      
      if (!nav || !navItems || !Array.isArray(navItems) || !navItems[index]) {
        return;
      }

      const activeItem = navItems[index];
      const itemWidth = activeItem.offsetWidth;
      const itemOffsetLeft = activeItem.offsetLeft;
      const navWidth = nav.clientWidth;
      
      // 计算滚动位置，使选中项居中显示
      const scrollPosition = itemOffsetLeft - (navWidth / 2) + (itemWidth / 2);
      
      // 使用scroll-view的scroll-into-view或手动设置scrollLeft
      if (scrollPosition > 0) {
        nav.scrollLeft = scrollPosition;
      }
    },
    // 触摸事件处理 - 适配移动端
    onTouchStart(e) {
      this.isDragging = true;
      this.startX = e.touches[0].pageX - this.$refs.nav.offsetLeft;
      this.scrollLeft = this.$refs.nav.scrollLeft;
    },
    onTouchMove(e) {
      if (!this.isDragging) return;
      e.preventDefault();
      const x = e.touches[0].pageX - this.$refs.nav.offsetLeft;
      const walk = (x - this.startX) * 2; // 滚动速度
      this.$refs.nav.scrollLeft = this.scrollLeft - walk;
    },
    onTouchEnd() {
      this.isDragging = false;
      this.updateUnderline();
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.updateUnderline();
    });
  },
  watch: {
    activeIndex() {
      this.$nextTick(() => {
        this.updateUnderline();
      });
    }
  }
};
</script>

<style scoped>
.header {
  position: relative;
  width: 100%;
  height: 5vh;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.nav {
  position: relative;
  width: 80vw;
  height: 100%;
  display: flex;
  flex-direction: row;
  overflow-x: auto;
  align-items: center;
  -webkit-overflow-scrolling: touch; /* 提升iOS上的滚动体验 */
}

/* 隐藏滚动条但保持功能 */
.nav::-webkit-scrollbar {
  display: none;
}
.nav {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.nav-item {
  color: rgba(255, 255, 255, 0.9);
  font-size: 28rpx;
  margin: 0 24rpx;
  flex-shrink: 0;
  user-select: none;
  padding: 10rpx 0;
}

.underline-container {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2rpx;
  background-color: transparent;
  overflow: hidden;
  pointer-events: none;
}

.underline {
  position: absolute;
  bottom: 0;
  height: 2rpx;
  background-color: white;
  transition: left 0.3s ease, width 0.3s ease;
}

.active {
  color: white;
  font-weight: bold;
}

.white-text {
  color: white !important;
}

.black-text {
  color: black !important;
}

.search {
  width: 40rpx;
  height: 40rpx;
  background-size: 32rpx 32rpx;
  background-repeat: no-repeat;
  background-position: center;
  margin-right: 20rpx;
}

.menu {
  width: 40rpx;
  height: 40rpx;
  background-size: 32rpx 28rpx;
  background-repeat: no-repeat;
  background-position: center;
  margin-left: 20rpx;
}

/* 适配不同设备尺寸 */
@media screen and (min-width: 768px) {
  .nav-item {
    font-size: 32rpx;
    margin: 0 32rpx;
  }
  
  .search, .menu {
    width: 50rpx;
    height: 50rpx;
    background-size: 40rpx 40rpx;
  }
}
</style>
