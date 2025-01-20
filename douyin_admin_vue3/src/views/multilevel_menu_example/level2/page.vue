<route lang="yaml">
meta:
  title: 导航2-1
</route>

<script setup>
import UserApi from '@/api/user/user' // 导入API请求方法
import { onMounted, ref } from 'vue'

const users = ref([])
const isModalVisible = ref(false)
const isEditMode = ref(false)
const currentUser = ref({
  id: null,
  username: '',
  nickname: '',
  email: '',
  role: 'user',
})

// 页面加载时触发请求
onMounted(async () => {
  try {
    const res = await UserApi.getAlluser() // 使用 await 等待请求完成
    console.log('请求成功:', res.data)
    users.value = res.data
  }
  catch (error) {
    console.error('请求失败:', error)
  }
})

// 打开添加用户的模态框
function openAddUserModal() {
  currentUser.value = { id: null, username: '', nickname: '', email: '', role: 'user' }
  isModalVisible.value = true
  isEditMode.value = false
}

// 打开编辑用户的模态框
function editUser(user) {
  currentUser.value = { ...user }
  isModalVisible.value = true
  isEditMode.value = true
}

// 保存用户信息（添加或编辑）
function saveUser() {
  if (isEditMode.value) {
    // 调用API进行编辑
    console.log('编辑用户:', currentUser.value)
  }
  else {
    // 调用API进行添加
    console.log('添加用户:', currentUser.value)
  }
  closeModal()
}

// 关闭模态框
function closeModal() {
  isModalVisible.value = false
}

// 删除用户
function deleteUser(userId) {
  console.log('删除用户ID:', userId)
  // 调用API进行删除
}
</script>

<template>
  <div class="user-management">
    <FaPageMain>
      用户管理
    </FaPageMain>

    <!-- 添加用户按钮 -->
    <div class="add-user-container">
      <button class="add-user-btn" @click="openAddUserModal">
        添加用户
      </button>
    </div>

    <!-- 用户列表 -->
    <div class="table-container">
      <table class="user-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>昵称</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.nickname }}</td>
            <td>{{ user.email || '无' }}</td>
            <td>{{ user.role || '未分配' }}</td>
            <td>
              <button class="edit-btn" @click="editUser(user)">
                编辑
              </button>
              <button class="delete-btn" @click="deleteUser(user.id)">
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 编辑/添加用户的模态框 -->
    <div v-if="isModalVisible" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ isEditMode ? '编辑用户' : '添加用户' }}</h3>
        <form class="user-form" @submit.prevent="saveUser">
          <div class="form-group">
            <label for="username">用户名</label>
            <input id="username" v-model="currentUser.username" type="text" required>
          </div>
          <div class="form-group">
            <label for="nickname">昵称</label>
            <input id="nickname" v-model="currentUser.nickname" type="text" required>
          </div>
          <div class="form-group">
            <label for="email">邮箱</label>
            <input id="email" v-model="currentUser.email" type="email">
          </div>
          <div class="form-group">
            <label for="role">角色</label>
            <select id="role" v-model="currentUser.role">
              <option value="admin">
                管理员
              </option>
              <option value="user">
                用户
              </option>
              <option value="guest">
                访客
              </option>
            </select>
          </div>
          <div class="form-actions">
            <button type="submit" class="submit-btn">
              {{ isEditMode ? '保存修改' : '添加用户' }}
            </button>
            <button type="button" class="cancel-btn" @click="closeModal">
              取消
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.user-management {
  font-family: 'Arial', sans-serif;
  padding: 2vh 4vw;
}

.add-user-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 2vh;
}

.add-user-btn {
  padding: 1vh 2vw;
  font-size: 1.2rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-user-btn:hover {
  background-color: #45a049;
}

.table-container {
  overflow-x: auto;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #ddd;
}

.user-table th,
.user-table td {
  padding: 1vh 2vw;
  text-align: left;
}

.user-table th {
  background-color: #f4f4f4;
}

.edit-btn,
.delete-btn {
  padding: 0.5vh 1vw;
  margin-right: 1vw;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.edit-btn {
  background-color: #ffa500;
  color: white;
}

.edit-btn:hover {
  background-color: #e69500;
}

.delete-btn {
  background-color: #f44336;
  color: white;
}

.delete-btn:hover {
  background-color: #e53935;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 3vh 5vw;
  border-radius: 8px;
  width: 40vw;
  max-width: 600px;
}

.user-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 2vh;
}

label {
  font-size: 1.1rem;
  margin-bottom: 0.5vh;
}

input,
select {
  padding: 1vh;
  font-size: 1rem;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 2vh;
}

.submit-btn,
.cancel-btn {
  padding: 1vh 2vw;
  font-size: 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn {
  background-color: #4CAF50;
  color: white;
}

.submit-btn:hover {
  background-color: #45a049;
}

.cancel-btn {
  background-color: #f44336;
  color: white;
}

.cancel-btn:hover {
  background-color: #e53935;
}
</style>
