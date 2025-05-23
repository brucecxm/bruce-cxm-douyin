<route lang="yaml">
  meta:
    title: 导航菜单管理
  </route>

<script setup>
import menuApi from '@/api/menu/menu'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

// 表格数据
const menuList = ref([])

// 查询条件（示例默认全部）
const queryForm = reactive({
  menuType: null,
  status: null,
})

// 表格loading状态
const loading = ref(false)

// 编辑表单弹窗
const dialogVisible = ref(false)
const editForm = reactive({
  id: null,
  name: '',
  menuType: '',
  status: '',
  // 这里按实际Menu字段添加更多属性
})

// 新增还是编辑
const isEdit = ref(false)

// 选中行
const multipleSelection = ref([])

// 获取列表
async function fetchList() {
  loading.value = true
  try {
    const res = await menuApi.getMenuList(queryForm)
    menuList.value = res.data || []
  }
  catch (error) {
    ElMessage.error('获取菜单列表失败')
  }
  finally {
    loading.value = false
  }
}

// 点击新增
function handleAdd() {
  isEdit.value = false
  Object.assign(editForm, {
    id: null,
    name: '',
    menuType: '',
    status: '',
  })
  dialogVisible.value = true
}

// 点击编辑
function handleEdit(row) {
  isEdit.value = true
  Object.assign(editForm, row)
  dialogVisible.value = true
}

// 保存（新增或更新）
async function handleSave() {
  try {
    if (isEdit.value) {
      await menuApi.updateMenu(editForm)
      ElMessage.success('更新成功')
    }
    else {
      await menuApi.addMenu(editForm)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchList()
  }
  catch (error) {
    ElMessage.error('保存失败')
  }
}

// 删除单条
function handleDelete(row) {
  ElMessageBox.confirm('确认删除该菜单？', '提示', {
    type: 'warning',
  }).then(async () => {
    try {
      await menuApi.deleteMenu([row.id])
      ElMessage.success('删除成功')
      fetchList()
    }
    catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 批量删除
function handleBatchDelete() {
  if (multipleSelection.value.length === 0) {
    return ElMessage.warning('请选择要删除的菜单')
  }
  ElMessageBox.confirm(`确认删除选中的${multipleSelection.value.length}条菜单？`, '提示', {
    type: 'warning',
  }).then(async () => {
    try {
      const ids = multipleSelection.value.map(item => item.id)
      await menuApi.deleteMenu(ids)
      ElMessage.success('删除成功')
      fetchList()
    }
    catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 选择变化
function handleSelectionChange(val) {
  multipleSelection.value = val
}

// 初始化加载数据
onMounted(() => {
  fetchList()
})
</script>

<template>
  <div>
    <FaPageMain>
      <template #header>
        <el-button type="primary" @click="handleAdd">
          新增菜单
        </el-button>
        <el-button type="danger" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">
          批量删除
        </el-button>
      </template>

      <!-- 查询条件示例 -->
      <el-form :model="queryForm" inline>
        <el-form-item label="菜单类型">
          <el-input v-model="queryForm.menuType" placeholder="请输入菜单类型" />
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="queryForm.status" placeholder="请输入状态" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchList">
            查询
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="menuList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="菜单名称" />
        <el-table-column prop="menuType" label="菜单类型" />
        <el-table-column prop="status" label="状态" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 编辑弹窗 -->
      <el-dialog
        v-model:visible="dialogVisible"
        title="菜单编辑"
        width="500px"
        :before-close="() => (dialogVisible.value = false)"
      >
        <el-form :model="editForm" label-width="80px">
          <el-form-item label="名称">
            <el-input v-model="editForm.name" />
          </el-form-item>
          <el-form-item label="菜单类型">
            <el-input v-model="editForm.menuType" />
          </el-form-item>
          <el-form-item label="状态">
            <el-input v-model="editForm.status" />
          </el-form-item>
          <!-- 根据你Menu实体的属性，可以添加更多表单项 -->
        </el-form>

        <template #footer>
          <el-button @click="dialogVisible = false">
            取消
          </el-button>
          <el-button type="primary" @click="handleSave">
            保存
          </el-button>
        </template>
      </el-dialog>
    </FaPageMain>
  </div>
</template>

  <style scoped>
  </style>
