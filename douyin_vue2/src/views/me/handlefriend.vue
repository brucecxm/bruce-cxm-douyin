<template>
  <div class="friend-container">
    <!-- 搜索与添加 -->
    <el-row
      :gutter="10"
      type="flex"
      justify="start"
      style="flex-wrap: wrap; margin-bottom: 15px"
    >
      <el-col :xs="24" style="margin-bottom: 10px">
        <el-input
          v-model="searchId"
          placeholder="🔍 输入好友ID搜索"
          clearable
          prefix-icon="el-icon-search"
          @keyup.enter.native="searchFriend"
        />
      </el-col>
      <el-col :xs="12" style="margin-bottom: 10px">
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="searchFriend"
          class="full-button"
        >
          搜索
        </el-button>
      </el-col>
      <el-col :xs="12" style="margin-bottom: 10px">
        <el-button
          type="success"
          icon="el-icon-plus"
          @click="openAddDialog"
          class="full-button"
        >
          添加好友
        </el-button>
      </el-col>
    </el-row>

    <!-- 好友列表 -->
    <el-card shadow="hover" class="card-box">
      <div slot="header" class="card-header">👥 我的好友</div>
      <el-table :data="friendList" size="small" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="userId" label="用户ID" />
        <el-table-column prop="friendId" label="好友ID" />
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="deleteFriend(scope.row.id)"
            >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加好友弹窗 -->
    <el-dialog title="添加好友" :visible.sync="addDialogVisible" width="90%">
      <el-form :model="addForm" label-position="top">
        <el-form-item label="好友ID" required>
          <el-input v-model="addForm.toUserId" placeholder="请输入好友ID" />
        </el-form-item>
        <el-form-item label="验证消息">
          <el-input v-model="addForm.verifyMsg" placeholder="验证信息 (可选)" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="sendRequest">发送请求</el-button>
      </div>
    </el-dialog>

    <!-- 好友请求 -->
    <el-card shadow="hover" class="card-box" style="margin-top: 20px">
      <div slot="header" class="card-header">📩 收到的好友请求</div>
      <el-table :data="requests" size="small" border stripe>
        <el-table-column prop="fromUserId" label="来自用户ID" />
        <el-table-column prop="verifyMsg" label="验证消息" />
        <el-table-column label="操作" width="140">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="mini"
              @click="handleRequest(scope.row.requestId, 'accept')"
              style="margin-right: 5px"
            >同意</el-button
            >
            <el-button
              type="warning"
              size="mini"
              @click="handleRequest(scope.row.requestId, 'reject')"
            >拒绝</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import axios from '@/utils/request';

export default {
  data() {
    return {
      friendList: [],
      requests: [],
      searchId: '',
      addDialogVisible: false,
      addForm: {
        toUserId: '',
        verifyMsg: ''
      }
    };
  },
  mounted() {
    this.loadFriends();
    this.loadRequests();
  },
  methods: {
    loadFriends() {
      axios.get('/user/friend/list').then((res) => {
        this.friendList = res.data;
      });
    },
    loadRequests() {
      axios.get('/user/friend/requests').then((res) => {
        this.requests = res.data;
      });
    },
    searchFriend() {
      axios
        .get('/user/friend/search', {
          params: { friendId: this.searchId }
        })
        .then((res) => {
          this.friendList = res.data.data || [];
        });
    },
    openAddDialog() {
      this.addDialogVisible = true;
    },
    sendRequest() {
      axios.post('/user/friend/request', this.addForm).then(() => {
        this.$message.success('请求已发送');
        this.addDialogVisible = false;
      });
    },
    deleteFriend(id) {
      axios.delete(`/user/friend/${id}`).then(() => {
        this.$message.success('已删除');
        this.loadFriends();
      });
    },
    handleRequest(requestId, action) {
      axios
        .post('/user/friend/handle', {
          requestId,
          action
        })
        .then(() => {
          this.$message.success('操作成功');
          this.loadRequests();
          this.loadFriends();
        });
    }
  }
};
</script>

<style scoped>
.friend-container {
  padding: 15px;
  background-color: #f9f9f9;
}

.card-box {
  margin-bottom: 20px;
  border-radius: 10px;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
  color: #333;
}

.full-button {
  width: 100%;
}

.dialog-footer {
  text-align: right;
}
</style>
