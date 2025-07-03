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
    <GlassSearch @search="onSearch" @back="onBack" />
    <caidan :style="caidancss"></caidan>

    <div class="home" :style="boxvideocss">
      <div class="videomain">
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
          <div class="videobox">
            <videobox-vue
              :externalVideos="videoList"
              :onReachEnd="handleReachEnd"
              ref="videoboxzidata"
            ></videobox-vue>
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
        v-if="false"
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
    </div>
    <div class="chat-input">
      <input type="text" placeholder="善语结善缘，恶言伤人心" />
      <div class="icons">
        <span>🖼️</span>
        <span>@</span>
        <span>😊</span>
      </div>
    </div>
  </div>
</template>
<script>
import { homegetVideo } from '@/api/video';
import GlassSearch from '@/components/GlassSearch.vue';
import HorizontalScrollList from '@/components/HorizontalScrollList.vue';
import caidan from '@/components/caidan.vue';
import videoboxVue from '@/components/videobox.vue';
import Pinglunqu from '@/components/Pinglunqu.vue';
import SlidePopup from '@/components/SlidePopup.vue';
import Danmu from '@/components/Danmu.vue';
export default {
  // provide() {
  //   // 提供一个回调函数给后代组件调用
  //   return {
  //     fromAside: this.handleAside,
  //     sharedState: this.sharedState
  //   };
  // },

  data() {
    return {
      videoList: [], // 存储全部视频
      page: 0,
      pageSize: 4,
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
      // sharedState: Vue.observable({
      //   commentVisible: false
      // }),
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
    videoboxVue,
    caidan,
    Pinglunqu,
    HorizontalScrollList,
    Danmu,
    GlassSearch
  },
  mounted() {
    // 初始化加载第一页数据
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
    this.$nextTick(() => {
      // 这里可以安全访问子组件引用
      this.handleReachEnd(); // 如果需要在挂载时调用
    });
  },

  created() {
    // 初始化 boxes 的数据，包括颜色和左边距
    this.boxes = [{ color: 'black', left: 0, boxtest: '视频详情' }];
  },
  beforeDestroy() {},
  methods: {
    handleReachEnd(retryCount = 0) {
      // 分页查询视频数据，比如调用接口
      homegetVideo(this.page, this.pageSize, 1)
        .then((newVideos) => {
          const appendData = () => {
            if (
              this.$refs.videoboxzidata[0] &&
              typeof this.$refs.videoboxzidata[0].appendPageData === 'function'
            ) {
              this.$refs.videoboxzidata[0].appendPageData(newVideos.data);
              this.page++;
            } else if (retryCount < 3) {
              console.warn(`子组件未就绪，重试第 ${retryCount + 1} 次...`);
              setTimeout(() => {
                this.handleReachEnd(retryCount + 1); // 递归延迟重试
              }, 500);
            } else {
              console.error('子组件引用不可用，超过最大重试次数');
            }
          };

          appendData();
        })
        .catch((error) => {
          console.error('获取视频出错:', error);
        });
    },
    // handleLoadMore() {
    //   this.page++;
    //   homegetVideo(this.page, this.pageSize, 1).then((res) => {
    //     if (res.data && res.data.length > 0) {
    //       // 把新数据合并到videoList，传给子组件
    //       this.videoList = [...this.videoList, ...res.data];
    //     }
    //   });
    // },
    onSearch(keyword) {
      console.log('搜索内容：', keyword);
    },
    onBack() {
      this.$router.back();
    },
    closeAllPopups() {
      // 关闭所有弹窗
      // this.sharedState.commentVisible = false;
    },
    sendDanmu() {
      if (this.danmuText.trim()) {
        // 触发弹幕组件中的sendDanmu方法
        this.$refs.danmuComponent.sendDanmu(this.danmuText);
        this.danmuText = ''; // 清空输入框
      }
    },
    // handleAside(msg) {
    //   // 处理来自子组件的消息
    //   console.log('父组件收到的消息：', msg);
    //   this.message = msg;
    // },

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
.chat-input {
  width: 100%;
  height: 5vh;
  display: flex;
  align-items: center;
  background-color: #2c2c2e;
  border-radius: 20px;
  padding: 0 10px;
  box-sizing: border-box;
  color: white;
  background-color: #121212;
}

.chat-input input {
  font-size: 0.5rem;
  flex: 1;
  height: 80%;
  border: none;
  background: transparent;
  color: #fff;
  outline: none;
}

.chat-input .icons {
  display: flex;
  gap: 10px;
  align-items: center;
}

.chat-input .icons span {
  cursor: pointer;
  font-size: 1em;
}
/* **************** */
.homecontain {
  overflow-x: hidden;
  /* 禁止水平滚动 */
  background-color: black;
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
