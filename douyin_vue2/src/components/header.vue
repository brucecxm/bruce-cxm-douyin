<template>
  <div class="header">
    <div class="menu" :style="menuStyle" @click="triggerParentEvent"></div>
    <div class="nav" ref="nav">
      <p v-for="(item, index) in navItems" :key="index"
         :class="{ 'active': activeIndex === index, 'black-text': isBlack, 'white-text': !isBlack }"
         @click="changeIndex(index)" ref="navItem">
        {{ item }}
      </p>
      <div class="underline-container">
        <div class="underline" ref="underline" v-show="underlineVisible"></div>
      </div>
    </div>
    <div class="search" :style="searchStyle" @click="gosearch"></div>
  </div>
</template>

<script>
import heicaidan from '@/assets/home/黑菜单.png';
import heifangdajing from '@/assets/home/黑放大镜.png';
import caidan from '@/assets/home/菜单.png';
import fangdajing from '@/assets/home/放大镜.png';
export default {
  props: {
    activeIndex: {
      type: Number,
      default: 0
    },
    onChildEvent: {
      type: Function,
      required: true,
    },
  },

  data() {
    return {
      navItems: ["推荐", "同城", "关注", "直播", "经验", "商城", "团购", "精选", "热点"],
      underlineVisible: true, // 控制下划线的可见性
      isBlack: false, // 控制是否将所有文字变成黑色
      menuStyle: { // 默认的菜单图标样式
        backgroundImage:`url(${caidan})`,
      },
      searchStyle: { // 默认的搜索图标样式
        backgroundImage:`url(${fangdajing})`,
      }
    };
  },
  methods: {
    gosearch() {
      this.$router.push("/search")
    },
    triggerParentEvent() {
      // 调用父组件的方法
      this.onChildEvent();
    },
    changeIndex(index) {
      this.$emit('update:index', index);
      this.updateUnderline(); // 更新下划线位置

      // 如果选择了商城、团购或经验，将所有文字变成黑色，并修改图标
      if (this.navItems[index] === "商城" || this.navItems[index] === "团购" || this.navItems[index] === "经验"
          || this.navItems[index] === "团购" || this.navItems[index] === "精选" || this.navItems[index] === "热点"
      ) {
        this.isBlack = true;
        this.menuStyle = {
          backgroundImage:`url(${heicaidan})`,
        };
        this.searchStyle = {
          backgroundImage:`url(${heifangdajing})`,
        };
      } else {
        this.isBlack = false;
        this.menuStyle = {
          backgroundImage:`url(${caidan})`,
        };
        this.searchStyle = {
          backgroundImage:`url(${fangdajing})`,
        };
      }
    },
    updateUnderline() {
      const underline = this.$refs.underline;
      const activeItem = this.$refs.navItem[this.activeIndex]; // 获取当前高亮项
      if (activeItem) {
        const nav = this.$refs.nav;
        const itemWidth = activeItem.offsetWidth;
        const itemOffsetLeft = activeItem.offsetLeft;
        underline.style.left = `${itemOffsetLeft}px`;
        underline.style.width = `${itemWidth}px`;

        // 确保当前项在视口内，更新下划线的可见性
        const navScrollLeft = nav.scrollLeft;
        const navWidth = nav.clientWidth;

        const isVisible = (
            itemOffsetLeft >= navScrollLeft &&
            itemOffsetLeft + itemWidth <= navScrollLeft + navWidth
        );

        this.underlineVisible = isVisible; // 根据是否可见更新下划线
      }
    }
  },
  mounted() {
    this.updateUnderline();
    const nav = this.$refs.nav;
    let isDragging = false;
    let startX;
    let scrollLeft;

    nav.addEventListener('mousedown', (e) => {
      isDragging = true;
      startX = e.pageX - nav.offsetLeft;
      scrollLeft = nav.scrollLeft;
    });

    nav.addEventListener('mouseleave', () => {
      isDragging = false;
    });

    nav.addEventListener('mouseup', () => {
      isDragging = false;
    });

    nav.addEventListener('mousemove', (e) => {
      if (!isDragging) return;
      e.preventDefault();
      const x = e.pageX - nav.offsetLeft;
      const scroll = (x - startX) * 2;
      nav.scrollLeft = scrollLeft - scroll;
      this.updateUnderline(); // 在移动时更新下划线位置和可见性
    });
  },
  watch: {
    activeIndex(newIndex) {
      this.updateUnderline(); // 更新下划线位置
    }
  }
};
</script>

<style scoped>
.header {
  position: relative;
}

.nav {
  position: relative;
}

.underline-container {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 0.1rem;
  /* 与下划线高度一致 */
  background-color: inherit;
  /* 与导航栏背景色一致 */
  overflow: hidden;
  /* 让下划线在容器内被遮住 */
}

.underline {
  position: absolute;
  bottom: 0;
  height: 0.1rem;
  background-color: white;
  transition: left 0.3s ease, width 0.3s ease;
}

.header {
  width: 100%;
  height: 5vh;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.nav {
  width: 80vw;
  height: 100%;
  display: flex;
  flex-direction: row;
  overflow: hidden;
  align-items: center;
  overflow-x: auto;
  cursor: grab;
  color: white;
}

.nav::-webkit-scrollbar {
  display: none;
}

p {
  color: rgb(255, 255, 255, 0.9);
  font-size: 0.5rem;
  margin: 0.6rem;
  flex-shrink: 0;
  user-select: none;
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
  width: 1rem;
  height: 1rem;
  background-size: 0.8rem 0.8rem;
  background-repeat: no-repeat;
  margin-right: 0.5rem;
  margin-top: 0.25rem;
}

.menu {
  width: 1rem;
  height: 1rem;
  background-size: 0.8rem 0.7rem;
  background-repeat: no-repeat;
  margin-left: 0.5rem;
  margin-top: 0.5rem;
}
</style>
