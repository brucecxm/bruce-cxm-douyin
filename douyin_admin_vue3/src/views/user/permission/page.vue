<!-- <script setup>
import {
  addPermission,
  deletePermissionByIds,
  getList,
  updatePermission,
} from '@/api/permission/permission'
import { onMounted, ref } from 'vue'

const permissions = ref([])
const dialogVisible = ref(false)
const form = ref({ id: null, name: '', description: '' })

async function loadData() {
  const res = await getList({ current: 1, size: 100 })
  permissions.value = res.records || []
}

function openDialog() {
  form.value = { id: null, name: '', description: '' }
  dialogVisible.value = true
}

function editPermission(row) {
  form.value = { ...row }
  dialogVisible.value = true
}

async function submitForm() {
  if (form.value.id) {
    await updatePermission(form.value)
  }
  else {
    await addPermission(form.value)
  }
  dialogVisible.value = false
  await loadData()
}

async function deletePermission(id) {
  await deletePermissionByIds([id])
  await loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="permission-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>权限管理</span>
          <el-button type="primary" @click="openDialog">
            新增权限
          </el-button>
        </div>
      </template>

      <el-table :data="permissions" stripe border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="权限名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="editPermission(row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="deletePermission(row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="权限表单" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="submitForm">
          提交
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.permission-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> -->
