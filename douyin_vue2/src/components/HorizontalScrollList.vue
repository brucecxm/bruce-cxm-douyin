<template>
  <div class="scroll-container" :style="containerStyle">
    <div class="scroll-content" :style="contentStyle">
      <div
        class="user-item"
        v-for="(user, index) in users"
        :key="index"
        :style="getUserItemStyle(user, index)"
        @click="handleUserClick(user, index)"
      >
        <img :src="user.avatar" class="avatar" :style="avatarStyle" />
        <div class="name" :style="nameStyle">{{ user.name }}</div>
        <div v-if="user.heat" class="heat" :style="heatStyle">
          🔥 {{ user.heat }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HorizontalScrollList',
  props: {
    users: {
      type: Array,
      required: true
    },
    containerStyle: {
      type: Object,
      default: () => ({})
    },
    contentStyle: {
      type: Object,
      default: () => ({})
    },
    userItemStyle: {
      type: [Object, Function],
      default: () => ({})
    },
    avatarStyle: {
      type: Object,
      default: () => ({})
    },
    nameStyle: {
      type: Object,
      default: () => ({})
    },
    heatStyle: {
      type: Object,
      default: () => ({})
    }
  },
  methods: {
    getUserItemStyle(user, index) {
      if (typeof this.userItemStyle === 'function') {
        return this.userItemStyle(user, index);
      }
      return this.userItemStyle;
    },
    handleUserClick(user, index) {
      // 触发自定义事件，将用户数据传递给父组件
      this.$emit('user-click', user, index);
    }
  }
};
</script>

<style scoped>
.scroll-container {
  overflow-x: auto;
  white-space: nowrap;
  padding: 10px 0;

  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
}

.scroll-container::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

.scroll-content {
  display: flex;
  flex-direction: row;
}

.user-item {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  margin-right: 16px;
  width: 64px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.user-item:hover {
  transform: scale(1.05);
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.name {
  font-size: 12px;
  margin-top: 6px;
  text-align: center;
  white-space: nowrap;
}

.heat {
  font-size: 10px;
  color: orange;
  margin-top: 2px;
}
</style>
