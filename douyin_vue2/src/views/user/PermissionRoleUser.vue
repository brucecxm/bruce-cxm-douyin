<template>
  <div class="manage-wrapper">
    <!-- 角色管理 -->
    <el-card class="box-card" style="margin-bottom: 20px">
      <div slot="header" class="clearfix">
        <span>角色管理</span>
        <el-button style="float: right" type="primary" @click="openRoleDialog()"
          >新增角色</el-button
        >
      </div>
      <el-table :data="roleList" style="width: 100%">
        <el-table-column prop="roleName" label="角色名称"></el-table-column>
        <el-table-column prop="roleCode" label="角色编码"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="editRole(scope.row)">编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="deleteRole(scope.row.id)"
              >删除</el-button
            >
            <el-button
              size="mini"
              type="success"
              @click="assignPermission(scope.row)"
              >分配权限</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 权限管理 -->
    <el-card class="box-card" style="margin-bottom: 20px">
      <div slot="header" class="clearfix">
        <span>权限管理</span>
        <el-button
          style="float: right"
          type="primary"
          @click="openPermissionDialog()"
          >新增权限</el-button
        >
      </div>
      <el-table :data="permissionList" style="width: 100%">
        <el-table-column
          prop="permissionName"
          label="权限名称"
        ></el-table-column>
        <el-table-column
          prop="permissionCode"
          label="权限编码"
        ></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="editPermission(scope.row)"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="deletePermission(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 用户角色绑定 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>用户角色绑定</span>
      </div>
      <el-form inline>
        <el-form-item label="用户ID">
          <el-input
            v-model="userRoleDTO.userId"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item label="选择角色">
          <el-select
            v-model="userRoleDTO.roleIds"
            multiple
            placeholder="请选择"
          >
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.roleName"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
        <el-button type="primary" @click="bindUserRoles">提交绑定</el-button>
      </el-form>
    </el-card>

    <!-- 角色权限绑定 -->
    <el-card class="box-card" style="margin-top: 20px">
      <div slot="header" class="clearfix">
        <span>角色权限绑定</span>
      </div>
      <el-form inline>
        <el-form-item label="角色ID">
          <el-input
            v-model="rolePermissionDTO.roleId"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item label="选择权限">
          <el-select
            v-model="rolePermissionDTO.permissionIds"
            multiple
            placeholder="请选择"
          >
            <el-option
              v-for="perm in permissionList"
              :key="perm.id"
              :label="perm.permissionName"
              :value="perm.id"
            />
          </el-select>
        </el-form-item>
        <el-button type="primary" @click="bindRolePermissions"
          >提交绑定</el-button
        >
      </el-form>
    </el-card>

    <!-- 弹窗 - 角色 -->
    <el-dialog
      :title="roleForm.id ? '编辑角色' : '新增角色'"
      :visible.sync="roleDialogVisible"
    >
      <el-form :model="roleForm" label-width="100px">
        <el-form-item label="角色名称">
          <el-input v-model="roleForm.roleName"></el-input>
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="roleForm.roleCode"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="roleForm.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRole">保存</el-button>
      </div>
    </el-dialog>

    <!-- 弹窗 - 权限 -->
    <el-dialog
      :title="permissionForm.id ? '编辑权限' : '新增权限'"
      :visible.sync="permissionDialogVisible"
    >
      <el-form :model="permissionForm" label-width="100px">
        <el-form-item label="权限名称">
          <el-input v-model="permissionForm.permissionName"></el-input>
        </el-form-item>
        <el-form-item label="权限编码">
          <el-input v-model="permissionForm.permissionCode"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="permissionForm.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePermission">保存</el-button>
      </div>
    </el-dialog>

    <!-- 弹窗 - 分配权限 -->
    <el-dialog title="分配权限" :visible.sync="assignDialogVisible">
      <el-checkbox-group v-model="rolePermissionDTO.permissionIds">
        <el-checkbox
          v-for="perm in permissionList"
          :key="perm.id"
          :label="perm.id"
        >
          {{ perm.permissionName }}
        </el-checkbox>
      </el-checkbox-group>
      <div slot="footer">
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="bindRolePermissions">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      roleList: [], // 角色列表数据
      permissionList: [], // 权限列表数据
      userRoleDTO: { userId: '', roleIds: [] }, // 用户角色绑定数据
      rolePermissionDTO: {
        roleId: '',
        permissionIds: []
      },
      permissionRoleDTO: { permissionId: '', roleIds: [] }, // 权限角色绑定数据

      roleForm: { id: null, roleName: '', roleCode: '', description: '' }, // 角色表单数据
      permissionForm: {
        id: null,
        permissionName: '',
        permissionCode: '',
        description: ''
      }, // 权限表单数据
      roleDialogVisible: false, // 角色弹窗显示状态
      permissionDialogVisible: false, // 权限弹窗显示状态
      assignDialogVisible: false // 分配权限弹窗显示状态
    };
  },
  mounted() {
    // 页面加载时获取角色和权限列表
    this.loadRoles();
    this.loadPermissions();
  },
  methods: {
    // 角色相关方法
    loadRoles() {
      axios
        .get('/api/user/role/list')
        .then((res) => {
          // 后端数据在 res.data.data 中（关键修改）
          this.roleList = res.data.data || [];
          console.log('角色列表数据:', this.roleList); // 调试用，可删除
        })
        .catch((err) => {
          console.error('加载角色失败:', err);
        });
    },
    openRoleDialog() {
      // 重置角色表单，打开弹窗
      this.roleForm = { id: null, roleName: '', roleCode: '', description: '' };
      this.roleDialogVisible = true;
    },
    editRole(role) {
      // 编辑角色：复制当前角色数据到表单，打开弹窗
      this.roleForm = { ...role };
      this.roleDialogVisible = true;
    },
    saveRole() {
      // 保存角色：新增或更新
      const url = this.roleForm.id
        ? '/api/user/role/update'
        : '/api/user/role/create';
      axios
        .post(url, this.roleForm)
        .then(() => {
          this.roleDialogVisible = false; // 关闭弹窗
          this.loadRoles(); // 重新加载角色列表
        })
        .catch((err) => {
          console.error('保存角色失败:', err);
        });
    },
    deleteRole(id) {
      // 删除角色
      axios
        .delete(`/api/user/role/delete/${id}`)
        .then(() => {
          this.loadRoles(); // 重新加载角色列表
        })
        .catch((err) => {
          console.error('删除角色失败:', err);
        });
    },

    // 权限相关方法
    loadPermissions() {
      axios
        .get('/api/user/permission/list')
        .then((res) => {
          // 后端数据在 res.data.data 中（关键修改）
          this.permissionList = res.data.data || [];
          console.log('权限列表数据:', this.permissionList); // 调试用，可删除
        })
        .catch((err) => {
          console.error('加载权限失败:', err);
        });
    },
    bindPermissionRoles() {
      axios
        .post('/api/user/role/assignPermissions', this.permissionRoleDTO)
        .then(() => {
          this.$message.success('绑定成功');
          this.permissionRoleDTO = { permissionId: '', roleIds: [] }; // 重置表单
        })
        .catch((err) => {
          console.error('权限角色绑定失败:', err);
          this.$message.error('绑定失败');
        });
    },

    openPermissionDialog() {
      // 重置权限表单，打开弹窗
      this.permissionForm = {
        id: null,
        permissionName: '',
        permissionCode: '',
        description: ''
      };
      this.permissionDialogVisible = true;
    },
    editPermission(permission) {
      // 编辑权限：复制当前权限数据到表单，打开弹窗
      this.permissionForm = { ...permission };
      this.permissionDialogVisible = true;
    },
    savePermission() {
      // 保存权限：新增或更新
      const url = this.permissionForm.id
        ? '/api/user/permission/update'
        : '/api/user/permission/create';
      axios
        .post(url, this.permissionForm)
        .then(() => {
          this.permissionDialogVisible = false; // 关闭弹窗
          this.loadPermissions(); // 重新加载权限列表
        })
        .catch((err) => {
          console.error('保存权限失败:', err);
        });
    },
    deletePermission(id) {
      // 删除权限
      axios
        .delete(`/api/user/permission/delete/${id}`)
        .then(() => {
          this.loadPermissions(); // 重新加载权限列表
        })
        .catch((err) => {
          console.error('删除权限失败:', err);
        });
    },

    // 用户角色绑定
    bindUserRoles() {
      axios
        .post('/api/user/assignRoles', this.userRoleDTO)
        .then(() => {
          this.$message.success('绑定成功');
          this.userRoleDTO = { userId: '', roleIds: [] }; // 重置表单
        })
        .catch((err) => {
          console.error('用户角色绑定失败:', err);
          this.$message.error('绑定失败');
        });
    },

    // 角色权限绑定
    assignPermission(role) {
      // 打开分配权限弹窗前，获取该角色当前的权限
      this.rolePermissionDTO.roleId = role.id;
      axios
        .get(`/api/user/role-permission/${role.id}`)
        .then((res) => {
          // 假设角色已有权限也在 res.data.data 中（根据后端格式调整）
          this.rolePermissionDTO.permissionIds = res.data.data || [];
          this.assignDialogVisible = true;
        })
        .catch((err) => {
          console.error('获取角色权限失败:', err);
        });
    },

    bindRolePermissions() {
      if (
        !this.rolePermissionDTO.roleId ||
        this.rolePermissionDTO.permissionIds.length === 0
      ) {
        this.$message.warning('请填写角色ID并至少选择一个权限');
        return;
      }
      axios
        .post('/api/user/role/assignPermissions', this.rolePermissionDTO)
        .then(() => {
          this.$message.success('绑定成功');
          this.rolePermissionDTO = { roleId: '', permissionIds: [] }; // 重置表单
        })
        .catch((err) => {
          console.error('角色权限绑定失败:', err);
          this.$message.error('绑定失败');
        });
    }
  }
};
</script>

<style scoped>
.manage-wrapper {
  padding: 20px;
}
</style>
