<template>
  <div class="danmu-container" :style="containerStyle" ref="danmuContainer">
    <transition-group name="danmu-move" tag="div">
      <div
        v-for="(danmu, index) in danmuList"
        :key="danmu.id"
        class="danmu"
        :style="getDanmuStyle(danmu)"
      >
        {{ danmu.text }}
      </div>
    </transition-group>
  </div>
</template>

<script>
export default {
  props: {
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '500px'
    },
    defaultSpeed: {
      type: Number,
      default: 10
    },
    maxDanmuCount: {
      type: Number,
      default: 20
    }
  },
  data() {
    return {
      danmuList: [],
      socket: null,
      sender: '匿名用户',
      danmuId: 0 // 用来为每个弹幕生成唯一的 id
    };
  },
  computed: {
    containerStyle() {
      return {
        width: this.width,
        height: this.height,
        position: 'relative',
        backgroundColor: '#000',
        overflow: 'hidden'
      };
    }
  },
  mounted() {
    this.socket = new WebSocket(
      '/api/video/ws?username=' + encodeURIComponent(this.sender)
    );
    this.socket.onmessage = (event) => {
      const danmu = JSON.parse(event.data);
      this.addDanmu(danmu);
    };
    this.socket.onclose = () => {
      console.log('WebSocket connection closed');
    };
  },
  methods: {
    addDanmu(danmu) {
      // 给每个弹幕一个唯一的 id
      danmu.id = this.danmuId++;

      // 设置弹幕的初始位置
      danmu.left = '90%'; // 弹幕从屏幕右边开始显示

      // 弹幕的初始纵向位置
      danmu.top = Math.random() * 80 + 10; // 随机生成顶部位置

      this.danmuList.push(danmu);
      if (this.danmuList.length > this.maxDanmuCount) {
        this.danmuList.shift();
      }

      // 动态生成 @keyframes 动画
      this.createDanmuAnimation(danmu);
      this.$forceUpdate();
    },
    createDanmuAnimation(danmu) {
      const style = document.createElement('style');
      style.innerHTML = `
        @keyframes moveDanmu${danmu.id} {
          0% {
            left: 100%;
          }
          100% {
            left: -100%;
          }
        }
      `;
      document.head.appendChild(style);
    },
    getDanmuStyle(danmu) {
      // 动画时长根据速度进行调整
      const animationDuration = (100 / danmu.speed).toFixed(2) + 's'; // 计算每个弹幕的移动时长

      return {
        position: 'absolute',
        top: `${danmu.top}%`, // 设置弹幕的垂直位置
        left: '100%', // 初始位置从屏幕的右侧开始
        color: danmu.color || this.getRandomColor(),
        fontSize: `${danmu.fontSize || 24}px`,
        fontWeight: danmu.bold ? 'bold' : 'normal',
        fontStyle: danmu.italic ? 'italic' : 'normal',
        whiteSpace: 'nowrap',
        zIndex: 9999,
        animation: `moveDanmu${danmu.id} ${animationDuration} linear forwards`
      };
    },
    getRandomColor() {
      const colors = ['#ff5733', '#33ff57', '#3357ff', '#ff33a8', '#f4e842'];
      return colors[Math.floor(Math.random() * colors.length)];
    },
    sendDanmu(danmuText) {
      if (danmuText.trim()) {
        const danmu = {
          text: danmuText,
          speed: this.defaultSpeed + Math.random() * 5,
          sender: this.sender,
          color: this.getRandomColor(),
          fontSize: Math.floor(18 + Math.random() * 10),
          bold: Math.random() > 0.5,
          italic: Math.random() > 0.5
        };
        this.socket.send(JSON.stringify(danmu));
      }
    }
  }
};
</script>

<style scoped>
.danmu-container {
  position: relative;
  overflow: hidden;
}

.danmu {
  position: absolute;
  white-space: nowrap;
  z-index: 9999;
}
</style>
