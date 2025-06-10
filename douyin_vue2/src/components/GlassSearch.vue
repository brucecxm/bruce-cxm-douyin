<template>
  <div class="search-bar-wrapper">
    <button class="back-button" @click="goBack">←</button>
    <input
      v-model="keyword"
      type="text"
      placeholder="搜你想看的"
      class="search-input"
    />
    <button class="search-btn" @click="handleSearch">搜索</button>
  </div>
</template>

<script>
export default {
  name: 'GlassSearch',
  data() {
    return {
      keyword: ''
    };
  },
  methods: {
    handleSearch() {
      this.$emit('search', this.keyword);
    },
    goBack() {
      this.$emit('back');
    }
  }
};
</script>

<style scoped>
.search-bar-wrapper {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 90%;
  max-width: 600px;
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 20px;
  backdrop-filter: blur(12px);
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  z-index: 999;
}

.back-button {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  margin-right: 10px;
  cursor: pointer;
}

.search-input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  color: white;
  font-size: 16px;
  padding: 6px;
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.6);
}

.search-btn {
  background: none;
  border: none;
  color: white;
  font-size: 16px;
  margin-left: 10px;
  cursor: pointer;
}
</style>
