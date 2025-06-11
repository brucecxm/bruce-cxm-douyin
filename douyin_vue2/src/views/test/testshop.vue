<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-input
          v-model="searchId"
          placeholder="输入好友ID搜索"
          @keyup.enter.native="searchFriend"
        />
      </el-col>
      <el-col :span="3">
        <el-button type="primary" @click="searchFriend">搜索</el-button>
      </el-col>
      <el-col :span="3">
        <el-button type="success" @click="openAddDialog">添加好友</el-button>
      </el-col>
    </el-row>

    <!-- 好友列表 -->
    <el-table :data="friendList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="100" />
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="friendId" label="好友ID" />
      <el-table-column label="操作" width="200">
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

    <!-- 添加好友弹窗 -->
    <el-dialog title="添加好友" :visible.sync="addDialogVisible">
      <el-form :model="addForm">
        <el-form-item label="好友ID" required>
          <el-input v-model="addForm.toUserId"></el-input>
        </el-form-item>
        <el-form-item label="验证消息">
          <el-input v-model="addForm.verifyMsg"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="sendRequest">发送请求</el-button>
      </div>
    </el-dialog>

    <!-- 好友请求 -->
    <h3 style="margin-top: 30px">收到的好友请求</h3>
    <el-table :data="requests">
      <el-table-column prop="fromUserId" label="来自用户ID" />
      <el-table-column prop="verifyMsg" label="验证消息" />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            @click="handleRequest(scope.row.requestId, 'accept')"
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
