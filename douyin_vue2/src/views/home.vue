<template>
  <div
    class="homecontain"
    @mousedown="startDrag"
    @mouseup="stopDrag"
    @mousemove="drag"
    @mouseleave="stopDrag"
    @touchstart="startDrag"
    @touchend="stopDrag"
    @touchmove="drag"
  >
    <caidan :style="caidancss"></caidan>

    <div class="home" :style="boxvideocss">
      <header-vue
        class="headercontain"
        :activeIndex="activeIndex"
        @update:index="updateActiveIndex"
        :onChildEvent="showcaidanone"
      ></header-vue>

      <!-- 容器div，包含鼠标和触摸事件监听 -->
      <div class="videomain">
        <!-- 循环渲染每个box，设置样式 -->
        <div
          v-for="(box, index) in boxes"
          :key="index"
          class="box"
          :ref="'box-' + index"
          :style="{
            left: box.left * 0.1 + 'vw',
            backgroundColor: box.color,
            width: boxWidth * 0.1 + 'vw',
            height: boxHeight + 'vh'
          }"
        >
          <!-- <div class="videobox"><videobox-vue></videobox-vue></div> -->
          <div class="videobox">
            <all-box-vue
              v-if="visibleIndexes.includes(index)"
              :boxtype="boxes[index].boxtest"
            ></all-box-vue>
          </div>
        </div>
      </div>
      <pinglunqu
        class="lpinglunqu"
        v-if="message"
        @send-message="handleChildMessage"
      ></pinglunqu>
      <div class="danmubox" v-if="showdanmu">
        <danmu
          :width="'100%'"
          :height="'400px'"
          :defaultSpeed="8"
          :maxDanmuCount="15"
          ref="danmuComponent"
        />

        <input
          v-model="danmuText"
          @keyup.enter="sendDanmu"
          placeholder="请输入弹幕"
          v-if="showdanmu"
        />
      </div>

      <!-- 统一弹窗 -->
      <SlidePopup
        v-if="sharedState.commentVisible"
        :direction="'bottom'"
        width="100vw"
        height="30vh"
        :isEdge="false"
        @close="closeAllPopups"
      >
        <template #default>
          <div style="padding: 10px">
            <p style="font-size: 0.6rem">分享给</p>
            <HorizontalScrollList :users="topUsers" />
            <HorizontalScrollList :users="bottomUsers" />
          </div>
        </template>
      </SlidePopup>
      <footer-vue></footer-vue>
    </div>
  </div>
