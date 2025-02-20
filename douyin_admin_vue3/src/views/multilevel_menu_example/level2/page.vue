<route lang="yaml">
  meta:
    title: 配置管理
</route>

<script setup>
import { ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElOption, ElSelect, ElTable, ElTableColumn } from 'element-plus'
import { reactive, ref } from 'vue'

const configs = ref([
  { id: 1, configItem: '登录方式', currentConfig: 'sa-token', availableConfigs: ['sa-token', 'jwt'] },
  { id: 2, configItem: '缓存策略', currentConfig: 'redis', availableConfigs: ['redis', 'memcached'] },
])

const showDialog = ref(false)
const currentConfig = reactive({
  id: null,
  configItem: '',
  currentConfig: '',
  availableConfigs: [],
})

function addConfig() {
  showDialog.value = true
  currentConfig.id = null
  currentConfig.configItem = ''
  currentConfig.currentConfig = ''
  currentConfig.availableConfigs = []
}

function editConfig(config) {
  showDialog.value = true
  currentConfig.id = config.id
  currentConfig.configItem = config.configItem
  currentConfig.currentConfig = config.currentConfig
  currentConfig.availableConfigs = [...config.availableConfigs]
}

function deleteConfig(id) {
  const index = configs.value.findIndex(config => config.id === id)
  if (index !== -1) {
    configs.value.splice(index, 1)
  }
}

function saveConfig() {
  if (currentConfig.id) {
    const index = configs.value.findIndex(config => config.id === currentConfig.id)
    if (index !== -1) {
      configs.value[index] = { ...currentConfig }
    }
  }
  else {
    currentConfig.id = configs.value.length + 1
    configs.value.push({ ...currentConfig })
  }
  showDialog.value = false
}

function closeDialog() {
  showDialog.value = false
}
</script>

<template>
  <div>
    <!-- 操作按钮 -->
    <ElButton type="primary" @click="addConfig">
      新增配置
    </ElButton>

    <!-- 配置表格 -->
    <ElTable :data="configs" style="width: 100%">
      <ElTableColumn prop="configItem" label="配置项" width="200" />
      <ElTableColumn prop="currentConfig" label="当前配置的方式" width="200" />
      <ElTableColumn label="待选的配置方式" width="300">
        <template #default="{ row }">
          <ElSelect v-model="row.currentConfig" placeholder="选择配置">
            <ElOption
              v-for="(config, index) in row.availableConfigs"
              :key="index"
              :label="config"
              :value="config"
            />
          </ElSelect>
        </template>
      </ElTableColumn>
      <ElTableColumn label="操作" width="150">
        <template #default="{ row }">
          <div class="operation-buttons">
            <ElButton size="small" @click="editConfig(row)">
              编辑
            </ElButton>
            <ElButton size="small" type="danger" @click="deleteConfig(row.id)">
              删除
            </ElButton>
          </div>
        </template>
      </ElTableColumn>
    </ElTable>

    <!-- 配置编辑/新增对话框 -->
    <ElDialog v-model:visible="showDialog" title="配置管理" width="400px">
      <ElForm :model="currentConfig">
        <ElFormItem label="配置项" prop="configItem">
          <ElInput v-model="currentConfig.configItem" placeholder="请输入配置项名称" />
        </ElFormItem>
        <ElFormItem label="当前配置的方式" prop="currentConfig">
          <ElSelect v-model="currentConfig.currentConfig" placeholder="选择当前配置">
            <ElOption
              v-for="(config, index) in currentConfig.availableConfigs"
              :key="index"
              :label="config"
              :value="config"
            />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="待选的配置方式" prop="availableConfigs">
          <ElSelect
            v-model="currentConfig.availableConfigs"
            multiple
            placeholder="选择待选配置"
          >
            <ElOption
              v-for="(config, index) in ['sa-token', 'jwt', 'redis', 'memcached']"
              :key="index"
              :label="config"
              :value="config"
            />
          </ElSelect>
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="closeDialog">
          取消
        </ElButton>
        <ElButton type="primary" @click="saveConfig">
          保存
        </ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<style scoped>
/* 页面容器 */
div {
  margin: 20px;
}
.operation-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.operation-buttons .el-button {
  margin-right: 10px;
}

.operation-buttons .el-button:last-child {
  margin-right: 0;
}

/* 操作按钮 */
.ElButton {
  margin-bottom: 20px;
}

/* 表格 */
.el-table {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

/* 表格列头 */
.el-table th {
  background-color: #f5f7fa;
  color: #333;
}

/* 表格数据 */
.el-table td {
  color: #555;
}

/* 表格内操作按钮 */
.el-table .el-button {
  margin-right: 10px;
}

/* 编辑/新增对话框 */
.el-dialog {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.el-dialog__header {
  background-color: #409EFF;
  color: white;
}

/* 表单元素 */
.el-form-item {
  margin-bottom: 20px;
}

.el-form-item label {
  font-weight: bold;
}

/* 选择框和输入框的宽度调整 */
.el-select,
.el-input {
  width: 100%;
}

.el-button.primary {
  background-color: #409EFF;
  border-color: #409EFF;
}

.el-button.primary:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

.el-button[type='danger'] {
  background-color: #f56c6c;
  border-color: #f56c6c;
}

.el-button[type='danger']:hover {
  background-color: #f79b9b;
  border-color: #f79b9b;
}

/* 表格的列宽自适应 */
.el-table-column {
  text-align: center;
}

/* 让按钮有更好的间距 */
.el-button + .el-button {
  margin-left: 10px;
}

/* 表格行的背景色交替效果 */
.el-table__row:nth-child(odd) {
  background-color: #f9f9f9;
}

.el-table__row:nth-child(even) {
  background-color: #ffffff;
}
</style>
