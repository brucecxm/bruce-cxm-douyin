<template>
  <div class="section">
    <div class="header">
      <p class="title">历史记录</p>
      <template v-if="!editMode">
        <button class="icon-btn" @click="editMode = true">🗑</button>
      </template>
      <template v-else>
        <button class="action-btn" @click="handleClearAll">全部删除</button>
        <button class="action-btn" @click="editMode = false">完成</button>
      </template>
    </div>

    <div class="history-list">
      <span v-for="(item, index) in history" :key="index" class="history-item">
        {{ item }}
        <button
          v-if="editMode"
          class="close-btn"
          @click="$emit('remove-item', index)"
        >
          ×
        </button>
      </span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    history: Array
  },
  data() {
    return {
      editMode: false
    };
  },
  methods: {
    handleClearAll() {
      this.$emit('clear');
      this.editMode = false;
    }
  }
};
</script>

<style scoped>
.section {
  padding: 0.5rem;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-size: 0.5rem;
  margin: 0;
}

.icon-btn,
.action-btn {
  background: transparent;
  border: none;
  font-size: 0.5rem;
  cursor: pointer;
  padding: 2px 6px;
}

.history-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
  margin-top: 6px;
}

.history-item {
  background: #fff;
  padding: 6px 10px;
  border-radius: 12px;
  font-size: 14px;
  border: 1px solid #eee;
  position: relative;
  display: flex;
  align-items: center;
}

.close-btn {
  margin-left: 6px;
  background: transparent;
  border: none;
  color: #888;
  font-size: 14px;
  cursor: pointer;
}
</style>
