<template>
  <div class="scroll-container">
    <div class="scroll-content">
      <div class="user-item" v-for="(user, index) in users" :key="index">
        <img :src="user.avatar" class="avatar" />
        <div class="name">{{ user.name }}</div>
        <div v-if="user.heat" class="heat">🔥 {{ user.heat }}</div>
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