</template>
<script>
import HorizontalScrollList from '@/components/HorizontalScrollList.vue';
import caidan from '../components/caidan.vue';
import AllBoxVue from '../components/allbox.vue';
import footerVue from '../components/footer.vue';
import headerVue from '../components/header.vue';
import Pinglunqu from '@/components/Pinglunqu.vue';
import SlidePopup from '@/components/SlidePopup.vue';
import Danmu from '../components/Danmu.vue';
import Vue from 'vue';
export default {
  provide() {
    // 提供一个回调函数给后代组件调用
    return {
      fromAside: this.handleAside,
      sharedState: this.sharedState
    };
  },

  data() {
    return {
      danmuText: '',
      showdanmu: false,
      topUsers: [
        {
          name: '单曲猫咪',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280',
          heat: 91
        },
        {
          name: '阿泽',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '小发发',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        }
        // ...
      ],
      bottomUsers: [
        {
          name: '推荐',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        },
        {
          name: '分享到日常',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        }
      ],
      sharedState: Vue.observable({
        commentVisible: false
      }),
      isAnyPopupVisible: false, // 是否有弹窗显示的标志
      message: false,
      isDragging: false, // 是否正在拖动的标志
      startX: 0, // 拖动的起始X位置
      currentX: 0, // 当前的X位置
      boxWidth: 1000, // 每个box的宽度
      activeIndex: 0, // 默认高亮推荐项
      boxHeight: 95,
      boxes: [], // box的数据，包括颜色和左边距
      visibleIndexes: [], // 存储进入视口的 box 的索引
      caidancss: {
        position: 'absolute',
        right: '100vw',
        bottom: '0px',
        transition: 'right 0.5s ease',
        height: '100vh' /* 控制位置变化的动画 */
      },
      boxvideocss: {
        transform: 'translateX(0px)',
        overflow: 'hidden',
        transition: 'transform 0.5s ease' /* 控制位置变化的动画 */
      }
    };
  },

  components: {
    SlidePopup,
    headerVue,
    footerVue,
    AllBoxVue,
    caidan,
    Pinglunqu,
    HorizontalScrollList,
    Danmu
  },
  mounted() {
    this.boxes.forEach((_, index) => {
      const el = this.$refs['box-' + index][0]; // 注意：ref 是数组
      const observer = new IntersectionObserver(
        ([entry]) => {
          if (entry.isIntersecting) {
            if (!this.visibleIndexes.includes(index)) {
              this.visibleIndexes.push(index);
            }
          }
        },
        {
          threshold: 1.0 // 100% 进入才触发
        }
      );

      if (el) observer.observe(el);
    });
  },

  created() {
    // 初始化 boxes 的数据，包括颜色和左边距
    this.boxes = [
      { color: 'black', left: 0, boxtest: '推荐' },
      { color: 'black', left: this.boxWidth, boxtest: '同城' },
      { color: 'black', left: 2 * this.boxWidth, boxtest: '关注' },
      { color: 'black', left: 3 * this.boxWidth, boxtest: '直播' }
      // { color: "pink", left: 4 * this.boxWidth, boxtest: "经验" },
      // { color: "white", left: 5 * this.boxWidth, boxtest: "商城" },
      // { color: "pink", left: 6 * this.boxWidth, boxtest: "团购" },
      // { color: "pink", left: 7 * this.boxWidth, boxtest: "精选" },
      // { color: "pink", left: 8 * this.boxWidth, boxtest: "热点" },
    ];
  },
  beforeDestroy() {},
  methods: {
    closeAllPopups() {
      // 关闭所有弹窗
      this.sharedState.commentVisible = false;
    },
    sendDanmu() {
      if (this.danmuText.trim()) {
        // 触发弹幕组件中的sendDanmu方法
        this.$refs.danmuComponent.sendDanmu(this.danmuText);
        this.danmuText = ''; // 清空输入框
      }
    },
    handleAside(msg) {
      // 处理来自子组件的消息
      console.log('父组件收到的消息：', msg);
      this.message = msg;
    },

    handleChildMessage(msg) {
      console.log('父组件收到的消息：', msg);
      this.message = msg;
    },
    updateActiveIndex(index) {
      this.activeIndex = index;
      this.go(index);
    },
    startDrag(event) {
      //会影响到子组件事件的执行
      // event.preventDefault(); // 阻止默认行为
      this.isDragging = true;
      this.startX = this.getEventClientX(event) - this.currentX;
    },
    stopDrag() {
      // event.preventDefault(); // 阻止默认行为
      this.isDragging = false;
      this.updateBoxPositionBasedOnCurrentX();
    },
    drag(event) {
      if (this.isDragging) {
        let newX = this.getEventClientX(event) - this.startX;
        newX = Math.max(
          -(this.boxWidth * (this.boxes.length - 1)),
          Math.min(newX, 0)
        );
        this.currentX = newX;
        this.updateBoxesPosition();

        // 计算当前索引并更新
        const nearestIndex = Math.round(
          Math.abs(this.currentX) / this.boxWidth
        );
        this.activeIndex = nearestIndex; // 更新 activeIndex

        // 将索引传递给子组件
        this.$emit('update:index', nearestIndex);
      }
    },
    getEventClientX(event) {
      return event.type.startsWith('touch')
        ? event.touches[0].clientX
        : event.clientX;
    },
    updateBoxesPosition() {
      this.boxes.forEach((box, index) => {
        box.left = this.currentX + index * this.boxWidth;
      });
    },
    updateBoxPositionBasedOnCurrentX() {
      const nearestIndex = Math.round(Math.abs(this.currentX) / this.boxWidth);
      this.currentX = -nearestIndex * this.boxWidth;
      this.updateBoxesPosition();
    },

    go(index) {
      this.activeIndex = index;
      const newX = -index * this.boxWidth;
      this.currentX = newX;
      this.updateBoxesPosition();

      // 同时将索引传递给子组件
      this.$emit('update:index', index);
    },

    showcaidanone() {
      // 更新 caidancss 的位置
      this.caidancss.right =
        this.caidancss.right === '100vw' ? '20vw' : '100vw';

      // 使用 Vue 的 $set 方法来触发响应式系统检测到 transform 属性变化
      this.$set(
        this.boxvideocss,
        'transform',
        this.boxvideocss.transform === 'translateX(0px)'
          ? 'translateX(80vw)'
          : 'translateX(0px)'
      );
    }
  }
};
</script>
<style scoped>
.homecontain {
  overflow-x: hidden;
  /* 禁止水平滚动 */
}
.danmubox {
  position: absolute;
  width: 100vw;
  left: 50%;
  transform: translateX(-50%); /* 使元素水平居中 */
}

.home {
  display: flex;
  width: 100%;
  height: 100%;
  flex-direction: column;
  background-color: black;
  overflow: hidden;
  margin: 0;
}

.headercontain {
  position: absolute;
  top: 0px;
  z-index: 10;
}

/* ****************************** */

.lpinglunqu {
  position: absolute;
  left: 0px;
  bottom: 0px;
  z-index: 10;
}

.videomain {
  display: flex;
  /* 使用flex布局 */
  width: 100vw;
  height: 95vh;
  position: relative;
  /* 相对定位 */
  overflow: hidden;
}

.box {
  position: absolute;
  /* 绝对定位 */
  transition: left 0.3s ease;
  /* 位置变化的过渡效果 */
  overflow: hidden;
}

/* 视频播放区域 */
.videobox {
  width: 100vw;
  background-color: black;
}

/* ****************************** */
</style>
