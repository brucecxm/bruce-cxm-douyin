<template>
  <div class="friend">
    <div class="header">
      <div class="fanhui" @click="onBackClick" title="返回">
        <svg viewBox="0 0 24 24" class="icon">
          <path d="M15 18l-6-6 6-6" stroke="white" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>

      <div class="tabs">
        <div
          v-for="(tag, index) in tags"
          :key="index"
          :class="['tab', activeName === tag ? 'active' : '']"
          @click="changeTab(tag)"
        >
          {{ tag }}
          <div v-if="activeName === tag" class="underline"></div>
        </div>
      </div>

      <div class="shezhi" @click="onSettingsClick" title="设置">
        <svg viewBox="0 0 24 24" class="icon">
          <circle cx="12" cy="12" r="3" stroke="white" stroke-width="2" fill="none"/>
          <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 1 1-4 0v-.09a1.65 1.65 0 0 0-1-1.51 1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 1 1 0-4h.09a1.65 1.65 0 0 0 1.51-1 1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33h.09a1.65 1.65 0 0 0 1-1.51V3a2 2 0 1 1 4 0v.09a1.65 1.65 0 0 0 1 1.51h.09a1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82v.09a1.65 1.65 0 0 0 1.51 1H21a2 2 0 1 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z" stroke="white" stroke-width="2" fill="none" stroke-linejoin="round"/>
        </svg>
      </div>
    </div>

    <transition :name="transitionName" mode="out-in">
      <div class="user-list" :key="activeName">
        <el-input
    v-model="input"
    placeholder="请输入用户名"
    prefix-icon="el-icon-search">
  </el-input>
        <user-info
          v-for="(item, index) in filteredUserInfo"
          :key="index"
          :message="item"
        />
        <div v-if="filteredUserInfo.length === 0" class="no-data">暂无数据</div>
      </div>
    </transition>
  </div>
</template>

<script>
import UserInfo from '../../components/userInfo.vue';

export default {
  components: { UserInfo },
  data() {
    return {
      input: '',
      transitionName: 'slide-left', // 默认动画
      tags: ['互关', '关注', '粉丝', '朋友'],
      activeName: '关注',
      previousIndex: 1, // 默认选中“关注”的索引
      allUserInfo: {
        '互关': [
          {
            username: 'wang',
            status: '在线',
            avatar: 'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
            height: '60px',
          },
          {
            username: 'wang',
            status: '在线',
            avatar: 'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
            height: '60px',
          },
          {
            username: 'wang',
            status: '在线',
            avatar: 'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
            height: '60px',
          },
          {
            username: 'wang',
            status: '在线',
            avatar: 'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
            height: '60px',
          },
        ],
        '关注': [
          {
            username: 'li',
            status: '在线',
            avatar: 'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
            height: '60px',
          },
        ],
        '粉丝': [
          {
            username: 'chen',
            status: '在线',
            avatar: 'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
            height: '60px',
          },
        ],
        '朋友': [
          {
            username: 'zhao',
            status: '在线',
            avatar: 'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
            height: '60px',
          },
        ],
      },
    };
  },
  computed: {
    filteredUserInfo() {
      return this.allUserInfo[this.activeName] || [];
    },
  },
  methods: {
    changeTab(tag) {
      const newIndex = this.tags.indexOf(tag);
      const oldIndex = this.previousIndex;
      this.transitionName = newIndex > oldIndex ? 'slide-left' : 'slide-right';
      this.activeName = tag;
      this.previousIndex = newIndex;
    },
    onBackClick() {
      alert('点击了返回按钮');
    },
    onSettingsClick() {
      alert('点击了设置按钮');
    },
  },
};
</script>

<style scoped>
.friend {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 头部 */
.header {
  display: flex;
  align-items: center;
  width: 100%;
  height: 40px;
  background: linear-gradient(90deg, #b7b8c4, #8f94fb);
  padding: 0 12px;
  box-sizing: border-box;
  color: white;
}

/* 左右按钮 */
.fanhui, .shezhi {
  width: 40px;
  height: 40px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}

.fanhui:hover, .shezhi:hover {
  background: rgba(255, 255, 255, 0.15);
  border-radius: 6px;
}

/* 图标大小 */
.icon {
  width: 24px;
  height: 24px;
  stroke: white;
  fill: none;
}

/* 中间标签栏 横向排列 */
.tabs {
  flex: 1;
  display: flex;
  justify-content: center;
  gap: 30px;
  user-select: none;
  font-size: 16px;
  font-weight: 500;
  position: relative;
}

.tab {
  position: relative;
  padding: 8px 0;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.7);
  font-weight: 500;
  transition: color 0.3s;
}

.tab:hover {
  color: #fff;
}

.tab.active {
  color: #fff;
  font-weight: 700;
}

/* 下划线动画 */
.underline {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #fff;
  border-radius: 1px;
  transition: all 0.3s ease;
}

/* 用户列表区域 */
.user-list {
  width: 100%;
  height: 90vh;
  margin-top: 12px;
  padding: 0 12px 12px;
  box-sizing: border-box;
  overflow-y: auto;
  flex: 1;
  overflow: hidden;
}

/* 无数据提示 */
.no-data {
  text-align: center;
  color: #999;
  padding: 40px 0;
  font-size: 18px;
}

/* 保留之前的滑动动画 */
.slide-left-enter-active, .slide-left-leave-active {
  transition: all 0.3s ease;
  position: relative;
}
.slide-left-enter {
  transform: translateX(100%);
  opacity: 0;
}
.slide-left-enter-to {
  transform: translateX(0);
  opacity: 1;
}
.slide-left-leave {
  transform: translateX(0);
  opacity: 1;
  position: absolute;
  width: 100%;
  top: 0;
  left: 0;
}
.slide-left-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}

.slide-right-enter-active, .slide-right-leave-active {
  transition: all 0.3s ease;
  position: relative;
}
.slide-right-enter {
  transform: translateX(-100%);
  opacity: 0;
}
.slide-right-enter-to {
  transform: translateX(0);
  opacity: 1;
}
.slide-right-leave {
  transform: translateX(0);
  opacity: 1;
  position: absolute;
  width: 100%;
  top: 0;
  left: 0;
}
.slide-right-leave-to {
  transform: translateX(100%);
  opacity: 0;
}
</style>
